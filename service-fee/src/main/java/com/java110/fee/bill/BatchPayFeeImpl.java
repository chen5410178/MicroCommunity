package com.java110.fee.bill;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.account.AccountDto;
import com.java110.dto.fee.ComputeDiscountDto;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.fee.PayFeeDto;
import com.java110.dto.payFee.BatchPayFeeOrder;
import com.java110.dto.user.UserDto;
import com.java110.fee.bmo.fee.IFinishFeeNotify;
import com.java110.fee.bmo.fee.IGeneratorPayFeeOrder;
import com.java110.fee.bmo.fee.IPayPrePayFeeBmo;
import com.java110.fee.bmo.receipt.IFeeReceiptBmo;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IFeeReceiptInnerServiceSMO;
import com.java110.intf.fee.IPayFeeDetailV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.fee.PayFeePo;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.lock.DistributedLock;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class BatchPayFeeImpl implements IBatchPayFee {

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;
    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;
    @Autowired
    private IPayFeeDetailV1InnerServiceSMO payFeeDetailNewV1InnerServiceSMOImpl;


    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IFeeReceiptInnerServiceSMO feeReceiptInnerServiceSMOImpl;

    @Autowired
    private IFinishFeeNotify finishFeeNotifyImpl;

    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;

    @Autowired
    private IComputeFeeDiscount computeFeeDiscountImpl;

    @Autowired
    private IGeneratorPayFeeOrder generatorPayFeeOrderImpl;

    @Autowired
    private IFeeReceiptBmo feeReceiptBmoImpl;

    @Autowired
    private IPayPrePayFeeBmo payPrePayFeeBmoImpl;

    @Override
    public JSONObject batchPayFee(String communityId,
                                  String primeRate,
                                  String acctId,
                                  String payOrderId,
                                  String remark,
                                  UserDto userDto,
                                  JSONArray feeConfigs,
                                  JSONArray rules) {

        String receiptCode = feeReceiptInnerServiceSMOImpl.generatorReceiptCode(communityId);
        JSONArray details = new JSONArray();

        JSONObject feeConfigObj = null;
        double configDiscountAmount = 0.0; // 费用项折扣
        JSONArray payFees = null;
        JSONObject feeObj = null;
        String feeId = "";
        Map<String, List<BatchPayFeeOrder>> batchPayFeeOrderMap = new HashMap<>();
        for (int configIndex = 0; configIndex < feeConfigs.size(); configIndex++) {
            feeConfigObj = feeConfigs.getJSONObject(configIndex);
            if (!feeConfigObj.containsKey("payFees")) {
                continue;
            }
            payFees = feeConfigObj.getJSONArray("payFees");
            if (ListUtil.isNull(payFees)) {
                continue;
            }
            // todo 计算优惠
            configDiscountAmount = computeFeesDiscount(feeConfigObj.getJSONArray("selectedDiscountIds"), payFees, communityId);
            feeConfigObj.put("configDiscountAmount", configDiscountAmount);

            for (int feeIndex = 0; feeIndex < payFees.size(); feeIndex++) {
                feeObj = payFees.getJSONObject(feeIndex);
                String requestId = DistributedLock.getLockUUID();
                String key = this.getClass().getSimpleName() + feeObj.getString("feeId");

                DistributedLock.waitGetDistributedLock(key, requestId);
                try {
                    doPayFee(userDto, communityId, primeRate, payOrderId, receiptCode, acctId, remark, feeObj, feeConfigObj, details, batchPayFeeOrderMap);
                } finally {
                    DistributedLock.releaseDistributedLock(key, requestId);
                }
            }
            feeId = getFeeIdWithMaxEndTime(payFees);
            //todo 修改车辆
            finishFeeNotifyImpl.updateCarEndTime(feeId, communityId);
            //todo 租金延期房屋结束时间
            finishFeeNotifyImpl.updateRoomEndTime(feeId, communityId);
        }
        for (String key : batchPayFeeOrderMap.keySet()) {
            generatorPayFeeOrderImpl.generatorPayFeeOrder(batchPayFeeOrderMap.get(key), payOrderId);
        }

        // 如果有预付款，保存预付款
        if (!ListUtil.isNull(rules)) {
            PayFeeDto payFeeDto = new PayFeeDto();
            payFeeDto.setFeeId(feeId);
            payFeeDto.setCommunityId(communityId);
            List<PayFeeDto> feeDtos = payFeeV1InnerServiceSMOImpl.queryPayFees(payFeeDto);
            if (!ListUtil.isNull(feeDtos)) {
                payPrePayFeeBmoImpl.payPreFee(rules,
                        receiptCode, feeDtos.get(0),
                        primeRate,
                        userDto,
                        remark);
            }

        }
        JSONObject data = new JSONObject();
        data.put("details", details);
        data.put("receiptCode", receiptCode);
        return data;
    }

    /**
     * 单个费用缴费
     *
     * @param userDto       员工
     * @param communityId   小区ID
     * @param primeRate     缴费方式
     * @param extPayOrderId 付款订单
     * @param receiptCode   收据编号
     * @param acctId        账户ID
     * @param feeObj        费用信息
     * @param feeConfigObj  费用项
     * @param details       缴费明细，缴费完 写入detailId
     */
    private void doPayFee(UserDto userDto, String communityId, String primeRate,
                          String extPayOrderId, String receiptCode, String acctId,
                          String remark,
                          JSONObject feeObj,
                          JSONObject feeConfigObj, JSONArray details,
                          Map<String, List<BatchPayFeeOrder>> batchPayFeeOrderMap) {
        BigDecimal receivedAmount = new BigDecimal(feeObj.getString("receivedAmount"));
        BigDecimal discountAmount = new BigDecimal(feeConfigObj.getString("configDiscountAmount"));
        String feeId = feeObj.getString("feeId");
        double acctAmount = 0.0;
        double deductionAmount = 0.0;
        double lateAmount = 0.0;

        PayFeeDto payFeeDto = new PayFeeDto();
        payFeeDto.setFeeId(feeId);
        payFeeDto.setCommunityId(communityId);
        payFeeDto.setState(FeeDto.STATE_DOING);
        List<PayFeeDto> feeDtos = payFeeV1InnerServiceSMOImpl.queryPayFees(payFeeDto);
        if (ListUtil.isNull(feeDtos)) {
            throw new CmdException("已缴费，请勿重复缴费");
        }
        String payOrderId = "";
        if (!batchPayFeeOrderMap.containsKey(feeDtos.get(0).getPayerObjId())) {
            payOrderId = GenerateCodeFactory.getGeneratorId("11");
            batchPayFeeOrderMap.put(feeDtos.get(0).getPayerObjId(), new ArrayList<>());
        } else {
            payOrderId = batchPayFeeOrderMap.get(feeDtos.get(0).getPayerObjId()).get(0).getPayFeeDetailPo().getPayOrderId();
        }

        // 处理优惠折扣
        if (discountAmount.doubleValue() > 0 && discountAmount.doubleValue() >= receivedAmount.doubleValue()) {
            deductionAmount = receivedAmount.doubleValue();
            discountAmount = discountAmount.subtract(receivedAmount);
            receivedAmount = new BigDecimal("0");
        } else if (discountAmount.doubleValue() > 0 && discountAmount.doubleValue() < receivedAmount.doubleValue()) {
            deductionAmount = discountAmount.doubleValue();
            receivedAmount = receivedAmount.subtract(discountAmount);
            discountAmount = new BigDecimal("0");
        } else if (discountAmount.doubleValue() < 0) { // 滞纳金
            lateAmount = discountAmount.multiply(new BigDecimal("-1")).doubleValue();
            receivedAmount = receivedAmount.subtract(discountAmount);
            discountAmount = new BigDecimal("0");
        }

        feeConfigObj.put("configDiscountAmount", discountAmount.doubleValue());

        // 账户扣款
        JSONObject receivedMap = withholdAccount(acctId, receivedAmount, feeId, communityId);
        receivedAmount = new BigDecimal(receivedMap.getString("receivedAmount"));
        acctAmount = receivedMap.getDouble("acctAmount");

        Double cycle = DateUtil.dayCompare(DateUtil.getDateFromStringA(feeDtos.get(0).getStartTime()), DateUtil.getDateFromStringA(feeDtos.get(0).getEndTime()));

        PayFeeDetailPo payFeeDetailPo = new PayFeeDetailPo();
        payFeeDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId("11"));
        payFeeDetailPo.setFeeId(feeDtos.get(0).getFeeId());
        payFeeDetailPo.setCommunityId(communityId);
        payFeeDetailPo.setCycles(cycle + "");
        payFeeDetailPo.setReceivableAmount(feeDtos.get(0).getOweAmount());
        payFeeDetailPo.setReceivedAmount(receivedAmount.doubleValue() + "");
        payFeeDetailPo.setPrimeRate(primeRate);
        payFeeDetailPo.setRemark(remark);
        payFeeDetailPo.setState(FeeDetailDto.STATE_NORMAL);
        payFeeDetailPo.setStartTime(feeDtos.get(0).getStartTime());
        payFeeDetailPo.setEndTime(feeDtos.get(0).getEndTime());
        payFeeDetailPo.setCreateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        payFeeDetailPo.setPayableAmount(feeDtos.get(0).getReceivableAmount());
        payFeeDetailPo.setPayOrderId(payOrderId);
        payFeeDetailPo.setCashierId(userDto.getUserId());
        payFeeDetailPo.setCashierName(userDto.getName());
        payFeeDetailPo.setOpenInvoice("N");
        payFeeDetailPo.setAcctAmount(acctAmount + "");
        payFeeDetailPo.setDiscountAmount("0");
        payFeeDetailPo.setDeductionAmount(deductionAmount + "");
        payFeeDetailPo.setLateAmount(lateAmount + "");
        payFeeDetailPo.setGiftAmount("0");

        payFeeDetailPo.setFeeTime(feeDtos.get(0).getFeeTime());


        //todo 缓存收据编号
        CommonCache.setValue(payFeeDetailPo.getDetailId() + CommonCache.RECEIPT_CODE, receiptCode, CommonCache.DEFAULT_EXPIRETIME_TWO_MIN);
        int flag = payFeeDetailNewV1InnerServiceSMOImpl.savePayFeeDetailNew(payFeeDetailPo);
        if (flag < 1) {
            throw new CmdException("缴费失败");
        }

        PayFeePo payFeePo = new PayFeePo();
        payFeePo.setFeeId(feeDtos.get(0).getFeeId());
        payFeePo.setOweAmount("0");
        payFeePo.setState(FeeDto.STATE_FINISH);
        BigDecimal receivableAmountDec = new BigDecimal(payFeeDetailPo.getReceivableAmount());
        BigDecimal receivedAmountDec = new BigDecimal(payFeeDetailPo.getReceivedAmount());
        BigDecimal acctAmountDec = new BigDecimal(payFeeDetailPo.getAcctAmount());
        BigDecimal discountAmountDec = new BigDecimal(payFeeDetailPo.getDiscountAmount());
        BigDecimal deductionAmountDec = new BigDecimal(payFeeDetailPo.getDeductionAmount());
        BigDecimal giftAmountDec = new BigDecimal(payFeeDetailPo.getGiftAmount());

        BigDecimal oweAmount = receivableAmountDec.subtract(receivedAmountDec)
                .subtract(acctAmountDec)
                .subtract(discountAmountDec)
                .subtract(deductionAmountDec)
                .subtract(giftAmountDec);

        if (oweAmount.doubleValue() > 0) {
            payFeePo.setState(FeeDto.STATE_DOING);
            payFeePo.setOweAmount(oweAmount.doubleValue() + "");
        }

        flag = payFeeV1InnerServiceSMOImpl.updatePayFee(payFeePo);
        if (flag < 1) {
            throw new CmdException("缴费失败");
        }

        //todo 生成收据
        feeReceiptBmoImpl.savePayFeeReceipt(payFeeDetailPo, receiptCode);

        List<BatchPayFeeOrder> batchPayFeeOrders = batchPayFeeOrderMap.get(feeDtos.get(0).getPayerObjId());
        batchPayFeeOrders.add(new BatchPayFeeOrder(payFeeDetailPo, feeDtos.get(0)));

        //todo 修改报修单
        finishFeeNotifyImpl.updateRepair(feeDtos.get(0).getFeeId(), feeDtos.get(0).getCommunityId(), payFeeDetailPo.getReceivedAmount());
        details.add(payFeeDetailPo.getDetailId());

    }


    /**
     * 账户扣款
     *
     * @param acctId
     * @param receivedAmount
     * @param feeId
     * @param communityId
     * @return
     */
    private JSONObject withholdAccount(String acctId, BigDecimal receivedAmount, String feeId, String communityId) {
        JSONObject paramOut = new JSONObject();
        double accountAmount = 0;
        paramOut.put("acctAmount", accountAmount);
        paramOut.put("receivedAmount", receivedAmount.doubleValue());
        if (StringUtil.isEmpty(acctId)) {
            return paramOut;
        }
        if (receivedAmount.doubleValue() <= 0) {
            return paramOut;
        }

        //todo 校验账户金额是否充足
        AccountDto accountDto = new AccountDto();
        accountDto.setAcctId(acctId);
        List<AccountDto> accountDtos = accountInnerServiceSMOImpl.queryAccounts(accountDto);

        if (ListUtil.isNull(accountDtos)) {
            return paramOut;
        }

        double acctTotalAmount = Double.parseDouble(accountDtos.get(0).getAmount());

        if (acctTotalAmount <= 0) {
            return paramOut;
        }
        // 账户余额不足
        if (acctTotalAmount <= receivedAmount.doubleValue()) {
            accountAmount = acctTotalAmount;
            receivedAmount = receivedAmount.subtract(new BigDecimal(acctTotalAmount));
            paramOut.put("receivedAmount", receivedAmount.doubleValue());
            paramOut.put("acctAmount", accountAmount);
        } else {
            accountAmount = receivedAmount.doubleValue();
            paramOut.put("receivedAmount", 0);
            paramOut.put("acctAmount", accountAmount);
        }

        JSONObject acctInfo = new JSONObject();
        acctInfo.put("accountAmount", accountAmount);
        acctInfo.put("acctId", acctId);
        //todo 账户扣款
        finishFeeNotifyImpl.withholdAccount(acctInfo, feeId, communityId);
        return paramOut;
    }

    private String getFeeIdWithMaxEndTime(JSONArray payFees) {
        if (payFees == null || payFees.isEmpty()) {
            return "-1";
        }

        JSONObject maxObj = null;
        Date maxDate = null;
        String endTime = null;
        for (Object obj : payFees) {
            JSONObject jsonObj = (JSONObject) obj;
            endTime = jsonObj.getString("endTime");
            if(!endTime.contains(":")){
                endTime += " 00:00:00";
            }
            Date currentDate = DateUtil.getDateFromStringA(endTime);

            if (maxDate == null || currentDate.after(maxDate)) {
                maxDate = currentDate;
                maxObj = jsonObj;
            }
        }

        return maxObj.getString("feeId");
    }


    /**
     * 计算 优惠折扣
     *
     * @param selectedDiscountIds
     * @param payFees
     * @return
     */
    private double computeFeesDiscount(JSONArray selectedDiscountIds, JSONArray payFees, String communityId) {
        if (ListUtil.isNull(selectedDiscountIds)) {
            return 0;
        }
        if (ListUtil.isNull(payFees)) {
            return 0;
        }

        List<String> feeIds = new ArrayList<>();
        for (int feeIndex = 0; feeIndex < payFees.size(); feeIndex++) {
            feeIds.add(payFees.getJSONObject(feeIndex).getString("feeId"));
        }

        PayFeeDto feeDto = new PayFeeDto();
        feeDto.setFeeIds(feeIds.toArray(new String[feeIds.size()]));
        feeDto.setCommunityId(communityId);
        feeDto.setState(FeeDto.STATE_DOING);
        List<PayFeeDto> feeDtos = payFeeV1InnerServiceSMOImpl.queryPayFees(feeDto);
        if (ListUtil.isNull(feeDtos)) {
            return 0;
        }

        List<ComputeDiscountDto> computeDiscountDtos = computeFeeDiscountImpl.compute(feeDtos);
        if (ListUtil.isNull(computeDiscountDtos)) {
            return 0;
        }
        BigDecimal discountFee = new BigDecimal("0");
        for (ComputeDiscountDto computeDiscountDto : computeDiscountDtos) {
            discountFee = discountFee.add(new BigDecimal(computeDiscountDto.getDiscountPrice() + ""));
        }
        return discountFee.doubleValue() * -1;
    }
}
