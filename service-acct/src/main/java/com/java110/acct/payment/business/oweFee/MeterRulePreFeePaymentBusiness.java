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
package com.java110.acct.payment.business.oweFee;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.payment.IPaymentBusiness;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.payment.PaymentOrderDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.fee.IComputeMeterPreFeeInnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 水电预存业务适配器
 * 参考 RulePayFeePaymentBusiness 类
 * 区别：没有周期来计算金额，而是通过页面填写，通过字段receivedAmount来确定金额
 * unified 方法不需要调用计算金额，在notifyPayment时 cycles 设置为1即可
 * add by 吴学文 at 2025-01-XX mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Service("meterRulePreFee")
public class MeterRulePreFeePaymentBusiness implements IPaymentBusiness {
    private final static Logger logger = LoggerFactory.getLogger(MeterRulePreFeePaymentBusiness.class);
    
    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IComputeMeterPreFeeInnerServiceSMO computeMeterPreFeeInnerServiceSMOImpl;

    @Override
    public PaymentOrderDto unified(ICmdDataFlowContext context, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(reqJson, "cashierUserId", "请求报文中未包含cashierUserId节点");
        Assert.hasKeyAndValue(reqJson, "payerObjId", "请求报文中未包含payerObjId");
        Assert.hasKeyAndValue(reqJson, "receivedAmount", "请求报文中未包含receivedAmount");
        Assert.hasKeyAndValue(reqJson, "ftId", "请求报文中未包含ftId");
        Assert.hasKeyAndValue(reqJson, "ftName", "请求报文中未包含ftName");
        Assert.hasKeyAndValue(reqJson, "feeName", "请求报文中未包含feeName");
        
        UserDto userDto = new UserDto();
        userDto.setUserId(reqJson.getString("cashierUserId"));
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        if (ListUtil.isNull(userDtos)) {
            throw new CmdException("缴费用户不存在");
        }

        // 水电预存不需要调用计算金额的方法，直接使用 receivedAmount
        String orderId = GenerateCodeFactory.getOId();

        PaymentOrderDto paymentOrderDto = new PaymentOrderDto();
        paymentOrderDto.setOrderId(orderId);
        paymentOrderDto.setMoney(reqJson.getDoubleValue("receivedAmount"));
        paymentOrderDto.setName(reqJson.getString("feeName"));
        paymentOrderDto.setUserId(userDtos.get(0).getUserId());
        paymentOrderDto.setCycles("1"); // 水电预存固定为1个周期

        JSONObject saveFees = new JSONObject();
        saveFees.put("orderId", orderId);
        saveFees.put("userId", userDtos.get(0).getUserId());
        saveFees.put("money", reqJson.getDoubleValue("receivedAmount"));
        saveFees.put("communityId", reqJson.getString("communityId"));
        saveFees.put("receivedAmount", reqJson.getDoubleValue("receivedAmount"));
        saveFees.put("roomId", reqJson.getString("payerObjId"));
        saveFees.put("ftId", reqJson.getString("ftId"));
        saveFees.put("ftName", reqJson.getString("ftName"));
        saveFees.put("feeName", reqJson.getString("feeName"));
        saveFees.put("payerObjId", reqJson.getString("payerObjId"));
        saveFees.put("payerObjType", reqJson.getString("payerObjType") != null ? reqJson.getString("payerObjType") : "3333");
        CommonCache.setValue("METER_RULE_PRE_FEE_" + orderId, saveFees.toJSONString(), CommonCache.PAY_DEFAULT_EXPIRE_TIME);
        return paymentOrderDto;
    }

    @Override
    public void notifyPayment(PaymentOrderDto paymentOrderDto, JSONObject reqJson) {
        UserDto userDto = new UserDto();
        userDto.setUserId(paymentOrderDto.getUserId());
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        if (ListUtil.isNull(userDtos)) {
            throw new CmdException("缴费人员不存在");
        }

        String order = CommonCache.getAndRemoveValue("METER_RULE_PRE_FEE_" + paymentOrderDto.getOrderId());

        JSONObject paramIn = JSONObject.parseObject(order);

        paramIn.put("userId", userDtos.get(0).getUserId());
        paramIn.put("userName", userDtos.get(0).getName());
        paramIn.put("cycles", "1"); // 水电预存固定为1个周期

        computeMeterPreFeeInnerServiceSMOImpl.meterPrePayFee(paramIn);
    }
}

