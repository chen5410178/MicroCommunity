package com.java110.community.dao;

import java.util.List;
import java.util.Map;

public interface IBuildingShopOwnerServiceDao {

    int saveBuildingShopOwnerInfo(Map info);

    List<Map> queryBuildingShopOwners(Map info);

    int queryBuildingShopOwnersCount(Map info);

    int updateBuildingShopOwnerInfo(Map info);

    int deleteBuildingShopOwnerInfo(Map info);
}
