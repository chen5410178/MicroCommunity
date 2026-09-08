package com.java110.acct.cmd.invoice;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.invoice.InvoiceApplyDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.acct.IInvoiceApplyV1InnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserV1InnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 手机端查询已开票记录（invoice.queryAppInvoiceApply）
 * 从请求头 user-id 取登录用户校验业主绑定，按 communityId + 业主手机号(applyTel) 分页查询开票记录
 */
@Java110Cmd(serviceCode = "invoice.queryAppInvoiceApply")
public class QueryAppInvoiceApplyCmd extends Cmd {

    @Autowired
    private IOwnerAppUserV1InnerServiceSMO ownerAppUserV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IInvoiceApplyV1InnerServiceSMO invoiceApplyV1InnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");

        String userId = CmdContextUtils.getUserId(context);
        if (StringUtil.isEmpty(userId)) {
            throw new CmdException("用户未登录");
        }

        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setUserId(userId);
        ownerAppUserDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserV1InnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);
        if (ListUtil.isNull(ownerAppUserDtos)) {
            throw new CmdException("未绑定业主");
        }

        String memberId = "";
        for (OwnerAppUserDto tmpOwnerAppUserDto : ownerAppUserDtos) {
            if ("-1".equals(tmpOwnerAppUserDto.getMemberId())) {
                continue;
            }
            memberId = tmpOwnerAppUserDto.getMemberId();
        }

        if (StringUtil.isEmpty(memberId)) {
            throw new CmdException("未绑定业主");
        }

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        ownerDto.setMemberId(memberId);
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);

        Assert.listOnlyOne(ownerDtos, "业主不存在");

        reqJson.put("applyTel", ownerDtos.get(0).getLink());
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        InvoiceApplyDto invoiceApplyDto = BeanConvertUtil.covertBean(reqJson, InvoiceApplyDto.class);

        int count = invoiceApplyV1InnerServiceSMOImpl.queryInvoiceApplysCount(invoiceApplyDto);

        List<InvoiceApplyDto> invoiceApplyDtos = null;

        if (count > 0) {
            invoiceApplyDtos = invoiceApplyV1InnerServiceSMOImpl.queryInvoiceApplys(invoiceApplyDto);
        } else {
            invoiceApplyDtos = new ArrayList<>();
        }

        //todo 查询电子发票
        queryInvoicePhoto(invoiceApplyDtos);

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, invoiceApplyDtos);

        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(resultVo);

        context.setResponseEntity(responseEntity);
    }

    private void queryInvoicePhoto(List<InvoiceApplyDto> invoiceApplyDtos) {

        if (invoiceApplyDtos == null || invoiceApplyDtos.size() != 1) {
            return;
        }

        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setObjId(invoiceApplyDtos.get(0).getApplyId());
        fileRelDto.setRelTypeCd(FileRelDto.REL_TYPE_CE_INVOICE); //业主照片
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);

        if (fileRelDtos != null && !fileRelDtos.isEmpty()) {
            List<String> urls = new ArrayList<>();
            for (FileRelDto fileRel : fileRelDtos) {
                urls.add(fileRel.getFileRealName());
            }
            invoiceApplyDtos.get(0).setUrls(urls);
        }
    }
}
