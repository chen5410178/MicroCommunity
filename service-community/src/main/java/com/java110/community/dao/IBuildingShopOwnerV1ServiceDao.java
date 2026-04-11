package com.java110.community.dao;

import com.java110.utils.exception.DAOException;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Map;

public interface IBuildingShopOwnerV1ServiceDao {

    int saveBuildingShopOwnerInfo(Map info) throws DAOException;

    List<Map> queryBuildingShopOwnerInfo(Map info) throws DAOException;

    int updateBuildingShopOwnerInfo(Map info) throws DAOException;

    int queryBuildingShopOwnersCount(Map info);

    int deleteBuildingShopOwnerInfo(Map info) throws DAOException;
}
