package com.java110.fee.bill;

import com.java110.dto.fee.FeeAmountDto;
import com.java110.dto.payFee.PayFeeRuleDto;

public interface IComputeBillPrice {
    /**
     * 计算账单应收金额
     * @param payFeeRuleDto
     * @param cycle
     * @return
     */
    FeeAmountDto computeBillReceiveAmount(PayFeeRuleDto payFeeRuleDto, int cycle);

    /**
     * 计算账单应收金额
     * @param ruleId
     * @param cycle
     * @return
     */
    FeeAmountDto computeBillReceiveAmount(String ruleId,int cycle);
}
