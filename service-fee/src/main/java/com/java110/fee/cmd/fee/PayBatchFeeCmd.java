package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.user.UserDto;
import com.java110.fee.bill.IBatchPayFee;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 请求报文
 * {
 * communityId: '',
 * primeRate: '',
 * acctId: '',
 * remark: '',
 * amount:100.00,
 * discountAmount:10.0,
 * feeConfigs: [
 * {
 * configId：'',
 * selectedDiscountIds:['1111','22222'],
 * payFees:[{
 * feeId:'',
 * receivableAmount:'',
 * receivedAmount:''
 * }]
 * }
 * ],
 * }
 */
@Java110Cmd(serviceCode = "fee.payBatchFee")
public class PayBatchFeeCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(PayBatchFeeCmd.class);


    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;
    @Autowired
    private IBatchPayFee batchPayFeeImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId节点");
        //Assert.jsonObjectHaveKey(reqJson, "acctId", "请求报文中未包含acctId节点");
        Assert.jsonObjectHaveKey(reqJson, "primeRate", "请求报文中未包含primeRate节点");
        Assert.jsonObjectHaveKey(reqJson, "amount", "请求报文中未包含primeRate节点");
        Assert.jsonObjectHaveKey(reqJson, "discountAmount", "请求报文中未包含primeRate节点");

        if (!reqJson.containsKey("feeConfigs")) {
            throw new CmdException("未包含费用");
        }
        JSONArray feeConfigs = reqJson.getJSONArray("feeConfigs");
        if (ListUtil.isNull(feeConfigs)) {
            throw new CmdException("未包含费用项");
        }


        JSONObject paramInObj = null;
        for (int feeIndex = 0; feeIndex < feeConfigs.size(); feeIndex++) {
            paramInObj = feeConfigs.getJSONObject(feeIndex);
            Assert.hasKeyAndValue(paramInObj, "payFees", "未包含缴费费用");
        }

    }


    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        //todo 封装好的参数
        String userId = CmdContextUtils.getUserId(cmdDataFlowContext);
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        if (ListUtil.isNull(userDtos)) {
            throw new CmdException("员工不存在");
        }


        String communityId = reqJson.getString("communityId");
        String primeRate = reqJson.getString("primeRate");
        String acctId = reqJson.getString("acctId");
        String payOrderId = reqJson.getString("payOrderId");
        String amount = reqJson.getString("amount");
        String discountAmount = reqJson.getString("discountAmount");
        String remark = reqJson.getString("remark");
        JSONArray feeConfigs = reqJson.getJSONArray("feeConfigs");


        //todo 生成收据编号
        JSONObject data = batchPayFeeImpl.batchPayFee(communityId, primeRate, acctId, payOrderId, remark, userDtos.get(0), feeConfigs);

        cmdDataFlowContext.setResponseEntity(ResultVo.createResponseEntity(data));
    }


}

