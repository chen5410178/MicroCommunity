package com.java110.acct.cmd.invoice;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.MallDataDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.job.IMallInnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserV1InnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 手机端查询发票抬头（invoice.queryAppInvoiceHeader）
 * 从请求头 user-id 取登录用户校验业主绑定，按业主档案手机号(link) 转发商城查询抬头；
 * 手机号由服务端从登录态推导，不信任前端传入的 userPhone，防止越权查询他人抬头
 */
@Java110Cmd(serviceCode = "invoice.queryAppInvoiceHeader")
public class QueryAppInvoiceHeaderCmd extends Cmd {

    @Autowired
    private IOwnerAppUserV1InnerServiceSMO ownerAppUserV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IMallInnerServiceSMO mallInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");

        String userId = CmdContextUtils.getUserId(context);
        if (StringUtil.isEmpty(userId)) {
            throw new CmdException("用户未登录");
        }

        // 1. 校验业主绑定（取 memberId，跳过 memberId='-1' 的助手绑定）
        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setUserId(userId);
        ownerAppUserDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserV1InnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);
        if (ListUtil.isNull(ownerAppUserDtos)) {
            throw new CmdException("未绑定业主");
        }

        String memberId = "";
        for (OwnerAppUserDto tmpOwnerAppUserDto : ownerAppUserDtos) {
            if ("-1".equals(tmpOwnerAppUserDto.getMemberId())) {
                continue;
            }
            memberId = tmpOwnerAppUserDto.getMemberId();
        }

        if (StringUtil.isEmpty(memberId)) {
            throw new CmdException("未绑定业主");
        }

        // 2. 查业主档案，取手机号（登录用户手机号）
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        ownerDto.setMemberId(memberId);
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);

        Assert.listOnlyOne(ownerDtos, "业主不存在");

        // 以服务端推导手机号覆盖前端传参，不信任 reqJson 中的 userPhone
        reqJson.put("userPhone", ownerDtos.get(0).getLink());
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        // 3. 按登录用户手机号转发商城查询抬头（同 QueryInvoiceHeadingsCmd）
        JSONObject paramIn = new JSONObject();
        paramIn.put("userPhone", reqJson.getString("userPhone"));
        paramIn.put("keyword", reqJson.getString("keyword"));
        paramIn.put("page", reqJson.getInteger("page"));
        paramIn.put("row", reqJson.getInteger("row"));

        ResultVo resultVo = mallInnerServiceSMOImpl.postMallData(new MallDataDto("listInvoiceHeadingsBmoImpl", paramIn));
        if (resultVo.getCode() != ResultVo.CODE_OK) {
            throw new CmdException(resultVo.getMsg());
        }
        if (resultVo.getData() == null) {
            throw new CmdException("商城系统未部署");
        }
        context.setResponseEntity(ResultVo.createResponseEntity((int) resultVo.getRecords(), (int) resultVo.getTotal(), resultVo.getData()));
    }
}
