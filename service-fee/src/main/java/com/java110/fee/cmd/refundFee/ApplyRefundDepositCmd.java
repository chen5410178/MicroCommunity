package com.java110.fee.cmd.refundFee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.payFee.ReturnPayFeeDto;
import com.java110.dto.payFeeOrder.PayFeeOrderDto;
import com.java110.dto.refundFeeOrder.RefundFeeOrderDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.fee.*;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.refundDepositPre.RefundDepositPrePo;
import com.java110.po.refundFeeDetail.RefundFeeDetailPo;
import com.java110.po.refundFeeOrder.RefundFeeOrderPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

/**
 * 退押金
 * {"feeId":"902025062436617538","feeName":"装修保证金2000","way":"C","communityId":"2025060586651511","rules":[],"reason":"dd"}
 */
@Java110Cmd(serviceCode = "refundFee.applyRefundDeposit")
public class ApplyRefundDepositCmd extends Cmd {

    @Autowired
    private IPayFeeOrderV1InnerServiceSMO payFeeOrderV1InnerServiceSMOImpl;

    @Autowired
    private IRefundFeeOrderV1InnerServiceSMO refundFeeOrderV1InnerServiceSMOImpl;

    @Autowired
    private IRefundFeeDetailV1InnerServiceSMO refundFeeDetailV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Autowired
    private IPayFeeDetailV1InnerServiceSMO payFeeDetailV1InnerServiceSMOImpl;

    @Autowired
    private IRefundDepositPreV1InnerServiceSMO refundDepositPreV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
        Assert.hasKeyAndValue(reqJson, "detailId", "未包含费用");
        Assert.hasKeyAndValue(reqJson, "way", "未包含退费方式");
        if ("C".equals(reqJson.getString("way"))) {
            return;
        }
        if (!reqJson.containsKey("rules")) {
            throw new CmdException("未包含预存");
        }
        JSONArray rules = reqJson.getJSONArray("rules");
        if (ListUtil.isNull(rules)) {
            throw new CmdException("未包含预存");
        }

        JSONObject rule = null;
        BigDecimal refundAmount = new BigDecimal("0");
        for (int ruleIndex = 0; ruleIndex < rules.size(); ruleIndex++) {
            rule = rules.getJSONObject(ruleIndex);
            Assert.hasKeyAndValue(rule, "ftName", "未包含ftName");
            Assert.hasKeyAndValue(rule, "ftId", "未包含ftName");
            Assert.hasKeyAndValue(rule, "feeName", "未包含feeName");
            Assert.hasKeyAndValue(rule, "ruleId", "未包含ruleId");
            Assert.hasKeyAndValue(rule, "amount", "未包含amount");
            refundAmount = refundAmount.add(new BigDecimal(rule.getString("amount")));
        }
        FeeDetailDto feeDetailDto = new FeeDetailDto();
        feeDetailDto.setDetailId(reqJson.getString("detailId"));
        List<FeeDetailDto> feeDetailDtos = feeDetailInnerServiceSMOImpl.queryFeeDetails(feeDetailDto);
        Assert.listOnlyOne(feeDetailDtos, "未找到需要修改的活动 或多条数据");

        BigDecimal depositAmount = new BigDecimal(feeDetailDtos.get(0).getReceivedAmount());
        if (depositAmount.doubleValue() != refundAmount.doubleValue()) {
            throw new CmdException("退款金额不相等");
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String userId = CmdContextUtils.getUserId(context);

        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户不存在");

        FeeDetailDto feeDetailDto = new FeeDetailDto();
        feeDetailDto.setDetailId(reqJson.getString("detailId"));
        List<FeeDetailDto> feeDetailDtos = feeDetailInnerServiceSMOImpl.queryFeeDetails(feeDetailDto);
        Assert.listOnlyOne(feeDetailDtos, "未找到需要修改的活动 或多条数据");

        PayFeeDetailPo payFeeDetailPo = new PayFeeDetailPo();
        payFeeDetailPo.setDetailId(feeDetailDtos.get(0).getDetailId());
        payFeeDetailPo.setState(FeeDetailDto.STATE_RETURNING);
        int flag = payFeeDetailV1InnerServiceSMOImpl.updatePayFeeDetailNew(payFeeDetailPo);
        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        PayFeeOrderDto payFeeOrderDto = new PayFeeOrderDto();
        payFeeOrderDto.setPayOrderId(feeDetailDtos.get(0).getPayOrderId());
        List<PayFeeOrderDto> payFeeOrderDtos = payFeeOrderV1InnerServiceSMOImpl.queryPayFeeOrders(payFeeOrderDto);
        Assert.listOnlyOne(payFeeOrderDtos, "缴费订单不存在");

        RefundFeeOrderPo refundFeeOrderPo = new RefundFeeOrderPo();
        refundFeeOrderPo.setOrderType(RefundFeeOrderDto.ORDER_TYPE_DEPOSIT);
        refundFeeOrderPo.setReason(reqJson.getString("reason"));
        refundFeeOrderPo.setPayTime(DateUtil.getFormatTimeStringA(payFeeOrderDtos.get(0).getCreateTime()));
        refundFeeOrderPo.setApplyPersonName(userDtos.get(0).getName());
        refundFeeOrderPo.setRemark(payFeeOrderDtos.get(0).getRemark());
        refundFeeOrderPo.setOwnerId(payFeeOrderDtos.get(0).getOwnerId());
        refundFeeOrderPo.setRefundOrderId(GenerateCodeFactory.getGeneratorId("11"));
        refundFeeOrderPo.setPayerObjName(payFeeOrderDtos.get(0).getPayerObjName());
        refundFeeOrderPo.setOwnerName(payFeeOrderDtos.get(0).getOwnerName());
        refundFeeOrderPo.setAuditPersonName("");
        refundFeeOrderPo.setAuditPersonId("");
        refundFeeOrderPo.setApplyPersonId(userId);
        refundFeeOrderPo.setState(ReturnPayFeeDto.STATE_WAIT);
        refundFeeOrderPo.setCommunityId(payFeeOrderDtos.get(0).getCommunityId());
        refundFeeOrderPo.setPayerObjType(payFeeOrderDtos.get(0).getPayerObjType());
        refundFeeOrderPo.setPayerObjId(payFeeOrderDtos.get(0).getPayerObjId());
        refundFeeOrderPo.setPayOrderId(payFeeOrderDtos.get(0).getExtOrderId());

        refundFeeOrderV1InnerServiceSMOImpl.saveRefundFeeOrder(refundFeeOrderPo);

        RefundFeeDetailPo refundFeeDetailPo = new RefundFeeDetailPo();
        refundFeeDetailPo.setRefundOrderId(refundFeeOrderPo.getRefundOrderId());
        refundFeeDetailPo.setFeeName(feeDetailDtos.get(0).getFeeName());
        refundFeeDetailPo.setDetailId(feeDetailDtos.get(0).getDetailId());
        refundFeeDetailPo.setStartTime(DateUtil.getFormatTimeStringA(feeDetailDtos.get(0).getStartTime()));
        refundFeeDetailPo.setRemark(feeDetailDtos.get(0).getRemark());
        refundFeeDetailPo.setEndTime(DateUtil.getFormatTimeStringA(feeDetailDtos.get(0).getEndTime()));
        refundFeeDetailPo.setReceivedAmount(feeDetailDtos.get(0).getReceivedAmount());
        refundFeeDetailPo.setCommunityId(feeDetailDtos.get(0).getCommunityId());
        refundFeeDetailPo.setRfdId(GenerateCodeFactory.getGeneratorId("11"));
        refundFeeDetailPo.setFeeId(feeDetailDtos.get(0).getFeeId());
        refundFeeDetailV1InnerServiceSMOImpl.saveRefundFeeDetail(refundFeeDetailPo);

        if ("C".equals(reqJson.getString("way"))) {
            return;
        }

        JSONArray rules = reqJson.getJSONArray("rules");
        JSONObject rule = null;
        for (int ruleIndex = 0; ruleIndex < rules.size(); ruleIndex++) {
            rule = rules.getJSONObject(ruleIndex);
            RefundDepositPrePo refundDepositPrePo = new RefundDepositPrePo();
            refundDepositPrePo.setRdpId(GenerateCodeFactory.getGeneratorId("11"));
            refundDepositPrePo.setOwnerId(payFeeOrderDtos.get(0).getOwnerId());
            refundDepositPrePo.setFtName(rule.getString("ftName"));
            refundDepositPrePo.setPayerObjName(payFeeOrderDtos.get(0).getPayerObjName());
            refundDepositPrePo.setOwnerName(payFeeOrderDtos.get(0).getOwnerName());
            refundDepositPrePo.setFeeName(rule.getString("feeName"));
            refundDepositPrePo.setFtId(rule.getString("ftId"));
            refundDepositPrePo.setStartTime("2020-01-01");
            refundDepositPrePo.setEndTime("2050-01-01");
            refundDepositPrePo.setRuleId(rule.getString("ruleId"));
            refundDepositPrePo.setCommunityId(payFeeOrderDtos.get(0).getCommunityId());
            refundDepositPrePo.setPayerObjId(payFeeOrderDtos.get(0).getPayerObjId());
            refundDepositPrePo.setRefundAmount(rule.getString("amount"));
            refundDepositPrePo.setRefundOrderId(refundFeeOrderPo.getRefundOrderId());
            refundDepositPreV1InnerServiceSMOImpl.saveRefundDepositPre(refundDepositPrePo);
        }

    }
}
