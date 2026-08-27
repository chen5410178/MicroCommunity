package com.java110.fee.bmo.fee.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.fee.PayFeeDataDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.fee.bmo.fee.IFinishFeeNotify;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.intf.community.*;
import com.java110.intf.fee.IFeeAttrInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IOwnerCarNewV1InnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.po.account.AccountDetailPo;
import com.java110.po.car.OwnerCarPo;
import com.java110.po.owner.OwnerRoomRelPo;
import com.java110.po.owner.RepairPoolPo;
import com.java110.po.owner.RepairUserPo;
import com.java110.po.parking.ParkingSpacePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class FinishFeeNotifyImpl implements IFinishFeeNotify {

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarNewV1InnerServiceSMO ownerCarNewV1InnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMO;

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMO;


    @Autowired
    private IRepairPoolV1InnerServiceSMO repairPoolV1InnerServiceSMOImpl;

    @Autowired
    private IRepairUserV1InnerServiceSMO repairUserV1InnerServiceSMOImpl;

    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Override
    public void updateCarEndTime(String feeId, String communityId) {
        FeeDto feeDto = new FeeDto();
        feeDto.setFeeId(feeId);
        feeDto.setCommunityId(communityId);
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        if (ListUtil.isNull(feeDtos)) {
            return;
        }
        if (!FeeDto.PAYER_OBJ_TYPE_PARKING_SPACE.equals(feeDtos.get(0).getPayerObjType())) {
            return;
        }
        Date feeEndTime = feeDtos.get(0).getEndTime();
        //6666 即车位：直接按 psId 查车位
        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setPsId(feeDtos.get(0).getPayerObjId());
        parkingSpaceDto.setCommunityId(communityId);
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);

        if (ListUtil.isNull(parkingSpaceDtos)) {
            return;
        }
        //后付费 加一个月
        if (FeeConfigDto.PAYMENT_CD_AFTER.equals(feeDtos.get(0).getPaymentCd())) {
            Calendar endTimeCalendar = Calendar.getInstance();
            endTimeCalendar.setTime(feeEndTime);
            endTimeCalendar.add(Calendar.MONTH, 1);
            feeEndTime = endTimeCalendar.getTime();
        }
        //车位已有到期时间大于等于缴费结束时间，无需续期
        boolean needUpdateParkingSpace = true;
        if (!StringUtil.isEmpty(parkingSpaceDtos.get(0).getEndTime())) {
            Date psEndTime = DateUtil.getDateFromStringA(parkingSpaceDtos.get(0).getEndTime());
            if (psEndTime.getTime() >= feeEndTime.getTime()) {
                needUpdateParkingSpace = false;
            }
        }
        if (needUpdateParkingSpace) {
            ParkingSpacePo parkingSpacePo = new ParkingSpacePo();
            parkingSpacePo.setPsId(parkingSpaceDtos.get(0).getPsId());
            parkingSpacePo.setEndTime(DateUtil.getFormatTimeStringA(feeEndTime));
            parkingSpaceInnerServiceSMOImpl.updateParkingSpace(parkingSpacePo);
            //车位延期后，车位绑定的车辆同步延期
            updateCarEndTimeByParkingSpace(parkingSpaceDtos.get(0).getCarId(), communityId, feeEndTime);
        }
    }

    /**
     * 车位延期后，同步延期车位绑定的车辆
     *
     * @param carId      车位绑定车辆ID
     * @param communityId
     * @param feeEndTime 车位续期后的到期时间
     */
    private void updateCarEndTimeByParkingSpace(String carId, String communityId, Date feeEndTime) {
        if (StringUtil.isEmpty(carId)) {
            return;
        }
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCarId(carId);
        ownerCarDto.setCommunityId(communityId);
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

        if (ListUtil.isNull(ownerCarDtos)) {
            return;
        }
        for (OwnerCarDto tmpOwnerCarDto : ownerCarDtos) {
            if (tmpOwnerCarDto.getEndTime() != null && tmpOwnerCarDto.getEndTime().getTime() >= feeEndTime.getTime()) {
                continue;
            }
            OwnerCarPo ownerCarPo = new OwnerCarPo();
            ownerCarPo.setMemberId(tmpOwnerCarDto.getMemberId());
            ownerCarPo.setEndTime(DateUtil.getFormatTimeStringA(feeEndTime));
            int flag = ownerCarNewV1InnerServiceSMOImpl.updateOwnerCarNew(ownerCarPo);
            if (flag < 1) {
                throw new CmdException("缴费失败");
            }
        }
    }

    @Override
    public void updateRoomEndTime(String feeId, String communityId) {
        FeeDto feeDto = new FeeDto();
        feeDto.setFeeId(feeId);
        feeDto.setCommunityId(communityId);
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        if (ListUtil.isNull(feeDtos)) {
            return;
        }
        if (!FeeDto.PAYER_OBJ_TYPE_ROOM.equals(feeDtos.get(0).getPayerObjType())) {
            return;
        }
        //todo 不是租金跳过
        if (!FeeConfigDto.FEE_TYPE_CD_RENT.equals(feeDtos.get(0).getFeeTypeCd())) {
            return;
        }

        Date feeEndTime = feeDtos.get(0).getEndTime();
        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setCommunityId(communityId);
        ownerRoomRelDto.setRoomId(feeDtos.get(0).getPayerObjId());
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);

        if (ListUtil.isNull(ownerRoomRelDtos)) {
            return;
        }
        for (OwnerRoomRelDto tOwnerRoomRelDto : ownerRoomRelDtos) {
            if (tOwnerRoomRelDto.getEndTime().getTime() >= feeEndTime.getTime()) {
                continue;
            }
            OwnerRoomRelPo ownerRoomRelPo = new OwnerRoomRelPo();
            ownerRoomRelPo.setRelId(tOwnerRoomRelDto.getRelId());
            ownerRoomRelPo.setEndTime(DateUtil.getFormatTimeStringA(feeEndTime));
            ownerRoomRelInnerServiceSMOImpl.updateOwnerRoomRels(ownerRoomRelPo);
        }


    }

    @Override
    public void updateRepair(String feeId, String communityId, String receivedAmount) {
        //判断是否有派单属性ID
        FeeAttrDto feeAttrDto = new FeeAttrDto();
        feeAttrDto.setCommunityId(communityId);
        feeAttrDto.setFeeId(feeId);
        feeAttrDto.setSpecCd(FeeAttrDto.SPEC_CD_REPAIR);
        List<FeeAttrDto> feeAttrDtos = feeAttrInnerServiceSMOImpl.queryFeeAttrs(feeAttrDto);

        int flag = 0;
        //修改 派单状态
        if (ListUtil.isNull(feeAttrDtos)) {
            return;
        }

        RepairPoolPo repairPoolPo = new RepairPoolPo();
        repairPoolPo.setRepairId(feeAttrDtos.get(0).getValue());
        repairPoolPo.setCommunityId(communityId);
        repairPoolPo.setState(RepairDto.STATE_APPRAISE);
        flag = repairPoolV1InnerServiceSMOImpl.updateRepairPoolNew(repairPoolPo);
        if (flag < 1) {
            throw new CmdException("修改失败");
        }

        RepairDto repairDto = new RepairDto();
        repairDto.setRepairId(feeAttrDtos.get(0).getValue());
        //查询报修记录
        List<RepairDto> repairDtos = repairInnerServiceSMO.queryRepairs(repairDto);
        Assert.listOnlyOne(repairDtos, "报修信息错误！");
        //获取报修渠道
        String repairChannel = repairDtos.get(0).getRepairChannel();
        RepairUserDto repairUserDto = new RepairUserDto();
        repairUserDto.setRepairId(feeAttrDtos.get(0).getValue());
        repairUserDto.setState(RepairUserDto.STATE_PAY_FEE);
        //查询待支付状态的记录
        List<RepairUserDto> repairUserDtoList = repairUserInnerServiceSMO.queryRepairUsers(repairUserDto);
        Assert.listOnlyOne(repairUserDtoList, "信息错误！");
        RepairUserPo repairUserPo = new RepairUserPo();
        repairUserPo.setRuId(repairUserDtoList.get(0).getRuId());
        //如果是已支付状态，就更新结束时间
        repairUserPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        repairUserPo.setState(RepairUserDto.STATE_FINISH_PAY_FEE);
        repairUserPo.setContext("已支付" + receivedAmount + "元");
        flag = repairUserV1InnerServiceSMOImpl.updateRepairUserNew(repairUserPo);
        if (flag < 1) {
            throw new CmdException("修改失败");
        }
        if ("Z".equals(repairChannel)) {  //如果业主是自主报修，状态就变成已支付，并新增一条待评价状态
            //新增待评价状态
            RepairUserPo repairUser = new RepairUserPo();
            repairUser.setRuId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ruId));
            repairUser.setStartTime(repairUserPo.getEndTime());
            repairUser.setState(RepairUserDto.STATE_EVALUATE);
            repairUser.setContext("待评价");
            repairUser.setCommunityId(communityId);
            repairUser.setCreateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            repairUser.setRepairId(repairUserDtoList.get(0).getRepairId());
            repairUser.setStaffId(repairUserDtoList.get(0).getStaffId());
            repairUser.setStaffName(repairUserDtoList.get(0).getStaffName());
            repairUser.setPreStaffId(repairUserDtoList.get(0).getStaffId());
            repairUser.setPreStaffName(repairUserDtoList.get(0).getStaffName());
            repairUser.setPreRuId(repairUserDtoList.get(0).getRuId());
            repairUser.setRepairEvent("auditUser");
            flag = repairUserV1InnerServiceSMOImpl.saveRepairUserNew(repairUser);
            if (flag < 1) {
                throw new CmdException("修改失败");
            }
        }


    }

    @Override
    public void withholdAccount(JSONObject paramObj, String feeId, String communityId) {
        if (!paramObj.containsKey("accountAmount")) {
            return;
        }

        double accountAmount = paramObj.getDouble("accountAmount");

        if (accountAmount <= 0) {
            return;
        }

        AccountDetailPo accountDetailPo = new AccountDetailPo();
        accountDetailPo.setAcctId(paramObj.getString("acctId"));
        accountDetailPo.setAmount(accountAmount + "");
        accountDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        accountInnerServiceSMOImpl.withholdAccount(accountDetailPo);


    }

    @Override
    public void withholdAccount(PayFeeDataDto payFeeDataDto, String feeId, String communityId) {
        if (payFeeDataDto.getAccountAmount() <= 0) {
            return;
        }

        double accountAmount = payFeeDataDto.getAccountAmount();

        if (accountAmount <= 0) {
            return;
        }


        AccountDetailPo accountDetailPo = new AccountDetailPo();
        accountDetailPo.setAcctId(payFeeDataDto.getAcctId());
        accountDetailPo.setAmount(accountAmount + "");
        accountDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        accountInnerServiceSMOImpl.withholdAccount(accountDetailPo);

    }
}
