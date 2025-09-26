package com.java110.fee.bmo.fee;

import com.alibaba.fastjson.JSONArray;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.fee.PayFeeDto;
import com.java110.dto.user.UserDto;

public interface IPayPrePayFeeBmo {
    int payPreFee(JSONArray rules, String receiptCode, FeeDto feeDto, String primeRate, UserDto userDto, String remark);
    int payPreFee(JSONArray rules, String receiptCode, PayFeeDto feeDto, String primeRate, UserDto userDto, String remark);
}
