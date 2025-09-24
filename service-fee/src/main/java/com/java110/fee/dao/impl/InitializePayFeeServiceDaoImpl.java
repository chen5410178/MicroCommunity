package com.java110.fee.dao.impl;

import com.java110.core.base.dao.BaseServiceDao;
import com.java110.fee.dao.IInitializePayFeeServiceDao;
import com.java110.utils.exception.DAOException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 缴费审核服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("initializePayFeeServiceDaoImpl")
//@Transactional
public class InitializePayFeeServiceDaoImpl extends BaseServiceDao implements IInitializePayFeeServiceDao {

    private static Logger logger = LoggerFactory.getLogger(InitializePayFeeServiceDaoImpl.class);



    public int deletePayFee(Map info) throws DAOException {
        logger.debug("删除费用信息 入参 info : {}", info);

        int deleteFlag = sqlSessionTemplate.delete("initializePayFeeServiceDaoImpl.deletePayFee", info);

        /*if (deleteFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "building_owner初始化失败：" + JSONObject.toJSONString(info));
        }*/
        return deleteFlag;
    }

    @Override
    public int deletePayFeeDetail(Map info) {
        logger.debug("删除费用信息 入参 info : {}", info);

        int deleteFlag = sqlSessionTemplate.delete("initializePayFeeServiceDaoImpl.deletePayFeeDetail", info);

        /*if (deleteFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "building_owner初始化失败：" + JSONObject.toJSONString(info));
        }*/
        return deleteFlag;
    }

    @Override
    public int deleteReportOweFee(Map info) {
        logger.debug("删除费用信息 入参 info : {}", info);

        int deleteFlag = sqlSessionTemplate.delete("initializePayFeeServiceDaoImpl.deleteReportOweFee", info);

        /*if (deleteFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "building_owner初始化失败：" + JSONObject.toJSONString(info));
        }*/
        return deleteFlag;
    }

    @Override
    public int deletePayFeeDetailMonth(Map info) {
        logger.debug("删除费用信息 入参 info : {}", info);

        int deleteFlag = sqlSessionTemplate.delete("initializePayFeeServiceDaoImpl.deletePayFeeDetailMonth", info);

        /*if (deleteFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "building_owner初始化失败：" + JSONObject.toJSONString(info));
        }*/
        return deleteFlag;
    }

    @Override
    public int deletePayFeeRule(Map info) {
        logger.debug("删除费用信息 入参 info : {}", info);

        int deleteFlag = sqlSessionTemplate.delete("initializePayFeeServiceDaoImpl.deletePayFeeRule", info);

        /*if (deleteFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "building_owner初始化失败：" + JSONObject.toJSONString(info));
        }*/
        return deleteFlag;
    }

    @Override
    public int deleteImportFee(Map info) {
        logger.debug("删除费用信息 入参 info : {}", info);

        int deleteFlag = sqlSessionTemplate.delete("initializePayFeeServiceDaoImpl.deleteImportFee", info);

        /*if (deleteFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "building_owner初始化失败：" + JSONObject.toJSONString(info));
        }*/
        return deleteFlag;
    }

    @Override
    public int deleteImportFeeDetail(Map info) {
        logger.debug("删除费用信息 入参 info : {}", info);

        int deleteFlag = sqlSessionTemplate.delete("initializePayFeeServiceDaoImpl.deleteImportFeeDetail", info);

        /*if (deleteFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "building_owner初始化失败：" + JSONObject.toJSONString(info));
        }*/
        return deleteFlag;
    }

    @Override
    public int deleteConfigsFeeDetail(Map info) {
        logger.debug("删除 deleteConfigsFeeDetail 入参 info : {}", info);

        int deleteFlag = sqlSessionTemplate.delete("initializePayFeeServiceDaoImpl.deleteConfigsFeeDetail", info);

        /*if (deleteFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "building_owner初始化失败：" + JSONObject.toJSONString(info));
        }*/
        return deleteFlag;
    }

    @Override
    public int deleteConfigsFeeReceipt(Map info) {
        logger.debug("删除 deleteConfigsFeeReceipt 入参 info : {}", info);

        int deleteFlag = sqlSessionTemplate.delete("initializePayFeeServiceDaoImpl.deleteConfigsFeeReceipt", info);

        /*if (deleteFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "building_owner初始化失败：" + JSONObject.toJSONString(info));
        }*/
        return deleteFlag;
    }

    @Override
    public int deleteConfigsMeterWater(Map info) {
        logger.debug("删除 deleteConfigsMeterWater 入参 info : {}", info);

        int deleteFlag = sqlSessionTemplate.delete("initializePayFeeServiceDaoImpl.deleteConfigsMeterWater", info);

        /*if (deleteFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "building_owner初始化失败：" + JSONObject.toJSONString(info));
        }*/
        return deleteFlag;
    }

    @Override
    public int deleteConfigsFeeDetailMonth(Map info) {
        logger.debug("删除 deleteConfigsMeterWater 入参 info : {}", info);

        int deleteFlag = sqlSessionTemplate.delete("initializePayFeeServiceDaoImpl.deleteConfigsFeeDetailMonth", info);

        /*if (deleteFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "building_owner初始化失败：" + JSONObject.toJSONString(info));
        }*/
        return deleteFlag;
    }

    @Override
    public int deleteConfigsFee(Map info) {
        logger.debug("删除 deleteConfigsMeterWater 入参 info : {}", info);

        int deleteFlag = sqlSessionTemplate.delete("initializePayFeeServiceDaoImpl.deleteConfigsFee", info);

        /*if (deleteFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "building_owner初始化失败：" + JSONObject.toJSONString(info));
        }*/
        return deleteFlag;
    }

    @Override
    public int deleteConfigsFeeRules(Map info) {
        logger.debug("删除 deleteConfigsMeterWater 入参 info : {}", info);

        int deleteFlag = sqlSessionTemplate.delete("initializePayFeeServiceDaoImpl.deleteConfigsFeeRules", info);

        /*if (deleteFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "building_owner初始化失败：" + JSONObject.toJSONString(info));
        }*/
        return deleteFlag;
    }

    @Override
    public int deleteAssetImportLog(Map info) {
        logger.debug("删除 deleteAssetImportLog 入参 info : {}", info);

        int deleteFlag = sqlSessionTemplate.delete("initializePayFeeServiceDaoImpl.deleteAssetImportLog", info);

        /*if (deleteFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "building_owner初始化失败：" + JSONObject.toJSONString(info));
        }*/
        return deleteFlag;
    }

    @Override
    public int deleteAssetImportLogDetail(Map info) {
        logger.debug("删除 deleteAssetImportLogDetail 入参 info : {}", info);

        int deleteFlag = sqlSessionTemplate.delete("initializePayFeeServiceDaoImpl.deleteAssetImportLogDetail", info);

        /*if (deleteFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "building_owner初始化失败：" + JSONObject.toJSONString(info));
        }*/
        return deleteFlag;
    }

    @Override
    public int deletePayFeeOrder(Map info) {
        logger.debug("删除 deletePayFeeOrder 入参 info : {}", info);

        int deleteFlag = sqlSessionTemplate.delete("initializePayFeeServiceDaoImpl.deletePayFeeOrder", info);

        return deleteFlag;
    }

}
