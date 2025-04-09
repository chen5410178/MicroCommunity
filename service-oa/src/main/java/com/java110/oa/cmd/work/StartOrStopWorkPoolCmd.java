package com.java110.oa.cmd.work;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.work.WorkPoolDto;
import com.java110.intf.oa.IWorkPoolV1InnerServiceSMO;
import com.java110.po.workPool.WorkPoolPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "work.startOrStopWorkPool")
public class StartOrStopWorkPoolCmd extends Cmd {

    @Autowired
    private IWorkPoolV1InnerServiceSMO workPoolV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区编号");
        Assert.hasKeyAndValue(reqJson, "workId", "workId不能为空");
        Assert.hasKeyAndValue(reqJson, "state", "state不能为空");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        WorkPoolDto workPoolDto = new WorkPoolDto();
        workPoolDto.setWorkId(reqJson.getString("workId"));
        workPoolDto.setCommunityId(reqJson.getString("communityId"));
        List<WorkPoolDto> workPoolDtos = workPoolV1InnerServiceSMOImpl.queryWorkPools(workPoolDto);

        if (ListUtil.isNull(workPoolDtos)) {
            throw new CmdException("工作单不存在");
        }

        WorkPoolPo workPoolPo = new WorkPoolPo();
        workPoolPo.setWorkId(workPoolDtos.get(0).getWorkId());
        workPoolPo.setState(reqJson.getString("state"));
        workPoolPo.setCommunityId(reqJson.getString("communityId"));
        workPoolV1InnerServiceSMOImpl.updateWorkPool(workPoolPo);
    }
}
