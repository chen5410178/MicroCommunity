package com.java110.fee.cmd.feeDiscount;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.fee.ComputeDiscountDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.fee.PayFeeDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.fee.bill.IComputeFeeDiscount;
import com.java110.intf.fee.IPayFeeV1InnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserInnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "feeDiscount.computeAppFeeDiscount")
public class ComputeAppFeeDiscountCmd extends Cmd {

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;


    @Autowired
    private IComputeFeeDiscount computeFeeDiscountImpl;


    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区编号");
        Assert.hasKey(reqJson, "feeIds", "未包含费用编号");

        String userId = CmdContextUtils.getUserId(context);

        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setUserId(userId);
        ownerAppUserDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);

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

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        ownerDto.setMemberId(memberId);
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);

        Assert.listOnlyOne(ownerDtos, "业主不存在");

        reqJson.put("ownerId", ownerDtos.get(0).getOwnerId());
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        JSONArray feeIds = reqJson.getJSONArray("feeIds");

        PayFeeDto feeDto = new PayFeeDto();
        feeDto.setFeeIds(feeIds.toArray(new String[feeIds.size()]));
        feeDto.setCommunityId(reqJson.getString("communityId"));
        feeDto.setOwnerId(reqJson.getString("ownerId"));
        feeDto.setState(FeeDto.STATE_DOING);
        List<PayFeeDto> feeDtos = payFeeV1InnerServiceSMOImpl.queryPayFees(feeDto);
        if (ListUtil.isNull(feeDtos)) {
            return;
        }

        List<ComputeDiscountDto> computeDiscountDtos = computeFeeDiscountImpl.compute(feeDtos);
        context.setResponseEntity(ResultVo.createResponseEntity(computeDiscountDtos));
    }
}
