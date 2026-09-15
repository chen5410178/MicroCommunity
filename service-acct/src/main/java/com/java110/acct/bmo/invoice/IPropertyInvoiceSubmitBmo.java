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

import com.java110.dto.invoice.InvoiceMallStateDto;
import com.java110.po.invoice.InvoiceApplyItemPo;
import com.java110.po.invoice.InvoiceApplyPo;

import java.util.List;

/**
 * 物业端提交开票申请(商城 propertyInvoiceBmoImpl) 编排, 物业端辅实现
 * 业务分工: 商城为抬头落库方(开票报文内 upsert)、开票单落库方、审核开关(audit_switch)唯一仲裁者、
 * 开票执行者与状态权威源; 物业端只调一个开票接口, 抬头字段随开票请求一起传;
 * 物业端只负责提交申请并记录提交结果:
 * 商城已建单(返回 invoiceId) → invoice_apply.state=S(已提交)、oi_id=商城单号;
 * 商城未建单(异常/无 invoiceId) → 保存路径不落库、失败原因直达前端; 重提路径 state=F 并记录失败原因摘要。
 * 缴费明细 pay_fee_detail.open_invoice 按商城即时结果回写(开票成功Y/开票失败N/开票中D)。
 */
public interface IPropertyInvoiceSubmitBmo {

    /**
     * 仅调用商城开票接口(不读写物业库): 商城为抬头/开票单/开票执行的权威方
     *
     * @param applyPo     物业端发票申请(applyId 为商城 ext_apply_id, 抬头与金额取自该对象)
     * @param headingType 商城抬头类型(公司/个人, 由发票类型 2002/1001 换算)
     * @param email       邮箱(可为空)
     * @param bankNo      开户行及账号(专票用, 可为空)
     * @param items       费用明细列表(多费用各自类目/金额, 商城逐条映射税率)
     * @return 商城开票结果(invoiceId 为空表示商城未建单, failReason 为可读摘要)
     */
    InvoiceMallStateDto submitMall(InvoiceApplyPo applyPo, List<InvoiceApplyItemPo> items,
                                   String headingType, String email, String bankNo);

    /**
     * 重新提交(申请单已存在): 先调商城开票;
     * 商城未建单回写 state=F + remark(失败原因摘要), 商城已建单回写 oi_id/state=S + 明细开票状态
     *
     * @param applyPo 物业端发票申请(抬头与金额取自该对象)
     * @param items   费用明细列表
     * @return 商城开票结果
     */
    InvoiceMallStateDto reSubmit(InvoiceApplyPo applyPo, List<InvoiceApplyItemPo> items);
}
