package com.java110.job.adapt.hcIot;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.machine.MachineTranslateDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.system.Business;
import com.java110.intf.common.IMachineTranslateInnerServiceSMO;
import com.java110.intf.community.IParkingSpaceV1InnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.adapt.hcIot.http.ISendIot;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 同步车辆信息
 */
@Component(value = "sendCarDataToIotAdapt")
public class SendCarDataToIotAdapt extends DatabusAdaptImpl {

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceV1InnerServiceSMO parkingSpaceV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerDataToIot ownerDataToIotImpl;

    @Autowired
    private IMachineTranslateInnerServiceSMO machineTranslateInnerServiceSMOImpl;

    @Autowired
    private ISendIot sendIotImpl;

    @Override
    public void execute(Business business, List<Business> businesses) {
        String iotSwitch = MappingCache.getValue("IOT", "IOT_SWITCH");
        if (!"ON".equals(iotSwitch)) {
            return;
        }

        JSONObject data = business.getData();
        String memberId = data.getString("memberId");
        if (StringUtil.isEmpty(memberId)) {
            throw new IllegalArgumentException("车辆不存在");
        }

        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setMemberId(memberId);
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

        if (ListUtil.isNull(ownerCarDtos)) {
            throw new IllegalArgumentException("车辆不存在");
        }

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(ownerCarDtos.get(0).getOwnerId());
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);
        if (ListUtil.isNull(ownerDtos)) {
            throw new IllegalArgumentException("业主不存在");
        }

        /**
         * {
         * “communityId”:”12313”,
         * “ownerId”:”123123”,
         * “name”:”张三”,
         * “link”:”18909711445”,
         * “carMemberId”:”1231”,
         * “carId”:”1231”,
         * “carNum”:”青A88488”,
         * “parkingArea”:{“paId”:”1231”,“paNum”:”A区停车场”},
         * “parkingSpace”:{“psId”:”8888”,“psNum”:”A-001”,“prId”:”6666”},
         * “parkingRegion”:{“prId”:”6666”,“regionCode”:”A01”,“regionName”:”A区一层”},
         * “carTypeCd”:”1001”,
         * “startTime”:”2024-01-01 11:22:22”,
         * “endTime”:”2024-12-01 11:22:22”,
         * “leaseType”:”H”,
         * “contact”:”张三”,
         * “tel”:”18909711445”
         * }
         */

        OwnerCarDto tmpOwnerCarDto = ownerCarDtos.get(0);

        //todo 按 carId 查询绑定车位
        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setCarId(tmpOwnerCarDto.getCarId());
        parkingSpaceDto.setCommunityId(tmpOwnerCarDto.getCommunityId());
        parkingSpaceDto.setStatusCd("0");
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceV1InnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);

        JSONObject car = new JSONObject();
        car.put("communityId", ownerDtos.get(0).getCommunityId());
        car.put("ownerId", ownerDtos.get(0).getMemberId());
        car.put("name", ownerDtos.get(0).getName());
        car.put("link", ownerDtos.get(0).getLink());
        car.put("carMemberId", tmpOwnerCarDto.getMemberId());
        car.put("carId", tmpOwnerCarDto.getCarId());
        car.put("carNum", tmpOwnerCarDto.getCarNum());
        car.put("carTypeCd", tmpOwnerCarDto.getCarTypeCd());
        car.put("startTime", DateUtil.getFormatTimeStringA(tmpOwnerCarDto.getStartTime()));
        car.put("endTime", DateUtil.getFormatTimeStringA(tmpOwnerCarDto.getEndTime()));
        car.put("leaseType", tmpOwnerCarDto.getLeaseType());
        car.put("contact", tmpOwnerCarDto.getContact());
        car.put("tel", tmpOwnerCarDto.getTel());

        //todo 停车场信息（始终携带）
        JSONObject parkingArea = new JSONObject();
        parkingArea.put("paId", tmpOwnerCarDto.getPaId());
        parkingArea.put("paNum", tmpOwnerCarDto.getAreaNum());
        car.put("parkingArea", parkingArea);

        //todo 有绑定车位时携带停车位信息
        if (!ListUtil.isNull(parkingSpaceDtos)) {
            ParkingSpaceDto tmpParkingSpaceDto = parkingSpaceDtos.get(0);
            JSONObject parkingSpace = new JSONObject();
            parkingSpace.put("psId", tmpParkingSpaceDto.getPsId());
            parkingSpace.put("psNum", tmpParkingSpaceDto.getNum());
            if (!StringUtil.isEmpty(tmpParkingSpaceDto.getSectionId())) {
                parkingSpace.put("prId", tmpParkingSpaceDto.getSectionId());
                //todo 有停车区域时携带停车区域信息
                JSONObject parkingRegion = new JSONObject();
                parkingRegion.put("prId", tmpParkingSpaceDto.getSectionId());
                parkingRegion.put("regionCode", tmpParkingSpaceDto.getSectionNum());
                parkingRegion.put("regionName", tmpParkingSpaceDto.getSectionName());
                car.put("parkingRegion", parkingRegion);
            }
            car.put("parkingSpace", parkingSpace);
        }

        ResultVo resultVo = sendIotImpl.post("/iot/api/car.addCarApi", car);
        if (resultVo.getCode() != ResultVo.CODE_OK) {
            saveTranslateLog(ownerDtos.get(0).getCommunityId(), MachineTranslateDto.CMD_ADD_OWNER_CAR,
                    car.getString("carMemberId"), car.getString("carNum"),
                    MachineTranslateDto.STATE_ERROR, resultVo.getMsg());
            return;
        }

        saveTranslateLog(ownerDtos.get(0).getCommunityId(), MachineTranslateDto.CMD_ADD_OWNER_CAR,
                car.getString("carMemberId"), car.getString("carNum"),
                MachineTranslateDto.STATE_SUCCESS, resultVo.getMsg());
    }

    /**
     * 存储交互 记录
     *
     * @param communityId
     */
    public void saveTranslateLog(String communityId, String cmd, String objId, String objName, String state, String remark) {
        MachineTranslateDto machineTranslateDto = new MachineTranslateDto();
        machineTranslateDto.setMachineTranslateId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_machineTranslateId));
        machineTranslateDto.setCommunityId(communityId);
        machineTranslateDto.setMachineCmd(cmd);
        machineTranslateDto.setMachineCode("-1");
        machineTranslateDto.setMachineId("-1");
        machineTranslateDto.setObjId(objId);
        machineTranslateDto.setObjName(objName);
        machineTranslateDto.setTypeCd(MachineTranslateDto.TYPE_OWNER_CAR);
        machineTranslateDto.setRemark(remark);
        machineTranslateDto.setState(state);
        machineTranslateDto.setbId("-1");
        machineTranslateDto.setObjBId("-1");
        machineTranslateDto.setUpdateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        machineTranslateInnerServiceSMOImpl.saveMachineTranslate(machineTranslateDto);
    }
}
