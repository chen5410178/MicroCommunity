package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.community.dao.IBuildingShopV1ServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("buildingShopV1ServiceDaoImpl")
public class BuildingShopV1ServiceDaoImpl extends BaseServiceDao implements IBuildingShopV1ServiceDao {

    private static Logger logger = LoggerFactory.getLogger(BuildingShopV1ServiceDaoImpl.class);

    @Override
    public int saveBuildingShopInfo(Map info) throws DAOException {
        logger.debug("保存商铺信息 入参 info : {}", info);
        int saveFlag = sqlSessionTemplate.insert("buildingShopServiceDaoImpl.saveBuildingShopInfo", info);
        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存商铺数据失败：" + JSONObject.toJSONString(info));
        }
        return saveFlag;
    }

    @Override
    public List<Map> getBuildingShopInfo(Map info) throws DAOException {
        logger.debug("查询商铺信息 入参 info : {}", info);
        List<Map> buildingShops = sqlSessionTemplate.selectList("buildingShopServiceDaoImpl.queryBuildingShops", info);
        return buildingShops;
    }

    @Override
    public int updateBuildingShopInfo(Map info) throws DAOException {
        logger.debug("修改商铺信息 入参 info : {}", info);
        int updateFlag = sqlSessionTemplate.update("buildingShopServiceDaoImpl.updateBuildingShopInfo", info);
        if (updateFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改商铺数据失败：" + JSONObject.toJSONString(info));
        }
        return updateFlag;
    }

    @Override
    public int queryBuildingShopsCount(Map info) {
        logger.debug("查询商铺数量 入参 info : {}", info);
        List<Map> buildingShops = sqlSessionTemplate.selectList("buildingShopServiceDaoImpl.queryBuildingShopsCount", info);
        if (buildingShops.size() < 1) {
            return 0;
        }
        return Integer.parseInt(buildingShops.get(0).get("count").toString());
    }
}
