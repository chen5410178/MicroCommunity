package com.java110.dev.cmd.cache;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dev.smo.IDevServiceCacheSMO;
import com.java110.dto.cache.CacheDto;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询缓存列表
 */
@Java110Cmd(serviceCode = "cache.queryCache")
public class QueryCacheCmd extends Cmd {

    @Autowired
    private IDevServiceCacheSMO devServiceCacheSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        CacheDto cacheDto = BeanConvertUtil.covertBean(reqJson, CacheDto.class);

        int count = devServiceCacheSMOImpl.queryCachesCount(cacheDto);

        List<CacheDto> caches = count > 0 ? devServiceCacheSMOImpl.queryCaches(cacheDto) : new ArrayList<>();

        // 不下发 url，避免暴露内部刷新地址
        caches.forEach(c -> c.setUrl(null));

        context.setResponseEntity(ResultVo.createResponseEntity(
                (int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, caches));
    }
}
