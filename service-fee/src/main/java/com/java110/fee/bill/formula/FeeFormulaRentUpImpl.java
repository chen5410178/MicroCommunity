package com.java110.fee.bill.formula;

import com.java110.core.factory.CommunitySettingFactory;
import com.java110.dto.contract.ContractRoomDto;
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
 * 租金递增
 */
@Service("feeFormula_1102")
public class FeeFormulaRentUpImpl implements IFeeFormula {

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
    public FeeAmountDto computeFeeAmount(FeeConfigDto feeConfigDto,String ruleId, String payerObjId, String payerObjType, int cycle,double amountValue) {
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

    private FeeAmountDto computeRoomAmount(FeeConfigDto feeConfigDto, String payerObjId, String payerObjType, int cycleValue, String monthFlag) {
        RoomDto roomDto = new RoomDto();
        roomDto.setRoomId(payerObjId);
        roomDto.setCommunityId(feeConfigDto.getCommunityId());
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
        if (ListUtil.isNull(roomDtos)) {
            throw new IllegalArgumentException("未查到房屋信息，查询多条数据 roomId=" + payerObjId);
        }
        roomDto = roomDtos.get(0);

        BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(roomDto.getRoomRent()));
       // feePrice = additionalAmount.setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_UP);

        BigDecimal cycle = new BigDecimal(cycleValue);
        
        // 添加monthFlag逻辑处理
        BigDecimal feeTotalPrice = additionalAmount;
        if ("2".equals(monthFlag)) {
            feeTotalPrice = feeTotalPrice.setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_UP);
            double tAmount = MoneyUtil.computePriceScale(
                    feeTotalPrice.doubleValue(),
                    feeConfigDto.getScale(),
                    Integer.parseInt(feeConfigDto.getDecimalPlace())
            );
            feeTotalPrice = new BigDecimal(tAmount + "");
        }
        
        feeTotalPrice = feeTotalPrice.multiply(cycle).setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_UP);

        String remark = "房屋租金：" + roomDto.getRoomRent();
        return new FeeAmountDto(feeTotalPrice.doubleValue(), remark);

    }

    private FeeAmountDto computeCarAmount(FeeConfigDto feeConfigDto, String payerObjId, String payerObjType, int cycleValue, String monthFlag) {
        String remark = "车位租金：" + 0;

        return new FeeAmountDto(0.0, remark);
    }

    private FeeAmountDto computeContractAmount(FeeConfigDto feeConfigDto, String payerObjId, String payerObjType, int cycleValue, String monthFlag) {
        ContractRoomDto contractRoomDto = new ContractRoomDto();
        contractRoomDto.setContractId(payerObjId);
        contractRoomDto.setCommunityId(feeConfigDto.getCommunityId());
        List<ContractRoomDto> contractRoomDtos = contractRoomInnerServiceSMOImpl.queryContractRooms(contractRoomDto);

        BigDecimal roomRent = new BigDecimal(0);
        for (ContractRoomDto tmpContractRoomDto : contractRoomDtos) {
            roomRent = roomRent.add(new BigDecimal(Double.parseDouble(tmpContractRoomDto.getRoomRent())));
        }
        BigDecimal cycle = new BigDecimal(cycleValue);
        
        // 添加monthFlag逻辑处理
        BigDecimal feeTotalPrice = roomRent;
        if ("2".equals(monthFlag)) {
            feeTotalPrice = feeTotalPrice.setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_UP);
            double tAmount = MoneyUtil.computePriceScale(
                    feeTotalPrice.doubleValue(),
                    feeConfigDto.getScale(),
                    Integer.parseInt(feeConfigDto.getDecimalPlace())
            );
            feeTotalPrice = new BigDecimal(tAmount + "");
        }
        
        feeTotalPrice = feeTotalPrice.multiply(cycle).setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_UP);
        String remark = "合同租金：" + roomRent.doubleValue();
        return new FeeAmountDto(feeTotalPrice.doubleValue(), remark);
    }

    private FeeAmountDto computeShopAmount(FeeConfigDto feeConfigDto, String payerObjId, String payerObjType, int cycleValue, String monthFlag) {
        BuildingShopDto shopDto = new BuildingShopDto();
        shopDto.setShopId(payerObjId);
        shopDto.setCommunityId(feeConfigDto.getCommunityId());
        List<BuildingShopDto> shopDtos = buildingShopInnerServiceSMOImpl.queryBuildingShops(shopDto);
        Assert.listOnlyOne(shopDtos, "未查到商铺信息，shopId=" + payerObjId);

        BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(shopDtos.get(0).getShopRent()));
        BigDecimal cycle = new BigDecimal(cycleValue);

        BigDecimal feeTotalPrice = additionalAmount;
        if ("2".equals(monthFlag)) {
            feeTotalPrice = feeTotalPrice.setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_UP);
            double tAmount = MoneyUtil.computePriceScale(
                    feeTotalPrice.doubleValue(),
                    feeConfigDto.getScale(),
                    Integer.parseInt(feeConfigDto.getDecimalPlace())
            );
            feeTotalPrice = new BigDecimal(tAmount + "");
        }

        feeTotalPrice = feeTotalPrice.multiply(cycle).setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_UP);
        String remark = "商铺租金：" + shopDtos.get(0).getShopRent();
        return new FeeAmountDto(feeTotalPrice.doubleValue(), remark);
    }


}
