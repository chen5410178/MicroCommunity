package com.java110.common.bmo.mall.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.mall.IMallCommonApiBmo;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.dto.repair.RepairSettingDto;
import com.java110.intf.community.IRepairSettingV1InnerServiceSMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getRepairSettingsImpl")
public class GetRepairSettingsImpl  implements IMallCommonApiBmo {

    /**
     * 报修设置服务接口
     */
    @Autowired
    private IRepairSettingV1InnerServiceSMO repairSettingV1InnerServiceSMOImpl;


    @Override
    public void validate(ICmdDataFlowContext context, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "请求中未包含小区ID");
        Assert.jsonObjectHaveKey(reqJson, "page", "请求中未包含page信息");
        Assert.jsonObjectHaveKey(reqJson, "row", "请求中未包含row信息");
    }

    @Override
    public void doCmd(ICmdDataFlowContext context, JSONObject reqJson) {
        RepairSettingDto repairSettingDto = BeanConvertUtil.covertBean(reqJson, RepairSettingDto.class);

        // 查询报修设置记录总数
        int count = repairSettingV1InnerServiceSMOImpl.queryRepairSettingsCount(repairSettingDto);

        List<RepairSettingDto> repairSettingDtos = null;

        // 根据查询结果数量决定返回数据
        if (count > 0) {
            // 查询报修设置详情列表
            repairSettingDtos = repairSettingV1InnerServiceSMOImpl.queryRepairSettings(repairSettingDto);
        } else {
            // 如果没有数据，返回空列表
            repairSettingDtos = new ArrayList<>();
        }

        // 计算总页数：总记录数除以每页记录数，向上取整
        int totalPage = (int) Math.ceil((double) count / (double) reqJson.getInteger("row"));
        // 封装返回结果
        ResultVo resultVo = new ResultVo(totalPage, count, repairSettingDtos);

        // 创建HTTP响应实体
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        // 设置响应结果到上下文
        context.setResponseEntity(responseEntity);
    }
}
