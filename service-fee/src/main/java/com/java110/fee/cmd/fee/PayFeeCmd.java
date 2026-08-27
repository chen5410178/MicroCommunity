package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.fee.*;
import com.java110.dto.user.UserDto;
import com.java110.fee.bill.IDeductionPrePayFee;
import com.java110.fee.bmo.fee.IFinishFeeNotify;
import com.java110.fee.bmo.fee.IGeneratorPayFeeOrder;
import com.java110.fee.bmo.fee.IPayPrePayFeeBmo;
import com.java110.fee.bmo.receipt.IFeeReceiptBmo;
import com.java110.fee.smo.impl.FeeReceiptInnerServiceSMOImpl;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.intf.community.*;
import com.java110.intf.fee.*;
import com.java110.intf.fee.IFeeAccountDetailServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.room.ApplyRoomDiscountPo;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.payFee.PayFeeDetailDiscountPo;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.lock.DistributedLock;
import com.java110.utils.util.*;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 前台 现金或者转账收费 缴费处理类
 * <p>
 * 假如 缴费 后要处理一些逻辑建议用databus
 * 这个类已经很复杂 ，最好不要加新逻辑
 */
@Java110Cmd(serviceCode = "fee.payFee")
public class PayFeeCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(PayFeeCmd.class);

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeDetailV1InnerServiceSMO payFeeDetailNewV1InnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMO;

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMO;

    @Autowired
    private IApplyRoomDiscountInnerServiceSMO applyRoomDiscountInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    @Autowired
    private IPayFeeDetailDiscountNewV1InnerServiceSMO payFeeDetailDiscountNewV1InnerServiceSMOImpl;

    @Autowired
    private IRepairPoolV1InnerServiceSMO repairPoolNewV1InnerServiceSMOImpl;

    @Autowired
    private IRepairUserV1InnerServiceSMO repairUserNewV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerCarNewV1InnerServiceSMO ownerCarNewV1InnerServiceSMOImpl;

    @Autowired
    private IFeeAccountDetailServiceSMO feeAccountDetailServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private FeeReceiptInnerServiceSMOImpl feeReceiptInnerServiceSMOImpl;

    @Autowired
    private IFeeDiscountInnerServiceSMO feeDiscountInnerServiceSMOImpl;

    @Autowired
    private IFinishFeeNotify finishFeeNotifyImpl;

    @Autowired
    private IGeneratorPayFeeOrder generatorPayFeeOrderImpl;

    @Autowired
    private IDeductionPrePayFee deductionPrePayFeeImpl;

    @Autowired
    private IFeeReceiptBmo feeReceiptBmoImpl;

    @Autowired
    private IPayPrePayFeeBmo payPrePayFeeBmoImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(reqJson, "cycles", "请求报文中未包含cycles节点");
        Assert.jsonObjectHaveKey(reqJson, "receivedAmount", "请求报文中未包含receivedAmount节点");
        Assert.jsonObjectHaveKey(reqJson, "feeId", "请求报文中未包含feeId节点");
        Assert.hasLength(reqJson.getString("communityId"), "小区ID不能为空");
        Assert.hasLength(reqJson.getString("cycles"), "周期不能为空");
        Assert.hasLength(reqJson.getString("receivedAmount"), "实收金额不能为空");
        Assert.hasLength(reqJson.getString("feeId"), "费用ID不能为空");
        //判断是否 费用状态为缴费结束
        FeeDto feeDto = new FeeDto();
        feeDto.setFeeId(reqJson.getString("feeId"));
        feeDto.setCommunityId(reqJson.getString("communityId"));
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        Assert.listOnlyOne(feeDtos, "传入费用ID错误");

        feeDto = feeDtos.get(0);

        if (FeeDto.STATE_FINISH.equals(feeDto.getState())) {
            throw new CmdException("收费已经结束，不能再缴费");
        }

        Date endTime = feeDto.getEndTime();

        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setConfigId(feeDto.getConfigId());
        feeConfigDto.setCommunityId(reqJson.getString("communityId"));
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        if (ListUtil.isNull(feeConfigDtos)) {
            throw new CmdException("费用项不存在");
        }


        JSONArray selectPreFees = reqJson.getJSONArray("selectPreFees");
        for (int paramIndex = 0; paramIndex < selectPreFees.size(); paramIndex++) {
            JSONObject param = selectPreFees.getJSONObject(paramIndex);
            String maximumNumber = param.getString("maximumNumber");
        }


        //todo 校验 优惠
        JSONArray selectDiscounts = reqJson.getJSONArray("selectDiscount");
        if (!ListUtil.isNull(selectDiscounts)) {
            for (int discountIndex = 0; discountIndex < selectDiscounts.size(); discountIndex++) {
                JSONObject param = selectDiscounts.getJSONObject(discountIndex);
                Assert.hasKeyAndValue(param, "discountId", "未包含优惠ID");
                Assert.hasKeyAndValue(param, "discountPrice", "未包含优惠金额");
            }
        }
    }


    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject paramObj) throws CmdException {

        logger.debug("paramObj : {}", paramObj);
        String extPayOrderId = paramObj.getString("payOrderId");
        String primeFile = paramObj.getString("primeFile");

        String userId = CmdContextUtils.getUserId(cmdDataFlowContext);
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        Assert.listOnlyOne(userDtos, "用户未登录");

        FeeDto feeDto = new FeeDto();
        feeDto.setFeeId(paramObj.getString("feeId"));
        feeDto.setCommunityId(paramObj.getString("communityId"));
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
        if (ListUtil.isNull(feeDtos)) {
            throw new CmdException("查询费用信息失败，未查到数据或查到多条数据");
        }
        FeeDto feeInfo = feeDtos.get(0);

        //todo 生成收据编号
        String receiptCode = feeReceiptInnerServiceSMOImpl.generatorReceiptCode(paramObj.getString("communityId"));

        String payOrderId = GenerateCodeFactory.getGeneratorId("11");
        PayFeePo payFeePo = null;
        String requestId = DistributedLock.getLockUUID();
        String key = this.getClass().getSimpleName() + paramObj.get("feeId");
        try {
            DistributedLock.waitGetDistributedLock(key, requestId);
            //todo 封装 缴费记录报文

            PayFeeDetailPo payFeeDetailPo = BeanConvertUtil.covertBean(paramObj, PayFeeDetailPo.class);
            payFeeDetailPo.setReceivableAmount(feeInfo.getReceivableAmount());
            payFeeDetailPo.setPayableAmount(paramObj.getString("feeReceivedAmount"));
            payFeeDetailPo.setReceivedAmount(paramObj.getString("feeReceivedAmount"));
            payFeeDetailPo.setState(FeeDetailDto.STATE_NORMAL);
            payFeeDetailPo.setStartTime(DateUtil.getFormatTimeStringA(feeDtos.get(0).getStartTime()));
            payFeeDetailPo.setEndTime(DateUtil.getFormatTimeStringA(feeDtos.get(0).getEndTime()));
            payFeeDetailPo.setFeeTime(DateUtil.getFormatTimeStringB(feeInfo.getFeeTime()));

            payFeeDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
            //todo 缓存收据编号
            CommonCache.setValue(payFeeDetailPo.getDetailId() + CommonCache.RECEIPT_CODE, receiptCode, CommonCache.DEFAULT_EXPIRETIME_TWO_MIN);


            //todo 判断是否有折扣规则
            hasDiscount(paramObj, payFeePo, payFeeDetailPo, feeInfo);

            // todo 从预付款扣
            dealUserPreFee(paramObj, payFeeDetailPo);

            // todo 如果 扫码枪支付 输入支付订单ID
            payFeeDetailPo.setPayOrderId(payOrderId);
            payFeeDetailPo.setCashierId(userDtos.get(0).getUserId());
            payFeeDetailPo.setCashierName(userDtos.get(0).getName());
            payFeeDetailPo.setOpenInvoice("N");

            if (!StringUtil.isEmpty(paramObj.getString("cashAmount"))) {
                BigDecimal cashAmount = new BigDecimal(paramObj.getString("cashAmount")).setScale(2, BigDecimal.ROUND_HALF_UP);
                if (!StringUtil.isEmpty(payFeeDetailPo.getRemark())) {
                    payFeeDetailPo.setRemark(payFeeDetailPo.getRemark() + "，现金账户抵扣" + cashAmount + "元");
                } else {
                    payFeeDetailPo.setRemark("现金账户抵扣" + cashAmount + "元");
                }
            }
            int flag = payFeeDetailNewV1InnerServiceSMOImpl.savePayFeeDetailNew(payFeeDetailPo);
            if (flag < 1) {
                throw new CmdException("缴费失败");
            }

            payFeePo = new PayFeePo();
            payFeePo.setFeeId(payFeeDetailPo.getFeeId());
            payFeePo.setState(FeeDto.STATE_FINISH);
            payFeePo.setOweAmount("0");
            BigDecimal receivableAmount = new BigDecimal(payFeeDetailPo.getReceivableAmount());
            BigDecimal receivedAmount = new BigDecimal(payFeeDetailPo.getReceivedAmount());
            BigDecimal acctAmount = new BigDecimal(payFeeDetailPo.getAcctAmount());
            BigDecimal discountAmount = new BigDecimal(payFeeDetailPo.getDiscountAmount());
            BigDecimal deductionAmount = new BigDecimal(payFeeDetailPo.getDeductionAmount());
            BigDecimal giftAmount = new BigDecimal(payFeeDetailPo.getGiftAmount());

            BigDecimal oweAmount = receivableAmount.subtract(receivedAmount)
                    .subtract(acctAmount)
                    .subtract(discountAmount)
                    .subtract(deductionAmount)
                    .subtract(giftAmount);

            if (oweAmount.doubleValue() > 0) {
                payFeePo.setState(FeeDto.STATE_DOING);
                payFeePo.setOweAmount(oweAmount.doubleValue() + "");
            }

            flag = payFeeV1InnerServiceSMOImpl.updatePayFee(payFeePo);
            if (flag < 1) {
                throw new CmdException("缴费失败");
            }
            generatorPayFeeOrderImpl.generatorPayFeeOrder(payFeeDetailPo, feeInfo, extPayOrderId, primeFile);

            //todo 账户处理
            dealPreFee(paramObj, payFeeDetailPo, payFeePo);

            // 生成收据
            feeReceiptBmoImpl.savePayFeeReceipt(payFeeDetailPo, receiptCode);
            // 如果有预付款，保存预付款
            if (paramObj.containsKey("rules")) {
                payPrePayFeeBmoImpl.payPreFee(paramObj.getJSONArray("rules"),
                        receiptCode, feeInfo,
                        paramObj.getString("primeRate"),
                        userDtos.get(0),
                        paramObj.getString("remark"));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            DistributedLock.releaseDistributedLock(key, requestId);
        }


        //todo 为停车费单独处理
        finishFeeNotifyImpl.updateParkingSpaceEndTime(paramObj.getString("feeId"), paramObj.getString("communityId"));

        //todo 修改报修单
        finishFeeNotifyImpl.updateRepair(paramObj.getString("feeId"), paramObj.getString("communityId"), paramObj.getString("receivedAmount"));

        //todo 租金延期房屋结束时间
        finishFeeNotifyImpl.updateRoomEndTime(paramObj.getString("feeId"), paramObj.getString("communityId"));

        //修改折扣申请状态，空置房折扣只能用一次
        String selectDiscount = paramObj.getString("selectDiscount");
        JSONArray params = JSONArray.parseArray(selectDiscount);
        for (int index = 0; index < params.size(); index++) {
            JSONObject param = params.getJSONObject(index);
            if (!StringUtil.isEmpty(param.getString("ardId"))) {
                ApplyRoomDiscountPo applyRoomDiscountPo = new ApplyRoomDiscountPo();
                //空置房优惠不可用
                applyRoomDiscountPo.setInUse("1");
                applyRoomDiscountPo.setArdId(param.getString("ardId"));
                applyRoomDiscountInnerServiceSMOImpl.updateApplyRoomDiscount(applyRoomDiscountPo);
            }
        }


        //根据明细ID 查询收据信息
        FeeReceiptDetailDto feeReceiptDetailDto = new FeeReceiptDetailDto();
        feeReceiptDetailDto.setReceiptCode(receiptCode);

        cmdDataFlowContext.setResponseEntity(ResultVo.createResponseEntity(feeReceiptDetailDto));
    }


    private void dealUserPreFee(JSONObject paramObj, PayFeeDetailPo payFeeDetailPo) {
        //判断选择的账号
        JSONArray jsonArray = paramObj.getJSONArray("selectPreFees");
        if (ListUtil.isNull(jsonArray)) {
            return;
        }

        JSONObject preFee = jsonArray.getJSONObject(0);
        //账户金额
        BigDecimal remainAmount = new BigDecimal(preFee.getString("remainAmount"));
        //实收金额
        BigDecimal receivedAmount = new BigDecimal(payFeeDetailPo.getReceivableAmount());
        int flag = remainAmount.compareTo(receivedAmount);
        BigDecimal redepositAmount = new BigDecimal(0.00);
        double received = 0;
        double deductionPreFee = 0;
        if (flag >= 0) {
            received = 0;
            deductionPreFee = receivedAmount.doubleValue();
        } else {
            received = receivedAmount.subtract(remainAmount).doubleValue();
            deductionPreFee = remainAmount.doubleValue();
        }
        payFeeDetailPo.setReceivedAmount(received + "");
        payFeeDetailPo.setAcctAmount(deductionPreFee + "");

    }

    /**
     * 改造赠送逻辑 if 嵌套有点多 优化
     *
     * @param paramObj
     * @param payFeePo
     * @param payFeeDetailPo
     * @throws ParseException
     */
    private void hasDiscount(JSONObject paramObj, PayFeePo payFeePo, PayFeeDetailPo payFeeDetailPo, FeeDto feeDto) throws ParseException {
        if (!paramObj.containsKey("selectDiscount")) {
            return;
        }
        JSONArray selectDiscounts = paramObj.getJSONArray("selectDiscount");

        if (ListUtil.isNull(selectDiscounts)) {
            return;
        }
        Map feePriceMap = computeFeeSMOImpl.getFeePrice(feeDto);
        for (int index = 0; index < selectDiscounts.size(); index++) {
            JSONObject paramJson = selectDiscounts.getJSONObject(index);
            if (!"102020008".equals(paramJson.getString("ruleId"))) { //赠送规则
                continue;
            }
            JSONArray feeDiscountSpecs = paramJson.getJSONArray("feeDiscountSpecs");
            if (ListUtil.isNull(feeDiscountSpecs)) {
                continue;
            }
            for (int specIndex = 0; specIndex < feeDiscountSpecs.size(); specIndex++) {
                JSONObject paramIn = feeDiscountSpecs.getJSONObject(specIndex);
                if (!"89002020980015".equals(paramIn.getString("specId"))) { //赠送月份
                    continue;
                }
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String specValue = paramIn.getString("specValue");
                //获取费用结束时间(也就是下次费用开始时间)
                Date endTime = df.parse(payFeeDetailPo.getEndTime());
                Calendar cal = Calendar.getInstance();
                cal.setTime(endTime);
                cal.add(Calendar.MONTH, Integer.parseInt(specValue));
                payFeeDetailPo.setEndTime(df.format(cal.getTime()));
                // payFeePo.setEndTime(df.format(cal.getTime()));

                BigDecimal value = new BigDecimal(payFeeDetailPo.getGiftAmount());
                value = value.add(new BigDecimal(specValue).multiply(new BigDecimal(feePriceMap.get("feePrice").toString())));
                payFeeDetailPo.setGiftAmount(value.doubleValue() + "");
            }
        }

        for (int discountIndex = 0; discountIndex < selectDiscounts.size(); discountIndex++) {
            JSONObject param = selectDiscounts.getJSONObject(discountIndex);
            addPayFeeDetailDiscount(paramObj, param, payFeeDetailPo);
        }

    }

    /**
     * 添加小区信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void addPayFeeDetailDiscount(JSONObject paramInJson, JSONObject discountJson, PayFeeDetailPo payFeeDetailPo) {
        JSONObject businessFee = new JSONObject();
        businessFee.put("detailDiscountId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailDiscountId));
        businessFee.put("discountPrice", discountJson.getString("discountPrice"));
        businessFee.put("discountId", discountJson.getString("discountId"));
        businessFee.put("detailId", paramInJson.getString("detailId"));
        businessFee.put("communityId", paramInJson.getString("communityId"));
        businessFee.put("feeId", paramInJson.getString("feeId"));
        PayFeeDetailDiscountPo payFeeDetailDiscount = BeanConvertUtil.covertBean(businessFee, PayFeeDetailDiscountPo.class);
        //businessFee.putAll(feeMap);
        int fage = payFeeDetailDiscountNewV1InnerServiceSMOImpl.savePayFeeDetailDiscountNew(payFeeDetailDiscount);

        if (fage < 1) {
            throw new CmdException("更新费用信息失败");
        }

        FeeDiscountDto feeDiscountDto = new FeeDiscountDto();
        feeDiscountDto.setDiscountId(discountJson.getString("discountId"));
        List<FeeDiscountDto> feeDiscountDtos = feeDiscountInnerServiceSMOImpl.queryFeeDiscounts(feeDiscountDto);

        if (ListUtil.isNull(feeDiscountDtos)) {
            return;
        }

        //todo 打折或者空置房打折
        if (FeeDiscountRuleDto.DISCOUNT_SMALL_TYPE_DISCOUNT.equals(feeDiscountDtos.get(0).getDiscountSmallType())
                || FeeDiscountRuleDto.DISCOUNT_SMALL_TYPE_APPLY_DISCOUNT.equals(feeDiscountDtos.get(0).getDiscountSmallType())
        ) {
            BigDecimal value = new BigDecimal(payFeeDetailPo.getDiscountAmount());
            value = value.add(new BigDecimal(discountJson.getString("discountPrice")));
            payFeeDetailPo.setDiscountAmount(value.doubleValue() + "");
        } else if (FeeDiscountRuleDto.DISCOUNT_SMALL_TYPE_DEDUCTION.equals(feeDiscountDtos.get(0).getDiscountSmallType())
                || FeeDiscountRuleDto.DISCOUNT_SMALL_TYPE_APPLY_DEDUCTION.equals(feeDiscountDtos.get(0).getDiscountSmallType())) {
            BigDecimal value = new BigDecimal(payFeeDetailPo.getDeductionAmount());
            value = value.add(new BigDecimal(discountJson.getString("discountPrice")));
            payFeeDetailPo.setDeductionAmount(value.doubleValue() + "");
        } else if (FeeDiscountRuleDto.DISCOUNT_SMALL_TYPE_LATE.equals(feeDiscountDtos.get(0).getDiscountSmallType())) {
            BigDecimal value = new BigDecimal(payFeeDetailPo.getLateAmount());
            value = value.add(new BigDecimal(discountJson.getString("discountPrice")));
            payFeeDetailPo.setLateAmount(value.doubleValue() + "");
        }
    }





    public void dealPreFee(JSONObject paramObj, PayFeeDetailPo payFeeDetailPo, PayFeePo payFeePo) {
        String acctAmount = payFeeDetailPo.getAcctAmount();
        if (StringUtil.isEmpty(acctAmount)) {
            return;
        }
        double acct = Double.parseDouble(acctAmount);
        if (acct <= 0) {
            return;
        }
        //判断选择的账号
        JSONArray jsonArray = paramObj.getJSONArray("selectPreFees");
        if (ListUtil.isNull(jsonArray)) {
            return;
        }

        JSONObject preFee = jsonArray.getJSONObject(0);

        deductionPrePayFeeImpl.deductionAmount(preFee.getString("ppfId"),
                Double.parseDouble(payFeeDetailPo.getAcctAmount()),
                payFeePo.getFeeId());
    }


    /**
     * 处理费用项ID
     *
     * @param tmpPayFeePo
     * @param feeInfo
     */
    private void doChangeConfigId(PayFeePo tmpPayFeePo, FeeDto feeInfo) {
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setConfigId(feeInfo.getConfigId());
        feeConfigDto.setCommunityId(feeInfo.getCommunityId());
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        Assert.listOnlyOne(feeConfigDtos, "费用项不存在");
        if (FeeDto.FEE_FLAG_CYCLE.equals(feeConfigDtos.get(0).getFeeFlag())) {
            return;
        }


        FeeConfigDto tmpFeeConfigDto = new FeeConfigDto();
        tmpFeeConfigDto.setFeeNameEq(feeConfigDtos.get(0).getFeeName() + "欠费");
        tmpFeeConfigDto.setFeeFlag(FeeDto.FEE_FLAG_CYCLE);
        tmpFeeConfigDto.setComputingFormula(feeConfigDto.getComputingFormula());
        //todo 校验的时候校验过了 所以这里不可能为空
        feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(tmpFeeConfigDto);

        tmpPayFeePo.setConfigId(feeConfigDtos.get(0).getConfigId());
    }

}
