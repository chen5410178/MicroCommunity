package com.java110.community.cmd.shop;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.shop.BuildingShopDto;
import com.java110.intf.community.IBuildingShopInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Java110Cmd(serviceCode = "shop.listBuildingShops")
public class ListBuildingShopsCmd extends Cmd {

    @Autowired
    private IBuildingShopInnerServiceSMO buildingShopInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "page", "page不能为空");
        Assert.jsonObjectHaveKey(reqJson, "row", "row不能为空");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "communityId不能为空");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        BuildingShopDto buildingShopDto = BeanConvertUtil.covertBean(reqJson, BuildingShopDto.class);
        int count = buildingShopInnerServiceSMOImpl.queryBuildingShopsCount(buildingShopDto);
        List<BuildingShopDto> buildingShops = null;
        if (count > 0) {
            buildingShops = buildingShopInnerServiceSMOImpl.queryBuildingShops(buildingShopDto);
        } else {
            buildingShops = new java.util.ArrayList<>();
        }
        int records = (int) Math.ceil((double) count / buildingShopDto.getRow());
        ResultVo resultVo = new ResultVo(records, count, buildingShops);
        cmdDataFlowContext.setResponseEntity(ResultVo.createResponseEntity(resultVo));
    }
}
