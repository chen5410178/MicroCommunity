package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.account.AccountDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserInnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.*;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 查询业主费用(业主端专用)
 */
@Java110Cmd(serviceCode = "fee.listAppOwnerFee")
public class ListAppOwnerFeeCmd extends Cmd {

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
        String userId = context.getReqHeaders().get("user-id");

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

        FeeDto feeDto = new FeeDto();
        feeDto.setOwnerId(reqJson.getString("ownerId"));
        feeDto.setCommunityId(reqJson.getString("communityId"));
        feeDto.setPayerObjType(reqJson.getString("payerObjType"));
        feeDto.setState(FeeDto.STATE_DOING);
        int count = feeInnerServiceSMOImpl.queryFeesCount(feeDto);
        List<FeeDto> feeDtos = null;
        if (count > 0) {
            feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);//查询费用项目
        } else {
            feeDtos = new ArrayList<>();
        }

        if (ListUtil.isNull(feeDtos)) {
            context.setResponseEntity(ResultVo.createResponseEntity(feeDtos));
            return;
        }

        //todo 查询账户余额
        AccountDto accountDto = new AccountDto();
        accountDto.setObjId(reqJson.getString("ownerId"));
        accountDto.setCommunityId(reqJson.getString("communityId"));
        List<AccountDto> accountDtos = accountInnerServiceSMOImpl.queryAccounts(accountDto);
        double acctAmount = 0.0;
        if (!ListUtil.isNull(accountDtos)) {
            BigDecimal accountDec = new BigDecimal("0");
            for (AccountDto tmpAccountDto : accountDtos) {
                accountDec = accountDec.add(new BigDecimal(tmpAccountDto.getAmount()));
            }
            accountDec = accountDec.setScale(2, BigDecimal.ROUND_HALF_UP);
            acctAmount = accountDec.doubleValue();
        }

        JSONArray feeObjs = new JSONArray();
        JSONObject feeObj = null;
        for (FeeDto tFeeDto : feeDtos) {
            if (hasInFeeObj(tFeeDto, feeObjs)) {
                continue;
            }
            feeObj = new JSONObject();
            feeObj.put("payerObjName", tFeeDto.getPayerObjName());
            feeObj.put("payerObjId", tFeeDto.getPayerObjId());
            feeObj.put("payerObjType", tFeeDto.getPayerObjType());
            feeObj.put("acctAmount", acctAmount);
            feeObj.put("integralQuantity", "0");

            feeObj.put("ownerId", tFeeDto.getOwnerId());
            feeObj.put("ownerName", tFeeDto.getOwnerName());
            feeObj.put("childs", new JSONArray());
            feeObjs.add(feeObj);
        }

        for (int fIndex = 0; fIndex < feeObjs.size(); fIndex++) {
            feeObj = feeObjs.getJSONObject(fIndex);
            createFeeObjFeeConfigs(feeObj, feeDtos);
        }


        context.setResponseEntity(ResultVo.createResponseEntity(feeObjs));
    }

    /**
     * 房屋下挂费用项
     *
     * @param feeObj
     * @param feeDtos
     */
    private void createFeeObjFeeConfigs(JSONObject feeObj, List<FeeDto> feeDtos) {
        JSONArray feeConfigs = feeObj.getJSONArray("childs");
        JSONObject feeConfig = null;
        for (FeeDto feeDto : feeDtos) {
            if (!feeObj.getString("payerObjId").equals(feeDto.getPayerObjId())) {
                continue;
            }
            if (hasInFeeConfig(feeDto, feeConfigs)) {
                continue;
            }
            feeConfig = new JSONObject();
            feeConfig.put("feeName", feeDto.getFeeName());
            feeConfig.put("configId", feeDto.getConfigId());
            feeConfig.put("payerObjId", feeDto.getPayerObjId());
            feeConfig.put("childs", new JSONArray());
            feeConfigs.add(feeConfig);
        }

        BigDecimal oweAmount = new BigDecimal("0");
        for (int fIndex = 0; fIndex < feeConfigs.size(); fIndex++) {
            feeConfig = feeConfigs.getJSONObject(fIndex);
            createFeeObjFee(feeConfig, feeDtos);
            oweAmount = oweAmount.add(new BigDecimal(feeConfig.getString("oweAmount")));
        }
        feeObj.put("oweAmount", oweAmount.doubleValue());

    }

    private void createFeeObjFee(JSONObject feeConfig, List<FeeDto> feeDtos) {
        JSONArray fees = feeConfig.getJSONArray("childs");
        JSONObject fee = null;
        for (FeeDto feeDto : feeDtos) {
            if (!feeConfig.getString("payerObjId").equals(feeDto.getPayerObjId())) {
                continue;
            }
            if (!feeConfig.getString("configId").equals(feeDto.getConfigId())) {
                continue;
            }
            if (hasInFee(feeDto, fees)) {
                continue;
            }
            fee = BeanConvertUtil.beanCovertJson(feeDto);
            fee.put("startTime",DateUtil.getFormatTimeStringB(feeDto.getStartTime()));
            fee.put("endTime",DateUtil.getFormatTimeStringB(feeDto.getEndTime()));
            fees.add(fee);
        }

        BigDecimal oweAmount = new BigDecimal("0");

        Date startTime = null;
        Date endTime = null;
        Date sTime = null;
        Date eTime = null;
        for (int fIndex = 0; fIndex < fees.size(); fIndex++) {
            fee = fees.getJSONObject(fIndex);
            oweAmount = oweAmount.add(new BigDecimal(fee.getString("oweAmount")));
            sTime = DateUtil.getDateFromStringB(fee.getString("startTime"));
            eTime = DateUtil.getDateFromStringB(fee.getString("endTime"));
            if (startTime == null) {
                startTime = sTime;
                endTime = eTime;
                continue;
            }

            if (sTime.before(startTime)) {
                startTime = sTime;
            }
            if (eTime.after(endTime)) {
                endTime = eTime;
            }

        }

        feeConfig.put("oweAmount", oweAmount.doubleValue());
        feeConfig.put("startTime",DateUtil.getFormatTimeStringB(startTime));
        feeConfig.put("endTime",DateUtil.getFormatTimeStringB(endTime));
    }

    private boolean hasInFeeObj(FeeDto tFeeDto, JSONArray feeObjs) {
        if (ListUtil.isNull(feeObjs)) {
            return false;
        }

        for (int fIndex = 0; fIndex < feeObjs.size(); fIndex++) {
            if (tFeeDto.getPayerObjId().equals(feeObjs.getJSONObject(fIndex).getString("payerObjId"))) {
                return true;
            }
        }


        return false;
    }

    private boolean hasInFeeConfig(FeeDto feeDto, JSONArray feeConfigs) {
        if (ListUtil.isNull(feeConfigs)) {
            return false;
        }

        for (int fIndex = 0; fIndex < feeConfigs.size(); fIndex++) {
            if (feeDto.getConfigId().equals(feeConfigs.getJSONObject(fIndex).getString("configId"))) {
                return true;
            }
        }
        return false;
    }

    private boolean hasInFee(FeeDto feeDto, JSONArray fees) {
        if (ListUtil.isNull(fees)) {
            return false;
        }

        for (int fIndex = 0; fIndex < fees.size(); fIndex++) {
            if (feeDto.getFeeId().equals(fees.getJSONObject(fIndex).getString("feeId"))) {
                return true;
            }
        }
        return false;
    }
}
