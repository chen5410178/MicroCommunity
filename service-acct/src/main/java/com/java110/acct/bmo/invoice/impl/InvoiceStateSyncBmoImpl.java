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
 * 商城开票状态同步实现(只读展示 + 缴费明细回写)
 */
@Service
public class InvoiceStateSyncBmoImpl implements IInvoiceStateSyncBmo {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceStateSyncBmoImpl.class);

    private static final String MALL_API_CODE = "queryInvoiceStatusBatchBmoImpl";
    private static final String OPEN_INVOICE_YES = "Y";
    private static final String OPEN_INVOICE_NO = "N";
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
        Map<String, InvoiceMallStateDto> mallStates = queryMallStates(collectOiIds(applies));
        if (mallStates.isEmpty()) {
            return;
        }
        for (InvoiceApplyDto apply : applies) {
            InvoiceMallStateDto mallState = mallStates.get(apply.getOiId());
            if (mallState == null) {
                continue;
            }
            apply.setMallState(mallState.getState());
            apply.setFailReason(mallState.getFailReason());
            if (StringUtil.isEmpty(apply.getInvoiceCode()) && !StringUtil.isEmpty(mallState.getInvoiceNo())) {
                apply.setInvoiceCode(mallState.getInvoiceNo());
            }
        }
    }

    @Override
    public void syncMallState(List<InvoiceApplyDto> applies) {
        fillMallState(applies);
        if (ListUtil.isNull(applies)) {
            return;
        }
        for (InvoiceApplyDto apply : applies) {
            if (InvoiceMallStateDto.STATE_DONE.equals(apply.getMallState())) {
                writeBackFeeDetail(apply.getApplyId(), OPEN_INVOICE_YES);
            } else if (InvoiceMallStateDto.STATE_FAIL.equals(apply.getMallState())) {
                writeBackFeeDetail(apply.getApplyId(), OPEN_INVOICE_NO);
            }
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
     * 收集已提交商城的开票单号
     */
    private String[] collectOiIds(List<InvoiceApplyDto> applies) {
        List<String> oiIds = new ArrayList<>();
        for (InvoiceApplyDto apply : applies) {
            if (StringUtil.isEmpty(apply.getOiId()) || "-1".equals(apply.getOiId())) {
                continue;
            }
            if (oiIds.contains(apply.getOiId())) {
                continue;
            }
            oiIds.add(apply.getOiId());
        }
        return oiIds.toArray(new String[oiIds.size()]);
    }

    /**
     * 调商城批量状态接口(queryInvoiceStatusBatchBmoImpl), 返回 invoiceId -> 商城状态
     */
    private Map<String, InvoiceMallStateDto> queryMallStates(String[] oiIds) {
        Map<String, InvoiceMallStateDto> mallStates = new HashMap<>();
        if (oiIds == null || oiIds.length < 1) {
            return mallStates;
        }
        // 商城未开启时无需回读状态
        if (!"ON".equals(MappingCache.getValue(MALL_DOMAIN, "MALL_SWITCH"))) {
            return mallStates;
        }
        JSONObject reqParam = new JSONObject();
        reqParam.put("invoiceIds", oiIds);
        ResultVo resultVo;
        try {
            resultVo = mallInnerServiceSMOImpl.postMallData(new MallDataDto(MALL_API_CODE, reqParam));
        } catch (Exception e) {
            logger.warn("查询商城开票状态异常", e);
            return mallStates;
        }
        if (resultVo == null || resultVo.getCode() != ResultVo.CODE_OK || resultVo.getData() == null) {
            logger.warn("查询商城开票状态未生效: {}", resultVo == null ? "null" : resultVo.getMsg());
            return mallStates;
        }
        JSONArray invoices = ((JSONObject) resultVo.getData()).getJSONArray("invoices");
        if (invoices == null || invoices.isEmpty()) {
            return mallStates;
        }
        for (int i = 0; i < invoices.size(); i++) {
            InvoiceMallStateDto mallState = BeanConvertUtil.covertBean(invoices.getJSONObject(i), InvoiceMallStateDto.class);
            if (StringUtil.isEmpty(mallState.getInvoiceId())) {
                continue;
            }
            mallStates.put(mallState.getInvoiceId(), mallState);
        }
        return mallStates;
    }
}
