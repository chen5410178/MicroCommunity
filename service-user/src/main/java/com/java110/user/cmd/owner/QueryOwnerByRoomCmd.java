package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 按房屋查询业主(户主)
 * 服务编码：owner.queryOwnerByRoom
 * 请求路径：/app/owner.queryOwnerByRoom
 * 复用 IOwnerInnerServiceSMO.queryOwnersByRoom(join building_room/building_owner_room_rel/building_owner, owner_type_cd='1001')
 */
@Java110Cmd(serviceCode = "owner.queryOwnerByRoom")
public class QueryOwnerByRoomCmd extends Cmd {

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "roomId", "未包含房屋编号");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        OwnerDto ownerDto = BeanConvertUtil.covertBean(reqJson, OwnerDto.class);
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnersByRoom(ownerDto);
        int total = ListUtil.isNull(ownerDtos) ? 0 : ownerDtos.size();
        if (ListUtil.isNull(ownerDtos)) {
            ownerDtos = new ArrayList<>();
        }
        context.setResponseEntity(ResultVo.createResponseEntity((int) Math.ceil((double) total / (double) reqJson.getInteger("row")), total, ownerDtos));
    }
}
