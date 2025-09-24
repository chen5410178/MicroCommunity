package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.intf.fee.IPayFeeV1InnerServiceSMO;
import com.java110.po.fee.PayFeePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;

/**
 * 修改费用备注
 */
@Java110Cmd(serviceCode = "fee.modifyFeeRemark")
public class ModifyFeeRemarkCmd extends Cmd {

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "feeId", "未包含费用");
        Assert.hasKeyAndValue(reqJson, "feeRemark", "未包含备注");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        PayFeePo payFeePo = new PayFeePo();
        payFeePo.setFeeId(reqJson.getString("feeId"));
        payFeePo.setFeeRemark(reqJson.getString("feeRemark"));
        payFeeV1InnerServiceSMOImpl.updatePayFee(payFeePo);
    }
}
