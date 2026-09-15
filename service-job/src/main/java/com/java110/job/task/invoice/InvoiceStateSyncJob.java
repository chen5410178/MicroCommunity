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
import com.java110.po.invoice.InvoiceApplyPo;
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
 * 商城开票状态兜底同步(定时任务)
 * 物业端不维护发票状态, 商城 invoice_order 为唯一权威状态;
 * 本任务扫描已提交商城(oi_id != '-1')的申请单, 按商城结果回写缴费明细 pay_fee_detail.open_invoice
 * (开票成功Y/开票失败N, 待审核或待开票不动), 并兜底回填发票号。
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
        logger.debug("开始执行商城开票状态兜底同步");

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
            InvoiceMallStateDto mallState = mallStates.get(invoiceApply.getOiId());
            if (mallState == null) {
                continue;
            }
            try {
                syncOne(invoiceApply, mallState);
            } catch (Exception e) {
                logger.error("同步商城开票状态失败 applyId: {}", invoiceApply.getApplyId(), e);
            }
        }
    }

    /**
     * 按商城结果回写缴费明细并兜底回填发票号(不修改物业端 state)
     */
    private void syncOne(InvoiceApplyDto invoiceApply, InvoiceMallStateDto mallState) {
        if (InvoiceMallStateDto.STATE_DONE.equals(mallState.getState())) {
            writeBackFeeDetail(invoiceApply.getApplyId(), OPEN_INVOICE_YES);
            if (StringUtil.isEmpty(invoiceApply.getInvoiceCode()) && !StringUtil.isEmpty(mallState.getInvoiceNo())) {
                InvoiceApplyPo updatePo = new InvoiceApplyPo();
                updatePo.setApplyId(invoiceApply.getApplyId());
                updatePo.setCommunityId(invoiceApply.getCommunityId());
                updatePo.setInvoiceCode(mallState.getInvoiceNo());
                invoiceApplyV1InnerServiceSMOImpl.updateInvoiceApply(updatePo);
            }
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
     * 分批调商城批量状态接口(queryInvoiceStatusBatchBmoImpl), 返回 invoiceId -> 商城状态
     */
    private Map<String, InvoiceMallStateDto> queryMallStates(List<InvoiceApplyDto> invoiceApplys) {
        Map<String, InvoiceMallStateDto> mallStates = new HashMap<>();
        List<String> oiIds = new ArrayList<>();
        for (InvoiceApplyDto invoiceApply : invoiceApplys) {
            if (StringUtil.isEmpty(invoiceApply.getOiId()) || oiIds.contains(invoiceApply.getOiId())) {
                continue;
            }
            oiIds.add(invoiceApply.getOiId());
            if (oiIds.size() < BATCH_SIZE) {
                continue;
            }
            queryMallStatesByBatch(oiIds, mallStates);
            oiIds = new ArrayList<>();
        }
        if (!oiIds.isEmpty()) {
            queryMallStatesByBatch(oiIds, mallStates);
        }
        return mallStates;
    }

    private void queryMallStatesByBatch(List<String> oiIds, Map<String, InvoiceMallStateDto> mallStates) {
        JSONObject reqParam = new JSONObject();
        reqParam.put("invoiceIds", oiIds);
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
            if (StringUtil.isEmpty(mallState.getInvoiceId())) {
                continue;
            }
            mallStates.put(mallState.getInvoiceId(), mallState);
        }
    }
}
