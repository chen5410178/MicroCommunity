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

import com.java110.dto.invoice.InvoiceRedDto;
import com.java110.vo.ResultVo;

/**
 * 发票红冲业务(物业端零新增表, 红冲单/红字发票号码/冲红状态全部以商城为权威源)
 * 发起: 调商城 propertyInvoiceRedBmoImpl; 查询: 按物业 applyId 调商城 queryInvoiceRedsBmoImpl
 */
public interface IInvoiceRedBmo {

    /**
     * 发起红冲(调商城 propertyInvoiceRedBmoImpl)
     * 商城未开启或调用失败时抛 CmdException(写操作需让用户感知失败)
     *
     * @param communityId 小区ID(商城 orgId)
     * @param applyId     物业发票申请ID(商城 ext_apply_id)
     * @param reason      红冲原因(必填)
     * @param remark      备注(选填)
     * @return 商城冲红结果(redId/invoiceId/state/redNo/failReason)
     */
    InvoiceRedDto applyRed(String communityId, String applyId, String reason, String remark);

    /**
     * 按物业 applyId 集合查询商城红冲分页数据(调商城 queryInvoiceRedsBmoImpl)
     * 商城开关未开启、调用异常或返回为空时静默返回空分页, 不向前端抛错
     *
     * @param invoiceRedDto 查询条件(applyIds) + 分页(page/row)
     * @return ResultVo(分页页数, 总记录数, List&lt;InvoiceRedDto&gt;)
     */
    ResultVo queryMallReds(InvoiceRedDto invoiceRedDto);
}
