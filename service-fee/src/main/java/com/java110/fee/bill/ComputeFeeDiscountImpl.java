package com.java110.fee.bill;

import com.java110.dto.fee.*;
import com.java110.dto.payFee.PayFeeConfigDiscountDto;
import com.java110.dto.payFee.PayFeeRuleDto;
import com.java110.dto.room.ApplyRoomDiscountDto;
import com.java110.fee.discount.IComputeDiscount;
import com.java110.fee.smo.impl.FeeDiscountInnerServiceSMOImpl;
import com.java110.intf.fee.IApplyRoomDiscountInnerServiceSMO;
import com.java110.intf.fee.IFeeDiscountRuleInnerServiceSMO;
import com.java110.intf.fee.IPayFeeConfigDiscountInnerServiceSMO;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ComputeFeeDiscountImpl implements IComputeFeeDiscount {

    @Autowired
    private IPayFeeConfigDiscountInnerServiceSMO payFeeConfigDiscountInnerServiceSMOImpl;

    @Autowired
    private FeeDiscountInnerServiceSMOImpl feeDiscountInnerServiceSMOImpl;

    @Autowired
    private IFeeDiscountRuleInnerServiceSMO feeDiscountRuleInnerServiceSMOImpl;

    @Autowired
    private IComputeBillPrice computeBillPriceImpl;

    @Autowired
    private IApplyRoomDiscountInnerServiceSMO applyRoomDiscountInnerServiceSMOImpl;

    @Override
    public List<ComputeDiscountDto> compute(List<PayFeeDto> payFeeDtos) {
        // 计算有多少个月
        double month = 0.0;
        double feeMonth = 0.0;
        BigDecimal monthDec = new BigDecimal("0");
        BigDecimal oweAmount = new BigDecimal("0");
        Date startTime = null;
        Date endTime = null;
        boolean plusOneSec = false;
        for (PayFeeDto payFeeDto : payFeeDtos) {
            plusOneSec = false;
            if (payFeeDto.getEndTime().contains("23:59:59")) {
                plusOneSec = true;
            }
            startTime = DateUtil.getDateFromStringA(payFeeDto.getStartTime());
            endTime = DateUtil.getDateFromStringA(payFeeDto.getEndTime());
            feeMonth = DateUtil.dayCompare(startTime, endTime, plusOneSec);
            monthDec = monthDec.add(new BigDecimal(feeMonth + ""));
            oweAmount = oweAmount.add(new BigDecimal(payFeeDto.getOweAmount()));
        }
        month = monthDec.doubleValue();

        // todo 计算普通优惠折扣
        List<ComputeDiscountDto> computeDiscountDtos = computeCommonDiscount(payFeeDtos, month, oweAmount.doubleValue());
        // todo 计算优惠申请折扣（批量缴费自动打折）
        computeDiscountDtos.addAll(computeApplyRoomDiscount(payFeeDtos));
        return computeDiscountDtos;
    }


    private List<ComputeDiscountDto> computeCommonDiscount(List<PayFeeDto> feeDtos, double month, double oweAmount) {
        List<ComputeDiscountDto> computeDiscountDtos = new ArrayList<>();

        PayFeeConfigDiscountDto payFeeConfigDiscountDto = new PayFeeConfigDiscountDto();
        payFeeConfigDiscountDto.setConfigId(feeDtos.get(0).getConfigId());
        payFeeConfigDiscountDto.setRow(100);
        payFeeConfigDiscountDto.setPage(1);
        payFeeConfigDiscountDto.setCommunityId(feeDtos.get(0).getCommunityId());
        payFeeConfigDiscountDto.setStatusCd("0");
        Date currentTime = new Date();
        payFeeConfigDiscountDto.setCurrentTime(currentTime);
        //根据费用项查询折扣（该费用项下的所有折扣信息）
        List<PayFeeConfigDiscountDto> payFeeConfigDiscountDtos =
                payFeeConfigDiscountInnerServiceSMOImpl.queryPayFeeConfigDiscounts(payFeeConfigDiscountDto);
        if (ListUtil.isNull(payFeeConfigDiscountDtos)) {
            return computeDiscountDtos;
        }
        List<FeeDiscountDto> feeDiscountDtos = null;
        for (PayFeeConfigDiscountDto tPayFeeConfigDiscountDto : payFeeConfigDiscountDtos) {
            try {
                FeeDiscountDto feeDiscountDto = new FeeDiscountDto();
                feeDiscountDto.setDiscountId(tPayFeeConfigDiscountDto.getDiscountId());
                feeDiscountDtos = feeDiscountInnerServiceSMOImpl.queryFeeDiscounts(feeDiscountDto);
                if (ListUtil.isNull(feeDiscountDtos)) {
                    continue;
                }

                FeeDiscountRuleDto feeDiscountRuleDto = new FeeDiscountRuleDto();
                feeDiscountRuleDto.setRuleId(feeDiscountDtos.get(0).getRuleId());
                //查询打折规则表
                List<FeeDiscountRuleDto> feeDiscountRuleDtos = feeDiscountRuleInnerServiceSMOImpl.queryFeeDiscountRules(feeDiscountRuleDto);
                //Assert.listOnlyOne(feeDiscountRuleDtos, "查询打折规则表错误！");
                if (ListUtil.isNull(feeDiscountRuleDtos)) {
                    continue;
                }
                feeDiscountDto = feeDiscountDtos.get(0);
                feeDiscountDto.setCycles(month);
                feeDiscountDto.setPayFeeDtos(feeDtos);
                feeDiscountDto.setOweAmount(oweAmount);

                IComputeDiscount computeDiscount = (IComputeDiscount) ApplicationContextFactory.getBean(feeDiscountDtos.get(0).getBeanImpl());
                ComputeDiscountDto computeDiscountDto = computeDiscount.compute(feeDiscountDtos.get(0));
                if (computeDiscountDto == null) {
                    continue;
                }
                computeDiscountDtos.add(computeDiscountDto);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return computeDiscountDtos;
    }

    /**
     * 计算优惠申请折扣（适用于批量缴费自动打折）
     * 命中条件：state='4' 审批通过、in_use='0' 可用、end_time >= 当前时间、feeId 命中子表勾选费用
     *
     * @param payFeeDtos
     * @return
     */
    private List<ComputeDiscountDto> computeApplyRoomDiscount(List<PayFeeDto> payFeeDtos) {
        List<ComputeDiscountDto> computeDiscountDtos = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime = simpleDateFormat.format(new Date());
        for (PayFeeDto payFeeDto : payFeeDtos) {
            try {
                if (!FeeDto.PAYER_OBJ_TYPE_ROOM.equals(payFeeDto.getPayerObjType())) {
                    continue;
                }
                //查询该费用命中的有效优惠申请
                ApplyRoomDiscountDto applyRoomDiscountDto = new ApplyRoomDiscountDto();
                //审核已通过
                applyRoomDiscountDto.setState("4");
                //是否可用状态标识(0表示在用；1表示不可用)
                applyRoomDiscountDto.setInUse("0");
                //小区ID
                applyRoomDiscountDto.setCommunityId(payFeeDto.getCommunityId());
                //房屋id
                applyRoomDiscountDto.setRoomId(payFeeDto.getPayerObjId());
                //费用ID（子表匹配）
                applyRoomDiscountDto.setFeeId(payFeeDto.getFeeId());
                //申请在有效期内（end_time >= 当前时间）
                applyRoomDiscountDto.setEndTime(nowTime);
                List<ApplyRoomDiscountDto> applyRoomDiscountDtos = applyRoomDiscountInnerServiceSMOImpl.queryApplyRoomDiscounts(applyRoomDiscountDto);
                if (ListUtil.isNull(applyRoomDiscountDtos)) {
                    continue;
                }
                ApplyRoomDiscountDto applyRoomDiscount = applyRoomDiscountDtos.get(0);
                String discountId = applyRoomDiscount.getDiscountId();
                if (StringUtil.isEmpty(discountId)) {
                    continue;
                }
                //查询折扣信息
                FeeDiscountDto feeDiscountDto = new FeeDiscountDto();
                feeDiscountDto.setCommunityId(payFeeDto.getCommunityId());
                feeDiscountDto.setDiscountId(discountId);
                List<FeeDiscountDto> feeDiscountDtos = feeDiscountInnerServiceSMOImpl.queryFeeDiscounts(feeDiscountDto);
                if (ListUtil.isNull(feeDiscountDtos)) {
                    continue;
                }
                FeeDiscountDto tmpFeeDiscountDto = feeDiscountDtos.get(0);
                //计算该费用缴费周期（月）
                boolean plusOneSec = payFeeDto.getEndTime().contains("23:59:59");
                Date startTime = DateUtil.getDateFromStringA(payFeeDto.getStartTime());
                Date endTime = DateUtil.getDateFromStringA(payFeeDto.getEndTime());
                double cycles = DateUtil.dayCompare(startTime, endTime, plusOneSec);
                tmpFeeDiscountDto.setCycles(cycles);
                tmpFeeDiscountDto.setFeeId(payFeeDto.getFeeId());
                double oweAmount = 0.0;
                if (!StringUtil.isEmpty(payFeeDto.getOweAmount())) {
                    oweAmount = Double.parseDouble(payFeeDto.getOweAmount());
                }
                tmpFeeDiscountDto.setOweAmount(oweAmount);
                IComputeDiscount computeDiscount = (IComputeDiscount) ApplicationContextFactory.getBean(tmpFeeDiscountDto.getBeanImpl());
                ComputeDiscountDto computeDiscountDto = computeDiscount.compute(tmpFeeDiscountDto);
                if (computeDiscountDto == null) {
                    continue;
                }
                computeDiscountDto.setDiscountType(FeeDiscountDto.DISCOUNT_TYPE_DV);
                computeDiscountDto.setArdId(applyRoomDiscount.getArdId());
                computeDiscountDtos.add(computeDiscountDto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return computeDiscountDtos;
    }



    @Override
    public List<ComputeDiscountDto> computePrePayFeeDiscount(PayFeeRuleDto payFeeRuleDto, int cycle) {
        List<ComputeDiscountDto> computeDiscountDtos = new ArrayList<>();

        PayFeeConfigDiscountDto payFeeConfigDiscountDto = new PayFeeConfigDiscountDto();
        payFeeConfigDiscountDto.setConfigId(payFeeRuleDto.getConfigId());
        payFeeConfigDiscountDto.setRow(100);
        payFeeConfigDiscountDto.setPage(1);
        payFeeConfigDiscountDto.setCommunityId(payFeeRuleDto.getCommunityId());
        payFeeConfigDiscountDto.setStatusCd("0");
        Date currentTime = new Date();
        payFeeConfigDiscountDto.setCurrentTime(currentTime);
        //根据费用项查询折扣（该费用项下的所有折扣信息）
        List<PayFeeConfigDiscountDto> payFeeConfigDiscountDtos =
                payFeeConfigDiscountInnerServiceSMOImpl.queryPayFeeConfigDiscounts(payFeeConfigDiscountDto);
        if (ListUtil.isNull(payFeeConfigDiscountDtos)) {
            return computeDiscountDtos;
        }

        FeeAmountDto feeAmountDto = computeBillPriceImpl.computeBillReceiveAmount(payFeeRuleDto, cycle);

        List<FeeDiscountDto> feeDiscountDtos = null;
        String discountSmallType = "";
        for (PayFeeConfigDiscountDto tPayFeeConfigDiscountDto : payFeeConfigDiscountDtos) {
            try {
                FeeDiscountDto feeDiscountDto = new FeeDiscountDto();
                feeDiscountDto.setDiscountId(tPayFeeConfigDiscountDto.getDiscountId());
                feeDiscountDtos = feeDiscountInnerServiceSMOImpl.queryFeeDiscounts(feeDiscountDto);
                if (ListUtil.isNull(feeDiscountDtos)) {
                    continue;
                }

                FeeDiscountRuleDto feeDiscountRuleDto = new FeeDiscountRuleDto();
                feeDiscountRuleDto.setRuleId(feeDiscountDtos.get(0).getRuleId());
                //查询打折规则表
                List<FeeDiscountRuleDto> feeDiscountRuleDtos = feeDiscountRuleInnerServiceSMOImpl.queryFeeDiscountRules(feeDiscountRuleDto);
                //Assert.listOnlyOne(feeDiscountRuleDtos, "查询打折规则表错误！");
                if (ListUtil.isNull(feeDiscountRuleDtos)) {
                    continue;
                }
                discountSmallType = feeDiscountRuleDtos.get(0).getDiscountSmallType();
                // 只计算 打折，减免，和赠送月 discountFeeRule reductionFeeRule reductionMonthFeeRule reductionMonthFeeRuleIn
                if (!"1".equals(discountSmallType) && !"2".equals(discountSmallType) && !"6".equals(discountSmallType)) {
                    continue;
                }
                feeDiscountDto = feeDiscountDtos.get(0);
                feeDiscountDto.setCycles(cycle);
                feeDiscountDto.setOweAmount(feeAmountDto.getAmount());

                IComputeDiscount computeDiscount = (IComputeDiscount) ApplicationContextFactory.getBean(feeDiscountDtos.get(0).getBeanImpl());
                ComputeDiscountDto computeDiscountDto = computeDiscount.compute(feeDiscountDtos.get(0));
                if (computeDiscountDto == null) {
                    continue;
                }
                computeDiscountDtos.add(computeDiscountDto);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return computeDiscountDtos;
    }
}
