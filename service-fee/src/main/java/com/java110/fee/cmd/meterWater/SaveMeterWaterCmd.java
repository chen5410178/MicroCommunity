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
package com.java110.fee.cmd.meterWater;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.room.RoomDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.fee.SaveMeterWaterFeeDto;
import com.java110.dto.payFee.PayFeeBatchDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.*;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.meter.MeterWaterPo;
import com.java110.po.payFee.PayFeeBatchPo;
import com.java110.po.payFee.PayFeeRulePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.*;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 类表述：保存
 * 服务编码：meterWater.saveMeterWater
 * 请求路劲：/app/meterWater.SaveMeterWater
 * add by 吴学文 at 2022-07-21 09:17:10 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "meterWater.saveMeterWater")
public class SaveMeterWaterCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveMeterWaterCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    //域
    public static final String DOMAIN_COMMON = "DOMAIN.COMMON";

    //键(水费黑名单)
    public static final String WATER_BLACK_LIST = "WATER_BLACK_LIST";

    //键(电费黑名单)
    public static final String ELECTRIC_BLACK_LIST = "ELECTRIC_BLACK_LIST";

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IPayFeeConfigV1InnerServiceSMO payFeeConfigV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeBatchV1InnerServiceSMO payFeeBatchV1InnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private ISaveMeterWaterFeeInnerServiceSMO saveMeterWaterFeeInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "configId", "请求报文中未包含费用项");
        Assert.hasKeyAndValue(reqJson, "objType", "请求报文中未包含objType");
        Assert.hasKeyAndValue(reqJson, "objId", "请求报文中未包含objId");
        Assert.hasKeyAndValue(reqJson, "preDegrees", "请求报文中未包含preDegrees");
        Assert.hasKeyAndValue(reqJson, "curDegrees", "请求报文中未包含curDegrees");
        Assert.hasKeyAndValue(reqJson, "preReadingTime", "请求报文中未包含preReadingTime");
        Assert.hasKeyAndValue(reqJson, "curReadingTime", "请求报文中未包含curReadingTime");
        Assert.hasKeyAndValue(reqJson, "objType", "请求报文中未包含objType");
        Assert.hasKeyAndValue(reqJson, "meterType", "请求报文中未包含抄表类型");

        if (reqJson.getDoubleValue("curDegrees") < reqJson.getDoubleValue("preDegrees")) {
            throw new CmdException("当前读数小于上期读数");
        }


    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        String userId = CmdContextUtils.getUserId(cmdDataFlowContext);
        String storeId = CmdContextUtils.getStoreId(cmdDataFlowContext);
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setConfigId(reqJson.getString("configId"));
        feeConfigDto.setCommunityId(reqJson.getString("communityId"));
        List<FeeConfigDto> feeConfigDtos = payFeeConfigV1InnerServiceSMOImpl.queryPayFeeConfigs(feeConfigDto);
        if (ListUtil.isNull(feeConfigDtos)) {
            throw new CmdException("费用项不存在");
        }
        reqJson.put("feeTypeCd", feeConfigDtos.get(0).getFeeTypeCd());
        String objId = reqJson.getString("objId");
        RoomDto roomDto = new RoomDto();
        roomDto.setRoomId(objId);
        List<RoomDto> roomList = roomInnerServiceSMOImpl.queryRooms(roomDto);
        Assert.listOnlyOne(roomList, "查询房屋信息错误！");
        //生成批次
        generatorBatch(reqJson);
        //获取抄表对象所属小区id
        String communityId = roomList.get(0).getCommunityId();
        String ruleId = GenerateCodeFactory.getGeneratorId("11");
        //获取表类型
        String meterType = reqJson.getString("meterType");

        MeterWaterPo meterWaterPo = BeanConvertUtil.covertBean(reqJson, MeterWaterPo.class);
        if (StringUtil.isEmpty(meterWaterPo.getbId())) {
            meterWaterPo.setbId("-1");
        }
        meterWaterPo.setWaterId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        meterWaterPo.setObjName(roomList.get(0).getFloorNum() + "-" + roomList.get(0).getUnitNum() + "-" + roomList.get(0).getRoomNum());
        meterWaterPo.setFeeId(ruleId);

        PayFeeRulePo payFeeRulePo = new PayFeeRulePo();
        payFeeRulePo.setMaxTime(reqJson.getString("curReadingTime"));
        payFeeRulePo.setAmount("-1");
        payFeeRulePo.setFeeTypeCd(feeConfigDtos.get(0).getFeeTypeCd());
        payFeeRulePo.setCurYearMonth(reqJson.getString("preReadingTime").split(" ")[0]);
        payFeeRulePo.setBatchId(reqJson.getString("batchId"));
        payFeeRulePo.setUserId(userId);
        payFeeRulePo.setIncomeObjId(storeId);
        payFeeRulePo.setConfigId(reqJson.getString("configId"));
        payFeeRulePo.setFeeFlag(FeeDto.FEE_FLAG_ONCE);
        payFeeRulePo.setStartTime(reqJson.getString("preReadingTime"));
        payFeeRulePo.setEndTime(reqJson.getString("preReadingTime"));
        payFeeRulePo.setState(FeeDto.STATE_DOING);
        payFeeRulePo.setRuleId(ruleId);
        payFeeRulePo.setCommunityId(reqJson.getString("communityId"));
        payFeeRulePo.setPayerObjType(reqJson.getString("objType"));
        payFeeRulePo.setPayerObjId(reqJson.getString("objId"));
        //todo 抄表建费补查业主信息，未绑定业主时置空
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(communityId);
        ownerDto.setRoomId(objId);
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnersByRoom(ownerDto);
        if (!ListUtil.isNull(ownerDtos)) {
            payFeeRulePo.setOwnerId(ownerDtos.get(0).getOwnerId());
            payFeeRulePo.setOwnerName(ownerDtos.get(0).getName());
        }
        payFeeRulePo.setPayerObjName(meterWaterPo.getObjName());

        //todo 统一走"抄表 -> 规则 -> 账单"标准链路（与批量导入抄表一致）
        SaveMeterWaterFeeDto saveMeterWaterFeeDto = new SaveMeterWaterFeeDto();
        saveMeterWaterFeeDto.setMeterWaterPo(meterWaterPo);
        saveMeterWaterFeeDto.setPayFeeRulePo(payFeeRulePo);
        int flag = saveMeterWaterFeeInnerServiceSMOImpl.saveMeterWaterAndFee(saveMeterWaterFeeDto);
        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
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

}
