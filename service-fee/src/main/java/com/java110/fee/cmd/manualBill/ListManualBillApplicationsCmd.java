package com.java110.fee.cmd.manualBill;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.fee.ManualBillGenerationDto;
import com.java110.intf.fee.IManualBillGenerationV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "fee.listManualBillApplications")
public class ListManualBillApplicationsCmd extends Cmd {

    @Autowired
    private IManualBillGenerationV1InnerServiceSMO manualBillGenerationV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
        reqJson.remove("userId");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        ManualBillGenerationDto manualBillGenerationDto = BeanConvertUtil.covertBean(reqJson, ManualBillGenerationDto.class);

        int count = manualBillGenerationV1InnerServiceSMOImpl.queryManualBillGenerationsCount(manualBillGenerationDto);
        List<ManualBillGenerationDto> list = new ArrayList<>();
        if (count > 0) {
            list = manualBillGenerationV1InnerServiceSMOImpl.queryManualBillGenerations(manualBillGenerationDto);
        }

        context.setResponseEntity(ResultVo.createResponseEntity(
                (int) Math.ceil((double) count / (double) reqJson.getInteger("row")),
                count,
                list));
    }
}
