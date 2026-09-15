package com.java110.dto.invoice;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * 发票红冲单(只读展示用)
 * 物业端不建红冲表、不落冲红状态: 数据全部来自商城 invoice_red
 * (由商城 propertyInvoiceRedBmoImpl / queryInvoiceRedsBmoImpl 返回)
 */
public class InvoiceRedDto extends PageDto implements Serializable {

    /**
     * 待冲红(已落冲红单, 红字开票未完成, 可在商城后台重试)
     */
    public static final String STATE_WAIT = "WAIT";
    /**
     * 已冲红(已有红字发票号码)
     */
    public static final String STATE_DONE = "DONE";

    /**
     * 物业发票申请ID(对应商城 invoice_order.ext_apply_id)
     */
    private String applyId;
    private String invoiceId;
    private String redId;
    private String invoiceNo;
    private String buyerName;
    private String amount;
    private String reason;
    private String remark;
    private String state;
    private String redNo;

    /**
     * 查询条件: 批量物业发票申请ID(先查物业 invoice_apply 取集合, 再按此查商城红冲)
     */
    private String[] applyIds;

    /**
     * 发起红冲失败原因(仅本次响应返回, 商城不落库)
     */
    private String failReason;

    private String statusCd = "0";

    private Date createTime;

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getRedId() {
        return redId;
    }

    public void setRedId(String redId) {
        this.redId = redId;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public String getRedNo() {
        return redNo;
    }

    public void setRedNo(String redNo) {
        this.redNo = redNo;
    }

    public String[] getApplyIds() {
        return applyIds;
    }

    public void setApplyIds(String[] applyIds) {
        this.applyIds = applyIds;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
