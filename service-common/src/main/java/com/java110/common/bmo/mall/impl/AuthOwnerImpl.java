package com.java110.common.bmo.mall.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.mall.IMallCommonApiBmo;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.room.RoomDto;
import com.java110.dto.user.UserAttrDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.user.*;
import com.java110.po.owner.OwnerAppUserPo;
import com.java110.po.user.UserPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("authOwnerImpl")
public class AuthOwnerImpl implements IMallCommonApiBmo {


    @Autowired
    private IOwnerAppUserV1InnerServiceSMO ownerAppUserV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelV1InnerServiceSMO ownerRoomRelV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IUserAttrV1InnerServiceSMO userAttrV1InnerServiceSMOImpl;

    @Override
    public void validate(ICmdDataFlowContext context, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
        Assert.hasKeyAndValue(reqJson, "roomName", "未包含房屋");
        Assert.hasKeyAndValue(reqJson, "roomId", "未包含房屋");
        Assert.hasKeyAndValue(reqJson, "link", "未包含手机号");
        Assert.hasKeyAndValue(reqJson, "tel", "未包含用户手机号");
        Assert.hasKeyAndValue(reqJson, "ownerName", "未包含人员名称");
        Assert.hasKeyAndValue(reqJson, "ownerTypeCd", "未包含人员类型");

        // 检查是否已存在认证关系（审核中或审核成功状态）
        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setLink(reqJson.getString("link"));
        ownerAppUserDto.setCommunityId(reqJson.getString("communityId"));
        ownerAppUserDto.setStates(new String[]{
                OwnerAppUserDto.STATE_AUDITING,
                OwnerAppUserDto.STATE_AUDIT_SUCCESS
        });
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserV1InnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);

        if (!ListUtil.isNull(ownerAppUserDtos)) {
            throw new CmdException("已存在认证关系，请勿重复认证");
        }

        // 验证房屋信息是否存在
        RoomDto roomDto = new RoomDto();
        roomDto.setRoomId(reqJson.getString("roomId"));
        roomDto.setCommunityId(reqJson.getString("communityId"));
        List<RoomDto> roomDtos = roomV1InnerServiceSMOImpl.queryRooms(roomDto);
        Assert.listOnlyOne(roomDtos, "房屋不存在");

        // 检查房屋是否已销售（存在业主房屋关系）
        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setRoomId(roomDtos.get(0).getRoomId());
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelV1InnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);

        if (ListUtil.isNull(ownerRoomRelDtos)) {
            throw new CmdException("房屋未销售，请联系物业");
        }

        // 如果是业主身份认证，需要额外检查业主是否已被认证
        if (!OwnerDto.OWNER_TYPE_CD_OWNER.equals(reqJson.getString("ownerTypeCd"))) {
            return;
        }

        // 检查业主是否已被其他用户认证
        ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setMemberId(ownerRoomRelDtos.get(0).getOwnerId());
        ownerAppUserDto.setCommunityId(reqJson.getString("communityId"));
        ownerAppUserDto.setStates(new String[]{
                OwnerAppUserDto.STATE_AUDITING,
                OwnerAppUserDto.STATE_AUDIT_SUCCESS
        });
        ownerAppUserDtos = ownerAppUserV1InnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);

        if (!ListUtil.isNull(ownerAppUserDtos)) {
            throw new CmdException("业主已经被认证");
        }
    }

    @Override
    public void doCmd(ICmdDataFlowContext context, JSONObject reqJson) {

        UserDto userDto = new UserDto();
        userDto.setTel(reqJson.getString("tel"));
        userDto.setLevelCd(UserDto.LEVEL_CD_USER);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        // 如果用户不存在，则自动注册新用户
        if (ListUtil.isNull(userDtos)) {
            registerUser(reqJson.getString("tel"));
            userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        }
        // 查询小区信息
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(reqJson.getString("communityId"));
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);
        Assert.listNotNull(communityDtos, "未包含小区信息");
        CommunityDto tmpCommunityDto = communityDtos.get(0);

        // 构建业主认证申请对象
        OwnerAppUserPo ownerAppUserPo = new OwnerAppUserPo();
        // 设置认证状态为审核中
        ownerAppUserPo.setState(OwnerAppUserDto.STATE_AUDITING);
        // 设置应用类型编码
        ownerAppUserPo.setAppTypeCd("10010");
        // 生成唯一应用用户ID
        ownerAppUserPo.setAppUserId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_appUserId));
        // 初始成员ID设为-1，审核通过后更新为实际业主ID
        ownerAppUserPo.setMemberId("-1");
        // 设置小区信息
        ownerAppUserPo.setCommunityName(tmpCommunityDto.getName());
        ownerAppUserPo.setCommunityId(tmpCommunityDto.getCommunityId());
        // 设置申请人信息
        ownerAppUserPo.setAppUserName(reqJson.getString("ownerName"));
        ownerAppUserPo.setAppType("WECHAT");
        ownerAppUserPo.setLink(reqJson.getString("link"));
        ownerAppUserPo.setUserId(userDtos.get(0).getUserId());
        // 初始openId设为-1，后续从用户属性中获取
        ownerAppUserPo.setOpenId("-1");
        // 设置房屋信息
        ownerAppUserPo.setRoomId(reqJson.getString("roomId"));
        ownerAppUserPo.setRoomName(reqJson.getString("roomName"));
        // 设置业主类型
        ownerAppUserPo.setOwnerTypeCd(reqJson.getString("ownerTypeCd"));

        // 查询用户OpenID属性
        UserAttrDto userAttrDto = new UserAttrDto();
        userAttrDto.setUserId(userDtos.get(0).getUserId());
        userAttrDto.setSpecCd(UserAttrDto.SPEC_OPEN_ID);
        List<UserAttrDto> userAttrDtos = userAttrV1InnerServiceSMOImpl.queryUserAttrs(userAttrDto);
        // 如果存在OpenID，则设置到认证记录中
        if (!ListUtil.isNull(userAttrDtos)) {
            ownerAppUserPo.setOpenId(userAttrDtos.get(0).getValue());
        }

        // 保存业主认证申请记录
        ownerAppUserV1InnerServiceSMOImpl.saveOwnerAppUser(ownerAppUserPo);
    }


    /**
     * 注册新用户
     * 根据手机号创建新的用户账户
     *
     * @param phoneNumber 手机号码
     * @throws CmdException 注册失败时抛出异常
     */
    private void registerUser(String phoneNumber) {
        // 生成随机密码
        String userPassword = AuthenticationFactory.passwdMd5(GenerateCodeFactory.getRandomCode(6));

        // 创建用户信息
        UserPo userPo = new UserPo();
        userPo.setUserId(GenerateCodeFactory.getGeneratorId("30"));
        userPo.setName(phoneNumber);
        userPo.setTel(phoneNumber);
        userPo.setLevelCd(UserDto.LEVEL_CD_USER);
        userPo.setPassword(AuthenticationFactory.passwdMd5(userPassword));

        // 保存用户信息
        int flag = userV1InnerServiceSMOImpl.saveUser(userPo);
        if (flag < 1) {
            throw new CmdException("注册失败");
        }
    }
}
