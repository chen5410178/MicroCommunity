package com.java110.fee.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.fee.dao.IManualBillGenerationServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("manualBillGenerationServiceDaoImpl")
public class ManualBillGenerationServiceDaoImpl extends BaseServiceDao implements IManualBillGenerationServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ManualBillGenerationServiceDaoImpl.class);

    @Override
    public void saveManualBillGenerationInfo(Map info) throws DAOException {
        logger.debug("保存账单手工生成申请信息 入参 info : {}", info);
        int saveFlag = sqlSessionTemplate.insert("manualBillGenerationServiceDaoImpl.saveManualBillGenerationInfo", info);
        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存账单手工生成申请信息失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public void saveManualBillGenerationDetailInfo(Map info) throws DAOException {
        logger.debug("保存账单手工生成明细信息 入参 info : {}", info);
        int saveFlag = sqlSessionTemplate.insert("manualBillGenerationServiceDaoImpl.saveManualBillGenerationDetailInfo", info);
        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存账单手工生成明细信息失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public void saveManualBillGenerationDetails(List<Map> infos) throws DAOException {
        logger.debug("批量保存账单手工生成明细信息 入参 infos : {}", infos);
        for (Map info : infos) {
            saveManualBillGenerationDetailInfo(info);
        }
    }

    @Override
    public List<Map> getManualBillGenerationInfo(Map info) throws DAOException {
        logger.debug("查询账单手工生成申请信息 入参 info : {}", info);
        return sqlSessionTemplate.selectList("manualBillGenerationServiceDaoImpl.getManualBillGenerationInfo", info);
    }

    @Override
    public List<Map> getManualBillGenerationDetailInfo(Map info) throws DAOException {
        logger.debug("查询账单手工生成明细信息 入参 info : {}", info);
        return sqlSessionTemplate.selectList("manualBillGenerationServiceDaoImpl.getManualBillGenerationDetailInfo", info);
    }

    @Override
    public void updateManualBillGenerationInfo(Map info) throws DAOException {
        logger.debug("修改账单手工生成申请信息 入参 info : {}", info);
        int saveFlag = sqlSessionTemplate.update("manualBillGenerationServiceDaoImpl.updateManualBillGenerationInfo", info);
        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改账单手工生成申请信息失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public void updateManualBillGenerationDetailInfo(Map info) throws DAOException {
        logger.debug("修改账单手工生成明细信息 入参 info : {}", info);
        int saveFlag = sqlSessionTemplate.update("manualBillGenerationServiceDaoImpl.updateManualBillGenerationDetailInfo", info);
        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改账单手工生成明细信息失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public int queryManualBillGenerationsCount(Map info) {
        logger.debug("查询账单手工生成申请数量 入参 info : {}", info);
        List<Map> result = sqlSessionTemplate.selectList("manualBillGenerationServiceDaoImpl.queryManualBillGenerationsCount", info);
        if (result.size() < 1) {
            return 0;
        }
        return Integer.parseInt(result.get(0).get("count").toString());
    }

    @Override
    public int queryManualBillGenerationDetailsCount(Map info) {
        logger.debug("查询账单手工生成明细数量 入参 info : {}", info);
        List<Map> result = sqlSessionTemplate.selectList("manualBillGenerationServiceDaoImpl.queryManualBillGenerationDetailsCount", info);
        if (result.size() < 1) {
            return 0;
        }
        return Integer.parseInt(result.get(0).get("count").toString());
    }
}
