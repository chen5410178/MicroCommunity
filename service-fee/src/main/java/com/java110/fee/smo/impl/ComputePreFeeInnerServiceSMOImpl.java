package com.java110.fee.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeAmountDto;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.payFee.PayFeeRuleDto;
import com.java110.dto.prePayFee.PrePayFeeDto;
import com.java110.dto.room.RoomDto;
import com.java110.fee.bill.IComputeBillPrice;
import com.java110.fee.bill.IDeductionPrePayFee;
import com.java110.fee.bmo.fee.IGeneratorPayFeeOrder;
import com.java110.fee.bmo.receipt.IFeeReceiptBmo;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.fee.IComputePreFeeInnerServiceSMO;
import com.java110.intf.fee.IFeeReceiptInnerServiceSMO;
import com.java110.intf.fee.IPayFeeRuleV1InnerServiceSMO;
import com.java110.intf.fee.IPrePayFeeV1InnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelV1InnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.po.prePayFee.PrePayFeePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ComputePreFeeInnerServiceSMOImpl implements IComputePreFeeInnerServiceSMO {

    @Autowired
    private IPayFeeRuleV1InnerServiceSMO payFeeRuleV1InnerServiceSMOImpl;

    @Autowired
    private IComputeBillPrice computeBillPriceImpl;

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
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;
    @Autowired
    private IOwnerRoomRelV1InnerServiceSMO ownerRoomRelV1InnerServiceSMOImpl;


    @Override
    public FeeAmountDto computeRulePayFeeAmount(@RequestBody JSONObject paramIn) {
        PayFeeRuleDto payFeeRuleDto = new PayFeeRuleDto();
        payFeeRuleDto.setRuleId(paramIn.getString("ruleId"));
        payFeeRuleDto.setCommunityId(paramIn.getString("communityId"));
        List<PayFeeRuleDto> payFeeRuleDtos = payFeeRuleV1InnerServiceSMOImpl.queryPayFeeRules(payFeeRuleDto);
        if (ListUtil.isNull(payFeeRuleDtos)) {
            throw new CmdException("缴费账单不存在");
        }
        FeeAmountDto  feeAmountDto = computeBillPriceImpl.computeBillReceiveAmount(payFeeRuleDtos.get(0),paramIn.getIntValue("cycles"));

        return feeAmountDto;
    }

    @Override
    public int prePayFee(@RequestBody JSONObject paramIn) {
        String communityId = paramIn.getString("communityId");
        String orderId = paramIn.getString("orderId");
        String userId = paramIn.getString("userId");
        String userName = paramIn.getString("userName");
        String ruleId = paramIn.getString("ruleId");
        String receiptCode = feeReceiptInnerServiceSMOImpl.generatorReceiptCode(communityId);
        int cycles = paramIn.getIntValue("cycles");
        double ruleReceivedAmount = paramIn.getDoubleValue("receivedAmount");


        PayFeeRuleDto payFeeRuleDto = new PayFeeRuleDto();
        payFeeRuleDto.setRuleId(ruleId);
        payFeeRuleDto.setCommunityId(communityId);
        List<PayFeeRuleDto> payFeeRuleDtos = payFeeRuleV1InnerServiceSMOImpl.queryPayFeeRules(payFeeRuleDto);
        if (ListUtil.isNull(payFeeRuleDtos)) {
            throw new CmdException("账单规则不存在");
        }
        computeStartMonth(payFeeRuleDtos.get(0));
        String startMonth = payFeeRuleDtos.get(0).getCurYearMonth();

        computeRoomInfo(paramIn, payFeeRuleDtos.get(0).getPayerObjId(), payFeeRuleDtos.get(0).getPayerObjType(), communityId);
        computeParkingSpaceInfo(paramIn, payFeeRuleDtos.get(0).getPayerObjId(), payFeeRuleDtos.get(0).getPayerObjType(), communityId);
        String ownerId = paramIn.getString("ownerId");
        String ownerName = paramIn.getString("ownerName");
        String payerObjName = paramIn.getString("payerObjName");

        String year = startMonth.split("-")[0];
        String month = startMonth.split("-")[1];

        //计算应收
        // FeeAmountDto feeAmountDto = computeBillPriceImpl.computeBillReceiveAmount(payFeeRuleDtos.get(0), cycles);

        Date startTime = DateUtil.getDateFromStringA(payFeeRuleDtos.get(0).getEndTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.set(Calendar.YEAR, Integer.parseInt(year));
        calendar.set(Calendar.MONTH, Integer.parseInt(month) - 1);
        startTime = calendar.getTime();
        calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.add(Calendar.MONTH, cycles);
        Date endTime = calendar.getTime();
        List<PrePayFeePo> prePayFeePos = new ArrayList<>();

        double discountAmount = 0.0;//computeFeeDiscountAmount(rule, communityId, cycles, payFeeRuleDtos.get(0));
        PrePayFeePo prePayFeePo = new PrePayFeePo();
        prePayFeePo.setReceivableAmount(ruleReceivedAmount + "");
        BigDecimal receivedAmountDec = new BigDecimal(ruleReceivedAmount + "");
        receivedAmountDec = receivedAmountDec.subtract(new BigDecimal(discountAmount));
        prePayFeePo.setGiftAmount("0");
        prePayFeePo.setPayOrderId(orderId);
        prePayFeePo.setPrimeRate("5");
        prePayFeePo.setDiscountAmount(discountAmount + "");
        prePayFeePo.setCycles(cycles + "");
        prePayFeePo.setRemark("手机端预存");
        prePayFeePo.setReceivedAmount(receivedAmountDec.doubleValue() + "");
        prePayFeePo.setOwnerId(ownerId);
        prePayFeePo.setPayerObjName(payerObjName);
        prePayFeePo.setOwnerName(ownerName);
        prePayFeePo.setRemainAmount(ruleReceivedAmount + "");
        prePayFeePo.setFeeName(payFeeRuleDtos.get(0).getFeeName());
        prePayFeePo.setOpenInvoice("N");
        prePayFeePo.setStartTime(DateUtil.getFormatTimeStringB(startTime));
        prePayFeePo.setCashierId(userId);
        prePayFeePo.setState(FeeDetailDto.STATE_NORMAL);
        prePayFeePo.setEndTime(DateUtil.getPreSecTime(endTime));
        prePayFeePo.setRuleId(ruleId);
        prePayFeePo.setCommunityId(communityId);
        prePayFeePo.setCashierName(userName);
        prePayFeePo.setPpfId(GenerateCodeFactory.getGeneratorId("11"));
        prePayFeePo.setPayerObjId(payFeeRuleDtos.get(0).getPayerObjId());
        prePayFeePo.setFtId(payFeeRuleDtos.get(0).getFtId());
        prePayFeePo.setFtName(payFeeRuleDtos.get(0).getFtName());
        prePayFeePo.setWay(PrePayFeeDto.WAY_YES);

        int flag = prePayFeeV1InnerServiceSMOImpl.savePrePayFee(prePayFeePo);

        feeReceiptBmoImpl.savePrePayFeeReceipt(prePayFeePo,receiptCode);

        prePayFeePos.add(prePayFeePo);

        generatorPayFeeOrderImpl.generatorPrePayFeeOrder(prePayFeePos,payFeeRuleDtos.get(0).getPayerObjType());


        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        deductionPrePayFeeImpl.deductionPrePayFee(prePayFeePo.getPpfId());
        return 0;
    }

    private void computeStartMonth(PayFeeRuleDto tPayFeeRuleDto) {

        PrePayFeeDto prePayFeeDto = new PrePayFeeDto();
        prePayFeeDto.setRuleId(tPayFeeRuleDto.getRuleId());
        prePayFeeDto.setCommunityId(tPayFeeRuleDto.getCommunityId());
        List<PrePayFeeDto> prePayFeeDtos = prePayFeeV1InnerServiceSMOImpl.queryPrePayFees(prePayFeeDto);
        if (ListUtil.isNull(prePayFeeDtos)) {
            return;
        }
        String endTime = prePayFeeDtos.get(0).getEndTime();
        endTime = DateUtil.getNextSecTime(endTime);
        endTime = DateUtil.getFormatTimeStringB(DateUtil.getDateFromStringA(endTime));
        tPayFeeRuleDto.setCurYearMonth(endTime);
    }

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
        paramOut.put("buildUpArea", roomDtos.get(0).getBuiltUpArea());
        paramOut.put("roomArea", roomDtos.get(0).getRoomArea());

        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setRoomId(roomDtos.get(0).getRoomId());
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelV1InnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);
        if (ListUtil.isNull(ownerRoomRelDtos)) {
            return;
        }

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(ownerRoomRelDtos.get(0).getOwnerId());
        ownerDto.setCommunityId(communityId);
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);
        if (ListUtil.isNull(ownerDtos)) {
            return;
        }
        paramOut.put("ownerName", ownerDtos.get(0).getName());
        paramOut.put("ownerTel", ownerDtos.get(0).getLink());
        paramOut.put("ownerId", ownerDtos.get(0).getMemberId());

    }

    /**
     * 6666 即车位：直接按 psId 查车位
     *
     * @param paramOut
     * @param payerObjId
     * @param payerObjType
     * @param communityId
     */
    private void computeParkingSpaceInfo(JSONObject paramOut, String payerObjId, String payerObjType, String communityId) {
        if (!FeeDto.PAYER_OBJ_TYPE_PARKING_SPACE.equals(payerObjType)) {
            return;
        }

        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setPsId(payerObjId);
        parkingSpaceDto.setCommunityId(communityId);
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
        if (ListUtil.isNull(parkingSpaceDtos)) {
            throw new CmdException("车位不存在");
        }
        String payerObjName = parkingSpaceDtos.get(0).getAreaNum() + "-" + parkingSpaceDtos.get(0).getSectionName() + "-" + parkingSpaceDtos.get(0).getNum();
        if (StringUtil.isEmpty(parkingSpaceDtos.get(0).getSectionName())) {
            payerObjName = parkingSpaceDtos.get(0).getAreaNum() + "-" + parkingSpaceDtos.get(0).getNum();
        }
        paramOut.put("payerObjName", payerObjName);
        paramOut.put("areaNum", parkingSpaceDtos.get(0).getAreaNum());
        paramOut.put("num", parkingSpaceDtos.get(0).getNum());

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(parkingSpaceDtos.get(0).getOwnerId());
        ownerDto.setCommunityId(communityId);
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);
        if (ListUtil.isNull(ownerDtos)) {
            return;
        }
        paramOut.put("ownerName", ownerDtos.get(0).getName());
        paramOut.put("ownerTel", ownerDtos.get(0).getLink());
        paramOut.put("ownerId", ownerDtos.get(0).getMemberId());

    }

}
