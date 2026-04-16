package com.java110.acct.cmd.payment;


import com.alibaba.fastjson.JSONObject;
import com.java110.acct.cmd.payment.util.GmUtil;
import com.java110.acct.cmd.payment.util.HttpClient;
import com.java110.acct.cmd.payment.util.JsonUtil;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.community.CommunitySettingDto;
import com.java110.dto.community.CommunitySettingKeyDto;
import com.java110.dto.payment.PaymentPoolDto;
import com.java110.dto.payment.PaymentPoolValueDto;
import com.java110.intf.acct.IPaymentPoolConfigV1InnerServiceSMO;
import com.java110.intf.acct.IPaymentPoolV1InnerServiceSMO;
import com.java110.intf.acct.IPaymentPoolValueV1InnerServiceSMO;
import com.java110.intf.community.ICommunitySettingInnerServiceSMO;
import com.java110.intf.community.ICommunitySettingKeyV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

/**
 * 获取小区微信AppId
 */
@Java110Cmd(serviceCode = "app.BhPayUrlCmd")
public class BhPayUrlCmd extends Cmd {

    @Value("${url}")
    private  String url;

    @Value("${asynUrl}")
    private String asynUrl;

    @Value("${returnUrl}")
    private  String returnUrl;

    @Value("${testUrl}")
    private  String testUrl;

    private static String VERSION = "1.0";

    private static String SIGN_TYPE = "SM4";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private IPaymentPoolV1InnerServiceSMO paymentPoolV1InnerServiceSMOImpl;

    @Autowired
    private IPaymentPoolValueV1InnerServiceSMO paymentPoolValueV1InnerServiceSMOImpl;

    @Autowired
    private IPaymentPoolConfigV1InnerServiceSMO paymentPoolConfigV1InnerServiceSMOImpl;


    @Autowired
    private ICommunitySettingKeyV1InnerServiceSMO communitySettingKeyV1InnerServiceSMOImpl;

    @Autowired
    private ICommunitySettingInnerServiceSMO communitySettingInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson,"communityId","未包含小区ID");
        Assert.hasKeyAndValue(reqJson,"amt","未包含金额");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String mchtNo_SM4 = null;
        String productNo_SM4 = null;
        String publicKey_SM4 = null;
        String privateKey_SM4 = null;
//        CommunitySettingKeyDto dto = new CommunitySettingKeyDto();
//        dto.setCommunityId(reqJson.getString("communityId"));
//        dto.setSettingType("9070");
//        List<CommunitySettingKeyDto> communitySettingKeyDtos = communitySettingKeyV1InnerServiceSMOImpl.queryCommunitySettingKeys(dto);
//        Map<String,Object> returnData = new HashMap<>();
//        computeKeyValue(communitySettingKeyDtos,reqJson.getString("communityId"),"9070");
//        if(communitySettingKeyDtos.size()>0){
//            for (CommunitySettingKeyDto item:
//            communitySettingKeyDtos) {
//                if (item.getSettingKey().equals("mchtNo_SM4")) {
////                    returnData.put("mchtNo",item.getSettingValue());
//                    mchtNo_SM4 =item.getSettingValue();
//                }
//                if (item.getSettingKey().equals("publicKey_SM4")) {
////                    returnData.put("publicKey",item.getSettingValue());
//                    publicKey_SM4 = item.getSettingValue();
//                }
//                if (item.getSettingKey().equals("privateKey_SM4")) {
////                    returnData.put("privateKey_SM4",item.getSettingValue());
//                    privateKey_SM4 = item.getSettingValue();
//                }
//                if (item.getSettingKey().equals("productNo_SM4")) {
////                    returnData.put("productNo",item.getSettingValue());
//                    productNo_SM4 = item.getSettingValue();
//                }
//            }
//
//        }

        PaymentPoolDto paymentPoolDto = new PaymentPoolDto();
        paymentPoolDto.setCommunityId(reqJson.getString("communityId"));
        List<PaymentPoolDto> paymentPoolDtos = paymentPoolV1InnerServiceSMOImpl.queryPaymentPools(paymentPoolDto);



        PaymentPoolValueDto paymentPoolValueDto = new PaymentPoolValueDto();
        paymentPoolValueDto.setPpId(paymentPoolDtos.get(0).getPpId());
        paymentPoolValueDto.setCommunityId(paymentPoolDtos.get(0).getCommunityId());
        List<PaymentPoolValueDto> values =  paymentPoolValueV1InnerServiceSMOImpl.queryPaymentPoolValues(paymentPoolValueDto);

        if(values!=null){
            for (PaymentPoolValueDto key:values
                 ) {
                if(key.getColumnKey().equals("mchtNo_SM4")){
                    mchtNo_SM4 = key.getColumnValue();
                    System.out.println("mchtNo_SM4================"+mchtNo_SM4);
                }
                if(key.getColumnKey().equals("productNo_SM4")){
                    productNo_SM4 = key.getColumnValue();
                    System.out.println("productNo_SM4================"+productNo_SM4);
                }
                if(key.getColumnKey().equals("publicKey_SM4")){
                    publicKey_SM4 = key.getColumnValue();
                    System.out.println("publicKey_SM4================"+publicKey_SM4);
                }
                if(key.getColumnKey().equals("privateKey_SM4")){
                    privateKey_SM4 = key.getColumnValue();
                    System.out.println("privateKey_SM4================"+privateKey_SM4);
                }
            }
        }
        if (mchtNo_SM4 == null){
            System.out.println("mchtNo_SM4======================="+mchtNo_SM4);
            throw new RuntimeException("mchtNo_SM4不存在 请先配置");
        }
        if (productNo_SM4 == null){
            System.out.println("productNo_SM4======================="+productNo_SM4);
            throw new RuntimeException("productNo_SM4 请先配置");
        }
        if (publicKey_SM4 == null){
            System.out.println("publicKey_SM4======================="+publicKey_SM4);
            throw new RuntimeException("publicKey_SM4不存在 请先配置");
        }
        if (privateKey_SM4 == null){
            System.out.println("privateKey_SM4======================="+privateKey_SM4);
            throw new RuntimeException("privateKey_SM4不存在 请先配置");
        }


        try{
            Map<String, Object> params = new HashMap();
            String tranNo1 = String.valueOf(System.nanoTime());
            params.put("version", VERSION);
//            params.put("mcht_no", returnData.get("mchtNo"));
            params.put("mcht_no", mchtNo_SM4);
//            params.put("product_no", returnData.get("productNo"));
            params.put("product_no", productNo_SM4);
            params.put("trade_type", "H5");//APPLET小程序跳转 H5
            String no = reqJson.getString("cashierUserId")+tranNo1;
            params.put("tran_no", no);
            params.put("amt", reqJson.getString("amt"));
            params.put("ware_name", "收银台物业费代缴收款");

            params.put("trans_source", "09");
            System.out.println("tran_no=================="+tranNo1+reqJson.getString("cashierUserId"));
            reqJson.put("context",context);
            redisTemplate.opsForValue().set(mchtNo_SM4,reqJson.getString("communityId"));
            redisTemplate.opsForValue().set(no,reqJson);
            String jsonString = JSONObject.toJSONString(context);
            redisTemplate.opsForValue().set("data"+no,jsonString);
            System.out.println("传入的communityId=================="+reqJson.getString("communityId"));
            params.put("ware_describe", reqJson.getString("communityId"));
            params.put("disable_pay_way", "wxpay;alipay;ebankpay;unib2bpay;qrcodepay;unib2cpay");//禁用支付方式
//            params.put("asyn_url", "http://116.8.107.234:8008/BhOrder/bhOrderCompleted");//支付完成后的通知地址测试环境的
            params.put("asyn_url", asynUrl);//支付完成后的通知地址
//            params.put("jump_url", "http://odev.guiejia.cn/#/pages/successPage/successPage?msg=支付成功&objType=3003");//支付完成后跳转地址 这里是测试环境的地址
            params.put("jump_url", returnUrl+"?msg=支付成功&objType=3003");//支付完成后跳转地址 这里是测试环境的地址
            String json = JsonUtil.mapToJson(params);
            System.out.println("加密前========================：" + json);
            String secretKey = GmUtil.generateSm4Key();
            String encrypt = GmUtil.encryptSm4(json, secretKey);
            System.out.println("加密后：" + encrypt);
            Map<String, Object> signParams = new HashMap();
//            signParams.put("mcht_no", returnData.get("mchtNo"));
            signParams.put("mcht_no", mchtNo_SM4);
            signParams.put("sign_type", SIGN_TYPE);
//            signParams.put("message_key", GmUtil.encryptSm2(secretKey, (String) returnData.get("publicKey")));
            String message_key = GmUtil.encryptSm2(secretKey, publicKey_SM4);
            signParams.put("message_key", message_key);
            signParams.put("enc_data", encrypt);
            String requestParams = JsonUtil.mapToJson(signParams);
            System.out.println("最终请求参数：" + requestParams);
            String yhUrl = url;
            System.out.println("url==="+yhUrl);
            String returnResult = HttpClient.post(yhUrl, requestParams, false);
            System.out.println("支付结果返回值(原文):" + returnResult);
            if (returnResult == null) {
                System.err.println("通道响应异常");
                context.setResponseEntity(ResultVo.error("通道响应异常"));
            } else {
                Map<String, Object> responseParams = JsonUtil.jsonToMap(returnResult);
                if (!responseParams.containsKey("enc_data")) {
                    System.err.println("交易失败-->" + responseParams.get("return_code") + ":" + responseParams.get("return_message"));
                    context.setResponseEntity(ResultVo.error("交易失败-->" + responseParams.get("return_code") + ":" + responseParams.get("return_message")));
                } else {
                    String decryptStr = (String)responseParams.get("enc_data");
                    String decrypt = GmUtil.decryptSm4(decryptStr, secretKey);
                    if (decrypt == null) {
                        System.err.println("解密失败");
                        context.setResponseEntity(ResultVo.error("解密失败"));
                    } else {
                        System.out.println("收银台url代码请求(解密后):" + decrypt);
                        Map<String,Object>map = JsonUtil.jsonToMap(decrypt);
                        context.setResponseEntity(ResultVo.createResponseEntity(map));
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            context.setResponseEntity(ResultVo.error("异常: "+e.getMessage()));
        }

    }

    private void computeKeyValue(List<CommunitySettingKeyDto> communitySettingKeyDtos, String communityId, String settingType) {
        if (communitySettingKeyDtos == null || communitySettingKeyDtos.size() < 1) {
            return;
        }


        CommunitySettingDto communitySettingDto = new CommunitySettingDto();
        communitySettingDto.setSettingType(settingType);
        communitySettingDto.setCommunityId(communityId);
        List<CommunitySettingDto> communitySettingDtos = communitySettingInnerServiceSMOImpl.queryCommunitySettings(communitySettingDto);
        if (communitySettingDtos == null || communitySettingDtos.size() < 1) {
            return;
        }

        for (CommunitySettingKeyDto communitySettingKeyDto : communitySettingKeyDtos) {
            for (CommunitySettingDto tmpCommunitySettingDto : communitySettingDtos) {
                if (!communitySettingKeyDto.getSettingKey().equals(tmpCommunitySettingDto.getSettingKey())) {
                    continue;
                }
                communitySettingKeyDto.setSettingValue(tmpCommunitySettingDto.getSettingValue());
            }

        }
    }
    public static String compressString(String str) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            GZIPOutputStream gzip = new GZIPOutputStream(bos);
            gzip.write(str.getBytes());
            gzip.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toString();
    }
}
