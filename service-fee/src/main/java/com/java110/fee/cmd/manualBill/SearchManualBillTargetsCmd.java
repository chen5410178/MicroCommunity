package com.java110.fee.cmd.manualBill;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.payFee.PayFeeRuleDto;
import com.java110.intf.fee.IPayFeeConfigV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeRuleV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Java110Cmd(serviceCode = "fee.searchManualBillTargets")
public class SearchManualBillTargetsCmd extends Cmd {

    @Autowired
    private IPayFeeRuleV1InnerServiceSMO payFeeRuleV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeConfigV1InnerServiceSMO payFeeConfigV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
        Assert.hasKeyAndValue(reqJson, "configId", "未包含费用项ID");
        Assert.hasKeyAndValue(reqJson, "billMonth", "未包含目标账单月份");
        super.validatePageInfo(reqJson);
        reqJson.remove("userId");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        String communityId = reqJson.getString("communityId");
        String configId = reqJson.getString("configId");
        String billMonth = reqJson.getString("billMonth");

        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setConfigId(configId);
        feeConfigDto.setCommunityId(communityId);
        List<FeeConfigDto> feeConfigDtos = payFeeConfigV1InnerServiceSMOImpl.queryPayFeeConfigs(feeConfigDto);
        Assert.listOnlyOne(feeConfigDtos, "费用项不存在");

        FeeConfigDto config = feeConfigDtos.get(0);
        if (!FeeDto.FEE_FLAG_CYCLE.equals(config.getFeeFlag())) {
            throw new CmdException("仅支持周期性费用（feeFlag=1003006），当前费用为：" + config.getFeeFlag());
        }

        int prepaymentPeriod = 1;
        if (StringUtil.isNumber(config.getPrepaymentPeriod())) {
            prepaymentPeriod = Integer.parseInt(config.getPrepaymentPeriod());
            if (prepaymentPeriod <= 0) {
                prepaymentPeriod = 1;
            }
        }

        // billMonth 标准化为 yyyy-MM，并计算账单日区间 [startMonth, endMonth)
        String startMonth = billMonth.length() == 7 ? billMonth : billMonth.substring(0, 5) + "0" + billMonth.substring(5);
        Date startDate;
        try {
            startDate = DateUtil.getDateFromString(startMonth + "-01", DateUtil.DATE_FORMATE_STRING_B);
        } catch (ParseException e) {
            throw new CmdException("目标账单月份格式不正确：" + billMonth);
        }
        Date endDate = DateUtil.getNextMonthDate(startDate, 1);
        String endMonth = DateUtil.getFormatTimeStringQ(endDate);

        PayFeeRuleDto payFeeRuleDto = BeanConvertUtil.covertBean(reqJson, PayFeeRuleDto.class);
        payFeeRuleDto.setCommunityId(communityId);
        payFeeRuleDto.setConfigId(configId);
        payFeeRuleDto.setState(FeeDto.STATE_DOING);
        payFeeRuleDto.setFeeFlag(FeeDto.FEE_FLAG_CYCLE);
        payFeeRuleDto.setCurYearMonthStart(startMonth);
        payFeeRuleDto.setCurYearMonthEnd(endMonth);

        int total = payFeeRuleV1InnerServiceSMOImpl.queryManualBillTargetRulesCount(payFeeRuleDto);
        List<PayFeeRuleDto> payFeeRuleDtos = payFeeRuleV1InnerServiceSMOImpl.queryManualBillTargetRules(payFeeRuleDto);

        List<Map<String, Object>> resultList = new ArrayList<>();
        if (payFeeRuleDtos != null && !payFeeRuleDtos.isEmpty()) {
            for (PayFeeRuleDto rule : payFeeRuleDtos) {
                Map<String, Object> item = new HashMap<>();
                item.put("ruleId", rule.getRuleId());
                item.put("configId", rule.getConfigId());
                item.put("feeName", config.getFeeName());
                item.put("roomId", rule.getPayerObjId());
                item.put("roomName", rule.getPayerObjName());
                item.put("ownerId", rule.getOwnerId());
                item.put("ownerName", rule.getOwnerName());
                item.put("feeTypeCd", rule.getFeeTypeCd());
                item.put("feeFlag", rule.getFeeFlag());
                item.put("curYearMonth", rule.getCurYearMonth());
                item.put("prepaymentPeriod", prepaymentPeriod);

                resultList.add(item);
            }
        }

        int row = reqJson.getInteger("row");
        context.setResponseEntity(ResultVo.createResponseEntity((int) Math.ceil((double) total / (double) row), total, resultList));
    }
}
