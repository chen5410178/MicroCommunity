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
package com.java110.intf.fee;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.fee.SaveMeterWaterFeeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 类表述： 保存抄表记录并按费用规则生成账单的内部服务接口，
 * 统一"抄表 -> 规则 -> 账单"标准链路，供手动抄表与批量导入抄表复用
 * 服务之前调用的接口类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2022-07-21 09:17:10 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 */
@FeignClient(name = "fee-service", configuration = {FeignConfiguration.class})
@RequestMapping("/saveMeterWaterFeeApi")
public interface ISaveMeterWaterFeeInnerServiceSMO {

    /**
     * 保存抄表记录并按规则生成账单
     * meter_water.fee_id = ruleId 与 pay_fee.rule_id 一对一
     *
     * @param saveMeterWaterFeeDto 抄表记录 + 费用规则
     * @return 大于0 成功，小于等于0 失败
     */
    @RequestMapping(value = "/saveMeterWaterAndFee", method = RequestMethod.POST)
    int saveMeterWaterAndFee(@RequestBody SaveMeterWaterFeeDto saveMeterWaterFeeDto);
}
