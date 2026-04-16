package com.java110.acct.payment.business.oweFee;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.payment.IPaymentBusiness;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.fee.FeeAmountDto;
import com.java110.dto.payFee.PayFeeRuleDto;
import com.java110.dto.payment.PaymentOrderDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.fee.IComputePreFeeInnerServiceSMO;
import com.java110.intf.fee.IPayFeeRuleV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("rulePayFee")
public class RulePayFeePaymentBusiness implements IPaymentBusiness {
    private final static Logger logger = LoggerFactory.getLogger(OweFeePaymentBusiness.class);
    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeRuleV1InnerServiceSMO payFeeRuleV1InnerServiceSMOImpl;

    @Autowired
    private IComputePreFeeInnerServiceSMO computePreFeeInnerServiceSMOImpl;

    @Override
    public PaymentOrderDto unified(ICmdDataFlowContext context, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(reqJson, "cashierUserId", "请求报文中未包含cashierUserId节点");
        Assert.hasKeyAndValue(reqJson, "payerObjId", "请求报文中未包含payerObjId");
        Assert.hasKeyAndValue(reqJson, "ruleId", "未包含规则ID");
        Assert.hasKeyAndValue(reqJson, "cycles", "未包含缴费周期");
        UserDto userDto = new UserDto();
        userDto.setUserId(reqJson.getString("cashierUserId"));
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        if (ListUtil.isNull(userDtos)) {
            throw new CmdException("缴费用户不存在");
        }

        PayFeeRuleDto payFeeRuleDto = new PayFeeRuleDto();
        payFeeRuleDto.setRuleId(reqJson.getString("ruleId"));
        payFeeRuleDto.setCommunityId(reqJson.getString("communityId"));
        List<PayFeeRuleDto> payFeeRuleDtos = payFeeRuleV1InnerServiceSMOImpl.queryPayFeeRules(payFeeRuleDto);
        if (ListUtil.isNull(payFeeRuleDtos)) {
            throw new CmdException("缴费账单不存在");
        }
        FeeAmountDto paramOut = computePreFeeInnerServiceSMOImpl.computeRulePayFeeAmount(reqJson);

        String orderId = GenerateCodeFactory.getOId();

        PaymentOrderDto paymentOrderDto = new PaymentOrderDto();
        paymentOrderDto.setOrderId(orderId);
        paymentOrderDto.setMoney(paramOut.getAmount());
        paymentOrderDto.setName(payFeeRuleDtos.get(0).getFeeName());
        paymentOrderDto.setUserId(userDtos.get(0).getUserId());
        paymentOrderDto.setCycles("1");

        JSONObject saveFees = new JSONObject();
        saveFees.put("orderId", orderId);
        saveFees.put("userId", userDtos.get(0).getUserId());
        saveFees.put("money", paramOut.getAmount());
        saveFees.put("communityId", reqJson.getString("communityId"));
        saveFees.put("receivedAmount", paramOut.getAmount());
        saveFees.put("ruleId", reqJson.getString("ruleId"));
        saveFees.put("cycles", reqJson.getString("cycles"));
        saveFees.put("payerObjId", reqJson.getString("payerObjId"));
        CommonCache.setValue("RULE_PAY_FEE_" + orderId, saveFees.toJSONString(), CommonCache.PAY_DEFAULT_EXPIRE_TIME);
        return paymentOrderDto;
    }

    @Override
    public void notifyPayment(PaymentOrderDto paymentOrderDto, JSONObject reqJson) {
        UserDto userDto = new UserDto();
        userDto.setUserId(paymentOrderDto.getUserId());
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        if (ListUtil.isNull(userDtos)) {
            throw new CmdException("缴费人员不存在");
        }

        String order = CommonCache.getAndRemoveValue("RULE_PAY_FEE_" + paymentOrderDto.getOrderId());

        JSONObject paramIn = JSONObject.parseObject(order);

        paramIn.put("userId",userDtos.get(0).getUserId());
        paramIn.put("userName",userDtos.get(0).getName());

//        String communityId = paramIn.getString("communityId");
//        String primeRate = "5";
//        String remark = "线上公众号支付";
//        String payOrderId = paramIn.getString("payOrderId");

        computePreFeeInnerServiceSMOImpl.prePayFee(paramIn);
    }
}
