package com.java110.fee.bmo.rentingFee;

import com.java110.dto.fee.FeeDto;
import org.springframework.http.ResponseEntity;

public interface IQueryRentingFee {

    /**
     * 查询租赁费用
     * @param feeDto
     * @return
     */
    ResponseEntity<String> queryFees(FeeDto feeDto);
}
