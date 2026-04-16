package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.community.dao.IShopLocationServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("shopLocationServiceDaoImpl")
public class ShopLocationServiceDaoImpl extends BaseServiceDao implements IShopLocationServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ShopLocationServiceDaoImpl.class);

    @Override
    public int saveShopLocationInfo(Map info) throws DAOException {
        logger.debug("保存商铺位置信息 入参 info : {}", info);
        int saveFlag = sqlSessionTemplate.insert("shopLocationServiceDaoImpl.saveShopLocationInfo", info);
        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存商铺位置数据失败：" + JSONObject.toJSONString(info));
        }
        return saveFlag;
    }

    @Override
    public List<Map> queryShopLocations(Map info) throws DAOException {
        logger.debug("查询商铺位置信息 入参 info : {}", info);
        List<Map> shopLocations = sqlSessionTemplate.selectList("shopLocationServiceDaoImpl.queryShopLocations", info);
        return shopLocations;
    }

    @Override
    public int queryShopLocationsCount(Map info) throws DAOException {
        logger.debug("查询商铺位置数量 入参 info : {}", info);
        List<Map> shopLocations = sqlSessionTemplate.selectList("shopLocationServiceDaoImpl.queryShopLocationsCount", info);
        if (shopLocations.size() < 1) {
            return 0;
        }
        return Integer.parseInt(shopLocations.get(0).get("count").toString());
    }

    @Override
    public int updateShopLocationInfo(Map info) throws DAOException {
        logger.debug("修改商铺位置信息 入参 info : {}", info);
        int updateFlag = sqlSessionTemplate.update("shopLocationServiceDaoImpl.updateShopLocationInfo", info);
        if (updateFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改商铺位置数据失败：" + JSONObject.toJSONString(info));
        }
        return updateFlag;
    }
}
