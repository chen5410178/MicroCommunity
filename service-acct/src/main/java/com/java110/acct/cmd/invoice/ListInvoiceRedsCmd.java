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
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询发票红冲列表(invoice.listInvoiceReds)
 * 物业端不建红冲表: 先按与发票列表一致的筛选条件查物业 invoice_apply 取 applyId 集合,
 * 再交商城 queryInvoiceRedsBmoImpl 按开票单号分页查询红冲(物业端不做二次过滤, 保证页码与总数准确);
 * 商城未开启或调用异常时返回空分页, 不向前端抛错。
 */
@Java110Cmd(serviceCode = "invoice.listInvoiceReds")
public class ListInvoiceRedsCmd extends Cmd {

    private static final Logger logger = LoggerFactory.getLogger(ListInvoiceRedsCmd.class);

    /**
     * 单次提交商城的申请单号上限(商城侧按 invoice_id IN 查询, 避免报文与 SQL 过大)
     */
    private static final int MAX_EXT_APPLY_IDS = 2000;

    @Autowired
    private IInvoiceApplyV1InnerServiceSMO invoiceApplyV1InnerServiceSMOImpl;

    @Autowired
    private IInvoiceRedBmo invoiceRedBmoImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        // ① 按发票列表同样的筛选条件取物业 applyId 集合(page=-1 仅用于取 id, 不用于展示)
        InvoiceApplyDto applyQuery = BeanConvertUtil.covertBean(reqJson, InvoiceApplyDto.class);
        applyQuery.setPage(-1);
        List<InvoiceApplyDto> applies = invoiceApplyV1InnerServiceSMOImpl.queryInvoiceApplys(applyQuery);
        if (ListUtil.isNull(applies)) {
            context.setResponseEntity(ResultVo.createResponseEntity(0, 0, new ArrayList<InvoiceRedDto>()));
            return;
        }

        List<String> applyIds = new ArrayList<>();
        for (InvoiceApplyDto apply : applies) {
            if (StringUtil.isEmpty(apply.getApplyId()) || applyIds.contains(apply.getApplyId())) {
                continue;
            }
            applyIds.add(apply.getApplyId());
        }
        if (applyIds.isEmpty()) {
            context.setResponseEntity(ResultVo.createResponseEntity(0, 0, new ArrayList<InvoiceRedDto>()));
            return;
        }
        if (applyIds.size() > MAX_EXT_APPLY_IDS) {
            logger.warn("红冲查询申请单号超限, 已截断至 {} 条(实际 {} 条), communityId: {}",
                    MAX_EXT_APPLY_IDS, applyIds.size(), reqJson.getString("communityId"));
            applyIds = new ArrayList<>(applyIds.subList(0, MAX_EXT_APPLY_IDS));
        }

        // ② 交商城按开票单号分页查询红冲(MALL_SWITCH 未开启时商城查询静默返回空分页)
        InvoiceRedDto invoiceRedDto = new InvoiceRedDto();
        invoiceRedDto.setApplyIds(applyIds.toArray(new String[applyIds.size()]));
        invoiceRedDto.setPage(reqJson.getInteger("page"));
        invoiceRedDto.setRow(reqJson.getInteger("row"));
        context.setResponseEntity(ResultVo.createResponseEntity(invoiceRedBmoImpl.queryMallReds(invoiceRedDto)));
    }
}
