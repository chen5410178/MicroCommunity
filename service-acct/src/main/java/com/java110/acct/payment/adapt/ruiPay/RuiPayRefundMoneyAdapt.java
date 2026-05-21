package com.java110.acct.payment.adapt.ruiPay;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.payment.IRefundMoneyAdapt;
import com.java110.acct.payment.adapt.easypay.BasePay;
import com.java110.acct.payment.adapt.easypay.utils.HttpConnectUtils;
import com.java110.core.client.FtpUploadTemplate;
import com.java110.core.client.S3UploadTemplate;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.factory.WechatFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.onlinePayRefund.OnlinePayRefundDto;
import com.java110.dto.payment.PaymentPoolDto;
import com.java110.dto.payment.PaymentPoolValueDto;
import com.java110.dto.wechat.OnlinePayDto;
import com.java110.intf.acct.IOnlinePayRefundV1InnerServiceSMO;
import com.java110.intf.acct.IOnlinePayV1InnerServiceSMO;
import com.java110.intf.acct.IPaymentPoolValueV1InnerServiceSMO;
import com.java110.intf.fee.IReturnPayFeeInnerServiceSMO;
import com.java110.intf.order.IOrderInnerServiceSMO;
import com.java110.intf.store.ISmallWechatV1InnerServiceSMO;
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
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("ruiPayRefundMoney")
public class RuiPayRefundMoneyAdapt implements IRefundMoneyAdapt {


    @Autowired
    private IPaymentPoolValueV1InnerServiceSMO paymentPoolValueV1InnerServiceSMOImpl;

    @Autowired
    private IReturnPayFeeInnerServiceSMO returnPayFeeInnerServiceSMOImpl;

    @Autowired
    private IOrderInnerServiceSMO orderInnerServiceSMOImpl;

    @Autowired
    private IOnlinePayV1InnerServiceSMO onlinePayV1InnerServiceSMOImpl;

    @Autowired
    private ISmallWechatV1InnerServiceSMO smallWechatV1InnerServiceSMOImpl;

    @Autowired
    private RestTemplate outRestTemplate;

    public static final String RUI_PAY_URL = "http://api.ruishengyi.com/rb-client/platform/openPlatform/router";

    private static Logger logger = LoggerFactory.getLogger(RuiPayRefundMoneyAdapt.class);

    @Autowired
    private FtpUploadTemplate ftpUploadTemplate;

    @Autowired
    private S3UploadTemplate s3UploadTemplate;

    @Autowired
    private IOnlinePayRefundV1InnerServiceSMO onlinePayRefundV1InnerServiceSMOImpl;

    @Override
    public ResultVo refund(OnlinePayDto onlinePayDto, PaymentPoolDto paymentPoolDto) throws Exception {

        PaymentPoolValueDto paymentPoolValueDto = new PaymentPoolValueDto();
        paymentPoolValueDto.setPpId(paymentPoolDto.getPpId());
        List<PaymentPoolValueDto> paymentPoolValueDtos = paymentPoolValueV1InnerServiceSMOImpl.queryPaymentPoolValues(paymentPoolValueDto);

        if (ListUtil.isNull(paymentPoolValueDtos)) {
            throw new IllegalArgumentException("配置错误,未配置参数");
        }

        String appKey = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "RUI_APPKEY"); // 客户编号
        String secret = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "RUI_SECRET");
        String rpiNo = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "RPI_NO");

        // todo 查询退费明细
        OnlinePayRefundDto onlinePayRefundDto = new OnlinePayRefundDto();
        onlinePayRefundDto.setPayId(onlinePayDto.getPayId());
        onlinePayRefundDto.setState(OnlinePayDto.STATE_WT);
        List<OnlinePayRefundDto> onlinePayRefundDtos = onlinePayRefundV1InnerServiceSMOImpl.queryOnlinePayRefunds(onlinePayRefundDto);
        String tranNo = GenerateCodeFactory.getGeneratorId("11");
        if (!ListUtil.isNull(onlinePayRefundDtos)) {
            tranNo = onlinePayRefundDtos.get(0).getRefundId();
        }

        Map<String, String> params = new HashMap<>();
        params.put("appkey", appKey);
        params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        params.put("method", "rsypay.online.trade.refundv1.1");
        params.put("rpi_no", rpiNo);
        //params.put("notify_url", notifyUrl + "?wId=" + WechatFactory.getWId(smallWeChatDto.getAppId()));

        Map<String, String> bizContent = new HashMap<>();
        bizContent.put("out_request_no", tranNo);//微信用户openId
        bizContent.put("refund_amount", onlinePayDto.getRefundFee());
        bizContent.put("refund_reason", "用户主动退款");
        String sign = RuiPaySignatureGenerator.generateSign(bizContent, secret);
        bizContent.put("order_code", onlinePayDto.getTransactionId());//支付方式(1微信、2支付宝、3银联)
        //bizContent.put("aisle_order_code", onlinePayDto.getOrderId());//支付方式(1微信、2支付宝、3银联)


        // 将bizContent转为JSON字符串
        String bizContentJson = JSONObject.toJSONString(bizContent);
        params.put("biz_content", bizContentJson);

        // 4. 生成签名
        params.put("sign", sign);

        String queryString = RuiPaySignatureGenerator.buildQueryString(params);

        // 6. 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        System.out.println("\n请求报文：" + queryString);
        // 7. 发送请求
        HttpEntity<String> requestEntity = new HttpEntity<>(queryString, headers);
        ResponseEntity<String> response = outRestTemplate.postForEntity(RUI_PAY_URL, requestEntity, String.class);
        System.out.println("\n响应报文：" + response);
        // 8. 处理响应
        if (response.getStatusCode() != HttpStatus.OK) {
            return new ResultVo(ResultVo.CODE_ERROR, "退款失败" + response.getBody());

        }

        JSONObject paramOut = JSONObject.parseObject(response.getBody());
        if(paramOut.getIntValue("code") != 200){
            return new ResultVo(ResultVo.CODE_ERROR, "退款失败" + paramOut.getString("msg"));

        }

        JSONObject data = paramOut.getJSONObject("data").getJSONObject("rsy_online_trade_refund_response");

        if (!"success".equals(data.getString("status"))) {
            return new ResultVo(ResultVo.CODE_ERROR, "退款失败" + data.getString("status_msg"));
        }

        return new ResultVo(ResultVo.CODE_OK, data.getString("status_msg"));


    }


}
