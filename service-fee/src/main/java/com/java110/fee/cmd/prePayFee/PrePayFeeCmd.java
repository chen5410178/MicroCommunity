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
package com.java110.fee.cmd.prePayFee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.*;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.payFee.PayFeeRuleDto;
import com.java110.dto.prePayFee.PrePayFeeDto;
import com.java110.dto.user.UserDto;
import com.java110.fee.bill.IComputeBillPrice;
import com.java110.fee.bill.IComputeFeeDiscount;
import com.java110.fee.bill.IDeductionPrePayFee;
import com.java110.fee.bmo.fee.IGeneratorPayFeeOrder;
import com.java110.fee.bmo.receipt.IFeeReceiptBmo;
import com.java110.intf.fee.*;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.prePayFee.PrePayFeePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 类表述：保存
 * 服务编码：prePayFee.savePrePayFee
 * 请求路劲：/app/prePayFee.SavePrePayFee
 * add by 吴学文 at 2025-06-17 14:55:11 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "prePayFee.prePayFee")
public class PrePayFeeCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(PrePayFeeCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IPrePayFeeV1InnerServiceSMO prePayFeeV1InnerServiceSMOImpl;

    @Autowired
    private IComputeFeeDiscount computeFeeDiscountImpl;

    @Autowired
    private IPayFeeRuleV1InnerServiceSMO payFeeRuleV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IComputeBillPrice computeBillPriceImpl;

    @Autowired
    private IGeneratorPayFeeOrder generatorPayFeeOrderImpl;

    @Autowired
    private IDeductionPrePayFee deductionPrePayFeeImpl;

    @Autowired
    private IFeeReceiptBmo feeReceiptBmoImpl;

    @Autowired
    private IFeeReceiptInnerServiceSMO feeReceiptInnerServiceSMOImpl;

    @Autowired
    private IFeeDiscountSpecInnerServiceSMO feeDiscountSpecInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "primeRate", "请求报文中未包含primeRate");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "payerObjId", "请求报文中未包含payerObjId");
        Assert.hasKeyAndValue(reqJson, "payerObjName", "请求报文中未包含payerObjName");
        Assert.hasKeyAndValue(reqJson, "ownerId", "请求报文中未包含ownerId");
        Assert.hasKeyAndValue(reqJson, "receivedAmount", "请求报文中未包含receivedAmount");


        JSONArray rules = reqJson.getJSONArray("rules");

        JSONArray meterRules = reqJson.getJSONArray("meterRules");
        if (ListUtil.isNull(rules) && ListUtil.isNull(meterRules)) {
            throw new CmdException("未包含预存内容");
        }
        if (!ListUtil.isNull(rules)) {
            JSONObject rule = null;
            for (int ruleIndex = 0; ruleIndex < rules.size(); ruleIndex++) {
                rule = rules.getJSONObject(ruleIndex);
                Assert.hasKeyAndValue(rule, "ruleId", "未包含规则ID");
                Assert.hasKeyAndValue(rule, "cycles", "未包含缴费周期");
                Assert.hasKeyAndValue(rule, "receivedAmount", "未包含实收金额");
                Assert.hasKeyAndValue(rule, "startMonth", "未包含起始月");

            }
        }
        if (!ListUtil.isNull(meterRules)) {
            JSONObject meterRule = null;
            for (int ruleIndex = 0; ruleIndex < meterRules.size(); ruleIndex++) {
                meterRule = meterRules.getJSONObject(ruleIndex);
                Assert.hasKeyAndValue(meterRule, "ftId", "未包含费用类型");
                Assert.hasKeyAndValue(meterRule, "ftName", "未包含费用类型");
                Assert.hasKeyAndValue(meterRule, "amount", "未包含实收金额");
            }
        }


    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        String primeRate = reqJson.getString("primeRate");
        String communityId = reqJson.getString("communityId");
        String payerObjId = reqJson.getString("payerObjId");
        String payerObjName = reqJson.getString("payerObjName");
        String receivedAmount = reqJson.getString("receivedAmount");
        String ownerId = reqJson.getString("ownerId");
        String remark = reqJson.getString("remark");
        String way = reqJson.getString("way");

        String userId = CmdContextUtils.getUserId(cmdDataFlowContext);
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        Assert.listOnlyOne(userDtos, "员工未登录");

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(ownerId);
        ownerDto.setCommunityId(communityId);
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);

        String payOrderId = GenerateCodeFactory.getGeneratorId("11");

        List<PrePayFeePo> prePayFeePos = new ArrayList<>();
        String receiptCode = feeReceiptInnerServiceSMOImpl.generatorReceiptCode(communityId);
        JSONArray rules = reqJson.getJSONArray("rules");
        if (!ListUtil.isNull(rules)) {
            JSONObject rule = null;
            for (int ruleIndex = 0; ruleIndex < rules.size(); ruleIndex++) {
                rule = rules.getJSONObject(ruleIndex);
                doPrePayFee(rule, primeRate, communityId,
                        payerObjId, payerObjName,
                        receivedAmount, remark, way, userDtos.get(0),
                        ownerDtos.get(0), payOrderId, prePayFeePos, receiptCode);
            }
        }
        JSONArray meterRules = reqJson.getJSONArray("meterRules");
        if (!ListUtil.isNull(meterRules)) {
            JSONObject meterRule = null;
            for (int ruleIndex = 0; ruleIndex < meterRules.size(); ruleIndex++) {
                meterRule = meterRules.getJSONObject(ruleIndex);
                doMeterPrePayFee(meterRule, primeRate,
                        communityId, payerObjId,
                        payerObjName, receivedAmount,
                        remark, way, userDtos.get(0),
                        ownerDtos.get(0), payOrderId, prePayFeePos, receiptCode);
            }
        }
        String payerObjType = reqJson.getString("payerObjType");
        if (StringUtil.isEmpty(payerObjType)) {
            payerObjType = FeeDto.PAYER_OBJ_TYPE_ROOM;
            if (!payerObjName.contains("-")) {
                payerObjType = FeeDto.PAYER_OBJ_TYPE_PARKING_SPACE;
            }
        }
        generatorPayFeeOrderImpl.generatorPrePayFeeOrder(prePayFeePos, payerObjType);

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }


    private void doPrePayFee(JSONObject rule, String primeRate, String communityId, String payerObjId,
                             String payerObjName, String receivedAmount, String remark, String way, UserDto userDto, OwnerDto ownerDto,
                             String payOrderId, List<PrePayFeePo> prePayFeePos, String receiptCode) {
        String ruleId = rule.getString("ruleId");
        int cycles = rule.getIntValue("cycles");
        double ruleReceivedAmount = rule.getDoubleValue("receivedAmount");
        String startMonth = rule.getString("startMonth");
        String year = startMonth.split("-")[0];
        String month = startMonth.split("-")[1];

        PayFeeRuleDto payFeeRuleDto = new PayFeeRuleDto();
        payFeeRuleDto.setRuleId(ruleId);
        payFeeRuleDto.setCommunityId(communityId);
        List<PayFeeRuleDto> payFeeRuleDtos = payFeeRuleV1InnerServiceSMOImpl.queryPayFeeRules(payFeeRuleDto);
        if (ListUtil.isNull(payFeeRuleDtos)) {
            throw new CmdException("账单规则不存在");
        }
        //计算应收
        // FeeAmountDto feeAmountDto = computeBillPriceImpl.computeBillReceiveAmount(payFeeRuleDtos.get(0), cycles);

        Date startTime = DateUtil.getDateFromStringA(payFeeRuleDtos.get(0).getEndTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.set(Calendar.YEAR, Integer.parseInt(year));
        calendar.set(Calendar.MONTH, Integer.parseInt(month) - 1);
        startTime = calendar.getTime();
        calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.add(Calendar.MONTH, cycles);
        Date endTime = calendar.getTime();
        PrePayFeePo prePayFeePo = new PrePayFeePo();
        prePayFeePo.setReceivableAmount(ruleReceivedAmount + "");
        prePayFeePo.setReceivedAmount(ruleReceivedAmount + "");
        prePayFeePo.setDiscountAmount("0");
        prePayFeePo.setCycles(cycles + "");
        prePayFeePo.setStartTime(DateUtil.getFormatTimeStringB(startTime));
        prePayFeePo.setEndTime(DateUtil.getPreSecTime(endTime));
        computeFeeDiscountAmount(rule, communityId, cycles, payFeeRuleDtos.get(0), prePayFeePo);

        prePayFeePo.setGiftAmount("0");
        prePayFeePo.setPayOrderId(payOrderId);
        prePayFeePo.setPrimeRate(primeRate);
        prePayFeePo.setRemark(remark);
        prePayFeePo.setOwnerId(ownerDto.getOwnerId());
        prePayFeePo.setPayerObjName(payerObjName);
        prePayFeePo.setOwnerName(ownerDto.getName());
        prePayFeePo.setRemainAmount(ruleReceivedAmount + "");
        prePayFeePo.setFeeName(payFeeRuleDtos.get(0).getFeeName());
        prePayFeePo.setOpenInvoice("N");
        prePayFeePo.setCashierId(userDto.getUserId());
        prePayFeePo.setState(FeeDetailDto.STATE_NORMAL);
        prePayFeePo.setRuleId(ruleId);
        prePayFeePo.setCommunityId(communityId);
        prePayFeePo.setCashierName(userDto.getName());
        prePayFeePo.setPpfId(GenerateCodeFactory.getGeneratorId("11"));
        prePayFeePo.setPayerObjId(payerObjId);
        prePayFeePo.setFtId(payFeeRuleDtos.get(0).getFtId());
        prePayFeePo.setFtName(payFeeRuleDtos.get(0).getFtName());
        prePayFeePo.setWay(way);

        int flag = prePayFeeV1InnerServiceSMOImpl.savePrePayFee(prePayFeePo);

        feeReceiptBmoImpl.savePrePayFeeReceipt(prePayFeePo, receiptCode);

        prePayFeePos.add(prePayFeePo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        if (PrePayFeeDto.WAY_NO.equals(way)) {
            return;
        }

        deductionPrePayFeeImpl.deductionPrePayFee(prePayFeePo.getPpfId());
    }

    private void doMeterPrePayFee(JSONObject meterRule, String primeRate, String communityId,
                                  String payerObjId, String payerObjName, String receivedAmount,
                                  String remark, String way, UserDto userDto, OwnerDto ownerDto,
                                  String payOrderId, List<PrePayFeePo> prePayFeePos, String receiptCode) {
        String ruleId = "-1";
        int cycles = 1;
        double amount = meterRule.getDoubleValue("amount");
        double discountAmount = 0.00;

        PrePayFeePo prePayFeePo = new PrePayFeePo();
        prePayFeePo.setGiftAmount("0");
        prePayFeePo.setPayOrderId(payOrderId);
        prePayFeePo.setPrimeRate(primeRate);
        prePayFeePo.setReceivableAmount(amount + "");
        prePayFeePo.setDiscountAmount("0");
        prePayFeePo.setCycles(cycles + "");
        prePayFeePo.setRemark(remark);
        prePayFeePo.setReceivedAmount(amount + "");
        prePayFeePo.setOwnerId(ownerDto.getOwnerId());
        prePayFeePo.setPayerObjName(payerObjName);
        prePayFeePo.setOwnerName(ownerDto.getName());
        prePayFeePo.setRemainAmount(amount + "");
        prePayFeePo.setFeeName(meterRule.getString("ftName"));
        prePayFeePo.setOpenInvoice("N");
        prePayFeePo.setStartTime(DateUtil.getFormatTimeStringB(new Date()));
        prePayFeePo.setCashierId(userDto.getUserId());
        prePayFeePo.setState(FeeDetailDto.STATE_NORMAL);
        prePayFeePo.setEndTime("2050-01-01");
        prePayFeePo.setRuleId(ruleId);
        prePayFeePo.setCommunityId(communityId);
        prePayFeePo.setCashierName(userDto.getName());
        prePayFeePo.setPpfId(GenerateCodeFactory.getGeneratorId("11"));
        prePayFeePo.setPayerObjId(payerObjId);
        prePayFeePo.setFtId(meterRule.getString("ftId"));
        prePayFeePo.setFtName(meterRule.getString("ftName"));
        prePayFeePo.setWay(way);

        int flag = prePayFeeV1InnerServiceSMOImpl.savePrePayFee(prePayFeePo);
        feeReceiptBmoImpl.savePrePayFeeReceipt(prePayFeePo, receiptCode);
        prePayFeePos.add(prePayFeePo);
        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }
        if (PrePayFeeDto.WAY_NO.equals(way)) {
            return;
        }

        deductionPrePayFeeImpl.deductionPrePayFee(prePayFeePo.getPpfId());
    }

    private void computeFeeDiscountAmount(JSONObject rule, String communityId, int cycles, PayFeeRuleDto payFeeRuleDto, PrePayFeePo prePayFeePo) {
        if (!rule.containsKey("selectedDiscountIds")) {
            return;
        }
        JSONArray selectedDiscountIds = rule.getJSONArray("selectedDiscountIds");

        if (ListUtil.isNull(selectedDiscountIds)) {
            return;
        }
        List<ComputeDiscountDto> computeDiscountDtos = computeFeeDiscountImpl.computePrePayFeeDiscount(payFeeRuleDto, cycles);
        if (ListUtil.isNull(computeDiscountDtos)) {
            return;
        }

        BigDecimal receivedAmountDec = new BigDecimal(prePayFeePo.getReceivedAmount());
        // receivedAmountDec = receivedAmountDec.subtract(new BigDecimal(discountAmount));

        BigDecimal discountAmount = new BigDecimal("0");
        for (ComputeDiscountDto computeDiscountDto : computeDiscountDtos) {
            for (int dIndex = 0; dIndex < selectedDiscountIds.size(); dIndex++) {
                if (!selectedDiscountIds.getString(dIndex).equals(computeDiscountDto.getDiscountId())) {
                    continue;
                }
                //这里不考虑多个折扣的情况
                discountAmount = discountAmount.add(new BigDecimal(computeDiscountDto.getDiscountPrice()));
                if (!"102020008".equals(computeDiscountDto.getRuleId())) {
                    receivedAmountDec = receivedAmountDec.add(new BigDecimal(computeDiscountDto.getDiscountPrice()));
                    continue;
                }
                FeeDiscountSpecDto feeDiscountSpecDto = new FeeDiscountSpecDto();
                feeDiscountSpecDto.setDiscountId(computeDiscountDto.getDiscountId());
                List<FeeDiscountSpecDto> feeDiscountSpecDtos = feeDiscountSpecInnerServiceSMOImpl.queryFeeDiscountSpecs(feeDiscountSpecDto);
                if (ListUtil.isNull(feeDiscountSpecDtos)) {
                    continue;
                }

                for (FeeDiscountSpecDto tFeeDiscountSpecDto : feeDiscountSpecDtos) {
                    if (!"89002020980015".equals(tFeeDiscountSpecDto.getSpecId())) { //赠送月份
                        continue;
                    }
                    String specValue = tFeeDiscountSpecDto.getSpecValue();
                    //获取费用结束时间(也就是下次费用开始时间)
                    Date endTime = DateUtil.getDateFromStringA(prePayFeePo.getEndTime());
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(endTime);
                    cal.add(Calendar.MONTH, Integer.parseInt(specValue));
                    //cal.add(Calendar.SECOND,1);
                    prePayFeePo.setEndTime(DateUtil.getFormatTimeStringA(cal.getTime()));
                    double cycle = Double.parseDouble(prePayFeePo.getCycles()) + Integer.parseInt(specValue);
                    prePayFeePo.setCycles(cycle + "");
                }
            }
        }
        discountAmount = discountAmount.multiply(new BigDecimal("-1")).setScale(2, BigDecimal.ROUND_HALF_UP);
        prePayFeePo.setReceivedAmount(receivedAmountDec.doubleValue() + "");
        prePayFeePo.setDiscountAmount(discountAmount.doubleValue() + "");
    }
}
