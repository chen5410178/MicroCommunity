package com.java110.dto.prePayFee;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 应付收款数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class PrePayFeeDto extends PageDto implements Serializable {
    public static final String STATE_RETURNING = "1000";
    public static final String STATE_RETURNED = "1100";
    public static final String STATE_NORMAL = "1400";

    public static final String WAY_YES = "Y"; // 自动抵扣
    public static final String WAY_NO = "N"; // 手工抵扣
    private String giftAmount;
    private String payOrderId;
    private String[] payOrderIds;
    private String primeRate;
    private String primeRateName;
    private String receivableAmount;
    private String discountAmount;
    private String cycles;
    private String remark;
    private String receivedAmount;
    private String ownerId;
    private String payerObjName;
    private String ownerName;
    private String remainAmount;
    private String feeName;
    private String openInvoice;
    private String startTime;
    private String cashierId;
    private String state;
    private String stateName;
    private String endTime;
    private String ruleId;
    private String communityId;
    private String cashierName;
    private String ppfId;
    private String[] ppfIds;
    private String payerObjId;

    private String ftId;
    private String ftName;

    private String hasMoney;

    private String squarePrice;

    private String additionalAmount;


    private Date createTime;

    private String statusCd = "0";

    private String queryEndTime;
    private String queryStartTime;

    private String way;

    public String getGiftAmount() {
        return giftAmount;
    }

    public void setGiftAmount(String giftAmount) {
        this.giftAmount = giftAmount;
    }

    public String getPayOrderId() {
        return payOrderId;
    }

    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId;
    }

    public String getPrimeRate() {
        return primeRate;
    }

    public void setPrimeRate(String primeRate) {
        this.primeRate = primeRate;
    }

    public String getReceivableAmount() {
        return receivableAmount;
    }

    public void setReceivableAmount(String receivableAmount) {
        this.receivableAmount = receivableAmount;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getCycles() {
        return cycles;
    }

    public void setCycles(String cycles) {
        this.cycles = cycles;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(String receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getPayerObjName() {
        return payerObjName;
    }

    public void setPayerObjName(String payerObjName) {
        this.payerObjName = payerObjName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getRemainAmount() {
        return remainAmount;
    }

    public void setRemainAmount(String remainAmount) {
        this.remainAmount = remainAmount;
    }

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }

    public String getOpenInvoice() {
        return openInvoice;
    }

    public void setOpenInvoice(String openInvoice) {
        this.openInvoice = openInvoice;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getCashierId() {
        return cashierId;
    }

    public void setCashierId(String cashierId) {
        this.cashierId = cashierId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }

    public String getPpfId() {
        return ppfId;
    }

    public void setPpfId(String ppfId) {
        this.ppfId = ppfId;
    }

    public String getPayerObjId() {
        return payerObjId;
    }

    public void setPayerObjId(String payerObjId) {
        this.payerObjId = payerObjId;
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

    public String getPrimeRateName() {
        return primeRateName;
    }

    public void setPrimeRateName(String primeRateName) {
        this.primeRateName = primeRateName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String[] getPpfIds() {
        return ppfIds;
    }

    public void setPpfIds(String[] ppfIds) {
        this.ppfIds = ppfIds;
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

    public String getHasMoney() {
        return hasMoney;
    }

    public void setHasMoney(String hasMoney) {
        this.hasMoney = hasMoney;
    }

    public String getSquarePrice() {
        return squarePrice;
    }

    public void setSquarePrice(String squarePrice) {
        this.squarePrice = squarePrice;
    }

    public String getAdditionalAmount() {
        return additionalAmount;
    }

    public void setAdditionalAmount(String additionalAmount) {
        this.additionalAmount = additionalAmount;
    }

    public String[] getPayOrderIds() {
        return payOrderIds;
    }

    public void setPayOrderIds(String[] payOrderIds) {
        this.payOrderIds = payOrderIds;
    }

    public String getQueryEndTime() {
        return queryEndTime;
    }

    public void setQueryEndTime(String queryEndTime) {
        this.queryEndTime = queryEndTime;
    }

    public String getQueryStartTime() {
        return queryStartTime;
    }

    public void setQueryStartTime(String queryStartTime) {
        this.queryStartTime = queryStartTime;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }
}
