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
package com.java110.fee.cmd.feeOrder;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.payFeeOrder.PayFeeOrderDetailDto;
import com.java110.dto.prePayFee.PrePayFeeDto;
import com.java110.dto.reportFee.ReportFeeMonthStatisticsTotalDto;
import com.java110.intf.fee.IPayFeeOrderV1InnerServiceSMO;
import com.java110.intf.fee.IPrePayFeeV1InnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import com.java110.dto.payFeeOrder.PayFeeOrderDto;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 类表述：查询
 * 服务编码：payFeeOrder.listPayFeeOrder
 * 请求路劲：/app/payFeeOrder.ListPayFeeOrder
 * add by 吴学文 at 2025-07-16 16:05:34 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "feeOrder.listPayFeeOrder")
public class ListPayFeeOrderCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(ListPayFeeOrderCmd.class);
    @Autowired
    private IPayFeeOrderV1InnerServiceSMO payFeeOrderV1InnerServiceSMOImpl;



    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
        String endTime = reqJson.getString("endTime");
        if (!StringUtil.isEmpty(endTime) && !endTime.contains(":")) {
            endTime += " 23:59:59";
            reqJson.put("endTime", endTime);
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        PayFeeOrderDto payFeeOrderDto = BeanConvertUtil.covertBean(reqJson, PayFeeOrderDto.class);

        JSONObject countInfo = payFeeOrderV1InnerServiceSMOImpl.queryPayFeeOrdersCount(payFeeOrderDto);
        int count = Integer.parseInt(countInfo.get("count").toString());
        List<PayFeeOrderDto> payFeeOrderDtos = null;

        ReportFeeMonthStatisticsTotalDto reportFeeMonthStatisticsTotalDto = new ReportFeeMonthStatisticsTotalDto();
        ResultVo resultVo = null;
        if (count < 1) {
            resultVo = new ResultVo((int) Math.ceil((double) count / (double) payFeeOrderDto.getRow()), count, payFeeOrderDtos, reportFeeMonthStatisticsTotalDto);
            cmdDataFlowContext.setResponseEntity(ResultVo.createResponseEntity(resultVo));
            return;
        }

        payFeeOrderDtos = payFeeOrderV1InnerServiceSMOImpl.queryReportPayFeeOrders(payFeeOrderDto);

//todo 应收金额(大计)
        reportFeeMonthStatisticsTotalDto.setAllReceivableAmount(String.format("%.2f", countInfo.getDouble("totalReceivableAmount")));
        //todo 实收金额(大计)
        reportFeeMonthStatisticsTotalDto.setAllReceivedAmount(String.format("%.2f", countInfo.getDouble("totalReceivedAmount")));

        //todo 打折金额（大计）
        reportFeeMonthStatisticsTotalDto.setAllPreferentialAmount(String.format("%.2f", countInfo.getDouble("discountAmount")));

        //todo 减免金额(大计)
        reportFeeMonthStatisticsTotalDto.setAllDeductionAmount(String.format("%.2f", countInfo.getDouble("deductionAmount")));
        //todo 滞纳金(大计)
        reportFeeMonthStatisticsTotalDto.setAllLateFee(String.format("%.2f", countInfo.getDouble("lateAmount")));

        //todo 赠送规则金额(大计)
        reportFeeMonthStatisticsTotalDto.setAllGiftAmount(String.format("%.2f", countInfo.getDouble("giftAmount")));

        //todo 计算小计
        computeTotalInfo(reportFeeMonthStatisticsTotalDto, payFeeOrderDtos);




        resultVo = new ResultVo((int) Math.ceil((double) count / (double) payFeeOrderDto.getRow()), count, payFeeOrderDtos, reportFeeMonthStatisticsTotalDto);
        cmdDataFlowContext.setResponseEntity(ResultVo.createResponseEntity(resultVo));

    }


    /**
     * 计算大计小计
     *
     * @param reportFeeMonthStatisticsTotalDto
     */
    private void computeTotalInfo(ReportFeeMonthStatisticsTotalDto reportFeeMonthStatisticsTotalDto,
                                  List<PayFeeOrderDto> payFeeOrderDtos) {

        BigDecimal totalReceivableAmount = new BigDecimal(0.00);

        BigDecimal totalReceivedAmount = new BigDecimal(0.00);
        BigDecimal totalPreferentialAmount = new BigDecimal(0.00);
        BigDecimal totalDeductionAmount = new BigDecimal(0.00);
        BigDecimal totalLateFee = new BigDecimal(0.00);
        BigDecimal totalGiftAmount = new BigDecimal(0.00);

        String imgUrl = MappingCache.getValue(MappingConstant.FILE_DOMAIN, "IMG_PATH");

        for (PayFeeOrderDto reportFeeMonthStatisticsDto : payFeeOrderDtos) {
            totalReceivableAmount = totalReceivableAmount.add(new BigDecimal(reportFeeMonthStatisticsDto.getReceivableAmount()));
            totalReceivedAmount = totalReceivedAmount.add(new BigDecimal(reportFeeMonthStatisticsDto.getReceivedAmount()));
            totalPreferentialAmount = totalPreferentialAmount.add(new BigDecimal(reportFeeMonthStatisticsDto.getDiscountAmount()));
            totalLateFee = totalLateFee.add(new BigDecimal(reportFeeMonthStatisticsDto.getLateAmount()));
            totalGiftAmount = totalGiftAmount.add(new BigDecimal(reportFeeMonthStatisticsDto.getGiftAmount()));

            reportFeeMonthStatisticsDto.setWithholdAmount(reportFeeMonthStatisticsDto.getAcctAmount());
            reportFeeMonthStatisticsDto.setPreferentialAmount(reportFeeMonthStatisticsDto.getDiscountAmount());
            reportFeeMonthStatisticsDto.setLateFee(reportFeeMonthStatisticsDto.getLateAmount());
            if(!StringUtil.isEmpty(reportFeeMonthStatisticsDto.getPrimeFile())){
                reportFeeMonthStatisticsDto.setPrimeFile(imgUrl+reportFeeMonthStatisticsDto.getPrimeFile());
            }
        }

        //todo 应收金额(小计)
        reportFeeMonthStatisticsTotalDto.setTotalReceivableAmount(String.format("%.2f", totalReceivableAmount.doubleValue()));

        //todo 实收金额(小计)
        reportFeeMonthStatisticsTotalDto.setTotalReceivedAmount(String.format("%.2f", totalReceivedAmount.doubleValue()));
        //todo 优惠金额(小计)
        reportFeeMonthStatisticsTotalDto.setTotalPreferentialAmount(String.format("%.2f", totalPreferentialAmount));
        //todo 减免金额(小计)
        reportFeeMonthStatisticsTotalDto.setTotalDeductionAmount(String.format("%.2f", totalDeductionAmount));
        //todo 滞纳金(小计)
        reportFeeMonthStatisticsTotalDto.setTotalLateFee(String.format("%.2f", totalLateFee));

    }


}
