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
 * 查询发票抬头(商城) 服务编码：invoice.queryInvoiceHeadings
 * 转发商城 listInvoiceHeadingsBmoImpl(按 userPhone 分页列表, 关键字模糊)
 */
@Java110Cmd(serviceCode = "invoice.queryInvoiceHeadings")
public class QueryInvoiceHeadingsCmd extends Cmd {

    @Autowired
    private IMallInnerServiceSMO mallInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "userPhone", "请求报文中未包含业主手机号");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        JSONObject paramIn = new JSONObject();
        paramIn.put("userPhone", reqJson.getString("userPhone"));
        paramIn.put("keyword", reqJson.getString("keyword"));
        paramIn.put("page", reqJson.getInteger("page"));
        paramIn.put("row", reqJson.getInteger("row"));

        ResultVo resultVo = mallInnerServiceSMOImpl.postMallData(new MallDataDto("listInvoiceHeadingsBmoImpl", paramIn));
        if (resultVo.getCode() != ResultVo.CODE_OK) {
            throw new CmdException(resultVo.getMsg());
        }
        if (resultVo.getData() == null) {
            throw new CmdException("商城系统未部署");
        }
        context.setResponseEntity(ResultVo.createResponseEntity((int) resultVo.getRecords(), (int) resultVo.getTotal(), resultVo.getData()));
    }
}
