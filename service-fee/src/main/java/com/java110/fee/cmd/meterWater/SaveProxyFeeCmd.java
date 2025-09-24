package com.java110.fee.cmd.meterWater;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.payFee.PayFeeBatchDto;
import com.java110.dto.user.UserDto;
import com.java110.fee.bill.ICycleConvertOnceFee;
import com.java110.intf.fee.*;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.payFee.PayFeeBatchPo;
import com.java110.po.payFee.PayFeeRulePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "meterWater.saveProxyFee")
public class SaveProxyFeeCmd extends Cmd {

    @Autowired
    private IPayFeeBatchV1InnerServiceSMO payFeeBatchV1InnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;


    @Autowired
    private IPayFeeRuleV1InnerServiceSMO payFeeRuleV1InnerServiceSMOImpl;

    @Autowired
    private ICycleConvertOnceFee cycleConvertOnceFeeImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "feeTypeCd", "请求报文中未包含费用类型");
        Assert.hasKeyAndValue(reqJson, "configId", "请求报文中未包含费用项");
        Assert.hasKeyAndValue(reqJson, "objType", "请求报文中未包含objType");
        Assert.hasKeyAndValue(reqJson, "objId", "请求报文中未包含objId");
        Assert.hasKeyAndValue(reqJson, "amount", "请求报文中未包含amount");
        Assert.hasKeyAndValue(reqJson, "consumption", "请求报文中未包含consumption");
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
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        //生成批次
        String userId = CmdContextUtils.getUserId(context);
        String storeId = CmdContextUtils.getStoreId(context);

        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setConfigId(reqJson.getString("configId"));
        feeConfigDto.setCommunityId(reqJson.getString("communityId"));
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        Assert.listOnlyOne(feeConfigDtos, "费用项不存在");

        //生成批次
        generatorBatch(reqJson, userId);
        List<PayFeeRulePo> payFeeRulePos = new ArrayList<>();

        PayFeeRulePo payFeeRulePo = new PayFeeRulePo();
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
        // 后付费
        if(FeeConfigDto.PAYMENT_CD_AFTER.equals(feeConfigDtos.get(0).getPaymentCd())){
            payFeeRulePo.setCurYearMonth(reqJson.getString("endTime"));
        }
        payFeeRulePos.add(payFeeRulePo);


        //todo 设置费用
        payFeeRuleV1InnerServiceSMOImpl.savePayFeeRules(payFeeRulePos);


        //todo 走账单模式
        cycleConvertOnceFeeImpl.rulesGeneratePayFees(payFeeRulePos);

        context.setResponseEntity(ResultVo.success());

    }


    private void addFeeAttr(String specCd, String value, List<FeeAttrPo> feeAttrPos, PayFeePo payFeePo) {

        if (StringUtil.isEmpty(value)) {
            return;
        }

        FeeAttrPo curFeeAttrPo = new FeeAttrPo();
        curFeeAttrPo.setFeeId(payFeePo.getFeeId());
        curFeeAttrPo.setCommunityId(payFeePo.getCommunityId());
        curFeeAttrPo.setValue(value);
        curFeeAttrPo.setSpecCd(specCd);
        curFeeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
        feeAttrPos.add(curFeeAttrPo);

    }


    /**
     * 生成批次号
     *
     * @param reqJson
     */
    private void generatorBatch(JSONObject reqJson, String userId) {
        PayFeeBatchPo payFeeBatchPo = new PayFeeBatchPo();
        payFeeBatchPo.setBatchId(GenerateCodeFactory.getGeneratorId("12"));
        payFeeBatchPo.setCommunityId(reqJson.getString("communityId"));
        payFeeBatchPo.setCreateUserId(reqJson.getString("userId"));
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
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
