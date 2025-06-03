package com.java110.dto.storeStaffLeader;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 员工领导数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class StoreStaffLeaderDto extends PageDto implements Serializable {

    private String leaderName;
private String storeId;
private String sslId;
private String staffId;
private String leaderId;
private String relCd;


    private Date createTime;

    private String statusCd = "0";


    public String getLeaderName() {
        return leaderName;
    }
public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }
public String getStoreId() {
        return storeId;
    }
public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
public String getSslId() {
        return sslId;
    }
public void setSslId(String sslId) {
        this.sslId = sslId;
    }
public String getStaffId() {
        return staffId;
    }
public void setStaffId(String staffId) {
        this.staffId = staffId;
    }
public String getLeaderId() {
        return leaderId;
    }
public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }
public String getRelCd() {
        return relCd;
    }
public void setRelCd(String relCd) {
        this.relCd = relCd;
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
