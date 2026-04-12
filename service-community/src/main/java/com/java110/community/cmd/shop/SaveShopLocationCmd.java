package com.java110.community.cmd.shop;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.shop.ShopLocationDto;
import com.java110.intf.community.IShopLocationInnerServiceSMO;
import com.java110.intf.community.IShopLocationV1InnerServiceSMO;
import com.java110.po.shop.ShopLocationPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Java110Cmd(serviceCode = "shop.saveShopLocation")
public class SaveShopLocationCmd extends Cmd {

    @Autowired
    private IShopLocationV1InnerServiceSMO shopLocationV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "locationName", "locationName不能为空");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "communityId不能为空");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        String locationId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_locationId);
        JSONObject businessShopLocation = new JSONObject();
        businessShopLocation.put("locationId", locationId);
        businessShopLocation.put("locationName", reqJson.getString("locationName"));
        businessShopLocation.put("parentId", reqJson.getString("parentId") == null ? "-1" : reqJson.getString("parentId"));
        businessShopLocation.put("level", reqJson.getString("level") == null ? "1" : reqJson.getString("level"));
        businessShopLocation.put("remark", reqJson.getString("remark"));
        businessShopLocation.put("communityId", reqJson.getString("communityId"));
        businessShopLocation.put("seq", reqJson.getString("seq") == null ? "1" : reqJson.getString("seq"));
        ShopLocationPo shopLocationPo = BeanConvertUtil.covertBean(businessShopLocation, ShopLocationPo.class);
        int flag = shopLocationV1InnerServiceSMOImpl.saveShopLocation(shopLocationPo);
        if (flag < 1) {
            throw new CmdException("保存商铺位置失败");
        }
        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
