package com.java110.fee.bill;

import com.java110.dto.fee.FeeAmountDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.payFee.PayFeeRuleDto;
import com.java110.intf.fee.IPayFeeConfigV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeRuleV1InnerServiceSMO;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.MoneyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComputeBillPriceImpl implements IComputeBillPrice {

    @Autowired
    private IPayFeeRuleV1InnerServiceSMO payFeeRuleV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeConfigV1InnerServiceSMO payFeeConfigV1InnerServiceSMOImpl;

    @Override
    public FeeAmountDto computeBillReceiveAmount(PayFeeRuleDto payFeeRuleDto, int cycle) {

        // 一次性 缴费周期直接为1
        if (FeeDto.FEE_FLAG_ONCE.equals(payFeeRuleDto.getFeeFlag())) {
            cycle = 1;
        }

        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setConfigId(payFeeRuleDto.getConfigId());
        feeConfigDto.setCommunityId(payFeeRuleDto.getCommunityId());
        List<FeeConfigDto> feeConfigDtos = payFeeConfigV1InnerServiceSMOImpl.queryPayFeeConfigs(feeConfigDto);
        if (ListUtil.isNull(feeConfigDtos)) {
            throw new IllegalArgumentException("费用项不存在");
        }

        String formula = feeConfigDtos.get(0).getComputingFormula();

        IFeeFormula feeFormula = ApplicationContextFactory.getBean("feeFormula_" + formula, IFeeFormula.class);
        if (feeFormula == null) {
            throw new IllegalArgumentException("不支持的公式");
        }

        FeeAmountDto amount = feeFormula.computeFeeAmount(feeConfigDtos.get(0),payFeeRuleDto.getRuleId(), payFeeRuleDto.getPayerObjId(), payFeeRuleDto.getPayerObjType(), cycle,payFeeRuleDto.getAmount());

        // 处理金额 四首五入
        double money = MoneyUtil.computePriceScale(amount.getAmount(), feeConfigDtos.get(0).getScale(), Integer.parseInt(feeConfigDtos.get(0).getDecimalPlace()));
        amount.setAmount(money);
        return amount;
    }

    @Override
    public FeeAmountDto computeBillReceiveAmount(String ruleId, int cycle) {

        PayFeeRuleDto payFeeRuleDto = new PayFeeRuleDto();
        payFeeRuleDto.setRuleId(ruleId);
        List<PayFeeRuleDto> payFeeRuleDtos = payFeeRuleV1InnerServiceSMOImpl.queryPayFeeRules(payFeeRuleDto);
        if (ListUtil.isNull(payFeeRuleDtos)) {
            throw new IllegalArgumentException("未查询到账单信息");
        }
        FeeAmountDto receiveAmount = computeBillReceiveAmount(payFeeRuleDtos.get(0), cycle);
        return receiveAmount;
    }
}
