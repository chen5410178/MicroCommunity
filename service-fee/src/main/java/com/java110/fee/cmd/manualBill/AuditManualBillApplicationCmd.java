package com.java110.fee.cmd.manualBill;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.fee.ManualBillGenerationDetailDto;
import com.java110.dto.fee.ManualBillGenerationDto;
import com.java110.dto.payFee.PayFeeRuleDto;
import com.java110.fee.bill.ManualBillGenerator;
import com.java110.intf.fee.IManualBillGenerationV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeRuleV1InnerServiceSMO;
import com.java110.po.fee.ManualBillGenerationDetailPo;
import com.java110.po.fee.ManualBillGenerationPo;
import com.java110.po.payFee.PayFeeRulePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@Java110Cmd(serviceCode = "fee.auditManualBillApplication")
public class AuditManualBillApplicationCmd extends Cmd {

    @Autowired
    private IManualBillGenerationV1InnerServiceSMO manualBillGenerationV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeRuleV1InnerServiceSMO payFeeRuleV1InnerServiceSMOImpl;

    @Autowired
    private ManualBillGenerator manualBillGenerator;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "applicationId", "未包含申请ID");
        Assert.hasKeyAndValue(reqJson, "state", "未包含审核状态");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
        String state = reqJson.getString("state");
        if (!"1002".equals(state) && !"1003".equals(state)) {
            throw new CmdException("审核状态必须为1002(通过)或1003(驳回)");
        }
        if ("1003".equals(state)) {
            Assert.hasKeyAndValue(reqJson, "auditRemark", "驳回时必须填写驳回原因");
        }
        reqJson.remove("userId");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        String applicationId = reqJson.getString("applicationId");
        String state = reqJson.getString("state");
        String auditRemark = reqJson.getString("auditRemark");
        String communityId = reqJson.getString("communityId");

        ManualBillGenerationDto queryDto = new ManualBillGenerationDto();
        queryDto.setApplicationId(applicationId);
        queryDto.setCommunityId(communityId);
        List<ManualBillGenerationDto> applications = manualBillGenerationV1InnerServiceSMOImpl.queryManualBillGenerations(queryDto);
        Assert.listOnlyOne(applications, "申请不存在");

        ManualBillGenerationDto application = applications.get(0);
        if (!"1001".equals(application.getState())) {
            throw new CmdException("当前申请状态不允许审核，当前状态：" + application.getState());
        }

        if ("1003".equals(state)) {
            ManualBillGenerationPo updatePo = new ManualBillGenerationPo();
            updatePo.setApplicationId(applicationId);
            updatePo.setState("1003");
            updatePo.setAuditUserId(reqJson.getString("userId"));
            updatePo.setAuditUserName(reqJson.getString("userName"));
            updatePo.setAuditTime(new Date());
            updatePo.setAuditRemark(auditRemark);
            manualBillGenerationV1InnerServiceSMOImpl.updateManualBillGeneration(updatePo);
            context.setResponseEntity(ResultVo.success());
            return;
        }

        ManualBillGenerationDetailDto detailQuery = new ManualBillGenerationDetailDto();
        detailQuery.setApplicationId(applicationId);
        detailQuery.setCommunityId(communityId);
        List<ManualBillGenerationDetailDto> details = manualBillGenerationV1InnerServiceSMOImpl.queryManualBillGenerationDetails(detailQuery);

        int successCount = 0;
        int errorCount = 0;

        for (ManualBillGenerationDetailDto detail : details) {
            if (!"1001".equals(detail.getState())) {
                continue;
            }
            try {
                PayFeeRuleDto ruleQuery = new PayFeeRuleDto();
                ruleQuery.setRuleId(detail.getRuleId());
                ruleQuery.setCommunityId(communityId);
                List<PayFeeRuleDto> payFeeRuleDtos = payFeeRuleV1InnerServiceSMOImpl.queryPayFeeRules(ruleQuery);
                Assert.listOnlyOne(payFeeRuleDtos, "费用规则不存在，ruleId=" + detail.getRuleId());

                PayFeeRulePo payFeeRulePo = BeanConvertUtil.covertBean(payFeeRuleDtos.get(0), PayFeeRulePo.class);
                manualBillGenerator.manualGeneratePayFee(payFeeRulePo, application.getBillMonth(), detail.getPrepaymentPeriod());

                ManualBillGenerationDetailPo updateDetail = new ManualBillGenerationDetailPo();
                updateDetail.setDetailId(detail.getDetailId());
                updateDetail.setState("1002");
                manualBillGenerationV1InnerServiceSMOImpl.updateManualBillGenerationDetail(updateDetail);
                successCount++;
            } catch (Exception e) {
                ManualBillGenerationDetailPo updateDetail = new ManualBillGenerationDetailPo();
                updateDetail.setDetailId(detail.getDetailId());
                updateDetail.setState("1003");
                updateDetail.setErrorMsg(e.getMessage());
                manualBillGenerationV1InnerServiceSMOImpl.updateManualBillGenerationDetail(updateDetail);
                errorCount++;
            }
        }

        ManualBillGenerationPo updatePo = new ManualBillGenerationPo();
        updatePo.setApplicationId(applicationId);
        updatePo.setState("1002");
        updatePo.setAuditUserId(reqJson.getString("userId"));
        updatePo.setAuditUserName(reqJson.getString("userName"));
        updatePo.setAuditTime(new Date());
        updatePo.setAuditRemark(auditRemark);
        updatePo.setSuccessCount(successCount);
        updatePo.setErrorCount(errorCount);
        manualBillGenerationV1InnerServiceSMOImpl.updateManualBillGeneration(updatePo);

        context.setResponseEntity(ResultVo.success());
    }
}
