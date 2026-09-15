package com.java110.dto.invoice;

import java.io.Serializable;

/**
 * 商城开票单状态(物业端只读展示用, 由商城 propertyInvoiceBmoImpl/queryInvoiceStatus(Batch)BmoImpl 返回)
 */
public class InvoiceMallStateDto implements Serializable {

    public static final String STATE_AUDIT = "AUDIT";//待审核
    public static final String STATE_WAIT = "WAIT";//待开票
    public static final String STATE_DONE = "DONE";//开票成功
    public static final String STATE_FAIL = "FAIL";//开票失败
    public static final String STATE_RED = "RED";//已冲红

    /**
     * 物业端发票申请ID(对应商城 invoice_order.ext_apply_id)
     */
    private String applyId;
    private String invoiceId;
    private String state;
    private String invoiceNo;
    private String invoiceCode;
    private String pdfUrl;
    private String failReason;

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }
}
