package com.java110.community.smo.impl;

import com.java110.community.dao.IShopLocationV1ServiceDao;
import com.java110.dto.shop.ShopLocationDto;
import com.java110.intf.community.IShopLocationV1InnerServiceSMO;
import com.java110.po.shop.ShopLocationPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ShopLocationV1InnerServiceSMOImpl extends BaseServiceSMO implements IShopLocationV1InnerServiceSMO {

    @Autowired
    private IShopLocationV1ServiceDao shopLocationV1ServiceDaoImpl;

    @Override
    public int saveShopLocation(@RequestBody ShopLocationPo shopLocationPo) {
        int saveFlag = shopLocationV1ServiceDaoImpl.saveShopLocationInfo(BeanConvertUtil.beanCovertMap(shopLocationPo));
        return saveFlag;
    }

    @Override
    public int updateShopLocation(@RequestBody ShopLocationPo shopLocationPo) {
        int updateFlag = shopLocationV1ServiceDaoImpl.updateShopLocationInfo(BeanConvertUtil.beanCovertMap(shopLocationPo));
        return updateFlag;
    }

    @Override
    public int deleteShopLocation(@RequestBody ShopLocationPo shopLocationPo) {
        shopLocationPo.setStatusCd("1");
        int deleteFlag = shopLocationV1ServiceDaoImpl.updateShopLocationInfo(BeanConvertUtil.beanCovertMap(shopLocationPo));
        return deleteFlag;
    }

    @Override
    public List<ShopLocationDto> queryShopLocations(@RequestBody ShopLocationDto shopLocationDto) {
        int page = shopLocationDto.getPage();
        if (page != PageDto.DEFAULT_PAGE) {
            shopLocationDto.setPage((page - 1) * shopLocationDto.getRow());
        }
        List<ShopLocationDto> shopLocations = BeanConvertUtil.covertBeanList(
                shopLocationV1ServiceDaoImpl.getShopLocationInfo(BeanConvertUtil.beanCovertMap(shopLocationDto)),
                ShopLocationDto.class);
        return shopLocations;
    }

    @Override
    public int queryShopLocationsCount(@RequestBody ShopLocationDto shopLocationDto) {
        return shopLocationV1ServiceDaoImpl.queryShopLocationsCount(BeanConvertUtil.beanCovertMap(shopLocationDto));
    }
}
