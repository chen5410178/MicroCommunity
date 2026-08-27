package com.java110.fee.bill.formula;

import com.java110.core.factory.CommunitySettingFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.contract.ContractRoomDto;
import com.java110.dto.fee.FeeAmountDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.room.RoomAttrDto;
import com.java110.dto.room.RoomDto;
import com.java110.dto.shop.BuildingShopDto;
import com.java110.fee.bill.IFeeFormula;
import com.java110.intf.community.IBuildingShopInnerServiceSMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.store.IContractRoomInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.utils.constant.FeeConfigConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.MoneyUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.math.BigDecimal;
import java.util.List;

/**
 * 自定义公式
 */
@Service("feeFormula_7007")
public class FeeFormulaCustomImpl implements IFeeFormula {

    private Logger logger = LoggerFactory.getLogger(FeeFormulaCustomImpl.class);

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IContractRoomInnerServiceSMO contractRoomInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IBuildingShopInnerServiceSMO buildingShopInnerServiceSMOImpl;

    @Override
    public FeeAmountDto computeFeeAmount(FeeConfigDto feeConfigDto, String ruleId, String payerObjId, String payerObjType, int cycle, double amountValue) {
        FeeAmountDto amount = null;
        String monthFlag = CommunitySettingFactory.getValue(feeConfigDto.getCommunityId(), "FEE_MONTH_FLAG");
        if (FeeDto.PAYER_OBJ_TYPE_ROOM.equals(payerObjType)) { //房屋相关
            amount = computeRoomAmount(feeConfigDto, payerObjId, payerObjType, cycle, monthFlag);
        } else if (FeeDto.PAYER_OBJ_TYPE_PARKING_SPACE.equals(payerObjType)) {
            amount = computeCarAmount(feeConfigDto, payerObjId, payerObjType, cycle, monthFlag);

        } else if (FeeDto.PAYER_OBJ_TYPE_CONTRACT.equals(payerObjType)) {
            amount = computeContractAmount(feeConfigDto, payerObjId, payerObjType, cycle, monthFlag);

        } else if (FeeDto.PAYER_OBJ_TYPE_SHOP.equals(payerObjType)) {
            amount = computeShopAmount(feeConfigDto, payerObjId, payerObjType, cycle, monthFlag);
        }
        return amount;
    }

    private FeeAmountDto computeRoomAmount(FeeConfigDto feeConfigDto, String payerObjId, String payerObjType, int cycle, String monthFlag) {
        RoomDto roomDto = new RoomDto();
        roomDto.setRoomId(payerObjId);
        roomDto.setCommunityId(feeConfigDto.getCommunityId());
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
        if (ListUtil.isNull(roomDtos)) {
            throw new IllegalArgumentException("未查到房屋信息，查询多条数据 roomId=" + payerObjId);
        }
        roomDto = roomDtos.get(0);

        BigDecimal feePrice = computeRoomCustomizeFormula(feeConfigDto, roomDto);
        BigDecimal cycleDec = new BigDecimal(cycle);
        
        // 添加monthFlag逻辑处理
        if ("2".equals(monthFlag)) {
            feePrice = feePrice.setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_UP);
            double tAmount = MoneyUtil.computePriceScale(
                    feePrice.doubleValue(),
                    feeConfigDto.getScale(),
                    Integer.parseInt(feeConfigDto.getDecimalPlace())
            );
            feePrice = new BigDecimal(tAmount + "");
        }

        BigDecimal   feeTotalPrice = feePrice.multiply(cycleDec).setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_UP);
        String remark = "公式：" + feeConfigDto.getComputingFormulaText();
        return new FeeAmountDto(feeTotalPrice.doubleValue(), remark);

    }

    private FeeAmountDto computeCarAmount(FeeConfigDto feeConfigDto, String payerObjId, String payerObjType, int cycleValue, String monthFlag) {
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCarTypeCd("1001"); //业主车辆
        ownerCarDto.setCommunityId(feeConfigDto.getCommunityId());
        ownerCarDto.setCarId(payerObjId);
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
        Assert.listOnlyOne(ownerCarDtos, "未找到车辆信息");

        BigDecimal feePrice = computeCarCustomizeFormula(feeConfigDto, ownerCarDtos.get(0));
        BigDecimal cycleDec = new BigDecimal(cycleValue);
        
        // 添加monthFlag逻辑处理
        if ("2".equals(monthFlag)) {
            feePrice = feePrice.setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_UP);
            double tAmount = MoneyUtil.computePriceScale(
                    feePrice.doubleValue(),
                    feeConfigDto.getScale(),
                    Integer.parseInt(feeConfigDto.getDecimalPlace())
            );
            feePrice = new BigDecimal(tAmount + "");
        }

        BigDecimal   feeTotalPrice = feePrice.multiply(cycleDec).setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_UP);

        String remark = "公式：" + feeConfigDto.getComputingFormulaText();
        return new FeeAmountDto(feeTotalPrice.doubleValue(), remark);    }

    private FeeAmountDto computeContractAmount(FeeConfigDto feeConfigDto, String payerObjId, String payerObjType, int cycleValue, String monthFlag) {
        ContractRoomDto contractRoomDto = new ContractRoomDto();
        contractRoomDto.setContractId(payerObjId);
        contractRoomDto.setCommunityId(feeConfigDto.getCommunityId());
        List<ContractRoomDto> contractRoomDtos = contractRoomInnerServiceSMOImpl.queryContractRooms(contractRoomDto);

        //feePrice = Double.parseDouble(feeDto.getSquarePrice()) * Double.parseDouble(roomDtos.get(0).getBuiltUpArea()) + Double.parseDouble(feeDto.getAdditionalAmount());
        BigDecimal feePrice = computeContractCustomizeFormula(feeConfigDto, contractRoomDtos);
        BigDecimal cycleDec = new BigDecimal(cycleValue);
        
        // 添加monthFlag逻辑处理
        if ("2".equals(monthFlag)) {
            feePrice = feePrice.setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_UP);
            double tAmount = MoneyUtil.computePriceScale(
                    feePrice.doubleValue(),
                    feeConfigDto.getScale(),
                    Integer.parseInt(feeConfigDto.getDecimalPlace())
            );
            feePrice = new BigDecimal(tAmount + "");
        }

        BigDecimal   feeTotalPrice = feePrice.multiply(cycleDec).setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_UP);

        String remark = "公式：" + feeConfigDto.getComputingFormulaText();
        return new FeeAmountDto(feeTotalPrice.doubleValue(), remark);
    }

    /**
     * 自定义公式计算
     *
     * @param feeConfigDto
     * @param roomDto
     * @return C 代表房屋对应小区面积
     * F 代表房屋对应楼栋面积
     * U 代表房屋对应单元面积
     * R 代表房屋面积
     * X 代表房屋收费系数（房屋管理中配置）
     * L 代表房屋层数
     */
    private BigDecimal computeRoomCustomizeFormula(FeeConfigDto feeConfigDto, RoomDto roomDto) {

        String value = feeConfigDto.getComputingFormulaText();

        if (StringUtil.isEmpty(value)) {
            return new BigDecimal(0);
        }

        value = value.replace("\n", "")
                .replace("\r", "")
                .trim();

        if (value.contains("C")) { //处理小区面积
            CommunityDto communityDto = new CommunityDto();
            communityDto.setCommunityId(feeConfigDto.getCommunityId());
            List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);
            if (ListUtil.isNull(communityDtos)) {
                value = value.replace("C", "0");
            } else {
                value = value.replace("C", communityDtos.get(0).getCommunityArea());
            }
        }
        if (value.contains("F")) { //处理楼栋
            value = value.replace("F", roomDto.getFloorArea());
        }
        if (value.contains("U")) { //处理单元
            value = value.replace("U", roomDto.getUnitArea());
        }
        if (value.contains("RL")) {
            List<RoomAttrDto> roomAttrDtos = roomDto.getRoomAttrDto();
            if (!ListUtil.isNull(roomAttrDtos)) {
                for (RoomAttrDto roomAttrDto : roomAttrDtos) {
                    value = value.replace("RL" + roomAttrDto.getSpecCd(), roomAttrDto.getValue());
                }
            }
            if(value.contains("RLPC")){ // 如果没有配置，那么替换为1
                value = value.replace("RLPC", "1");
            }
        }
        if (value.contains("R")) { //处理 房屋面积
            value = value.replace("R", roomDto.getBuiltUpArea());
        }
        if (value.contains("X")) {// 处理 房屋系数
            value = value.replace("X", roomDto.getFeeCoefficient());
        }
        if (value.contains("L")) {//处理房屋层数
            value = value.replace("L", roomDto.getLayer());
        }

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        BigDecimal valueObj = null;
        logger.debug("计算公式为：{}", value);
        try {
            value = engine.eval(value).toString();
            valueObj = new BigDecimal(Double.parseDouble(value));
        } catch (Exception e) {
            //throw new IllegalArgumentException("公式计算异常，公式为【" + feeDto.getComputingFormulaText() + "】,计算 【" + value + "】异常");
            logger.error("公式计算异常，公式为【" + feeConfigDto.getComputingFormulaText() + "】,计算 【" + value + "】异常", e);
            valueObj = new BigDecimal(0);
        }

        if (valueObj.doubleValue() < 0) {
            return new BigDecimal(0);
        }

        return valueObj;

    }

    /**
     * C 代表房屋对应小区面积
     * <p>
     * R 代表房屋面积
     *
     * @param feeConfigDto
     * @param ownerCarDto
     * @return
     */
    private BigDecimal computeCarCustomizeFormula(FeeConfigDto feeConfigDto, OwnerCarDto ownerCarDto) {
        String value = feeConfigDto.getComputingFormulaText();
        value = value.replace("\n", "")
                .replace("\r", "")
                .trim();

        if (value.contains("C")) { //处理小区面积
            CommunityDto communityDto = new CommunityDto();
            communityDto.setCommunityId(feeConfigDto.getCommunityId());
            List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);
            if (communityDtos == null || communityDtos.size() < 1) {
                value = value.replace("C", "0");
            } else {
                value = value.replace("C", communityDtos.get(0).getCommunityArea());
            }
        } else if (value.contains("R")) { //处理 房屋面积
            ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
            parkingSpaceDto.setCommunityId(feeConfigDto.getCommunityId());
            parkingSpaceDto.setPsId(ownerCarDto.getPsId());
            List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
            if (parkingSpaceDtos == null || parkingSpaceDtos.size() < 1) { //数据有问题
                //throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "未查到停车位信息，查询多条数据");
                value = value.replace("R", "0");
            } else {
                value = value.replace("R", parkingSpaceDtos.get(0).getArea());
            }
        }

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        BigDecimal valueObj = null;
        try {
            value = engine.eval(value).toString();
            valueObj = new BigDecimal(Double.parseDouble(value));
        } catch (Exception e) {
            //throw new IllegalArgumentException("公式计算异常，公式为【" + feeDto.getComputingFormulaText() + "】,计算 【" + value + "】异常");
            valueObj = new BigDecimal(0);
        }

        if (valueObj.doubleValue() < 0) {
            return new BigDecimal(0);
        }

        return valueObj;
    }
    /**
     * 自定义公式计算
     *
     * @param feeConfigDto
     * @return C 代表房屋对应小区面积
     * F 代表房屋对应楼栋面积
     * U 代表房屋对应单元面积
     * R 代表房屋面积
     * X 代表房屋收费系数（房屋管理中配置）
     * L 代表房屋层数
     */
    private BigDecimal computeContractCustomizeFormula(FeeConfigDto feeConfigDto, List<ContractRoomDto> contractRoomDtos) {

        BigDecimal total = new BigDecimal(0.0);
        for (ContractRoomDto contractRoomDto : contractRoomDtos) {
            total = total.add(computeRoomCustomizeFormula(feeConfigDto, contractRoomDto));
        }
        return total;
    }

    private FeeAmountDto computeShopAmount(FeeConfigDto feeConfigDto, String payerObjId, String payerObjType, int cycle, String monthFlag) {
        BuildingShopDto shopDto = new BuildingShopDto();
        shopDto.setShopId(payerObjId);
        shopDto.setCommunityId(feeConfigDto.getCommunityId());
        List<BuildingShopDto> shopDtos = buildingShopInnerServiceSMOImpl.queryBuildingShops(shopDto);
        Assert.listOnlyOne(shopDtos, "未查到商铺信息，shopId=" + payerObjId);

        BigDecimal feePrice = computeShopCustomizeFormula(feeConfigDto, shopDtos.get(0));
        BigDecimal cycleDec = new BigDecimal(cycle);

        if ("2".equals(monthFlag)) {
            feePrice = feePrice.setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_UP);
            double tAmount = MoneyUtil.computePriceScale(
                    feePrice.doubleValue(),
                    feeConfigDto.getScale(),
                    Integer.parseInt(feeConfigDto.getDecimalPlace())
            );
            feePrice = new BigDecimal(tAmount + "");
        }

        BigDecimal feeTotalPrice = feePrice.multiply(cycleDec).setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_UP);
        String remark = "公式：" + feeConfigDto.getComputingFormulaText();
        return new FeeAmountDto(feeTotalPrice.doubleValue(), remark);
    }

    private BigDecimal computeShopCustomizeFormula(FeeConfigDto feeConfigDto, BuildingShopDto shopDto) {
        String value = feeConfigDto.getComputingFormulaText();

        if (StringUtil.isEmpty(value)) {
            return new BigDecimal(0);
        }

        value = value.replace("\n", "")
                .replace("\r", "")
                .trim();

        if (value.contains("C")) {
            CommunityDto communityDto = new CommunityDto();
            communityDto.setCommunityId(feeConfigDto.getCommunityId());
            List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);
            if (ListUtil.isNull(communityDtos)) {
                value = value.replace("C", "0");
            } else {
                value = value.replace("C", communityDtos.get(0).getCommunityArea());
            }
        }
        if (value.contains("R")) {
            value = value.replace("R", shopDto.getBuiltUpArea() != null ? shopDto.getBuiltUpArea() : "0");
        }

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        BigDecimal valueObj = null;
        logger.debug("商铺计算公式为：{}", value);
        try {
            value = engine.eval(value).toString();
            valueObj = new BigDecimal(Double.parseDouble(value));
        } catch (Exception e) {
            logger.error("商铺公式计算异常，公式为【" + feeConfigDto.getComputingFormulaText() + "】,计算 【" + value + "】异常", e);
            valueObj = new BigDecimal(0);
        }

        if (valueObj.doubleValue() < 0) {
            return new BigDecimal(0);
        }

        return valueObj;
    }



}
