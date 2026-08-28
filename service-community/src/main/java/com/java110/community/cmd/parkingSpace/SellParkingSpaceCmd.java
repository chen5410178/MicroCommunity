package com.java110.community.cmd.parkingSpace;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IParkingSpaceV1InnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.user.IOwnerCarAttrInnerServiceSMO;
import com.java110.intf.user.IOwnerCarV1InnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.po.car.OwnerCarPo;
import com.java110.po.owner.OwnerCarAttrPo;
import com.java110.po.parking.ParkingSpacePo;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Java110Cmd(serviceCode = "parkingSpace.sellParkingSpace")
public class SellParkingSpaceCmd extends Cmd {


    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;
    @Autowired
    private IParkingSpaceV1InnerServiceSMO parkingSpaceV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerCarV1InnerServiceSMO ownerCarV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerCarAttrInnerServiceSMO ownerCarAttrInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "未包含小区ID");
        Assert.jsonObjectHaveKey(reqJson, "ownerId", "请求报文中未包含ownerId");
        Assert.jsonObjectHaveKey(reqJson, "psId", "未包含psId");
        Assert.jsonObjectHaveKey(reqJson, "storeId", "未包含storeId");
        Assert.jsonObjectHaveKey(reqJson, "sellOrHire", "未包含sellOrHire");

        Assert.hasLength(reqJson.getString("communityId"), "小区ID不能为空");
        Assert.hasLength(reqJson.getString("ownerId"), "ownerId不能为空");
        Assert.hasLength(reqJson.getString("psId"), "psId不能为空");

        if (!"H".equals(reqJson.getString("sellOrHire"))
                && !"S".equals(reqJson.getString("sellOrHire"))) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "请求报文中sellOrFire值错误 ，出售为S 出租为H");
        }

        //校验车位当前状态必须为空闲 F 才可出租/出售
        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setCommunityId(reqJson.getString("communityId"));
        parkingSpaceDto.setPsId(reqJson.getString("psId"));
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
        if (parkingSpaceDtos == null || parkingSpaceDtos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "未查询到停车位信息" + JSONObject.toJSONString(parkingSpaceDto));
        }
        if (!ParkingSpaceDto.STATE_FREE.equals(parkingSpaceDtos.get(0).getState())) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "当前车位状态不允许出租或出售");
        }

        //出租时 出租时间范围必填 且 结束时间晚于开始时间
        if ("H".equals(reqJson.getString("sellOrHire"))) {
            Assert.jsonObjectHaveKey(reqJson, "startTime", "出租时 开始时间不能为空");
            Assert.jsonObjectHaveKey(reqJson, "endTime", "出租时 结束时间不能为空");
            Assert.isDate(reqJson.getString("startTime"), DateUtil.DATE_FORMATE_STRING_A, "开始时间不是有效时间");
            Assert.isDate(reqJson.getString("endTime"), DateUtil.DATE_FORMATE_STRING_A, "结束时间不是有效时间");
            if (DateUtil.getDateFromStringA(reqJson.getString("endTime")).before(DateUtil.getDateFromStringA(reqJson.getString("startTime")))) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "结束时间必须晚于开始时间");
            }
        }
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        String feeId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId);
        reqJson.put("feeId", feeId);

        // 先查车位，取 paId（供车辆绑定租期）
        ParkingSpaceDto psDto = queryParkingSpace(reqJson);
        reqJson.put("paId", psDto.getPaId());

        // 传了车牌号或有效 carId 才处理车辆绑定（区分已有/新建车辆）
        boolean hasCarNum = reqJson.containsKey("carNum") && !StringUtil.isEmpty(reqJson.getString("carNum"));
        boolean hasCarId = reqJson.containsKey("carId") && !StringUtil.isEmpty(reqJson.getString("carId"))
                && !reqJson.getString("carId").startsWith("-");
        if (hasCarNum || hasCarId) {
            sellParkingSpace(reqJson);
        }

        reqJson.put("carNumType", reqJson.getString("sellOrHire"));

        modifySellParkingSpaceState(reqJson);
    }

    /**
     * 查询停车位信息
     *
     * @param paramInJson 接口调用方传入入参
     * @return 停车位信息
     */
    private ParkingSpaceDto queryParkingSpace(JSONObject paramInJson) {
        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setCommunityId(paramInJson.getString("communityId"));
        parkingSpaceDto.setPsId(paramInJson.getString("psId"));
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
        if (parkingSpaceDtos == null || parkingSpaceDtos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "未查询到停车位信息" + JSONObject.toJSONString(parkingSpaceDto));
        }
        return parkingSpaceDtos.get(0);
    }



    /**
     * 修改停车位状态信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void modifySellParkingSpaceState(JSONObject paramInJson) {

        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setCommunityId(paramInJson.getString("communityId"));
        parkingSpaceDto.setPsId(paramInJson.getString("psId"));
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);

        if (parkingSpaceDtos == null || parkingSpaceDtos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "未查询到停车位信息" + JSONObject.toJSONString(parkingSpaceDto));
        }

        parkingSpaceDto = parkingSpaceDtos.get(0);

        JSONObject businessParkingSpace = new JSONObject();

        businessParkingSpace.putAll(BeanConvertUtil.beanCovertMap(parkingSpaceDto));
        businessParkingSpace.put("state", paramInJson.getString("carNumType"));
        businessParkingSpace.put("ownerId", paramInJson.getString("ownerId"));
        businessParkingSpace.put("ownerName", queryOwnerName(paramInJson.getString("ownerId"), paramInJson.getString("communityId")));
        businessParkingSpace.put("carId", paramInJson.getString("carId"));
        businessParkingSpace.put("carNum", paramInJson.getString("carNum"));
        businessParkingSpace.put("startTime", paramInJson.getString("startTime"));
        businessParkingSpace.put("endTime", paramInJson.getString("endTime"));
        ParkingSpacePo parkingSpacePo = BeanConvertUtil.covertBean(businessParkingSpace, ParkingSpacePo.class);
        int flag = parkingSpaceV1InnerServiceSMOImpl.updateParkingSpace(parkingSpacePo);
        if (flag < 1) {
            throw new CmdException("修改车位失败");
        }
    }

    /**
     * 查询业主名称
     *
     * @param ownerId 业主ID
     * @return 业主名称
     */
    private String queryOwnerName(String ownerId, String communityId) {
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setOwnerId(ownerId);
        ownerDto.setCommunityId(communityId);
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
        if (ownerDtos == null || ownerDtos.size() < 1) {
            return "";
        }
        return ownerDtos.get(0).getName();
    }

    /**
     * 出租/出售车位绑定车辆：区分已有车辆（同步租期）与新建车辆（写入租期）
     * <p>
     * * name:'',
     * *                 age:'',
     * *                 link:'',
     * *                 sex:'',
     * *                 remark:''
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void sellParkingSpace(JSONObject paramInJson) {

        String carId = paramInJson.getString("carId");
        boolean isExistCar = !StringUtil.isEmpty(carId) && !carId.startsWith("-");

        JSONObject businessOwnerCar = new JSONObject();
        businessOwnerCar.putAll(paramInJson);

        if (isExistCar) {
            // 已有车辆：按 carId 查询确认车辆存在，未查询到直接报错，不允许走新建逻辑
            OwnerCarDto ownerCarDto = new OwnerCarDto();
            ownerCarDto.setCarId(carId);
            ownerCarDto.setCarTypeCd(OwnerCarDto.CAR_TYPE_PRIMARY);
            ownerCarDto.setCommunityId(paramInJson.getString("communityId"));
            List<OwnerCarDto> ownerCarDtos = ownerCarV1InnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
            if (ownerCarDtos == null || ownerCarDtos.size() != 1) {
                throw new CmdException("未查询到车辆信息，无法绑定该车辆");
            }

            OwnerCarPo ownerCarPo = BeanConvertUtil.covertBean(businessOwnerCar, OwnerCarPo.class);
            ownerCarPo.setState(OwnerCarDto.STATE_NORMAL);
            ownerCarPo.setLeaseType(paramInJson.getString("sellOrHire"));
            ownerCarPo.setPaId(paramInJson.getString("paId"));
            if ("H".equals(paramInJson.getString("sellOrHire"))) {
                ownerCarPo.setStartTime(paramInJson.getString("startTime"));
                ownerCarPo.setEndTime(paramInJson.getString("endTime"));
            }
            // 同步租期，不重复插入
            int flag = ownerCarV1InnerServiceSMOImpl.updateOwnerCar(ownerCarPo);
            if (flag < 1) {
                throw new CmdException("修改车辆租期失败");
            }
            return;
        }

        // 新建车辆（未传 carId 或以 - 开头），写入 leaseType/startTime/endTime/paId
        businessOwnerCar.put("memberId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_carId));
        businessOwnerCar.put("carId", businessOwnerCar.getString("memberId"));
        paramInJson.put("carId", businessOwnerCar.getString("memberId"));
        OwnerCarPo ownerCarPo = BeanConvertUtil.covertBean(businessOwnerCar, OwnerCarPo.class);
        ownerCarPo.setState(OwnerCarDto.STATE_NORMAL);
        ownerCarPo.setLeaseType(paramInJson.getString("sellOrHire"));
        ownerCarPo.setPaId(paramInJson.getString("paId"));
        if ("H".equals(paramInJson.getString("sellOrHire"))) {
            ownerCarPo.setStartTime(paramInJson.getString("startTime"));
            ownerCarPo.setEndTime(paramInJson.getString("endTime"));
        }

        //没有指定时为主要车辆
        if (!paramInJson.containsKey("carTypeCd") || StringUtil.isEmpty(paramInJson.getString("carTypeCd"))) {
            ownerCarPo.setCarTypeCd(OwnerCarDto.CAR_TYPE_PRIMARY);
        }
        //添加车辆属性
        dealOwnerCarAttr(paramInJson, ownerCarPo);
        int flag =  ownerCarV1InnerServiceSMOImpl.saveOwnerCar(ownerCarPo);
        if (flag < 1) {
            throw new CmdException("保存车辆失败");
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
