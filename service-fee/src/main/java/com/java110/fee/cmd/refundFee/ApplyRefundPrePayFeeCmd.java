package com.java110.fee.cmd.refundFee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.payFee.ReturnPayFeeDto;
import com.java110.dto.payFeeOrder.PayFeeOrderDto;
import com.java110.dto.prePayFee.PrePayFeeDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.fee.IPayFeeOrderV1InnerServiceSMO;
import com.java110.intf.fee.IPrePayFeeV1InnerServiceSMO;
import com.java110.intf.fee.IRefundFeeDetailV1InnerServiceSMO;
import com.java110.intf.fee.IRefundFeeOrderV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.prePayFee.PrePayFeePo;
import com.java110.po.refundFeeDetail.RefundFeeDetailPo;
import com.java110.po.refundFeeOrder.RefundFeeOrderPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "refundFee.applyRefundPrePayFee")
public class ApplyRefundPrePayFeeCmd extends Cmd {

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeOrderV1InnerServiceSMO payFeeOrderV1InnerServiceSMOImpl;

    @Autowired
    private IRefundFeeOrderV1InnerServiceSMO refundFeeOrderV1InnerServiceSMOImpl;

    @Autowired
    private IRefundFeeDetailV1InnerServiceSMO refundFeeDetailV1InnerServiceSMOImpl;

    @Autowired
    private IPrePayFeeV1InnerServiceSMO prePayFeeV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        //Assert.hasKeyAndValue(reqJson, "configId", "请求报文中未包含configId");
        Assert.hasKeyAndValue(reqJson, "payOrderId", "请求报文中未包含缴费订单");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "reason", "请求报文中未包含reason");

        if (!reqJson.containsKey("selectDetailIds")) {
            throw new CmdException("未包含退费明细");
        }

        JSONArray selectDetailIds = reqJson.getJSONArray("selectDetailIds");
        if (ListUtil.isNull(selectDetailIds)) {
            throw new CmdException("未包含退预付收款");
        }
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String userId = CmdContextUtils.getUserId(context);

        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户不存在");

        PayFeeOrderDto payFeeOrderDto = new PayFeeOrderDto();
        payFeeOrderDto.setPayOrderId(reqJson.getString("payOrderId"));
        List<PayFeeOrderDto> payFeeOrderDtos = payFeeOrderV1InnerServiceSMOImpl.queryPayFeeOrders(payFeeOrderDto);
        Assert.listOnlyOne(payFeeOrderDtos, "缴费订单不存在");

        RefundFeeOrderPo refundFeeOrderPo = new RefundFeeOrderPo();
        refundFeeOrderPo.setOrderType(payFeeOrderDtos.get(0).getOrderType());
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


        JSONArray selectDetailIds = reqJson.getJSONArray("selectDetailIds");
        int flag = 0;
        PrePayFeePo prePayFeePo = null;
        RefundFeeDetailPo refundFeeDetailPo = null;
        for(int detailIndex = 0; detailIndex < selectDetailIds.size() ; detailIndex++){
            PrePayFeeDto prePayFeeDto = new PrePayFeeDto();
            prePayFeeDto.setPpfId(selectDetailIds.getString(detailIndex));
            List<PrePayFeeDto> prePayFeeDtos = prePayFeeV1InnerServiceSMOImpl.queryPrePayFees(prePayFeeDto);
            Assert.listOnlyOne(prePayFeeDtos, "未找到需要修改的活动 或多条数据");
            prePayFeePo = new PrePayFeePo();
            prePayFeePo.setPpfId(prePayFeeDtos.get(0).getPpfId());
            prePayFeePo.setState(PrePayFeeDto.STATE_RETURNING);
            flag = prePayFeeV1InnerServiceSMOImpl.updatePrePayFee(prePayFeePo);
            if (flag < 1) {
                throw new CmdException("保存数据失败");
            }
            refundFeeDetailPo = new RefundFeeDetailPo();
            refundFeeDetailPo.setRefundOrderId(refundFeeOrderPo.getRefundOrderId());
            refundFeeDetailPo.setFeeName(prePayFeeDtos.get(0).getFeeName());
            refundFeeDetailPo.setDetailId(prePayFeeDtos.get(0).getPpfId());
            refundFeeDetailPo.setStartTime(prePayFeeDtos.get(0).getStartTime());
            refundFeeDetailPo.setRemark(prePayFeeDtos.get(0).getRemark());
            refundFeeDetailPo.setEndTime(prePayFeeDtos.get(0).getEndTime());
            refundFeeDetailPo.setReceivedAmount(prePayFeeDtos.get(0).getReceivedAmount());
            refundFeeDetailPo.setCommunityId(prePayFeeDtos.get(0).getCommunityId());
            refundFeeDetailPo.setRfdId(GenerateCodeFactory.getGeneratorId("11"));
            refundFeeDetailPo.setFeeId(prePayFeeDtos.get(0).getRuleId());
            refundFeeDetailV1InnerServiceSMOImpl.saveRefundFeeDetail(refundFeeDetailPo);
        }

        refundFeeOrderV1InnerServiceSMOImpl.saveRefundFeeOrder(refundFeeOrderPo);


        context.setResponseEntity(ResultVo.success());
    }
}
