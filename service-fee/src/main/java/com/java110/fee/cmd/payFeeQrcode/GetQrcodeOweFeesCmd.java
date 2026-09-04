package com.java110.fee.cmd.payFeeQrcode;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeDto;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.MoneyUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询 二维码 欠费费用
 */
@Java110Cmd(serviceCode = "payFeeQrcode.getQrcodeOweFees")
public class GetQrcodeOweFeesCmd extends Cmd {

    private final static Logger logger = LoggerFactory.getLogger(GetQrcodeOweFeesCmd.class);
    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;


    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    //域
    public static final String DOMAIN_COMMON = "DOMAIN.COMMON";

    //键
    public static final String TOTAL_FEE_PRICE = "TOTAL_FEE_PRICE";

    //键
    public static final String RECEIVED_AMOUNT_SWITCH = "RECEIVED_AMOUNT_SWITCH";

    //禁用电脑端提交收费按钮
    public static final String OFFLINE_PAY_FEE_SWITCH = "OFFLINE_PAY_FEE_SWITCH";

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
        Assert.hasKeyAndValue(reqJson, "ownerId", "未包含业主");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        FeeDto feeDto = new FeeDto();
        feeDto.setOwnerId(reqJson.getString("ownerId"));
        feeDto.setCommunityId(reqJson.getString("communityId"));
        feeDto.setState(FeeDto.STATE_DOING);
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        if (feeDtos == null || feeDtos.size() < 1) {
            feeDtos = new ArrayList<>();
            context.setResponseEntity(ResultVo.createResponseEntity(feeDtos));
            return;
        }

        context.setResponseEntity(ResultVo.createResponseEntity(feeDtos));
    }
}
