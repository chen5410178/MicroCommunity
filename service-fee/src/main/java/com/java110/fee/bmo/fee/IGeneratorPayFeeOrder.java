package com.java110.fee.bmo.fee;

import com.java110.dto.fee.FeeDto;
import com.java110.dto.fee.PayFeeDto;
import com.java110.dto.payFee.BatchPayFeeOrder;
import com.java110.dto.payFeeOrder.PayFeeOrderDto;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.prePayFee.PrePayFeePo;

import java.util.List;

public interface IGeneratorPayFeeOrder {
    /**
     * 按单个缴费明细生成订单
     * @param payFeeDetailPo
     * @return
     */
    int generatorPayFeeOrder(PayFeeDetailPo payFeeDetailPo, FeeDto feeDto,String extPayOrderId,String primeFile);

    /**
     * 按单个缴费明细生成订单
     * @param payFeeDetailPo
     * @return
     */
    int generatorPayFeeOrder(PayFeeDetailPo payFeeDetailPo, PayFeeDto feeDto, String extPayOrderId,String primeFile);

    /**
     * 按批量缴费明细生成订单
     * @param batchPayFeeOrders
     * @return
     */
    int generatorPayFeeOrder(List<BatchPayFeeOrder> batchPayFeeOrders,String extPayOrderId);

    int generatorPrePayFeeOrder(List<PrePayFeePo> prePayFeePos,String payerObjType);

    int computePayFeeOrder(PayFeeOrderDto payFeeOrderDto);
}
