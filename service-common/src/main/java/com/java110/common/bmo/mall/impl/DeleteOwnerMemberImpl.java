package com.java110.common.bmo.mall.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.mall.IMallCommonApiBmo;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.user.*;
import com.java110.po.owner.OwnerAppUserPo;
import com.java110.po.owner.OwnerPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("deleteOwnerMemberImpl")
public class DeleteOwnerMemberImpl implements IMallCommonApiBmo {
    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl; // 业主信息服务接口

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl; // 业主V1版本服务接口

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl; // 房间信息服务接口

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl; // 业主车辆信息服务接口

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl; // 业主APP用户信息服务接口

    @Autowired
    private IOwnerAppUserV1InnerServiceSMO ownerAppUserV1InnerServiceSMOImpl; // 业主APP用户V1版本服务接口

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl; // 用户V1版本服务接口
    @Override
    public void validate(ICmdDataFlowContext context, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求中未包含communityId信息");
        Assert.jsonObjectHaveKey(reqJson, "tel", "请求中未包含tel信息");
        Assert.jsonObjectHaveKey(reqJson, "memberId", "请求报文中未包含memberId");

        // 构建业主APP用户查询条件
        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setLink(reqJson.getString("tel"));
        ownerAppUserDto.setCommunityId(reqJson.getString("communityId"));
        // 查询当前用户的业主APP用户信息
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserV1InnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);

        // 验证业主APP用户是否存在
        if (ListUtil.isNull(ownerAppUserDtos)) {
            throw new CmdException("业主不存在");
        }

        // 验证业主成员ID是否为空
        if (StringUtil.isEmpty(ownerAppUserDtos.get(0).getMemberId())) {
            throw new CmdException("业主不存在");
        }

        // 构建业主成员查询条件
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(reqJson.getString("memberId")); // 设置要删除的成员ID
        ownerDto.setCommunityId(reqJson.getString("communityId")); // 设置小区ID
        ownerDto.setOwnerId(ownerAppUserDtos.get(0).getMemberId()); // 设置业主ID
        ownerDto.setOwnerTypeCds(new String[]{OwnerDto.OWNER_TYPE_CD_MEMBER}); // 设置业主类型为成员

        // 查询业主成员信息
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);

        // 验证业主成员是否存在且唯一
        Assert.listOnlyOne(ownerDtos, "业主成员不存在");
    }

    @Override
    public void doCmd(ICmdDataFlowContext context, JSONObject reqJson) {
        JSONObject businessOwner = new JSONObject();
        businessOwner.put("memberId", reqJson.getString("memberId"));
        businessOwner.put("communityId", reqJson.getString("communityId"));

        // 将JSON对象转换为PO对象
        OwnerPo ownerPo = BeanConvertUtil.covertBean(businessOwner, OwnerPo.class);
        // 执行删除业主操作
        int flag = ownerV1InnerServiceSMOImpl.deleteOwner(ownerPo);

        // 检查删除操作是否成功
        if (flag < 1) {
            throw new CmdException("删除失败");
        }

        // 构建业主APP用户查询条件
        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setMemberId(reqJson.getString("memberId"));
        ownerAppUserDto.setCommunityId(reqJson.getString("communityId"));
        // 查询关联的APP用户信息
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);

        // 如果没有关联的APP用户，直接返回
        if (ListUtil.isNull(ownerAppUserDtos)) {
            return;
        }

        // 遍历所有关联的APP用户，逐个删除
        for (OwnerAppUserDto ownerAppUser : ownerAppUserDtos) {
            // 将DTO对象转换为PO对象
            OwnerAppUserPo ownerAppUserPo = BeanConvertUtil.covertBean(ownerAppUser, OwnerAppUserPo.class);
            // 执行删除APP用户操作
            flag = ownerAppUserV1InnerServiceSMOImpl.deleteOwnerAppUser(ownerAppUserPo);

            // 检查删除操作是否成功
            if (flag < 1) {
                throw new CmdException("删除失败");
            }

            // TODO: 应该删除用户，但目前注释掉了相关代码
            // UserPo userPo = new UserPo();
            // userPo.setUserId(ownerAppUser.getUserId());
            // userV1InnerServiceSMOImpl.deleteUser(userPo);
        }
    }
}
