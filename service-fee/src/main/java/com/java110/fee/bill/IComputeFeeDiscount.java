package com.java110.fee.bill;

import com.java110.dto.fee.ComputeDiscountDto;
import com.java110.dto.fee.PayFeeDto;
import com.java110.dto.payFee.PayFeeRuleDto;

import java.util.List;

public interface IComputeFeeDiscount {

    List<ComputeDiscountDto> compute(List<PayFeeDto> payFeeDtos);

    /**
     * 计算预付款折扣
     * @param payFeeRuleDto
     * @param cycle
     * @return
     */
    List<ComputeDiscountDto> computePrePayFeeDiscount(PayFeeRuleDto payFeeRuleDto,int cycle);
}
