package com.java110.common.bmo.mall.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.mall.IMallCommonApiBmo;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.common.ISmsInnerServiceSMO;
import com.java110.intf.community.ICommunityV1InnerServiceSMO;
import com.java110.intf.user.*;
import com.java110.po.owner.OwnerPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("saveOwnerMemberImpl")
public class SaveOwnerMemberImpl implements IMallCommonApiBmo {

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private ISmsInnerServiceSMO smsInnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerAttrInnerServiceSMO ownerAttrInnerServiceSMOImpl;


    @Autowired
    private ICommunityV1InnerServiceSMO communityV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerAppUserV1InnerServiceSMO ownerAppUserV1InnerServiceSMOImpl; // 业主APP用户V1版本服务接口

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Override
    public void validate(ICmdDataFlowContext context, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "name", "请求报文中未包含name");

        Assert.jsonObjectHaveKey(reqJson, "sex", "请求报文中未包含sex");
        // Assert.jsonObjectHaveKey(reqJson, "ownerId", "请求报文中未包含业主");
        if (!reqJson.containsKey("link") || StringUtil.isEmpty(reqJson.getString("link"))) {
            reqJson.put("link", GenerateCodeFactory.getLink());
        }
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId");

        String link = reqJson.getString("link");
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setLink(link);
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryAllOwners(ownerDto);
        Assert.listIsNull(ownerDtos, "手机号重复，请重新输入");


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

        reqJson.put("ownerId", ownerAppUserDtos.get(0).getMemberId());

    }

    @Override
    public void doCmd(ICmdDataFlowContext context, JSONObject reqJson) {
//todo 生成memberId
        UserDto userDto = new UserDto();
        userDto.setTel(reqJson.getString("tel"));
        userDto.setLevelCd(UserDto.LEVEL_CD_USER);

        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        if (ListUtil.isNull(userDtos)) {
            throw new CmdException("用户不存在");
        }

        String memberId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ownerId);
        reqJson.put("memberId", memberId);

        OwnerPo ownerPo = BeanConvertUtil.covertBean(reqJson, OwnerPo.class);

        String ownerPhotoUrl = reqJson.getString("ownerPhotoUrl");
        ownerPo.setFaceUrl(ownerPhotoUrl);
        ownerPo.setAge("0");
        ownerPo.setUserId(userDtos.get(0).getUserId());
        ownerPo.setState(OwnerDto.STATE_FINISH);
        ownerPo.setPersonType(OwnerDto.PERSON_TYPE_PERSON);
        ownerPo.setOwnerTypeCd(OwnerDto.OWNER_TYPE_CD_MEMBER);
        int flag = ownerV1InnerServiceSMOImpl.saveOwner(ownerPo);
        if (flag < 1) {
            throw new CmdException("保存业主失败");
        }
    }
}
