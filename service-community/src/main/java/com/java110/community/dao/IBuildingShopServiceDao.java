package com.java110.community.dao;

import java.util.List;
import java.util.Map;

public interface IBuildingShopServiceDao {

    int saveBuildingShopInfo(Map info);

    List<Map> queryBuildingShops(Map info);

    int queryBuildingShopsCount(Map info);

    int updateBuildingShopInfo(Map info);
}
