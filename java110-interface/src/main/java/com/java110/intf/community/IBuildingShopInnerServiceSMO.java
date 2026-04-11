package com.java110.intf.community;

import com.java110.dto.shop.BuildingShopDto;
import com.java110.vo.ResultVo;

import java.util.List;

public interface IBuildingShopInnerServiceSMO {

    ResultVo listBuildingShops(BuildingShopDto buildingShopDto);

    int queryBuildingShopsCount(BuildingShopDto buildingShopDto);

    List<BuildingShopDto> queryBuildingShops(BuildingShopDto buildingShopDto);
}
