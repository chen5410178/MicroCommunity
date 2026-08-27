package com.java110.fee.bill.formula;

import com.java110.core.factory.CommunitySettingFactory;
import com.java110.dto.contract.ContractRoomDto;
import com.java110.dto.fee.FeeAmountDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.room.RoomDto;
import com.java110.dto.shop.BuildingShopDto;
import com.java110.fee.bill.IFeeFormula;
import com.java110.intf.community.IBuildingShopInnerServiceSMO;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.store.IContractRoomInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.utils.constant.FeeConfigConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.MoneyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 建筑面积乘以单价乘以比例
 */
@Service("feeFormula_1002")
public class FeeFormulaAreaRatePriceImpl implements IFeeFormula {

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

        BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeConfigDto.getSquarePrice()));
        BigDecimal builtUpArea = new BigDecimal(Double.parseDouble(roomDto.getBuiltUpArea()));
        BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeConfigDto.getAdditionalAmount()));
        //BigDecimal  feePrice = squarePrice.multiply(builtUpArea).add(additionalAmount).setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_UP);
        BigDecimal cycleDec = new BigDecimal(cycle);
        BigDecimal feeTotalPrice = (squarePrice.multiply(builtUpArea).multiply(additionalAmount));
        
        if("2".equals(monthFlag)){
            feeTotalPrice =  feeTotalPrice.setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_UP);
            double tAmount = MoneyUtil.computePriceScale(
                    feeTotalPrice.doubleValue(),
                    feeConfigDto.getScale(),
                    Integer.parseInt(feeConfigDto.getDecimalPlace())
            );
            feeTotalPrice = new BigDecimal(tAmount+"");
        }
        
        feeTotalPrice = feeTotalPrice.multiply(cycleDec).setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_UP);
        
        double amount = MoneyUtil.computePriceScale(
                feeTotalPrice.doubleValue(),
                feeConfigDto.getScale(),
                Integer.parseInt(feeConfigDto.getDecimalPlace())
        );
        String remark = "建筑面积："+roomDtos.get(0).getBuiltUpArea()+"\n单价："+feeConfigDto.getSquarePrice()+"\n比例系数："+feeConfigDto.getAdditionalAmount();
        return new FeeAmountDto(amount,remark);

    }

    private FeeAmountDto computeCarAmount(FeeConfigDto feeConfigDto, String payerObjId, String payerObjType, int cycleValue, String monthFlag) {
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCarTypeCd("1001"); //业主车辆
        ownerCarDto.setCommunityId(feeConfigDto.getCommunityId());
        ownerCarDto.setCarId(payerObjId);
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
        Assert.listOnlyOne(ownerCarDtos, "未找到车辆信息");

        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setCommunityId(feeConfigDto.getCommunityId());
        parkingSpaceDto.setPsId(ownerCarDtos.get(0).getPsId());
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
        if (ListUtil.isNull(parkingSpaceDtos)) { //数据有问题
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "未查到停车位信息，查询多条数据");
        }
        BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeConfigDto.getSquarePrice()));
        BigDecimal builtUpArea = new BigDecimal(Double.parseDouble(parkingSpaceDtos.get(0).getArea()));
        BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeConfigDto.getAdditionalAmount()));
        // feePrice = squarePrice.multiply(builtUpArea).add(additionalAmount).setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_UP);
        BigDecimal cycle = new BigDecimal(cycleValue);

        BigDecimal feeTotalPrice = (squarePrice.multiply(builtUpArea).multiply(additionalAmount));
        
        if("2".equals(monthFlag)){
            feeTotalPrice =  feeTotalPrice.setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_UP);
            double tAmount = MoneyUtil.computePriceScale(
                    feeTotalPrice.doubleValue(),
                    feeConfigDto.getScale(),
                    Integer.parseInt(feeConfigDto.getDecimalPlace())
            );
            feeTotalPrice = new BigDecimal(tAmount+"");
        }
        
        feeTotalPrice = feeTotalPrice.multiply(cycle).setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_UP);
        String remark = "车位面积："+parkingSpaceDtos.get(0).getArea()+"\n单价："+feeConfigDto.getSquarePrice()+"\n比例系数："+feeConfigDto.getAdditionalAmount();
        return new FeeAmountDto(feeTotalPrice.doubleValue(),remark);
    }

    private FeeAmountDto computeContractAmount(FeeConfigDto feeConfigDto, String payerObjId, String payerObjType, int cycleValue, String monthFlag) {
        ContractRoomDto contractRoomDto = new ContractRoomDto();
        contractRoomDto.setContractId(payerObjId);
        contractRoomDto.setCommunityId(feeConfigDto.getCommunityId());
        List<ContractRoomDto> contractRoomDtos = contractRoomInnerServiceSMOImpl.queryContractRooms(contractRoomDto);

        //feePrice = Double.parseDouble(feeDto.getSquarePrice()) * Double.parseDouble(roomDtos.get(0).getBuiltUpArea()) + Double.parseDouble(feeDto.getAdditionalAmount());
        BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeConfigDto.getSquarePrice()));
        BigDecimal builtUpArea = new BigDecimal(0);
        for (ContractRoomDto tmpContractRoomDto : contractRoomDtos) {
            builtUpArea = builtUpArea.add(new BigDecimal(Double.parseDouble(tmpContractRoomDto.getBuiltUpArea())));
        }
        BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeConfigDto.getAdditionalAmount()));
        //feePrice = squarePrice.multiply(builtUpArea).add(additionalAmount).setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_UP);
        BigDecimal cycle = new BigDecimal(cycleValue);
        BigDecimal feeTotalPrice = (squarePrice.multiply(builtUpArea).multiply(additionalAmount));
        
        if("2".equals(monthFlag)){
            feeTotalPrice =  feeTotalPrice.setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_UP);
            double tAmount = MoneyUtil.computePriceScale(
                    feeTotalPrice.doubleValue(),
                    feeConfigDto.getScale(),
                    Integer.parseInt(feeConfigDto.getDecimalPlace())
            );
            feeTotalPrice = new BigDecimal(tAmount+"");
        }
        
        feeTotalPrice = feeTotalPrice.multiply(cycle).setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_UP);
        String remark = "合同面积："+builtUpArea.doubleValue()+"\n单价："+feeConfigDto.getSquarePrice()+"\n比例系数："+feeConfigDto.getAdditionalAmount();
        return new FeeAmountDto(feeTotalPrice.doubleValue(),remark);
    }

    private FeeAmountDto computeShopAmount(FeeConfigDto feeConfigDto, String payerObjId, String payerObjType, int cycleValue, String monthFlag) {
        BuildingShopDto shopDto = new BuildingShopDto();
        shopDto.setShopId(payerObjId);
        shopDto.setCommunityId(feeConfigDto.getCommunityId());
        List<BuildingShopDto> shopDtos = buildingShopInnerServiceSMOImpl.queryBuildingShops(shopDto);
        Assert.listOnlyOne(shopDtos, "未查到商铺信息，shopId=" + payerObjId);

        BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeConfigDto.getSquarePrice()));
        BigDecimal builtUpArea = new BigDecimal(Double.parseDouble(shopDtos.get(0).getBuiltUpArea()));
        BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeConfigDto.getAdditionalAmount()));
        BigDecimal cycle = new BigDecimal(cycleValue);

        BigDecimal feeTotalPrice = (squarePrice.multiply(builtUpArea).multiply(additionalAmount));

        if("2".equals(monthFlag)){
            feeTotalPrice = feeTotalPrice.setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_UP);
            double tAmount = MoneyUtil.computePriceScale(
                    feeTotalPrice.doubleValue(),
                    feeConfigDto.getScale(),
                    Integer.parseInt(feeConfigDto.getDecimalPlace())
            );
            feeTotalPrice = new BigDecimal(tAmount+"");
        }

        feeTotalPrice = feeTotalPrice.multiply(cycle).setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_UP);
        String remark = "商铺面积："+shopDtos.get(0).getBuiltUpArea()+"\n单价："+feeConfigDto.getSquarePrice()+"\n比例系数："+feeConfigDto.getAdditionalAmount();
        return new FeeAmountDto(feeTotalPrice.doubleValue(), remark);
    }


}