package com.java110.dto.cache;

import com.java110.dto.PageDto;

import java.io.Serializable;

/**
 * 缓存 DTO（对应 c_cache 表）
 */
public class CacheDto extends PageDto implements Serializable {

    private String id;

    // 缓存编码
    private String cacheCode;

    // 调用服务编码 对应 c_service
    private String serviceCode;

    // 前台显示名称
    private String name;

    // 前台显示名称（与 name 同值，兼容前端 cacheName 字段）
    private String cacheName;

    // 列顺序
    private String seq;

    // 组，缓存属于哪个组
    private String group;

    // 地址
    private String url;

    private String statusCd = "0";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCacheCode() {
        return cacheCode;
    }

    public void setCacheCode(String cacheCode) {
        this.cacheCode = cacheCode;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
}
