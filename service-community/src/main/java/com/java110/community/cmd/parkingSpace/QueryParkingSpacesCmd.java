package com.java110.community.cmd.parkingSpace;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.api.ApiParkingSpaceDataVo;
import com.java110.vo.api.ApiParkingSpaceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Java110Cmd(serviceCode = "parkingSpace.queryParkingSpaces")
public class QueryParkingSpacesCmd extends Cmd {

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "page", "请求中未包含page信息");
        Assert.jsonObjectHaveKey(reqJson, "row", "请求中未包含row信息");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求中未包含communityId信息");
        Assert.isInteger(reqJson.getString("page"), "不是有效数字");
        Assert.isInteger(reqJson.getString("row"), "不是有效数字");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        //兼容老参数 carNum：转入普通查询分支，按 carNumLike 模糊过滤（支持与 num/state/ownerNameLike 等条件组合查询）
        if (reqJson.containsKey("carNum") && !StringUtil.isEmpty(reqJson.getString("carNum"))) {
            reqJson.put("carNumLike", reqJson.getString("carNum"));
            reqJson.remove("carNum");
        }
        int row = reqJson.getInteger("row");
        ApiParkingSpaceVo apiParkingSpaceVo = new ApiParkingSpaceVo();
        ParkingSpaceDto parkingSpaceDto = BeanConvertUtil.covertBean(reqJson, ParkingSpaceDto.class);
        //查询总记录数
        int total = parkingSpaceInnerServiceSMOImpl.queryParkingSpacesCount(parkingSpaceDto);
        apiParkingSpaceVo.setTotal(total);
        if (total > 0) {
            List<ParkingSpaceDto> parkingSpaceDtoList = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(BeanConvertUtil.covertBean(reqJson, ParkingSpaceDto.class));
            apiParkingSpaceVo.setParkingSpaces(BeanConvertUtil.covertBeanList(parkingSpaceDtoList, ApiParkingSpaceDataVo.class));
        }
        apiParkingSpaceVo.setRecords((int) Math.ceil((double) total / (double) row));
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiParkingSpaceVo), HttpStatus.OK);
        cmdDataFlowContext.setResponseEntity(responseEntity);
    }

    /**
     * 请求数据处理
     *
     * @param reqJson 请求数据对象
     */
    private void refreshReqJson(JSONObject reqJson) {
        if (!reqJson.containsKey("state")) {
            return;
        }
        if ("SH".equals(reqJson.getString("state"))) {
            reqJson.put("states", new String[]{"S", "H"});
            reqJson.remove("state");
        }
    }
}
