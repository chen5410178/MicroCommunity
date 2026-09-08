package com.java110.acct.cmd.invoice;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.MallDataDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.job.IMallInnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 新增/编辑发票抬头(商城) 服务编码：invoice.saveInvoiceHeading
 * 转发商城 saveInvoiceHeadingBmoImpl(按 phone+headingType upsert, 存在更新/不存在新增)
 * 抬头归属手机号(userPhone)优先取业主档案手机号(owner.link)
 */
@Java110Cmd(serviceCode = "invoice.saveInvoiceHeading")
public class SaveInvoiceHeadingCmd extends Cmd {

    @Autowired
    private IMallInnerServiceSMO mallInnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "headingType", "请求报文中未包含抬头类型");
        Assert.hasKeyAndValue(reqJson, "headingName", "请求报文中未包含抬头名称");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        // 1. 取归属手机号: 优先业主档案 link, 其次请求报文 userPhone, 最后 phone
        String userPhone = reqJson.getString("userPhone");
        if (StringUtil.isEmpty(userPhone) && reqJson.containsKey("ownerId")
                && !StringUtil.isEmpty(reqJson.getString("ownerId"))) {
            OwnerDto ownerDto = new OwnerDto();
            ownerDto.setMemberId(reqJson.getString("ownerId"));
            ownerDto.setCommunityId(reqJson.getString("communityId"));
            List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);
            if (!ListUtil.isNull(ownerDtos)) {
                userPhone = ownerDtos.get(0).getLink();
            }
        }
        if (StringUtil.isEmpty(userPhone)) {
            userPhone = reqJson.getString("phone");
        }
        if (StringUtil.isEmpty(userPhone)) {
            throw new CmdException("业主手机号不能为空, 请选择业主后重试");
        }

        // 2. 类型转换 1001-个人 2002-公司 -> 商城 个人/公司
        String headingType = reqJson.getString("headingType");
        if ("1001".equals(headingType)) {
            headingType = "个人";
        } else if ("2002".equals(headingType)) {
            headingType = "公司";
        }

        JSONObject paramIn = new JSONObject();
        paramIn.put("headingType", headingType);
        paramIn.put("headingName", reqJson.getString("headingName"));
        paramIn.put("taxNo", reqJson.getString("taxNo"));
        paramIn.put("phone", userPhone);
        paramIn.put("userPhone", userPhone);
        paramIn.put("email", reqJson.getString("email"));
        paramIn.put("address", reqJson.getString("address"));
        paramIn.put("bankNo", reqJson.getString("bankNo"));
        paramIn.put("remark", reqJson.getString("remark"));
        paramIn.put("isDefault", reqJson.getString("isDefault"));

        ResultVo resultVo = mallInnerServiceSMOImpl.postMallData(new MallDataDto("saveInvoiceHeadingBmoImpl", paramIn));
        if (resultVo.getCode() != ResultVo.CODE_OK) {
            throw new CmdException(resultVo.getMsg());
        }
        if (resultVo.getData() == null) {
            throw new CmdException("商城系统未部署");
        }
        context.setResponseEntity(ResultVo.createResponseEntity(resultVo));
    }
}
