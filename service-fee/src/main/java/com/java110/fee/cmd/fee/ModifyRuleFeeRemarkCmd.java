package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.intf.fee.IPayFeeRuleV1InnerServiceSMO;
import com.java110.po.payFee.PayFeeRulePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;

/**
 * 修改费用备注
 */
@Java110Cmd(serviceCode = "fee.modifyRuleFeeRemark")
public class ModifyRuleFeeRemarkCmd extends Cmd {

    @Autowired
    private IPayFeeRuleV1InnerServiceSMO payFeeRuleV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "ruleId", "未包含费用");
        Assert.hasKeyAndValue(reqJson, "feeRemark", "未包含备注");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        PayFeeRulePo payFeeRulePo = new PayFeeRulePo();
        payFeeRulePo.setRuleId(reqJson.getString("ruleId"));
        payFeeRulePo.setFeeRemark(reqJson.getString("feeRemark"));
        payFeeRuleV1InnerServiceSMOImpl.updatePayFeeRule(payFeeRulePo);
    }
}
