package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.payFee.PayFeeBatchDto;
import com.java110.dto.user.UserDto;
import com.java110.fee.bill.ICycleConvertOnceFee;
import com.java110.fee.bmo.fee.IFeeBMO;
import com.java110.fee.feeMonth.IPayFeeMonth;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.fee.*;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.payFee.PayFeeBatchPo;
import com.java110.po.payFee.PayFeeRulePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.*;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "fee.saveParkingSpaceCreateFee")
public class SaveParkingSpaceCreateFeeCmd extends Cmd {

    @Autowired
    private IFeeBMO feeBMOImpl;

    private static final int DEFAULT_ADD_FEE_COUNT = 200;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IPayFeeBatchV1InnerServiceSMO payFeeBatchV1InnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IPayFeeMonth payFeeMonthImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IRuleGeneratorPayFeeBillV1InnerServiceSMO ruleGeneratorPayFeeBillV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeRuleV1InnerServiceSMO payFeeRuleV1InnerServiceSMOImpl;

    @Autowired
    private ICycleConvertOnceFee cycleConvertOnceFeeImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        // super.validatePageInfo(pd);
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
        Assert.hasKeyAndValue(reqJson, "psId", "未包含收费对象");
        Assert.hasKeyAndValue(reqJson, "configId", "未包含收费项目");
        Assert.hasKeyAndValue(reqJson, "startTime", "未包含费用起始时间");
        Assert.hasKeyAndValue(reqJson, "endTime", "未包含费用结束时间");

        String endTime = reqJson.getString("endTime");
        if (!endTime.contains(":")) {
            endTime += " 23:59:59";
            reqJson.put("endTime", endTime);
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        List<ParkingSpaceDto> parkingSpaceDtos = null;
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(reqJson.getString("communityId"));
        feeConfigDto.setConfigId(reqJson.getString("configId"));
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        Assert.listOnlyOne(feeConfigDtos, "当前费用项ID不存在或存在多条" + reqJson.getString("configId"));
        reqJson.put("feeTypeCd", feeConfigDtos.get(0).getFeeTypeCd());
        reqJson.put("feeFlag", feeConfigDtos.get(0).getFeeFlag());
        reqJson.put("configEndTime", feeConfigDtos.get(0).getEndTime());

        //生成批次
        generatorBatch(reqJson);
        //判断收费范围
        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setCommunityId(reqJson.getString("communityId"));
        parkingSpaceDto.setPsId(reqJson.getString("psId"));
        parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);

        if (ListUtil.isNull(parkingSpaceDtos)) {
            throw new IllegalArgumentException("未查到需要收费的车位");
        }
        if (!ParkingSpaceDto.STATE_HIRE.equals(parkingSpaceDtos.get(0).getState())
                && !ParkingSpaceDto.STATE_SELL.equals(parkingSpaceDtos.get(0).getState())) {
            throw new IllegalArgumentException("当前车位未出租或出售，不能收费");
        }
        dealParkingSpaceFee(parkingSpaceDtos.get(0), cmdDataFlowContext, reqJson, event);
    }

    private void dealParkingSpaceFee(ParkingSpaceDto parkingSpaceDto, ICmdDataFlowContext context, JSONObject reqJson, CmdEvent event) {


        List<PayFeeRulePo> payFeeRulePos = new ArrayList<>();

        PayFeeRulePo payFeeRulePo = createPayFeeRulePo(parkingSpaceDto, reqJson);

        //todo 加入 房屋费用
        payFeeRulePos.add(payFeeRulePo);

        if (ListUtil.isNull(payFeeRulePos)) {
            throw new IllegalArgumentException("费用不存在");
        }

        String communityId = payFeeRulePos.get(0).getCommunityId();

        if (StringUtil.isEmpty(communityId)) {
            throw new IllegalArgumentException("小区不存在");
        }

        //todo 设置费用
        payFeeRuleV1InnerServiceSMOImpl.savePayFeeRules(payFeeRulePos);


        //todo 走账单模式
        cycleConvertOnceFeeImpl.rulesGeneratePayFees(payFeeRulePos);

        context.setResponseEntity(ResultVo.success());
    }




    private PayFeeRulePo createPayFeeRulePo(ParkingSpaceDto parkingSpaceDto, JSONObject paramInJson) {

        String time = DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A);
        if (paramInJson.containsKey("feeEndDate")) {
            time = paramInJson.getString("feeEndDate");
        } else if (paramInJson.containsKey("startTime")) {
            time = paramInJson.getString("startTime");
        }
        //查询费用项
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
        payFeeRulePo.setState(FeeDto.STATE_DOING);
        payFeeRulePo.setRuleId(GenerateCodeFactory.getGeneratorId("11"));
        payFeeRulePo.setCommunityId(paramInJson.getString("communityId"));
        payFeeRulePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_PARKING_SPACE);
        payFeeRulePo.setPayerObjId(parkingSpaceDto.getPsId());
        payFeeRulePo.setOwnerId(parkingSpaceDto.getOwnerId());
        payFeeRulePo.setOwnerName(parkingSpaceDto.getOwnerName());
        String payerObjName = parkingSpaceDto.getAreaNum() + "-" + parkingSpaceDto.getSectionName() + "-" + parkingSpaceDto.getNum();
        if (StringUtil.isEmpty(parkingSpaceDto.getSectionName())) {
            payerObjName = parkingSpaceDto.getAreaNum() + "-" + parkingSpaceDto.getNum();
        }
        payFeeRulePo.setPayerObjName(payerObjName);
        String curYearMonth  = DateUtil.getFormatTimeStringB(DateUtil.getDateFromStringB(payFeeRulePo.getEndTime()));
        payFeeRulePo.setCurYearMonth(curYearMonth);
        return payFeeRulePo;
    }

    /**
     * 生成批次号
     *
     * @param reqJson
     */
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
