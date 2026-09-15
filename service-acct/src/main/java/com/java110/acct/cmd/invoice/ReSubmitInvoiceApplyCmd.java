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

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.bmo.invoice.IPropertyInvoiceSubmitBmo;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.invoice.InvoiceApplyDto;
import com.java110.dto.invoice.InvoiceApplyItemDto;
import com.java110.intf.acct.IInvoiceApplyItemV1InnerServiceSMO;
import com.java110.intf.acct.IInvoiceApplyV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 提交失败重新提交(invoice.reSubmitInvoiceApply)
 * 复用商城幂等逻辑(orgId+orgType+source=WEB+remark), 商城已有单则按其状态决定复用/重试开票;
 * 提交成功 state=S(已提交), 提交失败 state=F(提交失败)并展示失败原因, 可继续重新提交。
 */
@Java110Cmd(serviceCode = "invoice.reSubmitInvoiceApply")
public class ReSubmitInvoiceApplyCmd extends Cmd {

    @Autowired
    private IInvoiceApplyV1InnerServiceSMO invoiceApplyV1InnerServiceSMOImpl;

    @Autowired
    private IInvoiceApplyItemV1InnerServiceSMO invoiceApplyItemV1InnerServiceSMOImpl;

    @Autowired
    private IPropertyInvoiceSubmitBmo propertyInvoiceSubmitBmo;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "applyId", "请求报文中未包含applyId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        String applyId = reqJson.getString("applyId");
        String communityId = reqJson.getString("communityId");

        InvoiceApplyDto applyQuery = new InvoiceApplyDto();
        applyQuery.setApplyId(applyId);
        applyQuery.setCommunityId(communityId);
        List<InvoiceApplyDto> applies = invoiceApplyV1InnerServiceSMOImpl.queryInvoiceApplys(applyQuery);
        Assert.listOnlyOne(applies, "发票申请不存在");
        InvoiceApplyDto invoiceApply = applies.get(0);

        InvoiceApplyItemDto itemQuery = new InvoiceApplyItemDto();
        itemQuery.setApplyId(applyId);
        List<InvoiceApplyItemDto> items = invoiceApplyItemV1InnerServiceSMOImpl.queryInvoiceApplyItems(itemQuery);
        if (ListUtil.isNull(items)) {
            throw new CmdException("未包含开票项");
        }

        InvoiceApplyDto submitResult = propertyInvoiceSubmitBmo.submit(applyId, communityId,
                invoiceApply.getInvoiceName(), invoiceApply.getInvoiceNum(),
                invoiceApply.getInvoiceAddress(), invoiceApply.getApplyTel(), items);

        JSONObject resultData = new JSONObject();
        resultData.put("applyId", submitResult.getApplyId());
        resultData.put("state", submitResult.getState());
        resultData.put("oiId", submitResult.getOiId());
        resultData.put("mallState", submitResult.getMallState());
        resultData.put("failReason", submitResult.getFailReason());

        context.setResponseEntity(ResultVo.createResponseEntity(resultData));
    }
}
