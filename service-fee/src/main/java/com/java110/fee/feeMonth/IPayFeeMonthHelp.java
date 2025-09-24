package com.java110.fee.feeMonth;

import com.java110.dto.fee.FeeDto;
import com.java110.dto.payFee.PayFeeMonthOwnerDto;

public interface IPayFeeMonthHelp {

    PayFeeMonthOwnerDto generatorOwnerRoom(FeeDto feeDto);



    /**
     * 处理欠费问题
     * @param feeDto
     * @param payFeeMonthOwnerDto
     */
    void generatorPayFeeDetailMonth(FeeDto feeDto, PayFeeMonthOwnerDto payFeeMonthOwnerDto);
}
