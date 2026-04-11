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
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Java110Cmd(serviceCode = "shop.deleteBuildingShop")
public class DeleteBuildingShopCmd extends Cmd {

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
        businessBuildingShop.put("statusCd", "1");
        BuildingShopPo buildingShopPo = JSONObject.parseObject(businessBuildingShop.toJSONString(), BuildingShopPo.class);
        int flag = buildingShopV1InnerServiceSMOImpl.deleteBuildingShop(buildingShopPo);
        if (flag < 1) {
            throw new CmdException("删除商铺失败");
        }
        ResponseEntity<String> responseEntity = new ResponseEntity<>(JSONObject.toJSONString(ResultVo.success()), HttpStatus.OK);
        cmdDataFlowContext.setResponseEntity(responseEntity);
    }
}
