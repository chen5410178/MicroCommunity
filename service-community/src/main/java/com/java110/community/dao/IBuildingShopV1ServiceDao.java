package com.java110.community.dao;

import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

public interface IBuildingShopV1ServiceDao {

    int saveBuildingShopInfo(Map info) throws DAOException;

    List<Map> getBuildingShopInfo(Map info) throws DAOException;

    int updateBuildingShopInfo(Map info) throws DAOException;

    int queryBuildingShopsCount(Map info);
}
