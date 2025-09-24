/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.fee.smo.impl;


import com.alibaba.fastjson.JSONObject;
import com.java110.dto.fee.PayFeeDetailDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.payFeeOrder.PayFeeOrderDetailDto;
import com.java110.dto.prePayFee.PrePayFeeDto;
import com.java110.dto.room.RoomDto;
import com.java110.fee.dao.IPayFeeOrderV1ServiceDao;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeDetailV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeOrderV1InnerServiceSMO;
import com.java110.dto.payFeeOrder.PayFeeOrderDto;
import com.java110.intf.fee.IPrePayFeeV1InnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.po.payFeeOrder.PayFeeOrderPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.utils.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 类表述： 服务之前调用的接口实现类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2025-07-16 16:05:34 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class PayFeeOrderV1InnerServiceSMOImpl extends BaseServiceSMO implements IPayFeeOrderV1InnerServiceSMO {

    @Autowired
    private IPayFeeOrderV1ServiceDao payFeeOrderV1ServiceDaoImpl;

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeDetailV1InnerServiceSMO payFeeDetailV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IPrePayFeeV1InnerServiceSMO prePayFeeV1InnerServiceSMOImpl;


    @Override
    public int savePayFeeOrder(@RequestBody  PayFeeOrderPo payFeeOrderPo) {
        int saveFlag = payFeeOrderV1ServiceDaoImpl.savePayFeeOrderInfo(BeanConvertUtil.beanCovertMap(payFeeOrderPo));
        return saveFlag;
    }

     @Override
    public int updatePayFeeOrder(@RequestBody  PayFeeOrderPo payFeeOrderPo) {
        int saveFlag = payFeeOrderV1ServiceDaoImpl.updatePayFeeOrderInfo(BeanConvertUtil.beanCovertMap(payFeeOrderPo));
        return saveFlag;
    }

     @Override
    public int deletePayFeeOrder(@RequestBody  PayFeeOrderPo payFeeOrderPo) {
       payFeeOrderPo.setStatusCd("1");
       int saveFlag = payFeeOrderV1ServiceDaoImpl.updatePayFeeOrderInfo(BeanConvertUtil.beanCovertMap(payFeeOrderPo));
       return saveFlag;
    }

    @Override
    public List<PayFeeOrderDto> queryPayFeeOrders(@RequestBody  PayFeeOrderDto payFeeOrderDto) {

        //校验是否传了 分页信息

        int page = payFeeOrderDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            payFeeOrderDto.setPage((page - 1) * payFeeOrderDto.getRow());
        }

        List<PayFeeOrderDto> payFeeOrders = BeanConvertUtil.covertBeanList(payFeeOrderV1ServiceDaoImpl.getPayFeeOrderInfo(BeanConvertUtil.beanCovertMap(payFeeOrderDto)), PayFeeOrderDto.class);

        return payFeeOrders;
    }


    @Override
    public JSONObject queryPayFeeOrdersCount(@RequestBody PayFeeOrderDto payFeeOrderDto) {
        Map paramout =  payFeeOrderV1ServiceDaoImpl.queryPayFeeOrdersCount(BeanConvertUtil.beanCovertMap(payFeeOrderDto));

        return JSONObject.parseObject(JSONObject.toJSONString(paramout));
    }

    @Override
    public List<PayFeeOrderDto> queryReportPayFeeOrders(@RequestBody PayFeeOrderDto payFeeOrderDto) {

       List<PayFeeOrderDto> payFeeOrderDtos = queryPayFeeOrders(payFeeOrderDto);

        // 刷入缴费记录
        queryPayFeeDetails(payFeeOrderDtos);

        // 刷入预存款
        queryPrePayFee(payFeeOrderDtos);

        //todo 计算房屋面积 和车位信息
        computeRoomAndParkingSpace(payFeeOrderDtos);
        return payFeeOrderDtos;
    }

    private void queryPrePayFee(List<PayFeeOrderDto> payFeeOrderDtos) {
        List<String> payOrderIds = new ArrayList<>();
        for (PayFeeOrderDto payFeeOrderDto : payFeeOrderDtos) {
            if (!PayFeeOrderDto.ORDER_TYPE_PRE_PAY_FEE.equals(payFeeOrderDto.getOrderType())) {
                continue;
            }
            payOrderIds.add(payFeeOrderDto.getPayOrderId());
        }
        if (ListUtil.isNull(payOrderIds)) {
            return;
        }

        PrePayFeeDto prePayFeeDto = new PrePayFeeDto();
        prePayFeeDto.setPayOrderIds(payOrderIds.toArray(new String[payOrderIds.size()]));
        prePayFeeDto.setCommunityId(payFeeOrderDtos.get(0).getCommunityId());
        List<PrePayFeeDto> prePayFeeDtos = prePayFeeV1InnerServiceSMOImpl.queryPrePayFees(prePayFeeDto);
        if (ListUtil.isNull(prePayFeeDtos)) {
            return;
        }
        List<PayFeeOrderDetailDto> payFeeOrderDetailDtos = null;
        PayFeeOrderDetailDto payFeeOrderDetailDto = null;
        for (PayFeeOrderDto payFeeOrderDto : payFeeOrderDtos) {
            if (!PayFeeOrderDto.ORDER_TYPE_PRE_PAY_FEE.equals(payFeeOrderDto.getOrderType())) {
                continue;
            }
            payFeeOrderDetailDtos = new ArrayList<>();
            for (PrePayFeeDto tPrePayFeeDto : prePayFeeDtos) {
                if(!payFeeOrderDto.getPayOrderId().equals(tPrePayFeeDto.getPayOrderId())){
                    continue;
                }
                payFeeOrderDetailDto = new PayFeeOrderDetailDto();
                payFeeOrderDetailDto.setDetailId(tPrePayFeeDto.getPpfId());
                payFeeOrderDetailDto.setFeeName(tPrePayFeeDto.getFeeName());
                payFeeOrderDetailDto.setStartTime(tPrePayFeeDto.getStartTime());
                payFeeOrderDetailDto.setEndTime(tPrePayFeeDto.getEndTime());
                payFeeOrderDetailDto.setReceivableAmount(tPrePayFeeDto.getReceivableAmount());
                payFeeOrderDetailDto.setReceivedAmount(tPrePayFeeDto.getReceivedAmount());
                payFeeOrderDetailDtos.add(payFeeOrderDetailDto);
            }
            payFeeOrderDto.setPayFeeOrderDetailDtos(payFeeOrderDetailDtos);
        }
    }

    private void queryPayFeeDetails(List<PayFeeOrderDto> payFeeOrderDtos) {
        List<String> payOrderIds = new ArrayList<>();
        for (PayFeeOrderDto payFeeOrderDto : payFeeOrderDtos) {
            if (!PayFeeOrderDto.ORDER_TYPE_PAY_FEE.equals(payFeeOrderDto.getOrderType())) {
                continue;
            }
            payOrderIds.add(payFeeOrderDto.getPayOrderId());
        }
        if (ListUtil.isNull(payOrderIds)) {
            return;
        }

        PayFeeDetailDto payFeeDetailDto = new PayFeeDetailDto();
        payFeeDetailDto.setPayOrderIds(payOrderIds.toArray(new String[payOrderIds.size()]));
        payFeeDetailDto.setCommunityId(payFeeOrderDtos.get(0).getCommunityId());
        List<PayFeeDetailDto> payFeeDetailDtos = payFeeDetailV1InnerServiceSMOImpl.queryPayFeeDetailSimple(payFeeDetailDto);
        if (ListUtil.isNull(payFeeDetailDtos)) {
            return;
        }
        List<PayFeeOrderDetailDto> payFeeOrderDetailDtos = null;
        PayFeeOrderDetailDto payFeeOrderDetailDto = null;
        for (PayFeeOrderDto payFeeOrderDto : payFeeOrderDtos) {
            if (!PayFeeOrderDto.ORDER_TYPE_PAY_FEE.equals(payFeeOrderDto.getOrderType())) {
                continue;
            }
            payFeeOrderDetailDtos = new ArrayList<>();
            for (PayFeeDetailDto tPayFeeDetailDto : payFeeDetailDtos) {
                if(!payFeeOrderDto.getPayOrderId().equals(tPayFeeDetailDto.getPayOrderId())){
                    continue;
                }
                payFeeOrderDetailDto = new PayFeeOrderDetailDto();
                payFeeOrderDetailDto.setDetailId(tPayFeeDetailDto.getDetailId());
                payFeeOrderDetailDto.setFeeName(tPayFeeDetailDto.getFeeName());
                payFeeOrderDetailDto.setStartTime(tPayFeeDetailDto.getStartTime());
                payFeeOrderDetailDto.setEndTime(tPayFeeDetailDto.getEndTime());
                payFeeOrderDetailDto.setReceivableAmount(tPayFeeDetailDto.getReceivableAmount());
                payFeeOrderDetailDto.setReceivedAmount(tPayFeeDetailDto.getReceivedAmount());
                payFeeOrderDetailDtos.add(payFeeOrderDetailDto);
            }
            payFeeOrderDto.setPayFeeOrderDetailDtos(payFeeOrderDetailDtos);
        }
    }


    private void computeRoomAndParkingSpace(List<PayFeeOrderDto> payFeeOrderDtos) {

        List<String> payerObjIds = new ArrayList<>();

        for (PayFeeOrderDto reportFeeMonthStatisticsDto : payFeeOrderDtos) {
            payerObjIds.add(reportFeeMonthStatisticsDto.getPayerObjId());
        }

        if (ListUtil.isNull(payerObjIds)) {
            return;
        }
        RoomDto roomDto = new RoomDto();
        roomDto.setRoomIds(payerObjIds.toArray(new String[payerObjIds.size()]));
        roomDto.setCommunityId(payFeeOrderDtos.get(0).getCommunityId());
        List<RoomDto> roomDtos = roomV1InnerServiceSMOImpl.queryRooms(roomDto);

        if (!ListUtil.isNull(roomDtos)) {
            for (PayFeeOrderDto reportFeeMonthStatisticsDto : payFeeOrderDtos) {
                for (RoomDto tmpRoomDto : roomDtos) {
                    if (reportFeeMonthStatisticsDto.getPayerObjId().equals(tmpRoomDto.getRoomId())) {
                        reportFeeMonthStatisticsDto.setBuiltUpArea(tmpRoomDto.getBuiltUpArea());
                    }
                }
            }
        }

        computeParkingSpace(payFeeOrderDtos, payerObjIds);

    }

    private void computeParkingSpace(List<PayFeeOrderDto> payFeeOrderDtos, List<String> payerObjIds) {
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setMemberIds(payerObjIds.toArray(new String[payerObjIds.size()]));
        ownerCarDto.setCommunityId(payFeeOrderDtos.get(0).getCommunityId());
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

        if (ListUtil.isNull(ownerCarDtos)) {
            return;
        }
        for (PayFeeOrderDto reportFeeMonthStatisticsDto : payFeeOrderDtos) {
            for (OwnerCarDto tmpOwnerCarDto : ownerCarDtos) {
                if (!reportFeeMonthStatisticsDto.getPayerObjId().equals(tmpOwnerCarDto.getMemberId())) {
                    continue;
                }
                reportFeeMonthStatisticsDto.setPsName(tmpOwnerCarDto.getAreaNum() + "-" + tmpOwnerCarDto.getNum());
            }
        }

    }

}
