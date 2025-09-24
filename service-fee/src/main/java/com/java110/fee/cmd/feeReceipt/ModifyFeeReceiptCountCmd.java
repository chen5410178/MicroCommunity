package com.java110.fee.cmd.feeReceipt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.fee.FeeReceiptDetailDto;
import com.java110.intf.fee.IFeeReceiptDetailInnerServiceSMO;
import com.java110.po.fee.FeeReceiptDetailPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "feeReceipt.modifyFeeReceiptCount")
public class ModifyFeeReceiptCountCmd extends Cmd {

    @Autowired
    private IFeeReceiptDetailInnerServiceSMO feeReceiptDetailInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
        if (!reqJson.containsKey("detailIds")) {
            throw new CmdException("未包含收据");
        }
        JSONArray detailIds = reqJson.getJSONArray("detailIds");
        if (ListUtil.isNull(detailIds)) {
            throw new CmdException("未包含收据");
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        JSONArray detailIds = reqJson.getJSONArray("detailIds");
        for (int detailIndex = 0; detailIndex < detailIds.size(); detailIndex++) {
            FeeReceiptDetailDto feeReceiptDetailDto = new FeeReceiptDetailDto();
            feeReceiptDetailDto.setDetailId(detailIds.getString(detailIndex));
            feeReceiptDetailDto.setCommunityId(reqJson.getString("communityId"));
            List<FeeReceiptDetailDto> feeReceiptDetailDtos = feeReceiptDetailInnerServiceSMOImpl.queryFeeReceiptDetails(feeReceiptDetailDto);
            if (ListUtil.isNull(feeReceiptDetailDtos)) {
                continue;
            }
            FeeReceiptDetailPo feeReceiptDetailPo = new FeeReceiptDetailPo();
            feeReceiptDetailPo.setDetailId(feeReceiptDetailDtos.get(0).getDetailId());
            feeReceiptDetailPo.setPrintCount((feeReceiptDetailDtos.get(0).getPrintCount()+1)+"");
            feeReceiptDetailInnerServiceSMOImpl.updateFeeReceiptDetail(feeReceiptDetailPo);
        }
    }
}
