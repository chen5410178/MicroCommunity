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
package com.java110.acct.bmo.invoice.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.acct.bmo.invoice.IInvoiceStateSyncBmo;
import com.java110.acct.bmo.invoice.IPropertyInvoiceSubmitBmo;
import com.java110.dto.MallDataDto;
import com.java110.dto.invoice.InvoiceApplyDto;
import com.java110.dto.invoice.InvoiceMallStateDto;
import com.java110.intf.acct.IInvoiceApplyV1InnerServiceSMO;
import com.java110.intf.job.IMallInnerServiceSMO;
import com.java110.po.invoice.InvoiceApplyItemPo;
import com.java110.po.invoice.InvoiceApplyPo;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 物业端提交开票申请实现
 * 只调商城一个开票接口(抬头随报文, 商城先 upsert 抬头再落开票单), 提交顺序为"先调商城、商城已建单才落物业记录",
 * 商城未建单时不写任何业务数据、失败原因摘要直达前端; 审核与开票进度全部以商城为准, 不在本地维护;
 * 发票PDF只存商城, 物业端查询时只读展示/下载
 */
@Service
public class PropertyInvoiceSubmitBmoImpl implements IPropertyInvoiceSubmitBmo {

    private static final Logger logger = LoggerFactory.getLogger(PropertyInvoiceSubmitBmoImpl.class);

    private static final String ORG_TYPE_COMMUNITY = "C";
    private static final String MALL_API_CODE = "propertyInvoiceBmoImpl";
    private static final String INVOICE_KIND_NORMAL = "普票";
    private static final String HEADING_TYPE_COMPANY = "公司";
    private static final String HEADING_TYPE_PERSONAL = "个人";
    private static final String OPEN_INVOICE_YES = "Y";
    private static final String OPEN_INVOICE_NO = "N";
    private static final String OPEN_INVOICE_ING = "D";
    private static final String INVOICE_TYPE_COMPANY = "2002";
    private static final String FAIL_REASON_SUBMIT_MALL = "提交商城开票申请失败";
    private static final String FAIL_REASON_CALL_MALL = "调用商城开票失败";
    private static final String FAIL_REASON_MALL_CLOSED = "商城未开启或返回数据为空(MALL_SWITCH)";
    private static final String FAIL_REASON_NO_INVOICE_ID = "商城未返回开票单号";
    private static final String FAIL_REASON_SQL_NOISE = "商城开票调用失败(详见服务日志)";
    private static final int REMARK_MAX_LEN = 200;
    private static final String[] SQL_NOISE_KEYWORDS = {"### Error updating database", "### SQL:", "nested exception"};

    @Autowired
    private IMallInnerServiceSMO mallInnerServiceSMOImpl;

    @Autowired
    private IInvoiceApplyV1InnerServiceSMO invoiceApplyV1InnerServiceSMOImpl;

    @Autowired
    private IInvoiceStateSyncBmo invoiceStateSyncBmoImpl;

    @Override
    public InvoiceMallStateDto submitMall(InvoiceApplyPo applyPo, List<InvoiceApplyItemPo> items,
                                          String headingType, String email, String bankNo) {
        InvoiceMallStateDto mallState = new InvoiceMallStateDto();
        mallState.setApplyId(applyPo.getApplyId());

        JSONObject data = buildMallData(applyPo, items, headingType, email, bankNo);
        ResultVo resultVo;
        try {
            resultVo = mallInnerServiceSMOImpl.postMallData(new MallDataDto(MALL_API_CODE, data));
        } catch (Exception e) {
            logger.error("提交商城开票申请异常 applyId: {}", applyPo.getApplyId(), e);
            mallState.setFailReason(safeFailReason(e.getMessage(), FAIL_REASON_SUBMIT_MALL));
            return mallState;
        }

        if (resultVo == null || resultVo.getCode() != ResultVo.CODE_OK) {
            mallState.setFailReason(safeFailReason(resultVo == null ? null : resultVo.getMsg(), FAIL_REASON_CALL_MALL));
            return mallState;
        }

        // MALL_SWITCH 未开启时商城返回 code=0 但无 data
        if (resultVo.getData() == null) {
            mallState.setFailReason(FAIL_REASON_MALL_CLOSED);
            return mallState;
        }

        JSONObject invoiceData = (JSONObject) resultVo.getData();
        String invoiceId = invoiceData.getString("invoiceId");
        if (StringUtil.isEmpty(invoiceId)) {
            mallState.setFailReason(FAIL_REASON_NO_INVOICE_ID);
            return mallState;
        }

        mallState.setInvoiceId(invoiceId);
        mallState.setState(invoiceData.getString("state"));
        mallState.setInvoiceNo(invoiceData.getString("invoiceNo"));
        mallState.setPdfUrl(invoiceData.getString("pdfUrl"));
        mallState.setFailReason(safeFailReason(invoiceData.getString("failReason"), null));
        return mallState;
    }

    @Override
    public InvoiceMallStateDto reSubmit(InvoiceApplyPo applyPo, List<InvoiceApplyItemPo> items) {
        String headingType = INVOICE_TYPE_COMPANY.equals(applyPo.getInvoiceType())
                ? HEADING_TYPE_COMPANY : HEADING_TYPE_PERSONAL;
        InvoiceMallStateDto mallState = submitMall(applyPo, items, headingType, null, null);
        if (StringUtil.isEmpty(mallState.getInvoiceId())) {
            // 商城未建单: 仅记录提交结果(F)与失败原因摘要, 不落任何发票业务状态
            updateSubmitFail(applyPo, mallState.getFailReason());
            return mallState;
        }
        // 商城已建单: 记录商城单号与提交结果(S), 审核/开票进度只读展示商城状态
        updateSubmitSuccess(applyPo, mallState);
        return mallState;
    }

    /**
     * 组装商城参数(物业申请单号+抬头信息+多费用明细): 商城先按 phone(headingType) upsert 抬头, 再落开票单
     */
    private JSONObject buildMallData(InvoiceApplyPo applyPo, List<InvoiceApplyItemPo> items,
                                     String headingType, String email, String bankNo) {
        JSONObject data = new JSONObject();
        data.put("applyId", applyPo.getApplyId());
        data.put("orgId", applyPo.getCommunityId());
        data.put("orgType", ORG_TYPE_COMMUNITY);
        data.put("headingType", headingType);
        data.put("buyerName", applyPo.getInvoiceName());
        data.put("buyerTaxNo", applyPo.getInvoiceNum());
        data.put("buyerAddress", applyPo.getInvoiceAddress());
        data.put("buyerTel", applyPo.getApplyTel());
        data.put("buyerBankNo", bankNo);
        data.put("email", email);
        String invoiceKind = applyPo.getInvoiceKind();
        if (StringUtil.isEmpty(invoiceKind)) {
            invoiceKind = INVOICE_KIND_NORMAL;
        }
        data.put("invoiceType", invoiceKind);
        data.put("amount", applyPo.getInvoiceAmount());
        JSONArray itemArray = new JSONArray();
        String firstBizCategory = "";
        if (!ListUtil.isNull(items)) {
            for (InvoiceApplyItemPo item : items) {
                JSONObject itemJson = new JSONObject();
                itemJson.put("bizCategory", item.getItemName());
                itemJson.put("amount", item.getItemAmount());
                itemArray.add(itemJson);
                if (StringUtil.isEmpty(firstBizCategory)) {
                    firstBizCategory = item.getItemName();
                }
            }
        }
        data.put("items", itemArray);
        data.put("bizCategory", firstBizCategory);
        data.put("remark", safeRemark(applyPo.getApplyId() + "|" + applyPo.getOwnerName() + "|" + firstBizCategory));
        return data;
    }

    /**
     * 提交成功: 回写商城单号与提交结果(S) + 按商城即时结果回写缴费明细开票状态
     * (发票PDF只存商城 invoice_order.pdf_url, 物业端不落库, 查询时读取商城地址直接下载)
     */
    private void updateSubmitSuccess(InvoiceApplyPo applyPo, InvoiceMallStateDto mallState) {
        InvoiceApplyPo updatePo = new InvoiceApplyPo();
        updatePo.setApplyId(applyPo.getApplyId());
        updatePo.setCommunityId(applyPo.getCommunityId());
        updatePo.setOiId(mallState.getInvoiceId());
        updatePo.setState(InvoiceApplyDto.STATE_SUBMIT);
        if (InvoiceMallStateDto.STATE_DONE.equals(mallState.getState())
                && !StringUtil.isEmpty(mallState.getInvoiceNo())) {
            updatePo.setInvoiceCode(mallState.getInvoiceNo());
        }
        invoiceApplyV1InnerServiceSMOImpl.updateInvoiceApply(updatePo);

        writeBackFeeDetail(applyPo.getApplyId(), mallState.getState());
    }

    /**
     * 提交失败: 仅记录提交结果(F)与失败原因(摘要, ≤200), 不落任何发票业务状态;
     * 回写异常只记日志, 失败原因已随响应直达前端
     */
    private void updateSubmitFail(InvoiceApplyPo applyPo, String failReason) {
        if (StringUtil.isEmpty(failReason)) {
            failReason = FAIL_REASON_SUBMIT_MALL;
        }
        InvoiceApplyPo updatePo = new InvoiceApplyPo();
        updatePo.setApplyId(applyPo.getApplyId());
        updatePo.setCommunityId(applyPo.getCommunityId());
        updatePo.setState(InvoiceApplyDto.STATE_SUBMIT_FAIL);
        updatePo.setRemark(safeRemark(failReason));
        try {
            invoiceApplyV1InnerServiceSMOImpl.updateInvoiceApply(updatePo);
        } catch (Exception e) {
            logger.error("回写提交失败结果异常 applyId: {}", applyPo.getApplyId(), e);
        }
    }

    /**
     * 按商城结果回写缴费明细开票状态: 开票成功Y / 开票失败N / 待审核与待开票D(开票中)
     */
    private void writeBackFeeDetail(String applyId, String mallState) {
        if (InvoiceMallStateDto.STATE_DONE.equals(mallState)) {
            invoiceStateSyncBmoImpl.writeBackFeeDetail(applyId, OPEN_INVOICE_YES);
            return;
        }
        if (InvoiceMallStateDto.STATE_FAIL.equals(mallState)) {
            invoiceStateSyncBmoImpl.writeBackFeeDetail(applyId, OPEN_INVOICE_NO);
            return;
        }
        invoiceStateSyncBmoImpl.writeBackFeeDetail(applyId, OPEN_INVOICE_ING);
    }

    /**
     * 所有写 remark 的入参统一: 压缩连续空白 + 截断至列宽以内(invoice_apply/item.remark 均 varchar(200))
     */
    private String safeRemark(String text) {
        if (StringUtil.isEmpty(text)) {
            return text;
        }
        return StringUtil.truncateMessage(text.replaceAll("\\s+", " ").trim(), REMARK_MAX_LEN);
    }

    /**
     * 失败原因摘要化: 原文入日志, 含 MyBatis/SQL 堆栈时仅保留首个可读片段, 再按 200 截断
     */
    private String safeFailReason(String reason, String fallback) {
        if (StringUtil.isEmpty(reason)) {
            return fallback;
        }
        logger.warn("商城开票失败原因(原文): {}", reason);
        String summary = reason;
        if (containsSqlNoise(reason)) {
            summary = firstReadableFragment(reason);
        }
        return safeRemark(summary);
    }

    private boolean containsSqlNoise(String text) {
        for (String keyword : SQL_NOISE_KEYWORDS) {
            if (text.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 长 SQL 异常文本按 "###" 分段, 取首个可读片段(跳过定位信息与 SQL 语句本身)
     */
    private String firstReadableFragment(String reason) {
        String[] fragments = reason.split("###");
        for (String fragment : fragments) {
            String text = fragment.replaceAll("\\s+", " ").trim();
            if (StringUtil.isEmpty(text)) {
                continue;
            }
            if (text.startsWith("The error may") || text.startsWith("SQL:") || text.startsWith("Cause:")) {
                continue;
            }
            return text;
        }
        return FAIL_REASON_SQL_NOISE;
    }
}
