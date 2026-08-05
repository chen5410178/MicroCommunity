package com.java110.job.adapt.fee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.room.RoomDto;
import com.java110.dto.system.Business;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IPayFeeRuleV1InnerServiceSMO;
import com.java110.intf.fee.IRuleGeneratorPayFeeBillV1InnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.po.payFee.PayFeeRulePo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 欠费催缴databus
 */
@Component(value = "roomCreatePayFeeAdapt")
public class RoomCreatePayFeeAdapt extends DatabusAdaptImpl {
    private static final int DEFAULT_ADD_FEE_COUNT = 200;
    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IPayFeeRuleV1InnerServiceSMO payFeeRuleV1InnerServiceSMOImpl;

    @Autowired
    private IRuleGeneratorPayFeeBillV1InnerServiceSMO ruleGeneratorPayFeeBillV1InnerServiceSMOImpl;

    @Override
    public void execute(Business business, List<Business> businesses) throws ParseException {
        JSONObject data = business.getData();


        JSONArray roomIds = data.getJSONArray("roomIds");

        List<String> tmpRoomIds = new ArrayList<>();


        for (int roomIndex = 0; roomIndex < roomIds.size(); roomIndex++) {
            tmpRoomIds.add(roomIds.getString(roomIndex));
            if (tmpRoomIds.size() == DEFAULT_ADD_FEE_COUNT) {
                doCreateRoomPayFee(data, tmpRoomIds);
                tmpRoomIds = new ArrayList<>();
            }
        }
        if (!tmpRoomIds.isEmpty()) {
            doCreateRoomPayFee(data, tmpRoomIds);
        }
    }

    private void doCreateRoomPayFee(JSONObject data, List<String> roomIds) {
        RoomDto roomDto = new RoomDto();
        roomDto.setRoomIds(roomIds.toArray(new String[roomIds.size()]));
        roomDto.setCommunityId(data.getString("communityId"));
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

        //todo 房屋不存在，这种场景一般执行不到
        if (roomDtos == null || roomDtos.isEmpty()) {
            throw new IllegalArgumentException("房屋不存在");
        }

        //todo 查询费用项配置
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(data.getString("communityId"));
        feeConfigDto.setConfigId(data.getString("configId"));
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        Assert.listOnlyOne(feeConfigDtos, "当前费用项ID不存在或存在多条" + data.getString("configId"));

        //todo 封装费用规则（与"设置费用"链路一致，走规则->账单模式）
        List<PayFeeRulePo> payFeeRulePos = new ArrayList<>();
        for (int roomIndex = 0; roomIndex < roomDtos.size(); roomIndex++) {
            payFeeRulePos.add(createPayFeeRulePo(roomDtos.get(roomIndex), data, feeConfigDtos.get(0)));
            if (payFeeRulePos.size() == DEFAULT_ADD_FEE_COUNT) {
                doSavePayFeeRules(payFeeRulePos);
                payFeeRulePos = new ArrayList<>();
            }
        }
        if (!payFeeRulePos.isEmpty()) {
            doSavePayFeeRules(payFeeRulePos);
        }

    }

    /**
     * 保存费用规则并生成费用账单
     *
     * @param payFeeRulePos
     */
    private void doSavePayFeeRules(List<PayFeeRulePo> payFeeRulePos) {
        //todo 写费用规则表 pay_fee_rule
        payFeeRuleV1InnerServiceSMOImpl.savePayFeeRules(payFeeRulePos);

        //todo 走账单模式生成 pay_fee（内部按 paymentCycle/paymentCd/deductFrom
        //     计算 start_time/end_time/fee_time/金额，与"设置费用"链路完全一致）
        ruleGeneratorPayFeeBillV1InnerServiceSMOImpl.rulesGeneratePayFees(payFeeRulePos);
    }

    private PayFeeRulePo createPayFeeRulePo(RoomDto roomDto, JSONObject paramInJson, FeeConfigDto feeConfigDto) {

        String time = DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A);
        if (paramInJson.containsKey("feeEndDate")) {
            time = paramInJson.getString("feeEndDate");
        } else if (paramInJson.containsKey("startTime")) {
            time = paramInJson.getString("startTime");
        }

        PayFeeRulePo payFeeRulePo = new PayFeeRulePo();
        payFeeRulePo.setRuleId(GenerateCodeFactory.getGeneratorId("11"));
        payFeeRulePo.setConfigId(paramInJson.getString("configId"));
        payFeeRulePo.setCommunityId(paramInJson.getString("communityId"));
        payFeeRulePo.setFeeTypeCd(paramInJson.getString("feeTypeCd"));
        payFeeRulePo.setFeeFlag(paramInJson.getString("feeFlag"));
        payFeeRulePo.setAmount("-1");
        if (paramInJson.containsKey("amount") && !StringUtil.isEmpty(paramInJson.getString("amount"))) {
            payFeeRulePo.setAmount(paramInJson.getString("amount"));
        }
        payFeeRulePo.setStartTime(time);
        payFeeRulePo.setEndTime(time);
        payFeeRulePo.setMaxTime(paramInJson.getString("endTime"));
        payFeeRulePo.setBatchId(paramInJson.getString("batchId"));
        payFeeRulePo.setUserId(paramInJson.getString("userId"));
        payFeeRulePo.setIncomeObjId(paramInJson.getString("storeId"));
        payFeeRulePo.setState(FeeDto.STATE_DOING);
        payFeeRulePo.setDeductFrom(feeConfigDto.getDeductFrom());
        payFeeRulePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
        payFeeRulePo.setPayerObjId(roomDto.getRoomId());
        String curYearMonth = DateUtil.getFormatTimeStringB(DateUtil.getDateFromStringB(payFeeRulePo.getEndTime()));
        // 后付费
        if (FeeConfigDto.PAYMENT_CD_AFTER.equals(feeConfigDto.getPaymentCd())) {
            int ruleCycle = Integer.parseInt(feeConfigDto.getPaymentCycle());
            Date endTime = DateUtil.getNextMonthDateB(payFeeRulePo.getEndTime(), ruleCycle);
            curYearMonth = DateUtil.getFormatTimeStringB(endTime);
        }
        payFeeRulePo.setCurYearMonth(curYearMonth);
        return payFeeRulePo;
    }

}
