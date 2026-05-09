package com.java110.common.bmo.mall.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.mall.IMallCommonApiBmo;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.dto.complaint.ComplaintDto;
import com.java110.dto.complaint.ComplaintTypeUserDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.common.IComplaintUserInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.store.IComplaintTypeUserV1InnerServiceSMO;
import com.java110.intf.store.IComplaintV1InnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import com.java110.vo.api.junkRequirement.PhotoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getUserComplaintsImpl")
public class GetUserComplaintsImpl implements IMallCommonApiBmo {

    @Autowired
    private IComplaintV1InnerServiceSMO complaintV1InnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IComplaintUserInnerServiceSMO complaintUserInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IComplaintTypeUserV1InnerServiceSMO complaintTypeUserV1InnerServiceSMOImpl;
    @Override
    public void validate(ICmdDataFlowContext context, JSONObject reqJson) {
        // 验证小区ID参数是否存在
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区信息");
        // 调用父类的分页信息验证方法
        Assert.hasKeyAndValue(reqJson, "page", "未包含page");
        Assert.hasKeyAndValue(reqJson, "row", "未包含row");
    }

    @Override
    public void doCmd(ICmdDataFlowContext context, JSONObject reqJson) {
        // 从上下文中获取当前用户ID
        // 验证用户信息
        UserDto userDto = new UserDto();
        userDto.setTel(reqJson.getString("mallTel"));
        userDto.setLevelCd(UserDto.LEVEL_CD_USER);

        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        //Assert.listOnlyOne(userDtos, "用户未登录");
        if(ListUtil.isNull(userDtos)){
            throw new CmdException("物业系统用户不存在");
        }
        // 将请求JSON转换为ComplaintDto对象
        ComplaintDto complaintDto = BeanConvertUtil.covertBean(reqJson, ComplaintDto.class);
        complaintDto.setStoreId("");
        // 设置查询条件：只查询当前用户发起的投诉
        complaintDto.setStartUserId(userDtos.get(0).getUserId());

        // 查询投诉总数
        int count = complaintV1InnerServiceSMOImpl.queryComplaintsCount(complaintDto);
        List<ComplaintDto> complaintDtos = null;

        if (count > 0) {
            // 查询投诉列表数据
            complaintDtos = complaintV1InnerServiceSMOImpl.queryComplaints(complaintDto);
            // 刷新投诉相关的图片信息
            refreshPhotos(complaintDtos);
        } else {
            // 如果没有数据，返回空列表
            complaintDtos = new ArrayList<>();
        }

        // 查询投诉类型对应的处理人员信息
        toQueryStaff(complaintDtos);

        // 构建返回结果，包括总页数、总记录数和数据列表
        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, complaintDtos);

        // 创建HTTP响应实体
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        // 设置响应到上下文
        context.setResponseEntity(responseEntity);
    }

    /**
     * 查询投诉处理人员信息
     * 根据投诉类型查询对应的处理人员，并关联到投诉数据中
     *
     * @param complaintDtos 投诉数据列表
     */
    private void toQueryStaff(List<ComplaintDto> complaintDtos) {
        // 如果投诉列表为空，直接返回
        if (ListUtil.isNull(complaintDtos)) {
            return;
        }

        // 收集所有投诉的类型编码
        List<String> typeCds = new ArrayList<>();
        for (ComplaintDto complaintDto : complaintDtos) {
            typeCds.add(complaintDto.getTypeCd());
        }

        // 如果类型编码列表为空，直接返回
        if (ListUtil.isNull(typeCds)) {
            return;
        }

        // 构建查询条件对象
        ComplaintTypeUserDto complaintTypeUserDto = new ComplaintTypeUserDto();
        complaintTypeUserDto.setTypeCds(typeCds.toArray(new String[typeCds.size()]));

        // 查询投诉类型对应的处理人员
        List<ComplaintTypeUserDto> complaintTypeUserDtos = complaintTypeUserV1InnerServiceSMOImpl.queryComplaintTypeUsers(complaintTypeUserDto);

        // 如果没有查询到处理人员，直接返回
        if (ListUtil.isNull(complaintTypeUserDtos)) {
            return;
        }

        List<ComplaintTypeUserDto> staffs = null;
        // 遍历投诉列表，为每个投诉关联对应的处理人员
        for (ComplaintDto complaintDto : complaintDtos) {
            staffs = new ArrayList<>();
            // 如果投诉已完成，则不查询处理人员
            if (ComplaintDto.STATE_FINISH.equals(complaintDto.getState())) {
                continue;
            }
            // 根据投诉类型匹配处理人员
            for (ComplaintTypeUserDto complaintTypeUserDto1 : complaintTypeUserDtos) {
                if (complaintDto.getTypeCd().equals(complaintTypeUserDto1.getTypeCd())) {
                    staffs.add(complaintTypeUserDto1);
                }
            }
            // 将处理人员列表设置到投诉对象中
            complaintDto.setStaffs(staffs);
        }
    }

    /**
     * 刷新投诉图片信息
     * 查询每个投诉关联的图片信息并设置到投诉对象中
     *
     * @param complaints 投诉数据列表
     */
    private void refreshPhotos(List<ComplaintDto> complaints) {
        List<PhotoVo> photoVos = null;
        PhotoVo photoVo = null;
        // 遍历所有投诉记录
        for (ComplaintDto complaintDataVo : complaints) {
            // 构建文件关联查询条件
            FileRelDto fileRelDto = new FileRelDto();
            fileRelDto.setObjId(complaintDataVo.getComplaintId()); // 设置投诉ID作为关联对象ID
            fileRelDto.setRelTypeCd("13000"); // 设置关联类型编码
            // 查询文件关联信息
            List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);

            photoVos = new ArrayList<>();
            // 遍历文件关联信息，构建图片VO列表
            for (FileRelDto tmpFileRelDto : fileRelDtos) {
                photoVo = new PhotoVo();
                photoVo.setUrl(tmpFileRelDto.getFileRealName()); // 设置图片URL
                photoVos.add(photoVo);
            }
            // 将图片列表设置到投诉对象中
            complaintDataVo.setPhotos(photoVos);
        }
    }
}
