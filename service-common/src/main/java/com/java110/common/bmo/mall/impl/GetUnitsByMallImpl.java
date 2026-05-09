package com.java110.common.bmo.mall.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.mall.IMallCommonApiBmo;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.dto.unit.UnitDto;
import com.java110.intf.community.IUnitInnerServiceSMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("getUnitsByMallImpl")
public class GetUnitsByMallImpl implements IMallCommonApiBmo {

    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;
    @Override
    public void validate(ICmdDataFlowContext context, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求中未包含communityId信息");
    }

    @Override
    public void doCmd(ICmdDataFlowContext context, JSONObject reqJson) {
        // 将请求参数转换为单元数据传输对象
        UnitDto unitDto = BeanConvertUtil.covertBean(reqJson, UnitDto.class);
        // 设置用户ID为空，避免用户信息干扰查询条件
        unitDto.setUserId("");

        // 根据查询条件查询单元列表
        List<UnitDto> unitDtos = unitInnerServiceSMOImpl.queryUnits(unitDto);

        // 构建HTTP响应实体，将API响应对象转换为JSON字符串
        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(unitDtos);
        // 设置响应实体到数据流上下文
        context.setResponseEntity(responseEntity);
    }
}
