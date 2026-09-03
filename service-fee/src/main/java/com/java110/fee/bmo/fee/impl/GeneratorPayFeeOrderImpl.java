package com.java110.fee.bmo.fee.impl;

import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.fee.PayFeeDetailDto;
import com.java110.dto.fee.PayFeeDto;
import com.java110.dto.payFee.BatchPayFeeOrder;
import com.java110.dto.payFeeOrder.PayFeeOrderDto;
import com.java110.dto.prePayFee.PrePayFeeDto;
import com.java110.fee.bmo.fee.IGeneratorPayFeeOrder;
import com.java110.intf.fee.IPayFeeDetailV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeOrderV1InnerServiceSMO;
import com.java110.intf.fee.IPrePayFeeV1InnerServiceSMO;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.payFeeOrder.PayFeeOrderPo;
import com.java110.po.prePayFee.PrePayFeePo;
import com.java110.utils.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class GeneratorPayFeeOrderImpl implements IGeneratorPayFeeOrder {

    @Autowired
    private IPayFeeOrderV1InnerServiceSMO payFeeOrderV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeDetailV1InnerServiceSMO payFeeDetailV1InnerServiceSMOImpl;

    @Autowired
    private IPrePayFeeV1InnerServiceSMO prePayFeeV1InnerServiceSMOImpl;

    @Override
    public int generatorPayFeeOrder(PayFeeDetailPo payFeeDetailPo, FeeDto feeDto, String extPayOrderId,String primeFile) {
        PayFeeOrderPo payFeeOrderPo = new PayFeeOrderPo();
        payFeeOrderPo.setGiftAmount(payFeeDetailPo.getGiftAmount());
        payFeeOrderPo.setOrderType(PayFeeOrderDto.ORDER_TYPE_PAY_FEE);
        payFeeOrderPo.setPayOrderId(payFeeDetailPo.getPayOrderId());
        payFeeOrderPo.setPrimeRate(payFeeDetailPo.getPrimeRate());
        payFeeOrderPo.setReceivableAmount(payFeeDetailPo.getReceivableAmount());
        payFeeOrderPo.setDiscountAmount(payFeeDetailPo.getDiscountAmount());
        payFeeOrderPo.setRemark(payFeeDetailPo.getRemark());
        payFeeOrderPo.setReceivedAmount(payFeeDetailPo.getReceivedAmount());
        payFeeOrderPo.setOwnerId(feeDto.getOwnerId());
        payFeeOrderPo.setPayerObjName(feeDto.getPayerObjName());
        payFeeOrderPo.setExtOrderId(extPayOrderId);
        payFeeOrderPo.setOwnerName(feeDto.getOwnerName());
        payFeeOrderPo.setLateAmount(payFeeDetailPo.getLateAmount());
        payFeeOrderPo.setCashierId(payFeeDetailPo.getCashierId());
        payFeeOrderPo.setAcctAmount(payFeeDetailPo.getAcctAmount());
        payFeeOrderPo.setState(PayFeeOrderDto.STATE_NORMAL);
        payFeeOrderPo.setCommunityId(payFeeDetailPo.getCommunityId());
        payFeeOrderPo.setPayerObjType(feeDto.getPayerObjType());
        payFeeOrderPo.setCashierName(payFeeDetailPo.getCashierName());
        payFeeOrderPo.setPayerObjId(feeDto.getPayerObjId());
        payFeeOrderPo.setPrimeFile(primeFile);

        int flag = payFeeOrderV1InnerServiceSMOImpl.savePayFeeOrder(payFeeOrderPo);
        return flag;
    }

    @Override
    public int generatorPayFeeOrder(PayFeeDetailPo payFeeDetailPo, PayFeeDto feeDto, String extPayOrderId,String primeFile) {
        PayFeeOrderPo payFeeOrderPo = new PayFeeOrderPo();
        payFeeOrderPo.setGiftAmount(payFeeDetailPo.getGiftAmount());
        payFeeOrderPo.setOrderType(PayFeeOrderDto.ORDER_TYPE_PAY_FEE);
        payFeeOrderPo.setPayOrderId(payFeeDetailPo.getPayOrderId());
        payFeeOrderPo.setPrimeRate(payFeeDetailPo.getPrimeRate());
        payFeeOrderPo.setReceivableAmount(payFeeDetailPo.getReceivableAmount());
        payFeeOrderPo.setDiscountAmount(payFeeDetailPo.getDiscountAmount());
        payFeeOrderPo.setRemark(payFeeDetailPo.getRemark());
        payFeeOrderPo.setReceivedAmount(payFeeDetailPo.getReceivedAmount());
        payFeeOrderPo.setOwnerId(feeDto.getOwnerId());
        payFeeOrderPo.setPayerObjName(feeDto.getPayerObjName());
        payFeeOrderPo.setExtOrderId(extPayOrderId);
        payFeeOrderPo.setOwnerName(feeDto.getOwnerName());
        payFeeOrderPo.setLateAmount(payFeeDetailPo.getLateAmount());
        payFeeOrderPo.setCashierId(payFeeDetailPo.getCashierId());
        payFeeOrderPo.setAcctAmount(payFeeDetailPo.getAcctAmount());
        payFeeOrderPo.setState(PayFeeOrderDto.STATE_NORMAL);
        payFeeOrderPo.setCommunityId(payFeeDetailPo.getCommunityId());
        payFeeOrderPo.setPayerObjType(feeDto.getPayerObjType());
        payFeeOrderPo.setCashierName(payFeeDetailPo.getCashierName());
        payFeeOrderPo.setPayerObjId(feeDto.getPayerObjId());
        payFeeOrderPo.setPrimeFile(primeFile);

        int flag = payFeeOrderV1InnerServiceSMOImpl.savePayFeeOrder(payFeeOrderPo);
        return flag;
    }

    @Override
    public int generatorPayFeeOrder(List<BatchPayFeeOrder> batchPayFeeOrders, String extPayOrderId) {
        PayFeeDto feeDto = batchPayFeeOrders.get(0).getFeeDto();
        PayFeeDetailPo payFeeDetailPo = batchPayFeeOrders.get(0).getPayFeeDetailPo();
        BigDecimal giftAmount = new BigDecimal("0");
        BigDecimal receivableAmount = new BigDecimal("0");
        BigDecimal deductionAmount = new BigDecimal("0");
        BigDecimal receivedAmount = new BigDecimal("0");
        BigDecimal lateAmount = new BigDecimal("0");
        BigDecimal acctAmount = new BigDecimal("0");
        for (BatchPayFeeOrder batchPayFeeOrder : batchPayFeeOrders) {
            giftAmount = giftAmount.add(new BigDecimal(batchPayFeeOrder.getPayFeeDetailPo().getGiftAmount()));
            receivableAmount = receivableAmount.add(new BigDecimal(batchPayFeeOrder.getPayFeeDetailPo().getReceivableAmount()));
            deductionAmount = deductionAmount.add(new BigDecimal(batchPayFeeOrder.getPayFeeDetailPo().getDeductionAmount()));
            receivedAmount = receivedAmount.add(new BigDecimal(batchPayFeeOrder.getPayFeeDetailPo().getReceivedAmount()));
            lateAmount = lateAmount.add(new BigDecimal(batchPayFeeOrder.getPayFeeDetailPo().getLateAmount()));
            acctAmount = acctAmount.add(new BigDecimal(batchPayFeeOrder.getPayFeeDetailPo().getAcctAmount()));
        }

        PayFeeOrderPo payFeeOrderPo = new PayFeeOrderPo();
        payFeeOrderPo.setGiftAmount(giftAmount.doubleValue() + "");
        payFeeOrderPo.setOrderType(PayFeeOrderDto.ORDER_TYPE_PAY_FEE);
        payFeeOrderPo.setPayOrderId(payFeeDetailPo.getPayOrderId());
        payFeeOrderPo.setPrimeRate(payFeeDetailPo.getPrimeRate());
        payFeeOrderPo.setReceivableAmount(receivableAmount.doubleValue() + "");
        payFeeOrderPo.setDiscountAmount(deductionAmount.doubleValue() + "");
        payFeeOrderPo.setRemark(payFeeDetailPo.getRemark());
        payFeeOrderPo.setReceivedAmount(receivedAmount.doubleValue() + "");
        payFeeOrderPo.setOwnerId(feeDto.getOwnerId());
        payFeeOrderPo.setPayerObjName(feeDto.getPayerObjName());
        payFeeOrderPo.setExtOrderId(extPayOrderId);
        payFeeOrderPo.setOwnerName(feeDto.getOwnerName());
        payFeeOrderPo.setLateAmount(lateAmount.doubleValue() + "");
        payFeeOrderPo.setCashierId(payFeeDetailPo.getCashierId());
        payFeeOrderPo.setAcctAmount(acctAmount.doubleValue() + "");
        payFeeOrderPo.setState(PayFeeOrderDto.STATE_NORMAL);
        payFeeOrderPo.setCommunityId(payFeeDetailPo.getCommunityId());
        payFeeOrderPo.setPayerObjType(feeDto.getPayerObjType());
        payFeeOrderPo.setCashierName(payFeeDetailPo.getCashierName());
        payFeeOrderPo.setPayerObjId(feeDto.getPayerObjId());

        int flag = payFeeOrderV1InnerServiceSMOImpl.savePayFeeOrder(payFeeOrderPo);
        return flag;
    }

    @Override
    public int generatorPrePayFeeOrder(List<PrePayFeePo> prePayFeePos,String payerObjType) {
        if(ListUtil.isNull(prePayFeePos)){
            return 0;
        }
        PrePayFeePo prePayFeePo = prePayFeePos.get(0);
        BigDecimal giftAmount = new BigDecimal("0");
        BigDecimal receivableAmount = new BigDecimal("0");
        BigDecimal discountAmount = new BigDecimal("0");
        BigDecimal receivedAmount = new BigDecimal("0");
        BigDecimal lateAmount = new BigDecimal("0");
        BigDecimal acctAmount = new BigDecimal("0");
        for (PrePayFeePo tPrePayFeePo : prePayFeePos) {
            giftAmount = giftAmount.add(new BigDecimal(tPrePayFeePo.getGiftAmount()));
            receivableAmount = receivableAmount.add(new BigDecimal(tPrePayFeePo.getReceivableAmount()));
            discountAmount = discountAmount.add(new BigDecimal(tPrePayFeePo.getDiscountAmount()));
            receivedAmount = receivedAmount.add(new BigDecimal(tPrePayFeePo.getReceivedAmount()));
        }

        PayFeeOrderPo payFeeOrderPo = new PayFeeOrderPo();
        payFeeOrderPo.setGiftAmount(giftAmount.doubleValue() + "");
        payFeeOrderPo.setOrderType(PayFeeOrderDto.ORDER_TYPE_PRE_PAY_FEE);
        payFeeOrderPo.setPayOrderId(prePayFeePo.getPayOrderId());
        payFeeOrderPo.setPrimeRate(prePayFeePo.getPrimeRate());
        payFeeOrderPo.setReceivableAmount(receivableAmount.doubleValue() + "");
        payFeeOrderPo.setDiscountAmount(discountAmount.doubleValue() + "");
        payFeeOrderPo.setRemark(prePayFeePo.getRemark());
        payFeeOrderPo.setReceivedAmount(receivedAmount.doubleValue() + "");
        payFeeOrderPo.setOwnerId(prePayFeePo.getOwnerId());
        payFeeOrderPo.setPayerObjName(prePayFeePo.getPayerObjName());
        payFeeOrderPo.setExtOrderId("");
        payFeeOrderPo.setOwnerName(prePayFeePo.getOwnerName());
        payFeeOrderPo.setLateAmount(lateAmount.doubleValue() + "");
        payFeeOrderPo.setCashierId(prePayFeePo.getCashierId());
        payFeeOrderPo.setAcctAmount(acctAmount.doubleValue() + "");
        payFeeOrderPo.setState(PayFeeOrderDto.STATE_NORMAL);
        payFeeOrderPo.setCommunityId(prePayFeePo.getCommunityId());
        payFeeOrderPo.setPayerObjType(payerObjType);
        payFeeOrderPo.setCashierName(prePayFeePo.getCashierName());
        payFeeOrderPo.setPayerObjId(prePayFeePo.getPayerObjId());

        int flag = payFeeOrderV1InnerServiceSMOImpl.savePayFeeOrder(payFeeOrderPo);
        return flag;
    }

    @Override
    public int computePayFeeOrder(PayFeeOrderDto payFeeOrderDto) {

        PayFeeDetailDto payFeeDetailDto = new PayFeeDetailDto();
        payFeeDetailDto.setPayOrderId(payFeeOrderDto.getPayOrderId());
        payFeeDetailDto.setState(FeeDetailDto.STATE_NORMAL);
        List<PayFeeDetailDto> payFeeDetailDtos = payFeeDetailV1InnerServiceSMOImpl.queryPayFeeDetailSimple(payFeeDetailDto);
        if (ListUtil.isNull(payFeeDetailDtos)) {
            PayFeeOrderPo payFeeOrderPo = new PayFeeOrderPo();
            payFeeOrderPo.setGiftAmount("0");
            payFeeOrderPo.setReceivableAmount("0");
            payFeeOrderPo.setDiscountAmount("0");
            payFeeOrderPo.setReceivedAmount("0");
            payFeeOrderPo.setLateAmount("0");
            payFeeOrderPo.setAcctAmount("0");
            payFeeOrderPo.setPayOrderId(payFeeOrderDto.getPayOrderId());
            int flag = payFeeOrderV1InnerServiceSMOImpl.updatePayFeeOrder(payFeeOrderPo);
            return flag;
        }

        BigDecimal giftAmount = new BigDecimal("0");
        BigDecimal receivableAmount = new BigDecimal("0");
        BigDecimal discountAmount = new BigDecimal("0");
        BigDecimal receivedAmount = new BigDecimal("0");
        BigDecimal lateAmount = new BigDecimal("0");
        BigDecimal acctAmount = new BigDecimal("0");
        for (PayFeeDetailDto tPayFeeDetailDto : payFeeDetailDtos) {
            giftAmount = giftAmount.add(new BigDecimal(tPayFeeDetailDto.getGiftAmount()));
            receivableAmount = receivableAmount.add(new BigDecimal(tPayFeeDetailDto.getReceivableAmount()));
            discountAmount = discountAmount.add(new BigDecimal(tPayFeeDetailDto.getDiscountAmount()));
            receivedAmount = receivedAmount.add(new BigDecimal(tPayFeeDetailDto.getReceivedAmount()));
            lateAmount = lateAmount.add(new BigDecimal(tPayFeeDetailDto.getLateAmount()));
            acctAmount = acctAmount.add(new BigDecimal(tPayFeeDetailDto.getAcctAmount()));
        }

        PayFeeOrderPo payFeeOrderPo = new PayFeeOrderPo();
        payFeeOrderPo.setGiftAmount(giftAmount.doubleValue() + "");
        payFeeOrderPo.setReceivableAmount(receivableAmount.doubleValue() + "");
        payFeeOrderPo.setDiscountAmount(discountAmount.doubleValue() + "");
        payFeeOrderPo.setReceivedAmount(receivedAmount.doubleValue() + "");
        payFeeOrderPo.setLateAmount(lateAmount.doubleValue() + "");
        payFeeOrderPo.setAcctAmount(acctAmount.doubleValue() + "");
        payFeeOrderPo.setPayOrderId(payFeeOrderDto.getPayOrderId());

        int flag = payFeeOrderV1InnerServiceSMOImpl.updatePayFeeOrder(payFeeOrderPo);
        return flag;
    }

    @Override
    public int computePayFeeOrderPre(PayFeeOrderDto payFeeOrderDto) {
        PrePayFeeDto prePayFeeDto = new PrePayFeeDto();
        prePayFeeDto.setPayOrderId(payFeeOrderDto.getPayOrderId());
        prePayFeeDto.setState(PrePayFeeDto.STATE_NORMAL);
        List<PrePayFeeDto> prePayFeeDtos = prePayFeeV1InnerServiceSMOImpl.queryPrePayFees(prePayFeeDto);
        if (ListUtil.isNull(prePayFeeDtos)) {
            PayFeeOrderPo payFeeOrderPo = new PayFeeOrderPo();
            payFeeOrderPo.setGiftAmount("0");
            payFeeOrderPo.setReceivableAmount("0");
            payFeeOrderPo.setDiscountAmount("0");
            payFeeOrderPo.setReceivedAmount("0");
            payFeeOrderPo.setLateAmount("0");
            payFeeOrderPo.setAcctAmount("0");
            payFeeOrderPo.setPayOrderId(payFeeOrderDto.getPayOrderId());
            int flag = payFeeOrderV1InnerServiceSMOImpl.updatePayFeeOrder(payFeeOrderPo);
            return flag;
        }

        BigDecimal receivableAmount = new BigDecimal("0");
        BigDecimal discountAmount = new BigDecimal("0");
        BigDecimal receivedAmount = new BigDecimal("0");
        for (PrePayFeeDto tPrePayFeeDto : prePayFeeDtos) {
            receivableAmount = receivableAmount.add(new BigDecimal(tPrePayFeeDto.getReceivableAmount()));
            discountAmount = discountAmount.add(new BigDecimal(tPrePayFeeDto.getDiscountAmount()));
            receivedAmount = receivedAmount.add(new BigDecimal(tPrePayFeeDto.getReceivedAmount()));
        }

        PayFeeOrderPo payFeeOrderPo = new PayFeeOrderPo();
        payFeeOrderPo.setReceivableAmount(receivableAmount.doubleValue() + "");
        payFeeOrderPo.setDiscountAmount(discountAmount.doubleValue() + "");
        payFeeOrderPo.setReceivedAmount(receivedAmount.doubleValue() + "");
        payFeeOrderPo.setPayOrderId(payFeeOrderDto.getPayOrderId());

        int flag = payFeeOrderV1InnerServiceSMOImpl.updatePayFeeOrder(payFeeOrderPo);
        return flag;
    }


}
