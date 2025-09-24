package com.java110.dto.feeType;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 费用类型数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class FeeTypeDto extends PageDto implements Serializable {

    private String feeTypeCd;
    private String feeTypeCdName;
    private String[] feeTypeCds;
    private String ftId;
    private String ftName;
    private String communityId;

    private String monthAmount;

    private Date createTime;

    private String statusCd = "0";


    public String getFeeTypeCd() {
        return feeTypeCd;
    }

    public void setFeeTypeCd(String feeTypeCd) {
        this.feeTypeCd = feeTypeCd;
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

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
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

    public String[] getFeeTypeCds() {
        return feeTypeCds;
    }

    public void setFeeTypeCds(String[] feeTypeCds) {
        this.feeTypeCds = feeTypeCds;
    }

    public String getMonthAmount() {
        return monthAmount;
    }

    public void setMonthAmount(String monthAmount) {
        this.monthAmount = monthAmount;
    }

    public String getFeeTypeCdName() {
        return feeTypeCdName;
    }

    public void setFeeTypeCdName(String feeTypeCdName) {
        this.feeTypeCdName = feeTypeCdName;
    }
}
