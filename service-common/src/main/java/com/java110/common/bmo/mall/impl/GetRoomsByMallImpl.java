package com.java110.common.bmo.mall.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.mall.IMallCommonApiBmo;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.dto.room.RoomDto;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.community.IUnitInnerServiceSMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getRoomsByMallImpl")
public class GetRoomsByMallImpl implements IMallCommonApiBmo {

    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Override
    public void validate(ICmdDataFlowContext context, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求中未包含communityId信息");
        Assert.jsonObjectHaveKey(reqJson, "page", "请求中未包含page信息");
        Assert.jsonObjectHaveKey(reqJson, "row", "请求中未包含row信息");
    }

    @Override
    public void doCmd(ICmdDataFlowContext context, JSONObject reqJson) {
        RoomDto roomDto = BeanConvertUtil.covertBean(reqJson, RoomDto.class);
        int row = reqJson.getInteger("row");


        // 查询总记录数
        int total = roomInnerServiceSMOImpl.queryRoomsCount(roomDto);
        List<RoomDto> roomDtos = null;
        if (total > 0) {
            // 查询房屋列表数据
            roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
        } else {
            roomDtos = new ArrayList<>();
        }
        // 转换数据格式并设置返回结果
        // 计算总页数

        // 构建HTTP响应
        // 构建HTTP响应实体，将API响应对象转换为JSON字符串
        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity((int) Math.ceil((double) total / (double) row), total, roomDtos);
        // 设置响应实体到数据流上下文
        context.setResponseEntity(responseEntity);
    }
}
