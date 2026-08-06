package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.contract.ContractDto;
import com.java110.dto.contract.ContractRoomDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.payFee.PayFeeBatchDto;
import com.java110.dto.user.UserDto;
import com.java110.fee.bill.ICycleConvertOnceFee;
import com.java110.fee.bmo.fee.IFeeBMO;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.fee.*;
import com.java110.intf.store.IContractInnerServiceSMO;
import com.java110.intf.store.IContractRoomInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.payFee.PayFeeBatchPo;
import com.java110.po.payFee.PayFeeRulePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "fee.saveContractCreateFee")
public class SaveContractCreateFeeCmd extends Cmd {

    @Autowired
    private IFeeBMO feeBMOImpl;

    private static final int DEFAULT_ADD_FEE_COUNT = 200;

    @Autowired
    private IContractInnerServiceSMO contractInnerServiceSMOImpl;

    @Autowired
    private IContractRoomInnerServiceSMO contractRoomInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IPayFeeBatchV1InnerServiceSMO payFeeBatchV1InnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

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
        List<ContractDto> contractDtos = null;
        String storeId = CmdContextUtils.getStoreId(cmdDataFlowContext);
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
        ContractDto contractDto = new ContractDto();
        contractDto.setContractId(reqJson.getString("contractId"));
        contractDto.setStoreId(storeId);
        contractDtos = contractInnerServiceSMOImpl.queryContracts(contractDto);

        if (ListUtil.isNull(contractDtos)) {
            throw new IllegalArgumentException("未查到需要付费的合同");
        }
        
        dealContractFee(contractDtos, cmdDataFlowContext, reqJson, event);
    }

    private void dealContractFee(List<ContractDto> contractDtos, ICmdDataFlowContext context, JSONObject reqJson, CmdEvent event) {

        List<PayFeeRulePo> payFeeRulePos = new ArrayList<>();
        ResponseEntity<String> responseEntity = null;

        for (int roomIndex = 0; roomIndex < contractDtos.size(); roomIndex++) {
            PayFeeRulePo payFeeRulePo = createPayFeeRulePo(contractDtos.get(roomIndex), reqJson);
            payFeeRulePos.add(payFeeRulePo);
        }

        if (ListUtil.isNull(payFeeRulePos)) {
            throw new IllegalArgumentException("费用不存在");
        }

        String communityId = payFeeRulePos.get(0).getCommunityId();

        if (StringUtil.isEmpty(communityId)) {
            throw new IllegalArgumentException("小区不存在");
        }

        //todo 业务处理
        JSONObject paramOut = new JSONObject();
        paramOut.put("totalContract", contractDtos.size());
        paramOut.put("successContract", contractDtos.size());
        paramOut.put("errorContract", 0);
        paramOut.put("code", 0);
        paramOut.put("msg", "成功");
        responseEntity = new ResponseEntity<>(paramOut.toJSONString(), HttpStatus.OK);

        if (payFeeRulePos.isEmpty()) {
            context.setResponseEntity(responseEntity);
            return;
        }

        //todo 设置费用
        payFeeRuleV1InnerServiceSMOImpl.savePayFeeRules(payFeeRulePos);

        //todo 走账单模式
        cycleConvertOnceFeeImpl.rulesGeneratePayFees(payFeeRulePos);

        context.setResponseEntity(responseEntity);
    }

    private PayFeeRulePo createPayFeeRulePo(ContractDto contractDto, JSONObject paramInJson) {

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
        payFeeRulePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_CONTRACT);
        payFeeRulePo.setPayerObjId(contractDto.getContractId());
        //todo 合同冗余业主信息：查合同关联房屋业主，无业主场景（如车位合同）置空
        ContractRoomDto contractRoomDto = new ContractRoomDto();
        contractRoomDto.setContractId(contractDto.getContractId());
        List<ContractRoomDto> contractRoomDtos = contractRoomInnerServiceSMOImpl.queryContractRooms(contractRoomDto);
        if (!ListUtil.isNull(contractRoomDtos)) {
            payFeeRulePo.setOwnerId(contractRoomDtos.get(0).getOwnerId());
            payFeeRulePo.setOwnerName(contractRoomDtos.get(0).getOwnerName());
        }
        payFeeRulePo.setPayerObjName(contractDto.getContractName());
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
