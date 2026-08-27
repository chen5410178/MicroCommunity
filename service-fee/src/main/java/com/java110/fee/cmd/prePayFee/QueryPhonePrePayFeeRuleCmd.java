package com.java110.fee.cmd.prePayFee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.fee.FeeAmountDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.payFee.PayFeeRuleDto;
import com.java110.dto.prePayFee.PrePayFeeDto;
import com.java110.dto.room.RoomDto;
import com.java110.fee.bill.IComputeBillPrice;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeRuleV1InnerServiceSMO;
import com.java110.intf.fee.IPrePayFeeV1InnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelV1InnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "prePayFee.queryPhonePrePayFeeRule")
public class QueryPhonePrePayFeeRuleCmd extends Cmd {

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelV1InnerServiceSMO ownerRoomRelV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeRuleV1InnerServiceSMO payFeeRuleV1InnerServiceSMOImpl;

    @Autowired
    private IComputeBillPrice computeBillPriceImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IPrePayFeeV1InnerServiceSMO prePayFeeV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "payerObjId", "未包含费用对象");
        Assert.hasKeyAndValue(reqJson, "payerObjType", "未包含费用对象类型");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        PayFeeRuleDto payFeeRuleDto = new PayFeeRuleDto();
        payFeeRuleDto.setPayerObjId(reqJson.getString("payerObjId"));
        payFeeRuleDto.setCommunityId(reqJson.getString("communityId"));
        payFeeRuleDto.setState(FeeDto.STATE_DOING);
        payFeeRuleDto.setFeeFlag(FeeDto.FEE_FLAG_CYCLE);
        List<PayFeeRuleDto> payFeeRuleDtos = payFeeRuleV1InnerServiceSMOImpl.queryPayFeeRules(payFeeRuleDto);
        if (!ListUtil.isNull(payFeeRuleDtos)) {
            FeeAmountDto feeAmountDto = null;
            for (PayFeeRuleDto tPayFeeRuleDto : payFeeRuleDtos) {
                feeAmountDto = computeBillPriceImpl.computeBillReceiveAmount(tPayFeeRuleDto, 1);
                tPayFeeRuleDto.setMonthAmount(feeAmountDto.getAmount());
                tPayFeeRuleDto.setRemark(feeAmountDto.getRemark());
                computeStartMonth(tPayFeeRuleDto);
            }
        } else {
            payFeeRuleDtos = new ArrayList<>();
        }

        JSONObject paramOut = new JSONObject();
        paramOut.put("rules", payFeeRuleDtos);
        // 计算房屋信息
        computeRoomInfo(paramOut, reqJson.getString("payerObjId"), reqJson.getString("payerObjType"), reqJson.getString("communityId"));
        computeParkingSpaceInfo(paramOut, reqJson.getString("payerObjId"), reqJson.getString("payerObjType"), reqJson.getString("communityId"));

        context.setResponseEntity(ResultVo.createResponseEntity(paramOut));

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
