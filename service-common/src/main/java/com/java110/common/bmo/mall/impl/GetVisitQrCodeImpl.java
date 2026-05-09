package com.java110.common.bmo.mall.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.mall.IMallCommonApiBmo;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.dto.IotDataDto;
import com.java110.intf.job.IIotInnerServiceSMO;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("getVisitQrCodeImpl")
public class GetVisitQrCodeImpl implements IMallCommonApiBmo {

    @Autowired
    private IIotInnerServiceSMO iotInnerServiceSMOImpl;

    @Override
    public void validate(ICmdDataFlowContext context, JSONObject reqJson) {
        // 验证请求参数中必须包含小区ID
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
    }

    @Override
    public void doCmd(ICmdDataFlowContext context, JSONObject reqJson) {
        // 创建物联网数据传输对象，指定业务模块和请求参数
        IotDataDto iotDataDto = new IotDataDto("getVisitQrCodeUrlBmoImpl", reqJson);

        // 调用物联网服务获取访客二维码URL结果
        ResultVo resultVo = iotInnerServiceSMOImpl.postIotData(iotDataDto);

        // 将处理结果设置到响应实体中返回给前端
        context.setResponseEntity(ResultVo.createResponseEntity(resultVo));
    }
}
