package com.java110.community.cmd.shop;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.shop.BuildingShopOwnerDto;
import com.java110.intf.community.IBuildingShopOwnerInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Java110Cmd(serviceCode = "shop.listBuildingShopOwners")
public class ListBuildingShopOwnersCmd extends Cmd {

    @Autowired
    private IBuildingShopOwnerInnerServiceSMO buildingShopOwnerInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "page", "page不能为空");
        Assert.jsonObjectHaveKey(reqJson, "row", "row不能为空");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "communityId不能为空");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        BuildingShopOwnerDto buildingShopOwnerDto = BeanConvertUtil.covertBean(reqJson, BuildingShopOwnerDto.class);
        int count = buildingShopOwnerInnerServiceSMOImpl.queryBuildingShopOwnersCount(buildingShopOwnerDto);
        List<BuildingShopOwnerDto> buildingShopOwners = null;
        if (count > 0) {
            buildingShopOwners = buildingShopOwnerInnerServiceSMOImpl.queryBuildingShopOwners(buildingShopOwnerDto);
        } else {
            buildingShopOwners = new java.util.ArrayList<>();
        }
        int records = (int) Math.ceil((double) count / buildingShopOwnerDto.getRow());
        ResultVo resultVo = new ResultVo(records, count, buildingShopOwners);
        cmdDataFlowContext.setResponseEntity(ResultVo.createResponseEntity(resultVo));
    }
}
