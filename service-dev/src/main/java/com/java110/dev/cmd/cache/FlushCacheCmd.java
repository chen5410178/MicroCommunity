package com.java110.dev.cmd.cache;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dev.smo.IDevServiceCacheSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 按id刷新缓存
 */
@Java110Cmd(serviceCode = "cache.flushCache")
public class FlushCacheCmd extends Cmd {

    @Autowired
    private IDevServiceCacheSMO devServiceCacheSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "id", "必填，请填写缓存ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        devServiceCacheSMOImpl.flushCacheById(reqJson.getString("id"));
        context.setResponseEntity(ResultVo.success());
    }
}
