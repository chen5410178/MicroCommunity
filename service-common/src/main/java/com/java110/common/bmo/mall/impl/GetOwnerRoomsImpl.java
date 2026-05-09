package com.java110.common.bmo.mall.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.mall.IMallCommonApiBmo;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.room.RoomDto;
import com.java110.intf.community.IFloorInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.user.IBuildingOwnerV1InnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("getOwnerRoomsImpl")
public class GetOwnerRoomsImpl implements IMallCommonApiBmo {

    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;


    @Autowired
    private IOwnerAppUserV1InnerServiceSMO ownerAppUserV1InnerServiceSMOImpl;

    @Autowired
    private IBuildingOwnerV1InnerServiceSMO buildingOwnerV1InnerServiceSMOImpl;


    /**
     * 处理房屋编号参数的方法
     *
     * 解析请求中的房屋编号参数，将其拆分为楼层号、单元号和房间号，
     * 并设置到RoomDto对象中
     *
     * @param reqJson 请求参数的JSON对象
     * @param roomDto 房屋数据传输对象，用于设置解析后的房屋信息
     */
    private void hasRoomNum(JSONObject reqJson, RoomDto roomDto) {
        // 检查请求参数中是否包含roomNum
        if (!reqJson.containsKey("roomNum")) {
            return;
        }

        String roomNum = reqJson.getString("roomNum");

        // 检查roomNum是否为空
        if (StringUtil.isEmpty(roomNum)) {
            return;
        }

        // 按照"-"分割房屋编号，格式为：楼层号-单元号-房间号
        String[] roomNums = reqJson.getString("roomNum").split("-");

        // 如果分割后的数组长度为3，说明格式正确
        if (roomNums != null && roomNums.length == 3) {
            // 分别设置楼层号、单元号和房间号
            roomDto.setFloorNum(roomNums[0]);
            roomDto.setUnitNum(roomNums[1]);
            roomDto.setRoomNum(roomNums[2]);
        }
    }

    @Override
    public void validate(ICmdDataFlowContext context, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "请求中未包含communityId信息");
    }

    @Override
    public void doCmd(ICmdDataFlowContext context, JSONObject reqJson) {
        // 构建业主应用用户查询条件
        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setLink(reqJson.getString("mallTel")); // 设置用户ID
        ownerAppUserDto.setCommunityId(reqJson.getString("communityId")); // 设置小区ID

        // 查询用户关联的房屋信息
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserV1InnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);

        // 检查是否已绑定业主身份
        if (ListUtil.isNull(ownerAppUserDtos)) {
            throw new CmdException("未绑定业主");
        }

        // 遍历绑定关系，获取有效的成员ID
        String memberId = "";
        for (OwnerAppUserDto tmpOwnerAppUserDto : ownerAppUserDtos) {
            // 跳过无效的成员ID（-1表示未认证）
            if ("-1".equals(tmpOwnerAppUserDto.getMemberId())) {
                continue;
            }
            memberId = tmpOwnerAppUserDto.getMemberId();
        }

        // 检查是否已认证房屋
        if (StringUtil.isEmpty(memberId)) {
            throw new CmdException("未认证房屋");
        }

        // 构建业主查询条件
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(memberId);
        ownerDto.setCommunityId(reqJson.getString("communityId"));

        // 查询业主详细信息
        List<OwnerDto> ownerDtos = buildingOwnerV1InnerServiceSMOImpl.queryBuildingOwners(ownerDto);

        // 检查业主是否存在
        if(ListUtil.isNull(ownerDtos)){
            throw new CmdException("业主或成员不存在");
        }

        // 将请求参数转换为RoomDto对象
        RoomDto roomDto = BeanConvertUtil.covertBean(reqJson, RoomDto.class);
        // 设置业主ID
        roomDto.setOwnerId(ownerDtos.get(0).getOwnerId());
        // 处理房屋编号参数
        hasRoomNum(reqJson, roomDto);


        // 查询业主名下的房屋信息
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRoomsByOwner(roomDto);

        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(roomDtos);

        context.setResponseEntity(responseEntity);
    }
}
