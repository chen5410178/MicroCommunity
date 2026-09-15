package com.java110.job.task.invoice;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.MallDataDto;
import com.java110.dto.invoice.InvoiceApplyDto;
import com.java110.dto.invoice.InvoiceApplyItemDto;
import com.java110.dto.invoice.InvoiceMallStateDto;
import com.java110.dto.task.TaskDto;
import com.java110.intf.acct.IInvoiceApplyItemV1InnerServiceSMO;
import com.java110.intf.acct.IInvoiceApplyV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeDetailV1InnerServiceSMO;
import com.java110.intf.job.IMallInnerServiceSMO;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 缴费明细开票状态兜底回写(定时任务)
 * 物业端不维护发票状态、不做状态展示(状态/失败原因/PDF 由查询时按 applyId 实时取商城),
 * 本任务按 applyId(商城 ext_apply_id) 批量取商城权威结果, 仅回写缴费明细 pay_fee_detail.open_invoice
 * (开票成功Y/开票失败N, 待审核或待开票不动)。
 */
@Component
public class InvoiceStateSyncJob extends TaskSystemQuartz {

    private static final String MALL_API_CODE = "queryInvoiceStatusBatchBmoImpl";
    private static final String OI_ID_NOT_SUBMIT = "-1";
    private static final String OPEN_INVOICE_YES = "Y";
    private static final String OPEN_INVOICE_NO = "N";
    private static final int BATCH_SIZE = 100;

    @Autowired
    private IInvoiceApplyV1InnerServiceSMO invoiceApplyV1InnerServiceSMOImpl;

    @Autowired
    private IInvoiceApplyItemV1InnerServiceSMO invoiceApplyItemV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeDetailV1InnerServiceSMO payFeeDetailV1InnerServiceSMOImpl;

    @Autowired
    private IMallInnerServiceSMO mallInnerServiceSMOImpl;

    @Override
    protected void process(TaskDto taskDto) throws Exception {
        logger.debug("开始执行缴费明细开票状态兜底回写");

        InvoiceApplyDto queryDto = new InvoiceApplyDto();
        queryDto.setStatusCd("0");
        queryDto.setOiIdNotEqual(OI_ID_NOT_SUBMIT);
        queryDto.setPage(-1);
        List<InvoiceApplyDto> invoiceApplys = invoiceApplyV1InnerServiceSMOImpl.queryInvoiceApplys(queryDto);
        if (ListUtil.isNull(invoiceApplys)) {
            return;
        }

        Map<String, InvoiceMallStateDto> mallStates = queryMallStates(invoiceApplys);
        if (mallStates.isEmpty()) {
            return;
        }

        for (InvoiceApplyDto invoiceApply : invoiceApplys) {
            InvoiceMallStateDto mallState = mallStates.get(invoiceApply.getApplyId());
            if (mallState == null) {
                continue;
            }
            try {
                syncOne(invoiceApply, mallState);
            } catch (Exception e) {
                logger.error("回写缴费明细开票状态失败 applyId: {}", invoiceApply.getApplyId(), e);
            }
        }
    }

    /**
     * 按商城结果回写缴费明细(不修改物业端 state, 不回填任何发票展示字段)
     */
    private void syncOne(InvoiceApplyDto invoiceApply, InvoiceMallStateDto mallState) {
        if (InvoiceMallStateDto.STATE_DONE.equals(mallState.getState())) {
            writeBackFeeDetail(invoiceApply.getApplyId(), OPEN_INVOICE_YES);
            return;
        }
        if (InvoiceMallStateDto.STATE_FAIL.equals(mallState.getState())) {
            writeBackFeeDetail(invoiceApply.getApplyId(), OPEN_INVOICE_NO);
        }
    }

    /**
     * 回写申请单关联的缴费明细开票状态
     */
    private void writeBackFeeDetail(String applyId, String openInvoice) {
        InvoiceApplyItemDto itemDto = new InvoiceApplyItemDto();
        itemDto.setApplyId(applyId);
        itemDto.setItemType(InvoiceApplyItemDto.ITEM_TYPE_FEE);
        List<InvoiceApplyItemDto> items = invoiceApplyItemV1InnerServiceSMOImpl.queryInvoiceApplyItems(itemDto);
        if (ListUtil.isNull(items)) {
            return;
        }
        for (InvoiceApplyItemDto item : items) {
            PayFeeDetailPo payFeeDetailPo = new PayFeeDetailPo();
            payFeeDetailPo.setDetailId(item.getItemObjId());
            payFeeDetailPo.setCommunityId(item.getCommunityId());
            payFeeDetailPo.setOpenInvoice(openInvoice);
            payFeeDetailV1InnerServiceSMOImpl.updatePayFeeDetailNew(payFeeDetailPo);
        }
    }

    /**
     * 分批调商城批量状态接口(queryInvoiceStatusBatchBmoImpl): 按 applyIds(商城 ext_apply_id) 查询.
     * 返回 applyId -> 商城状态
     */
    private Map<String, InvoiceMallStateDto> queryMallStates(List<InvoiceApplyDto> invoiceApplys) {
        Map<String, InvoiceMallStateDto> mallStates = new HashMap<>();
        List<String> applyIds = new ArrayList<>();
        for (InvoiceApplyDto invoiceApply : invoiceApplys) {
            if (StringUtil.isEmpty(invoiceApply.getApplyId()) || applyIds.contains(invoiceApply.getApplyId())) {
                continue;
            }
            applyIds.add(invoiceApply.getApplyId());
            if (applyIds.size() < BATCH_SIZE) {
                continue;
            }
            queryMallStatesByBatch(applyIds, mallStates);
            applyIds = new ArrayList<>();
        }
        if (!applyIds.isEmpty()) {
            queryMallStatesByBatch(applyIds, mallStates);
        }
        return mallStates;
    }

    private void queryMallStatesByBatch(List<String> applyIds, Map<String, InvoiceMallStateDto> mallStates) {
        JSONObject reqParam = new JSONObject();
        reqParam.put("applyIds", applyIds);
        ResultVo resultVo;
        try {
            resultVo = mallInnerServiceSMOImpl.postMallData(new MallDataDto(MALL_API_CODE, reqParam));
        } catch (Exception e) {
            logger.warn("查询商城开票状态异常", e);
            return;
        }
        if (resultVo == null || resultVo.getCode() != ResultVo.CODE_OK || resultVo.getData() == null) {
            logger.warn("查询商城开票状态未生效: {}", resultVo == null ? "null" : resultVo.getMsg());
            return;
        }
        JSONArray invoices = ((JSONObject) resultVo.getData()).getJSONArray("invoices");
        if (invoices == null || invoices.isEmpty()) {
            return;
        }
        for (int i = 0; i < invoices.size(); i++) {
            InvoiceMallStateDto mallState = BeanConvertUtil.covertBean(invoices.getJSONObject(i), InvoiceMallStateDto.class);
            if (StringUtil.isEmpty(mallState.getApplyId())) {
                continue;
            }
            mallStates.put(mallState.getApplyId(), mallState);
        }
    }
}
