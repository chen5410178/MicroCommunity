package com.java110.community.cmd.shop;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.community.IBuildingShopOwnerInnerServiceSMO;
import com.java110.intf.community.IBuildingShopV1InnerServiceSMO;
import com.java110.po.shop.BuildingShopOwnerPo;
import com.java110.po.shop.BuildingShopPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Java110Cmd(serviceCode = "shop.saveBuildingShopOwner")
public class SaveBuildingShopOwnerCmd extends Cmd {

    @Autowired
    private IBuildingShopOwnerInnerServiceSMO buildingShopOwnerInnerServiceSMOImpl;

    @Autowired
    private IBuildingShopV1InnerServiceSMO buildingShopV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "shopId", "shopId不能为空");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "communityId不能为空");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        String shopOwnerId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_shopId);
        JSONObject businessBuildingShopOwner = new JSONObject();
        businessBuildingShopOwner.put("shopOwnerId", shopOwnerId);
        businessBuildingShopOwner.put("shopId", reqJson.getString("shopId"));
        businessBuildingShopOwner.put("ownerId", reqJson.getString("ownerId"));
        businessBuildingShopOwner.put("ownerName", reqJson.getString("ownerName"));
        businessBuildingShopOwner.put("link", reqJson.getString("link"));
        businessBuildingShopOwner.put("state", reqJson.getString("state"));
        businessBuildingShopOwner.put("startTime", reqJson.getDate("startTime"));
        businessBuildingShopOwner.put("endTime", reqJson.getDate("endTime"));
        businessBuildingShopOwner.put("communityId", reqJson.getString("communityId"));
        BuildingShopOwnerPo buildingShopOwnerPo = BeanConvertUtil.covertBean(businessBuildingShopOwner, BuildingShopOwnerPo.class);
        int flag = buildingShopOwnerInnerServiceSMOImpl.saveBuildingShopOwner(buildingShopOwnerPo);
        if (flag < 1) {
            throw new CmdException("保存商铺租赁失败");
        }

        JSONObject businessBuildingShop = new JSONObject();
        businessBuildingShop.put("shopId", reqJson.getString("shopId"));
        businessBuildingShop.put("state", reqJson.getString("state"));
        BuildingShopPo buildingShopPo = BeanConvertUtil.covertBean(businessBuildingShop, BuildingShopPo.class);
        int updateFlag = buildingShopV1InnerServiceSMOImpl.updateBuildingShop(buildingShopPo);
        if (updateFlag < 1) {
            throw new CmdException("更新商铺状态失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
