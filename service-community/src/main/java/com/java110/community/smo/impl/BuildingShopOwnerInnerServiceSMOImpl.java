package com.java110.community.smo.impl;

import com.java110.community.dao.IBuildingShopOwnerV1ServiceDao;
import com.java110.dto.shop.BuildingShopOwnerDto;
import com.java110.intf.community.IBuildingShopOwnerInnerServiceSMO;
import com.java110.po.shop.BuildingShopOwnerPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BuildingShopOwnerInnerServiceSMOImpl extends BaseServiceSMO implements IBuildingShopOwnerInnerServiceSMO {

    @Autowired
    private IBuildingShopOwnerV1ServiceDao buildingShopOwnerV1ServiceDaoImpl;

    @Override
    public ResultVo listBuildingShopOwners(BuildingShopOwnerDto buildingShopOwnerDto) {
        int count = queryBuildingShopOwnersCount(buildingShopOwnerDto);
        List<BuildingShopOwnerDto> buildingShopOwners = null;
        if (count > 0) {
            buildingShopOwners = queryBuildingShopOwners(buildingShopOwnerDto);
        } else {
            buildingShopOwners = new java.util.ArrayList<>();
        }
        int records = (int) Math.ceil((double) count / buildingShopOwnerDto.getRow());
        return new ResultVo(records, count, buildingShopOwners);
    }

    @Override
    public int saveBuildingShopOwner(@RequestBody BuildingShopOwnerPo buildingShopOwnerPo) {
        int saveFlag = buildingShopOwnerV1ServiceDaoImpl.saveBuildingShopOwnerInfo(BeanConvertUtil.beanCovertMap(buildingShopOwnerPo));
        return saveFlag;
    }

    @Override
    public int updateBuildingShopOwner(@RequestBody BuildingShopOwnerPo buildingShopOwnerPo) {
        int updateFlag = buildingShopOwnerV1ServiceDaoImpl.updateBuildingShopOwnerInfo(BeanConvertUtil.beanCovertMap(buildingShopOwnerPo));
        return updateFlag;
    }

    @Override
    public int deleteBuildingShopOwner(@RequestBody BuildingShopOwnerPo buildingShopOwnerPo) {
        buildingShopOwnerPo.setStatusCd("1");
        int deleteFlag = buildingShopOwnerV1ServiceDaoImpl.updateBuildingShopOwnerInfo(BeanConvertUtil.beanCovertMap(buildingShopOwnerPo));
        return deleteFlag;
    }

    @Override
    public List<BuildingShopOwnerDto> queryBuildingShopOwners(@RequestBody BuildingShopOwnerDto buildingShopOwnerDto) {
        int page = buildingShopOwnerDto.getPage();
        if (page != PageDto.DEFAULT_PAGE) {
            buildingShopOwnerDto.setPage((page - 1) * buildingShopOwnerDto.getRow());
        }
        List<BuildingShopOwnerDto> buildingShopOwners = BeanConvertUtil.covertBeanList(
                buildingShopOwnerV1ServiceDaoImpl.queryBuildingShopOwnerInfo(BeanConvertUtil.beanCovertMap(buildingShopOwnerDto)),
                BuildingShopOwnerDto.class);
        return buildingShopOwners;
    }

    @Override
    public int queryBuildingShopOwnersCount(@RequestBody BuildingShopOwnerDto buildingShopOwnerDto) {
        return buildingShopOwnerV1ServiceDaoImpl.queryBuildingShopOwnersCount(BeanConvertUtil.beanCovertMap(buildingShopOwnerDto));
    }
}
