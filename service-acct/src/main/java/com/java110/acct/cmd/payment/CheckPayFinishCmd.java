package com.java110.acct.cmd.payment;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.smo.IQrCodePaymentSMO;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.CallApiServiceFactory;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.payment.PaymentPoolDto;
import com.java110.intf.acct.IPaymentPoolV1InnerServiceSMO;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 扫码付
 */
@Java110Cmd(serviceCode = "payment.checkPayFinish")
public class CheckPayFinishCmd extends Cmd {


    private IQrCodePaymentSMO qrCodePaymentSMOImpl;

    @Autowired
    private IPaymentPoolV1InnerServiceSMO paymentPoolV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "subServiceCode", "未包含支付接口");
        Assert.hasKeyAndValue(reqJson, "paymentPoolId", "未包含支付参数");


    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        String orderId = reqJson.getString("orderId");
        double receivedAmount = Double.parseDouble(reqJson.getString("receivedAmount"));
        String authCode = StringUtil.isEmpty(reqJson.getString("authCode")) ? "" : reqJson.getString("authCode").trim();
        if (!StringUtil.isEmpty(authCode)) {
            reqJson.put("authCode", authCode);
        }
        // String payQrAdapt = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.PAY_QR_ADAPT);
        String payQrAdapt = computeAdapt(reqJson.getString("subServiceCode"),reqJson);//MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.PAY_QR_ADAPT);


        if (StringUtil.isEmpty(payQrAdapt)) {
            if (authCode.length() < 2) {
                throw new IllegalArgumentException("未包含授权码");
            }
            String adaptPrimeRate = computePrimeRateByAuthCode(authCode);
            if (FeeDetailDto.PRIME_REATE_ALI_QRCODE.equals(adaptPrimeRate)) { // 支付宝
                qrCodePaymentSMOImpl = ApplicationContextFactory.getBean("qrCodeAliPaymentAdapt", IQrCodePaymentSMO.class);
            } else {
                qrCodePaymentSMOImpl = ApplicationContextFactory.getBean("qrCodeWechatPaymentAdapt", IQrCodePaymentSMO.class);
            }
        } else {
            qrCodePaymentSMOImpl = ApplicationContextFactory.getBean(payQrAdapt, IQrCodePaymentSMO.class);
        }

        //todo 与扫码付阶段保持一致的支付渠道
        String primeRate = computePrimeRate(orderId, authCode, reqJson.getString("primeRate"));
        if (!StringUtil.isEmpty(primeRate)) {
            reqJson.put("primeRate", primeRate);
        }


        ResultVo resultVo = null;
        try {
            resultVo = qrCodePaymentSMOImpl.checkPayFinish(reqJson.getString("communityId"), orderId,reqJson.getString("paymentPoolId"));
        } catch (Exception e) {
            cmdDataFlowContext.setResponseEntity(ResultVo.error(e.getLocalizedMessage()));
            return;
        }
        if (ResultVo.CODE_OK != resultVo.getCode()) {
            cmdDataFlowContext.setResponseEntity(ResultVo.createResponseEntity(resultVo));
            return;
        }
        String appId = cmdDataFlowContext.getReqHeaders().get(CommonConstant.APP_ID);
        String userId = cmdDataFlowContext.getReqHeaders().get(CommonConstant.USER_ID);
        //JSONObject paramOut = CallApiServiceFactory.postForApi(appId, reqJson, "fee.payFee", JSONObject.class, userId);
        reqJson.put("payOrderId",orderId);

        orderId = CommonCache.getAndRemoveValue("qrCode_order"+orderId);

        if(StringUtil.isEmpty(orderId)){
            throw new CmdException("订单已经处理过");
        }

        JSONObject paramOut = CallApiServiceFactory.postForApi(appId, reqJson, reqJson.getString("subServiceCode"), JSONObject.class, userId);
        cmdDataFlowContext.setResponseEntity(ResultVo.createResponseEntity(paramOut));
    }

    /**
     * 计算支付渠道，优先取扫码付阶段缓存，其次按授权码前缀推断，最后沿用请求传入值
     *
     * @param orderId      订单ID
     * @param authCode     授权码
     * @param reqPrimeRate 请求传入的支付渠道
     * @return 支付渠道
     */
    private String computePrimeRate(String orderId, String authCode, String reqPrimeRate) {
        String primeRate = CommonCache.getValue("qrCode_primeRate" + orderId);
        if (!StringUtil.isEmpty(primeRate)) {
            return primeRate;
        }
        //todo 只有微信、支付宝扫码渠道才按授权码前缀推断，其他渠道沿用请求传入值
        if (StringUtil.isEmpty(reqPrimeRate)
                || FeeDetailDto.PRIME_REATE_WECHAT_QRCODE.equals(reqPrimeRate)
                || FeeDetailDto.PRIME_REATE_ALI_QRCODE.equals(reqPrimeRate)) {
            String authCodePrimeRate = computePrimeRateByAuthCode(authCode);
            if (!StringUtil.isEmpty(authCodePrimeRate)) {
                return authCodePrimeRate;
            }
        }
        return reqPrimeRate;
    }

    /**
     * 根据授权码前缀计算支付渠道，前两位 25~30 为支付宝，其余为微信，无法识别时返回空
     *
     * @param authCode 授权码
     * @return 支付渠道
     */
    private String computePrimeRateByAuthCode(String authCode) {
        if (StringUtil.isEmpty(authCode) || authCode.length() < 2) {
            return "";
        }
        String preStr = authCode.substring(0, 2);
        if (!StringUtil.isNumber(preStr)) {
            return "";
        }
        int pre = Integer.parseInt(preStr);
        if (pre > 24 && pre < 31) { // 支付宝
            return FeeDetailDto.PRIME_REATE_ALI_QRCODE;
        }
        return FeeDetailDto.PRIME_REATE_WECHAT_QRCODE;
    }


    /**
     * 计算适配器
     *
     * @param reqJson
     * @return
     */
    private String computeAdapt(String business, JSONObject reqJson) {

        String communityId = reqJson.getString("communityId");


        //todo 按小区查询 支付信息
        PaymentPoolDto paymentPoolDto = new PaymentPoolDto();
        paymentPoolDto.setCommunityId(communityId);
        paymentPoolDto.setPayType(PaymentPoolDto.PAY_TYPE_COMMUNITY);
        paymentPoolDto.setPpId(reqJson.getString("paymentPoolId"));
        paymentPoolDto.setState("Y");
        List<PaymentPoolDto> paymentPoolDtos = paymentPoolV1InnerServiceSMOImpl.queryPaymentPools(paymentPoolDto);
        if (paymentPoolDtos == null || paymentPoolDtos.isEmpty()) {
            throw new IllegalArgumentException("小区未配置支付信息");
        }

        reqJson.put("paymentPoolId", paymentPoolDtos.get(0).getPpId());
        return paymentPoolDtos.get(0).getBeanQrcode();
    }


}
