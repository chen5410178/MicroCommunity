package com.java110.community.dao;

import java.util.List;
import java.util.Map;

public interface IShopLocationServiceDao {

    int saveShopLocationInfo(Map info);

    List<Map> queryShopLocations(Map info);

    int queryShopLocationsCount(Map info);

    int updateShopLocationInfo(Map info);
}
