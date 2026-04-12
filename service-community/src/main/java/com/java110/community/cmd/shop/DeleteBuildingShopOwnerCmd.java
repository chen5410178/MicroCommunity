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
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Java110Cmd(serviceCode = "shop.deleteBuildingShopOwner")
public class DeleteBuildingShopOwnerCmd extends Cmd {

    @Autowired
    private IBuildingShopOwnerInnerServiceSMO buildingShopOwnerInnerServiceSMOImpl;

    @Autowired
    private IBuildingShopV1InnerServiceSMO buildingShopV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        if (!reqJson.containsKey("shopOwnerId")) {
            Assert.jsonObjectHaveKey(reqJson, "shopId", "shopId不能为空");
            Assert.jsonObjectHaveKey(reqJson, "ownerId", "ownerId不能为空");
        }
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        String shopOwnerId = reqJson.getString("shopOwnerId");
        String shopId = reqJson.getString("shopId");
        if (shopOwnerId == null || shopOwnerId.isEmpty()) {
            BuildingShopOwnerDto queryDto = new BuildingShopOwnerDto();
            queryDto.setShopId(shopId);
            queryDto.setOwnerId(reqJson.getString("ownerId"));
            queryDto.setStatusCd("0");
            queryDto.setPage(1);
            queryDto.setRow(1);
            List<BuildingShopOwnerDto> shopOwners = buildingShopOwnerInnerServiceSMOImpl.queryBuildingShopOwners(queryDto);
            if (shopOwners != null && !shopOwners.isEmpty()) {
                shopOwnerId = shopOwners.get(0).getShopOwnerId();
            }
        }

        if (shopOwnerId == null || shopOwnerId.isEmpty()) {
            throw new CmdException("未找到商铺业主记录");
        }

        JSONObject businessBuildingShopOwner = new JSONObject();
        businessBuildingShopOwner.put("shopOwnerId", shopOwnerId);
        BuildingShopOwnerPo buildingShopOwnerPo = BeanConvertUtil.covertBean(businessBuildingShopOwner, BuildingShopOwnerPo.class);
        int flag = buildingShopOwnerInnerServiceSMOImpl.deleteBuildingShopOwner(buildingShopOwnerPo);
        if (flag < 1) {
            throw new CmdException("删除商铺租赁失败");
        }

        if (shopId != null && !shopId.isEmpty()) {
            JSONObject businessBuildingShop = new JSONObject();
            businessBuildingShop.put("shopId", shopId);
            businessBuildingShop.put("state", "2008");
            BuildingShopPo buildingShopPo = BeanConvertUtil.covertBean(businessBuildingShop, BuildingShopPo.class);
            int updateFlag = buildingShopV1InnerServiceSMOImpl.updateBuildingShop(buildingShopPo);
            if (updateFlag < 1) {
                throw new CmdException("更新商铺状态失败");
            }
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
