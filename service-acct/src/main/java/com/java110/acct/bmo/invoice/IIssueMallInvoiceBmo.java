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

import com.java110.dto.invoice.InvoiceApplyItemDto;

import java.util.List;

/**
 * 调商城自动开票(propertyInvoiceBmoImpl) 编排, 物业端辅实现
 * 业务流程: 组装费用+抬头信息 → postMallData → 商城生成开票单并诺诺开票
 * → 回写 invoice_apply 状态(G/F)/发票号/pdfUrl(file_rel)/pay_fee_detail.open_invoice/invoice_event
 */
public interface IIssueMallInvoiceBmo {

    /**
     * 调商城 propertyInvoiceBmoImpl 开票并回写物业端数据
     *
     * @param applyId      物业端发票申请ID(商城判重标识)
     * @param communityId  小区ID(商城 orgId/orgType=C)
     * @param buyerName    抬头名称
     * @param buyerTaxNo   税号
     * @param buyerAddress 地址电话
     * @param applyTel     申请电话
     * @param items        费用明细列表(多费用各自类目/金额, 商城逐条映射税率)
     * @return 是否开票成功
     */
    boolean issue(String applyId, String communityId, String buyerName,
                  String buyerTaxNo, String buyerAddress, String applyTel,
                  List<InvoiceApplyItemDto> items);
}
