package com.java110.fee.bill.formula;

import com.java110.core.factory.CommunitySettingFactory;
import com.java110.dto.fee.FeeAmountDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.fee.bill.IFeeFormula;
import com.java110.utils.constant.FeeConfigConstant;
import com.java110.utils.util.MoneyUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 固定费
 */
@Service("feeFormula_2002")
public class FeeFormulaFixedImpl implements IFeeFormula {

    @Override
    public FeeAmountDto computeFeeAmount(FeeConfigDto feeConfigDto, String ruleId, String payerObjId, String payerObjType, int cycleValue, double amountValue) {
        BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeConfigDto.getAdditionalAmount()));
        // feePrice = additionalAmount.setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_UP);
        BigDecimal cycle = new BigDecimal(cycleValue);
        
        // 添加monthFlag逻辑处理
        String monthFlag = CommunitySettingFactory.getValue(feeConfigDto.getCommunityId(), "FEE_MONTH_FLAG");
        BigDecimal feeTotalPrice = additionalAmount;
        
        if ("2".equals(monthFlag)) {
            feeTotalPrice = feeTotalPrice.setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_UP);
            double tAmount = MoneyUtil.computePriceScale(
                    feeTotalPrice.doubleValue(),
                    feeConfigDto.getScale(),
                    Integer.parseInt(feeConfigDto.getDecimalPlace())
            );
            feeTotalPrice = new BigDecimal(tAmount + "");
        }
        
        feeTotalPrice = feeTotalPrice.multiply(cycle).setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_UP);

        String remark = "单价：" + feeConfigDto.getAdditionalAmount();
        return new FeeAmountDto(feeTotalPrice.doubleValue(), remark);

    }

}
