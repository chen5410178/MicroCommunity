package com.java110.community.cmd.shop;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.dto.shop.ShopLocationDto;
import com.java110.intf.community.IShopLocationInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Java110Cmd(serviceCode = "shop.listShopLocations")
public class ListShopLocationsCmd extends Cmd {

    @Autowired
    private IShopLocationInnerServiceSMO shopLocationInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "communityId不能为空");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        ShopLocationDto shopLocationDto = BeanConvertUtil.covertBean(reqJson, ShopLocationDto.class);
        shopLocationDto.setPage(-1);
        List<ShopLocationDto> allLocations = shopLocationInnerServiceSMOImpl.queryShopLocations(shopLocationDto);
        List<ShopLocationDto> treeData = buildTree(allLocations);
        int count = treeData.size();
        ResultVo resultVo = new ResultVo(1, count, treeData);
        cmdDataFlowContext.setResponseEntity(ResultVo.createResponseEntity(resultVo));
    }

    private List<ShopLocationDto> buildTree(List<ShopLocationDto> allLocations) {
        if (allLocations == null || allLocations.isEmpty()) {
            return new ArrayList<>();
        }
        Map<String, ShopLocationDto> locationMap = new HashMap<>();
        List<ShopLocationDto> roots = new ArrayList<>();
        
        for (ShopLocationDto location : allLocations) {
            location.setChildren(new ArrayList<>());
            locationMap.put(location.getLocationId(), location);
        }
        
        for (ShopLocationDto location : allLocations) {
            if (location.getParentId() == null || "-1".equals(location.getParentId()) || !locationMap.containsKey(location.getParentId())) {
                roots.add(location);
            } else {
                ShopLocationDto parent = locationMap.get(location.getParentId());
                parent.getChildren().add(location);
            }
        }
        
        return roots;
    }
}
