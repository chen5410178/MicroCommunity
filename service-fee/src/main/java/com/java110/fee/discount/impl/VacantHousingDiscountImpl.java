package com.java110.fee.discount.impl;

import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.fee.ComputeDiscountDto;
import com.java110.dto.fee.FeeDiscountDto;
import com.java110.dto.fee.FeeDiscountSpecDto;
import com.java110.fee.discount.IComputeDiscount;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.utils.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * 空置房打折规则
 *
 * @author fqz
 * @date 2021-01-04 17:00
 */
@Component(value = "roomDiscountManageRule")
public class VacantHousingDiscountImpl implements IComputeDiscount {

    /**
     * 89002020980012	102020007	月份
     * 89002020980013	102020007	打折率
     */

    private static final String SPEC_MONTH = "89002020980012"; //月份
    private static final String SPEC_RATE = "89002020980013"; // 打折率

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    /**
     * @param feeDiscountDto 费用优惠
     * @return
     */
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
        for (FeeDiscountSpecDto feeDiscountSpecDto : feeDiscountSpecDtos) {
            //取得月份
            if (SPEC_MONTH.equals(feeDiscountSpecDto.getSpecId())) {
                month = 1;
            }
            //取得打折率
            if (SPEC_RATE.equals(feeDiscountSpecDto.getSpecId())) {
                rate = Double.parseDouble(feeDiscountSpecDto.getSpecValue());
            }
        }
        //缴费周期小于空置房打折规则至少应缴月份，就不享受打折优惠
        if (feeDiscountDto.getCycles() < month) {
            ComputeDiscountDto computeDiscountDto = new ComputeDiscountDto();
            computeDiscountDto.setDiscountId(feeDiscountDto.getDiscountId());
            computeDiscountDto.setDiscountType(FeeDiscountDto.DISCOUNT_TYPE_DV);
            computeDiscountDto.setRuleId(feeDiscountDto.getRuleId());
            computeDiscountDto.setRuleName(feeDiscountDto.getRuleName());
            computeDiscountDto.setDiscountName(feeDiscountDto.getDiscountName());
            computeDiscountDto.setDiscountPrice(0.0);
            computeDiscountDto.setFeeDiscountSpecs(feeDiscountSpecDtos);
            return computeDiscountDto;
        }
        //查询费用


        //BigDecimal cycleDec = new BigDecimal(month);
        //2021-03-05 根据缴费 时长来算 不应该按 至少缴费来算
        double oweAmount = feeDiscountDto.getOweAmount();
        BigDecimal priceDec = new BigDecimal(oweAmount + "");
        double discountPrice = priceDec.multiply(new BigDecimal(1.0 - rate)).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();

        ComputeDiscountDto computeDiscountDto = new ComputeDiscountDto();
        computeDiscountDto.setDiscountId(feeDiscountDto.getDiscountId());
        computeDiscountDto.setDiscountType(FeeDiscountDto.DISCOUNT_TYPE_DV);
        computeDiscountDto.setRuleId(feeDiscountDto.getRuleId());
        computeDiscountDto.setRuleName(feeDiscountDto.getRuleName());
        computeDiscountDto.setDiscountName(feeDiscountDto.getDiscountName());
        computeDiscountDto.setDiscountPrice(discountPrice* -1);
        computeDiscountDto.setFeeDiscountSpecs(feeDiscountSpecDtos);
        return computeDiscountDto;
    }
}
