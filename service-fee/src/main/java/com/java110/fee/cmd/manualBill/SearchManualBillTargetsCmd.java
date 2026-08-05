package com.java110.fee.cmd.manualBill;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.payFee.PayFeeRuleDto;
import com.java110.fee.bill.IComputeBillPrice;
import com.java110.intf.fee.IPayFeeConfigV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeRuleV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Java110Cmd(serviceCode = "fee.searchManualBillTargets")
public class SearchManualBillTargetsCmd extends Cmd {

    @Autowired
    private IPayFeeRuleV1InnerServiceSMO payFeeRuleV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeConfigV1InnerServiceSMO payFeeConfigV1InnerServiceSMOImpl;

    @Autowired
    private IComputeBillPrice computeBillPriceImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
        Assert.hasKeyAndValue(reqJson, "configId", "未包含费用项ID");
        Assert.hasKeyAndValue(reqJson, "billMonth", "未包含目标账单月份");
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

        PayFeeRuleDto payFeeRuleDto = new PayFeeRuleDto();
        payFeeRuleDto.setCommunityId(communityId);
        payFeeRuleDto.setConfigId(configId);
        payFeeRuleDto.setState(FeeDto.STATE_DOING);
        payFeeRuleDto.setFeeFlag(FeeDto.FEE_FLAG_CYCLE);
        List<PayFeeRuleDto> payFeeRuleDtos = payFeeRuleV1InnerServiceSMOImpl.queryPayFeeRules(payFeeRuleDto);

        if (payFeeRuleDtos != null) {
            payFeeRuleDtos.removeIf(rule ->
                    rule.getCurYearMonth() != null && rule.getCurYearMonth().compareTo(billMonth) > 0);
        }

        List<Map<String, Object>> resultList = new ArrayList<>();
        if (payFeeRuleDtos != null && !payFeeRuleDtos.isEmpty()) {
            for (PayFeeRuleDto rule : payFeeRuleDtos) {
                Map<String, Object> item = new HashMap<>();
                item.put("ruleId", rule.getRuleId());
                item.put("configId", rule.getConfigId());
                item.put("feeName", config.getFeeName());
                item.put("roomId", rule.getPayerObjId());
                item.put("roomName", "");
                item.put("ownerId", "");
                item.put("ownerName", "");
                item.put("feeTypeCd", rule.getFeeTypeCd());
                item.put("feeFlag", rule.getFeeFlag());
                item.put("curYearMonth", rule.getCurYearMonth());
                item.put("prepaymentPeriod", prepaymentPeriod);

                FeeDto feeDto = new FeeDto();
                feeDto.setPayerObjType(rule.getPayerObjType());
                feeDto.setCommunityId(communityId);
                feeDto.setPayerObjId(rule.getPayerObjId());
                OwnerDto ownerDto = computeFeeSMOImpl.getFeeOwnerDto(feeDto);
                String payerObjName = computeFeeSMOImpl.getFeeObjName(feeDto);
                item.put("roomName", payerObjName);
                if (ownerDto != null) {
                    item.put("ownerId", ownerDto.getOwnerId());
                    item.put("ownerName", ownerDto.getName());
                }

                try {
                    com.java110.dto.fee.FeeAmountDto feeAmountDto = computeBillPriceImpl.computeBillReceiveAmount(rule.getRuleId(), prepaymentPeriod);
                    item.put("amount", feeAmountDto.getAmount() + "");
                } catch (Exception e) {
                    item.put("amount", rule.getAmount());
                }

                resultList.add(item);
            }
        }

        context.setResponseEntity(ResultVo.createResponseEntity(resultList));
    }
}
