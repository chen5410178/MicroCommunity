/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.po.prePayFeeDetail;

import java.io.Serializable;

/**
 * 类表述： Po 数据模型实体对象 基本保持与数据库模型一直 用于 增加修改删除 等时的数据载体
 * add by 吴学文 at 2025-06-25 10:34:36 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
public class PrePayFeeDetailPo implements Serializable {

    private String feeTypeCd;
private String curYearMonth;
private String detailId;
private String remark;
private String statusCd = "0";
private String ownerId;
private String ftName;
private String feeId;
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
public String getStatusCd() {
        return statusCd;
    }
public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
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



}
