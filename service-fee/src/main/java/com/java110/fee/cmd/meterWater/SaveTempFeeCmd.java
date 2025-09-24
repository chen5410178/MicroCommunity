package com.java110.fee.cmd.meterWater;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.payFee.PayFeeBatchDto;
import com.java110.dto.user.UserDto;
import com.java110.fee.bill.ICycleConvertOnceFee;
import com.java110.intf.fee.IPayFeeBatchV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeRuleV1InnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.payFee.PayFeeBatchPo;
import com.java110.po.payFee.PayFeeRulePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "meterWater.saveTempFee")
public class SaveTempFeeCmd extends Cmd {

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IPayFeeBatchV1InnerServiceSMO payFeeBatchV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeRuleV1InnerServiceSMO payFeeRuleV1InnerServiceSMOImpl;

    @Autowired
    private ICycleConvertOnceFee cycleConvertOnceFeeImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "feeTypeCd", "请求报文中未包含费用类型");
        Assert.hasKeyAndValue(reqJson, "configId", "请求报文中未包含费用项");
        Assert.hasKeyAndValue(reqJson, "objType", "请求报文中未包含objType");
        Assert.hasKeyAndValue(reqJson, "objId", "请求报文中未包含objId");
        Assert.hasKeyAndValue(reqJson, "amount", "请求报文中未包含amount");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含开始时间");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含结束时间");
        String endTime = reqJson.getString("endTime");
        if (!endTime.contains(":")) {
            endTime += " 23:59:59";
            reqJson.put("endTime", endTime);
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String userId = CmdContextUtils.getUserId(context);
        String storeId = CmdContextUtils.getStoreId(context);
        //生成批次
        generatorBatch(reqJson,userId);
        List<PayFeeRulePo> payFeeRulePos = new ArrayList<>();

        PayFeeRulePo payFeeRulePo =new PayFeeRulePo();
        payFeeRulePo.setMaxTime(reqJson.getString("endTime"));
        payFeeRulePo.setAmount(reqJson.getString("amount"));
        payFeeRulePo.setFeeTypeCd(reqJson.getString("feeTypeCd"));
        payFeeRulePo.setCurYearMonth(reqJson.getString("startTime"));
        payFeeRulePo.setBatchId(reqJson.getString("batchId"));
        payFeeRulePo.setUserId(userId);
        payFeeRulePo.setIncomeObjId(storeId);
        payFeeRulePo.setConfigId(reqJson.getString("configId"));
        payFeeRulePo.setFeeFlag(FeeDto.FEE_FLAG_ONCE);
        payFeeRulePo.setStartTime(reqJson.getString("startTime"));
        payFeeRulePo.setEndTime(reqJson.getString("startTime"));
        payFeeRulePo.setState(FeeDto.STATE_DOING);
        payFeeRulePo.setRuleId(GenerateCodeFactory.getGeneratorId("11"));
        payFeeRulePo.setCommunityId(reqJson.getString("communityId"));
        payFeeRulePo.setPayerObjType(reqJson.getString("objType"));
        payFeeRulePo.setPayerObjId(reqJson.getString("objId"));
        payFeeRulePos.add(payFeeRulePo);

        //todo 设置费用
        payFeeRuleV1InnerServiceSMOImpl.savePayFeeRules(payFeeRulePos);


        //todo 走账单模式
        cycleConvertOnceFeeImpl.rulesGeneratePayFees(payFeeRulePos);

        context.setResponseEntity(ResultVo.success());

    }

    /**
     * 生成批次号
     *
     * @param reqJson
     */
    private void generatorBatch(JSONObject reqJson,String userId) {
        PayFeeBatchPo payFeeBatchPo = new PayFeeBatchPo();
        payFeeBatchPo.setBatchId(GenerateCodeFactory.getGeneratorId("12"));
        payFeeBatchPo.setCommunityId(reqJson.getString("communityId"));
        payFeeBatchPo.setCreateUserId(reqJson.getString("userId"));
        UserDto userDto = new UserDto();
        userDto.setUserId(reqJson.getString("userId"));
        List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户不存在");
        payFeeBatchPo.setCreateUserName(userDtos.get(0).getUserName());
        payFeeBatchPo.setState(PayFeeBatchDto.STATE_NORMAL);
        payFeeBatchPo.setMsg("正常");
        int flag = payFeeBatchV1InnerServiceSMOImpl.savePayFeeBatch(payFeeBatchPo);

        if (flag < 1) {
            throw new IllegalArgumentException("生成批次失败");
        }

        reqJson.put("batchId", payFeeBatchPo.getBatchId());
    }

}
