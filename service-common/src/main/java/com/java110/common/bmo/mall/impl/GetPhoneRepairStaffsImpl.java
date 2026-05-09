package com.java110.common.bmo.mall.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.mall.IMallCommonApiBmo;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.intf.community.IRepairUserInnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import com.java110.vo.api.junkRequirement.PhotoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getPhoneRepairStaffsImpl")
public class GetPhoneRepairStaffsImpl implements IMallCommonApiBmo {


    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Override
    public void validate(ICmdDataFlowContext context, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "page", "请求中未包含page信息");
        Assert.jsonObjectHaveKey(reqJson, "row", "请求中未包含row信息");
        Assert.hasKeyAndValue(reqJson, "repairId", "未包含报修单");

    }

    @Override
    public void doCmd(ICmdDataFlowContext context, JSONObject reqJson) {
        UserDto userDto = new UserDto();
        userDto.setTel(reqJson.getString("mallTel"));
        userDto.setLevelCd(UserDto.LEVEL_CD_USER);

        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        // 验证用户登录状态，确保只有一个用户记录
        Assert.listOnlyOne(userDtos, "用户未登录");

        // 构建报修工单查询条件，验证当前用户是否有权限访问该报修单
        RepairDto ownerRepairDto = new RepairDto();
        ownerRepairDto.setRepairId(reqJson.getString("repairId"));
        ownerRepairDto.setTel(userDtos.get(0).getTel());
        int count = repairInnerServiceSMOImpl.queryRepairsCount(ownerRepairDto);
        if (count < 1) {
            throw new CmdException("报修工单不存在");
        }

        // 将请求参数转换为维修人员查询条件
        RepairUserDto repairUserDto = BeanConvertUtil.covertBean(reqJson, RepairUserDto.class);
        count = repairUserInnerServiceSMOImpl.queryRepairUsersCount(repairUserDto);

        List<RepairUserDto> repairUserDtos = null;
        if (count > 0) {
            // 查询维修人员列表
            repairUserDtos = repairUserInnerServiceSMOImpl.queryRepairUsers(repairUserDto);
            // 刷新维修人员信息（计算时长、加载图片等）
            refreshRepairUser(repairUserDtos);
        } else {
            // 如果没有维修人员，返回空列表
            repairUserDtos = new ArrayList<>();
        }

        // 构建响应结果，包含分页信息
        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, repairUserDtos);

        context.setResponseEntity(responseEntity);
    }

    /**
     * 刷新维修人员信息
     *
     * 为维修人员列表中的每个人员：
     * 1. 计算维修时长
     * 2. 加载相关的维修图片
     *
     * @param repairUserDtos 维修人员列表
     */
    private void refreshRepairUser(List<RepairUserDto> repairUserDtos) {
        long duration = 0;
        // 遍历所有维修人员，计算每个人的维修时长
        for (RepairUserDto repairUserDto : repairUserDtos) {
            if (repairUserDto.getEndTime() == null) {
                // 如果维修未结束，计算从开始时间到当前时间的时长
                duration = DateUtil.getCurrentDate().getTime() - repairUserDto.getStartTime().getTime();
            } else {
                // 如果维修已结束，计算从开始时间到结束时间的时长
                duration = repairUserDto.getEndTime().getTime() - repairUserDto.getStartTime().getTime();
            }
            // 将毫秒时长转换为可读的时分格式
            repairUserDto.setDuration(getCostTime(duration));
        }

        // 刷入图片信息
        List<PhotoVo> photoVos = null;  // 用于存储业主上传的维修图片
        PhotoVo photoVo = null;

        // 从配置中获取图片访问域名
        String imgUrl = MappingCache.getValue(MappingConstant.FILE_DOMAIN, "IMG_PATH");

        // 遍历所有维修人员，为每个人加载相关的维修图片
        for (RepairUserDto repairUserDto : repairUserDtos) {
            FileRelDto fileRelDto = new FileRelDto();
            fileRelDto.setObjId(repairUserDto.getRepairId());
            // 查询与当前报修单相关的所有文件
            List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
            photoVos = new ArrayList<>();
            // 遍历文件关系列表，筛选出维修图片
            for (FileRelDto tmpFileRelDto : fileRelDtos) {
                if (tmpFileRelDto.getRelTypeCd().equals("14000")) {  // 关系类型码14000表示维修图片
                    photoVo = new PhotoVo();
                    if (!tmpFileRelDto.getFileRealName().startsWith("http")) {
                        // 如果文件名不是完整的URL，拼接图片访问路径
                        photoVo.setUrl(imgUrl + tmpFileRelDto.getFileRealName());
                    } else {
                        // 如果文件名已经是完整的URL，直接使用
                        photoVo.setUrl(tmpFileRelDto.getFileRealName());
                    }
                    photoVo.setUrl(tmpFileRelDto.getFileRealName());
                    photoVo.setRelTypeCd(tmpFileRelDto.getRelTypeCd());
                    photoVos.add(photoVo);
                } else {
                    // 跳过非维修图片类型的文件
                    continue;
                }
            }
            // 将图片列表设置到维修人员对象中
            repairUserDto.setPhotoVos(photoVos);
        }
    }

    /**
     * 计算耗时并格式化为时分字符串
     *
     * 将毫秒时长转换为"小时:分钟"的格式显示
     *
     * @param time 耗时（毫秒）
     * @return 格式化后的耗时字符串，格式为"HH:MM"
     */
    public String getCostTime(Long time) {
        if (time == null) {
            return "00:00";
        }
        // 计算小时数
        long hours = time / (1000 * 60 * 60);
        // 计算分钟数（减去小时部分）
        long minutes = (time - hours * (1000 * 60 * 60)) / (1000 * 60);
        String diffTime = "";
        if (minutes < 10) {
            // 分钟数小于10时补零
            diffTime = hours + ":0" + minutes;
        } else {
            diffTime = hours + ":" + minutes;
        }
        return diffTime;
    }
}
