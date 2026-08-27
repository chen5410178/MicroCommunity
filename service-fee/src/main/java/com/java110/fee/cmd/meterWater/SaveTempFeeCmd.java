package com.java110.fee.cmd.meterWater;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.contract.ContractDto;
import com.java110.dto.contract.ContractRoomDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.payFee.PayFeeBatchDto;
import com.java110.dto.room.RoomDto;
import com.java110.dto.shop.BuildingShopDto;
import com.java110.dto.shop.BuildingShopOwnerDto;
import com.java110.dto.user.UserDto;
import com.java110.fee.bill.ICycleConvertOnceFee;
import com.java110.intf.community.IBuildingShopInnerServiceSMO;
import com.java110.intf.community.IBuildingShopOwnerInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IPayFeeBatchV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeRuleV1InnerServiceSMO;
import com.java110.intf.store.IContractInnerServiceSMO;
import com.java110.intf.store.IContractRoomInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.payFee.PayFeeBatchPo;
import com.java110.po.payFee.PayFeeRulePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "meterWater.saveTempFee")
public class SaveTempFeeCmd extends Cmd {

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IPayFeeBatchV1InnerServiceSMO payFeeBatchV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeRuleV1InnerServiceSMO payFeeRuleV1InnerServiceSMOImpl;

    @Autowired
    private ICycleConvertOnceFee cycleConvertOnceFeeImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IBuildingShopInnerServiceSMO buildingShopInnerServiceSMOImpl;

    @Autowired
    private IBuildingShopOwnerInnerServiceSMO buildingShopOwnerInnerServiceSMOImpl;

    @Autowired
    private IContractInnerServiceSMO contractInnerServiceSMOImpl;

    @Autowired
    private IContractRoomInnerServiceSMO contractRoomInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "feeTypeCd", "请求报文中未包含费用类型");
        Assert.hasKeyAndValue(reqJson, "configId", "请求报文中未包含费用项");
        Assert.hasKeyAndValue(reqJson, "objType", "请求报文中未包含objType");
        Assert.hasKeyAndValue(reqJson, "objId", "请求报文中未包含objId");
        Assert.hasKeyAndValue(reqJson, "amount", "请求报文中未包含amount");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含开始时间");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含结束时间");
        String endTime = reqJson.getString("endTime");
        if (!endTime.contains(":")) {
            endTime += " 23:59:59";
            reqJson.put("endTime", endTime);
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String userId = CmdContextUtils.getUserId(context);
        String storeId = CmdContextUtils.getStoreId(context);
        //生成批次
        generatorBatch(reqJson,userId);
        List<PayFeeRulePo> payFeeRulePos = new ArrayList<>();

        PayFeeRulePo payFeeRulePo =new PayFeeRulePo();
        payFeeRulePo.setMaxTime(reqJson.getString("endTime"));
        payFeeRulePo.setAmount(reqJson.getString("amount"));
        payFeeRulePo.setFeeTypeCd(reqJson.getString("feeTypeCd"));
        payFeeRulePo.setCurYearMonth(reqJson.getString("startTime"));
        payFeeRulePo.setBatchId(reqJson.getString("batchId"));
        payFeeRulePo.setUserId(userId);
        payFeeRulePo.setIncomeObjId(storeId);
        payFeeRulePo.setConfigId(reqJson.getString("configId"));
        payFeeRulePo.setFeeFlag(FeeDto.FEE_FLAG_ONCE);
        payFeeRulePo.setStartTime(reqJson.getString("startTime"));
        payFeeRulePo.setEndTime(reqJson.getString("startTime"));
        payFeeRulePo.setState(FeeDto.STATE_DOING);
        payFeeRulePo.setRuleId(GenerateCodeFactory.getGeneratorId("11"));
        payFeeRulePo.setCommunityId(reqJson.getString("communityId"));
        payFeeRulePo.setPayerObjType(reqJson.getString("objType"));
        payFeeRulePo.setPayerObjId(reqJson.getString("objId"));
        //todo 临时费用业主与对象名称统一按 objType 查询数据库，禁止前端透传
        refreshPayerObjInfo(payFeeRulePo, reqJson);
        payFeeRulePos.add(payFeeRulePo);

        //todo 设置费用
        payFeeRuleV1InnerServiceSMOImpl.savePayFeeRules(payFeeRulePos);


        //todo 走账单模式
        cycleConvertOnceFeeImpl.rulesGeneratePayFees(payFeeRulePos);

        context.setResponseEntity(ResultVo.success());

    }

    /**
     * 按 objType 查询真实业主与对象名称，写入 pay_fee_rule 三字段
     * 业主查不到置空，对象名称必须查到，否则抛异常（禁止用对象ID兜底）
     *
     * @param payFeeRulePo
     * @param reqJson
     */
    private void refreshPayerObjInfo(PayFeeRulePo payFeeRulePo, JSONObject reqJson) {
        String objType = payFeeRulePo.getPayerObjType();
        String objId = payFeeRulePo.getPayerObjId();
        String communityId = reqJson.getString("communityId");
        if (FeeDto.PAYER_OBJ_TYPE_ROOM.equals(objType)) {
            // 3333 房屋
            OwnerDto ownerDto = new OwnerDto();
            ownerDto.setCommunityId(communityId);
            ownerDto.setRoomId(objId);
            List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnersByRoom(ownerDto);
            if (!ListUtil.isNull(ownerDtos)) {
                payFeeRulePo.setOwnerId(ownerDtos.get(0).getOwnerId());
                payFeeRulePo.setOwnerName(ownerDtos.get(0).getName());
            }
            RoomDto roomDto = new RoomDto();
            roomDto.setRoomId(objId);
            List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
            Assert.listOnlyOne(roomDtos, "查询房屋信息错误");
            payFeeRulePo.setPayerObjName(roomDtos.get(0).getFloorNum() + "-"
                    + roomDtos.get(0).getUnitNum() + "-" + roomDtos.get(0).getRoomNum());
        } else if (FeeDto.PAYER_OBJ_TYPE_PARKING_SPACE.equals(objType)) {
            // 6666 车辆
            OwnerCarDto ownerCarDto = new OwnerCarDto();
            ownerCarDto.setCarId(objId);
            ownerCarDto.setCommunityId(communityId);
            List<OwnerCarDto> carDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
            Assert.listOnlyOne(carDtos, "查询车辆信息错误");
            payFeeRulePo.setOwnerId(carDtos.get(0).getOwnerId());
            payFeeRulePo.setOwnerName(carDtos.get(0).getOwnerName());
            payFeeRulePo.setPayerObjName(carDtos.get(0).getCarNum());
        } else if (FeeDto.PAYER_OBJ_TYPE_SHOP.equals(objType)) {
            // 4444 商铺
            BuildingShopDto buildingShopDto = new BuildingShopDto();
            buildingShopDto.setCommunityId(communityId);
            buildingShopDto.setShopId(objId);
            List<BuildingShopDto> shopDtos = buildingShopInnerServiceSMOImpl.queryBuildingShops(buildingShopDto);
            Assert.listOnlyOne(shopDtos, "查询商铺信息错误");
            BuildingShopOwnerDto shopOwnerDto = new BuildingShopOwnerDto();
            shopOwnerDto.setShopId(objId);
            List<BuildingShopOwnerDto> shopOwnerDtos = buildingShopOwnerInnerServiceSMOImpl.queryBuildingShopOwners(shopOwnerDto);
            if (!ListUtil.isNull(shopOwnerDtos)) {
                OwnerDto ownerDto = new OwnerDto();
                ownerDto.setOwnerId(shopOwnerDtos.get(0).getOwnerId());
                List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
                if (!ListUtil.isNull(ownerDtos)) {
                    payFeeRulePo.setOwnerId(ownerDtos.get(0).getOwnerId());
                    payFeeRulePo.setOwnerName(ownerDtos.get(0).getName());
                }
            }
            payFeeRulePo.setPayerObjName(shopDtos.get(0).getShopNum());
        } else if (FeeDto.PAYER_OBJ_TYPE_CONTRACT.equals(objType)) {
            // 7777 合同
            ContractRoomDto contractRoomDto = new ContractRoomDto();
            contractRoomDto.setContractId(objId);
            List<ContractRoomDto> contractRoomDtos = contractRoomInnerServiceSMOImpl.queryContractRooms(contractRoomDto);
            if (!ListUtil.isNull(contractRoomDtos)) {
                payFeeRulePo.setOwnerId(contractRoomDtos.get(0).getOwnerId());
                payFeeRulePo.setOwnerName(contractRoomDtos.get(0).getOwnerName());
            }
            ContractDto contractDto = new ContractDto();
            contractDto.setContractId(objId);
            List<ContractDto> contractDtos = contractInnerServiceSMOImpl.queryContracts(contractDto);
            Assert.listOnlyOne(contractDtos, "查询合同信息错误");
            payFeeRulePo.setPayerObjName(contractDtos.get(0).getContractName());
        }
        // 其他 objType 不支持，payerObjName 保持空，由 CycleConvertOnceFeeImpl 强校验报错阻断
    }

    /**
     * 生成批次号
     *
     * @param reqJson
     */
    private void generatorBatch(JSONObject reqJson,String userId) {
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
