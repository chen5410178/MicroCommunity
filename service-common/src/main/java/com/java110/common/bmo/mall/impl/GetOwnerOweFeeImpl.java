package com.java110.common.bmo.mall.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.mall.IMallCommonApiBmo;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.fee.IOweFeeInnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserV1InnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("getOwnerOweFeeImpl")
public class GetOwnerOweFeeImpl implements IMallCommonApiBmo {

    /**
     * 业主应用用户V1版本内部服务
     */
    @Autowired
    private IOwnerAppUserV1InnerServiceSMO ownerAppUserV1InnerServiceSMOImpl;

    /**
     * 业主V1版本内部服务
     */
    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    /**
     * 欠费查询业务接口
     */
    @Autowired
    private IOweFeeInnerServiceSMO oweFeeInnerServiceSMOImpl;

    @Override
    public void validate(ICmdDataFlowContext context, JSONObject reqJson) {
// 验证必填参数：小区ID
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
        // 调用父类分页信息验证
        Assert.jsonObjectHaveKey(reqJson, "page", "请求中未包含page信息");
        Assert.jsonObjectHaveKey(reqJson, "row", "请求中未包含row信息");

        // 从上下文中获取当前用户ID
        // 构建业主应用用户查询条件
        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setLink(reqJson.getString("mallTel"));
        ownerAppUserDto.setCommunityId(reqJson.getString("communityId"));

        // 查询业主应用用户信息
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserV1InnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);

        // 验证用户认证状态
        if (ListUtil.isNull(ownerAppUserDtos)
                || OwnerAppUserDto.STATE_AUDIT_ERROR.equals(ownerAppUserDtos.get(0).getState())) {
            throw new CmdException("用户未认证,请先认证");
        }

        // 检查认证审核状态
        if (OwnerAppUserDto.STATE_AUDITING.equals(ownerAppUserDtos.get(0).getState())) {
            ResultVo resultVo = new ResultVo(-101, "认证审核中");
            context.setResponseEntity(ResultVo.createResponseEntity(resultVo));
            return;
        }

        // 构建业主查询条件
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(ownerAppUserDtos.get(0).getMemberId());
        ownerDto.setCommunityId(ownerAppUserDtos.get(0).getCommunityId());
        // 查询业主信息
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);

        // 验证业主是否存在
        if (ListUtil.isNull(ownerDtos)) {
            throw new CmdException("业主不存在");
        }

        // 获取第一个业主信息
        ownerDto = ownerDtos.get(0);
        // 将业主ID设置到请求参数中，供后续处理使用
        reqJson.put("ownerId", ownerDto.getOwnerId());
    }

    @Override
    public void doCmd(ICmdDataFlowContext context, JSONObject reqJson) {
        // 将请求参数转换为FeeDto对象
        FeeDto feeDto = BeanConvertUtil.covertBean(reqJson, FeeDto.class);

        // 处理缴费对象ID参数
        String payObjId = reqJson.getString("payObjId");
        if (!StringUtil.isEmpty(payObjId)) {
            feeDto.setPayerObjId("");
            // 判断是否为多个缴费对象ID（逗号分隔）
            if (payObjId.contains(",")) {
                feeDto.setPayerObjIds(payObjId.split(","));
            } else {
                feeDto.setPayerObjId(payObjId);
            }
        }

        // 处理目标结束时间参数
        String targetEndTime = reqJson.getString("targetEndTime");
        if (!StringUtil.isEmpty(targetEndTime)) {
            // 将日期格式化为当天的最后时刻
            targetEndTime = targetEndTime + " 23:59:59";
            feeDto.setTargetEndTime(targetEndTime);
        }

        // 调用欠费查询服务
        List<FeeDto> feeDtos = oweFeeInnerServiceSMOImpl.queryOwnerOweFee(feeDto);
        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(feeDtos);        // 设置响应结果
        context.setResponseEntity(responseEntity);
    }
}
