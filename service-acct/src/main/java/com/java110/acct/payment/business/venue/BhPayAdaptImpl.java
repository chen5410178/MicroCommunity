package com.java110.acct.payment.business.venue;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.payment.IBhPayAdapt;
import com.java110.acct.payment.IPaymentBusiness;
import com.java110.core.factory.CallApiServiceFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.payment.PaymentOrderDto;
import com.java110.dto.payment.PaymentPoolValueDto;
import com.java110.dto.wechat.OnlinePayDto;
import com.java110.dto.wechat.SmallWeChatDto;
import com.java110.intf.acct.IOnlinePayV1InnerServiceSMO;
import com.java110.intf.acct.IPaymentPoolValueV1InnerServiceSMO;
import com.java110.intf.store.ISmallWechatV1InnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserInnerServiceSMO;
import com.java110.po.wechat.OnlinePayPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.MoneyUtil;
import com.java110.utils.util.PayUtil;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("BhPayAdaptImpl")
public class BhPayAdaptImpl implements IBhPayAdapt {

    @Autowired
    private ISmallWechatV1InnerServiceSMO smallWechatV1InnerServiceSMOImpl;


    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;


    @Autowired
    private IOnlinePayV1InnerServiceSMO onlinePayV1InnerServiceSMOImpl;

    @Autowired
    protected SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    private RestTemplate outRestTemplate;

    @Autowired
    private IPaymentPoolValueV1InnerServiceSMO paymentPoolValueV1InnerServiceSMOImpl;
    @Override
    public Map paymentClose(PaymentOrderDto paymentOrderDto, JSONObject reqJson) throws Exception {


        System.out.println("paymentOrderDto ========================"+paymentOrderDto.toString());
        SmallWeChatDto smallWeChatDto = getSmallWechat(reqJson);
        String paymentPoolId = reqJson.getString("paymentPoolId");
        ;

//        String appId = reqJson.getString("appId");
//        String userId = reqJson.getString("userId");
//        String tradeType = reqJson.getString("tradeType");

//        Map<String,Object> reqJsonMap = JsonUtil.jsonToMap(String.valueOf(reqJson));
        Map<String,Object>decryptMap = (Map<String, Object>) reqJson.get("decrypt");
        String orderNum = "1";
        PaymentPoolValueDto paymentPoolValueDto = new PaymentPoolValueDto();
        paymentPoolValueDto.setPpId(paymentPoolId);
        List<PaymentPoolValueDto> paymentPoolValueDtos = paymentPoolValueV1InnerServiceSMOImpl.queryPaymentPoolValues(paymentPoolValueDto);
//        JSONObject paramOut = JSONObject.parseObject(decryParams);
        String outTradeNo = decryptMap.get("tran_no").toString();
        String openId = paymentOrderDto.getOpenId();
        String oderId = paymentOrderDto.getOrderId();
        paymentOrderDto.setOrderId(oderId);
        paymentOrderDto.setTransactionId(decryptMap.get("txn_no").toString());
        double payAmount = PayUtil.getPayAmountByEnv(MappingCache.getValue(MappingConstant.ENV_DOMAIN, "HC_ENV"), paymentOrderDto.getMoney());



//        doUpdateOnlinePay(outTradeNo, OnlinePayDto.STATE_COMPILE, "支付成功");

        JSONObject resJson = new JSONObject();
        resJson.put("return_code", "SUCCESS");
        resJson.put("return message", "成功");
        paymentOrderDto.setResponseEntity(new ResponseEntity<String>(resJson.toJSONString(), HttpStatus.OK));


        IPaymentBusiness paymentBusiness = ApplicationContextFactory.getBean(reqJson.getString("business"), IPaymentBusiness.class);

        if (paymentBusiness == null) {
            throw new CmdException("当前支付业务不支持");
        }

        paymentOrderDto.setAppId(reqJson.getString("appId"));

        //2.0 相应业务 下单 返回 单号 ，金额，
//        paymentBusiness.notifyPaymentBh(paymentOrderDto, reqJson);
        Map<String,Object>info = new HashMap<>();
        List<Map> businessAppInfos = sqlSessionTemplate.selectList("appServiceDaoImpl.getAppInfo",info);
        if (businessAppInfos.size()>0){
            businessAppInfos.forEach(e->{
                if(e.get("name").equals("微信支付")){
                    paymentOrderDto.setAppId((String) e.get("appId"));
                }
            });
        }
        paymentOrderDto.setAppId("992020011134400001");
        paymentBusiness.notifyPayment(paymentOrderDto, reqJson);
        doSaveOnlinePay(smallWeChatDto,openId,oderId, paymentOrderDto.getName(),
                payAmount, OnlinePayDto.STATE_COMPILE, "支付成功",paymentPoolValueDtos.get(0).getPpId());

        return null;
    }

    @Override
    public PaymentOrderDto unifiedPayFee(JSONObject reqJson) {

        String appId = reqJson.getString("appId");
        String userId = reqJson.getString("userId");
        JSONObject orderInfo = CallApiServiceFactory.postForApi(appId, reqJson, "fee.payFeePre", JSONObject.class, userId);
        String orderId = orderInfo.getString("oId");
        String feeName = orderInfo.getString("feeName");
        double money = Double.parseDouble(orderInfo.getString("receivedAmount"));

        //这里防止 小数点不是 2位 比如 3位之类的 微信平台不支持
        money = MoneyUtil.computePriceScale(money,"1",2);


        PaymentOrderDto paymentOrderDto = new PaymentOrderDto();
        paymentOrderDto.setOrderId(orderId);
        paymentOrderDto.setMoney(money);
        paymentOrderDto.setName(feeName);
        return paymentOrderDto;
    }

    private SmallWeChatDto getSmallWechat(JSONObject paramIn) {

        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setObjId(paramIn.getString("communityId"));
        smallWeChatDto.setAppId(paramIn.getString("appId"));
        smallWeChatDto.setPage(1);
        smallWeChatDto.setRow(1);
        List<SmallWeChatDto> smallWeChatDtos = smallWechatV1InnerServiceSMOImpl.querySmallWechats(smallWeChatDto);

        if (smallWeChatDtos == null || smallWeChatDtos.size() < 1) {
            smallWeChatDto = new SmallWeChatDto();
            smallWeChatDto.setAppId(MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, "appId"));
            smallWeChatDto.setAppSecret(MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, "appSecret"));
            smallWeChatDto.setObjId(paramIn.getString("communityId"));

            return smallWeChatDto;
        }

        return BeanConvertUtil.covertBean(smallWeChatDtos.get(0), SmallWeChatDto.class);
    }

    private void doUpdateOnlinePay(String orderId, String state, String message) {
        OnlinePayPo onlinePayPo = new OnlinePayPo();
        onlinePayPo.setMessage(message.length() > 1000 ? message.substring(0, 1000) : message);
        onlinePayPo.setOrderId(orderId);
        onlinePayPo.setState(state);
        onlinePayV1InnerServiceSMOImpl.updateOnlinePay(onlinePayPo);
    }

    private void doSaveOnlinePay(SmallWeChatDto smallWeChatDto, String openId, String orderId, String feeName,
                                 double money, String state, String message,
                                 String ppId) {
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
}
