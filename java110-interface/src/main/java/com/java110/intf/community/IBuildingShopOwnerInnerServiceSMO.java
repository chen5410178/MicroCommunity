package com.java110.intf.community;

import com.java110.dto.shop.BuildingShopOwnerDto;
import com.java110.po.shop.BuildingShopOwnerPo;
import com.java110.vo.ResultVo;

import java.util.List;

public interface IBuildingShopOwnerInnerServiceSMO {

    ResultVo listBuildingShopOwners(BuildingShopOwnerDto buildingShopOwnerDto);

    int queryBuildingShopOwnersCount(BuildingShopOwnerDto buildingShopOwnerDto);

    List<BuildingShopOwnerDto> queryBuildingShopOwners(BuildingShopOwnerDto buildingShopOwnerDto);

    int saveBuildingShopOwner(BuildingShopOwnerPo buildingShopOwnerPo);

    int updateBuildingShopOwner(BuildingShopOwnerPo buildingShopOwnerPo);

    int deleteBuildingShopOwner(BuildingShopOwnerPo buildingShopOwnerPo);
}
