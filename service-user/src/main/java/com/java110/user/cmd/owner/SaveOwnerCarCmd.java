package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.machine.CarInoutDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.parking.ParkingAreaDto;
import com.java110.intf.common.ICarInoutInnerServiceSMO;
import com.java110.intf.community.IParkingAreaInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.user.IOwnerCarAttrInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerCarV1InnerServiceSMO;
import com.java110.po.car.OwnerCarPo;
import com.java110.po.owner.OwnerCarAttrPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@Java110Cmd(serviceCode = "owner.saveOwnerCar")
public class SaveOwnerCarCmd extends Cmd {


    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IParkingAreaInnerServiceSMO parkingAreaInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarAttrInnerServiceSMO ownerCarAttrInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarV1InnerServiceSMO ownerCarV1InnerServiceSMOImpl;

    @Autowired
    private ICarInoutInnerServiceSMO carInoutInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
        Assert.hasKeyAndValue(reqJson, "ownerId", "请求报文中未包含ownerId");
        Assert.hasKeyAndValue(reqJson, "carNum", "请求报文中未包含carNum");
        Assert.hasKeyAndValue(reqJson, "carType", "请求报文中未包含carType");
        Assert.hasKeyAndValue(reqJson, "paId", "未包含paId");

        //车牌类型仅允许 月租车（H）、内部车（I）、免费车（NM）
        if (!StringUtil.isEmpty(reqJson.getString("leaseType"))
                && !OwnerCarDto.LEASE_TYPE_MONTH.equals(reqJson.getString("leaseType"))
                && !OwnerCarDto.LEASE_TYPE_INNER.equals(reqJson.getString("leaseType"))
                && !OwnerCarDto.LEASE_TYPE_NO_MONEY.equals(reqJson.getString("leaseType"))) {
            throw new IllegalArgumentException("车牌类型仅支持月租车、内部车、免费车");
        }

        //校验停车场是否存在
        ParkingAreaDto parkingAreaDto = new ParkingAreaDto();
        parkingAreaDto.setPaId(reqJson.getString("paId"));
        List<ParkingAreaDto> parkingAreaDtos = parkingAreaInnerServiceSMOImpl.queryParkingAreas(parkingAreaDto);
        Assert.listOnlyOne(parkingAreaDtos, "查询停车场错误！");

        if (OwnerCarDto.LEASE_TYPE_MONTH.equals(reqJson.getString("leaseType"))) {
            Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含开始时间");
            Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含结束时间");
            Assert.isDate(reqJson.getString("startTime"),DateUtil.DATE_FORMATE_STRING_B,"开始时间格式错误");
            Assert.isDate(reqJson.getString("endTime"),DateUtil.DATE_FORMATE_STRING_B,"结束时间格式错误");
        }

        String startTime = reqJson.getString("startTime");
        String endTime = reqJson.getString("endTime");
        if(StringUtil.isEmpty(startTime)){
            startTime = DateUtil.getFormatTimeString(new Date(), DateUtil.DATE_FORMATE_STRING_B);
            reqJson.put("startTime",startTime);
        }
        if(StringUtil.isEmpty(endTime)){
            endTime = "2050-01-01";
            reqJson.put("endTime",endTime);
        }

        //联系电话格式校验
        if (!StringUtil.isEmpty(reqJson.getString("tel")) && !ValidatorUtil.isMobile(reqJson.getString("tel"))) {
            throw new IllegalArgumentException("联系电话格式不正确");
        }

        //校验车牌号是否存在
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCommunityId(reqJson.getString("communityId"));
        ownerCarDto.setCarNum(reqJson.getString("carNum"));
        ownerCarDto.setPaIds(new String[]{reqJson.getString("paId")});
        ownerCarDto.setCarTypeCds(new String[]{OwnerCarDto.CAR_TYPE_PRIMARY, OwnerCarDto.CAR_TYPE_MEMBER}); // 临时车除外
        int count = ownerCarInnerServiceSMOImpl.queryOwnerCarsCount(ownerCarDto);

        if (count > 0) {
            throw new IllegalArgumentException("车辆已存在");
        }

        //判断临时车 是否在场
        String parkingIn = MappingCache.getValue("TEMP_CAR_IN_PARKING");

        if (!"ON".equals(parkingIn)) {
            return;
        }

        ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCommunityId(reqJson.getString("communityId"));
        ownerCarDto.setCarNum(reqJson.getString("carNum"));
        ownerCarDto.setCarTypeCds(new String[]{OwnerCarDto.CAR_TYPE_TEMP}); // 临时车除外
        count = ownerCarInnerServiceSMOImpl.queryOwnerCarsCount(ownerCarDto);
        if (count < 1) {
            return;
        }

        CarInoutDto carInoutDto = new CarInoutDto();
        carInoutDto.setCarNum(reqJson.getString("carNum"));
        carInoutDto.setStates(new String[]{CarInoutDto.STATE_PAY, CarInoutDto.STATE_IN, CarInoutDto.STATE_REPAY});
        List<CarInoutDto> carInoutDtos = carInoutInnerServiceSMOImpl.queryCarInouts(carInoutDto);
        if (!ListUtil.isNull(carInoutDtos)) {
            throw new CmdException("车辆在场，请出场后再办理月租车");
        }
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        if (!reqJson.containsKey("leaseType")) {
            reqJson.put("leaseType", OwnerCarDto.LEASE_TYPE_MONTH);
        }

        JSONObject businessOwnerCar = new JSONObject();
        businessOwnerCar.putAll(reqJson);
        businessOwnerCar.put("memberId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_carId));
        if (!reqJson.containsKey("carId") || reqJson.getString("carId").startsWith("-")) {
            businessOwnerCar.put("carId", businessOwnerCar.getString("memberId"));
        }
        OwnerCarPo ownerCarPo = BeanConvertUtil.covertBean(businessOwnerCar, OwnerCarPo.class);
        ownerCarPo.setState(OwnerCarDto.STATE_NORMAL);

        //没有指定时为主要车辆
        if (!reqJson.containsKey("carTypeCd") || StringUtil.isEmpty(reqJson.getString("carTypeCd"))) {
            ownerCarPo.setCarTypeCd(OwnerCarDto.CAR_TYPE_PRIMARY);
        }
        //添加车辆属性
        dealOwnerCarAttr(reqJson, ownerCarPo);


        int flag = ownerCarV1InnerServiceSMOImpl.saveOwnerCar(ownerCarPo);
        if (flag < 1) {
            throw new CmdException("保存车辆属性失败");
        }

    }


    private void dealOwnerCarAttr(JSONObject paramInJson, OwnerCarPo ownerCarPo) {

        if (!paramInJson.containsKey("attrs")) {
            return;
        }

        JSONArray attrs = paramInJson.getJSONArray("attrs");
        if (attrs.size() < 1) {
            return;
        }
        JSONObject attr = null;
        int flag = 0;
        for (int attrIndex = 0; attrIndex < attrs.size(); attrIndex++) {
            attr = attrs.getJSONObject(attrIndex);
            OwnerCarAttrPo ownerCarAttrPo = new OwnerCarAttrPo();
            ownerCarAttrPo.setAttrId(GenerateCodeFactory.getAttrId());
            ownerCarAttrPo.setCommunityId(ownerCarPo.getCommunityId());
            ownerCarAttrPo.setCarId(ownerCarPo.getCarId());
            ownerCarAttrPo.setSpecCd(attr.getString("specCd"));
            ownerCarAttrPo.setValue(attr.getString("value"));
            flag = ownerCarAttrInnerServiceSMOImpl.saveOwnerCarAttr(ownerCarAttrPo);
            if (flag < 1) {
                throw new CmdException("保存车辆属性失败");
            }
        }

    }
}
