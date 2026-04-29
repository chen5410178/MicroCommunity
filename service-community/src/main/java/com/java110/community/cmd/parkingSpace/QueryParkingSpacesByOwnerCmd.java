package com.java110.community.cmd.parkingSpace;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserV1InnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.api.ApiParkingSpaceDataVo;
import com.java110.vo.api.ApiParkingSpaceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "parkingSpace.queryParkingSpacesByOwner")
public class QueryParkingSpacesByOwnerCmd extends Cmd{

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IOwnerAppUserV1InnerServiceSMO ownerAppUserV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "communityId", "请求中未包含communityId信息");
        Assert.hasKeyAndValue(reqJson, "ownerId", "请求中未包含ownerId信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        ApiParkingSpaceVo apiParkingSpaceVo = new ApiParkingSpaceVo();


        String userId = CmdContextUtils.getUserId(context);

        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setUserId(userId);
        ownerAppUserDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserV1InnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);

        if (ListUtil.isNull(ownerAppUserDtos)) {
            throw new CmdException("未绑定业主");
        }

        String memberId = "";
        for (OwnerAppUserDto tmpOwnerAppUserDto : ownerAppUserDtos) {
            if ("-1".equals(tmpOwnerAppUserDto.getMemberId())) {
                continue;
            }

            memberId = tmpOwnerAppUserDto.getMemberId();
        }
        if (StringUtil.isEmpty(memberId)) {
            throw new CmdException("未认证房屋");
        }


        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setOwnerId(memberId);
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

        //在没有 停车位的情况下 直接返回空
        ResponseEntity<String> responseEntity = null;
        if (ownerCarDtos == null || ownerCarDtos.size() == 0) {
            responseEntity = new ResponseEntity<String>("{\"parkingSpaces\":[]}", HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return;
        }

        ParkingSpaceDto parkingSpaceDto = null;
        List<ApiParkingSpaceDataVo> apiParkingSpaceDataVos = new ArrayList<>();
        for (OwnerCarDto tmpOwnerCarDto : ownerCarDtos) {

            parkingSpaceDto = new ParkingSpaceDto();
            parkingSpaceDto.setCommunityId(reqJson.getString("communityId"));
            parkingSpaceDto.setPsId(tmpOwnerCarDto.getPsId());
            List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);

            if (parkingSpaceDtos == null || parkingSpaceDtos.size() != 1) {
                //throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "根据psId 查询存在多条停车位信息" + tmpOwnerCarDto.getPsId());
                continue;
            }

            parkingSpaceDto = parkingSpaceDtos.get(0);

            ApiParkingSpaceDataVo apiParkingSpaceDataVo = BeanConvertUtil.covertBean(tmpOwnerCarDto, ApiParkingSpaceDataVo.class);

            apiParkingSpaceDataVo = BeanConvertUtil.covertBean(parkingSpaceDto, apiParkingSpaceDataVo);
            apiParkingSpaceDataVo.setCarNum(tmpOwnerCarDto.getCarNum());
            apiParkingSpaceDataVo.setCarType(tmpOwnerCarDto.getCarType());
            apiParkingSpaceDataVo.setCarTypeName(tmpOwnerCarDto.getCarTypeName());

            apiParkingSpaceDataVos.add(apiParkingSpaceDataVo);

        }

        apiParkingSpaceVo.setParkingSpaces(apiParkingSpaceDataVos);
        responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiParkingSpaceVo), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }
}
