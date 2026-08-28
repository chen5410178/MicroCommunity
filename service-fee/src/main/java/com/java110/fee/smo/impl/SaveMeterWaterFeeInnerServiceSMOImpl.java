/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.fee.smo.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.fee.SaveMeterWaterFeeDto;
import com.java110.fee.bill.ICycleConvertOnceFee;
import com.java110.intf.fee.IMeterWaterV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeRuleV1InnerServiceSMO;
import com.java110.intf.fee.ISaveMeterWaterFeeInnerServiceSMO;
import com.java110.po.meter.MeterWaterPo;
import com.java110.po.payFee.PayFeeRulePo;
import com.java110.utils.exception.CmdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 类表述： 保存抄表记录并按费用规则生成账单的内部服务实现，
 * 封装"保存 meter_water（fee_id = ruleId）-> savePayFeeRules -> rulesGeneratePayFees"标准链路，
 * 供手动抄表(SaveMeterWaterCmd)与批量导入抄表(ImportMeterWaterFeeQueueDataAdapt)复用
 * 服务之前调用的接口实现类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2022-07-21 09:17:10 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 */
@RestController
public class SaveMeterWaterFeeInnerServiceSMOImpl extends BaseServiceSMO implements ISaveMeterWaterFeeInnerServiceSMO {

    @Autowired
    private IMeterWaterV1InnerServiceSMO meterWaterV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeRuleV1InnerServiceSMO payFeeRuleV1InnerServiceSMOImpl;

    @Autowired
    private ICycleConvertOnceFee cycleConvertOnceFeeImpl;

    @Override
    @Java110Transactional
    public int saveMeterWaterAndFee(@RequestBody SaveMeterWaterFeeDto saveMeterWaterFeeDto) {
        MeterWaterPo meterWaterPo = saveMeterWaterFeeDto.getMeterWaterPo();
        PayFeeRulePo payFeeRulePo = saveMeterWaterFeeDto.getPayFeeRulePo();

        //todo 保存抄表记录
        int flag = meterWaterV1InnerServiceSMOImpl.saveMeterWater(meterWaterPo);
        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        //todo 保存费用规则
        List<PayFeeRulePo> payFeeRulePos = new ArrayList<>();
        payFeeRulePos.add(payFeeRulePo);
        payFeeRuleV1InnerServiceSMOImpl.savePayFeeRules(payFeeRulePos);

        //todo 走账单模式生成费用
        cycleConvertOnceFeeImpl.rulesGeneratePayFees(payFeeRulePos);
        return 1;
    }
}
