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
package com.java110.acct.cmd.invoice;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.acct.bmo.invoice.IInvoiceStateSyncBmo;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.invoice.InvoiceApplyDto;
import com.java110.intf.acct.IInvoiceApplyV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 同步商城开票状态(invoice.syncInvoiceApplyState)
 * 商城为开票单状态权威源, 物业端只读展示商城状态(待审核/待开票/开票成功/开票失败/已冲红)、发票号与失败原因,
 * 并按商城结果回写缴费明细 pay_fee_detail.open_invoice(开票成功Y/开票失败N), 不修改物业端 state(始终为提交结果 S/F)。
 */
@Java110Cmd(serviceCode = "invoice.syncInvoiceApplyState")
public class SyncInvoiceApplyStateCmd extends Cmd {

    @Autowired
    private IInvoiceApplyV1InnerServiceSMO invoiceApplyV1InnerServiceSMOImpl;

    @Autowired
    private IInvoiceStateSyncBmo invoiceStateSyncBmoImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        // 未指定申请单时按小区分页同步
        if (parseApplyIds(reqJson).length < 1) {
            super.validatePageInfo(reqJson);
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        String communityId = reqJson.getString("communityId");
        String[] applyIds = parseApplyIds(reqJson);

        InvoiceApplyDto queryDto = new InvoiceApplyDto();
        queryDto.setCommunityId(communityId);
        queryDto.setStatusCd("0");
        if (applyIds.length > 0) {
            queryDto.setApplyIds(applyIds);
            queryDto.setPage(-1);
        } else {
            queryDto.setPage(reqJson.getIntValue("page"));
            queryDto.setRow(reqJson.getIntValue("row"));
        }

        List<InvoiceApplyDto> invoiceApplys = invoiceApplyV1InnerServiceSMOImpl.queryInvoiceApplys(queryDto);
        if (invoiceApplys == null) {
            invoiceApplys = new ArrayList<>();
        }

        // 填充商城状态(只读展示) + 回写缴费明细, 不修改物业端 state
        invoiceStateSyncBmoImpl.syncMallState(invoiceApplys);

        List<JSONObject> resultList = new ArrayList<>();
        for (InvoiceApplyDto invoiceApply : invoiceApplys) {
            JSONObject resultData = new JSONObject();
            resultData.put("applyId", invoiceApply.getApplyId());
            resultData.put("state", invoiceApply.getState());
            resultData.put("oiId", invoiceApply.getOiId());
            resultData.put("mallState", invoiceApply.getMallState());
            resultData.put("invoiceCode", invoiceApply.getInvoiceCode());
            resultData.put("failReason", invoiceApply.getFailReason());
            resultList.add(resultData);
        }

        context.setResponseEntity(ResultVo.createResponseEntity(resultList));
    }

    /**
     * 归一化入参: 支持 applyIds 数组与逗号分隔字符串
     */
    private String[] parseApplyIds(JSONObject reqJson) {
        List<String> applyIds = new ArrayList<>();
        Object applyIdObj = reqJson.get("applyIds");
        if (applyIdObj == null) {
            return new String[0];
        }
        if (applyIdObj instanceof JSONArray) {
            JSONArray applyIdArray = (JSONArray) applyIdObj;
            for (int i = 0; i < applyIdArray.size(); i++) {
                String applyId = applyIdArray.getString(i);
                if (!StringUtil.isEmpty(applyId)) {
                    applyIds.add(applyId);
                }
            }
            return applyIds.toArray(new String[applyIds.size()]);
        }
        String applyIdStr = applyIdObj.toString();
        if (StringUtil.isEmpty(applyIdStr)) {
            return new String[0];
        }
        for (String applyId : applyIdStr.split(",")) {
            if (!StringUtil.isEmpty(applyId)) {
                applyIds.add(applyId.trim());
            }
        }
        return applyIds.toArray(new String[applyIds.size()]);
    }
}
