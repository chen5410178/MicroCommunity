package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.user.IOwnerCarV1InnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.po.car.OwnerCarPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@Java110Cmd(serviceCode = "owner.changeCarOwner")
public class ChangeOwnerCarCmd extends Cmd {

    private static final Logger logger = LoggerFactory.getLogger(ChangeOwnerCarCmd.class);

    @Autowired
    private IOwnerCarV1InnerServiceSMO ownerCarV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
        Assert.hasKeyAndValue(reqJson, "memberId", "请求报文中未包含carId");
        Assert.hasKeyAndValue(reqJson, "newOwnerId", "请求报文中未包含newOwnerId");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        String communityId = reqJson.getString("communityId");
        String memberId = reqJson.getString("memberId");
        String newOwnerId = reqJson.getString("newOwnerId");

        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setMemberId(memberId);
        ownerCarDto.setCommunityId(communityId);
        List<OwnerCarDto> ownerCarDtos = ownerCarV1InnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

        if (ownerCarDtos == null || ownerCarDtos.isEmpty()) {
            throw new IllegalArgumentException("车辆不存在或不属于当前小区");
        }

        String oldOwnerId = ownerCarDtos.get(0).getOwnerId();
        if (oldOwnerId.equals(newOwnerId)) {
            throw new IllegalArgumentException("新业主与原业主相同，无需修改");
        }

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setOwnerId(newOwnerId);
        ownerDto.setCommunityId(communityId);
        List<OwnerDto> owners = ownerInnerServiceSMOImpl.queryOwners(ownerDto);

        if (owners == null || owners.isEmpty()) {
            throw new IllegalArgumentException("新业主不存在或不属于当前小区");
        }

        OwnerCarPo ownerCarPo = new OwnerCarPo();
        ownerCarPo.setMemberId(memberId);
        ownerCarPo.setOwnerId(newOwnerId);

        int flag = ownerCarV1InnerServiceSMOImpl.updateOwnerCar(ownerCarPo);
        if (flag < 1) {
            throw new CmdException("修改车辆业主失败");
        }

        logger.info("车辆业主变更成功: carId={}, oldOwnerId={}, newOwnerId={}, operatorTime={}",
                memberId, oldOwnerId, newOwnerId, DateUtil.getFormatTimeString(new Date(), DateUtil.DATE_FORMATE_STRING_A));
    }
}
