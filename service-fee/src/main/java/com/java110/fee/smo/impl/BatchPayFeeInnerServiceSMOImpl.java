package com.java110.fee.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.fee.BatchPayFeeDto;
import com.java110.fee.bill.IBatchPayFee;
import com.java110.intf.fee.IBatchPayFeeInnerServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BatchPayFeeInnerServiceSMOImpl implements IBatchPayFeeInnerServiceSMO {

    @Autowired
    private IBatchPayFee batchPayFeeImpl;

    @Override
    public JSONObject batchPayFee(@RequestBody BatchPayFeeDto batchPayFeeDto) {
        return batchPayFeeImpl.batchPayFee(batchPayFeeDto.getCommunityId(), batchPayFeeDto.getPrimeRate(),
                batchPayFeeDto.getAcctId(), batchPayFeeDto.getPayOrderId(), batchPayFeeDto.getRemark(),
                batchPayFeeDto.getUserDto(), batchPayFeeDto.getFeeConfigs());
    }
}
