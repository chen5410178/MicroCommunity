package com.java110.acct.cmd.payment;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.payment.IPaymentBusiness;
import com.java110.acct.payment.IPaymentFactoryAdapt;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.fee.PayFeeDto;
import com.java110.dto.payment.PaymentOrderDto;
import com.java110.dto.payment.PaymentPoolConfigDto;
import com.java110.dto.payment.PaymentPoolDto;
import com.java110.intf.acct.IPaymentPoolConfigV1InnerServiceSMO;
import com.java110.intf.acct.IPaymentPoolV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeV1InnerServiceSMO;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 统一支付接口命令类
 * 负责处理支付收银台相关业务逻辑，包括参数验证、支付业务处理、支付适配器选择等
 * 支持多种支付场景：费用支付、临时车支付等，根据业务类型动态选择支付方式
 * 
 * @author Java110
 * @version 1.0
 * @since 2023
 */
@Java110Cmd(serviceCode = "payment.cashierForMall")
public class CashierForMallCmd extends Cmd {

    /**
     * 日志记录器
     */
    private static final Logger logger = LoggerFactory.getLogger(CashierForMallCmd.class);

    /**
     * 默认支付适配器 - 微信通用支付
     */
    protected static final String DEFAULT_PAYMENT_ADAPT = "wechatPaymentFactory";

    /**
     * 支付池配置服务接口
     */
    @Autowired
    private IPaymentPoolConfigV1InnerServiceSMO paymentPoolConfigV1InnerServiceSMOImpl;

    /**
     * 支付池服务接口
     */
    @Autowired
    private IPaymentPoolV1InnerServiceSMO paymentPoolV1InnerServiceSMOImpl;

    /**
     * 费用服务接口
     */
    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    /**
     * 参数验证方法
     * 验证请求参数是否完整，确保必要的参数都存在
     *
     * @param event   命令事件对象
     * @param context 命令数据流上下文，包含请求和响应信息
     * @param reqJson 请求的JSON数据
     * @throws CmdException 当参数验证失败时抛出异常
     */
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        // 验证必要参数是否存在
        Assert.hasKeyAndValue(reqJson, "business", "未包含业务");
        Assert.hasKeyAndValue(reqJson, "mallTel", "未包含商城手机号");
        Assert.hasKeyAndValue(reqJson, "openId", "未包含openId");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");

        String userId = CmdContextUtils.getUserId(context);
        reqJson.put("cashierUserId",userId);

        // 设置用户ID到请求头中
        context.getReqHeaders().put("user-id", reqJson.getString("cashierUserId"));
    }

    /**
     * 执行支付命令的主要业务逻辑
     * 包括支付业务处理、环境判断、支付适配器选择、支付厂家调用等
     *
     * @param event   命令事件对象
     * @param context 命令数据流上下文
     * @param reqJson 请求的JSON数据
     * @throws CmdException    当支付业务处理失败时抛出异常
     * @throws ParseException  当数据解析异常时抛出
     */
    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        // 记录支付参数日志
        logger.debug(">>>>>>>>>>>>>>>>支付参数报文,{}", reqJson.toJSONString());
        
        // 从请求头中获取应用ID和用户ID
        String appId = context.getReqHeaders().get("app-id");
        String userId = reqJson.getString("cashierUserId");

        // 1.0 根据业务类型获取对应的支付业务处理器
        IPaymentBusiness paymentBusiness = ApplicationContextFactory.getBean(reqJson.getString("business"), IPaymentBusiness.class);

        if (paymentBusiness == null) {
            throw new CmdException("当前支付业务不支持");
        }

        // 2.0 调用支付业务处理器的统一下单接口，生成支付订单
        PaymentOrderDto paymentOrderDto = paymentBusiness.unified(context, reqJson);
        paymentOrderDto.setAppId(appId);
        paymentOrderDto.setUserId(userId);

        logger.debug(">>>>>>>>>>>>>>>>支付业务下单返回,{}", JSONObject.toJSONString(paymentOrderDto));

        // 获取当前环境配置
        String env = MappingCache.getValue(MappingConstant.ENV_DOMAIN, "HC_ENV");

        // 演示环境处理：不实际调用支付接口，直接返回模拟结果
        if ("DEV".equals(env) || "TEST".equals(env)) {
            // 调用支付完成通知
            paymentBusiness.notifyPayment(paymentOrderDto, reqJson);
            JSONObject param = new JSONObject();
            param.put("code", "100");
            param.put("msg", "演示环境不触发支付");
            context.setResponseEntity(new ResponseEntity(JSONObject.toJSONString(param), HttpStatus.OK));
            return;
        }

        // 3.0 如果支付金额为0，直接调用支付完成通知接口
        if (paymentOrderDto.getMoney() <= 0) {
            paymentBusiness.notifyPayment(paymentOrderDto, reqJson);
            JSONObject param = new JSONObject();
            param.put("code", "100");
            param.put("msg", "扣费为0回调成功");
            context.setResponseEntity(new ResponseEntity(JSONObject.toJSONString(param), HttpStatus.OK));
            return;
        }

        // 3.1 根据业务类型和请求参数计算支付适配器
        String payAdapt = computeAdapt(reqJson.getString("business"), reqJson);
        // 如果计算出的适配器为空，使用默认适配器
        payAdapt = StringUtil.isEmpty(payAdapt) ? DEFAULT_PAYMENT_ADAPT : payAdapt;

        // 如果请求中指定了支付适配器，优先使用请求中的适配器
        if (reqJson.containsKey("payAdapt") && !StringUtil.isEmpty(reqJson.getString("payAdapt"))) {
            payAdapt = reqJson.getString("payAdapt");
        }

        // 获取支付适配器实例
        IPaymentFactoryAdapt tPayAdapt = ApplicationContextFactory.getBean(payAdapt, IPaymentFactoryAdapt.class);

        // 4.0 调用支付厂家的支付接口
        Map result = null;
        try {
            result = tPayAdapt.java110Payment(paymentOrderDto, reqJson, context);
        } catch (Exception e) {
            logger.error("支付异常", e);
            throw new CmdException(e.getLocalizedMessage());
        }
        
        // 设置响应结果
        ResponseEntity<String> responseEntity = new ResponseEntity(JSONObject.toJSONString(result), HttpStatus.OK);
        logger.debug("调用支付厂家返回,{}", responseEntity);
        context.setResponseEntity(responseEntity);

        // 将请求参数保存到Redis缓存中，用于后续处理
        CommonCache.setValue("unifiedPayment_" + paymentOrderDto.getOrderId(), reqJson.toJSONString(), CommonCache.PAY_DEFAULT_EXPIRE_TIME);
    }

    /**
     * 计算支付适配器
     * 根据业务类型和请求参数动态选择最适合的支付适配器
     *
     * @param business 业务类型
     * @param reqJson  请求参数
     * @return 支付适配器Bean名称
     */
    private String computeAdapt(String business, JSONObject reqJson) {
        String communityId = reqJson.getString("communityId");
        
        // 1. 如果是单个费用缴费业务，查询对应的支付池配置
        PaymentPoolDto paymentPoolDto = ifPayFeeBusiness(business, reqJson);
        if (paymentPoolDto != null) {
            reqJson.put("paymentPoolId", paymentPoolDto.getPpId());
            return paymentPoolDto.getBeanJsapi();
        }

        // 2. 如果是临时车费用业务，查询临时车支付配置
        paymentPoolDto = ifTempCarFeeBusiness(business, communityId);
        if (paymentPoolDto != null) {
            reqJson.put("paymentPoolId", paymentPoolDto.getPpId());
            return paymentPoolDto.getBeanJsapi();
        }

        // 3. 按小区查询默认支付信息
        paymentPoolDto = new PaymentPoolDto();
        paymentPoolDto.setCommunityId(communityId);
        paymentPoolDto.setPayType(PaymentPoolDto.PAY_TYPE_COMMUNITY); // 小区级别支付配置
        paymentPoolDto.setState("Y"); // 只查询启用状态的配置
        List<PaymentPoolDto> paymentPoolDtos = paymentPoolV1InnerServiceSMOImpl.queryPaymentPools(paymentPoolDto);
        
        if (paymentPoolDtos == null || paymentPoolDtos.isEmpty()) {
            throw new IllegalArgumentException("小区未配置支付信息");
        }

        reqJson.put("paymentPoolId", paymentPoolDtos.get(0).getPpId());
        return paymentPoolDtos.get(0).getBeanJsapi();
    }

    /**
     * 处理临时车费用业务场景
     * 检查是否为临时车费用业务，并返回对应的支付池配置
     *
     * @param business    业务类型
     * @param communityId 小区ID
     * @return 支付池配置信息，如果不是临时车业务返回null
     */
    private PaymentPoolDto ifTempCarFeeBusiness(String business, String communityId) {
        // 检查业务类型是否为临时车费用
        if (!"tempCarFee".equals(business)) {
            return null;
        }
        
        // 查询临时车支付配置
        PaymentPoolDto paymentPoolDto = new PaymentPoolDto();
        paymentPoolDto.setCommunityId(communityId);
        paymentPoolDto.setPayType(PaymentPoolDto.PAY_TYPE_TEMP_CAT); // 临时车支付类型
        paymentPoolDto.setState("Y"); // 只查询启用状态的配置
        List<PaymentPoolDto> paymentPoolDtos = paymentPoolV1InnerServiceSMOImpl.queryPaymentPools(paymentPoolDto);
        
        if (paymentPoolDtos == null || paymentPoolDtos.isEmpty()) {
            return null;
        }

        return paymentPoolDtos.get(0);
    }

    /**
     * 处理费用支付业务场景
     * 检查是否为费用支付业务，并根据费用配置查询对应的支付池配置
     *
     * @param business 业务类型
     * @param reqJson  请求参数
     * @return 支付池配置信息，如果不是费用支付业务或未找到配置返回null
     */
    private PaymentPoolDto ifPayFeeBusiness(String business, JSONObject reqJson) {
        String feeId = "";
        
        // 检查业务类型是否为费用支付且包含费用ID
        if (!"payFee".equals(business) || !reqJson.containsKey("feeId")) {
            return null;
        }

        feeId = reqJson.getString("feeId");
        // 检查费用ID是否为数字（可能是其他类型的ID）
        if (StringUtil.isNumber(feeId)) {
            return null;
        }

        // 查询费用信息
        PayFeeDto feeDto = new PayFeeDto();
        feeDto.setFeeId(feeId);
        feeDto.setCommunityId(reqJson.getString("communityId"));
        List<PayFeeDto> feeDtos = payFeeV1InnerServiceSMOImpl.queryPayFees(feeDto);

        if (feeDtos == null || feeDtos.isEmpty()) {
            return null;
        }

        // 根据费用配置ID查询支付池配置
        PaymentPoolConfigDto paymentPoolConfigDto = new PaymentPoolConfigDto();
        paymentPoolConfigDto.setConfigId(feeDtos.get(0).getConfigId());
        paymentPoolConfigDto.setCommunityId(feeDtos.get(0).getCommunityId());
        List<PaymentPoolConfigDto> paymentPoolConfigDtos = paymentPoolConfigV1InnerServiceSMOImpl.queryPaymentPoolConfigs(paymentPoolConfigDto);
        
        if (paymentPoolConfigDtos == null || paymentPoolConfigDtos.isEmpty()) {
            return null;
        }

        // 查询支付池信息
        PaymentPoolDto paymentPoolDto = new PaymentPoolDto();
        paymentPoolDto.setPpId(paymentPoolConfigDtos.get(0).getPpId());
        paymentPoolDto.setCommunityId(paymentPoolConfigDtos.get(0).getCommunityId());
        paymentPoolDto.setPayType(PaymentPoolDto.PAY_TYPE_FEE_CONFIG); // 费用配置级别支付类型
        paymentPoolDto.setState("Y"); // 只查询启用状态的配置
        List<PaymentPoolDto> paymentPoolDtos = paymentPoolV1InnerServiceSMOImpl.queryPaymentPools(paymentPoolDto);
        
        if (paymentPoolDtos == null || paymentPoolDtos.isEmpty()) {
            return null;
        }

        return paymentPoolDtos.get(0);
    }
}