package com.java110.acct.cmd.invoice;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.MallDataDto;
import com.java110.intf.job.IMallInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 删除发票抬头(商城) 服务编码：invoice.deleteInvoiceHeading
 * 转发商城 deleteInvoiceHeadingBmoImpl(默认抬头禁止删除)
 */
@Java110Cmd(serviceCode = "invoice.deleteInvoiceHeading")
public class DeleteInvoiceHeadingCmd extends Cmd {

    @Autowired
    private IMallInnerServiceSMO mallInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "headingId", "请求报文中未包含抬头ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        JSONObject paramIn = new JSONObject();
        paramIn.put("headingId", reqJson.getString("headingId"));

        ResultVo resultVo = mallInnerServiceSMOImpl.postMallData(new MallDataDto("deleteInvoiceHeadingBmoImpl", paramIn));
        if (resultVo.getCode() != ResultVo.CODE_OK) {
            throw new CmdException(resultVo.getMsg());
        }
        if (resultVo.getData() == null) {
            throw new CmdException("商城系统未部署");
        }
        context.setResponseEntity(ResultVo.success());
    }
}
