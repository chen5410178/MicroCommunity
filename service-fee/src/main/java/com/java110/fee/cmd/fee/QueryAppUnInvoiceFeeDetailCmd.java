package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.fee.IFeeDetailInnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserV1InnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 手机端查询未开票缴费明细（fee.queryAppUnInvoiceFeeDetail）
 * 从请求头 user-id 取登录用户，校验业主绑定后查询 open_invoice='N' 且已支付的缴费明细分页 + 未开票合计金额
 */
@Java110Cmd(serviceCode = "fee.queryAppUnInvoiceFeeDetail")
public class QueryAppUnInvoiceFeeDetailCmd extends Cmd {

    @Autowired
    private IOwnerAppUserV1InnerServiceSMO ownerAppUserV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");

        String userId = CmdContextUtils.getUserId(context);
        if (StringUtil.isEmpty(userId)) {
            throw new CmdException("用户未登录");
        }

        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setUserId(userId);
        ownerAppUserDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserV1InnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);
        if (ListUtil.isNull(ownerAppUserDtos)
                || OwnerAppUserDto.STATE_AUDIT_ERROR.equals(ownerAppUserDtos.get(0).getState())) {
            throw new CmdException("用户未认证,请先认证");
        }

        if (OwnerAppUserDto.STATE_AUDITING.equals(ownerAppUserDtos.get(0).getState())) {
            ResultVo resultVo = new ResultVo(-101, "认证审核中");
            context.setResponseEntity(ResultVo.createResponseEntity(resultVo));
            return;
        }

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(ownerAppUserDtos.get(0).getMemberId());
        ownerDto.setCommunityId(ownerAppUserDtos.get(0).getCommunityId());
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);
        if (ListUtil.isNull(ownerDtos)) {
            throw new CmdException("业主不存在");
        }

        reqJson.put("ownerId", ownerDtos.get(0).getOwnerId());
        reqJson.put("applyTel", ownerDtos.get(0).getLink());
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        FeeDetailDto feeDetailDto = BeanConvertUtil.covertBean(reqJson, FeeDetailDto.class);
        // 未开票且已支付
        feeDetailDto.setOpenInvoice("N");
        feeDetailDto.setState("1400");

        int total = feeDetailInnerServiceSMOImpl.queryFeeDetailsCount(feeDetailDto);
        List<FeeDetailDto> feeDetailDtos = null;
        if (total > 0) {
            feeDetailDtos = feeDetailInnerServiceSMOImpl.queryFeeDetails(feeDetailDto);
        } else {
            feeDetailDtos = new ArrayList<>();
        }

        // 未开票合计金额（同口径）
        double totalAmount = feeDetailInnerServiceSMOImpl.queryFeeDetailAmount(feeDetailDto);

        int row = reqJson.getInteger("row");
        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(
                (int) Math.ceil((double) total / (double) row),
                total, feeDetailDtos,
                String.format("%.2f", totalAmount));
        context.setResponseEntity(responseEntity);
    }
}
