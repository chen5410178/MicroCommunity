package com.java110.fee.cmd.prePayFee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.feeType.FeeTypeDto;
import com.java110.intf.fee.IFeeTypeV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询水电费预存款
 */
@Java110Cmd(serviceCode = "prePayFee.queryPhoneMeterPayFeeRule")
public class QueryPhoneMeterPayFeeRuleCmd extends Cmd {

    @Autowired
    private IFeeTypeV1InnerServiceSMO feeTypeV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区编号");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        FeeTypeDto feeTypeDto = new FeeTypeDto();
        feeTypeDto.setCommunityId(reqJson.getString("communityId"));
        feeTypeDto.setFeeTypeCds(new String[]{FeeConfigDto.FEE_TYPE_CD_WATER, FeeConfigDto.FEE_TYPE_CD_METER, FeeConfigDto.FEE_TYPE_CD_GAS});
        List<FeeTypeDto> feeTypeDtos = feeTypeV1InnerServiceSMOImpl.queryFeeTypes(feeTypeDto);
        if (ListUtil.isNull(feeTypeDtos)) {
            feeTypeDtos = new ArrayList<>();
        }
        JSONArray datas = new JSONArray();
        JSONObject dataObj = null;
        for (FeeTypeDto tFeeTypeDto : feeTypeDtos) {
            dataObj = new JSONObject();
            dataObj.put("ftName", tFeeTypeDto.getFtName());
            dataObj.put("ftId", tFeeTypeDto.getFtId());
            dataObj.put("amount", "0");
            datas.add(dataObj);
        }
        context.setResponseEntity(ResultVo.createResponseEntity(datas));
    }
}
