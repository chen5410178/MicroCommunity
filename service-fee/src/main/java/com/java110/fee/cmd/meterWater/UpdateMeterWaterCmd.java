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
package com.java110.fee.cmd.meterWater;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.fee.FeeAmountDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.meter.MeterWaterDto;
import com.java110.fee.bill.IComputeBillPrice;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IMeterWaterInnerServiceSMO;
import com.java110.intf.fee.IMeterWaterV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeRuleV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeV1InnerServiceSMO;
import com.java110.po.fee.PayFeePo;
import com.java110.po.meter.MeterWaterPo;
import com.java110.po.payFee.PayFeeRulePo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * 类表述：更新
 * 服务编码：meterWater.updateMeterWater
 * 请求路劲：/app/meterWater.UpdateMeterWater
 * add by 吴学文 at 2022-07-21 09:17:10 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "meterWater.updateMeterWater")
public class UpdateMeterWaterCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(UpdateMeterWaterCmd.class);

    //域
    public static final String DOMAIN_COMMON = "DOMAIN.COMMON";

    //键(水费黑名单)
    public static final String WATER_BLACK_LIST = "WATER_BLACK_LIST";

    //键(电费黑名单)
    public static final String ELECTRIC_BLACK_LIST = "ELECTRIC_BLACK_LIST";

    public static final String METER_TYPE_WATER = "2020";

    public static final String METER_TYPE_ELECTRIC = "1010";

    public static final String MSG_NOT_SYNC_FEE = "该抄表记录未同步费用";

    @Autowired
    private IMeterWaterInnerServiceSMO meterWaterInnerServiceSMOImpl;

    @Autowired
    private IMeterWaterV1InnerServiceSMO meterWaterV1InnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeRuleV1InnerServiceSMO payFeeRuleV1InnerServiceSMOImpl;

    @Autowired
    private IComputeBillPrice computeBillPriceImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "waterId", "waterId不能为空");
        Assert.hasKeyAndValue(reqJson, "curDegrees", "请求报文中未包含curDegrees");
        Assert.hasKeyAndValue(reqJson, "curReadingTime", "请求报文中未包含curReadingTime");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        String waterId = reqJson.getString("waterId");
        MeterWaterDto meterWaterDto = new MeterWaterDto();
        meterWaterDto.setWaterId(waterId);
        meterWaterDto.setCommunityId(reqJson.getString("communityId"));
        List<MeterWaterDto> meterWaterDtos = meterWaterInnerServiceSMOImpl.queryMeterWaters(meterWaterDto);
        Assert.listOnlyOne(meterWaterDtos, "数据异常未找到抄表信息");
        MeterWaterDto meterWater = meterWaterDtos.get(0);

        String curDegrees = reqJson.getString("curDegrees").trim();
        String curReadingTime = formatReadingTime(reqJson.getString("curReadingTime"));

        if (parseDegrees(curDegrees) < parseDegrees(meterWater.getPreDegrees())) {
            throw new CmdException("本期度数不能小于上期度数");
        }

        if (!StringUtil.isEmpty(meterWater.getPreReadingTime())
                && parseTime(curReadingTime).before(parseTime(meterWater.getPreReadingTime()))) {
            throw new CmdException("本期抄表时间不能早于上期抄表时间");
        }

        //todo fee_id 存的是 rule_id，按 pay_fee.rule_id 定位费用，不做 fee_id 兜底
        FeeDto feeDto = null;
        String tip = null;
        if (isBlackListCommunity(meterWater.getCommunityId(), meterWater.getMeterType())) {
            logger.warn("小区{}为抄表黑名单，抄表记录{}不生成费用，跳过费用同步", meterWater.getCommunityId(), waterId);
        } else {
            FeeDto queryFeeDto = new FeeDto();
            queryFeeDto.setRuleId(meterWater.getFeeId());
            queryFeeDto.setCommunityId(meterWater.getCommunityId());
            List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(queryFeeDto);
            if (ListUtil.isNull(feeDtos)) {
                logger.warn("抄表记录{}未查询到关联费用，仅更新抄表数据", waterId);
                tip = MSG_NOT_SYNC_FEE;
            } else {
                feeDto = feeDtos.get(0);
                if (isPaidFee(feeDto)) {
                    throw new CmdException("该费用已缴费，请先退费再修改抄表");
                }
            }
        }

        //todo 用 DB 原值补齐，避免 price 等字段被覆盖
        MeterWaterPo meterWaterPo = new MeterWaterPo();
        meterWaterPo.setWaterId(waterId);
        meterWaterPo.setCommunityId(meterWater.getCommunityId());
        meterWaterPo.setCurDegrees(curDegrees);
        meterWaterPo.setCurReadingTime(curReadingTime);
        meterWaterPo.setRemark(reqJson.getString("remark"));
        meterWaterPo.setPrice(meterWater.getPrice());
        meterWaterPo.setObjId(meterWater.getObjId());
        meterWaterPo.setObjName(meterWater.getObjName());
        meterWaterPo.setObjType(meterWater.getObjType());
        meterWaterPo.setMeterType(meterWater.getMeterType());
        meterWaterPo.setPreDegrees(meterWater.getPreDegrees());
        meterWaterPo.setPreReadingTime(meterWater.getPreReadingTime());
        meterWaterPo.setFeeId(meterWater.getFeeId());
        int flag = meterWaterV1InnerServiceSMOImpl.updateMeterWater(meterWaterPo);
        if (flag < 1) {
            throw new CmdException("更新抄表数据失败");
        }

        if (feeDto != null) {
            //todo 复用抄表建费公式重算金额与费用说明
            FeeAmountDto feeAmountDto = computeBillPriceImpl.computeBillReceiveAmount(meterWater.getFeeId(), 1);

            PayFeePo payFeePo = new PayFeePo();
            payFeePo.setFeeId(feeDto.getFeeId());
            payFeePo.setCommunityId(feeDto.getCommunityId());
            payFeePo.setReceivableAmount(feeAmountDto.getAmount() + "");
            payFeePo.setOweAmount(feeAmountDto.getAmount() + "");
            payFeePo.setRemark(feeAmountDto.getRemark());
            payFeePo.setStartTime(meterWater.getPreReadingTime());
            payFeePo.setEndTime(curReadingTime);
            payFeePo.setFeeTime(computeFeeTime(meterWater.getPreReadingTime(), feeDto.getDeductFrom()));
            flag = payFeeV1InnerServiceSMOImpl.updatePayFee(payFeePo);
            if (flag < 1) {
                throw new CmdException("更新费用数据失败");
            }

            PayFeeRulePo payFeeRulePo = new PayFeeRulePo();
            payFeeRulePo.setRuleId(meterWater.getFeeId());
            payFeeRulePo.setCommunityId(meterWater.getCommunityId());
            payFeeRulePo.setMaxTime(curReadingTime);
            payFeeRulePo.setCurYearMonth(DateUtil.getFormatTimeStringB(parseTime(curReadingTime)));
            payFeeRulePo.setState(FeeDto.STATE_FINISH);
            flag = payFeeRuleV1InnerServiceSMOImpl.updatePayFeeRule(payFeeRulePo);
            if (flag < 1) {
                throw new CmdException("更新费用规则失败");
            }
        }

        if (StringUtil.isEmpty(tip)) {
            cmdDataFlowContext.setResponseEntity(ResultVo.success());
            return;
        }
        cmdDataFlowContext.setResponseEntity(ResultVo.createResponseEntity(ResultVo.CODE_OK, ResultVo.MSG_OK, tip));
    }

    private boolean isBlackListCommunity(String communityId, String meterType) {
        if (StringUtil.isEmpty(communityId) || StringUtil.isEmpty(meterType)) {
            return false;
        }
        if (METER_TYPE_WATER.equals(meterType)) {
            return getBlackList(WATER_BLACK_LIST).contains(communityId);
        }
        if (METER_TYPE_ELECTRIC.equals(meterType)) {
            return getBlackList(ELECTRIC_BLACK_LIST).contains(communityId);
        }
        return false;
    }

    private List<String> getBlackList(String key) {
        String remark = MappingCache.getRemark(DOMAIN_COMMON, key);
        if (StringUtil.isEmpty(remark)) {
            return new ArrayList<>();
        }
        return Arrays.asList(remark.split(","));
    }

    private boolean isPaidFee(FeeDto feeDto) {
        if (StringUtil.isEmpty(feeDto.getReceivableAmount()) || StringUtil.isEmpty(feeDto.getOweAmount())) {
            return false;
        }
        try {
            return Double.parseDouble(feeDto.getReceivableAmount()) != Double.parseDouble(feeDto.getOweAmount());
        } catch (Exception e) {
            logger.warn("费用{}金额异常，应收：{}，欠费：{}", feeDto.getFeeId(), feeDto.getReceivableAmount(),
                    feeDto.getOweAmount());
            return false;
        }
    }

    private String computeFeeTime(String startTime, String deductFrom) {
        if (StringUtil.isEmpty(startTime)) {
            return null;
        }
        Date feeTime = parseTime(startTime);
        if (FeeDto.DEDUCT_FROM_NEXT_MONTH.equals(deductFrom)) {
            feeTime = DateUtil.getNextMonthDate(feeTime, 1);
        } else if (FeeDto.DEDUCT_FROM_HISTORY_MONTH.equals(deductFrom)) {
            feeTime = DateUtil.getNextMonthDate(feeTime, -1);
        }
        return DateUtil.getFormatTimeStringB(feeTime);
    }

    private String formatReadingTime(String readingTime) {
        return DateUtil.getFormatTimeStringA(parseTime(readingTime));
    }

    private Date parseTime(String time) {
        if (StringUtil.isEmpty(time)) {
            throw new CmdException("抄表时间不能为空");
        }
        String tmpTime = time.trim();
        if (tmpTime.length() == 10) {
            tmpTime = tmpTime + " 23:59:59";
        }
        try {
            return DateUtil.getDateFromStringA(tmpTime);
        } catch (Exception e) {
            throw new CmdException("抄表时间格式错误，格式为yyyy-MM-dd HH:mm:ss");
        }
    }

    private double parseDegrees(String degrees) {
        if (StringUtil.isEmpty(degrees)) {
            throw new CmdException("本期度数不能为空");
        }
        try {
            return Double.parseDouble(degrees.trim());
        } catch (Exception e) {
            throw new CmdException("本期度数格式错误");
        }
    }
}
