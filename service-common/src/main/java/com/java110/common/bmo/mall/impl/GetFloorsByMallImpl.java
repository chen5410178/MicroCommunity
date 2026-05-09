package com.java110.common.bmo.mall.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.mall.IMallCommonApiBmo;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.dto.floor.FloorDto;
import com.java110.intf.community.IFloorInnerServiceSMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getFloorsByMallImpl")
public class GetFloorsByMallImpl implements IMallCommonApiBmo {
    /**
     * 楼栋内部服务接口
     */
    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;
    @Override
    public void validate(ICmdDataFlowContext context, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "page", "请求中未包含page信息");
        Assert.jsonObjectHaveKey(reqJson, "row", "请求中未包含row信息");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求中未包含communityId信息");
    }

    @Override
    public void doCmd(ICmdDataFlowContext context, JSONObject reqJson) {
        int row = reqJson.getInteger("row");
        // String communityId = reqJson.getString("communityId"); // 注释掉的小区ID参数

        // 创建API响应对象

        // 查询总记录数 - 将请求参数转换为FloorDto对象进行查询
        int total = floorInnerServiceSMOImpl.queryFloorsCount(BeanConvertUtil.covertBean(reqJson, FloorDto.class));

        List<FloorDto> floorDtos = null;
        // 如果存在数据，则查询楼栋列表
        if (total > 0) {
            // 查询楼栋数据列表
            floorDtos = floorInnerServiceSMOImpl.queryFloors(BeanConvertUtil.covertBean(reqJson, FloorDto.class));
            // 将楼栋DTO列表转换为API数据VO列表
        }else{
            floorDtos = new ArrayList<>();
        }


        // 构建HTTP响应实体，将API响应对象转换为JSON字符串
        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity((int) Math.ceil((double) total / (double) row), total, floorDtos);
        // 设置响应实体到数据流上下文
        context.setResponseEntity(responseEntity);
    }
}
