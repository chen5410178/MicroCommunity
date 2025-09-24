package com.java110.dto.fee;

import com.alibaba.fastjson.JSONArray;
import com.java110.dto.PageDto;
import com.java110.dto.user.UserDto;

import java.io.Serializable;

/**
 * @ClassName PayFeePo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/27 22:57
 * @Version 1.0
 * add by wuxw 2020/5/27
 **/
public class BatchPayFeeDto extends PageDto implements Serializable {
    public BatchPayFeeDto(){}

    public BatchPayFeeDto(String communityId, String primeRate, String acctId, String payOrderId, String remark, UserDto userDto, JSONArray feeConfigs) {
        this.communityId = communityId;
        this.primeRate = primeRate;
        this.acctId = acctId;
        this.payOrderId = payOrderId;
        this.remark = remark;
        this.userDto = userDto;
        this.feeConfigs = feeConfigs;
    }

    private String communityId;
    private String primeRate;
    private String acctId;
    private String payOrderId;
    private String remark;
    private UserDto userDto;
    private JSONArray feeConfigs;

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getPrimeRate() {
        return primeRate;
    }

    public void setPrimeRate(String primeRate) {
        this.primeRate = primeRate;
    }

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    public String getPayOrderId() {
        return payOrderId;
    }

    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public JSONArray getFeeConfigs() {
        return feeConfigs;
    }

    public void setFeeConfigs(JSONArray feeConfigs) {
        this.feeConfigs = feeConfigs;
    }
}
