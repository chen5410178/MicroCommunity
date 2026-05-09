package com.java110.common.bmo.mall.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.mall.IMallCommonApiBmo;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.dto.IotDataDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.intf.job.IIotInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("getAppCarMonthCardImpl")
public class GetAppCarMonthCardImpl implements IMallCommonApiBmo {
    /**
     * 物联网服务接口，用于调用物联网相关业务
     */
    @Autowired
    private IIotInnerServiceSMO iotInnerServiceSMOImpl;

    /**
     * 车主车辆信息服务接口，用于查询车辆相关信息
     */
    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;
    @Override
    public void validate(ICmdDataFlowContext context, JSONObject reqJson) {
        // 调用父类的分页信息验证方法
        Assert.hasKeyAndValue(reqJson, "page", "未包含page");
        Assert.hasKeyAndValue(reqJson, "row", "未包含row");
        // 验证请求中必须包含小区ID参数
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
        // 验证请求中必须包含车辆ID参数
        Assert.hasKeyAndValue(reqJson, "carId", "未包含carId");
    }

    @Override
    public void doCmd(ICmdDataFlowContext context, JSONObject reqJson) {
        // 创建车辆信息查询对象
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        // 设置车辆ID，从请求参数中获取
        ownerCarDto.setMemberId(reqJson.getString("carId"));
        // 设置小区ID，从请求参数中获取
        ownerCarDto.setCommunityId(reqJson.getString("communityId"));

        // 调用车辆信息服务查询车辆信息
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

        // 检查查询结果是否为空，如果为空则抛出异常
        if (ListUtil.isNull(ownerCarDtos)) {
            throw new CmdException("车辆不存在");
        }

        // 将查询到的车辆区域编号添加到请求参数中，供后续物联网服务使用
        reqJson.put("paNum", ownerCarDtos.get(0).getAreaNum());

        // 创建物联网数据传输对象，设置服务标识和请求参数
        IotDataDto iotDataDto = new IotDataDto("queryCarMonthCardBmoImpl", reqJson);
        // 调用物联网服务查询月卡信息
        ResultVo data = iotInnerServiceSMOImpl.postIotData(iotDataDto);

        // 将查询结果设置到响应上下文中
        context.setResponseEntity(ResultVo.createResponseEntity(data));
    }
}
