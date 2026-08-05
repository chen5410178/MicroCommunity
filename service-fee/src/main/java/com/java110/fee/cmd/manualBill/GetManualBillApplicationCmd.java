package com.java110.fee.cmd.manualBill;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.fee.ManualBillGenerationDetailDto;
import com.java110.dto.fee.ManualBillGenerationDto;
import com.java110.intf.fee.IManualBillGenerationV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Java110Cmd(serviceCode = "fee.getManualBillApplication")
public class GetManualBillApplicationCmd extends Cmd {

    @Autowired
    private IManualBillGenerationV1InnerServiceSMO manualBillGenerationV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "applicationId", "未包含申请ID");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
        reqJson.remove("userId");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        ManualBillGenerationDto queryDto = new ManualBillGenerationDto();
        queryDto.setApplicationId(reqJson.getString("applicationId"));
        queryDto.setCommunityId(reqJson.getString("communityId"));
        List<ManualBillGenerationDto> applications = manualBillGenerationV1InnerServiceSMOImpl.queryManualBillGenerations(queryDto);

        if (applications == null || applications.isEmpty()) {
            context.setResponseEntity(ResultVo.error("申请不存在"));
            return;
        }

        ManualBillGenerationDetailDto detailQuery = new ManualBillGenerationDetailDto();
        detailQuery.setApplicationId(reqJson.getString("applicationId"));
        detailQuery.setCommunityId(reqJson.getString("communityId"));
        List<ManualBillGenerationDetailDto> details = manualBillGenerationV1InnerServiceSMOImpl.queryManualBillGenerationDetails(detailQuery);

        Map<String, Object> result = new HashMap<>();
        result.put("application", applications.get(0));
        result.put("details", details != null ? details : new java.util.ArrayList<>());

        context.setResponseEntity(ResultVo.createResponseEntity(result));
    }
}
