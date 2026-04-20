package com.java110.community.cmd.shop;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.shop.BuildingShopOwnerDto;
import com.java110.intf.community.IBuildingShopOwnerInnerServiceSMO;
import com.java110.intf.community.IBuildingShopV1InnerServiceSMO;
import com.java110.po.shop.BuildingShopOwnerPo;
import com.java110.po.shop.BuildingShopPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Java110Cmd(serviceCode = "shop.deleteBuildingShop")
public class DeleteBuildingShopCmd extends Cmd {

    @Autowired
    private IBuildingShopOwnerInnerServiceSMO buildingShopOwnerInnerServiceSMOImpl;

    @Autowired
    private IBuildingShopV1InnerServiceSMO buildingShopV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "shopId", "shopId不能为空");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "小区编号不能为空");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        String shopId = reqJson.getString("shopId");
        String communityId = reqJson.getString("communityId");

        // 删除商铺业主关联
        BuildingShopOwnerDto buildingShopOwnerDto = new BuildingShopOwnerDto();
        buildingShopOwnerDto.setShopId(shopId);
        buildingShopOwnerDto.setCommunityId(communityId);
        int count = buildingShopOwnerInnerServiceSMOImpl.queryBuildingShopOwnersCount(buildingShopOwnerDto);
        if(count >0) {
            BuildingShopOwnerPo buildingShopOwnerPo = new BuildingShopOwnerPo();
            buildingShopOwnerPo.setShopId(shopId);
            buildingShopOwnerPo.setCommunityId(communityId);
            buildingShopOwnerInnerServiceSMOImpl.deleteBuildingShopOwner(buildingShopOwnerPo);
        }

        // 删除商铺
        BuildingShopPo buildingShopPo = new BuildingShopPo();
        buildingShopPo.setShopId(shopId);
        buildingShopPo.setCommunityId(communityId);
        buildingShopPo.setStatusCd("1");
        int flag = buildingShopV1InnerServiceSMOImpl.deleteBuildingShop(buildingShopPo);
        if (flag < 1) {
            throw new CmdException("删除商铺失败");
        }
        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
