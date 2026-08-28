package com.java110.dto.fee;

import com.java110.po.meter.MeterWaterPo;
import com.java110.po.payFee.PayFeeRulePo;

import java.io.Serializable;

/**
 * 抄表建费入参 DTO：封装抄表记录与费用规则，
 * 供手动抄表(SaveMeterWaterCmd)与批量导入抄表(ImportMeterWaterFeeQueueDataAdapt)
 * 统一走"抄表 -> 规则 -> 账单"标准链路使用。
 */
public class SaveMeterWaterFeeDto implements Serializable {

    private MeterWaterPo meterWaterPo;

    private PayFeeRulePo payFeeRulePo;

    public MeterWaterPo getMeterWaterPo() {
        return meterWaterPo;
    }

    public void setMeterWaterPo(MeterWaterPo meterWaterPo) {
        this.meterWaterPo = meterWaterPo;
    }

    public PayFeeRulePo getPayFeeRulePo() {
        return payFeeRulePo;
    }

    public void setPayFeeRulePo(PayFeeRulePo payFeeRulePo) {
        this.payFeeRulePo = payFeeRulePo;
    }
}
