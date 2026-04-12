package com.java110.community.cmd.shop;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.shop.ShopLocationDto;
import com.java110.intf.community.IShopLocationV1InnerServiceSMO;
import com.java110.po.shop.ShopLocationPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Java110Cmd(serviceCode = "shop.updateShopLocation")
public class UpdateShopLocationCmd extends Cmd {

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
        if (reqJson.containsKey("locationName")) {
            businessShopLocation.put("locationName", reqJson.getString("locationName"));
        }
        if (reqJson.containsKey("parentId")) {
            businessShopLocation.put("parentId", reqJson.getString("parentId"));
        }
        if (reqJson.containsKey("level")) {
            businessShopLocation.put("level", reqJson.getString("level"));
        }
        if (reqJson.containsKey("remark")) {
            businessShopLocation.put("remark", reqJson.getString("remark"));
        }
        if (reqJson.containsKey("seq")) {
            businessShopLocation.put("seq", reqJson.getString("seq"));
        }
        ShopLocationPo shopLocationPo = BeanConvertUtil.covertBean(businessShopLocation, ShopLocationPo.class);
        int flag = shopLocationV1InnerServiceSMOImpl.updateShopLocation(shopLocationPo);
        if (flag < 1) {
            throw new CmdException("修改商铺位置失败");
        }
        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
