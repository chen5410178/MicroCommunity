package com.java110.community.cmd.shop;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.intf.community.IBuildingShopV1InnerServiceSMO;
import com.java110.po.shop.BuildingShopPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Java110Cmd(serviceCode = "shop.updateBuildingShop")
public class UpdateBuildingShopCmd extends Cmd {

    @Autowired
    private IBuildingShopV1InnerServiceSMO buildingShopV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "shopId", "shopId不能为空");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        JSONObject businessBuildingShop = new JSONObject();
        businessBuildingShop.put("shopId", reqJson.getString("shopId"));
        if (reqJson.containsKey("shopNum")) {
            businessBuildingShop.put("shopNum", reqJson.getString("shopNum"));
        }
        if (reqJson.containsKey("locationId")) {
            businessBuildingShop.put("locationId", reqJson.getString("locationId"));
        }
        if (reqJson.containsKey("builtUpArea")) {
            businessBuildingShop.put("builtUpArea", reqJson.getString("builtUpArea"));
        }
        if (reqJson.containsKey("shopArea")) {
            businessBuildingShop.put("shopArea", reqJson.getString("shopArea"));
        }
        if (reqJson.containsKey("remark")) {
            businessBuildingShop.put("remark", reqJson.getString("remark"));
        }
        if (reqJson.containsKey("state")) {
            businessBuildingShop.put("state", reqJson.getString("state"));
        }
        if (reqJson.containsKey("shopRent")) {
            businessBuildingShop.put("shopRent", reqJson.getString("shopRent"));
        }
        BuildingShopPo buildingShopPo = BeanConvertUtil.covertBean(businessBuildingShop, BuildingShopPo.class);
        int flag = buildingShopV1InnerServiceSMOImpl.updateBuildingShop(buildingShopPo);
        if (flag < 1) {
            throw new CmdException("修改商铺失败");
        }
        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
