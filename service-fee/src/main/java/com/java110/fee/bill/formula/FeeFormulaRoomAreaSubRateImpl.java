package com.java110.fee.bill.formula;

import com.java110.core.factory.CommunitySettingFactory;
import com.java110.dto.fee.FeeAmountDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.room.RoomDto;
import com.java110.dto.shop.BuildingShopDto;
import com.java110.fee.bill.IFeeFormula;
import com.java110.intf.community.IBuildingShopInnerServiceSMO;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.store.IContractRoomInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.utils.constant.FeeConfigConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.MoneyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * （建筑面积-室内面积）*单价*比例系数
 */
@Service("feeFormula_1004")
public class FeeFormulaRoomAreaSubRateImpl implements IFeeFormula {

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IContractRoomInnerServiceSMO contractRoomInnerServiceSMOImpl;

    @Autowired
    private IBuildingShopInnerServiceSMO buildingShopInnerServiceSMOImpl;

    @Override
    public FeeAmountDto computeFeeAmount(FeeConfigDto feeConfigDto, String ruleId, String payerObjId, String payerObjType, int cycle, double amountValue) {
        FeeAmountDto amount = null;
        String monthFlag = CommunitySettingFactory.getValue(feeConfigDto.getCommunityId(), "FEE_MONTH_FLAG");
        if (FeeDto.PAYER_OBJ_TYPE_ROOM.equals(payerObjType)) { //房屋相关
            amount = computeRoomAmount(feeConfigDto, payerObjId, payerObjType, cycle, monthFlag);
        } else if (FeeDto.PAYER_OBJ_TYPE_PARKING_SPACE.equals(payerObjType)) {
            // 车辆没有建筑面积和室内面积，直接返回0
            amount = new FeeAmountDto(0.0, "车辆无建筑面积和室内面积");

        } else if (FeeDto.PAYER_OBJ_TYPE_CONTRACT.equals(payerObjType)) {
            // 合同暂时不处理，直接返回0
            amount = new FeeAmountDto(0.0, "合同无建筑面积和室内面积");

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

        BigDecimal builtUpArea = new BigDecimal(roomDto.getBuiltUpArea());
        BigDecimal roomArea = new BigDecimal(roomDto.getRoomArea());
        BigDecimal squarePrice = new BigDecimal(feeConfigDto.getSquarePrice());
        BigDecimal additionalAmount = new BigDecimal(feeConfigDto.getAdditionalAmount());
        BigDecimal cycleDec = new BigDecimal(cycle);
        
        // 计算（建筑面积-室内面积）*单价*比例系数
        BigDecimal diffArea = builtUpArea.subtract(roomArea);
        BigDecimal feePrice = (diffArea.multiply(squarePrice).multiply(additionalAmount));
        
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
        
        double amount = MoneyUtil.computePriceScale(
                feeTotalPrice.doubleValue(),
                feeConfigDto.getScale(),
                Integer.parseInt(feeConfigDto.getDecimalPlace())
        );
        String remark = "建筑面积：" + roomDtos.get(0).getBuiltUpArea() + "\n室内面积：" + roomDtos.get(0).getRoomArea() 
                + "\n差值面积：" + diffArea.doubleValue() + "\n单价：" + feeConfigDto.getSquarePrice() + "\n比例系数：" + feeConfigDto.getAdditionalAmount();
        return new FeeAmountDto(amount, remark);

    }

    private FeeAmountDto computeCarAmount(FeeConfigDto feeConfigDto, String payerObjId, String payerObjType, int cycleValue) {
        // 车辆没有建筑面积和室内面积，直接返回0
        return new FeeAmountDto(0.0, "车辆无建筑面积和室内面积");
    }

    private FeeAmountDto computeContractAmount(FeeConfigDto feeConfigDto, String payerObjId, String payerObjType, int cycleValue) {
        // 合同暂时不处理，直接返回0
        return new FeeAmountDto(0.0, "合同无建筑面积和室内面积");
    }

    private FeeAmountDto computeShopAmount(FeeConfigDto feeConfigDto, String payerObjId, String payerObjType, int cycle, String monthFlag) {
        BuildingShopDto shopDto = new BuildingShopDto();
        shopDto.setShopId(payerObjId);
        shopDto.setCommunityId(feeConfigDto.getCommunityId());
        List<BuildingShopDto> shopDtos = buildingShopInnerServiceSMOImpl.queryBuildingShops(shopDto);
        Assert.listOnlyOne(shopDtos, "未查到商铺信息，shopId=" + payerObjId);

        BigDecimal builtUpArea = new BigDecimal(shopDto.getBuiltUpArea() != null ? shopDto.getBuiltUpArea() : "0");
        BigDecimal roomArea = new BigDecimal(shopDto.getShopArea() != null ? shopDto.getShopArea() : "0");
        BigDecimal squarePrice = new BigDecimal(feeConfigDto.getSquarePrice());
        BigDecimal additionalAmount = new BigDecimal(feeConfigDto.getAdditionalAmount());
        BigDecimal cycleDec = new BigDecimal(cycle);

        BigDecimal diffArea = builtUpArea.subtract(roomArea);
        BigDecimal feePrice = (diffArea.multiply(squarePrice).multiply(additionalAmount));

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

        double amount = MoneyUtil.computePriceScale(
                feeTotalPrice.doubleValue(),
                feeConfigDto.getScale(),
                Integer.parseInt(feeConfigDto.getDecimalPlace())
        );
        String remark = "商铺建筑面积：" + shopDto.getBuiltUpArea() + "\n商铺室内面积：" + shopDto.getShopArea()
                + "\n差值面积：" + diffArea.doubleValue() + "\n单价：" + feeConfigDto.getSquarePrice() + "\n比例系数：" + feeConfigDto.getAdditionalAmount();
        return new FeeAmountDto(amount, remark);
    }
}