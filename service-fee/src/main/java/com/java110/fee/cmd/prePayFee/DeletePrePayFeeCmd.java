package com.java110.fee.cmd.prePayFee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.prePayFee.PrePayFeeDto;
import com.java110.intf.fee.IPayFeeOrderV1InnerServiceSMO;
import com.java110.intf.fee.IPrePayFeeV1InnerServiceSMO;
import com.java110.po.payFeeOrder.PayFeeOrderPo;
import com.java110.po.prePayFee.PrePayFeePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "prePayFee.deletePrePayFee")
public class DeletePrePayFeeCmd extends Cmd {

    @Autowired
    private IPrePayFeeV1InnerServiceSMO prePayFeeV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeOrderV1InnerServiceSMO payFeeOrderV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "ppfId", "未包含预付款编号");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        PrePayFeeDto prePayFeeDto = new PrePayFeeDto();
        prePayFeeDto.setPpfId(reqJson.getString("ppfId"));
        prePayFeeDto.setCommunityId(reqJson.getString("communityId"));
        List<PrePayFeeDto> prePayFeeDtos = prePayFeeV1InnerServiceSMOImpl.queryPrePayFees(prePayFeeDto);
        if(ListUtil.isNull(prePayFeeDtos)){
            throw new CmdException("预付款不存在");
        }

        PrePayFeePo prePayFeePo = new PrePayFeePo();
        prePayFeePo.setPpfId(reqJson.getString("ppfId"));
        prePayFeePo.setCommunityId(reqJson.getString("communityId"));
        int flag = prePayFeeV1InnerServiceSMOImpl.deletePrePayFee(prePayFeePo);
        if (flag < 1) {
            throw new CmdException("删除失败");
        }

        //计算预存款订单
        prePayFeeDto = new PrePayFeeDto();
        prePayFeeDto.setPayOrderId(prePayFeeDtos.get(0).getPayOrderId());
        List<PrePayFeeDto> orderPrePayFeeDtos = prePayFeeV1InnerServiceSMOImpl.queryPrePayFees(prePayFeeDto);
        if(ListUtil.isNull(orderPrePayFeeDtos)){
            PayFeeOrderPo payFeeOrderPo = new PayFeeOrderPo();
            payFeeOrderPo.setPayOrderId(prePayFeeDtos.get(0).getPayOrderId());
            payFeeOrderV1InnerServiceSMOImpl.deletePayFeeOrder(payFeeOrderPo);
            return;
        }
        BigDecimal orderReceivableAmount = new BigDecimal("0");
        BigDecimal orderReceivedAmount = new BigDecimal("0");
        for(PrePayFeeDto orderPrePayFeeDto: orderPrePayFeeDtos){
            orderReceivableAmount = orderReceivableAmount.add(new BigDecimal(orderPrePayFeeDto.getReceivableAmount()));
            orderReceivedAmount = orderReceivedAmount.add(new BigDecimal(orderPrePayFeeDto.getReceivedAmount()));
        }

        PayFeeOrderPo payFeeOrderPo = new PayFeeOrderPo();
        payFeeOrderPo.setPayOrderId(prePayFeeDtos.get(0).getPayOrderId());
        payFeeOrderPo.setReceivableAmount(orderReceivableAmount.doubleValue()+"");
        payFeeOrderPo.setReceivedAmount(orderReceivedAmount.doubleValue()+"");
        payFeeOrderV1InnerServiceSMOImpl.updatePayFeeOrder(payFeeOrderPo);
    }
}
