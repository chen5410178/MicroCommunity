package com.java110.community.cmd.parkingSpace;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IParkingSpaceV1InnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerCarV1InnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.po.car.OwnerCarPo;
import com.java110.po.parking.ParkingSpacePo;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "parkingSpace.updateParkingSpaceCar")
public class UpdateParkingSpaceCarCmd extends Cmd {

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceV1InnerServiceSMO parkingSpaceV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarV1InnerServiceSMO ownerCarV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "未包含小区ID");
        Assert.jsonObjectHaveKey(reqJson, "psId", "未包含psId");
        Assert.jsonObjectHaveKey(reqJson, "carId", "未包含carId");
        Assert.jsonObjectHaveKey(reqJson, "ownerId", "未包含ownerId");
        Assert.hasLength(reqJson.getString("communityId"), "小区ID不能为空");
        Assert.hasLength(reqJson.getString("psId"), "psId不能为空");
        Assert.hasLength(reqJson.getString("carId"), "carId不能为空");
        Assert.hasLength(reqJson.getString("ownerId"), "ownerId不能为空");

        //校验车位存在且当前状态必须为出租 H 或出售 S 才可修改车辆
        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setCommunityId(reqJson.getString("communityId"));
        parkingSpaceDto.setPsId(reqJson.getString("psId"));
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
        if (parkingSpaceDtos == null || parkingSpaceDtos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "未查询到停车位信息" + JSONObject.toJSONString(parkingSpaceDto));
        }
        if (ParkingSpaceDto.STATE_FREE.equals(parkingSpaceDtos.get(0).getState())) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "请先出租或出售车位后再修改车辆");
        }

        //按 carId 查询车辆，确认车辆存在并取车牌号（后端以车辆表为准）
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCarId(reqJson.getString("carId"));
        ownerCarDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
        if (ownerCarDtos == null || ownerCarDtos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "未查询到车辆信息" + JSONObject.toJSONString(ownerCarDto));
        }

        //原车位状态为出租 H 时 出租时间范围必填 且 结束时间晚于开始时间
        if (ParkingSpaceDto.STATE_HIRE.equals(parkingSpaceDtos.get(0).getState())) {
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

        // 查询车位原信息（含 paId、state），供同步车辆租期使用
        ParkingSpaceDto parkingSpaceDto = queryParkingSpace(reqJson);

        modifySellParkingSpaceState(reqJson);

        // 同步新选中车辆租期（仅同步新车辆，原绑定车辆租期不做清除）
        OwnerCarPo ownerCarPo = new OwnerCarPo();
        ownerCarPo.setCarId(reqJson.getString("carId"));
        ownerCarPo.setOwnerId(reqJson.getString("ownerId"));
        ownerCarPo.setCommunityId(reqJson.getString("communityId"));
        ownerCarPo.setPaId(parkingSpaceDto.getPaId());
        ownerCarPo.setLeaseType(parkingSpaceDto.getState());
        if (ParkingSpaceDto.STATE_HIRE.equals(parkingSpaceDto.getState())) {
            ownerCarPo.setStartTime(reqJson.getString("startTime"));
            ownerCarPo.setEndTime(reqJson.getString("endTime"));
        }
        int ownerCarFlag = ownerCarV1InnerServiceSMOImpl.updateOwnerCar(ownerCarPo);
        if (ownerCarFlag < 1) {
            throw new CmdException("修改车辆租期失败");
        }

        context.setResponseEntity(ResultVo.success());
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
     * 修改停车位绑定车辆信息（状态不变）
     *
     * @param paramInJson 接口调用放传入入参
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

        //按 carId 查询车辆信息，以车辆表为准取车牌号
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCarId(paramInJson.getString("carId"));
        ownerCarDto.setCommunityId(paramInJson.getString("communityId"));
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
        if (ownerCarDtos == null || ownerCarDtos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "未查询到车辆信息" + JSONObject.toJSONString(ownerCarDto));
        }

        JSONObject businessParkingSpace = new JSONObject();

        businessParkingSpace.putAll(BeanConvertUtil.beanCovertMap(parkingSpaceDto));
        businessParkingSpace.put("ownerId", paramInJson.getString("ownerId"));
        businessParkingSpace.put("ownerName", queryOwnerName(paramInJson.getString("ownerId"), paramInJson.getString("communityId")));
        businessParkingSpace.put("carId", paramInJson.getString("carId"));
        businessParkingSpace.put("carNum", ownerCarDtos.get(0).getCarNum());
        if (ParkingSpaceDto.STATE_HIRE.equals(parkingSpaceDto.getState())) {
            businessParkingSpace.put("startTime", paramInJson.getString("startTime"));
            businessParkingSpace.put("endTime", paramInJson.getString("endTime"));
        } else {
            businessParkingSpace.put("startTime", "");
            businessParkingSpace.put("endTime", "");
        }
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
}
