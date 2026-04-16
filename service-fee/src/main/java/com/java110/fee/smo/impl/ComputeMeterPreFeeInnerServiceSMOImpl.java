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
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.prePayFee.PrePayFeeDto;
import com.java110.dto.room.RoomDto;
import com.java110.fee.bill.IDeductionPrePayFee;
import com.java110.fee.bmo.fee.IGeneratorPayFeeOrder;
import com.java110.fee.bmo.receipt.IFeeReceiptBmo;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.fee.IComputeMeterPreFeeInnerServiceSMO;
import com.java110.intf.fee.IFeeReceiptInnerServiceSMO;
import com.java110.intf.fee.IPrePayFeeV1InnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelV1InnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.po.prePayFee.PrePayFeePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 水电预存费用计算服务实现类
 * 参考 ComputePreFeeInnerServiceSMOImpl 和 PrePayFeeCmd.doMeterPrePayFee 方法
 * add by 吴学文 at 2025-01-XX mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Service
public class ComputeMeterPreFeeInnerServiceSMOImpl implements IComputeMeterPreFeeInnerServiceSMO {

    @Autowired
    private IFeeReceiptBmo feeReceiptBmoImpl;

    @Autowired
    private IFeeReceiptInnerServiceSMO feeReceiptInnerServiceSMOImpl;

    @Autowired
    private IPrePayFeeV1InnerServiceSMO prePayFeeV1InnerServiceSMOImpl;

    @Autowired
    private IDeductionPrePayFee deductionPrePayFeeImpl;

    @Autowired
    private IGeneratorPayFeeOrder generatorPayFeeOrderImpl;

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelV1InnerServiceSMO ownerRoomRelV1InnerServiceSMOImpl;

    @Override
    public int meterPrePayFee(@RequestBody JSONObject paramIn) {
        String communityId = paramIn.getString("communityId");
        String orderId = paramIn.getString("orderId");
        String userId = paramIn.getString("userId");
        String userName = paramIn.getString("userName");
        String receiptCode = feeReceiptInnerServiceSMOImpl.generatorReceiptCode(communityId);
        int cycles = 1; // 水电预存固定为1个周期
        double amount = paramIn.getDoubleValue("receivedAmount");

        // 计算房屋和业主信息
        computeRoomInfo(paramIn, paramIn.getString("payerObjId"), paramIn.getString("payerObjType"), communityId);
        String ownerId = paramIn.getString("ownerId");
        String ownerName = paramIn.getString("ownerName");
        String payerObjName = paramIn.getString("payerObjName");

        // 创建预存费用记录
        PrePayFeePo prePayFeePo = new PrePayFeePo();
        prePayFeePo.setGiftAmount("0");
        prePayFeePo.setPayOrderId(orderId);
        prePayFeePo.setPrimeRate("5");
        prePayFeePo.setReceivableAmount(amount + "");
        prePayFeePo.setDiscountAmount("0");
        prePayFeePo.setCycles(cycles + "");
        prePayFeePo.setRemark("手机端预存");
        prePayFeePo.setReceivedAmount(amount + "");
        prePayFeePo.setOwnerId(ownerId);
        prePayFeePo.setPayerObjName(payerObjName);
        prePayFeePo.setOwnerName(ownerName);
        prePayFeePo.setRemainAmount(amount + "");
        prePayFeePo.setFeeName(paramIn.getString("feeName"));
        prePayFeePo.setOpenInvoice("N");
        prePayFeePo.setStartTime(DateUtil.getFormatTimeStringB(new Date()));
        prePayFeePo.setCashierId(userId);
        prePayFeePo.setState(FeeDetailDto.STATE_NORMAL);
        prePayFeePo.setEndTime("2050-01-01");
        prePayFeePo.setRuleId("-1");
        prePayFeePo.setCommunityId(communityId);
        prePayFeePo.setCashierName(userName);
        prePayFeePo.setPpfId(GenerateCodeFactory.getGeneratorId("11"));
        prePayFeePo.setPayerObjId(paramIn.getString("payerObjId"));
        prePayFeePo.setFtId(paramIn.getString("ftId"));
        prePayFeePo.setFtName(paramIn.getString("ftName"));
        prePayFeePo.setWay(PrePayFeeDto.WAY_YES);

        int flag = prePayFeeV1InnerServiceSMOImpl.savePrePayFee(prePayFeePo);

        feeReceiptBmoImpl.savePrePayFeeReceipt(prePayFeePo, receiptCode);

        List<PrePayFeePo> prePayFeePos = new ArrayList<>();
        prePayFeePos.add(prePayFeePo);

        generatorPayFeeOrderImpl.generatorPrePayFeeOrder(prePayFeePos, paramIn.getString("payerObjType"));

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        deductionPrePayFeeImpl.deductionPrePayFee(prePayFeePo.getPpfId());
        return 0;
    }

    /**
     * 计算房屋和业主信息
     */
    private void computeRoomInfo(JSONObject paramOut, String payerObjId, String payerObjType, String communityId) {
        if (!FeeDto.PAYER_OBJ_TYPE_ROOM.equals(payerObjType)) {
            return;
        }
        RoomDto roomDto = new RoomDto();
        roomDto.setRoomId(payerObjId);
        roomDto.setCommunityId(communityId);
        List<RoomDto> roomDtos = roomV1InnerServiceSMOImpl.queryRooms(roomDto);
        if (ListUtil.isNull(roomDtos)) {
            throw new CmdException("房屋不存在");
        }
        paramOut.put("payerObjName", roomDtos.get(0).getFloorNum() + "-" + roomDtos.get(0).getUnitNum() + "-" + roomDtos.get(0).getRoomNum());

        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setRoomId(roomDtos.get(0).getRoomId());
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelV1InnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);
        if (ListUtil.isNull(ownerRoomRelDtos)) {
            return;
        }

        com.java110.dto.owner.OwnerDto ownerDto = new com.java110.dto.owner.OwnerDto();
        ownerDto.setMemberId(ownerRoomRelDtos.get(0).getOwnerId());
        ownerDto.setCommunityId(communityId);
        List<com.java110.dto.owner.OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);
        if (ListUtil.isNull(ownerDtos)) {
            return;
        }
        paramOut.put("ownerName", ownerDtos.get(0).getName());
        paramOut.put("ownerTel", ownerDtos.get(0).getLink());
        paramOut.put("ownerId", ownerDtos.get(0).getMemberId());
    }
}


