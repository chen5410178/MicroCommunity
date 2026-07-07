package com.java110.common.bmo.mall.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.mall.IMallCommonApiBmo;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.dto.account.AccountDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserV1InnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.*;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("getOwnerOweFeeImpl")
public class GetOwnerOweFeeImpl implements IMallCommonApiBmo {

    /**
     * 业主应用用户V1版本内部服务
     */
    @Autowired
    private IOwnerAppUserV1InnerServiceSMO ownerAppUserV1InnerServiceSMOImpl;

    /**
     * 业主V1版本内部服务
     */
    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;

    /**
     * 欠费查询业务接口
     */
    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Override
    public void validate(ICmdDataFlowContext context, JSONObject reqJson) {
// 验证必填参数：小区ID
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
        // 调用父类分页信息验证
        Assert.jsonObjectHaveKey(reqJson, "page", "请求中未包含page信息");
        Assert.jsonObjectHaveKey(reqJson, "row", "请求中未包含row信息");

        // 从上下文中获取当前用户ID
        // 构建业主应用用户查询条件
        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setLink(reqJson.getString("mallTel"));
        ownerAppUserDto.setCommunityId(reqJson.getString("communityId"));

        // 查询业主应用用户信息
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserV1InnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);

        // 验证用户认证状态
        if (ListUtil.isNull(ownerAppUserDtos)
                || OwnerAppUserDto.STATE_AUDIT_ERROR.equals(ownerAppUserDtos.get(0).getState())) {
            throw new CmdException("用户未认证,请先认证");
        }

        // 检查认证审核状态
        if (OwnerAppUserDto.STATE_AUDITING.equals(ownerAppUserDtos.get(0).getState())) {
            ResultVo resultVo = new ResultVo(-101, "认证审核中");
            context.setResponseEntity(ResultVo.createResponseEntity(resultVo));
            return;
        }

        // 构建业主查询条件
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(ownerAppUserDtos.get(0).getMemberId());
        ownerDto.setCommunityId(ownerAppUserDtos.get(0).getCommunityId());
        // 查询业主信息
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);

        // 验证业主是否存在
        if (ListUtil.isNull(ownerDtos)) {
            throw new CmdException("业主不存在");
        }

        // 获取第一个业主信息
        ownerDto = ownerDtos.get(0);
        // 将业主ID设置到请求参数中，供后续处理使用
        reqJson.put("ownerId", ownerDto.getOwnerId());
    }

    @Override
    public void doCmd(ICmdDataFlowContext context, JSONObject reqJson) {
        // 将请求参数转换为FeeDto对象
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
            fee.put("startTime", DateUtil.getFormatTimeStringB(feeDto.getStartTime()));
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
