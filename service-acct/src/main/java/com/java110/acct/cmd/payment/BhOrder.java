package com.java110.acct.cmd.payment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.java110.acct.cmd.payment.util.GmUtil;
import com.java110.acct.payment.IBhPayAdapt;
import com.java110.acct.payment.IPaymentBusiness;
import com.java110.acct.payment.IPaymentFactoryAdapt;
import com.java110.acct.payment.adapt.bbgpay.lib.JsonUtil;
import com.java110.core.context.CmdDataFlow;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.community.CommunitySettingDto;
import com.java110.dto.community.CommunitySettingKeyDto;
import com.java110.dto.fee.PayFeeDto;
import com.java110.dto.payment.PaymentOrderDto;
import com.java110.dto.payment.PaymentPoolConfigDto;
import com.java110.dto.payment.PaymentPoolDto;
import com.java110.dto.payment.PaymentPoolValueDto;
import com.java110.intf.acct.IPaymentPoolConfigV1InnerServiceSMO;
import com.java110.intf.acct.IPaymentPoolV1InnerServiceSMO;
import com.java110.intf.acct.IPaymentPoolValueV1InnerServiceSMO;
import com.java110.intf.community.ICommunitySettingInnerServiceSMO;
import com.java110.intf.community.ICommunitySettingKeyV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeV1InnerServiceSMO;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 获取加密
 *
 */
@Controller
@PropertySource("classpath:yhUrl/ybh.yml")
@RequestMapping("/BhOrder")
public class BhOrder {

    protected static final String DEFAULT_PAYMENT_ADAPT = "wechatPaymentFactory";// 默认微信通用支付


    @Autowired
    private ICommunitySettingKeyV1InnerServiceSMO communitySettingKeyV1InnerServiceSMOImpl;

    @Autowired
    private IBhPayAdapt iBhPayAdapt;

    @Autowired
    private ICommunitySettingInnerServiceSMO communitySettingInnerServiceSMOImpl;

    private static final Logger logger = LoggerFactory.getLogger(BhOrder.class);

    @Autowired
    private IPaymentPoolValueV1InnerServiceSMO paymentPoolValueV1InnerServiceSMOImpl;
    @Autowired
    private IPaymentPoolConfigV1InnerServiceSMO paymentPoolConfigV1InnerServiceSMOImpl;

    @Autowired
    private IPaymentPoolV1InnerServiceSMO paymentPoolV1InnerServiceSMOImpl;


    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 结单接口
     * @return
     */
    @PostMapping("/bhOrderCompleted")
    @ResponseBody
    public String bhOrderCompleted(@RequestBody String returnResult){
        try {
            String mchtNo_SM4 = null;
            String productNo_SM4 = null;
            String publicKey_SM4 = null;
            String privateKey_SM4 = null;
            //接收交易报文
            if (returnResult == null) {
                System.err.println("通道响应异常");
                return "通道响应异常";
            } else {
                System.out.println("returnResult================"+returnResult);
                Map<String, Object> responseParams = JsonUtil.jsonToMap(returnResult);
                String messageKey = (String) responseParams.get("message_key");
                String redisKey = (String) responseParams.get("mcht_no");
                String communityId = "null";
                if(redisTemplate.hasKey(redisKey)){
                    communityId = (String)redisTemplate.opsForValue().get(redisKey);
                    redisTemplate.delete(redisKey);
                }else {
                    return "当前已完成支付处理";
                }
//                CommunitySettingKeyDto dto = new CommunitySettingKeyDto();
//                dto.setCommunityId((String) responseParams.get("communityId"));
//                dto.setSettingType("9070");

                PaymentPoolDto paymentPoolDto = new PaymentPoolDto();
                System.out.println("读到的communityId================"+communityId);
                paymentPoolDto.setCommunityId(communityId);
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
                String secretKey = GmUtil.decryptSm2(messageKey, privateKey_SM4);
                if (!responseParams.containsKey("enc_data")) {
                    System.err.println("交易失败-->" + responseParams.get("return_code") + ":" + responseParams.get("return_message"));
                    return "交易失败-->" + responseParams.get("return_code") + ":" + responseParams.get("return_message");
                } else {
                    String decryptStr = (String)responseParams.get("enc_data");
                    String decrypt = GmUtil.decryptSm4(decryptStr, secretKey);
                    if (decrypt == null) {
                        System.err.println("解密失败");
                        return "解密失败";
                    } else {
                        System.out.println("解密完成");
                        System.out.println("付款完成的===========支付结果返回值(解密后)============:" + decrypt);
                        Map<String,Object> map = JsonUtil.jsonToMap(decrypt);
                        String tranNo = (String) map.get("tran_no");
                        Map<String,Object> reqJson;
                        if(redisTemplate.hasKey(tranNo)){
                            reqJson= (Map<String, Object>) redisTemplate.opsForValue().get(tranNo);
                            reqJson.put("decrypt",map);
                            System.out.println("==============================================================");
                            System.out.println("reqJson============="+JsonUtil.mapToJson(reqJson));
                            System.out.println("==============================================================");
                        }else {
                            System.out.println("==============================================================本条数据已处理");
                            return "本条数据已处理";
                        }
                        if (reqJson != null && reqJson.isEmpty()) {
                            System.out.println("=========================reqJson是空的=============================");
                            return "reqJson是空的";
                        }
                        System.out.println("================================================================================");

                        System.out.println("================================================================================");
//                        CmdDataFlow context = new CmdDataFlow();
//                        CmdDataFlow context = (CmdDataFlow) redisTemplate.opsForValue().get("data"+tranNo);
                        String jsonFromRedis = (String) redisTemplate.opsForValue().get("data"+tranNo);
                        CmdDataFlow context = JSONObject.parseObject(jsonFromRedis, CmdDataFlow.class);
//                        System.out.println("context========================"+context.toString());

//                        System.out.println("reqJson.get(context)==========="+context);
                        System.out.println(context.getResponseEntity());
                        System.out.println(context.getReqHeaders());
                        System.out.println(context.getReqData());
                        System.out.println(context.getResHeaders());
//                        context.setReqHeaders((Map<String, String>) contextMap.get("reqHeaders"));
//                        context.setResHeaders((Map<String, String>) contextMap.get("resHeaders"));
//                        context.setServiceCode((String) contextMap.get("serviceCode"));
                        String json = JSON.toJSONString(reqJson);//map转String
                        JSONObject jsonObject = JSON.parseObject(json);//String转json
                        System.out.println("================================================================================");
                        System.out.println("从redis中读取到的支付参数报文============{}:" + jsonObject);
                        System.out.println("================================================================================");
                        String userId = jsonObject.getString("userId");
//                        String appId = jsonObject.getString("appId");
                        System.out.println("================================================================================");
//                        System.out.println("context:"+context);
                        System.out.println("================================================================================");



                        //1.0 查询当前支付的业务

                        IPaymentBusiness paymentBusiness = ApplicationContextFactory.getBean(jsonObject.getString("business"), IPaymentBusiness.class);

                        if (paymentBusiness == null) {
                            throw new CmdException("当前支付业务不支持");
                        }

                        //2.0 相应业务 下单 返回 单号 ，金额，

                        PaymentOrderDto paymentOrderDto = paymentBusiness.unified(context, jsonObject);
                        paymentOrderDto.setAppId(jsonObject.getString("appId"));
                        paymentOrderDto.setUserId(userId);
                        String aa = paymentOrderDto.toString();
                        System.out.println("================================================================================");
                        System.out.println("paymentOrderDto===========================" +
                                "" +
                                "====================================================="+aa);
                        System.out.println("================================================================================");

                        logger.debug(">>>>>>>>>>>>>>>>支付业务下单返回,{}", JSONObject.toJSONString(paymentOrderDto));

                        // 3.0 如果支付金额为0 直接调用 支付完通知接口
                        if (paymentOrderDto.getMoney() <= 0) {
                            paymentBusiness.notifyPayment(paymentOrderDto, jsonObject);
                            JSONObject param = new JSONObject();
                            param.put("code", "100");
                            param.put("msg", "扣费为0回调成功");
                        }

                        // todo 3.0 寻找当前支付适配器
                        String payAdapt = computeAdapt(jsonObject.getString("business"), jsonObject);
                        payAdapt = StringUtil.isEmpty(payAdapt) ? DEFAULT_PAYMENT_ADAPT : payAdapt;

                        if (jsonObject.containsKey("payAdapt") && !StringUtil.isEmpty(jsonObject.getString("payAdapt"))) {
                            payAdapt = jsonObject.getString("payAdapt");
                        }


                        // 4.0 相应支付厂家下单
                        Map result = null;
                        try {
                            result = iBhPayAdapt.paymentClose(paymentOrderDto, jsonObject);
//                            paymentBusiness.notifyPayment(paymentOrderDto,jsonObject);
                        } catch (Exception e) {
                            logger.error("支付异常", e);
                            throw new CmdException(e.getLocalizedMessage());
                        }
                        ResponseEntity<String> responseEntity = new ResponseEntity(JSONObject.toJSONString(result), HttpStatus.OK);

                        logger.debug("调用支付厂家返回,{}", responseEntity);

                        // redis 中 保存 请求参数
                        CommonCache.setValue("unifiedPayment_" + paymentOrderDto.getOrderId(), jsonObject.toJSONString(), CommonCache.PAY_DEFAULT_EXPIRE_TIME);
                        if(Boolean.TRUE.equals(redisTemplate.hasKey(tranNo))){
                            System.out.println("处理完数据========="+tranNo+"后执行redis删除key==================");
                            redisTemplate.delete(tranNo);
                            redisTemplate.delete(redisKey);
                        }
                        return decrypt;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @PostMapping("/testdata")
    @ResponseBody
    public String testdata(@RequestBody String returnResult){
        try{
            Map<String,Object> map = JsonUtil.jsonToMap(returnResult);
            String json = JSON.toJSONString(map);//map转String
            JSONObject jsonObject = JSON.parseObject(json);//String转json
            String userId = jsonObject.getString("cashierUserId");
            CmdDataFlow context = new CmdDataFlow();
            Map<String,Object>contextMap = (Map<String, Object>) map.get("context");
            System.out.println("================================================================================");

            System.out.println("================================================================================");

            context.setReqHeaders((Map<String, String>) contextMap.get("reqHeaders"));
            context.setResHeaders((Map<String, String>) contextMap.get("resHeaders"));
            context.setServiceCode((String) contextMap.get("serviceCode"));
            //1.0 查询当前支付的业务

            IPaymentBusiness paymentBusiness = ApplicationContextFactory.getBean(jsonObject.getString("business"), IPaymentBusiness.class);

            if (paymentBusiness == null) {
                throw new CmdException("当前支付业务不支持");
            }

            //2.0 相应业务 下单 返回 单号 ，金额，
            PaymentOrderDto paymentOrderDto = paymentBusiness.unified(context, jsonObject);
            paymentOrderDto.setAppId(jsonObject.getString("appId"));
            paymentOrderDto.setUserId(userId);

            String aa = paymentOrderDto.toString();
            System.out.println("================================================================================");
            System.out.println("paymentOrderDto================================================================================"+aa);
            System.out.println("================================================================================");


            logger.debug(">>>>>>>>>>>>>>>>支付业务下单返回,{}", JSONObject.toJSONString(paymentOrderDto));

            // 3.0 如果支付金额为0 直接调用 支付完通知接口
            if (paymentOrderDto.getMoney() <= 0) {
                paymentBusiness.notifyPayment(paymentOrderDto, jsonObject);
                JSONObject param = new JSONObject();
                param.put("code", "100");
                param.put("msg", "扣费为0回调成功");
            }

            // todo 3.0 寻找当前支付适配器
            String payAdapt = computeAdapt(jsonObject.getString("business"), jsonObject);
            payAdapt = StringUtil.isEmpty(payAdapt) ? DEFAULT_PAYMENT_ADAPT : payAdapt;

            if (jsonObject.containsKey("payAdapt") && !StringUtil.isEmpty(jsonObject.getString("payAdapt"))) {
                payAdapt = jsonObject.getString("payAdapt");
            }

            IPaymentFactoryAdapt tPayAdapt = ApplicationContextFactory.getBean(payAdapt, IPaymentFactoryAdapt.class);


            // 4.0 相应支付厂家下单
            Map result = null;
            try {
                result = iBhPayAdapt.paymentClose(paymentOrderDto, jsonObject);

            } catch (Exception e) {
                logger.error("支付异常", e);
                throw new CmdException(e.getLocalizedMessage());
            }
            ResponseEntity<String> responseEntity = new ResponseEntity(JSONObject.toJSONString(result), HttpStatus.OK);

            logger.debug("调用支付厂家返回,{}", responseEntity);

            // redis 中 保存 请求参数
            CommonCache.setValue("unifiedPayment_" + paymentOrderDto.getOrderId(), jsonObject.toJSONString(), CommonCache.PAY_DEFAULT_EXPIRE_TIME);

            return returnResult;
        }catch (Exception e){
            e.printStackTrace();
            return e.getLocalizedMessage();
        }

    }

    /**
     * 计算适配器
     *
     * @param business
     * @param reqJson
     * @return
     */
    private String computeAdapt(String business, JSONObject reqJson) {

        String communityId = reqJson.getString("communityId");
        //todo 如果是单个费用缴费
        PaymentPoolDto paymentPoolDto = ifPayFeeBusiness(business, reqJson);
        if (paymentPoolDto != null) {
            reqJson.put("paymentPoolId", paymentPoolDto.getPpId());
            return paymentPoolDto.getBeanJsapi();
        }

        //todo 如果是临时车
        paymentPoolDto = ifTempCarFeeBusiness(business, communityId);
        if (paymentPoolDto != null) {
            reqJson.put("paymentPoolId", paymentPoolDto.getPpId());
            return paymentPoolDto.getBeanJsapi();
        }

        //todo 按小区查询 支付信息
        paymentPoolDto = new PaymentPoolDto();
        paymentPoolDto.setCommunityId(communityId);
        paymentPoolDto.setPayType(PaymentPoolDto.PAY_TYPE_COMMUNITY);
        paymentPoolDto.setState("Y");
        List<PaymentPoolDto> paymentPoolDtos = paymentPoolV1InnerServiceSMOImpl.queryPaymentPools(paymentPoolDto);
        if (paymentPoolDtos == null || paymentPoolDtos.isEmpty()) {
            throw new IllegalArgumentException("小区未配置支付信息");
        }

        reqJson.put("paymentPoolId", paymentPoolDtos.get(0).getPpId());
        return paymentPoolDtos.get(0).getBeanJsapi();
    }

    /**
     * 临时车场景处理
     *
     * @param business
     * @param communityId
     * @return
     */
    private PaymentPoolDto ifTempCarFeeBusiness(String business, String communityId) {
        if (!"tempCarFee".equals(business)) {
            return null;
        }
        //todo 按小区查询 支付信息
        PaymentPoolDto paymentPoolDto = new PaymentPoolDto();
        paymentPoolDto.setCommunityId(communityId);
        paymentPoolDto.setPayType(PaymentPoolDto.PAY_TYPE_TEMP_CAT);
        paymentPoolDto.setState("Y");
        List<PaymentPoolDto> paymentPoolDtos = paymentPoolV1InnerServiceSMOImpl.queryPaymentPools(paymentPoolDto);
        if (paymentPoolDtos == null || paymentPoolDtos.isEmpty()) {
            return null;
        }

        return paymentPoolDtos.get(0);
    }

    private PaymentPoolDto ifPayFeeBusiness(String business, JSONObject reqJson) {
        String feeId = "";
        if (!"payFee".equals(business) || !reqJson.containsKey("feeId")) {
            return null;
        }

        feeId = reqJson.getString("feeId");
        if (StringUtil.isNumber(feeId)) {
            return null;
        }

        PayFeeDto feeDto = new PayFeeDto();
        feeDto.setFeeId(feeId);
        feeDto.setCommunityId(reqJson.getString("communityId"));
        List<PayFeeDto> feeDtos = payFeeV1InnerServiceSMOImpl.queryPayFees(feeDto);

        if (feeDtos == null || feeDtos.isEmpty()) {
            return null;
        }

        PaymentPoolConfigDto paymentPoolConfigDto = new PaymentPoolConfigDto();
        paymentPoolConfigDto.setConfigId(feeDtos.get(0).getConfigId());
        paymentPoolConfigDto.setCommunityId(feeDtos.get(0).getCommunityId());
        List<PaymentPoolConfigDto> paymentPoolConfigDtos = paymentPoolConfigV1InnerServiceSMOImpl.queryPaymentPoolConfigs(paymentPoolConfigDto);
        if (paymentPoolConfigDtos == null || paymentPoolConfigDtos.isEmpty()) {
            return null;
        }

        PaymentPoolDto paymentPoolDto = new PaymentPoolDto();
        paymentPoolDto.setPpId(paymentPoolConfigDtos.get(0).getPpId());
        paymentPoolDto.setCommunityId(paymentPoolConfigDtos.get(0).getCommunityId());
        paymentPoolDto.setPayType(PaymentPoolDto.PAY_TYPE_FEE_CONFIG);
        paymentPoolDto.setState("Y");
        List<PaymentPoolDto> paymentPoolDtos = paymentPoolV1InnerServiceSMOImpl.queryPaymentPools(paymentPoolDto);
        if (paymentPoolDtos == null || paymentPoolDtos.isEmpty()) {
            return null;
        }

        return paymentPoolDtos.get(0);
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
}
