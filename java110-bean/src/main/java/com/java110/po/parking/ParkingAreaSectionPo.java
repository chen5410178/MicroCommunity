package com.java110.po.parking;

import java.io.Serializable;

/**
 * @ClassName ParkingAreaSectionPo
 * @Description 停车区域持久化对象
 * @Author wuxw
 * @Date 2026/8/27
 * @Version 1.0
 * add by wuxw 2026/8/27
 **/
public class ParkingAreaSectionPo implements Serializable {

    private String sectionId;
    private String paId;
    private String communityId;
    private String sectionNum;
    private String sectionName;
    private String remark;
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

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
}
