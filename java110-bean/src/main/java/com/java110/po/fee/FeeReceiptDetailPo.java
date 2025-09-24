package com.java110.po.fee;

import java.io.Serializable;

public class FeeReceiptDetailPo implements Serializable {

    private String area;
    private String amount;
    private String feeName;
    private String detailId;
    private String startTime;
    private String statusCd = "0";
    private String endTime;
    private String communityId;
    private String receiptId;
    private String feeId;
    private String cycle;
    private String squarePrice;
    private String createTime;
    private String remark;
    private String feeMonth; // fee_month
    private String startDegree; // start_degree
    private String endDegree; // end_degree
    private String payWay; // pay_way
    private String deductionAmount; // deduction_amount
    private String lateAmount; // late_amount

    private String printCount;

    private String ftName;

    private String acctAmount;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public String getFeeId() {
        return feeId;
    }

    public void setFeeId(String feeId) {
        this.feeId = feeId;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getSquarePrice() {
        return squarePrice;
    }

    public void setSquarePrice(String squarePrice) {
        this.squarePrice = squarePrice;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFeeMonth() {
        return feeMonth;
    }

    public void setFeeMonth(String feeMonth) {
        this.feeMonth = feeMonth;
    }

    public String getStartDegree() {
        return startDegree;
    }

    public void setStartDegree(String startDegree) {
        this.startDegree = startDegree;
    }

    public String getEndDegree() {
        return endDegree;
    }

    public void setEndDegree(String endDegree) {
        this.endDegree = endDegree;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getDeductionAmount() {
        return deductionAmount;
    }

    public void setDeductionAmount(String deductionAmount) {
        this.deductionAmount = deductionAmount;
    }

    public String getLateAmount() {
        return lateAmount;
    }

    public void setLateAmount(String lateAmount) {
        this.lateAmount = lateAmount;
    }

    public String getPrintCount() {
        return printCount;
    }

    public void setPrintCount(String printCount) {
        this.printCount = printCount;
    }

    public String getFtName() {
        return ftName;
    }

    public void setFtName(String ftName) {
        this.ftName = ftName;
    }

    public String getAcctAmount() {
        return acctAmount;
    }

    public void setAcctAmount(String acctAmount) {
        this.acctAmount = acctAmount;
    }
}
