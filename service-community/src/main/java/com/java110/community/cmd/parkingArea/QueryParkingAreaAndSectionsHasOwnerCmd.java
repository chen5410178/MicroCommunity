package com.java110.community.cmd.parkingArea;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.dto.parking.ParkingAreaDto;
import com.java110.dto.parking.ParkingAreaSectionDto;
import com.java110.intf.community.IParkingAreaSectionV1InnerServiceSMO;
import com.java110.intf.community.IParkingAreaV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


@Java110CmdDoc(title = "查询有主车位的停车场及停车区域",
        description = "按小区ID返回旗下存在已出售或已出租车位的停车场及停车区域列表，前端组装两级树",
        httpMethod = "get",
        url = "http://{ip}:{port}/app/parkingArea.queryParkingAreaAndSectionsHasOwner",
        resource = "communityDoc",
        author = "吴学文",
        serviceCode = "parkingArea.queryParkingAreaAndSectionsHasOwner",
        seq = 26
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区ID"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@Java110ExampleDoc(
        reqBody="http://{ip}:{port}/app/parkingArea.queryParkingAreaAndSectionsHasOwner?communityId=123123",
        resBody="{'code':0,'msg':'成功','data':[{'paId':'123','num':'A','sectionId':'','sectionNum':'','sectionName':''},{'paId':'123','num':'A','sectionId':'372026082790440002','sectionNum':'A01','sectionName':'A区'}]}"
)

@Java110Cmd(serviceCode = "parkingArea.queryParkingAreaAndSectionsHasOwner")
public class QueryParkingAreaAndSectionsHasOwnerCmd extends Cmd {

    @Autowired
    private IParkingAreaV1InnerServiceSMO parkingAreaV1InnerServiceSMOImpl;

    @Autowired
    private IParkingAreaSectionV1InnerServiceSMO parkingAreaSectionV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId节点");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        List<JSONObject> data = new ArrayList<>();

        ParkingAreaDto parkingAreaDto = BeanConvertUtil.covertBean(reqJson, ParkingAreaDto.class);
        parkingAreaDto.setHasOwnerSpace("1");
        List<ParkingAreaDto> parkingAreaDtos = parkingAreaV1InnerServiceSMOImpl.queryParkingAreas(parkingAreaDto);

        if (parkingAreaDtos == null || parkingAreaDtos.size() < 1) {
            cmdDataFlowContext.setResponseEntity(ResultVo.createResponseEntity(data));
            return;
        }

        ParkingAreaSectionDto parkingAreaSectionDto = new ParkingAreaSectionDto();
        parkingAreaSectionDto.setCommunityId(reqJson.getString("communityId"));
        parkingAreaSectionDto.setHasOwnerSpace("1");
        List<ParkingAreaSectionDto> parkingAreaSectionDtos = parkingAreaSectionV1InnerServiceSMOImpl.queryParkingAreaSections(parkingAreaSectionDto);

        for (ParkingAreaDto tmpParkingAreaDto : parkingAreaDtos) {
            JSONObject paJson = new JSONObject();
            paJson.put("paId", tmpParkingAreaDto.getPaId());
            paJson.put("num", tmpParkingAreaDto.getNum());
            paJson.put("sectionId", "");
            paJson.put("sectionNum", "");
            paJson.put("sectionName", "");
            data.add(paJson);

            if (parkingAreaSectionDtos == null || parkingAreaSectionDtos.size() < 1) {
                continue;
            }
            for (ParkingAreaSectionDto tmpSectionDto : parkingAreaSectionDtos) {
                if (!tmpParkingAreaDto.getPaId().equals(tmpSectionDto.getPaId())) {
                    continue;
                }
                JSONObject sectionJson = new JSONObject();
                sectionJson.put("paId", tmpParkingAreaDto.getPaId());
                sectionJson.put("num", tmpParkingAreaDto.getNum());
                sectionJson.put("sectionId", tmpSectionDto.getSectionId());
                sectionJson.put("sectionNum", tmpSectionDto.getSectionNum());
                sectionJson.put("sectionName", tmpSectionDto.getSectionName());
                data.add(sectionJson);
            }
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.createResponseEntity(data));
    }
}
