package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.payFee.PayFeeBatchDto;
import com.java110.dto.payFee.PayFeeRuleDto;
import com.java110.dto.shop.BuildingShopDto;
import com.java110.dto.shop.BuildingShopOwnerDto;
import com.java110.dto.user.UserDto;
import com.java110.fee.bill.ICycleConvertOnceFee;
import com.java110.intf.community.IBuildingShopInnerServiceSMO;
import com.java110.intf.community.IBuildingShopOwnerInnerServiceSMO;
import com.java110.intf.fee.*;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.payFee.PayFeeBatchPo;
import com.java110.po.payFee.PayFeeRulePo;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Java110Cmd(serviceCode = "fee.saveShopCreateFee")
public class SaveShopCreateFeeCmd extends Cmd {

    @Autowired
    private IBuildingShopInnerServiceSMO buildingShopInnerServiceSMOImpl;

    @Autowired
    private IBuildingShopOwnerInnerServiceSMO buildingShopOwnerInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IPayFeeBatchV1InnerServiceSMO payFeeBatchV1InnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IPayFeeRuleV1InnerServiceSMO payFeeRuleV1InnerServiceSMOImpl;

    @Autowired
    private ICycleConvertOnceFee cycleConvertOnceFeeImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
        Assert.hasKeyAndValue(reqJson, "payerObjId", "未包含商铺ID");
        Assert.hasKeyAndValue(reqJson, "configId", "未包含收费项目");
        Assert.hasKeyAndValue(reqJson, "startTime", "未包含计费起始时间");
        Assert.hasKeyAndValue(reqJson, "endTime", "未包含计费结束时间");

        String endTime = reqJson.getString("endTime");
        if (!endTime.contains(":")) {
            endTime += " 23:59:59";
            reqJson.put("endTime", endTime);
        }

        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(reqJson.getString("communityId"));
        feeConfigDto.setConfigId(reqJson.getString("configId"));
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        Assert.listOnlyOne(feeConfigDtos, "当前费用项ID不存在或存在多条" + reqJson.getString("configId"));
        reqJson.put("feeTypeCd", feeConfigDtos.get(0).getFeeTypeCd());
        reqJson.put("feeFlag", feeConfigDtos.get(0).getFeeFlag());
        reqJson.put("configEndTime", feeConfigDtos.get(0).getEndTime());

        if (FeeConfigDto.COMPUTING_FORMULA_RANT_RATE.equals(feeConfigDtos.get(0).getComputingFormula())) {
            Assert.hasKeyAndValue(reqJson, "rateCycle", "未包含递增周期");
            Assert.hasKeyAndValue(reqJson, "rate", "未包含递增率");
            Assert.hasKeyAndValue(reqJson, "rateStartTime", "未包含递增开始时间");
            reqJson.put("configComputingFormula", feeConfigDtos.get(0).getComputingFormula());
        }

        // 判断物业费不能重复创建
        if (!FeeConfigDto.FEE_TYPE_CD_PROPERTY.equals(feeConfigDtos.get(0).getFeeTypeCd())) {
            return;
        }

        PayFeeRuleDto payFeeRuleDto = new PayFeeRuleDto();
        payFeeRuleDto.setConfigId(feeConfigDtos.get(0).getConfigId());
        payFeeRuleDto.setPayerObjId(reqJson.getString("payerObjId"));
        payFeeRuleDto.setState(FeeDto.STATE_DOING);
        List<PayFeeRuleDto> payFeeRuleDtos = payFeeRuleV1InnerServiceSMOImpl.queryPayFeeRules(payFeeRuleDto);
        if (ListUtil.isNull(payFeeRuleDtos)) {
            return;
        }
        Date aTime = DateUtil.getDateFromStringA(payFeeRuleDtos.get(0).getEndTime());
        Date bTime = DateUtil.getDateFromStringA(payFeeRuleDtos.get(0).getMaxTime());

        if (DateUtil.belongCalendar(DateUtil.getDateFromStringB(reqJson.getString("startTime")),
                aTime, bTime
        )) {
            throw new CmdException("存在重复费用不能创建");
        }

        if (DateUtil.belongCalendar(DateUtil.getDateFromStringA(reqJson.getString("endTime")),
                aTime, bTime
        )) {
            throw new CmdException("存在重复费用不能创建");
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        String userId = cmdDataFlowContext.getReqHeaders().get(CommonConstant.USER_ID);
        reqJson.put("userId", userId);
        String storeId = CmdContextUtils.getStoreId(cmdDataFlowContext);
        reqJson.put("storeId", storeId);

        // 生成批次号
        generatorBatch(reqJson);

        // 查询商铺信息
        BuildingShopDto buildingShopDto = new BuildingShopDto();
        buildingShopDto.setCommunityId(reqJson.getString("communityId"));
        buildingShopDto.setShopId(reqJson.getString("payerObjId"));
        List<BuildingShopDto> shopDtos = buildingShopInnerServiceSMOImpl.queryBuildingShops(buildingShopDto);

        if (ListUtil.isNull(shopDtos)) {
            throw new IllegalArgumentException("未查到需要付费的商铺");
        }

        dealShopFee(shopDtos.get(0), cmdDataFlowContext, reqJson, event);
    }

    private void dealShopFee(BuildingShopDto shopDto, ICmdDataFlowContext context, JSONObject reqJson, CmdEvent event) {
        // 先根据shopId查询商铺业主关联表获取ownerId
        BuildingShopOwnerDto shopOwnerDto = new BuildingShopOwnerDto();
        shopOwnerDto.setShopId(shopDto.getShopId());
        List<BuildingShopOwnerDto> shopOwnerDtos = buildingShopOwnerInnerServiceSMOImpl.queryBuildingShopOwners(shopOwnerDto);

        if (!ListUtil.isNull(shopOwnerDtos)) {
            // 再根据ownerId查询业主信息
            OwnerDto ownerDto = new OwnerDto();
            ownerDto.setOwnerId(shopOwnerDtos.get(0).getOwnerId());
            List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);

            if (!ListUtil.isNull(ownerDtos)) {
                shopDto.setOwnerId(ownerDtos.get(0).getOwnerId());
                shopDto.setOwnerName(ownerDtos.get(0).getName());
                shopDto.setLink(ownerDtos.get(0).getLink());
            }
        }

        List<PayFeeRulePo> payFeeRulePos = new ArrayList<>();

        PayFeeRulePo payFeeRulePo = createPayFeeRulePo(shopDto, reqJson);
        payFeeRulePos.add(payFeeRulePo);

        if (ListUtil.isNull(payFeeRulePos)) {
            throw new IllegalArgumentException("费用不存在");
        }

        String communityId = payFeeRulePos.get(0).getCommunityId();

        if (StringUtil.isEmpty(communityId)) {
            throw new IllegalArgumentException("小区不存在");
        }

        // 业务处理
        JSONObject paramOut = new JSONObject();
        paramOut.put("totalRoom", 1);
        paramOut.put("successRoom", 1);
        paramOut.put("errorRoom", 0);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(paramOut.toJSONString(), HttpStatus.OK);

        if (payFeeRulePos.isEmpty()) {
            context.setResponseEntity(responseEntity);
            return;
        }

        // 保存费用规则
        payFeeRuleV1InnerServiceSMOImpl.savePayFeeRules(payFeeRulePos);

        // 走账单模式
        cycleConvertOnceFeeImpl.rulesGeneratePayFees(payFeeRulePos);

        context.setResponseEntity(responseEntity);
    }

    private PayFeeRulePo createPayFeeRulePo(BuildingShopDto shopDto, JSONObject paramInJson) {
        String time = DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A);
        if (paramInJson.containsKey("feeEndDate")) {
            time = paramInJson.getString("feeEndDate");
        } else if (paramInJson.containsKey("startTime")) {
            time = paramInJson.getString("startTime");
        }

        // 查询费用项
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setConfigId(paramInJson.getString("configId"));
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        Assert.listOnlyOne(feeConfigDtos, "查询费用项错误！");

        PayFeeRulePo payFeeRulePo = new PayFeeRulePo();
        payFeeRulePo.setMaxTime(paramInJson.getString("endTime"));

        payFeeRulePo.setAmount("-1");
        if (!StringUtil.isEmpty(paramInJson.getString("amount"))) {
            payFeeRulePo.setAmount(paramInJson.getString("amount"));
        }
        payFeeRulePo.setFeeTypeCd(feeConfigDtos.get(0).getFeeTypeCd());
        payFeeRulePo.setBatchId(paramInJson.getString("batchId"));
        payFeeRulePo.setUserId("-1");
        payFeeRulePo.setIncomeObjId(paramInJson.getString("storeId"));
        payFeeRulePo.setConfigId(feeConfigDtos.get(0).getConfigId());
        payFeeRulePo.setFeeFlag(feeConfigDtos.get(0).getFeeFlag());
        payFeeRulePo.setStartTime(time);
        payFeeRulePo.setEndTime(time);
        payFeeRulePo.setDeductFrom(feeConfigDtos.get(0).getDeductFrom());
        payFeeRulePo.setState(FeeDto.STATE_DOING);
        payFeeRulePo.setRuleId(GenerateCodeFactory.getGeneratorId("11"));
        payFeeRulePo.setCommunityId(paramInJson.getString("communityId"));
        payFeeRulePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_SHOP);
        payFeeRulePo.setPayerObjId(shopDto.getShopId());
        payFeeRulePo.setOwnerId(shopDto.getOwnerId());
        payFeeRulePo.setOwnerName(shopDto.getOwnerName());
        payFeeRulePo.setPayerObjName(shopDto.getShopNum());

        String curYearMonth = DateUtil.getFormatTimeStringB(DateUtil.getDateFromStringB(payFeeRulePo.getEndTime()));
        // 后付费
        if (FeeConfigDto.PAYMENT_CD_AFTER.equals(feeConfigDtos.get(0).getPaymentCd())) {
            int ruleCycle = Integer.parseInt(feeConfigDtos.get(0).getPaymentCycle());
            Date endTime = DateUtil.getNextMonthDateB(payFeeRulePo.getEndTime(), ruleCycle);
            curYearMonth = DateUtil.getFormatTimeStringB(endTime);
        }
        payFeeRulePo.setCurYearMonth(curYearMonth);

        return payFeeRulePo;
    }

    private void generatorBatch(JSONObject reqJson) {
        PayFeeBatchPo payFeeBatchPo = new PayFeeBatchPo();
        payFeeBatchPo.setBatchId(GenerateCodeFactory.getGeneratorId("12"));
        payFeeBatchPo.setCommunityId(reqJson.getString("communityId"));
        payFeeBatchPo.setCreateUserId(reqJson.getString("userId"));
        UserDto userDto = new UserDto();
        userDto.setUserId(reqJson.getString("userId"));
        List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户不存在");
        payFeeBatchPo.setCreateUserName(userDtos.get(0).getUserName());
        payFeeBatchPo.setState(PayFeeBatchDto.STATE_NORMAL);
        payFeeBatchPo.setMsg("正常");
        int flag = payFeeBatchV1InnerServiceSMOImpl.savePayFeeBatch(payFeeBatchPo);

        if (flag < 1) {
            throw new IllegalArgumentException("生成批次失败");
        }

        reqJson.put("batchId", payFeeBatchPo.getBatchId());
    }
}