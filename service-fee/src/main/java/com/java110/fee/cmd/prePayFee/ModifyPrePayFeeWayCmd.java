package com.java110.fee.cmd.prePayFee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.intf.fee.IPrePayFeeV1InnerServiceSMO;
import com.java110.po.prePayFee.PrePayFeePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;

@Java110Cmd(serviceCode = "prePayFee.modifyPrePayFeeWay")
public class ModifyPrePayFeeWayCmd extends Cmd {

    @Autowired
    private IPrePayFeeV1InnerServiceSMO prePayFeeV1InnerServiceSMOImpl;
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "ppfId", "未包含预付款编号");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
        Assert.hasKeyAndValue(reqJson, "way", "未包含扣款方式");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        PrePayFeePo prePayFeePo = new PrePayFeePo();
        prePayFeePo.setPpfId(reqJson.getString("ppfId"));
        prePayFeePo.setCommunityId(reqJson.getString("communityId"));
        prePayFeePo.setWay(reqJson.getString("way"));
        int flag = prePayFeeV1InnerServiceSMOImpl.updatePrePayFee(prePayFeePo);
        if (flag < 1) {
            throw new CmdException("修改失败");
        }
    }
}
