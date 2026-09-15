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
import com.java110.dto.invoice.InvoiceApplyItemDto;
import com.java110.dto.invoice.InvoiceMallStateDto;
import com.java110.intf.acct.IInvoiceApplyV1InnerServiceSMO;
import com.java110.intf.job.IMallInnerServiceSMO;
import com.java110.po.invoice.InvoiceApplyPo;
import com.java110.utils.exception.CmdException;
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
 * 只调商城一个开票接口(抬头随报文, 商城先 upsert 抬头再落开票单), 只记录提交结果(S 已提交 / F 提交失败),
 * 审核与开票进度全部以商城为准, 不在本地维护; 发票PDF只存商城, 物业端查询时只读展示/下载
 */
@Service
public class PropertyInvoiceSubmitBmoImpl implements IPropertyInvoiceSubmitBmo {

    private static final Logger logger = LoggerFactory.getLogger(PropertyInvoiceSubmitBmoImpl.class);

    private static final String ORG_TYPE_COMMUNITY = "C";
    private static final String MALL_API_CODE = "propertyInvoiceBmoImpl";
    private static final String OPEN_INVOICE_YES = "Y";
    private static final String OPEN_INVOICE_NO = "N";
    private static final String FAIL_REASON_MALL_CLOSED = "商城未开启或返回数据为空(MALL_SWITCH)";
    private static final String FAIL_REASON_NO_INVOICE_ID = "商城未返回开票单号";

    @Autowired
    private IMallInnerServiceSMO mallInnerServiceSMOImpl;

    @Autowired
    private IInvoiceApplyV1InnerServiceSMO invoiceApplyV1InnerServiceSMOImpl;

    @Autowired
    private IInvoiceStateSyncBmo invoiceStateSyncBmoImpl;

    @Override
    public InvoiceApplyDto submit(String applyId, String communityId, String headingType, String buyerName,
                                  String buyerTaxNo, String buyerAddress, String applyTel, String email,
                                  String bankNo, List<InvoiceApplyItemDto> items) {

        InvoiceApplyDto applyQuery = new InvoiceApplyDto();
        applyQuery.setApplyId(applyId);
        applyQuery.setCommunityId(communityId);
        List<InvoiceApplyDto> applies = invoiceApplyV1InnerServiceSMOImpl.queryInvoiceApplys(applyQuery);
        if (ListUtil.isNull(applies)) {
            throw new CmdException("发票申请不存在");
        }
        InvoiceApplyDto invoiceApply = applies.get(0);

        // 组装商城参数(物业申请单号+抬头信息+多费用明细): 商城先按 phone(headingType) upsert 抬头, 再落开票单
        JSONObject data = new JSONObject();
        data.put("applyId", applyId);
        data.put("orgId", communityId);
        data.put("orgType", ORG_TYPE_COMMUNITY);
        data.put("headingType", headingType);
        data.put("buyerName", buyerName);
        data.put("buyerTaxNo", buyerTaxNo);
        data.put("buyerAddress", buyerAddress);
        data.put("buyerTel", applyTel);
        data.put("buyerBankNo", bankNo);
        data.put("email", email);
        String invoiceKind = invoiceApply.getInvoiceKind();
        if (StringUtil.isEmpty(invoiceKind)) {
            invoiceKind = "普票";
        }
        data.put("invoiceType", invoiceKind);
        data.put("amount", invoiceApply.getInvoiceAmount());
        JSONArray itemArray = new JSONArray();
        String firstBizCategory = "";
        if (!ListUtil.isNull(items)) {
            for (InvoiceApplyItemDto item : items) {
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
        data.put("remark", applyId + "|" + invoiceApply.getOwnerName() + "|" + firstBizCategory);

        ResultVo resultVo = null;
        try {
            resultVo = mallInnerServiceSMOImpl.postMallData(new MallDataDto(MALL_API_CODE, data));
        } catch (Exception e) {
            logger.error("提交商城开票申请异常 applyId: {}", applyId, e);
            return updateSubmitFail(invoiceApply, e.getMessage());
        }

        if (resultVo == null || resultVo.getCode() != ResultVo.CODE_OK) {
            return updateSubmitFail(invoiceApply, resultVo == null ? "调用商城开票失败" : resultVo.getMsg());
        }

        // MALL_SWITCH 未开启时商城返回 code=0 但无 data
        if (resultVo.getData() == null) {
            return updateSubmitFail(invoiceApply, FAIL_REASON_MALL_CLOSED);
        }

        JSONObject invoiceData = (JSONObject) resultVo.getData();
        String invoiceId = invoiceData.getString("invoiceId");
        if (StringUtil.isEmpty(invoiceId)) {
            return updateSubmitFail(invoiceApply, FAIL_REASON_NO_INVOICE_ID);
        }

        // 提交成功: 记录商城单号与提交结果(S), 审核/开票进度只读展示商城状态
        String mallState = invoiceData.getString("state");
        String invoiceNo = invoiceData.getString("invoiceNo");
        String failReason = invoiceData.getString("failReason");
        InvoiceApplyPo updatePo = new InvoiceApplyPo();
        updatePo.setApplyId(applyId);
        updatePo.setCommunityId(communityId);
        updatePo.setOiId(invoiceId);
        updatePo.setState(InvoiceApplyDto.STATE_SUBMIT);
        if (InvoiceMallStateDto.STATE_DONE.equals(mallState) && !StringUtil.isEmpty(invoiceNo)) {
            updatePo.setInvoiceCode(invoiceNo);
        }
        invoiceApplyV1InnerServiceSMOImpl.updateInvoiceApply(updatePo);

        // 按商城即时结果回写缴费明细(已开票Y/开票失败N, 待审核或待开票不动)
        // 发票PDF只存商城(invoice_order.pdf_url), 物业端不落库, 查询时读取商城地址直接下载
        if (InvoiceMallStateDto.STATE_DONE.equals(mallState)) {
            invoiceStateSyncBmoImpl.writeBackFeeDetail(applyId, OPEN_INVOICE_YES);
            invoiceApply.setMallPdfUrl(invoiceData.getString("pdfUrl"));
        } else if (InvoiceMallStateDto.STATE_FAIL.equals(mallState)) {
            invoiceStateSyncBmoImpl.writeBackFeeDetail(applyId, OPEN_INVOICE_NO);
        }

        invoiceApply.setOiId(invoiceId);
        invoiceApply.setState(InvoiceApplyDto.STATE_SUBMIT);
        invoiceApply.setMallState(mallState);
        invoiceApply.setFailReason(failReason);
        if (!StringUtil.isEmpty(invoiceNo)) {
            invoiceApply.setInvoiceCode(invoiceNo);
        }
        return invoiceApply;
    }

    /**
     * 提交失败: 仅记录提交结果(F)与失败原因, 不落任何发票业务状态
     */
    private InvoiceApplyDto updateSubmitFail(InvoiceApplyDto invoiceApply, String failReason) {
        if (StringUtil.isEmpty(failReason)) {
            failReason = "提交商城开票申请失败";
        }
        InvoiceApplyPo updatePo = new InvoiceApplyPo();
        updatePo.setApplyId(invoiceApply.getApplyId());
        updatePo.setCommunityId(invoiceApply.getCommunityId());
        updatePo.setState(InvoiceApplyDto.STATE_SUBMIT_FAIL);
        updatePo.setRemark(failReason);
        invoiceApplyV1InnerServiceSMOImpl.updateInvoiceApply(updatePo);

        invoiceApply.setState(InvoiceApplyDto.STATE_SUBMIT_FAIL);
        invoiceApply.setFailReason(failReason);
        invoiceApply.setRemark(failReason);
        return invoiceApply;
    }
}
