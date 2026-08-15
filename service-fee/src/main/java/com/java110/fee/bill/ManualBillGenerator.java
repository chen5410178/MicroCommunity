package com.java110.fee.bill;

import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.fee.FeeAmountDto;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.payFee.PayFeeDetailRefreshFeeMonthDto;
import com.java110.dto.prePayFee.PrePayFeeDto;
import com.java110.intf.fee.*;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.payFee.PayFeeRulePo;
import com.java110.utils.util.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ManualBillGenerator {

    private static Logger logger = LoggerFactory.getLogger(ManualBillGenerator.class);

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IPayFeeConfigV1InnerServiceSMO payFeeConfigV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeRuleV1InnerServiceSMO payFeeRuleV1InnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IPayFeeMonthInnerServiceSMO payFeeMonthInnerServiceSMOImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    @Autowired
    private IComputeBillPrice computeBillPriceImpl;

    @Autowired
    private IPrePayFeeV1InnerServiceSMO prePayFeeV1InnerServiceSMOImpl;

    @Autowired
    private IDeductionPrePayFee deductionPrePayFeeImpl;

    /**
     * 手动强制生成账单（仅周期性费用）
     * @param payFeeRulePo 来源规则
     * @param targetMonth 目标月份，如 "2027-03"
     * @param prepaymentPeriod 生成期数
     * @return 生成的账单数
     */
    public int manualGeneratePayFee(PayFeeRulePo payFeeRulePo, String targetMonth, int prepaymentPeriod) {
        if (!FeeDto.FEE_FLAG_CYCLE.equals(payFeeRulePo.getFeeFlag())) {
            throw new IllegalArgumentException("手动生成仅支持周期性费用，当前费用标识：" + payFeeRulePo.getFeeFlag());
        }

        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setConfigId(payFeeRulePo.getConfigId());
        feeConfigDto.setCommunityId(payFeeRulePo.getCommunityId());
        List<FeeConfigDto> feeConfigDtos = payFeeConfigV1InnerServiceSMOImpl.queryPayFeeConfigs(feeConfigDto);
        Assert.listOnlyOne(feeConfigDtos, "费用项不存在，configId=" + payFeeRulePo.getConfigId());

        int ruleCycle = Integer.parseInt(feeConfigDtos.get(0).getPaymentCycle());
        if (prepaymentPeriod <= 0) {
            prepaymentPeriod = 1;
        }

        Date targetEndTime = computeTargetEndTimeForManual(payFeeRulePo, targetMonth, prepaymentPeriod, feeConfigDtos.get(0));

        List<PayFeePo> tmpPayFeePos = new ArrayList<>();
        List<FeeAttrPo> tmpFeeAttrPos = new ArrayList<>();

        FeeDto feeDto = new FeeDto();
        feeDto.setPayerObjType(payFeeRulePo.getPayerObjType());
        feeDto.setCommunityId(payFeeRulePo.getCommunityId());
        feeDto.setPayerObjId(payFeeRulePo.getPayerObjId());
        OwnerDto ownerDto = computeFeeSMOImpl.getFeeOwnerDto(feeDto);
        String payerObjName = computeFeeSMOImpl.getFeeObjName(feeDto);

        Date startTime = DateUtil.getDateFromStringB(payFeeRulePo.getCurYearMonth());
        Date endTime = null;

        if (FeeConfigDto.PAYMENT_CD_AFTER.equals(feeConfigDtos.get(0).getPaymentCd())) {
            startTime = DateUtil.getNextMonthDate(startTime, ruleCycle * -1);
        }

        do {
            endTime = DateUtil.getNextMonthDate(startTime, ruleCycle);
            if (endTime.getTime() >= targetEndTime.getTime()) {
                endTime = targetEndTime;
            }
            doGeneratorPayFee(payFeeRulePo, startTime, endTime,
                    feeConfigDtos.get(0),
                    tmpPayFeePos,
                    tmpFeeAttrPos,
                    ownerDto,
                    payerObjName,
                    ruleCycle);
            startTime = endTime;
        }
        while (endTime.getTime() < targetEndTime.getTime());

        int saveFlag = 0;
        if (!tmpPayFeePos.isEmpty()) {
            saveFlag = saveFeeAndAttrs(tmpPayFeePos, tmpFeeAttrPos);
        }

        PayFeeRulePo updateRulePo = new PayFeeRulePo();
        updateRulePo.setRuleId(payFeeRulePo.getRuleId());
        updateRulePo.setCurYearMonth(DateUtil.getFormatTimeStringB(endTime));
        if (FeeConfigDto.PAYMENT_CD_AFTER.equals(feeConfigDtos.get(0).getPaymentCd())) {
            endTime = DateUtil.getNextMonthDate(endTime, ruleCycle);
            updateRulePo.setCurYearMonth(DateUtil.getFormatTimeStringB(endTime));
        }
        updateRulePo.setCommunityId(payFeeRulePo.getCommunityId());
        if (!endTime.before(DateUtil.getDateFromStringB(payFeeRulePo.getMaxTime()))) {
            updateRulePo.setState(FeeDto.STATE_FINISH);
        }
        payFeeRuleV1InnerServiceSMOImpl.updatePayFeeRule(updateRulePo);

        PrePayFeeDto prePayFeeDto = new PrePayFeeDto();
        prePayFeeDto.setCommunityId(payFeeRulePo.getCommunityId());
        prePayFeeDto.setFtId(feeConfigDtos.get(0).getFtId());
        prePayFeeDto.setRuleId(payFeeRulePo.getRuleId());
        if (FeeConfigDto.FEE_TYPE_CD_WATER.equals(feeConfigDtos.get(0).getFeeTypeCd())
                || FeeConfigDto.FEE_TYPE_CD_METER.equals(feeConfigDtos.get(0).getFeeTypeCd())
                || FeeConfigDto.FEE_TYPE_CD_GAS.equals(feeConfigDtos.get(0).getFeeTypeCd())) {
            prePayFeeDto.setRuleId("");
        }
        prePayFeeDto.setHasMoney("1");
        List<PrePayFeeDto> prePayFeeDtos = prePayFeeV1InnerServiceSMOImpl.queryPrePayFees(prePayFeeDto);

        if (!ListUtil.isNull(prePayFeeDtos)) {
            for (PrePayFeeDto tPrePayFeeDto : prePayFeeDtos) {
                try {
                    deductionPrePayFeeImpl.deductionPrePayFee(tPrePayFeeDto);
                } catch (Exception e) {
                    logger.error("预付费扣款失败", e);
                }
            }
        }

        return saveFlag;
    }

    private void doGeneratorPayFee(PayFeeRulePo tmpPayFeeRulePo, Date startTime, Date endTime, FeeConfigDto feeConfigDto,
                                    List<PayFeePo> tmpPayFeePos, List<FeeAttrPo> tmpFeeAttrPos,
                                    OwnerDto ownerDto,
                                    String payerObjName,
                                    int ruleCycle) {

        PayFeePo payFeePo = BeanConvertUtil.covertBean(tmpPayFeeRulePo, PayFeePo.class);
        payFeePo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
        payFeePo.setFeeFlag(FeeDto.FEE_FLAG_ONCE);
        payFeePo.setStartTime(DateUtil.getFormatTimeStringB(startTime));
        payFeePo.setRuleId(tmpPayFeeRulePo.getRuleId());
        payFeePo.setPayerObjName(payerObjName);
        FeeAmountDto feeAmountDto = computeBillPriceImpl.computeBillReceiveAmount(tmpPayFeeRulePo.getRuleId(), ruleCycle);
        payFeePo.setReceivableAmount(feeAmountDto.getAmount() + "");
        payFeePo.setOweAmount(feeAmountDto.getAmount() + "");
        payFeePo.setRemark(feeAmountDto.getRemark());
        payFeePo.setFeeRemark(tmpPayFeeRulePo.getFeeRemark());
        Date feeTime = startTime;
        String deductFrom = feeConfigDto.getDeductFrom();
        if (FeeDto.DEDUCT_FROM_NEXT_MONTH.equals(deductFrom)) {
            feeTime = DateUtil.getNextMonthDate(startTime, 1);
        } else if (FeeDto.DEDUCT_FROM_HISTORY_MONTH.equals(deductFrom)) {
            feeTime = DateUtil.getNextMonthDate(startTime, -1);
        }
        payFeePo.setFeeTime(DateUtil.getFormatTimeStringB(feeTime));

        if (FeeDto.FEE_FLAG_CYCLE.equals(tmpPayFeeRulePo.getFeeFlag())) {
            payFeePo.setEndTime(DateUtil.getPreSecTime(endTime));
        } else {
            payFeePo.setEndTime(DateUtil.getFormatTimeStringA(endTime));
        }

        if (ownerDto != null) {
            payFeePo.setOwnerId(ownerDto.getOwnerId());
            payFeePo.setOwnerName(ownerDto.getName());
            tmpFeeAttrPos.add(addFeeAttr(payFeePo, FeeAttrDto.SPEC_CD_OWNER_NAME, ownerDto.getName()));
        }
        tmpPayFeePos.add(payFeePo);
    }

    private FeeAttrPo addFeeAttr(PayFeePo payFeePo, String specCd, String value) {
        FeeAttrPo feeAttrPo = new FeeAttrPo();
        feeAttrPo.setCommunityId(payFeePo.getCommunityId());
        feeAttrPo.setSpecCd(specCd);
        feeAttrPo.setValue(value);
        feeAttrPo.setFeeId(payFeePo.getFeeId());
        feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId, true));
        return feeAttrPo;
    }

    private int saveFeeAndAttrs(List<PayFeePo> feePos, List<FeeAttrPo> feeAttrsPos) {
        if (feePos == null || feePos.isEmpty()) {
            return 1;
        }
        int flag = feeInnerServiceSMOImpl.saveFee(feePos);
        if (flag < 1) {
            return flag;
        }

        flag = feeAttrInnerServiceSMOImpl.saveFeeAttrs(feeAttrsPos);

        List<String> feeIds = new ArrayList<>();
        for (PayFeePo feePo : feePos) {
            feeIds.add(feePo.getFeeId());
        }

        PayFeeDetailRefreshFeeMonthDto payFeeDetailRefreshFeeMonthDto = new PayFeeDetailRefreshFeeMonthDto();
        payFeeDetailRefreshFeeMonthDto.setCommunityId(feePos.get(0).getCommunityId());
        payFeeDetailRefreshFeeMonthDto.setFeeIds(feeIds);

        payFeeMonthInnerServiceSMOImpl.doGeneratorFeeMonths(payFeeDetailRefreshFeeMonthDto);
        payFeeMonthInnerServiceSMOImpl.doGeneratorOweFees(payFeeDetailRefreshFeeMonthDto);
        return flag;
    }

    private Date computeTargetEndTimeForManual(PayFeeRulePo tmpPayFeeRulePo, String targetMonth, int prepaymentPeriod, FeeConfigDto feeConfigDto) {
        long paymentCycle = Long.parseLong(feeConfigDto.getPaymentCycle());

        Date endDate = DateUtil.getDateFromStringB(tmpPayFeeRulePo.getEndTime());
        String maxTime = tmpPayFeeRulePo.getMaxTime();
        if (maxTime.contains("23:59:59")) {
            maxTime = DateUtil.getNextSecTime(maxTime);
        }
        Date maxEndTime = DateUtil.getDateFromStringB(maxTime);

        // 按 prepaymentPeriod * paymentCycle 计算目标截止时间
        int generateCycles = prepaymentPeriod;
        if (generateCycles == 1231) {
            // 特殊处理：1231（到当年12月）换算为当年的实际期数 = 12 - 当前月份 + 1
            Calendar calendar = Calendar.getInstance();
            int curMonth = calendar.get(Calendar.MONTH) + 1;   // 1~12
            generateCycles = 12 - curMonth + 1;
        }
        Date targetEndDate = DateUtil.getTargetEndTime(generateCycles * paymentCycle, endDate);

        // 确保至少覆盖到用户选择的目标月份
        Date targetMonthDate = DateUtil.getDateFromStringB(targetMonth + "-01");
        if (targetMonthDate.getTime() > targetEndDate.getTime()) {
            targetEndDate = DateUtil.getNextMonthDate(targetMonthDate, 1);
        }

        if (maxEndTime.getTime() < targetEndDate.getTime()) {
            targetEndDate = maxEndTime;
        }

        if (DateUtil.getDateFromStringB(tmpPayFeeRulePo.getEndTime()).getTime() > targetEndDate.getTime()) {
            targetEndDate = DateUtil.getDateFromStringB(tmpPayFeeRulePo.getEndTime());
        }

        return targetEndDate;
    }
}
