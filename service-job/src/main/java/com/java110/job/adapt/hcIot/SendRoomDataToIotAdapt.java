package com.java110.job.adapt.hcIot;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.system.Business;
import com.java110.intf.user.IOwnerRoomRelV1InnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 修改房屋同步房屋信息
 */
@Component(value = "sendRoomDataToIotAdapt")
public class SendRoomDataToIotAdapt extends DatabusAdaptImpl {

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelV1InnerServiceSMO ownerRoomRelV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerDataToIot ownerDataToIotImpl;

    @Override
    public void execute(Business business, List<Business> businesses) {
        String iotSwitch = MappingCache.getValue("IOT", "IOT_SWITCH");
        if (!"ON".equals(iotSwitch)) {
            return;
        }

        JSONObject data = business.getData();
        String roomId = data.getString("roomId");
        if (StringUtil.isEmpty(roomId)) {
            throw new IllegalArgumentException("未包含房屋信息");
        }

        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setRoomId(roomId);
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelV1InnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);
        if(ListUtil.isNull(ownerRoomRelDtos)){
            return;
        }


        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setOwnerId(ownerRoomRelDtos.get(0).getOwnerId());
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);

        if (ListUtil.isNull(ownerDtos)) {
            throw new IllegalArgumentException("业主不存在");
        }
        for(OwnerDto tmpOwnerDto:ownerDtos) {
            ownerDataToIotImpl.sendOwnerData(tmpOwnerDto);
        }
    }
}
