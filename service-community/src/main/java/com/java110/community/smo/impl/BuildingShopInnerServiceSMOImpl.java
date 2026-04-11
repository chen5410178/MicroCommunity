package com.java110.community.smo.impl;

import com.java110.community.dao.IBuildingShopServiceDao;
import com.java110.dto.shop.BuildingShopDto;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.community.IBuildingShopInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BuildingShopInnerServiceSMOImpl extends BaseServiceSMO implements IBuildingShopInnerServiceSMO {

    @Autowired
    private IBuildingShopServiceDao buildingShopServiceDaoImpl;

    @Override
    public ResultVo listBuildingShops(@RequestBody BuildingShopDto buildingShopDto) {
        List<BuildingShopDto> buildingShops = queryBuildingShops(buildingShopDto);
        int total = queryBuildingShopsCount(buildingShopDto);
        int records = (int) Math.ceil((double) total / buildingShopDto.getRow());
        ResultVo resultVo = new ResultVo(records, total, buildingShops);
        return resultVo;
    }

    @Override
    public int queryBuildingShopsCount(@RequestBody BuildingShopDto buildingShopDto) {
        return buildingShopServiceDaoImpl.queryBuildingShopsCount(BeanConvertUtil.beanCovertMap(buildingShopDto));
    }

    @Override
    public List<BuildingShopDto> queryBuildingShops(@RequestBody BuildingShopDto buildingShopDto) {
        int page = buildingShopDto.getPage();
        if (page != com.java110.dto.PageDto.DEFAULT_PAGE) {
            buildingShopDto.setPage((page - 1) * buildingShopDto.getRow());
        }
        List<BuildingShopDto> buildingShops = BeanConvertUtil.covertBeanList(
                buildingShopServiceDaoImpl.queryBuildingShops(BeanConvertUtil.beanCovertMap(buildingShopDto)),
                BuildingShopDto.class);
        return buildingShops;
    }

    public IBuildingShopServiceDao getBuildingShopServiceDaoImpl() {
        return buildingShopServiceDaoImpl;
    }

    public void setBuildingShopServiceDaoImpl(IBuildingShopServiceDao buildingShopServiceDaoImpl) {
        this.buildingShopServiceDaoImpl = buildingShopServiceDaoImpl;
    }
}
