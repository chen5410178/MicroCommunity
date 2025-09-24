package com.java110.dto.prePayFeeDetail;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 扣款记录数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class PrePayFeeDetailDto extends PageDto implements Serializable {

    private String feeTypeCd;
    private String curYearMonth;
    private String detailId;
    private String remark;
    private String ownerId;
    private String ftName;
    private String feeId;

    private String feeName;
    private String preAmount;
    private String payerObjName;
    private String ownerName;
    private String remainAmount;
    private String hisOweAmount;
    private String ftId;
    private String communityId;
    private String curAmount;
    private String curOweAmount;
    private String ppfId;
    private String payerObjId;


    private Date createTime;

    private String statusCd = "0";


    public String getFeeTypeCd() {
        return feeTypeCd;
    }

    public void setFeeTypeCd(String feeTypeCd) {
        this.feeTypeCd = feeTypeCd;
    }

    public String getCurYearMonth() {
        return curYearMonth;
    }

    public void setCurYearMonth(String curYearMonth) {
        this.curYearMonth = curYearMonth;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getFtName() {
        return ftName;
    }

    public void setFtName(String ftName) {
        this.ftName = ftName;
    }

    public String getFeeId() {
        return feeId;
    }

    public void setFeeId(String feeId) {
        this.feeId = feeId;
    }

    public String getPreAmount() {
        return preAmount;
    }

    public void setPreAmount(String preAmount) {
        this.preAmount = preAmount;
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

    public String getHisOweAmount() {
        return hisOweAmount;
    }

    public void setHisOweAmount(String hisOweAmount) {
        this.hisOweAmount = hisOweAmount;
    }

    public String getFtId() {
        return ftId;
    }

    public void setFtId(String ftId) {
        this.ftId = ftId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getCurAmount() {
        return curAmount;
    }

    public void setCurAmount(String curAmount) {
        this.curAmount = curAmount;
    }

    public String getCurOweAmount() {
        return curOweAmount;
    }

    public void setCurOweAmount(String curOweAmount) {
        this.curOweAmount = curOweAmount;
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

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }
}
