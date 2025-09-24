package com.java110.fee.bill;

import com.java110.dto.prePayFee.PrePayFeeDto;

public interface IDeductionPrePayFee {

    int deductionPrePayFee(String ppfId);

    int deductionPrePayFee(PrePayFeeDto prePayFeeDto);

    /**
     * 预付款扣款
     * @param ppfId
     * @param amount
     * @return
     */
    int deductionAmount(String ppfId,double amount,String feeId);
}
