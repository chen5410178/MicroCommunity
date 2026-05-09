package com.java110.common.bmo.mall.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.mall.IMallCommonApiBmo;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.dto.IotDataDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.intf.job.IIotInnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("getOwnerVisitsImpl")
public class GetOwnerVisitsImpl implements IMallCommonApiBmo {

    /**
     * 物联网服务接口，用于调用物联网相关功能
     */
    @Autowired
    private IIotInnerServiceSMO iotInnerServiceSMOImpl;

    /**
     * 业主应用用户服务接口，用于查询业主房屋关联信息
     */
    @Autowired
    private IOwnerAppUserV1InnerServiceSMO ownerAppUserV1InnerServiceSMOImpl;

    @Override
    public void validate(ICmdDataFlowContext context, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
        Assert.hasKeyAndValue(reqJson, "tel", "未包含手机号");
    }

    @Override
    public void doCmd(ICmdDataFlowContext context, JSONObject reqJson) {

        // 构建业主应用用户查询条件
        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setLink(reqJson.getString("tel")); // 设置用户ID
        ownerAppUserDto.setCommunityId(reqJson.getString("communityId")); // 设置小区ID

        // 查询用户关联的房屋信息
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserV1InnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);

        // 验证用户是否已认证房屋，如果未认证则抛出异常
        if (ListUtil.isNull(ownerAppUserDtos)) {
            throw new CmdException("未认证房屋");
        }

        // 将业主ID添加到请求参数中，用于后续查询
        reqJson.put("ownerId", ownerAppUserDtos.get(0).getMemberId());

        // 构建物联网数据请求对象并调用物联网服务获取访客记录
        IotDataDto iotDataDto = new IotDataDto("listVisitBmoImpl", reqJson);
        ResultVo resultVo = iotInnerServiceSMOImpl.postIotData(iotDataDto);

        // 设置响应结果到上下文
        context.setResponseEntity(ResultVo.createResponseEntity(resultVo));
    }
}
