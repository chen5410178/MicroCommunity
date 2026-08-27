package com.java110.fee.cmd.carMonth;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.factory.Java110TransactionalFactory;
import com.java110.dto.IotDataDto;
import com.java110.dto.community.CommunityMemberDto;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.payFee.PayFeeBatchDto;
import com.java110.dto.user.UserDto;
import com.java110.fee.bill.ICycleConvertOnceFee;
import com.java110.intf.community.ICommunityMemberV1InnerServiceSMO;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.fee.IFeeAttrInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IPayFeeBatchV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeConfigV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeDetailV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeRuleV1InnerServiceSMO;
import com.java110.intf.job.IIotInnerServiceSMO;
import com.java110.intf.user.*;
import com.java110.po.car.OwnerCarPo;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.payFee.PayFeeBatchPo;
import com.java110.po.payFee.PayFeeRulePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Java110Cmd(serviceCode = "carMonth.buyCarMonthOrder")
public class BuyCarMonthOrderCmd extends Cmd {

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;
    @Autowired
    private IOwnerCarV1InnerServiceSMO ownerCarV1InnerServiceSMOImpl;
    @Autowired
    private IPayFeeConfigV1InnerServiceSMO payFeeConfigV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityMemberV1InnerServiceSMO communityMemberV1InnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelV1InnerServiceSMO ownerRoomRelV1InnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IPayFeeDetailV1InnerServiceSMO payFeeDetailV1InnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;


    @Autowired
    private IIotInnerServiceSMO iotInnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeBatchV1InnerServiceSMO payFeeBatchV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeRuleV1InnerServiceSMO payFeeRuleV1InnerServiceSMOImpl;

    @Autowired
    private ICycleConvertOnceFee cycleConvertOnceFeeImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {


        Assert.hasKeyAndValue(reqJson, "cardId", "请求报文中未包含cardId");
        Assert.hasKeyAndValue(reqJson, "carNum", "请求报文中未包含carNum");
        Assert.hasKeyAndValue(reqJson, "carId", "请求报文中未包含carId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "primeRate", "请求报文中未包含primeRate");
        Assert.hasKeyAndValue(reqJson, "receivedAmount", "请求报文中未包含receivedAmount");

        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCarId(reqJson.getString("carId"));
        ownerCarDto.setCarNum(reqJson.getString("carNum"));
        ownerCarDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

        if (ListUtil.isNull(ownerCarDtos)) {
            throw new CmdException("月租车不存在");
        }


        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setCommunityId(reqJson.getString("communityId"));
        parkingSpaceDto.setPsId(ownerCarDtos.get(0).getPsId());
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);

        if (ListUtil.isNull(parkingSpaceDtos)) {
            throw new CmdException("车辆停车位不存在");
        }


        JSONObject paramIn = new JSONObject();
        paramIn.put("row", 1);
        paramIn.put("page", 1);
        paramIn.put("cardId", reqJson.getString("cardId"));
        paramIn.put("communityId", reqJson.getString("communityId"));
        paramIn.put("paNum", parkingSpaceDtos.get(0).getAreaNum());

        ResultVo data = iotInnerServiceSMOImpl.postIotData(new IotDataDto("queryCarMonthCardBmoImpl", paramIn));

        if (data.getCode() != ResultVo.CODE_OK) {
            throw new CmdException(data.getMsg());
        }

        JSONArray carMonthCardDtos = (JSONArray) data.getData();

        if (ListUtil.isNull(carMonthCardDtos)) {
            throw new CmdException("月卡不存在");
        }


        reqJson.put("receivableAmount", carMonthCardDtos.getJSONObject(0).getString("cardPrice"));
        reqJson.put("cardMonth", carMonthCardDtos.getJSONObject(0).getString("cardMonth"));
        reqJson.put("carMemberId", ownerCarDtos.get(0).getMemberId());
        reqJson.put("endTime", ownerCarDtos.get(0).getEndTime());
        reqJson.put("psId", ownerCarDtos.get(0).getPsId());
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        String userId = CmdContextUtils.getUserId(context);

        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户未登录");


        Date startTime = reqJson.getDate("endTime");

        if (startTime.getTime() < DateUtil.getCurrentDate().getTime()) {
            startTime = DateUtil.getCurrentDate();
        }

        String endTime = DateUtil.getAddMonthStringA(startTime, reqJson.getIntValue("cardMonth"));


        //todo 查询 名称为月租车月卡 公式为动态费用，费用类型为停车费的费用项，查不到直接报错

        FeeConfigDto feeConfigDto = getFeeConfigDto(reqJson.getString("communityId"));


        CommunityMemberDto communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setCommunityId(reqJson.getString("communityId"));
        communityMemberDto.setMemberTypeCd(CommunityMemberDto.MEMBER_TYPE_PROPERTY);
        List<CommunityMemberDto> communityMemberDtos = communityMemberV1InnerServiceSMOImpl.queryCommunityMembers(communityMemberDto);

        Assert.listOnlyOne(communityMemberDtos, "物业不存在");

        //todo 查询车辆信息
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCommunityId(reqJson.getString("communityId"));
        ownerCarDto.setCarId(reqJson.getString("carId"));
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

        if (ListUtil.isNull(ownerCarDtos)) {
            throw new CmdException("车辆不存在");
        }

        //todo 生成批次
        PayFeeBatchPo payFeeBatchPo = new PayFeeBatchPo();
        payFeeBatchPo.setBatchId(GenerateCodeFactory.getGeneratorId("12"));
        payFeeBatchPo.setCommunityId(reqJson.getString("communityId"));
        payFeeBatchPo.setCreateUserId(userId);
        payFeeBatchPo.setCreateUserName(userDtos.get(0).getName());
        payFeeBatchPo.setState(PayFeeBatchDto.STATE_NORMAL);
        payFeeBatchPo.setMsg("正常");
        int batchFlag = payFeeBatchV1InnerServiceSMOImpl.savePayFeeBatch(payFeeBatchPo);

        if (batchFlag < 1) {
            throw new CmdException("生成批次失败");
        }

        //todo 创建账单规则（费用挂载车辆 5555）
        List<PayFeeRulePo> payFeeRulePos = new ArrayList<>();
        PayFeeRulePo payFeeRulePo = new PayFeeRulePo();
        payFeeRulePo.setRuleId(GenerateCodeFactory.getGeneratorId("11"));
        payFeeRulePo.setCommunityId(reqJson.getString("communityId"));
        payFeeRulePo.setConfigId(feeConfigDto.getConfigId());
        payFeeRulePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_CAR);
        payFeeRulePo.setPayerObjId(reqJson.getString("carId"));
        payFeeRulePo.setPayerObjName(reqJson.getString("carNum"));
        payFeeRulePo.setOwnerId(ownerCarDtos.get(0).getOwnerId());
        payFeeRulePo.setOwnerName(ownerCarDtos.get(0).getOwnerName());
        payFeeRulePo.setAmount(reqJson.getString("receivedAmount"));
        payFeeRulePo.setFeeTypeCd(feeConfigDto.getFeeTypeCd());
        payFeeRulePo.setFeeFlag(feeConfigDto.getFeeFlag());
        payFeeRulePo.setStartTime(DateUtil.getFormatTimeStringA(startTime));
        payFeeRulePo.setEndTime(endTime);
        payFeeRulePo.setMaxTime(endTime);
        payFeeRulePo.setState(FeeDto.STATE_DOING);
        payFeeRulePo.setBatchId(payFeeBatchPo.getBatchId());
        payFeeRulePo.setIncomeObjId(communityMemberDtos.get(0).getMemberId());
        payFeeRulePo.setUserId("-1");
        payFeeRulePo.setCurYearMonth(DateUtil.getFormatTimeStringB(startTime));
        payFeeRulePos.add(payFeeRulePo);

        //todo 保存账单规则
        payFeeRuleV1InnerServiceSMOImpl.savePayFeeRules(payFeeRulePos);

        //todo 生成账单（pay_fee）
        cycleConvertOnceFeeImpl.rulesGeneratePayFees(payFeeRulePos);

        //todo 查询生成的费用
        FeeDto feeDto = new FeeDto();
        feeDto.setCommunityId(reqJson.getString("communityId"));
        feeDto.setPayerObjId(reqJson.getString("carId"));
        feeDto.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_CAR);
        feeDto.setConfigId(feeConfigDto.getConfigId());
        feeDto.setState(FeeDto.STATE_DOING);
        feeDto.setStartTime(DateUtil.getDateFromStringB(DateUtil.getFormatTimeStringB(startTime)));
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        Assert.listOnlyOne(feeDtos, "生成账单失败");

        //todo 缴费对象载体
        PayFeePo payFeePo = new PayFeePo();
        payFeePo.setFeeId(feeDtos.get(0).getFeeId());
        payFeePo.setCommunityId(feeDtos.get(0).getCommunityId());
        payFeePo.setEndTime(DateUtil.getFormatTimeStringA(feeDtos.get(0).getEndTime()));

        //todo 计算用量
        List<FeeAttrPo> feeAttrsPos = new ArrayList<>();
        if (!ListUtil.isNull(ownerCarDtos)) {
            feeAttrsPos.add(addFeeAttr(payFeePo, FeeAttrDto.SPEC_CD_PAY_OBJECT_NAME,
                    ownerCarDtos.get(0).getAreaNum() + "-" + ownerCarDtos.get(0).getNum() + "(" + ownerCarDtos.get(0).getCarNum() + ")"));
        }

        //todo 查询业主信息
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(ownerCarDtos.get(0).getOwnerId());
        ownerDto.setOwnerTypeCd(OwnerDto.OWNER_TYPE_CD_OWNER);
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);
        if (!ListUtil.isNull(ownerDtos)) {

            feeAttrsPos.add(addFeeAttr(payFeePo, FeeAttrDto.SPEC_CD_ONCE_FEE_DEADLINE_TIME,
                    payFeePo.getEndTime()));

            feeAttrsPos.add(addFeeAttr(payFeePo, FeeAttrDto.SPEC_CD_OWNER_ID, ownerDtos.get(0).getOwnerId()));
            feeAttrsPos.add(addFeeAttr(payFeePo, FeeAttrDto.SPEC_CD_OWNER_LINK, ownerDtos.get(0).getLink()));
            feeAttrsPos.add(addFeeAttr(payFeePo, FeeAttrDto.SPEC_CD_OWNER_NAME, ownerDtos.get(0).getName()));

        }
        feeAttrInnerServiceSMOImpl.saveFeeAttrs(feeAttrsPos);
        String oId = Java110TransactionalFactory.getOId();

        PayFeeDetailPo payFeeDetailPo = new PayFeeDetailPo();
        payFeeDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        if (StringUtil.isEmpty(oId)) {
            oId = payFeeDetailPo.getDetailId();
        }
        payFeeDetailPo.setCommunityId(feeConfigDto.getCommunityId());
        payFeeDetailPo.setReceivedAmount(reqJson.getString("receivedAmount"));
        payFeeDetailPo.setReceivableAmount(reqJson.getString("receivedAmount"));
        payFeeDetailPo.setCycles("1");
        payFeeDetailPo.setPrimeRate(reqJson.getString("primeRate"));
        payFeeDetailPo.setFeeId(payFeePo.getFeeId());
        payFeeDetailPo.setStartTime(DateUtil.getFormatTimeStringA(feeDtos.get(0).getStartTime()));
        payFeeDetailPo.setEndTime(DateUtil.getPreSecTime(DateUtil.getFormatTimeStringA(feeDtos.get(0).getEndTime())));
        payFeeDetailPo.setRemark(reqJson.getString("remark"));
        payFeeDetailPo.setCreateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        payFeeDetailPo.setState("1400");
        payFeeDetailPo.setPayableAmount(reqJson.getString("receivedAmount"));
        payFeeDetailPo.setPayOrderId(oId);
        payFeeDetailPo.setOpenInvoice("N");
        payFeeDetailPo.setDeductionAmount("0");
        payFeeDetailPo.setGiftAmount("0");
        payFeeDetailPo.setDiscountAmount("0");
        payFeeDetailPo.setLateAmount("0");
        if (feeDtos.get(0).getFeeTime() != null) {
            payFeeDetailPo.setFeeTime(DateUtil.getFormatTimeStringA(feeDtos.get(0).getFeeTime()));
        }
        payFeeDetailPo.setCashierName(userDtos.get(0).getName());
        payFeeDetailPo.setCashierId("-1");

        payFeeDetailV1InnerServiceSMOImpl.savePayFeeDetailNew(payFeeDetailPo);

        //todo 修改车辆时间
        OwnerCarPo ownerCarPo = new OwnerCarPo();
        ownerCarPo.setCarId(ownerCarDtos.get(0).getCarId());
        ownerCarPo.setEndTime(endTime);
        ownerCarPo.setCommunityId(ownerCarDtos.get(0).getCommunityId());
        ownerCarV1InnerServiceSMOImpl.updateOwnerCar(ownerCarPo);

        //todo 通知物联网 月卡信息
        JSONObject paramIn = new JSONObject();
        paramIn.put("cardId", reqJson.getString("cardId"));
        paramIn.put("carNum", reqJson.getString("carNum"));
        paramIn.put("communityId", reqJson.getString("communityId"));
        paramIn.put("primeRate", reqJson.getString("primeRate"));
        paramIn.put("receivedAmount", reqJson.getString("receivedAmount"));
        paramIn.put("endTime", endTime);
        paramIn.put("cashierId", userId);
        paramIn.put("cashierName", userDtos.get(0).getName());

        ResultVo data = iotInnerServiceSMOImpl.postIotData(new IotDataDto("buyCarMonthOrderBmoImpl", paramIn));

        if (data.getCode() != ResultVo.CODE_OK) {
            throw new CmdException(data.getMsg());
        }


    }

    private FeeConfigDto getFeeConfigDto(String communityId) {

        String feeName = "月租车月卡";


        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setFeeName(feeName);
        feeConfigDto.setCommunityId(communityId);
        feeConfigDto.setFeeTypeCd(FeeConfigDto.FEE_TYPE_CD_PARKING);
        feeConfigDto.setComputingFormula(FeeConfigDto.COMPUTING_FORMULA_DYNAMIC);
        List<FeeConfigDto> feeConfigDtos = payFeeConfigV1InnerServiceSMOImpl.queryPayFeeConfigs(feeConfigDto);

        if (ListUtil.isNull(feeConfigDtos)) {
            throw new CmdException("在停车费类型下，添加名称为 月租车月卡 费用项名称 并且是公式为动态费用的 费用项");
        }

        return feeConfigDtos.get(0);

    }


    public FeeAttrPo addFeeAttr(PayFeePo payFeePo, String specCd, String value) {
        FeeAttrPo feeAttrPo = new FeeAttrPo();
        feeAttrPo.setCommunityId(payFeePo.getCommunityId());
        feeAttrPo.setSpecCd(specCd);
        feeAttrPo.setValue(value);
        feeAttrPo.setFeeId(payFeePo.getFeeId());
        feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
        return feeAttrPo;

    }

}
