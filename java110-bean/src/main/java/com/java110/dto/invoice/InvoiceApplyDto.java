package com.java110.dto.invoice;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 发票申请数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class InvoiceApplyDto extends PageDto implements Serializable {

    public static final String STATE_SUBMIT = "S";//S 已提交(提交成功, 审核/开票进度以商城为准)
    public static final String STATE_SUBMIT_FAIL = "F";//F 提交失败(可重新提交)

    /**
     * @deprecated 物业端不再维护发票状态机(审核/上传/登记/领用), 保留常量仅用于历史数据兼容
     */
    @Deprecated
    public static final String STATE_WAIT = "W";//W待审核 U 待上传 F 审核失败 G 带领用 C 已领用
    /**
     * @deprecated 物业端不再维护发票状态机
     */
    @Deprecated
    public static final String STATE_UPLOAD = "U";//W待审核 U 待上传 F 审核失败 G 带领用 C 已领用
    /**
     * @deprecated 物业端不再维护发票状态机, 用 STATE_SUBMIT_FAIL 替代
     */
    @Deprecated
    public static final String STATE_FAIL = "F";//W待审核 U 待上传 F 审核失败 G 带领用 C 已领用
    /**
     * @deprecated 物业端不再维护发票状态机
     */
    @Deprecated
    public static final String STATE_GET = "G";//W待审核 U 待上传 F 审核失败 G 带领用 C 已领用
    /**
     * @deprecated 物业端不再维护发票状态机
     */
    @Deprecated
    public static final String STATE_COMPLETE = "C";//W待审核 U 待上传 F 审核失败 G 带领用 C 已领用



    private String applyId;
    private String oiId;
    private String createUserId;
    private String ownerName;
    private String invoiceType;
    private String invoiceKind;
    private String invoiceName;
    private String invoiceNum;
    private String invoiceAddress;
    private String invoiceLink;
    private String invoiceAccount;
    private String ownerId;
    private String applyTel;
    private String invoiceAmount;
    private String createUserName;
    private String remark;
    private String state;
    private String stateName;
    private String communityId;
    private String roomId;
    private String roomName;

    /**
     * 查询条件: 房屋编号模糊匹配
     */
    private String roomNameLike;

    private String invoiceCode;

    /**
     * 商城开票单状态(只读展示, 实时从商城读取: AUDIT/WAIT/DONE/FAIL/RED)
     */
    private String mallState;

    /**
     * 商城开票失败原因(只读展示)
     */
    private String failReason;

    /**
     * 商城发票PDF地址(只读展示/下载, 商城 invoice_order.pdf_url 为唯一存放方, 物业端不落库)
     */
    private String mallPdfUrl;

    /**
     * 查询条件: 排除的开票单号(定时任务扫描已提交商城(oi_id != '-1')的单)
     */
    private String oiIdNotEqual;

    /**
     * 查询条件: 批量申请ID(按商城 ext_apply_id 批量取状态用)
     */
    private String[] applyIds;




    private Date createTime;

    private String statusCd = "0";

    private List<String> urls;


    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public String getOiId() {
        return oiId;
    }

    public void setOiId(String oiId) {
        this.oiId = oiId;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getInvoiceKind() {
        return invoiceKind;
    }

    public void setInvoiceKind(String invoiceKind) {
        this.invoiceKind = invoiceKind;
    }

    public String getInvoiceName() {
        return invoiceName;
    }

    public void setInvoiceName(String invoiceName) {
        this.invoiceName = invoiceName;
    }

    public String getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public String getInvoiceAddress() {
        return invoiceAddress;
    }

    public void setInvoiceAddress(String invoiceAddress) {
        this.invoiceAddress = invoiceAddress;
    }

    public String getInvoiceLink() {
        return invoiceLink;
    }

    public void setInvoiceLink(String invoiceLink) {
        this.invoiceLink = invoiceLink;
    }

    public String getInvoiceAccount() {
        return invoiceAccount;
    }

    public void setInvoiceAccount(String invoiceAccount) {
        this.invoiceAccount = invoiceAccount;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getApplyTel() {
        return applyTel;
    }

    public void setApplyTel(String applyTel) {
        this.applyTel = applyTel;
    }

    public String getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(String invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomNameLike() {
        return roomNameLike;
    }

    public void setRoomNameLike(String roomNameLike) {
        this.roomNameLike = roomNameLike;
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

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public String getMallState() {
        return mallState;
    }

    public void setMallState(String mallState) {
        this.mallState = mallState;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public String getMallPdfUrl() {
        return mallPdfUrl;
    }

    public void setMallPdfUrl(String mallPdfUrl) {
        this.mallPdfUrl = mallPdfUrl;
    }

    public String getOiIdNotEqual() {
        return oiIdNotEqual;
    }

    public void setOiIdNotEqual(String oiIdNotEqual) {
        this.oiIdNotEqual = oiIdNotEqual;
    }

    public String[] getApplyIds() {
        return applyIds;
    }

    public void setApplyIds(String[] applyIds) {
        this.applyIds = applyIds;
    }
}
