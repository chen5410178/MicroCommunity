package com.java110.acct.payment.business.oweFee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.acct.payment.IPaymentBusiness;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.account.AccountDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.fee.*;
import com.java110.dto.payment.PaymentOrderDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.intf.community.ICommunityV1InnerServiceSMO;
import com.java110.intf.fee.IBatchPayFeeInnerServiceSMO;
import com.java110.intf.fee.IFeeReceiptInnerServiceSMO;
import com.java110.intf.fee.IPayFeeDetailV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeV1InnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.fee.PayFeePo;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.lock.DistributedLock;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 批量缴费 （专供手机端使用）
 */
@Service("oweFee")
public class OweFeePaymentBusiness implements IPaymentBusiness {


    private final static Logger logger = LoggerFactory.getLogger(OweFeePaymentBusiness.class);

    public static final String CODE_PREFIX_ID = "10";


    //域
    public static final String DOMAIN_COMMON = "DOMAIN.COMMON";

    //键
    public static final String TOTAL_FEE_PRICE = "TOTAL_FEE_PRICE";

    //键
    public static final String RECEIVED_AMOUNT_SWITCH = "RECEIVED_AMOUNT_SWITCH";

    //禁用电脑端提交收费按钮
    public static final String OFFLINE_PAY_FEE_SWITCH = "OFFLINE_PAY_FEE_SWITCH";

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;


    @Autowired
    private ICommunityV1InnerServiceSMO communityV1InnerServiceSMOImpl;

    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;


    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IBatchPayFeeInnerServiceSMO batchPayFeeInnerServiceSMOImpl;


    @Override
    public PaymentOrderDto unified(ICmdDataFlowContext context, JSONObject reqJson) {

        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(reqJson, "cashierUserId", "请求报文中未包含cashierUserId节点");
        if (!reqJson.containsKey("feeConfigs")) {
            throw new CmdException("未包含费用");
        }
        JSONArray feeConfigs = reqJson.getJSONArray("feeConfigs");
        if (ListUtil.isNull(feeConfigs)) {
            throw new CmdException("未包含费用项");
        }
        JSONObject paramInObj = null;
        for (int feeIndex = 0; feeIndex < feeConfigs.size(); feeIndex++) {
            paramInObj = feeConfigs.getJSONObject(feeIndex);
            Assert.hasKeyAndValue(paramInObj, "payFees", "未包含缴费费用");
        }

        UserDto userDto = new UserDto();
        userDto.setUserId(reqJson.getString("cashierUserId"));
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        if (ListUtil.isNull(userDtos)) {
            throw new CmdException("缴费用户不存在");
        }
        JSONArray payFees = null;
        JSONObject feeObj = null;
        String feeId = "";
        JSONObject feeConfigObj = null;
        List<String> feeIds = null;
        BigDecimal oweAmount = new BigDecimal("0");
        BigDecimal disAmount = new BigDecimal("0");
        String feeName = reqJson.getString("feeName");
        JSONArray selectedDiscountIds = null;
        for (int configIndex = 0; configIndex < feeConfigs.size(); configIndex++) {
            feeConfigObj = feeConfigs.getJSONObject(configIndex);
            if (!feeConfigObj.containsKey("payFees")) {
                continue;
            }
            payFees = feeConfigObj.getJSONArray("payFees");
            if (ListUtil.isNull(payFees)) {
                continue;
            }

            feeIds = new ArrayList<>();
            for (int feeIndex = 0; feeIndex < payFees.size(); feeIndex++) {
                feeIds.add(payFees.getJSONObject(feeIndex).getString("feeId"));
            }

            // 计算费用
            PayFeeDto feeDto = new PayFeeDto();
            feeDto.setFeeIds(feeIds.toArray(new String[feeIds.size()]));
            feeDto.setCommunityId(reqJson.getString("communityId"));
            feeDto.setOwnerId(reqJson.getString("ownerId"));
            feeDto.setState(FeeDto.STATE_DOING);
            List<PayFeeDto> feeDtos = payFeeV1InnerServiceSMOImpl.queryPayFees(feeDto);
            if (ListUtil.isNull(feeDtos)) {
                throw new CmdException("费用不存在");
            }

            feeName = getFeeName(feeDtos.get(0)) + "-" + feeName;


            for (PayFeeDto payFeeDto : feeDtos) {
                oweAmount = oweAmount.add(new BigDecimal(payFeeDto.getOweAmount()));
            }

            List<ComputeDiscountDto> computeDiscountDtos = payFeeV1InnerServiceSMOImpl.computeDiscount(feeDtos);
            selectedDiscountIds = new JSONArray();
            for (ComputeDiscountDto computeDiscountDto : computeDiscountDtos) {
                if (computeDiscountDto.getDiscountPrice() != 0) {
                    disAmount = disAmount.add(new BigDecimal(computeDiscountDto.getDiscountPrice() + ""));
                    selectedDiscountIds.add(computeDiscountDto.getDiscountId());
                }
            }
            feeConfigObj.put("discountAmount", disAmount.doubleValue());
            feeConfigObj.put("selectedDiscountIds", selectedDiscountIds);

        }
        String orderId = GenerateCodeFactory.getOId();
        BigDecimal moneyDec = oweAmount.add(disAmount);
        double money = moneyDec.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();

        //todo 支持账户扣款
        money = ifHasAccount(reqJson, money);

        PaymentOrderDto paymentOrderDto = new PaymentOrderDto();
        paymentOrderDto.setOrderId(orderId);
        paymentOrderDto.setMoney(money);
        paymentOrderDto.setName(feeName);
        paymentOrderDto.setUserId(userDtos.get(0).getUserId());
        paymentOrderDto.setCycles("1");


        JSONObject saveFees = new JSONObject();
        saveFees.put("orderId", orderId);
        saveFees.put("userId", userDtos.get(0).getUserId());
        saveFees.put("money", money);
        saveFees.put("communityId", reqJson.getString("communityId"));
        saveFees.put("feeConfigs", feeConfigs);
        if (reqJson.containsKey("acctId")) {
            saveFees.put("accountAmount", reqJson.getDoubleValue("accountAmount"));
            saveFees.put("acctId", reqJson.getString("acctId"));
        }
        CommonCache.setValue(FeeDto.REDIS_PAY_OWE_FEE + orderId, saveFees.toJSONString(), CommonCache.PAY_DEFAULT_EXPIRE_TIME);
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

        String order = CommonCache.getAndRemoveValue(FeeDto.REDIS_PAY_OWE_FEE + paymentOrderDto.getOrderId());

        JSONObject paramIn = JSONObject.parseObject(order);


        String communityId = paramIn.getString("communityId");
        String primeRate = "5";
        String remark = "线上公众号支付";
        String acctId = paramIn.getString("acctId");
        String payOrderId = paramIn.getString("payOrderId");
        String amount = paramIn.getString("money");
        JSONArray feeConfigs = reqJson.getJSONArray("feeConfigs");

        batchPayFeeInnerServiceSMOImpl.batchPayFee(new BatchPayFeeDto(communityId, primeRate, remark, acctId, payOrderId, userDtos.get(0), feeConfigs));
    }


    private String getFeeName(PayFeeDto feeDto) {
        //查询小区名称
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(feeDto.getCommunityId());
        List<CommunityDto> communityDtos = communityV1InnerServiceSMOImpl.queryCommunitys(communityDto);

        Assert.listOnlyOne(communityDtos, "小区不存在");


        return communityDtos.get(0).getName() + "-" + feeDto.getPayerObjName();
    }

    private double ifHasAccount(JSONObject reqJson, double money) {

        if (!reqJson.containsKey("acctId")) {
            return money;
        }

        String acctId = reqJson.getString("acctId");

        if (StringUtil.isEmpty(acctId)) {
            return money;
        }

        AccountDto accountDto = new AccountDto();
        accountDto.setAcctId(acctId);
        accountDto.setAcctType(AccountDto.ACCT_TYPE_CASH);
        List<AccountDto> accountDtos = accountInnerServiceSMOImpl.queryAccounts(accountDto);

        if (ListUtil.isNull(accountDtos)) {
            return money;
        }

        Double acctAmount = Double.parseDouble(accountDtos.get(0).getAmount());
        if (acctAmount >= money) {
            reqJson.put("accountAmount", money);
            return 0.00;
        }

        reqJson.put("accountAmount", acctAmount.doubleValue());

        BigDecimal accountAmountDec = new BigDecimal(acctAmount);
        accountAmountDec = new BigDecimal(money).subtract(accountAmountDec).setScale(2, BigDecimal.ROUND_HALF_UP);
        return accountAmountDec.doubleValue();
    }

    private String getFeeIdWithMaxEndTime(JSONArray payFees) {
        if (payFees == null || payFees.isEmpty()) {
            return "-1";
        }

        JSONObject maxObj = null;
        Date maxDate = null;

        for (Object obj : payFees) {
            JSONObject jsonObj = (JSONObject) obj;
            Date currentDate = DateUtil.getDateFromStringA(jsonObj.getString("endTime"));

            if (maxDate == null || currentDate.after(maxDate)) {
                maxDate = currentDate;
                maxObj = jsonObj;
            }
        }

        return maxObj.getString("feeId");
    }


}
