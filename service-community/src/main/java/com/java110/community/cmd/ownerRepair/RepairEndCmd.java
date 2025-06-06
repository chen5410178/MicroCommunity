package com.java110.community.cmd.ownerRepair;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.intf.community.IRepairPoolV1InnerServiceSMO;
import com.java110.intf.community.IRepairUserInnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.owner.RepairPoolPo;
import com.java110.po.owner.RepairUserPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "ownerRepair.repairEnd")
public class RepairEndCmd extends Cmd {

    @Autowired
    private IRepairPoolV1InnerServiceSMO repairPoolV1InnerServiceSMOImpl;
    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
        Assert.hasKeyAndValue(reqJson, "repairId", "未包含工单");
        Assert.hasKeyAndValue(reqJson, "context", "未包含工单");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        String staffId = CmdContextUtils.getUserId(context);
        UserDto userDto = new UserDto();
        userDto.setUserId(staffId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        if (ListUtil.isNull(userDtos)) {
            throw new CmdException("登陆用户不存在");
        }

        RepairDto repairDto = new RepairDto();
        repairDto.setRepairId(reqJson.getString("repairId"));
        repairDto.setCommunityId(reqJson.getString("communityId"));
        List<RepairDto> repairDtos = repairInnerServiceSMOImpl.queryRepairs(repairDto);
        if (ListUtil.isNull(repairDtos)) {
            throw new CmdException("工单不存在");
        }
        RepairUserDto repairUserDto = new RepairUserDto();
        repairUserDto.setRepairId(reqJson.getString("repairId"));
        repairUserDto.setCommunityId(reqJson.getString("communityId"));
        repairUserDto.setState(RepairUserDto.STATE_DOING);
        List<RepairUserDto> repairUserDtos = repairUserInnerServiceSMOImpl.queryRepairUsers(repairUserDto);
        RepairUserPo repairUserPo = null;
        if (!ListUtil.isNull(repairUserDtos)) {
            for (RepairUserDto tRepairUserDto : repairUserDtos) {
                repairUserPo = new RepairUserPo();
                repairUserPo.setRuId(tRepairUserDto.getRuId());
                repairUserPo.setState(RepairUserDto.STATE_CLOSE);
                repairUserPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
                repairUserPo.setCommunityId(reqJson.getString("communityId"));
                repairUserPo.setContext(userDtos.get(0).getName() + "结束了工单");
                repairUserInnerServiceSMOImpl.updateRepairUser(repairUserPo);
            }
        }


        RepairPoolPo repairPoolPo = new RepairPoolPo();
        repairPoolPo.setRepairId(reqJson.getString("repairId"));
        repairPoolPo.setState(RepairDto.STATE_COMPLATE);
        repairPoolPo.setFinishTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        int flag = repairPoolV1InnerServiceSMOImpl.updateRepairPoolNew(repairPoolPo);

        repairUserPo = new RepairUserPo();
        repairUserPo.setRuId(GenerateCodeFactory.getGeneratorId("11"));
        repairUserPo.setRepairId(reqJson.getString("repairId"));
        repairUserPo.setUserId(staffId);
        repairUserPo.setCommunityId(reqJson.getString("communityId"));
        repairUserPo.setState(RepairUserDto.STATE_CLOSE);
        repairUserPo.setContext("结束原因："+reqJson.getString("context"));
        repairUserPo.setStaffId(staffId);
        repairUserPo.setStaffName(userDtos.get(0).getName());
        repairUserPo.setPreStaffId(staffId);
        repairUserPo.setPreStaffName(userDtos.get(0).getName());
        repairUserPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        repairUserPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        repairUserPo.setRepairEvent(RepairUserDto.REPAIR_EVENT_AUDIT_USER);
        repairUserPo.setPreRuId("-1");
        repairUserPo.setCreateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        repairUserInnerServiceSMOImpl.saveRepairUser(repairUserPo);
    }
}
