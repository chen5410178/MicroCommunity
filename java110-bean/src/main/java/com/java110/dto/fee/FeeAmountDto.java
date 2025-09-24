package com.java110.dto.fee;

import java.io.Serializable;

public class FeeAmountDto implements Serializable {
    private double amount;
    private String remark;
    public FeeAmountDto() {

    }
    public FeeAmountDto(double amount, String remark) {
        this.amount = amount;
        this.remark = remark;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
