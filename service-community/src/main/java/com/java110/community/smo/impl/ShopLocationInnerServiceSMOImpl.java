package com.java110.community.smo.impl;

import com.java110.community.dao.IShopLocationServiceDao;
import com.java110.dto.shop.ShopLocationDto;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.community.IShopLocationInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ShopLocationInnerServiceSMOImpl extends BaseServiceSMO implements IShopLocationInnerServiceSMO {

    @Autowired
    private IShopLocationServiceDao shopLocationServiceDaoImpl;

    @Override
    public ResultVo listShopLocations(@RequestBody ShopLocationDto shopLocationDto) {
        List<ShopLocationDto> shopLocations = queryShopLocations(shopLocationDto);
        int total = queryShopLocationsCount(shopLocationDto);
        int records = (int) Math.ceil((double) total / shopLocationDto.getRow());
        ResultVo resultVo = new ResultVo(records, total, shopLocations);
        return resultVo;
    }

    @Override
    public List<ShopLocationDto> getShopLocationChildren(String parentId, String communityId) {
        ShopLocationDto shopLocationDto = new ShopLocationDto();
        shopLocationDto.setParentId(parentId);
        shopLocationDto.setCommunityId(communityId);
        return queryShopLocations(shopLocationDto);
    }

    @Override
    public int queryShopLocationsCount(@RequestBody ShopLocationDto shopLocationDto) {
        return shopLocationServiceDaoImpl.queryShopLocationsCount(BeanConvertUtil.beanCovertMap(shopLocationDto));
    }

    @Override
    public List<ShopLocationDto> queryShopLocations(@RequestBody ShopLocationDto shopLocationDto) {
        int page = shopLocationDto.getPage();
        if (page != com.java110.dto.PageDto.DEFAULT_PAGE) {
            shopLocationDto.setPage((page - 1) * shopLocationDto.getRow());
        }
        List<ShopLocationDto> shopLocations = BeanConvertUtil.covertBeanList(
                shopLocationServiceDaoImpl.queryShopLocations(BeanConvertUtil.beanCovertMap(shopLocationDto)),
                ShopLocationDto.class);
        return shopLocations;
    }

    public List<ShopLocationDto> getShopLocationTree(String communityId) {
        ShopLocationDto shopLocationDto = new ShopLocationDto();
        shopLocationDto.setCommunityId(communityId);
        shopLocationDto.setParentId("-1");
        shopLocationDto.setPage(-1);
        List<ShopLocationDto> topLocations = queryShopLocations(shopLocationDto);
        return buildTree(topLocations, communityId);
    }

    private List<ShopLocationDto> buildTree(List<ShopLocationDto> parentNodes, String communityId) {
        if (parentNodes == null || parentNodes.isEmpty()) {
            return new ArrayList<>();
        }
        for (ShopLocationDto node : parentNodes) {
            List<ShopLocationDto> children = getShopLocationChildren(node.getLocationId(), communityId);
            node.setChildren(buildTree(children, communityId));
        }
        return parentNodes;
    }

    public IShopLocationServiceDao getShopLocationServiceDaoImpl() {
        return shopLocationServiceDaoImpl;
    }

    public void setShopLocationServiceDaoImpl(IShopLocationServiceDao shopLocationServiceDaoImpl) {
        this.shopLocationServiceDaoImpl = shopLocationServiceDaoImpl;
    }
}
