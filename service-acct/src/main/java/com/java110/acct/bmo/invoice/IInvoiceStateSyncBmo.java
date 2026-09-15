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
package com.java110.acct.bmo.invoice;

import com.java110.dto.invoice.InvoiceApplyDto;

import java.util.List;

/**
 * 商城开票状态同步(只读展示 + 缴费明细回写), 物业端不落发票状态
 * 商城 invoice_order 为唯一权威状态源(审核/开票进度、发票号、PDF、失败原因)
 */
public interface IInvoiceStateSyncBmo {

    /**
     * 按 oi_id 从商城批量取状态, 仅填充到 DTO 供展示(mallState/failReason/invoiceCode), 不落库、不改物业 state
     *
     * @param applies 物业端发票申请列表
     */
    void fillMallState(List<InvoiceApplyDto> applies);

    /**
     * 填充商城状态展示 + 按商城结果回写缴费明细 pay_fee_detail.open_invoice(开票成功Y/开票失败N)
     *
     * @param applies 物业端发票申请列表
     */
    void syncMallState(List<InvoiceApplyDto> applies);

    /**
     * 回写单个申请单关联的缴费明细开票状态
     *
     * @param applyId     物业端发票申请ID
     * @param openInvoice Y 已开票 N 未开票
     */
    void writeBackFeeDetail(String applyId, String openInvoice);
}
