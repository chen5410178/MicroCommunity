package com.java110.community.cmd.shop;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.community.IBuildingShopV1InnerServiceSMO;
import com.java110.po.shop.BuildingShopPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@Java110Cmd(serviceCode = "shop.saveBuildingShop")
public class SaveBuildingShopCmd extends Cmd {

    @Autowired
    private IBuildingShopV1InnerServiceSMO buildingShopV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "shopNum", "shopNum不能为空");
        Assert.jsonObjectHaveKey(reqJson, "locationId", "locationId不能为空");
        Assert.jsonObjectHaveKey(reqJson, "locationName", "locationName不能为空");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "communityId不能为空");
        Assert.jsonObjectHaveKey(reqJson, "builtUpArea", "builtUpArea不能为空");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        String shopId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_shopId);
        JSONObject businessBuildingShop = new JSONObject();
        businessBuildingShop.put("shopId", shopId);
        businessBuildingShop.put("shopNum", reqJson.getString("shopNum"));
        businessBuildingShop.put("locationId", reqJson.getString("locationId"));
        businessBuildingShop.put("locationName", reqJson.getString("locationName"));
        businessBuildingShop.put("builtUpArea", reqJson.getString("builtUpArea"));
        businessBuildingShop.put("shopArea", reqJson.getString("shopArea"));
        businessBuildingShop.put("remark", reqJson.getString("remark"));
        businessBuildingShop.put("state", reqJson.getString("state") == null ? "2008" : reqJson.getString("state"));
        businessBuildingShop.put("communityId", reqJson.getString("communityId"));
        businessBuildingShop.put("shopRent", reqJson.getString("shopRent") == null ? "0.00" : reqJson.getString("shopRent"));
        BuildingShopPo buildingShopPo = BeanConvertUtil.covertBean(businessBuildingShop, BuildingShopPo.class);
        int flag = buildingShopV1InnerServiceSMOImpl.saveBuildingShop(buildingShopPo);
        if (flag < 1) {
            throw new CmdException("保存商铺失败");
        }
        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
