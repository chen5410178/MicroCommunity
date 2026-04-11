package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.community.dao.IBuildingShopOwnerV1ServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("buildingShopOwnerV1ServiceDaoImpl")
public class BuildingShopOwnerV1ServiceDaoImpl extends BaseServiceDao implements IBuildingShopOwnerV1ServiceDao {

    private static Logger logger = LoggerFactory.getLogger(BuildingShopOwnerV1ServiceDaoImpl.class);

    @Override
    public int saveBuildingShopOwnerInfo(Map info) throws DAOException {
        logger.debug("保存商铺租赁信息 入参 info : {}", info);
        int saveFlag = sqlSessionTemplate.insert("buildingShopOwnerServiceDaoImplMapper.saveBuildingShopOwner", info);
        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存商铺租赁数据失败：" + JSONObject.toJSONString(info));
        }
        return saveFlag;
    }

    @Override
    public List<Map> queryBuildingShopOwnerInfo(Map info) throws DAOException {
        logger.debug("查询商铺租赁信息 入参 info : {}", info);
        List<Map> buildingShopOwners = sqlSessionTemplate.selectList("buildingShopOwnerServiceDaoImplMapper.queryBuildingShopOwners", info);
        return buildingShopOwners;
    }

    @Override
    public int updateBuildingShopOwnerInfo(Map info) throws DAOException {
        logger.debug("修改商铺租赁信息 入参 info : {}", info);
        int updateFlag = sqlSessionTemplate.update("buildingShopOwnerServiceDaoImplMapper.updateBuildingShopOwner", info);
        if (updateFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改商铺租赁数据失败：" + JSONObject.toJSONString(info));
        }
        return updateFlag;
    }

    @Override
    public int queryBuildingShopOwnersCount(Map info) {
        logger.debug("查询商铺租赁数量 入参 info : {}", info);
        List<Map> buildingShopOwners = sqlSessionTemplate.selectList("buildingShopOwnerServiceDaoImplMapper.queryBuildingShopOwnersCount", info);
        if (buildingShopOwners.size() < 1) {
            return 0;
        }
        return Integer.parseInt(buildingShopOwners.get(0).get("count").toString());
    }

    @Override
    public int deleteBuildingShopOwnerInfo(Map info) throws DAOException {
        logger.debug("删除商铺租赁信息 入参 info : {}", info);
        int updateFlag = sqlSessionTemplate.update("buildingShopOwnerServiceDaoImplMapper.deleteBuildingShopOwner", info);
        if (updateFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "删除商铺租赁数据失败：" + JSONObject.toJSONString(info));
        }
        return updateFlag;
    }
}
