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
package com.java110.fee.cmd.returnPayFee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.onlinePayRefund.OnlinePayRefundDto;
import com.java110.dto.refundFeeDetail.RefundFeeDetailDto;
import com.java110.dto.refundFeeOrder.RefundFeeOrderDto;
import com.java110.intf.acct.IOnlinePayRefundV1InnerServiceSMO;
import com.java110.intf.fee.IFeeAccountDetailServiceSMO;
import com.java110.intf.fee.IPayFeeDetailDiscountInnerServiceSMO;
import com.java110.intf.fee.IRefundFeeDetailV1InnerServiceSMO;
import com.java110.intf.fee.IRefundFeeOrderV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 类表述：查询
 * 服务编码：returnPayFee.listReturnPayFee
 * 请求路劲：/app/returnPayFee.ListReturnPayFee
 * add by 吴学文 at 2022-02-21 12:20:03 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "returnPayFee.listReturnPayFees")
public class ListReturnPayFeesCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(ListReturnPayFeesCmd.class);

    @Autowired
    private IRefundFeeOrderV1InnerServiceSMO refundFeeOrderV1InnerServiceSMOImpl;

    @Autowired
    private IFeeAccountDetailServiceSMO feeAccountDetailServiceSMOImpl;

    @Autowired
    private IPayFeeDetailDiscountInnerServiceSMO payFeeDetailDiscountInnerServiceSMOImpl;

    @Autowired
    private IOnlinePayRefundV1InnerServiceSMO onlinePayRefundV1InnerServiceSMOImpl;

    @Autowired
    private IRefundFeeDetailV1InnerServiceSMO refundFeeDetailV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        RefundFeeOrderDto refundFeeOrderDto = BeanConvertUtil.covertBean(reqJson, RefundFeeOrderDto.class);

        int count = refundFeeOrderV1InnerServiceSMOImpl.queryRefundFeeOrdersCount(refundFeeOrderDto);

        List<RefundFeeOrderDto> refundFeeOrderDtos;

        if (count > 0) {
            //注意这里处理 记得测试退费逻辑
            refundFeeOrderDtos = refundFeeOrderV1InnerServiceSMOImpl.queryRefundFeeOrders(refundFeeOrderDto);
        } else {
            refundFeeOrderDtos = new ArrayList<>();
        }
        // 查询退费明细
        computeDetail(refundFeeOrderDtos);
        //todo 查询退款 支付系统返回内容
        computeOnlinePayRefundRemark(refundFeeOrderDtos);

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, refundFeeOrderDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        cmdDataFlowContext.setResponseEntity(responseEntity);
    }

    private void computeDetail(List<RefundFeeOrderDto> refundFeeOrderDtos) {
        if (ListUtil.isNull(refundFeeOrderDtos)) {
            return;
        }
        List<String> refundOrderIds = new ArrayList<>();
        for (RefundFeeOrderDto returnPayFeeDto : refundFeeOrderDtos) {
            refundOrderIds.add(returnPayFeeDto.getRefundOrderId());
        }

        RefundFeeDetailDto refundFeeDetailDto = new RefundFeeDetailDto();
        refundFeeDetailDto.setRefundOrderIds(refundOrderIds.toArray(new String[refundOrderIds.size()]));
        List<RefundFeeDetailDto> refundFeeDetailDtos = refundFeeDetailV1InnerServiceSMOImpl.queryRefundFeeDetails(refundFeeDetailDto);
        if (ListUtil.isNull(refundFeeDetailDtos)) {
            return;
        }
        List<RefundFeeDetailDto> details = null;
        for (RefundFeeOrderDto refundFeeOrderDto : refundFeeOrderDtos) {
            details = new ArrayList<>();
            for (RefundFeeDetailDto refundFeeDetailDto1 : refundFeeDetailDtos) {
                if (!refundFeeOrderDto.getRefundOrderId().equals(refundFeeDetailDto1.getRefundOrderId())) {
                    continue;
                }
                details.add(refundFeeDetailDto1);
            }
            refundFeeOrderDto.setDetails(details);
        }
    }

    private void computeOnlinePayRefundRemark(List<RefundFeeOrderDto> refundFeeOrderDtos) {

        if (ListUtil.isNull(refundFeeOrderDtos)) {
            return;
        }

        List<String> detailIds = new ArrayList<>();
        for (RefundFeeOrderDto returnPayFeeDto : refundFeeOrderDtos) {
            detailIds.add(returnPayFeeDto.getRefundOrderId());
        }

        OnlinePayRefundDto onlinePayRefundDto = new OnlinePayRefundDto();
        onlinePayRefundDto.setBusiIds(detailIds.toArray(new String[detailIds.size()]));
        List<OnlinePayRefundDto> onlinePayRefundDtos = onlinePayRefundV1InnerServiceSMOImpl.queryOnlinePayRefunds(onlinePayRefundDto);

        if (ListUtil.isNull(onlinePayRefundDtos)) {
            return;
        }

        for (RefundFeeOrderDto refundFeeOrderDto : refundFeeOrderDtos) {
            for (OnlinePayRefundDto tmpOnlinePayRefundDto : onlinePayRefundDtos) {
                if (refundFeeOrderDto.getRefundOrderId().equals(tmpOnlinePayRefundDto.getBusiId())) {
                    refundFeeOrderDto.setRefundState(tmpOnlinePayRefundDto.getState());
                    refundFeeOrderDto.setRefundRemark(tmpOnlinePayRefundDto.getMessage());
                }
            }
        }
    }
}
