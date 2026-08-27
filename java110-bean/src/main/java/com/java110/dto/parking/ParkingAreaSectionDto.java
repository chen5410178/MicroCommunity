package com.java110.dto.parking;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName ParkingAreaSectionDto
 * @Description 停车区域数据层封装
 * @Author wuxw
 * @Date 2026/8/27
 * @Version 1.0
 * add by wuxw 2026/8/27
 **/
public class ParkingAreaSectionDto extends PageDto implements Serializable {

    private String sectionId;
    private String paId;
    private String paName;
    private String communityId;
    private String sectionNum;
    private String sectionName;
    private String remark;
    private String hasOwnerSpace;

    private Date createTime;

    private String statusCd = "0";

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getPaId() {
        return paId;
    }

    public void setPaId(String paId) {
        this.paId = paId;
    }

    public String getPaName() {
        return paName;
    }

    public void setPaName(String paName) {
        this.paName = paName;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getSectionNum() {
        return sectionNum;
    }

    public void setSectionNum(String sectionNum) {
        this.sectionNum = sectionNum;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getHasOwnerSpace() {
        return hasOwnerSpace;
    }

    public void setHasOwnerSpace(String hasOwnerSpace) {
        this.hasOwnerSpace = hasOwnerSpace;
    }
}
