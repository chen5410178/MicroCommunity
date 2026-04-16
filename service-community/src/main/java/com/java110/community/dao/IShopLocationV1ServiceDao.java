package com.java110.community.dao;

import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

public interface IShopLocationV1ServiceDao {

    int saveShopLocationInfo(Map info) throws DAOException;

    List<Map> getShopLocationInfo(Map info) throws DAOException;

    int updateShopLocationInfo(Map info) throws DAOException;

    int queryShopLocationsCount(Map info);
}
