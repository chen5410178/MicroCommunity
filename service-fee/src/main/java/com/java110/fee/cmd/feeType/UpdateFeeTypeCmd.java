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
package com.java110.fee.cmd.feeType;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.feeType.FeeTypeDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.fee.IFeeTypeLogV1InnerServiceSMO;
import com.java110.intf.fee.IFeeTypeV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.feeType.FeeTypePo;
import com.java110.po.feeTypeLog.FeeTypeLogPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * 类表述：更新
 * 服务编码：feeType.updateFeeType
 * 请求路劲：/app/feeType.UpdateFeeType
 * add by 吴学文 at 2025-06-05 18:00:01 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "feeType.updateFeeType")
public class UpdateFeeTypeCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(UpdateFeeTypeCmd.class);


    @Autowired
    private IFeeTypeV1InnerServiceSMO feeTypeV1InnerServiceSMOImpl;

    @Autowired
    private IFeeTypeLogV1InnerServiceSMO feeTypeLogV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "ftId", "ftId不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        FeeTypeDto feeTypeDto = new FeeTypeDto();
        feeTypeDto.setFtId(reqJson.getString("ftId"));
        List<FeeTypeDto> feeTypeDtos = feeTypeV1InnerServiceSMOImpl.queryFeeTypes(feeTypeDto);
        if (ListUtil.isNull(feeTypeDtos)) {
            throw new CmdException("未包含费用类型");
        }

        FeeTypePo feeTypePo = BeanConvertUtil.covertBean(reqJson, FeeTypePo.class);
        int flag = feeTypeV1InnerServiceSMOImpl.updateFeeType(feeTypePo);

        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }
        if (StringUtil.isEmpty(feeTypePo.getMonthAmount()) || feeTypeDtos.get(0).getMonthAmount().equals(feeTypePo.getMonthAmount())) {
            return;
        }

        String userId = CmdContextUtils.getUserId(cmdDataFlowContext);
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        if (ListUtil.isNull(userDtos)) {
            return;
        }

        FeeTypeLogPo feeTypeLogPo = new FeeTypeLogPo();
        feeTypeLogPo.setOrgMonthAmount(feeTypeDtos.get(0).getMonthAmount());
        feeTypeLogPo.setMonthAmount(feeTypePo.getMonthAmount());
        feeTypeLogPo.setStaffName(userDtos.get(0).getName());
        feeTypeLogPo.setFtId(feeTypeDtos.get(0).getFtId());
        feeTypeLogPo.setLogId(GenerateCodeFactory.getGeneratorId("11"));
        feeTypeLogPo.setFtName(feeTypeDtos.get(0).getFtName());
        feeTypeLogPo.setCommunityId(feeTypeDtos.get(0).getCommunityId());

        feeTypeLogV1InnerServiceSMOImpl.saveFeeTypeLog(feeTypeLogPo);

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
