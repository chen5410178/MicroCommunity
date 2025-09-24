package com.java110.fee.bill;

import com.java110.dto.fee.*;
import com.java110.dto.payFee.PayFeeConfigDiscountDto;
import com.java110.dto.payFee.PayFeeRuleDto;
import com.java110.fee.discount.IComputeDiscount;
import com.java110.fee.smo.impl.FeeDiscountInnerServiceSMOImpl;
import com.java110.intf.fee.IFeeDiscountRuleInnerServiceSMO;
import com.java110.intf.fee.IPayFeeConfigDiscountInnerServiceSMO;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
