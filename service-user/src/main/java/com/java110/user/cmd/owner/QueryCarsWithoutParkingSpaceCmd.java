package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.Java110CmdDoc;
import com.java110.doc.annotation.Java110ExampleDoc;
import com.java110.doc.annotation.Java110ParamDoc;
import com.java110.doc.annotation.Java110ParamsDoc;
import com.java110.doc.annotation.Java110ResponseDoc;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110CmdDoc(title = "查询无车位车辆",
        description = "查询未绑定任何车位的业主车辆，供车位出租/出售/修改车辆时选择车辆使用",
        httpMethod = "get",
        url = "http://{ip}:{port}/app/owner.queryCarsWithoutParkingSpace",
        resource = "userDoc",
        author = "txcode",
        serviceCode = "owner.queryCarsWithoutParkingSpace",
        seq = 13
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "page", type = "int", length = 11, remark = "页数"),
        @Java110ParamDoc(name = "row", type = "int", length = 11, remark = "行数"),
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @Java110ParamDoc(name = "carNumLike", length = 12, remark = "车牌号模糊查询"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @Java110ParamDoc(name = "data", type = "Array", length = -1, defaultValue = "成功", remark = "数据"),
                @Java110ParamDoc(parentNodeName = "data", name = "carId", length = 30, remark = "车辆ID"),
                @Java110ParamDoc(parentNodeName = "data", name = "carNum", length = 12, remark = "车牌号"),
                @Java110ParamDoc(parentNodeName = "data", name = "ownerId", length = 30, remark = "业主ID"),
                @Java110ParamDoc(parentNodeName = "data", name = "ownerName", length = 64, remark = "业主名称"),
                @Java110ParamDoc(parentNodeName = "data", name = "link", length = 11, remark = "业主手机号"),
        }
)

@Java110ExampleDoc(
        reqBody = "http://ip:port/app/owner.queryCarsWithoutParkingSpace?page=1&row=10&communityId=2022121921870161",
        resBody = "{\n" +
                "\t\"data\": [{\n" +
                "\t\t\"carId\": \"772023012589770046\",\n" +
                "\t\t\"carNum\": \"京A88888\",\n" +
                "\t\t\"ownerId\": \"772023012589770046\",\n" +
                "\t\t\"ownerName\": \"张三\",\n" +
                "\t\t\"link\": \"18909718888\"\n" +
                "\t}],\n" +
                "\t\"records\": 1,\n" +
                "\t\"total\": 1\n" +
                "}"
)
@Java110Cmd(serviceCode = "owner.queryCarsWithoutParkingSpace")
public class QueryCarsWithoutParkingSpaceCmd extends Cmd {

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        super.validateProperty(cmdDataFlowContext);
        Assert.jsonObjectHaveKey(reqJson, "page", "请求中未包含page信息");
        Assert.jsonObjectHaveKey(reqJson, "row", "请求中未包含row信息");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求中未包含communityId信息");
        Assert.isInteger(reqJson.getString("page"), "不是有效数字");
        Assert.isInteger(reqJson.getString("row"), "不是有效数字");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        int row = reqJson.getInteger("row");
        OwnerCarDto ownerCarDto = BeanConvertUtil.covertBean(reqJson, OwnerCarDto.class);
        //查询总记录数
        int total = ownerCarInnerServiceSMOImpl.queryCarsWithoutParkingSpaceCount(ownerCarDto);
        List<OwnerCarDto> ownerCarDtoList = null;
        if (total > 0) {
            ownerCarDtoList = ownerCarInnerServiceSMOImpl.queryCarsWithoutParkingSpace(ownerCarDto);
        } else {
            ownerCarDtoList = new ArrayList<>();
        }
        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity((int) Math.ceil((double) total / (double) row), total, ownerCarDtoList);
        cmdDataFlowContext.setResponseEntity(responseEntity);
    }
}
