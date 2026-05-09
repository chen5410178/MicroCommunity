package com.java110.common.bmo.mall.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.mall.IMallCommonApiBmo;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.dto.complaint.ComplaintTypeDto;
import com.java110.dto.complaint.ComplaintTypeUserDto;
import com.java110.intf.store.IComplaintTypeUserV1InnerServiceSMO;
import com.java110.intf.store.IComplaintTypeV1InnerServiceSMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getComplaintTypeImpl")
public class GetComplaintTypeImpl implements IMallCommonApiBmo {
    /**
     * 投诉类型服务接口
     */
    @Autowired
    private IComplaintTypeV1InnerServiceSMO complaintTypeV1InnerServiceSMOImpl;

    /**
     * 投诉类型员工关联服务接口
     */
    @Autowired
    private IComplaintTypeUserV1InnerServiceSMO complaintTypeUserV1InnerServiceSMOImpl;
    @Override
    public void validate(ICmdDataFlowContext context, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "page", "请求中未包含page信息");
        Assert.jsonObjectHaveKey(reqJson, "row", "请求中未包含row信息");
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");

    }

    @Override
    public void doCmd(ICmdDataFlowContext context, JSONObject reqJson) {
        // 将请求参数转换为投诉类型DTO对象
        ComplaintTypeDto complaintTypeDto = BeanConvertUtil.covertBean(reqJson, ComplaintTypeDto.class);

        // 查询投诉类型总数
        int count = complaintTypeV1InnerServiceSMOImpl.queryComplaintTypesCount(complaintTypeDto);

        List<ComplaintTypeDto> complaintTypeDtos = null;

        if (count > 0) {
            // 如果有数据，查询投诉类型列表
            complaintTypeDtos = complaintTypeV1InnerServiceSMOImpl.queryComplaintTypes(complaintTypeDto);
        } else {
            // 如果没有数据，返回空列表
            complaintTypeDtos = new ArrayList<>();
        }

        // 查询每个投诉类型关联的员工信息
        toQueryStaff(complaintTypeDtos);

        // 构建返回结果
        // 计算总页数：总记录数 / 每页记录数，向上取整
        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, complaintTypeDtos);

        // 设置HTTP响应
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    /**
     * 查询投诉类型关联员工信息
     *
     * 根据投诉类型列表查询每个类型关联的员工信息，并将员工信息设置到对应的投诉类型对象中
     *
     * @param complaintTypeDtos 投诉类型列表
     */
    private void toQueryStaff(List<ComplaintTypeDto> complaintTypeDtos) {
        // 如果投诉类型列表为空，直接返回
        if (ListUtil.isNull(complaintTypeDtos)) {
            return;
        }

        // 收集所有投诉类型编码
        List<String> typeCds = new ArrayList<>();
        for (ComplaintTypeDto complaintTypeDto : complaintTypeDtos) {
            typeCds.add(complaintTypeDto.getTypeCd());
        }

        // 构建查询条件
        ComplaintTypeUserDto complaintTypeUserDto = new ComplaintTypeUserDto();
        complaintTypeUserDto.setTypeCds(typeCds.toArray(new String[typeCds.size()]));

        // 批量查询投诉类型员工关联信息
        List<ComplaintTypeUserDto> complaintTypeUserDtos = complaintTypeUserV1InnerServiceSMOImpl.queryComplaintTypeUsers(complaintTypeUserDto);

        // 如果没有关联的员工信息，直接返回
        if (ListUtil.isNull(complaintTypeUserDtos)) {
            return;
        }

        List<ComplaintTypeUserDto> staffs = null;
        // 遍历每个投诉类型，设置对应的员工信息
        for (ComplaintTypeDto complaintTypeDto : complaintTypeDtos) {
            staffs = new ArrayList<>();
            // 筛选当前投诉类型对应的员工信息
            for(ComplaintTypeUserDto complaintTypeUserDto1 : complaintTypeUserDtos){
                if(complaintTypeDto.getTypeCd().equals(complaintTypeUserDto1.getTypeCd())){
                    staffs.add(complaintTypeUserDto1);
                }
            }
            // 将员工信息设置到投诉类型对象中
            complaintTypeDto.setStaffs(staffs);
        }
    }
}
