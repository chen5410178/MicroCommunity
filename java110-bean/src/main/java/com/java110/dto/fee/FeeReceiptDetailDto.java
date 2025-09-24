package com.java110.dto.fee;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 收据明细数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class FeeReceiptDetailDto extends PageDto implements Serializable {

    private String area;
    private String amount;
    private String orgAmount;
    private String orgReceiptCode;
    private String receiptCode;
    private String feeName;
    private String detailId;
    private String[] detailIds;
    private String startTime;
    private String endTime;
    private String communityId;
    private String receiptId;
    private String[] receiptIds;
    private String feeId;
    private String cycle;
    private String squarePrice;
    private String remark;
    private String feeFlag;
    private String preDegrees;
    private String curDegrees;
    private String primeRate;
    private String units;
    private String feeMonth; // fee_month
    private String startDegree; // start_degree
    private String endDegree; // end_degree
    private String payWay; // pay_way
    private String deductionAmount; // deduction_amount
    private String lateAmount; // late_amount


    private Date createTime;

    private String statusCd = "0";

    private String payOrderId;

    private String orderBy;

    private String discountPrice;

    private String payerObjId;

    private String ftId;

    private String ftName;

    private String yearMonth;

    private String deductFrom;

    private int printCount;

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


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getSquarePrice() {
        return squarePrice;
    }

    public void setSquarePrice(String squarePrice) {
        this.squarePrice = squarePrice;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFeeFlag() {
        return feeFlag;
    }

    public void setFeeFlag(String feeFlag) {
        this.feeFlag = feeFlag;
    }

    public String[] getReceiptIds() {
        return receiptIds;
    }

    public void setReceiptIds(String[] receiptIds) {
        this.receiptIds = receiptIds;
    }

    public String[] getDetailIds() {
        return detailIds;
    }

    public void setDetailIds(String[] detailIds) {
        this.detailIds = detailIds;
    }

    public String getPreDegrees() {
        return preDegrees;
    }

    public void setPreDegrees(String preDegrees) {
        this.preDegrees = preDegrees;
    }

    public String getCurDegrees() {
        return curDegrees;
    }

    public void setCurDegrees(String curDegrees) {
        this.curDegrees = curDegrees;
    }

    public String getPrimeRate() {
        return primeRate;
    }

    public void setPrimeRate(String primeRate) {
        this.primeRate = primeRate;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getPayOrderId() {
        return payOrderId;
    }

    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getPayerObjId() {
        return payerObjId;
    }

    public void setPayerObjId(String payerObjId) {
        this.payerObjId = payerObjId;
    }

    public String getFtId() {
        return ftId;
    }

    public void setFtId(String ftId) {
        this.ftId = ftId;
    }

    public String getFtName() {
        return ftName;
    }

    public void setFtName(String ftName) {
        this.ftName = ftName;
    }

    public String getOrgAmount() {
        return orgAmount;
    }

    public void setOrgAmount(String orgAmount) {
        this.orgAmount = orgAmount;
    }

    public String getOrgReceiptCode() {
        return orgReceiptCode;
    }

    public void setOrgReceiptCode(String orgReceiptCode) {
        this.orgReceiptCode = orgReceiptCode;
    }

    public String getReceiptCode() {
        return receiptCode;
    }

    public void setReceiptCode(String receiptCode) {
        this.receiptCode = receiptCode;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public String getDeductFrom() {
        return deductFrom;
    }

    public void setDeductFrom(String deductFrom) {
        this.deductFrom = deductFrom;
    }

    public int getPrintCount() {
        return printCount;
    }

    public void setPrintCount(int printCount) {
        this.printCount = printCount;
    }

    public String getAcctAmount() {
        return acctAmount;
    }

    public void setAcctAmount(String acctAmount) {
        this.acctAmount = acctAmount;
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
}
