package com.java110.fee.bill;

import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.fee.PayFeeDto;
import com.java110.dto.prePayFee.PrePayFeeDto;
import com.java110.dto.store.StoreUserDto;
import com.java110.fee.bmo.fee.IFinishFeeNotify;
import com.java110.fee.bmo.fee.IGeneratorPayFeeOrder;
import com.java110.intf.fee.*;
import com.java110.intf.store.IStoreUserV1InnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.prePayFee.PrePayFeePo;
import com.java110.po.prePayFeeDetail.PrePayFeeDetailPo;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DeductionPrePayFeeImpl implements IDeductionPrePayFee {

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    @Autowired
    private IStoreUserV1InnerServiceSMO storeUserV1InnerServiceSMOImpl;

    @Autowired
    private IFeeReceiptInnerServiceSMO feeReceiptInnerServiceSMOImpl;

    @Autowired
    private IPrePayFeeV1InnerServiceSMO prePayFeeV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeDetailV1InnerServiceSMO payFeeDetailNewV1InnerServiceSMOImpl;

    @Autowired
    private IPrePayFeeDetailV1InnerServiceSMO prePayFeeDetailV1InnerServiceSMOImpl;

    @Autowired
    private IFinishFeeNotify finishFeeNotifyImpl;

    @Autowired
    private IGeneratorPayFeeOrder generatorPayFeeOrderImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Override
    public int deductionPrePayFee(String ppfId) {
        PrePayFeeDto tPrePayFeeDto = new PrePayFeeDto();
        tPrePayFeeDto.setPpfId(ppfId);
        tPrePayFeeDto.setHasMoney("Y");
        List<PrePayFeeDto> prePayFeeDtos = prePayFeeV1InnerServiceSMOImpl.queryPrePayFees(tPrePayFeeDto);
        if (ListUtil.isNull(prePayFeeDtos)) {
            return -1;
        }
        return deductionPrePayFee(prePayFeeDtos.get(0));
    }

    @Override
    //@Java110Transactional
    public int deductionPrePayFee(PrePayFeeDto prePayFeeDto) {
        // 查询业主的费用
        PayFeeDto payFeeDto = new PayFeeDto();
        payFeeDto.setCommunityId(prePayFeeDto.getCommunityId());
        if (!"-1".equals(prePayFeeDto.getRuleId())) {
            payFeeDto.setRuleId(prePayFeeDto.getRuleId());
        }
        payFeeDto.setFtId(prePayFeeDto.getFtId());
        payFeeDto.setPayerObjId(prePayFeeDto.getPayerObjId());
        payFeeDto.setState(FeeDto.STATE_DOING);
        List<PayFeeDto> payFeeDtos = payFeeV1InnerServiceSMOImpl.queryPayFees(payFeeDto);
        if (ListUtil.isNull(payFeeDtos)) {
            return -1;
        }
        StoreUserDto storeUserDto = new StoreUserDto();
        storeUserDto.setStoreId(payFeeDtos.get(0).getIncomeObjId());
        storeUserDto.setRelCd(StoreUserDto.REL_CD_MANAGER);
        List<StoreUserDto> storeUserDtos = storeUserV1InnerServiceSMOImpl.queryStoreUsers(storeUserDto);

        Assert.listOnlyOne(storeUserDtos, "没有扣款的用户");


        //todo 生成收据编号
        //String receiptCode = feeReceiptInnerServiceSMOImpl.generatorReceiptCode(prePayFeeDto.getCommunityId());
        String receiptCode = "0";
        for (PayFeeDto tPayFeeDto : payFeeDtos) {
            // 开始时间不在付款时间内 不允许抵扣
            if (!DateUtil.belongCalendarA(tPayFeeDto.getStartTime(), prePayFeeDto.getStartTime(), prePayFeeDto.getEndTime())) {
                continue;
            }
            doPayFee(tPayFeeDto, prePayFeeDto, storeUserDtos.get(0), receiptCode);
        }
        return 1;
    }


    private void doPayFee(PayFeeDto tPayFeeDto, PrePayFeeDto prePayFeeDto, StoreUserDto storeUserDto, String receiptCode) {


        PrePayFeeDto tPrePayFeeDto = new PrePayFeeDto();
        tPrePayFeeDto.setPpfId(prePayFeeDto.getPpfId());
        tPrePayFeeDto.setHasMoney("Y");
        List<PrePayFeeDto> prePayFeeDtos = prePayFeeV1InnerServiceSMOImpl.queryPrePayFees(tPrePayFeeDto);
        if (ListUtil.isNull(prePayFeeDtos)) {
            return;
        }
        BigDecimal oweAmountDec = new BigDecimal(tPayFeeDto.getOweAmount());
        // 优惠金额
        BigDecimal discountAmountDec = new BigDecimal(prePayFeeDtos.get(0).getDiscountAmount());
        // 剩余金额
        BigDecimal remainAmountDec = new BigDecimal(prePayFeeDtos.get(0).getRemainAmount());

        double receivedAmount = 0.00;
        double discountAmount = 0.00;

        //预存优惠
        double preDiscountAmount = discountAmountDec.doubleValue();
        double remainAmount = remainAmountDec.doubleValue();

        if (discountAmountDec.doubleValue() >= oweAmountDec.doubleValue()) { // 如果优惠够
            receivedAmount = 0.00;
            discountAmount = oweAmountDec.doubleValue();
            preDiscountAmount = discountAmountDec.subtract(oweAmountDec).doubleValue();
        } else {
            BigDecimal preAllRemainAmount = discountAmountDec.add(remainAmountDec);
            if (remainAmountDec.doubleValue() >= oweAmountDec.doubleValue()) { // 如果预付金额够
                receivedAmount = oweAmountDec.doubleValue();
                discountAmount = 0;
                preDiscountAmount = 0;
                remainAmount = remainAmountDec.subtract(oweAmountDec).doubleValue();
            } else if (preAllRemainAmount.doubleValue() > oweAmountDec.doubleValue()) { // 如果预付款和优惠都够,但是优惠完全抵扣不够
                receivedAmount = oweAmountDec.subtract(discountAmountDec).doubleValue();// 欠费减去优惠
                discountAmount = discountAmountDec.doubleValue();
                preDiscountAmount = 0;
                remainAmount = preAllRemainAmount.subtract(oweAmountDec).doubleValue();
            } else if (preAllRemainAmount.doubleValue() < oweAmountDec.doubleValue()) { // 预付款和优惠 不够
                receivedAmount = remainAmountDec.doubleValue();
                discountAmount = discountAmountDec.doubleValue();
                preDiscountAmount = 0;
                remainAmount = 0;
            } else {
                return;
            }
        }


        PayFeeDetailPo payFeeDetailPo = new PayFeeDetailPo();
        payFeeDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId("11"));
        payFeeDetailPo.setFeeId(tPayFeeDto.getFeeId());
        payFeeDetailPo.setCommunityId(tPayFeeDto.getCommunityId());
        payFeeDetailPo.setCycles("1");
        payFeeDetailPo.setReceivableAmount(tPayFeeDto.getOweAmount());
        payFeeDetailPo.setReceivedAmount(receivedAmount + "");
        payFeeDetailPo.setPrimeRate(FeeDetailDto.PRIME_REATE_DEDUCTION);
        payFeeDetailPo.setRemark("从预付款扣款缴费");
        payFeeDetailPo.setState(FeeDetailDto.STATE_NORMAL);
        payFeeDetailPo.setStartTime(tPayFeeDto.getStartTime());
        payFeeDetailPo.setEndTime(tPayFeeDto.getEndTime());
        payFeeDetailPo.setCreateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        payFeeDetailPo.setPayableAmount(tPayFeeDto.getOweAmount());
        payFeeDetailPo.setPayOrderId(GenerateCodeFactory.getGeneratorId("11"));
        payFeeDetailPo.setCashierId(storeUserDto.getUserId());
        payFeeDetailPo.setCashierName(storeUserDto.getName());
        payFeeDetailPo.setOpenInvoice("Y");
        payFeeDetailPo.setAcctAmount("0");
        payFeeDetailPo.setDiscountAmount(discountAmount + "");
        payFeeDetailPo.setDeductionAmount("0");
        payFeeDetailPo.setLateAmount("0");
        payFeeDetailPo.setGiftAmount("0");
        FeeDto feeDto = new FeeDto();
        feeDto.setFeeId(tPayFeeDto.getFeeId());
        feeDto.setCommunityId(tPayFeeDto.getCommunityId());
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
        if (!ListUtil.isNull(feeDtos) && feeDtos.get(0).getFeeTime() != null) {
            payFeeDetailPo.setFeeTime(DateUtil.getFormatTimeStringB(feeDtos.get(0).getFeeTime()));
        }

        CommonCache.setValue(payFeeDetailPo.getDetailId() + CommonCache.RECEIPT_CODE, receiptCode, CommonCache.DEFAULT_EXPIRETIME_TWO_MIN);
        int flag = payFeeDetailNewV1InnerServiceSMOImpl.savePayFeeDetailNew(payFeeDetailPo);
        if (flag < 1) {
            throw new CmdException("缴费失败");
        }

        PayFeePo payFeePo = new PayFeePo();
        payFeePo.setFeeId(payFeeDetailPo.getFeeId());
        payFeePo.setOweAmount("0");
        payFeePo.setState(FeeDto.STATE_FINISH);
        BigDecimal receivableAmountDec = new BigDecimal(payFeeDetailPo.getReceivableAmount());
        BigDecimal receivedAmountDec = new BigDecimal(payFeeDetailPo.getReceivedAmount());
        BigDecimal acctAmountDec = new BigDecimal(payFeeDetailPo.getAcctAmount());
        discountAmountDec = new BigDecimal(payFeeDetailPo.getDiscountAmount());
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
        //生成订单
        generatorPayFeeOrderImpl.generatorPayFeeOrder(payFeeDetailPo, tPayFeeDto, "-1", "");

        BigDecimal curAmount = remainAmountDec.subtract(new BigDecimal(remainAmount + ""));


        PrePayFeeDetailPo prePayFeeDetailPo = new PrePayFeeDetailPo();
        prePayFeeDetailPo.setFeeTypeCd(tPayFeeDto.getFeeTypeCd());
        prePayFeeDetailPo.setCurYearMonth(tPayFeeDto.getStartTime());
        prePayFeeDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId("11"));
        prePayFeeDetailPo.setRemark("系统自动扣款");
        prePayFeeDetailPo.setOwnerId(prePayFeeDtos.get(0).getOwnerId());
        prePayFeeDetailPo.setFtName(prePayFeeDtos.get(0).getFtName());
        prePayFeeDetailPo.setFeeId(tPayFeeDto.getFeeId());
        prePayFeeDetailPo.setPreAmount(prePayFeeDtos.get(0).getRemainAmount());
        prePayFeeDetailPo.setPayerObjName(tPayFeeDto.getPayerObjName());
        prePayFeeDetailPo.setOwnerName(tPayFeeDto.getOwnerName());
        prePayFeeDetailPo.setRemainAmount(remainAmount + "");
        prePayFeeDetailPo.setHisOweAmount("0");
        prePayFeeDetailPo.setFtId(prePayFeeDtos.get(0).getFtId());
        prePayFeeDetailPo.setCommunityId(prePayFeeDtos.get(0).getCommunityId());
        prePayFeeDetailPo.setCurAmount(curAmount.doubleValue() + "");
        prePayFeeDetailPo.setCurOweAmount("0");
        prePayFeeDetailPo.setPpfId(prePayFeeDtos.get(0).getPpfId());
        prePayFeeDetailPo.setPayerObjId(prePayFeeDtos.get(0).getPayerObjId());
        prePayFeeDetailV1InnerServiceSMOImpl.savePrePayFeeDetail(prePayFeeDetailPo);

        PrePayFeePo prePayFeePo = new PrePayFeePo();
        prePayFeePo.setPpfId(prePayFeeDtos.get(0).getPpfId());
        prePayFeePo.setRemainAmount(remainAmount + "");
        prePayFeePo.setDiscountAmount(preDiscountAmount + "");
        prePayFeeV1InnerServiceSMOImpl.updatePrePayFee(prePayFeePo);

        //todo 修改车辆
        finishFeeNotifyImpl.updateCarEndTime(payFeeDetailPo.getFeeId(), payFeeDetailPo.getCommunityId());
        //todo 租金延期房屋结束时间
        finishFeeNotifyImpl.updateRoomEndTime(payFeeDetailPo.getFeeId(), payFeeDetailPo.getCommunityId());

        finishFeeNotifyImpl.updateRepair(payFeeDetailPo.getFeeId(), payFeeDetailPo.getCommunityId(), payFeeDetailPo.getReceivedAmount());


    }

    @Override
    public int deductionAmount(String ppfId, double amount, String feeId) {
        PrePayFeeDto tPrePayFeeDto = new PrePayFeeDto();
        tPrePayFeeDto.setPpfId(ppfId);
        tPrePayFeeDto.setHasMoney("Y");
        List<PrePayFeeDto> prePayFeeDtos = prePayFeeV1InnerServiceSMOImpl.queryPrePayFees(tPrePayFeeDto);
        if (ListUtil.isNull(prePayFeeDtos)) {
            throw new IllegalArgumentException("余额不足");
        }

        BigDecimal remainAmountDec = new BigDecimal(prePayFeeDtos.get(0).getRemainAmount());


        BigDecimal curAmount = remainAmountDec.subtract(new BigDecimal(amount + ""));

        if (curAmount.doubleValue() < 0) {
            throw new IllegalArgumentException("余额不足");
        }
        PayFeeDto payFeeDto = new PayFeeDto();
        payFeeDto.setFeeId(feeId);
        payFeeDto.setCommunityId(prePayFeeDtos.get(0).getCommunityId());
        List<PayFeeDto> payFeeDtos = payFeeV1InnerServiceSMOImpl.queryPayFees(payFeeDto);
        if (ListUtil.isNull(payFeeDtos)) {
            throw new IllegalArgumentException("费用不存在");
        }


        PrePayFeeDetailPo prePayFeeDetailPo = new PrePayFeeDetailPo();
        prePayFeeDetailPo.setFeeTypeCd(payFeeDtos.get(0).getFeeTypeCd());
        prePayFeeDetailPo.setCurYearMonth(prePayFeeDtos.get(0).getStartTime());
        prePayFeeDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId("11"));
        prePayFeeDetailPo.setRemark("手动扣款");
        prePayFeeDetailPo.setOwnerId(prePayFeeDtos.get(0).getOwnerId());
        prePayFeeDetailPo.setFtName(prePayFeeDtos.get(0).getFtName());
        prePayFeeDetailPo.setFeeId(feeId);
        prePayFeeDetailPo.setPreAmount(prePayFeeDtos.get(0).getRemainAmount());
        prePayFeeDetailPo.setPayerObjName(prePayFeeDtos.get(0).getPayerObjName());
        prePayFeeDetailPo.setOwnerName(prePayFeeDtos.get(0).getOwnerName());
        prePayFeeDetailPo.setRemainAmount(curAmount.doubleValue() + "");
        prePayFeeDetailPo.setHisOweAmount("0");
        prePayFeeDetailPo.setFtId(prePayFeeDtos.get(0).getFtId());
        prePayFeeDetailPo.setCommunityId(prePayFeeDtos.get(0).getCommunityId());
        prePayFeeDetailPo.setCurAmount(amount + "");
        prePayFeeDetailPo.setCurOweAmount("0");
        prePayFeeDetailPo.setPpfId(prePayFeeDtos.get(0).getPpfId());
        prePayFeeDetailPo.setPayerObjId(prePayFeeDtos.get(0).getPayerObjId());
        prePayFeeDetailV1InnerServiceSMOImpl.savePrePayFeeDetail(prePayFeeDetailPo);

        PrePayFeePo prePayFeePo = new PrePayFeePo();
        prePayFeePo.setPpfId(prePayFeeDtos.get(0).getPpfId());
        prePayFeePo.setRemainAmount(curAmount.doubleValue() + "");
        prePayFeeV1InnerServiceSMOImpl.updatePrePayFee(prePayFeePo);
        return 0;
    }

}
