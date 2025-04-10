package com.java110.acct.payment.adapt.ruiPay;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.payment.adapt.easypay.BasePay;
import com.java110.acct.payment.adapt.easypay.utils.HttpConnectUtils;
import com.java110.acct.smo.IQrCodePaymentSMO;
import com.java110.core.client.RestTemplate;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.payment.PaymentPoolValueDto;
import com.java110.dto.wechat.OnlinePayDto;
import com.java110.dto.wechat.SmallWeChatDto;
import com.java110.intf.acct.IOnlinePayV1InnerServiceSMO;
import com.java110.intf.acct.IPaymentPoolValueV1InnerServiceSMO;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.po.wechat.OnlinePayPo;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.PayUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.*;

/**
 * 微信支付
 */
@Service
public class QrCodeRuiPayPaymentAdapt implements IQrCodePaymentSMO {
    private static Logger logger = LoggerFactory.getLogger(QrCodeRuiPayPaymentAdapt.class);

    //微信支付
    public static final String DOMAIN_WECHAT_PAY = "WECHAT_PAY";
    // 微信服务商支付开关
    public static final String WECHAT_SERVICE_PAY_SWITCH = "WECHAT_SERVICE_PAY_SWITCH";

    //开关ON打开
    public static final String WECHAT_SERVICE_PAY_SWITCH_ON = "ON";


    private static final String WECHAT_SERVICE_APP_ID = "SERVICE_APP_ID";

    private static final String WECHAT_SERVICE_MCH_ID = "SERVICE_MCH_ID";


    @Autowired
    private ISmallWeChatInnerServiceSMO smallWeChatInnerServiceSMOImpl;

    @Autowired
    private RestTemplate outRestTemplate;

    @Autowired
    private IPaymentPoolValueV1InnerServiceSMO paymentPoolValueV1InnerServiceSMOImpl;

    @Autowired
    private IOnlinePayV1InnerServiceSMO onlinePayV1InnerServiceSMOImpl;

    public static final String RUI_PAY_URL = "http://api.ruishengyi.com/rb-client/platform/openPlatform/router";


    @Override
    public ResultVo pay(String communityId, String orderNum, double money, String authCode, String feeName, String paymentPoolId) throws Exception {
        logger.info("【小程序支付】 统一下单开始, 订单编号=" + orderNum);
        SortedMap<String, String> resultMap = new TreeMap<String, String>();
        //生成支付金额，开发环境处理支付金额数到0.01、0.02、0.03元
        double payAmount = PayUtil.getPayAmountByEnv(MappingCache.getValue(MappingConstant.ENV_DOMAIN, "HC_ENV"), money);
        //添加或更新支付记录(参数跟进自己业务需求添加)

        Map<String, String> resMap = null;
        logger.debug("resMap=" + resMap);
        PaymentPoolValueDto paymentPoolValueDto = new PaymentPoolValueDto();
        paymentPoolValueDto.setPpId(paymentPoolId);
        List<PaymentPoolValueDto> paymentPoolValueDtos = paymentPoolValueV1InnerServiceSMOImpl.queryPaymentPoolValues(paymentPoolValueDto);

        if (ListUtil.isNull(paymentPoolValueDtos)) {
            throw new IllegalArgumentException("配置错误,未配置参数");
        }

        String appKey = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "RUI_APPKEY"); // 客户编号
        String secret = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "RUI_SECRET");
        String rpiNo = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "RPI_NO");
        SmallWeChatDto shopSmallWeChatDto = null;

        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setObjId(communityId);
        List<SmallWeChatDto> smallWeChatDtos = smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto);
        if (smallWeChatDtos == null || smallWeChatDtos.size() < 1) {
            shopSmallWeChatDto = new SmallWeChatDto();
            shopSmallWeChatDto.setObjId(communityId);
            shopSmallWeChatDto.setAppId(MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, "appId"));
            shopSmallWeChatDto.setAppSecret(MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, "appSecret"));
        } else {
            shopSmallWeChatDto = smallWeChatDtos.get(0);
        }

        Map<String, String> params = new HashMap<>();
        params.put("appkey", appKey);
        params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        params.put("method", "rsypay.online.barcodepay");
        params.put("rpi_no", rpiNo);
        //params.put("notify_url", notifyUrl + "?wId=" + WechatFactory.getWId(smallWeChatDto.getAppId()));

        Map<String, String> bizContent = new HashMap<>();
        bizContent.put("total_amount", payAmount + "");
        bizContent.put("auth_code", authCode);
        int pre = Integer.parseInt(authCode.substring(0, 2));
        String payWay = "1";
        if (pre > 24 && pre < 31) { // 支付宝
            payWay = "2";
        }
        bizContent.put("pay_way", payWay);//支付方式(1微信、2支付宝、3银联)
        bizContent.put("out_trade_no", orderNum);
        bizContent.put("shopdate", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_H));
        bizContent.put("subject", feeName);

        // 将bizContent转为JSON字符串
        String bizContentJson = JSONObject.toJSONString(bizContent);
        params.put("biz_content", bizContentJson);

        // 4. 生成签名
        String sign = RuiPaySignatureGenerator.generateSign(params, secret);
        params.put("sign", sign);


        // 5. 准备form数据
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        params.forEach(formData::add);

        // 6. 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 7. 发送请求
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);
        ResponseEntity<String> response = outRestTemplate.postForEntity(RUI_PAY_URL, requestEntity, String.class);
        System.out.println("\n响应报文：" + response);

        // 8. 处理响应
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException(response.getBody());
        }

        JSONObject paramOut = JSONObject.parseObject(response.getBody());
        if (paramOut.getIntValue("code") != 200) {
            throw new IllegalArgumentException(paramOut.getString("msg"));
        }

        JSONObject data = paramOut.getJSONObject("data").getJSONObject("rsy_online_pay_response");
        doSaveOnlinePay(shopSmallWeChatDto, "-1", orderNum, data.getString("order_code"), feeName, payAmount,
                OnlinePayDto.STATE_WAIT, "待支付", paymentPoolValueDtos.get(0).getPpId());

        if ("TRADE_SUCCESS".equals(data.getString("trade_status"))) {
            doUpdateOnlinePay(orderNum, OnlinePayDto.STATE_COMPILE, "支付成功");
            return new ResultVo(ResultVo.CODE_OK, "成功");
        } else {
            return new ResultVo(ResultVo.CODE_WAIT_PAY, "等待用户支付中");
        }
    }

    public ResultVo checkPayFinish(String communityId, String orderNum, String paymentPoolId) {
        Map<String, String> result = null;
        PaymentPoolValueDto paymentPoolValueDto = new PaymentPoolValueDto();
        paymentPoolValueDto.setPpId(paymentPoolId);
        paymentPoolValueDto.setCommunityId(communityId);
        List<PaymentPoolValueDto> paymentPoolValueDtos = paymentPoolValueV1InnerServiceSMOImpl.queryPaymentPoolValues(paymentPoolValueDto);


        if (ListUtil.isNull(paymentPoolValueDtos)) {
            throw new IllegalArgumentException("配置错误,未配置参数");
        }
        String appKey = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "RUI_APPKEY"); // 客户编号
        String secret = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "RUI_SECRET");
        String rpiNo = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "RPI_NO");


        Map<String, String> params = new HashMap<>();
        params.put("appkey", appKey);
        params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        params.put("method", "rsypay.online.trade.order.query");
        params.put("rpi_no", rpiNo);
        //params.put("notify_url", notifyUrl + "?wId=" + WechatFactory.getWId(smallWeChatDto.getAppId()));
        OnlinePayDto onlinePayDto = new OnlinePayDto();
        onlinePayDto.setOrderId(orderNum);
        List<OnlinePayDto> onlinePayDtos = onlinePayV1InnerServiceSMOImpl.queryOnlinePays(onlinePayDto);
        Map<String, String> bizContent = new HashMap<>();
        if (!ListUtil.isNull(onlinePayDtos)) {
            bizContent.put("order_code", onlinePayDtos.get(0).getTransactionId());
        }
        bizContent.put("out_trade_no", orderNum);
        bizContent.put("shopdate", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_H));

        // 将bizContent转为JSON字符串
        String bizContentJson = JSONObject.toJSONString(bizContent);
        params.put("biz_content", bizContentJson);

        // 4. 生成签名
        String sign = RuiPaySignatureGenerator.generateSign(params, secret);
        params.put("sign", sign);


        // 5. 准备form数据
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        params.forEach(formData::add);

        // 6. 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 7. 发送请求
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);
        ResponseEntity<String> response = outRestTemplate.postForEntity(RUI_PAY_URL, requestEntity, String.class);
        System.out.println("\n响应报文：" + response);

        // 8. 处理响应
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException(response.getBody());
        }

        JSONObject paramOut = JSONObject.parseObject(response.getBody());
        if (paramOut.getIntValue("code") != 200) {
            throw new IllegalArgumentException(paramOut.getString("msg"));
        }

        JSONObject data = paramOut.getJSONObject("data").getJSONObject("rsy_online_pay_response");

        if ("1".equals(data.getString("pay_state"))) {
            doUpdateOnlinePay(orderNum, OnlinePayDto.STATE_COMPILE, "支付成功");
            return new ResultVo(ResultVo.CODE_OK, "成功");
        } else {
            return new ResultVo(ResultVo.CODE_ERROR, "等待用户支付中");
        }

    }

    private void doSaveOnlinePay(SmallWeChatDto smallWeChatDto, String openId, String orderId, String transactionId, String feeName,
                                 double money, String state, String message, String ppId) {
        OnlinePayPo onlinePayPo = new OnlinePayPo();
        onlinePayPo.setAppId(smallWeChatDto.getAppId());
        onlinePayPo.setMchId(smallWeChatDto.getMchId());
        onlinePayPo.setMessage(message.length() > 1000 ? message.substring(0, 1000) : message);
        onlinePayPo.setOpenId(openId);
        onlinePayPo.setOrderId(orderId);
        onlinePayPo.setPayId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_orderId));
        onlinePayPo.setPayName(feeName);
        onlinePayPo.setRefundFee("0");
        onlinePayPo.setState(state);
        onlinePayPo.setTotalFee(money + "");
        onlinePayPo.setTransactionId(orderId);
        onlinePayPo.setPaymentPoolId(ppId);
        onlinePayV1InnerServiceSMOImpl.saveOnlinePay(onlinePayPo);
    }

    private void doUpdateOnlinePay(String orderId, String state, String message) {
        OnlinePayPo onlinePayPo = new OnlinePayPo();
        onlinePayPo.setMessage(message.length() > 1000 ? message.substring(0, 1000) : message);
        onlinePayPo.setOrderId(orderId);
        onlinePayPo.setState(state);
        onlinePayV1InnerServiceSMOImpl.updateOnlinePay(onlinePayPo);
    }
}
