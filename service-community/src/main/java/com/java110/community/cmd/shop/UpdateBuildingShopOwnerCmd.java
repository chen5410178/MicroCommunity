package com.java110.community.cmd.shop;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.intf.community.IBuildingShopOwnerInnerServiceSMO;
import com.java110.po.shop.BuildingShopOwnerPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Java110Cmd(serviceCode = "shop.updateBuildingShopOwner")
public class UpdateBuildingShopOwnerCmd extends Cmd {

    @Autowired
    private IBuildingShopOwnerInnerServiceSMO buildingShopOwnerInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "shopOwnerId", "shopOwnerId不能为空");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        JSONObject businessBuildingShopOwner = new JSONObject();
        businessBuildingShopOwner.put("shopOwnerId", reqJson.getString("shopOwnerId"));
        businessBuildingShopOwner.put("shopId", reqJson.getString("shopId"));
        businessBuildingShopOwner.put("ownerId", reqJson.getString("ownerId"));
        businessBuildingShopOwner.put("ownerName", reqJson.getString("ownerName"));
        businessBuildingShopOwner.put("link", reqJson.getString("link"));
        businessBuildingShopOwner.put("state", reqJson.getString("state"));
        businessBuildingShopOwner.put("startTime", reqJson.getDate("startTime"));
        businessBuildingShopOwner.put("endTime", reqJson.getDate("endTime"));
        BuildingShopOwnerPo buildingShopOwnerPo = BeanConvertUtil.covertBean(businessBuildingShopOwner, BuildingShopOwnerPo.class);
        int flag = buildingShopOwnerInnerServiceSMOImpl.updateBuildingShopOwner(buildingShopOwnerPo);
        if (flag < 1) {
            throw new CmdException("修改商铺租赁失败");
        }
        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
