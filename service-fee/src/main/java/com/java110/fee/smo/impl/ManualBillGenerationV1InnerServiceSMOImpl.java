package com.java110.fee.smo.impl;

import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.fee.ManualBillGenerationDetailDto;
import com.java110.dto.fee.ManualBillGenerationDto;
import com.java110.fee.dao.IManualBillGenerationServiceDao;
import com.java110.intf.fee.IManualBillGenerationV1InnerServiceSMO;
import com.java110.po.fee.ManualBillGenerationDetailPo;
import com.java110.po.fee.ManualBillGenerationPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ManualBillGenerationV1InnerServiceSMOImpl extends BaseServiceSMO implements IManualBillGenerationV1InnerServiceSMO {

    @Autowired
    private IManualBillGenerationServiceDao manualBillGenerationServiceDaoImpl;

    @Override
    public int saveManualBillGeneration(@RequestBody ManualBillGenerationPo manualBillGenerationPo) {
        manualBillGenerationServiceDaoImpl.saveManualBillGenerationInfo(BeanConvertUtil.beanCovertMap(manualBillGenerationPo));
        return 1;
    }

    @Override
    public int updateManualBillGeneration(@RequestBody ManualBillGenerationPo manualBillGenerationPo) {
        manualBillGenerationServiceDaoImpl.updateManualBillGenerationInfo(BeanConvertUtil.beanCovertMap(manualBillGenerationPo));
        return 1;
    }

    @Override
    public int deleteManualBillGeneration(@RequestBody ManualBillGenerationPo manualBillGenerationPo) {
        manualBillGenerationPo.setStatusCd("1");
        manualBillGenerationServiceDaoImpl.updateManualBillGenerationInfo(BeanConvertUtil.beanCovertMap(manualBillGenerationPo));
        return 1;
    }

    @Override
    public int saveManualBillGenerationDetail(@RequestBody ManualBillGenerationDetailPo manualBillGenerationDetailPo) {
        manualBillGenerationServiceDaoImpl.saveManualBillGenerationDetailInfo(BeanConvertUtil.beanCovertMap(manualBillGenerationDetailPo));
        return 1;
    }

    @Override
    public int saveManualBillGenerationDetails(@RequestBody List<ManualBillGenerationDetailPo> manualBillGenerationDetailPos) {
        for (ManualBillGenerationDetailPo po : manualBillGenerationDetailPos) {
            manualBillGenerationServiceDaoImpl.saveManualBillGenerationDetailInfo(BeanConvertUtil.beanCovertMap(po));
        }
        return manualBillGenerationDetailPos.size();
    }

    @Override
    public int updateManualBillGenerationDetail(@RequestBody ManualBillGenerationDetailPo manualBillGenerationDetailPo) {
        manualBillGenerationServiceDaoImpl.updateManualBillGenerationDetailInfo(BeanConvertUtil.beanCovertMap(manualBillGenerationDetailPo));
        return 1;
    }

    @Override
    public List<ManualBillGenerationDto> queryManualBillGenerations(@RequestBody ManualBillGenerationDto manualBillGenerationDto) {
        int page = manualBillGenerationDto.getPage();
        if (page != PageDto.DEFAULT_PAGE) {
            manualBillGenerationDto.setPage((page - 1) * manualBillGenerationDto.getRow());
        }
        return BeanConvertUtil.covertBeanList(
                manualBillGenerationServiceDaoImpl.getManualBillGenerationInfo(BeanConvertUtil.beanCovertMap(manualBillGenerationDto)),
                ManualBillGenerationDto.class);
    }

    @Override
    public int queryManualBillGenerationsCount(@RequestBody ManualBillGenerationDto manualBillGenerationDto) {
        return manualBillGenerationServiceDaoImpl.queryManualBillGenerationsCount(BeanConvertUtil.beanCovertMap(manualBillGenerationDto));
    }

    @Override
    public List<ManualBillGenerationDetailDto> queryManualBillGenerationDetails(@RequestBody ManualBillGenerationDetailDto manualBillGenerationDetailDto) {
        int page = manualBillGenerationDetailDto.getPage();
        if (page != PageDto.DEFAULT_PAGE) {
            manualBillGenerationDetailDto.setPage((page - 1) * manualBillGenerationDetailDto.getRow());
        }
        return BeanConvertUtil.covertBeanList(
                manualBillGenerationServiceDaoImpl.getManualBillGenerationDetailInfo(BeanConvertUtil.beanCovertMap(manualBillGenerationDetailDto)),
                ManualBillGenerationDetailDto.class);
    }

    @Override
    public int queryManualBillGenerationDetailsCount(@RequestBody ManualBillGenerationDetailDto manualBillGenerationDetailDto) {
        return manualBillGenerationServiceDaoImpl.queryManualBillGenerationDetailsCount(BeanConvertUtil.beanCovertMap(manualBillGenerationDetailDto));
    }

    public IManualBillGenerationServiceDao getManualBillGenerationServiceDaoImpl() {
        return manualBillGenerationServiceDaoImpl;
    }

    public void setManualBillGenerationServiceDaoImpl(IManualBillGenerationServiceDao manualBillGenerationServiceDaoImpl) {
        this.manualBillGenerationServiceDaoImpl = manualBillGenerationServiceDaoImpl;
    }
}
