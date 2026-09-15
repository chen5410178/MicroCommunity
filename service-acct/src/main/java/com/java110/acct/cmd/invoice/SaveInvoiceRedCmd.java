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
import com.java110.acct.bmo.invoice.IInvoiceRedBmo;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.invoice.InvoiceApplyDto;
import com.java110.dto.invoice.InvoiceRedDto;
import com.java110.intf.acct.IInvoiceApplyV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 发起发票红冲(invoice.saveInvoiceRed)
 * 物业端不落任何红冲数据、无冲红状态机: 校验申请单归属当前小区后调商城 propertyInvoiceRedBmoImpl,
 * 商城为冲红单(invoice_red)、红字发票号码、冲红状态唯一权威源。
 */
@Java110Cmd(serviceCode = "invoice.saveInvoiceRed")
public class SaveInvoiceRedCmd extends Cmd {

    @Autowired
    private IInvoiceApplyV1InnerServiceSMO invoiceApplyV1InnerServiceSMOImpl;

    @Autowired
    private IInvoiceRedBmo invoiceRedBmoImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "applyId", "请求报文中未包含applyId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "reason", "请求报文中未包含红冲原因");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        String applyId = reqJson.getString("applyId");
        String communityId = reqJson.getString("communityId");

        // 校验申请单存在且归属当前小区
        InvoiceApplyDto applyQuery = new InvoiceApplyDto();
        applyQuery.setApplyId(applyId);
        applyQuery.setCommunityId(communityId);
        List<InvoiceApplyDto> applies = invoiceApplyV1InnerServiceSMOImpl.queryInvoiceApplys(applyQuery);
        Assert.listOnlyOne(applies, "发票申请不存在");

        // 商城未开启/开票单不存在/非开票成功状态/调用异常均抛 CmdException, 由商城给出失败原因
        InvoiceRedDto invoiceRedDto = invoiceRedBmoImpl.applyRed(communityId, applyId,
                reqJson.getString("reason"), reqJson.getString("remark"));

        JSONObject resultData = new JSONObject();
        resultData.put("applyId", invoiceRedDto.getApplyId());
        resultData.put("invoiceId", invoiceRedDto.getInvoiceId());
        resultData.put("redId", invoiceRedDto.getRedId());
        resultData.put("state", invoiceRedDto.getState());
        resultData.put("redNo", invoiceRedDto.getRedNo());
        resultData.put("failReason", invoiceRedDto.getFailReason());
        context.setResponseEntity(ResultVo.createResponseEntity(resultData));
    }
}
