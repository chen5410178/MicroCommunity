package com.java110.community.cmd.shop;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.intf.community.IShopLocationV1InnerServiceSMO;
import com.java110.po.shop.ShopLocationPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Java110Cmd(serviceCode = "shop.deleteShopLocation")
public class DeleteShopLocationCmd extends Cmd {

    @Autowired
    private IShopLocationV1InnerServiceSMO shopLocationV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "locationId", "locationId不能为空");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        JSONObject businessShopLocation = new JSONObject();
        businessShopLocation.put("locationId", reqJson.getString("locationId"));
        businessShopLocation.put("statusCd", "1");
        ShopLocationPo shopLocationPo = JSONObject.parseObject(businessShopLocation.toJSONString(), ShopLocationPo.class);
        int flag = shopLocationV1InnerServiceSMOImpl.deleteShopLocation(shopLocationPo);
        if (flag < 1) {
            throw new CmdException("删除商铺位置失败");
        }
        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
