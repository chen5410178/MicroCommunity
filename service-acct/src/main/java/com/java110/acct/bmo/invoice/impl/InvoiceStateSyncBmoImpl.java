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
import com.java110.dto.MallDataDto;
import com.java110.dto.invoice.InvoiceApplyDto;
import com.java110.dto.invoice.InvoiceApplyItemDto;
import com.java110.dto.invoice.InvoiceMallStateDto;
import com.java110.intf.acct.IInvoiceApplyItemV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeDetailV1InnerServiceSMO;
import com.java110.intf.job.IMallInnerServiceSMO;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.utils.cache.MappingCache;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商城开票状态查询填充实现(只读展示 + 缴费明细回写)
 * 物业端不落状态: 列表/详情每次查询按 applyId(商城 ext_apply_id) 实时取商城状态、失败原因、发票号、发票PDF
 */
@Service
public class InvoiceStateSyncBmoImpl implements IInvoiceStateSyncBmo {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceStateSyncBmoImpl.class);

    private static final String MALL_API_CODE = "queryInvoiceStatusBatchBmoImpl";
    private static final String OI_ID_NOT_SUBMIT = "-1";
    private static final String MALL_DOMAIN = "MALL";

    @Autowired
    private IMallInnerServiceSMO mallInnerServiceSMOImpl;

    @Autowired
    private IInvoiceApplyItemV1InnerServiceSMO invoiceApplyItemV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeDetailV1InnerServiceSMO payFeeDetailV1InnerServiceSMOImpl;

    @Override
    public void fillMallState(List<InvoiceApplyDto> applies) {
        if (ListUtil.isNull(applies)) {
            return;
        }
        Map<String, InvoiceMallStateDto> mallStatesByApplyId = new HashMap<>();
        Map<String, InvoiceMallStateDto> mallStatesByInvoiceId = new HashMap<>();
        queryMallStates(applies, mallStatesByApplyId, mallStatesByInvoiceId);
        if (mallStatesByApplyId.isEmpty() && mallStatesByInvoiceId.isEmpty()) {
            return;
        }
        for (InvoiceApplyDto apply : applies) {
            InvoiceMallStateDto mallState = mallStatesByApplyId.get(apply.getApplyId());
            if (mallState == null) {
                // 存量单(商城开票单无 ext_apply_id)兼容按商城单号兜底匹配
                mallState = mallStatesByInvoiceId.get(apply.getOiId());
            }
            if (mallState == null) {
                continue;
            }
            fillOne(apply, mallState);
        }
    }

    @Override
    public void writeBackFeeDetail(String applyId, String openInvoice) {
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

    /**
     * 商城结果回填到申请单(只读展示, 不落库)
     */
    private void fillOne(InvoiceApplyDto apply, InvoiceMallStateDto mallState) {
        apply.setMallState(mallState.getState());
        apply.setFailReason(mallState.getFailReason());
        if (StringUtil.isEmpty(apply.getInvoiceCode()) && !StringUtil.isEmpty(mallState.getInvoiceNo())) {
            apply.setInvoiceCode(mallState.getInvoiceNo());
        }
        // 发票PDF只存商城, 物业端查询时读取用于展示/下载
        apply.setMallPdfUrl(mallState.getPdfUrl());
        // 历史提单未记录商城单号时以商城返回值补齐展示, 不改库
        if (StringUtil.isEmpty(apply.getOiId()) || OI_ID_NOT_SUBMIT.equals(apply.getOiId())) {
            apply.setOiId(mallState.getInvoiceId());
        }
    }

    /**
     * 调商城批量状态接口(queryInvoiceStatusBatchBmoImpl):
     * 按 applyIds(商城 ext_apply_id) 查询, 并对已有商城单号的存量单按 invoiceIds 兼容查询
     *
     * @param applies              物业端发票申请列表
     * @param mallStatesByApplyId  出参: applyId -> 商城状态
     * @param mallStatesByInvoiceId 出参: 商城开票单号 -> 商城状态
     */
    private void queryMallStates(List<InvoiceApplyDto> applies, Map<String, InvoiceMallStateDto> mallStatesByApplyId,
                                 Map<String, InvoiceMallStateDto> mallStatesByInvoiceId) {
        List<String> applyIds = new ArrayList<>();
        List<String> oiIds = new ArrayList<>();
        for (InvoiceApplyDto apply : applies) {
            if (!StringUtil.isEmpty(apply.getApplyId()) && !applyIds.contains(apply.getApplyId())) {
                applyIds.add(apply.getApplyId());
            }
            if (StringUtil.isEmpty(apply.getOiId()) || OI_ID_NOT_SUBMIT.equals(apply.getOiId())) {
                continue;
            }
            if (!oiIds.contains(apply.getOiId())) {
                oiIds.add(apply.getOiId());
            }
        }
        if (applyIds.isEmpty() && oiIds.isEmpty()) {
            return;
        }
        // 商城未开启时无需回读状态
        if (!"ON".equals(MappingCache.getValue(MALL_DOMAIN, "MALL_SWITCH"))) {
            return;
        }
        JSONObject reqParam = new JSONObject();
        if (!applyIds.isEmpty()) {
            reqParam.put("applyIds", applyIds);
        }
        if (!oiIds.isEmpty()) {
            reqParam.put("invoiceIds", oiIds);
        }
        ResultVo resultVo;
        try {
            resultVo = mallInnerServiceSMOImpl.postMallData(new MallDataDto(MALL_API_CODE, reqParam));
        } catch (Exception e) {
            logger.warn("查询商城开票状态异常", e);
            return;
        }
        if (resultVo == null || resultVo.getCode() != ResultVo.CODE_OK || resultVo.getData() == null) {
            logger.warn("查询商城开票状态未生效: {}", resultVo == null ? "null" : resultVo.getMsg());
            return;
        }
        JSONArray invoices = ((JSONObject) resultVo.getData()).getJSONArray("invoices");
        if (invoices == null || invoices.isEmpty()) {
            return;
        }
        for (int i = 0; i < invoices.size(); i++) {
            InvoiceMallStateDto mallState = BeanConvertUtil.covertBean(invoices.getJSONObject(i), InvoiceMallStateDto.class);
            if (!StringUtil.isEmpty(mallState.getApplyId())) {
                mallStatesByApplyId.put(mallState.getApplyId(), mallState);
            }
            if (!StringUtil.isEmpty(mallState.getInvoiceId())) {
                mallStatesByInvoiceId.put(mallState.getInvoiceId(), mallState);
            }
        }
    }
}
