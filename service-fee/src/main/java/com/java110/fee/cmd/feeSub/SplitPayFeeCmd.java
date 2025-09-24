/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.fee.cmd.feeSub;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.payFee.PayFeeSubDto;
import com.java110.intf.fee.*;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.payFee.PayFeeSubPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 类表述：拆分费用
 * 服务编码：payFeeSub.savePayFeeSub
 * 请求路劲：/app/payFeeSub.SavePayFeeSub
 * add by 吴学文 at 2024-01-05 12:10:47 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "feeSub.splitPayFee")
public class SplitPayFeeCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SplitPayFeeCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IPayFeeSubV1InnerServiceSMO payFeeSubV1InnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "feeId", "请求报文中未包含feeId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKey(reqJson, "subFees", "未包含拆分费用");
        JSONArray subFees = reqJson.getJSONArray("subFees");
        if (ListUtil.isNull(subFees)) {
            throw new CmdException("未包含拆分费用");
        }

        //todo 判断 费用是不是 间接性费用或者是一次性费用
        FeeDto feeDto = new FeeDto();
        feeDto.setFeeId(reqJson.getString("feeId"));
        feeDto.setCommunityId(reqJson.getString("communityId"));

        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        Assert.listOnlyOne(feeDtos, "费用不存在");

        if (!FeeDto.STATE_DOING.equals(feeDtos.get(0).getState())) {
            throw new CmdException("费用已结束不能拆分");
        }

        PayFeeSubDto payFeeSubDto = new PayFeeSubDto();
        payFeeSubDto.setFeeId(reqJson.getString("feeId"));
        List<PayFeeSubDto> payFeeSubDtos = payFeeSubV1InnerServiceSMOImpl.queryPayFeeSubs(payFeeSubDto);
        if (!ListUtil.isNull(payFeeSubDtos)) {
            throw new CmdException("拆分费用不能再拆分");
        }

        reqJson.put("feeDto", feeDtos.get(0));
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        FeeDto feeDto = (FeeDto) reqJson.get("feeDto");


        JSONArray subFees = reqJson.getJSONArray("subFees");
        JSONObject subFee = null;
        for(int subIndex  = 0;subIndex < subFees.size();subIndex++){
            subFee = subFees.getJSONObject(subIndex);
            saveSubFee(feeDto, subFee);
        }

        //todo 结束原费用

        PayFeePo payFeePo = new PayFeePo();
        payFeePo.setFeeId(feeDto.getFeeId());
        payFeePo.setCommunityId(feeDto.getCommunityId());
        int flag = payFeeV1InnerServiceSMOImpl.deletePayFee(payFeePo);

        if (flag < 1) {
            throw new CmdException("结束费用失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }

    /**
     * 拆分费用
     *
     * @param feeDto  费用对象
     * @param subFee
     */
    private void saveSubFee(FeeDto feeDto, JSONObject subFee) {

        PayFeeSubPo payFeeSubPo = new PayFeeSubPo();
        payFeeSubPo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
        payFeeSubPo.setPreFeeId(feeDto.getFeeId());
        payFeeSubPo.setCommunityId(feeDto.getCommunityId());
        int flag = payFeeSubV1InnerServiceSMOImpl.savePayFeeSub(payFeeSubPo);
        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        PayFeePo payFeePo = BeanConvertUtil.covertBean(feeDto, PayFeePo.class);
        payFeePo.setReceivableAmount(subFee.getString("oweAmount"));
        payFeePo.setOweAmount(subFee.getString("oweAmount"));
        payFeePo.setRemark(payFeePo.getRemark()+"\n"+subFee.getString("remark"));
        payFeePo.setFeeId(payFeeSubPo.getFeeId());
        payFeePo.setState(FeeDto.STATE_DOING);
        payFeePo.setAmount("0");// 这个字段没有用了

        flag = payFeeV1InnerServiceSMOImpl.savePayFee(payFeePo);
        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        List<FeeAttrDto> feeAttrDtos = feeDto.getFeeAttrDtos();

        if (ListUtil.isNull(feeAttrDtos)) {
            return;
        }
        List<FeeAttrPo> feeAttrPos = new ArrayList<>();
        FeeAttrPo feeAttrPo = null;
        for (FeeAttrDto feeAttrDto : feeAttrDtos) {
            feeAttrPo = BeanConvertUtil.covertBean(feeAttrDto, FeeAttrPo.class);
            feeAttrPo.setFeeId(payFeePo.getFeeId());
            feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
            feeAttrPos.add(feeAttrPo);
        }


        feeAttrInnerServiceSMOImpl.saveFeeAttrs(feeAttrPos);
    }
}
