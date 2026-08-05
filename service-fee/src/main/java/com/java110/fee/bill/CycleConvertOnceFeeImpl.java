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
import com.java110.dto.payFee.PayFeeRuleDto;
import com.java110.dto.prePayFee.PrePayFeeDto;
import com.java110.fee.dao.impl.PayFeeV1ServiceDaoImpl;
import com.java110.intf.fee.*;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.payFee.PayFeeRulePo;
import com.java110.po.payFee.PayFeeRuleBillPo;
import com.java110.utils.util.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 转换一次性费用
 */

@Service
public class CycleConvertOnceFeeImpl implements ICycleConvertOnceFee {

    private static Logger logger = LoggerFactory.getLogger(PayFeeV1ServiceDaoImpl.class);


    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IPayFeeConfigV1InnerServiceSMO payFeeConfigV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeRuleV1InnerServiceSMO payFeeRuleV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IPayFeeMonthInnerServiceSMO payFeeMonthInnerServiceSMOImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    @Autowired
    private IPayFeeRuleBillV1InnerServiceSMO payFeeRuleBillV1InnerServiceSMOImpl;

    @Autowired
    private IComputeBillPrice computeBillPriceImpl;

    @Autowired
    private IPrePayFeeV1InnerServiceSMO prePayFeeV1InnerServiceSMOImpl;

    @Autowired
    private IDeductionPrePayFee deductionPrePayFeeImpl;

    @Override
    public int convertPayFees(List<PayFeePo> payFeePos) {

        if (payFeePos.isEmpty()) {
            return 0;
        }

        //todo 业务处理

        List<PayFeeRulePo> payFeeRulePos = new ArrayList<>();

        PayFeeRulePo tmpPayFeeRulePo = null;
        String curYearMonth = "";
        for (PayFeePo tmpPayFeePo : payFeePos) {
            tmpPayFeeRulePo = BeanConvertUtil.covertBean(tmpPayFeePo, PayFeeRulePo.class);
            tmpPayFeeRulePo.setRuleId(GenerateCodeFactory.getGeneratorId("11"));
            curYearMonth = DateUtil.getFormatTimeStringB(DateUtil.getDateFromStringB(tmpPayFeePo.getEndTime()));
            tmpPayFeeRulePo.setCurYearMonth(curYearMonth);
            payFeeRulePos.add(tmpPayFeeRulePo);
        }

        if (payFeeRulePos.isEmpty()) {
            return 0;
        }

        //todo 保存规则
        payFeeRuleV1InnerServiceSMOImpl.savePayFeeRules(payFeeRulePos);

        //todo 根据规则生成费用
        rulesGeneratePayFees(payFeeRulePos);

        return 0;
    }

    @Override
    public int convertPayFee(PayFeePo payFeePo) {
        List<PayFeePo> payFeePos = new ArrayList<>();
        payFeePos.add(payFeePo);
        return convertPayFees(payFeePos);
    }

    @Override
    public int covertCommunityPayFee(String communityId) {
        PayFeeRuleDto payFeeRuleDto = new PayFeeRuleDto();
        payFeeRuleDto.setCommunityId(communityId);
        payFeeRuleDto.setFeeFlag(FeeDto.FEE_FLAG_CYCLE);
        payFeeRuleDto.setState(FeeDto.STATE_DOING);
        payFeeRuleDto.setValid("Y"); // 只差账单日小于等于当前时间的

        List<PayFeeRuleDto> payFeeRuleDtos = payFeeRuleV1InnerServiceSMOImpl.queryPayFeeRules(payFeeRuleDto);
        if (ListUtil.isNull(payFeeRuleDtos)) {
            return 0;
        }

        List<PayFeeRulePo> tmpPayFeeRulePos = BeanConvertUtil.covertBeanList(payFeeRuleDtos, PayFeeRulePo.class);
        return rulesGeneratePayFees(tmpPayFeeRulePos);
    }

    @Override
    public int covertRuleIdsPayFee(List<String> ruleIds) {
        PayFeeRuleDto payFeeRuleDto = new PayFeeRuleDto();
        payFeeRuleDto.setRuleIds(ruleIds.toArray(new String[ruleIds.size()]));
        List<PayFeeRuleDto> payFeeRuleDtos = payFeeRuleV1InnerServiceSMOImpl.queryPayFeeRules(payFeeRuleDto);
        if (payFeeRuleDtos == null || payFeeRuleDtos.isEmpty()) {
            return 0;
        }

        List<PayFeeRulePo> tmpPayFeeRulePos = BeanConvertUtil.covertBeanList(payFeeRuleDtos, PayFeeRulePo.class);
        return rulesGeneratePayFees(tmpPayFeeRulePos);
    }

    @Override
    public int rulesGeneratePayFees(List<PayFeeRulePo> payFeeRulePos) {

        if (ListUtil.isNull(payFeeRulePos)) {
            return 0;
        }

        for (PayFeeRulePo tmpPayFeeRulePo : payFeeRulePos) {
            try {
                //todo 单个费用处理
                ruleGeneratePayFee(tmpPayFeeRulePo);
            } catch (Exception e) {
                logger.error("处理异常 ruleId" + tmpPayFeeRulePo.getRuleId(), e);
                throw e;
            }
        }
        return payFeeRulePos.size();
    }

    /**
     * 规则生成费用
     *
     * @param tmpPayFeeRulePo
     */
    public int ruleGeneratePayFee(PayFeeRulePo tmpPayFeeRulePo) {
        int ruleCycle = 1;


        //todo 查询费用项信息
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setConfigId(tmpPayFeeRulePo.getConfigId());
        feeConfigDto.setCommunityId(tmpPayFeeRulePo.getCommunityId());
        List<FeeConfigDto> feeConfigDtos = payFeeConfigV1InnerServiceSMOImpl.queryPayFeeConfigs(feeConfigDto);

        Assert.listOnlyOne(feeConfigDtos, "费用项不存在，configId=" + tmpPayFeeRulePo.getConfigId());
        ruleCycle = Integer.parseInt(feeConfigDtos.get(0).getPaymentCycle());
        // 一次性周期为1
        if (FeeDto.FEE_FLAG_ONCE.equals(feeConfigDtos.get(0).getFeeFlag())) {
            ruleCycle = 1;
        }


        //todo 计算目标 结束时间
        Date targetEndTime = computeTargetEndTime(tmpPayFeeRulePo, feeConfigDtos.get(0));

        //todo 创建 pay_fee 和 attrs 数据
        List<PayFeeRuleBillPo> payFeeRuleBillPos = new ArrayList<>();
        List<PayFeePo> tmpPayFeePos = new ArrayList<>();
        List<FeeAttrPo> tmpFeeAttrPos = new ArrayList<>();

        FeeDto feeDto = new FeeDto();
        feeDto.setPayerObjType(tmpPayFeeRulePo.getPayerObjType());
        feeDto.setCommunityId(tmpPayFeeRulePo.getCommunityId());
        feeDto.setPayerObjId(tmpPayFeeRulePo.getPayerObjId());
        OwnerDto ownerDto = computeFeeSMOImpl.getFeeOwnerDto(feeDto);
        String payerObjName = computeFeeSMOImpl.getFeeObjName(feeDto);

        Date startTime = DateUtil.getDateFromStringB(tmpPayFeeRulePo.getCurYearMonth());
        Date endTime = null;

        // 如果是后付费，startTime 要减去一个周期时间
        if (FeeConfigDto.PAYMENT_CD_AFTER.equals(feeConfigDtos.get(0).getPaymentCd()) && FeeDto.FEE_FLAG_CYCLE.equals(tmpPayFeeRulePo.getFeeFlag())) {
            startTime = DateUtil.getNextMonthDate(startTime, ruleCycle * -1);
        }

        //todo 没到时间
        if (startTime.getTime() >= targetEndTime.getTime()) {
            return 0;
        }
        // 周期性费用
        if (FeeDto.FEE_FLAG_CYCLE.equals(tmpPayFeeRulePo.getFeeFlag())) {
            do {
                endTime = DateUtil.getNextMonthDate(startTime, ruleCycle);
                if (endTime.getTime() >= targetEndTime.getTime()) {
                    endTime = targetEndTime;
                }
                //todo 生成 费用
                doGeneratorPayFee(tmpPayFeeRulePo, startTime, endTime,
                        feeConfigDtos.get(0),
                        tmpPayFeePos,
                        tmpFeeAttrPos,
                        payFeeRuleBillPos,
                        ownerDto,
                        payerObjName,
                        ruleCycle);
                startTime = endTime;

            }
            while (endTime.getTime() < targetEndTime.getTime());
        } else { // 一次性账单
            endTime = targetEndTime;
            doGeneratorPayFee(tmpPayFeeRulePo, startTime, endTime,
                    feeConfigDtos.get(0),
                    tmpPayFeePos,
                    tmpFeeAttrPos,
                    payFeeRuleBillPos,
                    ownerDto,
                    payerObjName,
                    ruleCycle);
            startTime = endTime;

        }
        int saveFlag = 0;
        if (!tmpPayFeePos.isEmpty()) {
            saveFlag = saveFeeAndAttrs(tmpPayFeePos, tmpFeeAttrPos, payFeeRuleBillPos);
        }

        //todo 修改pay_fee_rule 的CurYearMonth
        PayFeeRulePo payFeeRulePo = new PayFeeRulePo();
        payFeeRulePo.setRuleId(tmpPayFeeRulePo.getRuleId());
        payFeeRulePo.setCurYearMonth(DateUtil.getFormatTimeStringB(endTime));
        // 后付费 最后刷账单日时还是要多刷一个月
        if (FeeConfigDto.PAYMENT_CD_AFTER.equals(feeConfigDtos.get(0).getPaymentCd())) {
            endTime = DateUtil.getNextMonthDate(endTime, ruleCycle);
            payFeeRulePo.setCurYearMonth(DateUtil.getFormatTimeStringB(endTime));
        }
        payFeeRulePo.setCommunityId(tmpPayFeeRulePo.getCommunityId());
        if (!endTime.before(DateUtil.getDateFromStringB(tmpPayFeeRulePo.getMaxTime()))) {
            payFeeRulePo.setState(FeeDto.STATE_FINISH);
        }
        payFeeRuleV1InnerServiceSMOImpl.updatePayFeeRule(payFeeRulePo);

        // 这里触发扣款
        PrePayFeeDto prePayFeeDto = new PrePayFeeDto();
        prePayFeeDto.setCommunityId(tmpPayFeeRulePo.getCommunityId());
        prePayFeeDto.setFtId(feeConfigDtos.get(0).getFtId());
        prePayFeeDto.setRuleId(tmpPayFeeRulePo.getRuleId());
        if (FeeConfigDto.FEE_TYPE_CD_WATER.equals(feeConfigDtos.get(0).getFeeTypeCd())
                || FeeConfigDto.FEE_TYPE_CD_METER.equals(feeConfigDtos.get(0).getFeeTypeCd())
                || FeeConfigDto.FEE_TYPE_CD_GAS.equals(feeConfigDtos.get(0).getFeeTypeCd())
        ) {
            prePayFeeDto.setRuleId("");
        }
        prePayFeeDto.setHasMoney("1");
        List<PrePayFeeDto> prePayFeeDtos = prePayFeeV1InnerServiceSMOImpl.queryPrePayFees(prePayFeeDto);

        if (ListUtil.isNull(prePayFeeDtos)) {
            return saveFlag;
        }
        for (PrePayFeeDto tPrePayFeeDto : prePayFeeDtos) {
            try {
                deductionPrePayFeeImpl.deductionPrePayFee(tPrePayFeeDto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return saveFlag;
    }


    private void doGeneratorPayFee(PayFeeRulePo tmpPayFeeRulePo, Date startTime, Date endTime, FeeConfigDto feeConfigDto,
                                   List<PayFeePo> tmpPayFeePos, List<FeeAttrPo> tmpFeeAttrPos,
                                   List<PayFeeRuleBillPo> payFeeRuleBillPos,
                                   OwnerDto ownerDto,
                                   String payerObjName,
                                   int ruleCycle) {

        PayFeePo payFeePo = BeanConvertUtil.covertBean(tmpPayFeeRulePo, PayFeePo.class);
        payFeePo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
        payFeePo.setFeeFlag(FeeDto.FEE_FLAG_ONCE);
        payFeePo.setStartTime(DateUtil.getFormatTimeStringB(startTime));
        payFeePo.setRuleId(tmpPayFeeRulePo.getRuleId());
        payFeePo.setPayerObjName(payerObjName);
        payFeePo.setRuleId(tmpPayFeeRulePo.getRuleId());
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


        //todo 生成ruleBill 数据
        PayFeeRuleBillPo payFeeRuleBillPo = new PayFeeRuleBillPo();
        payFeeRuleBillPo.setFeeId(payFeePo.getFeeId());
        payFeeRuleBillPo.setBillId(GenerateCodeFactory.getGeneratorId("13"));
        payFeeRuleBillPo.setBillName(feeConfigDto.getFeeName());
        payFeeRuleBillPo.setRuleId(tmpPayFeeRulePo.getRuleId());
        payFeeRuleBillPo.setConfigId(tmpPayFeeRulePo.getConfigId());
        payFeeRuleBillPo.setBatchId(payFeePo.getBatchId());
        payFeeRuleBillPo.setCurYearMonth(DateUtil.getFormatTimeStringB(startTime));
        payFeeRuleBillPo.setCommunityId(payFeePo.getCommunityId());
        payFeeRuleBillPo.setMonthCycle(ruleCycle + "");
        payFeeRuleBillPos.add(payFeeRuleBillPo);

        //todo 这里时间少一天，显示，因为是周期性转一次性 所以 这里为了显示合理 结束时间少一天处理
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


    private int saveFeeAndAttrs(List<PayFeePo> feePos, List<FeeAttrPo> feeAttrsPos, List<PayFeeRuleBillPo> payFeeRuleBillPos) {
        if (feePos == null || feePos.isEmpty()) {
            return 1;
        }
        int flag = feeInnerServiceSMOImpl.saveFee(feePos);
        if (flag < 1) {
            return flag;
        }

//        if (!payFeeRuleBillPos.isEmpty()) {
//            payFeeRuleBillV1InnerServiceSMOImpl.savePayFeeRuleBills(payFeeRuleBillPos);
//        }

        flag = feeAttrInnerServiceSMOImpl.saveFeeAttrs(feeAttrsPos);

        // todo 这里异步的方式计算 月数据 和欠费数据
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

    /**
     * 计算目标结束时间
     *
     * @param tmpPayFeeRulePo
     * @param feeConfigDto
     * @return
     */
    private Date computeTargetEndTime(PayFeeRulePo tmpPayFeeRulePo, FeeConfigDto feeConfigDto) {

        Date targetEndDate = null;
        //todo 判断当前费用是否已结束
        if (FeeDto.STATE_FINISH.equals(tmpPayFeeRulePo.getState())) {
            targetEndDate = DateUtil.getDateFromStringB(tmpPayFeeRulePo.getEndTime());
            return targetEndDate;
        }

        // todo 一次性费用直接 把最大时间给出去
        if (FeeDto.FEE_FLAG_ONCE.equals(tmpPayFeeRulePo.getFeeFlag())) {
            return DateUtil.getDateFromStringA(tmpPayFeeRulePo.getMaxTime());
        }

        String maxTime = tmpPayFeeRulePo.getMaxTime();
        if (maxTime.contains("23:59:59")) {
            maxTime = DateUtil.getNextSecTime(maxTime);
        }
        Date maxEndTime = DateUtil.getDateFromStringB(maxTime);

        Date billEndTime = DateUtil.getCurrentDate();
        //建账时间
        Date startDate = DateUtil.getDateFromStringB(tmpPayFeeRulePo.getStartTime());

        Date endDate = DateUtil.getDateFromStringB(tmpPayFeeRulePo.getEndTime());
        long paymentCycle = Long.parseLong(feeConfigDto.getPaymentCycle());

        // 当前时间 - 开始时间  = 月份
        double mulMonth = 0.0;
        mulMonth = DateUtil.dayCompare(startDate, billEndTime);

        // 月份/ 周期 = 轮数（向上取整）
        double round = 0.0;
        if ("1200".equals(feeConfigDto.getPaymentCd())) { // 1200预付费
            round = Math.floor(mulMonth / paymentCycle) + 1;
        } else { //2100后付费
            round = Math.floor(mulMonth / paymentCycle);
        }
        // 轮数 * 周期 * 30 + 开始时间 = 目标 到期时间
        targetEndDate = DateUtil.getTargetEndTime(round * paymentCycle, startDate);//目标结束时间


        int generateCycles = 1;
        if (StringUtil.isNumber(feeConfigDto.getPrepaymentPeriod())) {
            generateCycles = Integer.parseInt(feeConfigDto.getPrepaymentPeriod());
        }
        if(generateCycles >1){
            targetEndDate = DateUtil.getTargetEndTime((generateCycles-1) * paymentCycle, targetEndDate);
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
