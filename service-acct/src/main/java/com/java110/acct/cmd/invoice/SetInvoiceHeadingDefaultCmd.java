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
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 设置默认发票抬头(商城) 服务编码：invoice.setInvoiceHeadingDefault
 * 转发商城 setInvoiceHeadingDefaultBmoImpl(同类型默认唯一)
 */
@Java110Cmd(serviceCode = "invoice.setInvoiceHeadingDefault")
public class SetInvoiceHeadingDefaultCmd extends Cmd {

    @Autowired
    private IMallInnerServiceSMO mallInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "headingId", "请求报文中未包含抬头ID");
        Assert.hasKeyAndValue(reqJson, "headingType", "请求报文中未包含抬头类型");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        // 类型转换 1001-个人 2002-公司 -> 商城 个人/公司
        String headingType = reqJson.getString("headingType");
        if ("1001".equals(headingType)) {
            headingType = "个人";
        } else if ("2002".equals(headingType)) {
            headingType = "公司";
        }

        JSONObject paramIn = new JSONObject();
        paramIn.put("headingId", reqJson.getString("headingId"));
        paramIn.put("headingType", headingType);
        if (!StringUtil.isEmpty(reqJson.getString("userPhone"))) {
            paramIn.put("userPhone", reqJson.getString("userPhone"));
        }

        ResultVo resultVo = mallInnerServiceSMOImpl.postMallData(new MallDataDto("setInvoiceHeadingDefaultBmoImpl", paramIn));
        if (resultVo.getCode() != ResultVo.CODE_OK) {
            throw new CmdException(resultVo.getMsg());
        }
        if (resultVo.getData() == null) {
            throw new CmdException("商城系统未部署");
        }
        context.setResponseEntity(ResultVo.success());
    }
}
