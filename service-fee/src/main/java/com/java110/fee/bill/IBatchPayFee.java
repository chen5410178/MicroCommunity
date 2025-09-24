package com.java110.fee.bill;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.user.UserDto;

public interface IBatchPayFee {

    JSONObject batchPayFee(String communityId, String primeRate, String acctId, String payOrderId,
                           String remark, UserDto userDto,
                           JSONArray feeConfigs);
}
