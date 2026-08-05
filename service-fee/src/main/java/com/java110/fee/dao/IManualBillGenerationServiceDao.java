package com.java110.fee.dao;

import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

public interface IManualBillGenerationServiceDao {

    void saveManualBillGenerationInfo(Map info) throws DAOException;

    void saveManualBillGenerationDetailInfo(Map info) throws DAOException;

    void saveManualBillGenerationDetails(List<Map> infos) throws DAOException;

    List<Map> getManualBillGenerationInfo(Map info) throws DAOException;

    List<Map> getManualBillGenerationDetailInfo(Map info) throws DAOException;

    void updateManualBillGenerationInfo(Map info) throws DAOException;

    void updateManualBillGenerationDetailInfo(Map info) throws DAOException;

    int queryManualBillGenerationsCount(Map info);

    int queryManualBillGenerationDetailsCount(Map info);
}
