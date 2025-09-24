package com.java110.fee.discount.impl;

import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.fee.*;
import com.java110.fee.discount.IComputeDiscount;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 打折无欠费规则
 *
 * @author fqz
 * @date 2020-2-11 9:18
 */
@Component(value = "discountWithoutArrearsRule")
public class DiscountWithoutArrearsRule implements IComputeDiscount {

    /**
     * 89002020980007	102020005	月份
     * 89002020980008	102020005	打折率
     * 89002020980009   102020005   欠费时长
     */
    private static final String SPEC_MONTH = "89002020980007"; //月份
    private static final String SPEC_RATE = "89002020980008"; // 打折率
    private static final String LENGTH_OF_ARREARS = "89002020980009"; //欠费时长(天)

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    @Override
    public ComputeDiscountDto compute(FeeDiscountDto feeDiscountDto) {
        List<FeeDiscountSpecDto> feeDiscountSpecDtos = feeDiscountDto.getFeeDiscountSpecs();

        if (ListUtil.isNull(feeDiscountSpecDtos)) {
            return null;
        }
        //月份
        double month = 0.0;
        //打折率
        double rate = 0.0;
        //欠费时长(天)
        int arrearsDay = 0;
        for (FeeDiscountSpecDto feeDiscountSpecDto : feeDiscountSpecDtos) {
            //取得月份
            if (SPEC_MONTH.equals(feeDiscountSpecDto.getSpecId())) {
                month = Double.parseDouble(feeDiscountSpecDto.getSpecValue());
            }
            //取得打折率
            if (SPEC_RATE.equals(feeDiscountSpecDto.getSpecId())) {
                rate = Double.parseDouble(feeDiscountSpecDto.getSpecValue());
            }
            //取得欠费时长
            if (LENGTH_OF_ARREARS.equals(feeDiscountSpecDto.getSpecId())) {
                arrearsDay = Integer.parseInt(feeDiscountSpecDto.getSpecValue());
            }
        }

        //判断周期是否小于打折无欠费规则的月份
        if (feeDiscountDto.getCycles() < month) {
            ComputeDiscountDto computeDiscountDto = new ComputeDiscountDto();
            computeDiscountDto.setDiscountId(feeDiscountDto.getDiscountId());
            computeDiscountDto.setDiscountType(FeeDiscountDto.DISCOUNT_TYPE_D);
            computeDiscountDto.setRuleId(feeDiscountDto.getRuleId());
            computeDiscountDto.setRuleName(feeDiscountDto.getRuleName());
            computeDiscountDto.setDiscountName(feeDiscountDto.getDiscountName());
            computeDiscountDto.setDiscountPrice(0.0);
            computeDiscountDto.setFeeDiscountSpecs(feeDiscountSpecDtos);
            return computeDiscountDto;
        }

        //查询费用
        List<PayFeeDto> payFeeDtos = feeDiscountDto.getPayFeeDtos();
        Date startTime = null;
        Date sTime = null;
        for (PayFeeDto payFeeDto : payFeeDtos) {
            sTime = DateUtil.getDateFromStringA(payFeeDto.getStartTime());
            if (startTime == null) {
                startTime = sTime;
                continue;
            }
            if (sTime.before(startTime)) {
                startTime = sTime;
            }
        }

        //获取当前时间
        Date date = new Date();
        //计算相差的天数
        long day = 0;
        if (date.getTime() >= startTime.getTime()) {
            //天数不大于1天时按1天来算
            day = (date.getTime() - startTime.getTime()) / (1000 * 60 * 60 * 24) + 1;
        }
        if (day > arrearsDay) {
            ComputeDiscountDto computeDiscountDto = new ComputeDiscountDto();
            computeDiscountDto.setDiscountId(feeDiscountDto.getDiscountId());
            computeDiscountDto.setDiscountType(FeeDiscountDto.DISCOUNT_TYPE_D);
            computeDiscountDto.setRuleId(feeDiscountDto.getRuleId());
            computeDiscountDto.setRuleName(feeDiscountDto.getRuleName());
            computeDiscountDto.setDiscountName(feeDiscountDto.getDiscountName());
            computeDiscountDto.setDiscountPrice(0.0);
            computeDiscountDto.setFeeDiscountSpecs(feeDiscountSpecDtos);
            return computeDiscountDto;
        }
        double oweAmount = feeDiscountDto.getOweAmount();

        BigDecimal priceDec = new BigDecimal(oweAmount + "");
        double discountPrice = priceDec.multiply(new BigDecimal(1.0 - rate)).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();

        ComputeDiscountDto computeDiscountDto = new ComputeDiscountDto();
        computeDiscountDto.setDiscountId(feeDiscountDto.getDiscountId());
        computeDiscountDto.setDiscountType(FeeDiscountDto.DISCOUNT_TYPE_D);
        computeDiscountDto.setRuleId(feeDiscountDto.getRuleId());
        computeDiscountDto.setRuleName(feeDiscountDto.getRuleName());
        computeDiscountDto.setDiscountName(feeDiscountDto.getDiscountName());
        computeDiscountDto.setDiscountPrice(discountPrice*-1);
        computeDiscountDto.setFeeDiscountSpecs(feeDiscountSpecDtos);
        return computeDiscountDto;
    }
}
