package com.java110.fee.cmd.manualBill;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.fee.ManualBillGenerationDto;
import com.java110.intf.fee.IManualBillGenerationV1InnerServiceSMO;
import com.java110.po.fee.ManualBillGenerationPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Java110Cmd(serviceCode = "fee.deleteManualBillApplication")
public class DeleteManualBillApplicationCmd extends Cmd {

    @Autowired
    private IManualBillGenerationV1InnerServiceSMO manualBillGenerationV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "applicationId", "未包含申请ID");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
        reqJson.remove("userId");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        String applicationId = reqJson.getString("applicationId");
        String communityId = reqJson.getString("communityId");

        ManualBillGenerationDto queryDto = new ManualBillGenerationDto();
        queryDto.setApplicationId(applicationId);
        queryDto.setCommunityId(communityId);
        List<ManualBillGenerationDto> applications = manualBillGenerationV1InnerServiceSMOImpl.queryManualBillGenerations(queryDto);
        Assert.listOnlyOne(applications, "申请不存在");

        if (!"1001".equals(applications.get(0).getState())) {
            throw new CmdException("仅待审核状态的申请可删除");
        }

        ManualBillGenerationPo deletePo = new ManualBillGenerationPo();
        deletePo.setApplicationId(applicationId);
        deletePo.setStatusCd("1");
        manualBillGenerationV1InnerServiceSMOImpl.updateManualBillGeneration(deletePo);

        context.setResponseEntity(ResultVo.success());
    }
}
