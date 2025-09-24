package com.java110.fee.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.fee.dao.IInitializePayFeeServiceDao;
import com.java110.intf.fee.IInitializePayFeeInnerServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 缴费审核内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class InitializePayFeeInnerServiceSMOImpl extends BaseServiceSMO implements IInitializePayFeeInnerServiceSMO {

    @Autowired
    private IInitializePayFeeServiceDao initializePayFeeServiceDaoImpl;


    @Override
    public int deletePayFee(@RequestBody Map communityId) {

        //todo 删除缴费明细
        initializePayFeeServiceDaoImpl.deletePayFeeDetail(communityId);

        int deleteFee = initializePayFeeServiceDaoImpl.deletePayFee(communityId);

        //todo 删除 欠费表
        initializePayFeeServiceDaoImpl.deleteReportOweFee(communityId);

        //todo 删除 月报表
        initializePayFeeServiceDaoImpl.deletePayFeeDetailMonth(communityId);

        // todo 删除 账单规则
        initializePayFeeServiceDaoImpl.deletePayFeeRule(communityId);

        initializePayFeeServiceDaoImpl.deleteImportFee(communityId);

        initializePayFeeServiceDaoImpl.deleteImportFeeDetail(communityId);


        return deleteFee;
    }

    @Override
    public int deleteConfigsFeeDetail(@RequestBody List<String> configIds) {

        Map info = new HashMap();
        info.put("configIds", configIds.toArray(new String[configIds.size()]));

        return initializePayFeeServiceDaoImpl.deleteConfigsFeeDetail(info);


    }

    @Override
    public int deleteConfigsFeeReceipt(@RequestBody List<String> configIds) {
        Map info = new HashMap();
        info.put("configIds", configIds.toArray(new String[configIds.size()]));

        return initializePayFeeServiceDaoImpl.deleteConfigsFeeReceipt(info);

    }

    @Override
    public int deleteConfigsMeterWater(@RequestBody List<String> configIds) {
        Map info = new HashMap();
        info.put("configIds", configIds.toArray(new String[configIds.size()]));

        return initializePayFeeServiceDaoImpl.deleteConfigsMeterWater(info);
    }

    @Override
    public int deleteConfigsFeeDetailMonth(@RequestBody List<String> configIds) {
        Map info = new HashMap();
        info.put("configIds", configIds.toArray(new String[configIds.size()]));

        return initializePayFeeServiceDaoImpl.deleteConfigsFeeDetailMonth(info);
    }

    @Override
    public int deleteConfigsFee(@RequestBody List<String> configIds) {
        Map info = new HashMap();
        info.put("configIds", configIds.toArray(new String[configIds.size()]));

        return initializePayFeeServiceDaoImpl.deleteConfigsFee(info);
    }

    @Override
    public int deleteConfigsFeeRules(@RequestBody List<String> configIds) {
        Map info = new HashMap();
        info.put("configIds", configIds.toArray(new String[configIds.size()]));

        return initializePayFeeServiceDaoImpl.deleteConfigsFeeRules(info);
    }

    @Override
    public int deleteAssetImportLog(@RequestBody String communityId) {
        Map info = new HashMap();
        info.put("communityId", communityId);

        return initializePayFeeServiceDaoImpl.deleteAssetImportLog(info);
    }

    @Override
    public int deleteAssetImportLogDetail(@RequestBody String communityId) {
        Map info = new HashMap();
        info.put("communityId", communityId);

        return initializePayFeeServiceDaoImpl.deleteAssetImportLogDetail(info);
    }

    @Override
    public int deleteImportFee(@RequestBody String communityId) {
        Map info = new HashMap();
        info.put("communityId", communityId);
        return initializePayFeeServiceDaoImpl.deleteImportFee(info);
    }

    @Override
    public int deleteImportFeeDetail(@RequestBody String communityId) {
        Map info = new HashMap();
        info.put("communityId", communityId);
        return initializePayFeeServiceDaoImpl.deleteImportFeeDetail(info);
    }

    @Override
    public int deletePayFeeOrder(@RequestBody String communityId) {
        Map info = new HashMap();
        info.put("communityId", communityId);
        return initializePayFeeServiceDaoImpl.deletePayFeeOrder(info);
    }

    public IInitializePayFeeServiceDao getInitializePayFeeServiceDaoImpl() {
        return initializePayFeeServiceDaoImpl;
    }

    public void setInitializePayFeeServiceDaoImpl(IInitializePayFeeServiceDao initializePayFeeServiceDaoImpl) {
        this.initializePayFeeServiceDaoImpl = initializePayFeeServiceDaoImpl;
    }
}
