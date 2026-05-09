package com.java110.common.bmo.mall.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.mall.IMallCommonApiBmo;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.user.IOwnerAppUserInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getAppOwnerCarsImpl")
public class GetAppOwnerCarsImpl implements IMallCommonApiBmo {
    /**
     * 业主应用用户内部服务接口
     * 用于查询业主与用户的绑定关系
     */
    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;

    /**
     * 业主V1内部服务接口
     * 用于查询业主基本信息
     */
    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    /**
     * 业主车辆内部服务接口
     * 用于查询业主车辆信息
     */
    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Override
    public void validate(ICmdDataFlowContext context, JSONObject reqJson) {
        // 调用父类的分页信息验证方法
        Assert.hasKeyAndValue(reqJson, "page", "未包含page");
        Assert.hasKeyAndValue(reqJson, "row", "未包含row");

        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setLink(reqJson.getString("mallTel"));
        ownerAppUserDto.setCommunityId(reqJson.getString("communityId"));
        ownerAppUserDto.setMemberId(reqJson.getString("memberId"));

        // 查询用户与业主的绑定关系
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);

        // 检查是否绑定业主，未绑定则抛出异常
        if (ListUtil.isNull(ownerAppUserDtos)) {
            throw new CmdException("未绑定业主");
        }

        String memberId = "";
        // 遍历绑定关系，查找有效的成员ID
        for (OwnerAppUserDto tmpOwnerAppUserDto : ownerAppUserDtos) {
            // 跳过无效的成员ID（-1表示无效）
            if ("-1".equals(tmpOwnerAppUserDto.getMemberId())) {
                continue;
            }

            memberId = tmpOwnerAppUserDto.getMemberId();
        }

        // 检查是否找到有效的成员ID
        if (StringUtil.isEmpty(memberId)) {
            throw new CmdException("未绑定业主");
        }

        // 构建业主查询条件
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        ownerDto.setMemberId(memberId);

        // 查询业主信息
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);

        // 验证业主信息存在且唯一
        Assert.listOnlyOne(ownerDtos, "业主不存在");

        // 将业主ID设置到请求参数中，供后续查询使用
        reqJson.put("ownerId", ownerDtos.get(0).getOwnerId());
    }

    @Override
    public void doCmd(ICmdDataFlowContext context, JSONObject reqJson) {
// 将请求参数转换为业主车辆查询对象
        OwnerCarDto ownerCarDto = BeanConvertUtil.covertBean(reqJson, OwnerCarDto.class);

        // 查询业主车辆总数
        int total = ownerCarInnerServiceSMOImpl.queryOwnerCarsCount(ownerCarDto);

        List<OwnerCarDto> ownerCarDtos = null;
        // 根据查询结果数量决定是否查询详细数据
        if (total > 0) {
            // 查询业主车辆详细列表
            ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
        } else {
            // 如果没有数据，返回空列表
            ownerCarDtos = new ArrayList<>();
        }

        // 获取每页显示行数
        int row = reqJson.getIntValue("row");

        // 计算总页数并创建响应实体
        // 使用Math.ceil确保向上取整，正确处理余数
        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity((int) Math.ceil((double) total / (double) row),
                total, ownerCarDtos);

        // 设置响应实体到上下文
        context.setResponseEntity(responseEntity);
    }
}
