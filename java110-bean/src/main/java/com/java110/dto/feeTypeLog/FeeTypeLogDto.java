package com.java110.dto.feeTypeLog;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 费用记录数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class FeeTypeLogDto extends PageDto implements Serializable {

    private String orgMonthAmount;
private String monthAmount;
private String staffName;
private String ftId;
private String logId;
private String ftName;
private String communityId;


    private Date createTime;

    private String statusCd = "0";


    public String getOrgMonthAmount() {
        return orgMonthAmount;
    }
public void setOrgMonthAmount(String orgMonthAmount) {
        this.orgMonthAmount = orgMonthAmount;
    }
public String getMonthAmount() {
        return monthAmount;
    }
public void setMonthAmount(String monthAmount) {
        this.monthAmount = monthAmount;
    }
public String getStaffName() {
        return staffName;
    }
public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
public String getFtId() {
        return ftId;
    }
public void setFtId(String ftId) {
        this.ftId = ftId;
    }
public String getLogId() {
        return logId;
    }
public void setLogId(String logId) {
        this.logId = logId;
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
}
