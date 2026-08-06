package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.room.RoomDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.payFee.PayFeeBatchDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.user.IOwnerRoomRelV1InnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.fee.*;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.owner.OwnerPo;
import com.java110.po.owner.OwnerRoomRelPo;
import com.java110.po.payFee.PayFeeBatchPo;
import com.java110.po.payFee.PayFeeRulePo;
import com.java110.po.room.RoomPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.*;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Java110Cmd(serviceCode = "owner.saveHandover")
public class SaveHandoverCmd extends Cmd {

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelV1InnerServiceSMO ownerRoomRelV1InnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IPayFeeBatchV1InnerServiceSMO payFeeBatchV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeRuleV1InnerServiceSMO payFeeRuleV1InnerServiceSMOImpl;

    @Autowired
    private IRuleGeneratorPayFeeBillV1InnerServiceSMO ruleGeneratorPayFeeBillV1InnerServiceSMOImpl;

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        Assert.jsonObjectHaveKey(reqJson, "name", "请求报文中未包含name");
        Assert.jsonObjectHaveKey(reqJson, "userId", "请求报文中未包含userId");
        Assert.jsonObjectHaveKey(reqJson, "age", "请求报文中未包含age");
        Assert.jsonObjectHaveKey(reqJson, "link", "请求报文中未包含link");
        Assert.jsonObjectHaveKey(reqJson, "sex", "请求报文中未包含sex");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId");

        if (!reqJson.containsKey("rooms")) {
            throw new CmdException("未包含房屋信息");
        }

        JSONArray rooms = reqJson.getJSONArray("rooms");

        if (ListUtil.isNull(rooms)) {
            throw new CmdException("未包含房屋信息");
        }
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        OwnerPo ownerPo = BeanConvertUtil.covertBean(reqJson, OwnerPo.class);
        int flag = 0;
        if (StringUtil.isEmpty(ownerPo.getAge())) {
            ownerPo.setAge(null);
        }
        if (reqJson.containsKey("ownerId") && !StringUtil.isEmpty(reqJson.getString("ownerId"))) {
            ownerPo.setMemberId(ownerPo.getOwnerId());
            flag = ownerV1InnerServiceSMOImpl.updateOwner(ownerPo);
        } else {
            ownerPo.setOwnerTypeCd(OwnerDto.OWNER_TYPE_CD_OWNER);
            ownerPo.setOwnerId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ownerId));
            ownerPo.setMemberId(ownerPo.getOwnerId());
            ownerPo.setState(OwnerDto.STATE_FINISH);
            ownerPo.setOwnerFlag(OwnerDto.OWNER_FLAG_TRUE);
            ownerPo.setPersonType(OwnerDto.PERSON_TYPE_PERSON);
            ownerPo.setPersonRole(OwnerDto.PERSON_ROLE_OWNER);
            flag = ownerV1InnerServiceSMOImpl.saveOwner(ownerPo);
            reqJson.put("ownerId", ownerPo.getOwnerId());
        }
        if (flag < 1) {
            throw new CmdException("操作业主失败");
        }
        JSONArray rooms = reqJson.getJSONArray("rooms");
        OwnerRoomRelPo ownerRoomRelPo = null;
        for (int roomIndex = 0; roomIndex < rooms.size(); roomIndex++) {
            ownerRoomRelPo = new OwnerRoomRelPo();
            ownerRoomRelPo.setbId("-1");
            ownerRoomRelPo.setEndTime(DateUtil.getLastTime());
            ownerRoomRelPo.setOwnerId(reqJson.getString("ownerId"));
            ownerRoomRelPo.setOperate("ADD");
            ownerRoomRelPo.setRelId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_relId));
            ownerRoomRelPo.setRoomId(rooms.getJSONObject(roomIndex).getString("roomId"));
            ownerRoomRelPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            ownerRoomRelPo.setState("2001");
            ownerRoomRelPo.setUserId("-1");
            flag = ownerRoomRelV1InnerServiceSMOImpl.saveOwnerRoomRel(ownerRoomRelPo);
            if (flag < 1) {
                throw new CmdException("操作业主失败");
            }
            RoomPo roomPo = new RoomPo();
            roomPo.setRoomId(rooms.getJSONObject(roomIndex).getString("roomId"));
            roomPo.setCommunityId(reqJson.getString("communityId"));
            roomPo.setState(RoomDto.STATE_SELL);
            flag = roomV1InnerServiceSMOImpl.updateRoom(roomPo);
            if (flag < 1) {
                throw new CmdException("操作业主失败");
            }
        }
        if (!reqJson.containsKey("fees")) {
            cmdDataFlowContext.setResponseEntity(ResultVo.success());
            return;
        }
        JSONArray fees = reqJson.getJSONArray("fees");
        if (fees.size() < 1) {
            cmdDataFlowContext.setResponseEntity(ResultVo.success());
            return;
        }
        generatorBatch(reqJson);

        List<PayFeeRulePo> payFeeRulePos = new ArrayList<>();
        JSONObject paramInJson = null;
        for (int roomIndex = 0; roomIndex < rooms.size(); roomIndex++) {
            for (int feeIndex = 0; feeIndex < fees.size(); feeIndex++) {
                paramInJson = fees.getJSONObject(feeIndex);

                FeeConfigDto feeConfigDto = new FeeConfigDto();
                feeConfigDto.setConfigId(paramInJson.getString("configId"));
                List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
                Assert.listOnlyOne(feeConfigDtos, "查询费用项错误！");

                PayFeeRulePo payFeeRulePo = createPayFeeRulePo(rooms.getJSONObject(roomIndex), paramInJson, reqJson, feeConfigDtos.get(0));
                payFeeRulePos.add(payFeeRulePo);
            }
        }

        payFeeRuleV1InnerServiceSMOImpl.savePayFeeRules(payFeeRulePos);

        ruleGeneratorPayFeeBillV1InnerServiceSMOImpl.rulesGeneratePayFees(payFeeRulePos);

    }

    /**
     * 生成批次号
     *
     * @param reqJson
     */
    private void generatorBatch(JSONObject reqJson) {
        PayFeeBatchPo payFeeBatchPo = new PayFeeBatchPo();
        payFeeBatchPo.setBatchId(GenerateCodeFactory.getGeneratorId("12"));
        payFeeBatchPo.setCommunityId(reqJson.getString("communityId"));
        payFeeBatchPo.setCreateUserId(reqJson.getString("userId"));
        UserDto userDto = new UserDto();
        userDto.setUserId(reqJson.getString("userId"));
        List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);
        Assert.listOnlyOne(userDtos, "用户不存在");
        payFeeBatchPo.setCreateUserName(userDtos.get(0).getUserName());
        payFeeBatchPo.setState(PayFeeBatchDto.STATE_NORMAL);
        payFeeBatchPo.setMsg("正常");
        int flag = payFeeBatchV1InnerServiceSMOImpl.savePayFeeBatch(payFeeBatchPo);
        if (flag < 1) {
            throw new IllegalArgumentException("生成批次失败");
        }
        reqJson.put("batchId", payFeeBatchPo.getBatchId());
    }


    private PayFeeRulePo createPayFeeRulePo(JSONObject room, JSONObject paramInJson, JSONObject reqJson, FeeConfigDto feeConfigDto) {
        String time = DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A);
        if (paramInJson.containsKey("startTime")) {
            time = paramInJson.getString("startTime");
        }

        PayFeeRulePo payFeeRulePo = new PayFeeRulePo();
        payFeeRulePo.setMaxTime(paramInJson.containsKey("endTime") && !StringUtil.isEmpty(paramInJson.getString("endTime"))
                ? paramInJson.getString("endTime") : feeConfigDto.getEndTime());
        payFeeRulePo.setAmount("-1");
        if (paramInJson.containsKey("amount") && !StringUtil.isEmpty(paramInJson.getString("amount"))) {
            payFeeRulePo.setAmount(paramInJson.getString("amount"));
        }
        payFeeRulePo.setFeeTypeCd(feeConfigDto.getFeeTypeCd());
        payFeeRulePo.setBatchId(reqJson.getString("batchId"));
        payFeeRulePo.setUserId("-1");
        payFeeRulePo.setIncomeObjId(reqJson.getString("storeId"));
        payFeeRulePo.setConfigId(feeConfigDto.getConfigId());
        payFeeRulePo.setFeeFlag(feeConfigDto.getFeeFlag());
        payFeeRulePo.setStartTime(time);
        payFeeRulePo.setEndTime(time);
        payFeeRulePo.setDeductFrom(feeConfigDto.getDeductFrom());
        payFeeRulePo.setState(FeeDto.STATE_DOING);
        payFeeRulePo.setRuleId(GenerateCodeFactory.getGeneratorId("11"));
        payFeeRulePo.setCommunityId(reqJson.getString("communityId"));
        payFeeRulePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
        payFeeRulePo.setPayerObjId(room.getString("roomId"));
        //todo 交接建费回查业主表确认业主信息，禁止前端透传
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setOwnerId(reqJson.getString("ownerId"));
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);
        Assert.listOnlyOne(ownerDtos, "查询业主信息错误");
        payFeeRulePo.setOwnerId(ownerDtos.get(0).getOwnerId());
        payFeeRulePo.setOwnerName(ownerDtos.get(0).getName());
        //todo 交接建费缴费对象名称经 queryRooms 查询房屋拼接，禁止 roomId 兜底
        RoomDto roomDto = new RoomDto();
        roomDto.setRoomId(room.getString("roomId"));
        List<RoomDto> roomDtos = roomV1InnerServiceSMOImpl.queryRooms(roomDto);
        Assert.listOnlyOne(roomDtos, "查询房屋信息错误");
        payFeeRulePo.setPayerObjName(roomDtos.get(0).getFloorNum() + "-"
                + roomDtos.get(0).getUnitNum() + "-" + roomDtos.get(0).getRoomNum());

        String curYearMonth = DateUtil.getFormatTimeStringB(DateUtil.getDateFromStringB(payFeeRulePo.getEndTime()));
        if (FeeConfigDto.PAYMENT_CD_AFTER.equals(feeConfigDto.getPaymentCd())) {
            int ruleCycle = Integer.parseInt(feeConfigDto.getPaymentCycle());
            Date endTime = DateUtil.getNextMonthDateB(payFeeRulePo.getEndTime(), ruleCycle);
            curYearMonth = DateUtil.getFormatTimeStringB(endTime);
        }
        payFeeRulePo.setCurYearMonth(curYearMonth);

        return payFeeRulePo;
    }
}