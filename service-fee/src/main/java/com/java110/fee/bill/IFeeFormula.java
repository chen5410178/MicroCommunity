package com.java110.fee.bill;

import com.java110.dto.fee.FeeAmountDto;
import com.java110.dto.fee.FeeConfigDto;

public interface IFeeFormula {

    /**
     * 计算费用金额
     * @param feeConfigDto
     * @param payerObjId
     * @param payerObjType
     * @param cycle
     * @return
     */
    FeeAmountDto computeFeeAmount(FeeConfigDto feeConfigDto, String ruleId, String payerObjId, String payerObjType, int cycle, double amount);
}
