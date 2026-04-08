package com.java110.acct.payment;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.payment.PaymentOrderDto;

import java.util.Map;

public interface IBhPayAdapt {

    //保存订单到数据库表里
    Map paymentClose(PaymentOrderDto paymentOrderDto, JSONObject reqJson) throws Exception;

    PaymentOrderDto unifiedPayFee(JSONObject reqJson);
}
