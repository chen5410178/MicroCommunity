package com.java110.community.cmd.parkingAreaSection;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.dto.parking.ParkingAreaDto;
import com.java110.dto.parking.ParkingAreaSectionDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.intf.community.IParkingAreaV1InnerServiceSMO;
import com.java110.intf.community.IParkingAreaSectionV1InnerServiceSMO;
import com.java110.intf.community.IParkingSpaceV1InnerServiceSMO;
import com.java110.po.parking.ParkingAreaSectionPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;


@Java110CmdDoc(title = "删除停车区域",
        description = "用于外系统删除停车区域信息功能",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/parkingArea.deleteParkingAreaSection",
        resource = "communityDoc",
        author = "吴学文",
        serviceCode = "parkingArea.deleteParkingAreaSection",
        seq = 23
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @Java110ParamDoc(name = "paId", length = 30, remark = "停车场ID"),
        @Java110ParamDoc(name = "sectionId", length = 30, remark = "区域ID"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@Java110ExampleDoc(
        reqBody="{\"communityId\":\"2022081539020475\",\"paId\":\"102022112706900045\",\"sectionId\":\"372026082790440002\"}",
        resBody="{'code':0,'msg':'成功'}"
)

@Java110Cmd(serviceCode = "parkingArea.deleteParkingAreaSection")
public class DeleteParkingAreaSectionCmd extends Cmd {

    @Autowired
    private IParkingAreaV1InnerServiceSMO parkingAreaV1InnerServiceSMOImpl;

    @Autowired
    private IParkingAreaSectionV1InnerServiceSMO parkingAreaSectionV1InnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceV1InnerServiceSMO parkingSpaceV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(reqJson, "paId", "请求报文中未包含paId节点");
        Assert.jsonObjectHaveKey(reqJson, "sectionId", "请求报文中未包含sectionId节点");

        //校验停车场ID和小区是否有对应关系
        ParkingAreaDto parkingAreaDto = new ParkingAreaDto();
        parkingAreaDto.setCommunityId(reqJson.getString("communityId"));
        parkingAreaDto.setPaId(reqJson.getString("paId"));
        int total = parkingAreaV1InnerServiceSMOImpl.queryParkingAreasCount(parkingAreaDto);

        if (total < 1) {
            throw new IllegalArgumentException("传入停车场ID不是该小区的停车场");
        }

        //校验 停车场ID 和区域ID是否有关系
        ParkingAreaSectionDto parkingAreaSectionDto = new ParkingAreaSectionDto();
        parkingAreaSectionDto.setPaId(reqJson.getString("paId"));
        parkingAreaSectionDto.setSectionId(reqJson.getString("sectionId"));
        total = parkingAreaSectionV1InnerServiceSMOImpl.queryParkingAreaSectionsCount(parkingAreaSectionDto);
        if (total < 1) {
            throw new IllegalArgumentException("传入区域不是该停车场的区域");
        }

        //校验区域下是否存在车位
        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setSectionId(reqJson.getString("sectionId"));
        parkingSpaceDto.setCommunityId(reqJson.getString("communityId"));
        int count = parkingSpaceV1InnerServiceSMOImpl.queryParkingSpacesCount(parkingSpaceDto);
        if (count > 0) {
            throw new IllegalArgumentException("区域下存在车位 请先删除车位");
        }
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        JSONObject businessParkingAreaSection = new JSONObject();
        businessParkingAreaSection.put("sectionId", reqJson.getString("sectionId"));
        ParkingAreaSectionPo parkingAreaSectionPo = BeanConvertUtil.covertBean(businessParkingAreaSection, ParkingAreaSectionPo.class);
        int flag = parkingAreaSectionV1InnerServiceSMOImpl.deleteParkingAreaSection(parkingAreaSectionPo);

        if (flag < 1) {
            throw new CmdException("删除停车区域失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
