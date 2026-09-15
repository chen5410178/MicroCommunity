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
import com.java110.dto.invoice.InvoiceApplyItemDto;

import java.util.List;

/**
 * 物业端提交开票申请(商城 propertyInvoiceBmoImpl) 编排, 物业端辅实现
 * 业务分工: 商城为抬头落库方(开票报文内 upsert)、开票单落库方、审核开关(audit_switch)唯一仲裁者、
 * 开票执行者与状态权威源; 物业端只调一个开票接口, 抬头字段随开票请求一起传;
 * 物业端只负责提交申请并记录提交结果:
 * 提交成功 → invoice_apply.state=S(已提交)、oi_id=商城单号;
 * 提交失败 → invoice_apply.state=F(提交失败)、remark=失败原因, 可重新提交。
 * 缴费明细 pay_fee_detail.open_invoice 按商城即时结果回写(开票成功Y/开票失败N, 待审核或待开票不动)。
 */
public interface IPropertyInvoiceSubmitBmo {

    /**
     * 提交开票申请到商城并回写物业端提交结果(抬头随开票报文传递, 商城按 phone+headingType upsert)
     *
     * @param applyId      物业端发票申请ID(商城 ext_apply_id, 幂等判重与状态查询一等键)
     * @param communityId  小区ID(商城 orgId/orgType=C)
     * @param headingType  商城抬头类型(公司/个人, 由发票类型 2002/1001 换算)
     * @param buyerName    抬头名称
     * @param buyerTaxNo   税号
     * @param buyerAddress 地址电话
     * @param applyTel     申请电话(商城抬头归属手机号 phone)
     * @param email        邮箱(可为空)
     * @param bankNo       开户行及账号(专票用, 可为空)
     * @param items        费用明细列表(多费用各自类目/金额, 商城逐条映射税率)
     * @return 含提交结果(state/oiId/mallState/failReason/invoiceCode)的发票申请
     */
    InvoiceApplyDto submit(String applyId, String communityId, String headingType, String buyerName,
                           String buyerTaxNo, String buyerAddress, String applyTel, String email,
                           String bankNo, List<InvoiceApplyItemDto> items);
}
