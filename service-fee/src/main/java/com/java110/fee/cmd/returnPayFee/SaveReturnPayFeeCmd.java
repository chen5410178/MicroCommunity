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
package com.java110.fee.cmd.returnPayFee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.payFee.ReturnPayFeeDto;
import com.java110.dto.payFeeOrder.PayFeeOrderDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.fee.*;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.refundFeeDetail.RefundFeeDetailPo;
import com.java110.po.refundFeeOrder.RefundFeeOrderPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 类表述：保存
 * 服务编码：returnPayFee.saveReturnPayFee
 * 请求路劲：/app/returnPayFee.SaveReturnPayFee
 * add by 吴学文 at 2022-02-21 12:20:03 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "returnPayFee.saveReturnPayFee")
public class SaveReturnPayFeeCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveReturnPayFeeCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IReturnPayFeeV1InnerServiceSMO returnPayFeeV1InnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IPayFeeDetailV1InnerServiceSMO payFeeDetailNewV1InnerServiceSMOImpl;


    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeOrderV1InnerServiceSMO payFeeOrderV1InnerServiceSMOImpl;

    @Autowired
    private IRefundFeeOrderV1InnerServiceSMO refundFeeOrderV1InnerServiceSMOImpl;

    @Autowired
    private IRefundFeeDetailV1InnerServiceSMO refundFeeDetailV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "configId", "请求报文中未包含configId");
        Assert.hasKeyAndValue(reqJson, "payOrderId", "请求报文中未包含缴费订单");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "reason", "请求报文中未包含reason");

        if (!reqJson.containsKey("selectDetailIds")) {
            throw new CmdException("未包含退费明细");
        }

        JSONArray selectDetailIds = reqJson.getJSONArray("selectDetailIds");
        if (ListUtil.isNull(selectDetailIds)) {
            throw new CmdException("未包含退费明细");
        }
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        String userId = cmdDataFlowContext.getReqHeaders().get("user-id");

        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户不存在");

        PayFeeOrderDto payFeeOrderDto = new PayFeeOrderDto();
        payFeeOrderDto.setPayOrderId(reqJson.getString("payOrderId"));
        List<PayFeeOrderDto> payFeeOrderDtos = payFeeOrderV1InnerServiceSMOImpl.queryPayFeeOrders(payFeeOrderDto);
        Assert.listOnlyOne(payFeeOrderDtos, "缴费订单不存在");

        RefundFeeOrderPo refundFeeOrderPo = new RefundFeeOrderPo();
        refundFeeOrderPo.setOrderType(payFeeOrderDtos.get(0).getOrderType());
        refundFeeOrderPo.setReason(reqJson.getString("reason"));
        refundFeeOrderPo.setPayTime(DateUtil.getFormatTimeStringA(payFeeOrderDtos.get(0).getCreateTime()));
        refundFeeOrderPo.setApplyPersonName(userDtos.get(0).getName());
        refundFeeOrderPo.setRemark(payFeeOrderDtos.get(0).getRemark());
        refundFeeOrderPo.setOwnerId(payFeeOrderDtos.get(0).getOwnerId());
        refundFeeOrderPo.setRefundOrderId(GenerateCodeFactory.getGeneratorId("11"));
        refundFeeOrderPo.setPayerObjName(payFeeOrderDtos.get(0).getPayerObjName());
        refundFeeOrderPo.setOwnerName(payFeeOrderDtos.get(0).getOwnerName());
        refundFeeOrderPo.setAuditPersonName("");
        refundFeeOrderPo.setAuditPersonId("");
        refundFeeOrderPo.setApplyPersonId(userId);
        refundFeeOrderPo.setState(ReturnPayFeeDto.STATE_WAIT);
        refundFeeOrderPo.setCommunityId(payFeeOrderDtos.get(0).getCommunityId());
        refundFeeOrderPo.setPayerObjType(payFeeOrderDtos.get(0).getPayerObjType());
        refundFeeOrderPo.setPayerObjId(payFeeOrderDtos.get(0).getPayerObjId());
        refundFeeOrderPo.setPayOrderId(payFeeOrderDtos.get(0).getPayOrderId());
//        if(!StringUtil.isEmpty(payFeeOrderDtos.get(0).getExtOrderId())) {
//            refundFeeOrderPo.setPayOrderId(payFeeOrderDtos.get(0).getExtOrderId());
//        }


        JSONArray selectDetailIds = reqJson.getJSONArray("selectDetailIds");
        int flag = 0;
        PayFeeDetailPo payFeeDetailPo = null;
        RefundFeeDetailPo refundFeeDetailPo = null;
        for(int detailIndex = 0; detailIndex < selectDetailIds.size() ; detailIndex++){
            FeeDetailDto feeDetailDto = new FeeDetailDto();
            feeDetailDto.setDetailId(selectDetailIds.getString(detailIndex));
            List<FeeDetailDto> feeDetailDtos = feeDetailInnerServiceSMOImpl.queryFeeDetails(feeDetailDto);
            Assert.listOnlyOne(feeDetailDtos, "未找到需要修改的活动 或多条数据");
            payFeeDetailPo = new PayFeeDetailPo();
            payFeeDetailPo.setDetailId(feeDetailDtos.get(0).getDetailId());
            payFeeDetailPo.setState(FeeDetailDto.STATE_RETURNING);
            flag = payFeeDetailNewV1InnerServiceSMOImpl.updatePayFeeDetailNew(payFeeDetailPo);
            if (flag < 1) {
                throw new CmdException("保存数据失败");
            }
            refundFeeDetailPo = new RefundFeeDetailPo();
            refundFeeDetailPo.setRefundOrderId(refundFeeOrderPo.getRefundOrderId());
            refundFeeDetailPo.setFeeName(feeDetailDtos.get(0).getFeeName());
            refundFeeDetailPo.setDetailId(feeDetailDtos.get(0).getDetailId());
            refundFeeDetailPo.setStartTime(DateUtil.getFormatTimeStringA(feeDetailDtos.get(0).getStartTime()));
            refundFeeDetailPo.setRemark(feeDetailDtos.get(0).getRemark());
            refundFeeDetailPo.setEndTime(DateUtil.getFormatTimeStringA(feeDetailDtos.get(0).getEndTime()));
            refundFeeDetailPo.setReceivedAmount(feeDetailDtos.get(0).getReceivedAmount());
            refundFeeDetailPo.setCommunityId(feeDetailDtos.get(0).getCommunityId());
            refundFeeDetailPo.setRfdId(GenerateCodeFactory.getGeneratorId("11"));
            refundFeeDetailPo.setFeeId(feeDetailDtos.get(0).getFeeId());
            refundFeeDetailV1InnerServiceSMOImpl.saveRefundFeeDetail(refundFeeDetailPo);
        }

        refundFeeOrderV1InnerServiceSMOImpl.saveRefundFeeOrder(refundFeeOrderPo);


        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
