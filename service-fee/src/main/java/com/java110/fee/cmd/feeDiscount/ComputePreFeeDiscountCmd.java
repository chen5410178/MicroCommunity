package com.java110.fee.cmd.feeDiscount;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.fee.ComputeDiscountDto;
import com.java110.dto.payFee.PayFeeRuleDto;
import com.java110.fee.bill.IComputeFeeDiscount;
import com.java110.intf.fee.IPayFeeRuleV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * 预付款 计算折扣
 */
@Java110Cmd(serviceCode = "feeDiscount.computePreFeeDiscount")
public class ComputePreFeeDiscountCmd extends Cmd {


    @Autowired
    private IComputeFeeDiscount computeFeeDiscountImpl;

    @Autowired
    private IPayFeeRuleV1InnerServiceSMO payFeeRuleV1InnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区不存在");
        Assert.hasKeyAndValue(reqJson, "ruleId", "未包含费用账单规则");
        Assert.hasKeyAndValue(reqJson, "cycles", "未包含周期");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        PayFeeRuleDto payFeeRuleDto = new PayFeeRuleDto();
        payFeeRuleDto.setRuleId(reqJson.getString("ruleId"));
        payFeeRuleDto.setCommunityId(reqJson.getString("communityId"));
        List<PayFeeRuleDto> payFeeRuleDtos = payFeeRuleV1InnerServiceSMOImpl.queryPayFeeRules(payFeeRuleDto);
        if (ListUtil.isNull(payFeeRuleDtos)) {
            throw new CmdException("账单规则不存在");
        }

        List<ComputeDiscountDto> computeDiscountDtos = computeFeeDiscountImpl.computePrePayFeeDiscount(payFeeRuleDtos.get(0),reqJson.getIntValue("cycles"));
        context.setResponseEntity(ResultVo.createResponseEntity(computeDiscountDtos));
    }
}
