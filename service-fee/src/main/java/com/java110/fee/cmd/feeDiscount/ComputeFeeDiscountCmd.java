package com.java110.fee.cmd.feeDiscount;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.fee.ComputeDiscountDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.fee.PayFeeDto;
import com.java110.fee.bill.IComputeFeeDiscount;
import com.java110.intf.fee.IPayFeeV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * 计算费用折扣
 */
@Java110Cmd(serviceCode = "feeDiscount.computeFeeDiscount")
public class ComputeFeeDiscountCmd extends Cmd {

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;


    @Autowired
    private IComputeFeeDiscount computeFeeDiscountImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区编号");
        Assert.hasKey(reqJson, "feeIds", "未包含费用编号");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        JSONArray feeIds = reqJson.getJSONArray("feeIds");

        PayFeeDto feeDto = new PayFeeDto();
        feeDto.setFeeIds(feeIds.toArray(new String[feeIds.size()]));
        feeDto.setCommunityId(reqJson.getString("communityId"));
        feeDto.setState(FeeDto.STATE_DOING);
        List<PayFeeDto> feeDtos = payFeeV1InnerServiceSMOImpl.queryPayFees(feeDto);
        if (ListUtil.isNull(feeDtos)) {
            return;
        }

        List<ComputeDiscountDto> computeDiscountDtos = computeFeeDiscountImpl.compute(feeDtos);
        context.setResponseEntity(ResultVo.createResponseEntity(computeDiscountDtos));
    }



}
