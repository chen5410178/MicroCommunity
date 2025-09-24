package com.java110.dto.payFee;

import com.java110.dto.fee.PayFeeDto;
import com.java110.po.fee.PayFeeDetailPo;

import java.io.Serializable;

public class BatchPayFeeOrder implements Serializable {
    private PayFeeDetailPo payFeeDetailPo;

    private PayFeeDto feeDto;

    public BatchPayFeeOrder() {
    }

    public BatchPayFeeOrder(PayFeeDetailPo payFeeDetailPo, PayFeeDto feeDto) {
        this.payFeeDetailPo = payFeeDetailPo;
        this.feeDto = feeDto;
    }

    public PayFeeDetailPo getPayFeeDetailPo() {
        return payFeeDetailPo;
    }

    public void setPayFeeDetailPo(PayFeeDetailPo payFeeDetailPo) {
        this.payFeeDetailPo = payFeeDetailPo;
    }

    public PayFeeDto getFeeDto() {
        return feeDto;
    }

    public void setFeeDto(PayFeeDto feeDto) {
        this.feeDto = feeDto;
    }
}
