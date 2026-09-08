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
import com.java110.acct.bmo.invoice.IIssueMallInvoiceBmo;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.MallDataDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.invoice.InvoiceApplyDto;
import com.java110.dto.invoice.InvoiceApplyItemDto;
import com.java110.dto.invoice.InvoiceEventDto;
import com.java110.intf.acct.IInvoiceApplyItemV1InnerServiceSMO;
import com.java110.intf.acct.IInvoiceApplyV1InnerServiceSMO;
import com.java110.intf.acct.IInvoiceEventV1InnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.fee.IPayFeeDetailV1InnerServiceSMO;
import com.java110.intf.job.IMallInnerServiceSMO;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.file.FileRelPo;
import com.java110.po.invoice.InvoiceApplyPo;
import com.java110.po.invoice.InvoiceEventPo;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 调商城物业开票接口(propertyInvoiceBmoImpl)实现
 */
@Service
public class IssueMallInvoiceBmoImpl implements IIssueMallInvoiceBmo {

    private static Logger logger = LoggerFactory.getLogger(IssueMallInvoiceBmoImpl.class);

    private static final String ORG_TYPE_COMMUNITY = "C";
    private static final String STATE_DONE = "DONE";
    private static final String FAIL_REASON_UNCONFIGURED = "商城开票未生效(未开启商城或返回异常)";

    @Autowired
    private IMallInnerServiceSMO mallInnerServiceSMOImpl;

    @Autowired
    private IInvoiceApplyV1InnerServiceSMO invoiceApplyV1InnerServiceSMOImpl;

    @Autowired
    private IInvoiceApplyItemV1InnerServiceSMO invoiceApplyItemV1InnerServiceSMOImpl;

    @Autowired
    private IInvoiceEventV1InnerServiceSMO invoiceEventV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeDetailV1InnerServiceSMO payFeeDetailV1InnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Override
    public boolean issue(String applyId, String communityId, String buyerName,
                         String buyerTaxNo, String buyerAddress, String applyTel,
                         List<InvoiceApplyItemDto> items) {

        InvoiceApplyDto applyDto = new InvoiceApplyDto();
        applyDto.setApplyId(applyId);
        applyDto.setCommunityId(communityId);
        List<InvoiceApplyDto> applies = invoiceApplyV1InnerServiceSMOImpl.queryInvoiceApplys(applyDto);
        if (ListUtil.isNull(applies)) {
            logger.warn("发票申请不存在, 跳过开票 applyId: {}", applyId);
            return false;
        }
        InvoiceApplyDto invoiceApply = applies.get(0);

        // 幂等: 已开票(G)/已领用(C)不重复调用
        if (InvoiceApplyDto.STATE_GET.equals(invoiceApply.getState())
                || InvoiceApplyDto.STATE_COMPLETE.equals(invoiceApply.getState())) {
            logger.info("发票申请已开票, 跳过开票 applyId: {}", applyId);
            return true;
        }

        // 组装商城参数(多费用明细+抬头信息)
        JSONObject data = new JSONObject();
        data.put("orgId", communityId);
        data.put("orgType", ORG_TYPE_COMMUNITY);
        data.put("buyerName", buyerName);
        data.put("buyerTaxNo", buyerTaxNo);
        data.put("buyerAddress", buyerAddress);
        data.put("buyerTel", applyTel);
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
            resultVo = mallInnerServiceSMOImpl.postMallData(new MallDataDto("propertyInvoiceBmoImpl", data));
        } catch (Exception e) {
            logger.error("调商城开票异常 applyId: {}", applyId, e);
            updateFail(applyId, communityId, invoiceApply, e.getMessage());
            return false;
        }

        if (resultVo == null || resultVo.getCode() != ResultVo.CODE_OK || resultVo.getData() == null) {
            // MALL_SWITCH 未开启时商城返回 code=0 但无 data
            updateFail(applyId, communityId, invoiceApply, FAIL_REASON_UNCONFIGURED);
            return false;
        }

        JSONObject invoiceData = (JSONObject) resultVo.getData();
        if (invoiceData == null || StringUtil.isEmpty(invoiceData.getString("invoiceId"))) {
            updateFail(applyId, communityId, invoiceApply, FAIL_REASON_UNCONFIGURED);
            return false;
        }

        if (STATE_DONE.equals(invoiceData.getString("state"))) {
            updateSuccess(applyId, communityId, invoiceApply, invoiceData);
            return true;
        }

        String failReason = invoiceData.getString("failReason");
        if (StringUtil.isEmpty(failReason)) {
            failReason = "商城开票失败, 状态: " + invoiceData.getString("state");
        }
        updateFail(applyId, communityId, invoiceApply, failReason);
        return false;
    }

    /**
     * 开票成功回写: 状态G + 发票号 + pdfUrl(file_rel 29000) + pay_fee_detail Y + 事件1001
     */
    private void updateSuccess(String applyId, String communityId, InvoiceApplyDto invoiceApply, JSONObject invoiceData) {
        InvoiceApplyPo invoiceApplyPo = new InvoiceApplyPo();
        invoiceApplyPo.setApplyId(applyId);
        invoiceApplyPo.setCommunityId(communityId);
        invoiceApplyPo.setState(InvoiceApplyDto.STATE_GET);
        invoiceApplyPo.setInvoiceCode(invoiceData.getString("invoiceNo"));
        invoiceApplyV1InnerServiceSMOImpl.updateInvoiceApply(invoiceApplyPo);

        // 保存电子发票PDF链接(29000)
        String pdfUrl = invoiceData.getString("pdfUrl");
        if (!StringUtil.isEmpty(pdfUrl)) {
            FileRelPo deleteFileRelPo = new FileRelPo();
            deleteFileRelPo.setObjId(applyId);
            deleteFileRelPo.setRelTypeCd(FileRelDto.REL_TYPE_CE_INVOICE);
            fileRelInnerServiceSMOImpl.deleteFileRel(deleteFileRelPo);

            FileRelPo fileRelPo = new FileRelPo();
            fileRelPo.setObjId(applyId);
            fileRelPo.setRelTypeCd(FileRelDto.REL_TYPE_CE_INVOICE);
            fileRelPo.setFileRelId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_fileRelId));
            fileRelPo.setSaveWay("OSS");
            fileRelPo.setFileRealName(pdfUrl);
            fileRelPo.setFileSaveName(pdfUrl);
            fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);
        }

        // 缴费明细开票状态置为完成
        updatePayFeeDetail(applyId, "Y");

        // 写事件(1001 开票成功)
        InvoiceEventPo invoiceEventPo = new InvoiceEventPo();
        invoiceEventPo.setApplyId(applyId);
        invoiceEventPo.setCommunityId(communityId);
        invoiceEventPo.setEventId(GenerateCodeFactory.getGeneratorId("11"));
        invoiceEventPo.setEventType(InvoiceEventDto.STATE_COMPLETE);
        invoiceEventPo.setCreateUserId(invoiceApply.getCreateUserId());
        invoiceEventPo.setCreateUserName(invoiceApply.getCreateUserName());
        invoiceEventPo.setRemark("商城开票成功，发票号：" + invoiceData.getString("invoiceNo"));
        invoiceEventV1InnerServiceSMOImpl.saveInvoiceEvent(invoiceEventPo);
    }

    /**
     * 开票失败回写: 状态F + 失败原因 + pay_fee_detail N + 事件3003
     */
    private void updateFail(String applyId, String communityId, InvoiceApplyDto invoiceApply, String failReason) {
        InvoiceApplyPo invoiceApplyPo = new InvoiceApplyPo();
        invoiceApplyPo.setApplyId(applyId);
        invoiceApplyPo.setCommunityId(communityId);
        invoiceApplyPo.setState(InvoiceApplyDto.STATE_FAIL);
        invoiceApplyPo.setRemark(failReason);
        invoiceApplyV1InnerServiceSMOImpl.updateInvoiceApply(invoiceApplyPo);

        // 缴费明细开票状态置为待开票
        updatePayFeeDetail(applyId, "N");

        InvoiceEventPo invoiceEventPo = new InvoiceEventPo();
        invoiceEventPo.setApplyId(applyId);
        invoiceEventPo.setCommunityId(communityId);
        invoiceEventPo.setEventId(GenerateCodeFactory.getGeneratorId("11"));
        invoiceEventPo.setEventType(InvoiceEventDto.STATE_FAIL);
        invoiceEventPo.setCreateUserId(invoiceApply.getCreateUserId());
        invoiceEventPo.setCreateUserName(invoiceApply.getCreateUserName());
        invoiceEventPo.setRemark("商城开票失败：" + failReason);
        invoiceEventV1InnerServiceSMOImpl.saveInvoiceEvent(invoiceEventPo);
    }

    /**
     * 回写缴费明细开票状态
     */
    private void updatePayFeeDetail(String applyId, String openInvoice) {
        InvoiceApplyItemDto itemDto = new InvoiceApplyItemDto();
        itemDto.setApplyId(applyId);
        itemDto.setItemType(InvoiceApplyItemDto.ITEM_TYPE_FEE);
        List<InvoiceApplyItemDto> items = invoiceApplyItemV1InnerServiceSMOImpl.queryInvoiceApplyItems(itemDto);
        if (ListUtil.isNull(items)) {
            return;
        }
        for (InvoiceApplyItemDto item : items) {
            PayFeeDetailPo payFeeDetailPo = new PayFeeDetailPo();
            payFeeDetailPo.setDetailId(item.getItemObjId());
            payFeeDetailPo.setCommunityId(item.getCommunityId());
            payFeeDetailPo.setOpenInvoice(openInvoice);
            payFeeDetailV1InnerServiceSMOImpl.updatePayFeeDetailNew(payFeeDetailPo);
        }
    }
}
