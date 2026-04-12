package com.java110.intf.community;

import com.java110.dto.shop.ShopLocationDto;
import com.java110.vo.ResultVo;

import java.util.List;

public interface IShopLocationInnerServiceSMO {

    ResultVo listShopLocations(ShopLocationDto shopLocationDto);

    List<ShopLocationDto> getShopLocationChildren(String parentId, String communityId);

    int queryShopLocationsCount(ShopLocationDto shopLocationDto);

    List<ShopLocationDto> queryShopLocations(ShopLocationDto shopLocationDto);

    List<ShopLocationDto> getShopLocationTree(String communityId);
}
