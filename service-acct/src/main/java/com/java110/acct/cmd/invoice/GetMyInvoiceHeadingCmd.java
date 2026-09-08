package com.java110.acct.cmd.invoice;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.MallDataDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.job.IMallInnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 查询我的发票抬头(商城单条默认, 对齐IoT) 服务编码：invoice.getMyInvoiceHeading
 * 优先按业主手机号(ownerId+communityId), 其次取登录用户手机号; 转发商城 queryInvoiceHeadingBmoImpl
 */
@Java110Cmd(serviceCode = "invoice.getMyInvoiceHeading")
public class GetMyInvoiceHeadingCmd extends Cmd {

    @Autowired
    private IMallInnerServiceSMO mallInnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        String userPhone = getOwnerPhone(reqJson);
        if (StringUtil.isEmpty(userPhone)) {
            userPhone = getLoginPhone(context);
        }
        if (StringUtil.isEmpty(userPhone)) {
            throw new CmdException("未获取到业主手机号");
        }

        JSONObject paramIn = new JSONObject();
        paramIn.put("userPhone", userPhone);

        ResultVo resultVo = mallInnerServiceSMOImpl.postMallData(new MallDataDto("queryInvoiceHeadingBmoImpl", paramIn));
        if (resultVo.getCode() != ResultVo.CODE_OK) {
            throw new CmdException(resultVo.getMsg());
        }
        if (resultVo.getData() == null) {
            throw new CmdException("商城系统未部署");
        }
        context.setResponseEntity(ResultVo.createResponseEntity(resultVo));
    }

    /**
     * 优先取业主档案手机号(ownerId+communityId 查询)
     */
    private String getOwnerPhone(JSONObject reqJson) {
        if (!reqJson.containsKey("ownerId") || StringUtil.isEmpty(reqJson.getString("ownerId"))) {
            return "";
        }
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(reqJson.getString("ownerId"));
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);
        if (ListUtil.isNull(ownerDtos)) {
            return "";
        }
        return ownerDtos.get(0).getLink();
    }

    /**
     * 取登录用户手机号(与IoT GetMyInvoiceHeadingCmd 一致)
     */
    private String getLoginPhone(ICmdDataFlowContext context) {
        String userId = CmdContextUtils.getUserId(context);
        if (StringUtil.isEmpty(userId)) {
            return "";
        }
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        if (ListUtil.isNull(userDtos)) {
            return "";
        }
        return userDtos.get(0).getTel();
    }
}
