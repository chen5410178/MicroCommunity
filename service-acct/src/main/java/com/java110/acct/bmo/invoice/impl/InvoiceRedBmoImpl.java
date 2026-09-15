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
import com.java110.acct.bmo.invoice.IInvoiceRedBmo;
import com.java110.dto.MallDataDto;
import com.java110.dto.invoice.InvoiceRedDto;
import com.java110.intf.job.IMallInnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 发票红冲业务实现(物业端不落任何红冲数据)
 * 发起: 商城按 ext_apply_id(=物业 applyId) 定位开票单, 落 invoice_red + 原单 RED 并同步执行红字开票;
 * 查询: 物业端先按筛选条件取 applyId 集合, 再交商城按 invoiceIds 分页查询, 物业端不做二次过滤
 */
@Service
public class InvoiceRedBmoImpl implements IInvoiceRedBmo {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceRedBmoImpl.class);

    private static final String MALL_DOMAIN = "MALL";
    private static final String MALL_SWITCH_ON = "ON";
    private static final String ORG_TYPE_COMMUNITY = "C";
    private static final String MALL_API_CODE_APPLY_RED = "propertyInvoiceRedBmoImpl";
    private static final String MALL_API_CODE_QUERY_REDS = "queryInvoiceRedsBmoImpl";
    private static final String FAIL_REASON_CALL_MALL = "调用商城红冲失败";
    private static final String FAIL_REASON_RED_APPLY = "发起红冲失败";
    private static final String FAIL_REASON_MALL_CLOSED = "商城未开启, 无法发起红冲";
    private static final String FAIL_REASON_NO_DATA = "商城未返回冲红数据";
    private static final String FAIL_REASON_SQL_NOISE = "商城红冲调用失败(详见服务日志)";
    private static final int REMARK_MAX_LEN = 200;
    private static final String[] SQL_NOISE_KEYWORDS = {"### Error updating database", "### SQL:", "nested exception"};

    @Autowired
    private IMallInnerServiceSMO mallInnerServiceSMOImpl;

    @Override
    public InvoiceRedDto applyRed(String communityId, String applyId, String reason, String remark) {
        if (!mallSwitchOn()) {
            throw new CmdException(FAIL_REASON_MALL_CLOSED);
        }

        JSONObject data = new JSONObject();
        data.put("orgId", communityId);
        data.put("orgType", ORG_TYPE_COMMUNITY);
        data.put("applyId", applyId);
        data.put("reason", reason);
        data.put("remark", safeRemark(remark));

        ResultVo resultVo;
        try {
            resultVo = mallInnerServiceSMOImpl.postMallData(new MallDataDto(MALL_API_CODE_APPLY_RED, data));
        } catch (Exception e) {
            logger.error("调用商城发起红冲异常 applyId: {}", applyId, e);
            throw new CmdException(safeFailReason(e.getMessage(), FAIL_REASON_CALL_MALL));
        }

        if (resultVo == null || resultVo.getCode() != ResultVo.CODE_OK) {
            throw new CmdException(safeFailReason(resultVo == null ? null : resultVo.getMsg(), FAIL_REASON_RED_APPLY));
        }
        if (resultVo.getData() == null) {
            throw new CmdException(FAIL_REASON_NO_DATA);
        }

        JSONObject redData = (JSONObject) resultVo.getData();
        InvoiceRedDto invoiceRedDto = BeanConvertUtil.covertBean(redData, InvoiceRedDto.class);
        if (StringUtil.isEmpty(invoiceRedDto.getApplyId())) {
            invoiceRedDto.setApplyId(applyId);
        }
        invoiceRedDto.setFailReason(safeFailReason(invoiceRedDto.getFailReason(), null));
        return invoiceRedDto;
    }

    @Override
    public ResultVo queryMallReds(InvoiceRedDto invoiceRedDto) {
        // 商城未开启: 红冲查询静默返回空分页(用户要求, 前端不报错)
        if (!mallSwitchOn() || invoiceRedDto == null
                || invoiceRedDto.getApplyIds() == null || invoiceRedDto.getApplyIds().length < 1) {
            return emptyRedVo();
        }

        int page = invoiceRedDto.getPage();
        int row = invoiceRedDto.getRow();
        if (row < 1) {
            return emptyRedVo();
        }

        JSONObject reqParam = new JSONObject();
        reqParam.put("applyIds", invoiceRedDto.getApplyIds());
        reqParam.put("page", page);
        reqParam.put("row", row);

        ResultVo resultVo;
        try {
            resultVo = mallInnerServiceSMOImpl.postMallData(new MallDataDto(MALL_API_CODE_QUERY_REDS, reqParam));
        } catch (Exception e) {
            logger.warn("查询商城红冲数据异常, applyIds size: {}", invoiceRedDto.getApplyIds().length, e);
            return emptyRedVo();
        }

        if (resultVo == null || resultVo.getCode() != ResultVo.CODE_OK || resultVo.getData() == null) {
            logger.warn("查询商城红冲数据未生效: {}", resultVo == null ? "null" : resultVo.getMsg());
            return emptyRedVo();
        }

        JSONObject redPage = (JSONObject) resultVo.getData();
        int total = redPage.getIntValue("total");
        List<InvoiceRedDto> invoiceRedDtos = new ArrayList<>();
        JSONArray reds = redPage.getJSONArray("reds");
        if (reds != null && !reds.isEmpty()) {
            for (int i = 0; i < reds.size(); i++) {
                invoiceRedDtos.add(BeanConvertUtil.covertBean(reds.getJSONObject(i), InvoiceRedDto.class));
            }
        }
        return new ResultVo((int) Math.ceil((double) total / (double) row), total, invoiceRedDtos);
    }

    private boolean mallSwitchOn() {
        return MALL_SWITCH_ON.equals(MappingCache.getValue(MALL_DOMAIN, "MALL_SWITCH"));
    }

    private ResultVo emptyRedVo() {
        return new ResultVo(0, 0, new ArrayList<InvoiceRedDto>());
    }

    /**
     * 所有写 remark 的入参统一: 压缩连续空白 + 截断至列宽以内(invoice_red.remark varchar(200))
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
        logger.warn("商城红冲失败原因(原文): {}", reason);
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
