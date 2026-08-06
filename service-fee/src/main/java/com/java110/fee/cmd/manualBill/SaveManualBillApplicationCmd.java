package com.java110.fee.cmd.manualBill;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.payFee.PayFeeRuleDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.fee.IManualBillGenerationV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeConfigV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeRuleV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.fee.ManualBillGenerationDetailPo;
import com.java110.po.fee.ManualBillGenerationPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Java110Cmd(serviceCode = "fee.saveManualBillApplication")
public class SaveManualBillApplicationCmd extends Cmd {

    @Autowired
    private IManualBillGenerationV1InnerServiceSMO manualBillGenerationV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeConfigV1InnerServiceSMO payFeeConfigV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeRuleV1InnerServiceSMO payFeeRuleV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
        Assert.hasKeyAndValue(reqJson, "configId", "未包含费用项ID");
        Assert.hasKeyAndValue(reqJson, "billMonth", "未包含目标账单月份");
        Assert.hasKey(reqJson, "details", "未包含明细数据");
        reqJson.remove("userId");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        String userId = CmdContextUtils.getUserId(context);

        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户未登录");

        String communityId = reqJson.getString("communityId");
        String configId = reqJson.getString("configId");
        String billMonth = reqJson.getString("billMonth");
        String remark = reqJson.getString("remark");

        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setConfigId(configId);
        feeConfigDto.setCommunityId(communityId);
        List<FeeConfigDto> feeConfigDtos = payFeeConfigV1InnerServiceSMOImpl.queryPayFeeConfigs(feeConfigDto);
        Assert.listOnlyOne(feeConfigDtos, "费用项不存在");

        String feeName = feeConfigDtos.get(0).getFeeName();
        String applicationId = GenerateCodeFactory.getGeneratorId("16");

        JSONArray details = reqJson.getJSONArray("details");
        Assert.isNotNull(details, "明细数据不能为空");

        ManualBillGenerationPo applicationPo = new ManualBillGenerationPo();
        applicationPo.setApplicationId(applicationId);
        applicationPo.setCommunityId(communityId);
        applicationPo.setApplicationName(feeName + "-" + billMonth + "账单手工生成");
        applicationPo.setBillMonth(billMonth);
        applicationPo.setConfigId(configId);
        applicationPo.setFeeName(feeName);
        applicationPo.setState("1001");
        applicationPo.setTotalCount(details.size());
        applicationPo.setSuccessCount(0);
        applicationPo.setErrorCount(0);
        applicationPo.setRemark(remark);
        applicationPo.setCreateUserId(userDtos.get(0).getUserId());
        applicationPo.setCreateUserName(userDtos.get(0).getName());
        applicationPo.setCreateTime(new Date());
        applicationPo.setStatusCd("0");

        manualBillGenerationV1InnerServiceSMOImpl.saveManualBillGeneration(applicationPo);

        List<ManualBillGenerationDetailPo> detailPos = new ArrayList<>();
        for (int i = 0; i < details.size(); i++) {
            JSONObject detail = details.getJSONObject(i);
            ManualBillGenerationDetailPo detailPo = new ManualBillGenerationDetailPo();
            detailPo.setDetailId(GenerateCodeFactory.getGeneratorId("17"));
            detailPo.setApplicationId(applicationId);
            detailPo.setCommunityId(communityId);
            detailPo.setConfigId(detail.getString("configId"));
            detailPo.setFeeName(detail.getString("feeName"));
            detailPo.setRuleId(detail.getString("ruleId"));
            detailPo.setRoomId(detail.getString("roomId"));
            detailPo.setRoomName(detail.getString("roomName"));
            detailPo.setOwnerId(detail.getString("ownerId"));
            detailPo.setOwnerName(detail.getString("ownerName"));
            detailPo.setFeeTypeCd(detail.getString("feeTypeCd"));
            detailPo.setFeeFlag(detail.getString("feeFlag"));
            detailPo.setCurYearMonth(detail.getString("curYearMonth"));
            detailPo.setPrepaymentPeriod(detail.getIntValue("prepaymentPeriod"));
            detailPo.setAmount(detail.getString("amount"));
            detailPo.setState("1001");
            detailPo.setCreateTime(new Date());
            detailPo.setStatusCd("0");
            detailPos.add(detailPo);
        }

        manualBillGenerationV1InnerServiceSMOImpl.saveManualBillGenerationDetails(detailPos);

        context.setResponseEntity(ResultVo.success());
    }
}
