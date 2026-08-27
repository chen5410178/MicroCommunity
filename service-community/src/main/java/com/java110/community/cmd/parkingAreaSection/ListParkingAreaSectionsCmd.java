package com.java110.community.cmd.parkingAreaSection;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.dto.parking.ParkingAreaSectionDto;
import com.java110.intf.community.IParkingAreaSectionV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Java110CmdDoc(title = "查询停车区域",
        description = "用于外系统查询停车区域信息功能",
        httpMethod = "get",
        url = "http://{ip}:{port}/app/parkingArea.listParkingAreaSections",
        resource = "communityDoc",
        author = "吴学文",
        serviceCode = "parkingArea.listParkingAreaSections",
        seq = 24
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @Java110ParamDoc(name = "paId", length = 30, remark = "停车场ID"),
        @Java110ParamDoc(name = "page", type = "int", length = 11, remark = "页数"),
        @Java110ParamDoc(name = "row", type = "int", length = 11, remark = "行数"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@Java110ExampleDoc(
        reqBody="http://{ip}:{port}/app/parkingArea.listParkingAreaSections?page=1&row=10&communityId=123123&paId=123",
        resBody="{'code':0,'msg':'成功','data':[{'sectionId':'372026082790440002','sectionNum':'A01','sectionName':'A区'}]}"
)

@Java110Cmd(serviceCode = "parkingArea.listParkingAreaSections")
public class ListParkingAreaSectionsCmd extends Cmd {

    @Autowired
    private IParkingAreaSectionV1InnerServiceSMO parkingAreaSectionV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId节点");
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        ParkingAreaSectionDto parkingAreaSectionDto = BeanConvertUtil.covertBean(reqJson, ParkingAreaSectionDto.class);

        int count = parkingAreaSectionV1InnerServiceSMOImpl.queryParkingAreaSectionsCount(parkingAreaSectionDto);
        List<ParkingAreaSectionDto> parkingAreaSectionDtos = null;

        if (count > 0) {
            parkingAreaSectionDtos = parkingAreaSectionV1InnerServiceSMOImpl.queryParkingAreaSections(parkingAreaSectionDto);
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.createResponseEntity(
                (int) Math.ceil((double) count / (double) reqJson.getInteger("row")),
                count,
                parkingAreaSectionDtos == null ? new java.util.ArrayList<>() : parkingAreaSectionDtos));
    }
}
