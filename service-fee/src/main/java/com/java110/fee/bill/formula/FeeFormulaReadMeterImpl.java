package com.java110.fee.bill.formula;

import com.java110.core.factory.CommunitySettingFactory;
import com.java110.dto.fee.FeeAmountDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.meter.MeterTypeDto;
import com.java110.dto.meter.MeterWaterDto;
import com.java110.fee.bill.IFeeFormula;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IMeterTypeV1InnerServiceSMO;
import com.java110.intf.fee.IMeterWaterV1InnerServiceSMO;
import com.java110.intf.store.IContractRoomInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.utils.constant.FeeConfigConstant;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.MoneyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 抄表公式
 */
@Service("feeFormula_5005")
public class FeeFormulaReadMeterImpl implements IFeeFormula {

    @Autowired
    private IMeterWaterV1InnerServiceSMO meterWaterV1InnerServiceSMOImpl;

    @Autowired
    private IMeterTypeV1InnerServiceSMO meterTypeV1InnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IContractRoomInnerServiceSMO contractRoomInnerServiceSMOImpl;

    @Override
    public FeeAmountDto computeFeeAmount(FeeConfigDto feeConfigDto, String ruleId, String payerObjId, String payerObjType, int cycleValue, double amountValue) {

        MeterWaterDto meterWaterDto = new MeterWaterDto();
        meterWaterDto.setFeeId(ruleId);
        meterWaterDto.setCommunityId(feeConfigDto.getCommunityId());
        List<MeterWaterDto> meterWaterDtos = meterWaterV1InnerServiceSMOImpl.queryMeterWaters(meterWaterDto);
        if (ListUtil.isNull(meterWaterDtos)) {
            return new FeeAmountDto(0.0, "抄表记录不存在");
        }

        MeterTypeDto meterTypeDto = new MeterTypeDto();
        meterTypeDto.setTypeId(meterWaterDtos.get(0).getMeterType());
        meterTypeDto.setCommunityId(feeConfigDto.getCommunityId());
        String radio = "1";
        List<MeterTypeDto> meterTypeDtos = meterTypeV1InnerServiceSMOImpl.queryMeterTypes(meterTypeDto);
        if (!ListUtil.isNull(meterTypeDtos)) {
            radio = meterTypeDtos.get(0).getRatio();
        }

        BigDecimal curDegree = new BigDecimal(meterWaterDtos.get(0).getCurDegrees());
        BigDecimal preDegree = new BigDecimal(meterWaterDtos.get(0).getPreDegrees());
        BigDecimal squarePrice = new BigDecimal(feeConfigDto.getSquarePrice());
        BigDecimal additionalAmount = new BigDecimal(feeConfigDto.getAdditionalAmount());
        BigDecimal sub = curDegree.subtract(preDegree);
//        feePrice = sub.multiply(squarePrice)
//                .add(additionalAmount)
//                .setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_UP);

        BigDecimal cycle = new BigDecimal(cycleValue);

        // 添加monthFlag逻辑处理
        String monthFlag = CommunitySettingFactory.getValue(feeConfigDto.getCommunityId(), "FEE_MONTH_FLAG");
        BigDecimal feePrice = sub.multiply(squarePrice).add(additionalAmount);
        
        if ("2".equals(monthFlag)) {
            feePrice = feePrice.setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_UP);
            double tAmount = MoneyUtil.computePriceScale(
                    feePrice.doubleValue(),
                    feeConfigDto.getScale(),
                    Integer.parseInt(feeConfigDto.getDecimalPlace())
            );
            feePrice = new BigDecimal(tAmount + "");
        }
        
        BigDecimal feeTotalPrice = feePrice.multiply(cycle)
                .multiply(new BigDecimal(radio))
                .setScale(FeeConfigConstant.FEE_SCALE, BigDecimal.ROUND_HALF_UP);


        String remark = "上期度数：" + preDegree.doubleValue() + "\n本期度数：" + curDegree.doubleValue() + "\n用量：" + sub.doubleValue() + "\n倍率：" + radio;
        return new FeeAmountDto(feeTotalPrice.doubleValue(), remark);
    }


}
