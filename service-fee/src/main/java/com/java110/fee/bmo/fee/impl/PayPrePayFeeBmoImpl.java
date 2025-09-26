package com.java110.fee.bmo.fee.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.ComputeDiscountDto;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.fee.PayFeeDto;
import com.java110.dto.payFee.PayFeeRuleDto;
import com.java110.dto.prePayFee.PrePayFeeDto;
import com.java110.dto.user.UserDto;
import com.java110.fee.bill.IComputeFeeDiscount;
import com.java110.fee.bmo.fee.IGeneratorPayFeeOrder;
import com.java110.fee.bmo.fee.IPayPrePayFeeBmo;
import com.java110.fee.bmo.receipt.IFeeReceiptBmo;
import com.java110.intf.fee.IFeeReceiptInnerServiceSMO;
import com.java110.intf.fee.IPayFeeRuleV1InnerServiceSMO;
import com.java110.intf.fee.IPrePayFeeV1InnerServiceSMO;
import com.java110.po.prePayFee.PrePayFeePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class PayPrePayFeeBmoImpl implements IPayPrePayFeeBmo {

    @Autowired
    private IPayFeeRuleV1InnerServiceSMO payFeeRuleV1InnerServiceSMOImpl;

    @Autowired
    private IComputeFeeDiscount computeFeeDiscountImpl;

    @Autowired
    private IFeeReceiptInnerServiceSMO feeReceiptInnerServiceSMOImpl;

    @Autowired
    private IPrePayFeeV1InnerServiceSMO prePayFeeV1InnerServiceSMOImpl;

    @Autowired
    private IGeneratorPayFeeOrder generatorPayFeeOrderImpl;

    @Autowired
    private IFeeReceiptBmo feeReceiptBmoImpl;

    @Override
    public int payPreFee(JSONArray rules, String receiptCode, FeeDto feeDto, String primeRate, UserDto userDto, String remark) {
        if (ListUtil.isNull(rules)) {
            return 0;
        }
        String payOrderId = GenerateCodeFactory.getGeneratorId("11");
        List<PrePayFeePo> prePayFeePos = new ArrayList<>();

        for (int i = 0; i < rules.size(); i++) {
            JSONObject rule = rules.getJSONObject(i);
            doPayPreFee(rule, receiptCode, feeDto, primeRate, payOrderId, prePayFeePos, userDto, remark);
        }

        generatorPayFeeOrderImpl.generatorPrePayFeeOrder(prePayFeePos, feeDto.getPayerObjType());
        return 1;
    }

    @Override
    public int payPreFee(JSONArray rules, String receiptCode, PayFeeDto payFeeDto, String primeRate, UserDto userDto, String remark) {
        return payPreFee(rules, receiptCode, BeanConvertUtil.covertBean(payFeeDto, FeeDto.class), primeRate, userDto, remark);
    }

    private void doPayPreFee(JSONObject rule, String receiptCode, FeeDto feeDto, String primeRate, String payOrderId,
                             List<PrePayFeePo> prePayFeePos, UserDto userDto, String remark) {
        String ruleId = rule.getString("ruleId");
        int cycles = rule.getIntValue("cycles");
        double ruleReceivedAmount = rule.getDoubleValue("receivedAmount");
        String startMonth = rule.getString("startMonth");
        String year = startMonth.split("-")[0];
        String month = startMonth.split("-")[1];
        PayFeeRuleDto payFeeRuleDto = new PayFeeRuleDto();
        payFeeRuleDto.setRuleId(ruleId);
        payFeeRuleDto.setCommunityId(feeDto.getCommunityId());
        List<PayFeeRuleDto> payFeeRuleDtos = payFeeRuleV1InnerServiceSMOImpl.queryPayFeeRules(payFeeRuleDto);
        if (ListUtil.isNull(payFeeRuleDtos)) {
            throw new CmdException("账单规则不存在");
        }

        Date startTime = DateUtil.getDateFromStringA(payFeeRuleDtos.get(0).getEndTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.set(Calendar.YEAR, Integer.parseInt(year));
        calendar.set(Calendar.MONTH, Integer.parseInt(month) - 1);
        startTime = calendar.getTime();
        calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.add(Calendar.MONTH, cycles);
        Date endTime = calendar.getTime();

        double discountAmount = computeFeeDiscountAmount(rule, feeDto.getCommunityId(), cycles, payFeeRuleDtos.get(0));
        PrePayFeePo prePayFeePo = new PrePayFeePo();
        prePayFeePo.setReceivableAmount(ruleReceivedAmount + "");
        BigDecimal receivedAmountDec = new BigDecimal(ruleReceivedAmount + "");
        receivedAmountDec = receivedAmountDec.subtract(new BigDecimal(discountAmount));
        prePayFeePo.setGiftAmount("0");
        prePayFeePo.setPayOrderId(payOrderId);
        prePayFeePo.setPrimeRate(primeRate);
        prePayFeePo.setDiscountAmount(discountAmount + "");
        prePayFeePo.setCycles(cycles + "");
        prePayFeePo.setRemark(remark);
        prePayFeePo.setReceivedAmount(receivedAmountDec.doubleValue() + "");
        prePayFeePo.setOwnerId(feeDto.getOwnerId());
        prePayFeePo.setPayerObjName(feeDto.getPayerObjName());
        prePayFeePo.setOwnerName(feeDto.getOwnerName());
        prePayFeePo.setRemainAmount(ruleReceivedAmount + "");
        prePayFeePo.setFeeName(payFeeRuleDtos.get(0).getFeeName());
        prePayFeePo.setOpenInvoice("N");
        prePayFeePo.setStartTime(DateUtil.getFormatTimeStringB(startTime));
        prePayFeePo.setCashierId(userDto.getUserId());
        prePayFeePo.setState(FeeDetailDto.STATE_NORMAL);
        prePayFeePo.setEndTime(DateUtil.getPreSecTime(endTime));
        prePayFeePo.setRuleId(ruleId);
        prePayFeePo.setCommunityId(feeDto.getCommunityId());
        prePayFeePo.setCashierName(userDto.getName());
        prePayFeePo.setPpfId(GenerateCodeFactory.getGeneratorId("11"));
        prePayFeePo.setPayerObjId(feeDto.getPayerObjId());
        prePayFeePo.setFtId(payFeeRuleDtos.get(0).getFtId());
        prePayFeePo.setFtName(payFeeRuleDtos.get(0).getFtName());
        prePayFeePo.setWay(PrePayFeeDto.WAY_NO);

        int flag = prePayFeeV1InnerServiceSMOImpl.savePrePayFee(prePayFeePo);

        feeReceiptBmoImpl.savePrePayFeeReceipt(prePayFeePo, receiptCode);
        prePayFeePos.add(prePayFeePo);
    }

    private double computeFeeDiscountAmount(JSONObject rule, String communityId, int cycles, PayFeeRuleDto payFeeRuleDto) {
        if (!rule.containsKey("selectedDiscountIds")) {
            return 0.0;
        }
        JSONArray selectedDiscountIds = rule.getJSONArray("selectedDiscountIds");

        if (ListUtil.isNull(selectedDiscountIds)) {
            return 0.0;
        }
        List<ComputeDiscountDto> computeDiscountDtos = computeFeeDiscountImpl.computePrePayFeeDiscount(payFeeRuleDto, cycles);
        if (ListUtil.isNull(computeDiscountDtos)) {
            return 0.0;
        }
        BigDecimal discountAmount = new BigDecimal("0");
        for (ComputeDiscountDto computeDiscountDto : computeDiscountDtos) {
            for (int dIndex = 0; dIndex < selectedDiscountIds.size(); dIndex++) {
                if (selectedDiscountIds.getString(dIndex).equals(computeDiscountDto.getDiscountId())) {
                    discountAmount = discountAmount.add(new BigDecimal(computeDiscountDto.getDiscountPrice()));
                }
            }
        }
        discountAmount = discountAmount.multiply(new BigDecimal("-1")).setScale(2, BigDecimal.ROUND_HALF_UP);
        return discountAmount.doubleValue();
    }
}
