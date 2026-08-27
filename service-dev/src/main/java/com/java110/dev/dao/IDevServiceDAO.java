package com.java110.dev.dao;

import com.java110.dto.cache.CacheDto;
import com.java110.dto.privilege.BasePrivilegeDto;
import com.java110.dto.system.BusinessDatabusDto;
import com.java110.dto.system.BusinessTableHisDto;
import com.java110.dto.mapping.Mapping;

import java.util.List;
import java.util.Map;

/**
 * Created by wuxw on 2018/4/14.
 */
public interface IDevServiceDAO {

    /**
     * 查询所有组件
     *
     * @return
     */
    public List<Map> getAppRouteAndServiceInfoAll();

    /**
     * 查询映射表
     *
     * @return
     */
    public List<Mapping> getMappingInfoAll();

    /**
     * 查询映射表
     *
     * @return
     */
    public List<BasePrivilegeDto> getPrivilegeAll();
    /**
     * 查询映射表
     *
     * @return
     */
    public List<BusinessDatabusDto> getDatabusAll();
    /**
     * 查询映射表
     *
     * @return
     */
    public List<BusinessTableHisDto> getBusinessTableHisAll();

    /**
     * 分页查询缓存列表
     *
     * @param info 分页参数 page(row 偏移)/row
     * @return
     */
    public List<CacheDto> getCaches(Map info);

    /**
     * 查询缓存总数
     *
     * @param info
     * @return
     */
    public int getCachesCount(Map info);

    /**
     * 按id查询缓存
     *
     * @param info
     * @return
     */
    public CacheDto getCacheById(Map info);




}
