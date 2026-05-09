package com.java110.common.bmo.mall.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.mall.IMallCommonApiBmo;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.file.FileDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairSettingDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.IRepairPoolV1InnerServiceSMO;
import com.java110.intf.community.IRepairSettingV1InnerServiceSMO;
import com.java110.intf.community.IRepairUserV1InnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.user.INotepadV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.file.FileRelPo;
import com.java110.po.notepad.NotepadPo;
import com.java110.po.owner.RepairPoolPo;
import com.java110.po.owner.RepairUserPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.FeeTypeConstant;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("saveOwnerRepairImpl")
public class SaveOwnerRepairImpl implements IMallCommonApiBmo {
    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IRepairPoolV1InnerServiceSMO repairPoolV1InnerServiceSMOImpl;

    @Autowired
    private IRepairUserV1InnerServiceSMO repairUserV1InnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private INotepadV1InnerServiceSMO notepadV1InnerServiceSMOImpl;

    @Autowired
    private IRepairSettingV1InnerServiceSMO repairSettingV1InnerServiceSMOImpl;


    //键(报修业主未处理费用条数)
    public static final String REPAIR_FEE_NUMBER = "REPAIR_FEE_NUMBER";
    @Override
    public void validate(ICmdDataFlowContext context, JSONObject reqJson) {
        // 验证必填参数
        Assert.hasKeyAndValue(reqJson, "repairType", "必填，请选择报修类型");
        Assert.hasKeyAndValue(reqJson, "repairName", "必填，请填写报修人名称");
        Assert.hasKeyAndValue(reqJson, "tel", "必填，请填写报修人手机号");
        Assert.hasKeyAndValue(reqJson, "repairObjType", "必填，请填写报修对象类型");
        Assert.hasKeyAndValue(reqJson, "repairObjId", "必填，请填写报修对象ID");
        Assert.hasKeyAndValue(reqJson, "repairObjName", "必填，请填写报修对象名称");
        Assert.hasKeyAndValue(reqJson, "appointmentTime", "必填，请填写预约时间");
        Assert.hasKeyAndValue(reqJson, "context", "必填，请填写报修内容");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区ID");

        // 获取并验证用户信息
        String userId = CmdContextUtils.getUserId(context);
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        userDto.setPage(1);
        userDto.setRow(1);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        Assert.listOnlyOne(userDtos, "未查询到用户");
        // 将用户信息添加到请求参数中
        reqJson.put("userId", userDtos.get(0).getUserId());
        reqJson.put("userName", userDtos.get(0).getName());

        // 查询默认费用项配置
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(reqJson.getString("communityId"));
        feeConfigDto.setFeeTypeCd(FeeTypeConstant.FEE_TYPE_REPAIR); // 设置费用类型为报修费
        feeConfigDto.setIsDefault(FeeConfigDto.DEFAULT_FEE_CONFIG); // 查询默认配置
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        if (ListUtil.isNull(feeConfigDtos)) {
            return; // 没有配置默认费用项，直接返回
        }

        // 查询处理中的报修费用
        FeeDto feeDto = new FeeDto();
        feeDto.setConfigId(feeConfigDtos.get(0).getConfigId()); // 使用第一个默认配置
        feeDto.setPayerObjId(reqJson.getString("repairObjId")); // 设置缴费对象ID
        feeDto.setState(FeeDto.STATE_DOING); // 查询处理中的费用
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
        if (ListUtil.isNull(feeDtos)) {
            return; // 没有处理中的费用，允许报修
        }

        // 检查未处理费用条数限制
        String repairFeeNumber = MappingCache.getValue(MappingConstant.REPAIR_DOMAIN, REPAIR_FEE_NUMBER);
        if (!StringUtil.isInteger(repairFeeNumber)) {
            return; // 配置值不是数字，不进行限制
        }
        // 如果未处理费用超过配置的限制数量，抛出异常
        if (feeDtos.size() >= Integer.parseInt(repairFeeNumber)) {
            throw new CmdException("该房屋存在" + Integer.parseInt(repairFeeNumber) + "条未处理的费用，请缴费后再进行报修！");
        }
    }

    @Override
    @Java110Transactional
    public void doCmd(ICmdDataFlowContext context, JSONObject reqJson) {
// 查询报修设置信息
        RepairSettingDto repairSettingDto = new RepairSettingDto();
        repairSettingDto.setCommunityId(reqJson.getString("communityId"));
        repairSettingDto.setRepairType(reqJson.getString("repairType"));
        List<RepairSettingDto> repairSettingDtos = repairSettingV1InnerServiceSMOImpl.queryRepairSettings(repairSettingDto);
        if (ListUtil.isNull(repairSettingDtos)) {
            throw new CmdException("报销刘类型不存在");
        }

        // 创建报修池记录
        RepairPoolPo repairPoolPo = BeanConvertUtil.covertBean(reqJson, RepairPoolPo.class);
        repairPoolPo.setRepairId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_repairId));
        repairPoolPo.setState(RepairDto.STATE_WAIT); // 设置初始状态为等待处理

        // 计算超时时间
        String appointmentTime = repairPoolPo.getAppointmentTime();
        Date sAppTime = DateUtil.getDateFromStringA(appointmentTime);
        String timeout = DateUtil.getAddHoursStringA(sAppTime, repairSettingDtos.get(0).getDoTime());
        repairPoolPo.setTimeout(timeout);

        // 保存报修池记录
        int flag = repairPoolV1InnerServiceSMOImpl.saveRepairPoolNew(repairPoolPo);
        if (flag < 1) {
            throw new CmdException("修改失败");
        }

        // 创建报修用户记录
        RepairUserPo repairUserPo = BeanConvertUtil.covertBean(reqJson, RepairUserPo.class);
        repairUserPo.setContext("订单提交");
        repairUserPo.setPreStaffId("-1"); // 设置前一个处理人员ID为默认值
        repairUserPo.setPreStaffName("-1");
        repairUserPo.setPreRuId("-1");
        repairUserPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A)); // 设置开始时间为当前时间
        repairUserPo.setRepairEvent(RepairUserDto.REPAIR_EVENT_START_USER); // 设置报修事件类型
        repairUserPo.setStaffId(reqJson.getString("userId")); // 设置当前处理人员
        repairUserPo.setStaffName(reqJson.getString("userName"));
        repairUserPo.setRepairId(repairPoolPo.getRepairId()); // 关联报修ID
        repairUserPo.setState(RepairUserDto.STATE_SUBMIT); // 设置状态为已提交
        repairUserPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A)); // 设置结束时间为当前时间
        repairUserPo.setRuId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ruId));

        // 保存报修用户记录
        flag = repairUserV1InnerServiceSMOImpl.saveRepairUserNew(repairUserPo);
        if (flag < 1) {
            throw new CmdException("修改用户失败");
        }

        // 保存报修图片
        saveRepairPhoto(reqJson, repairPoolPo.getRepairId());

        // 如果存在记事本ID，更新记事本关联
        if (StringUtil.jsonHasKayAndValue(reqJson, "noteId")) {
            NotepadPo notepadPo = new NotepadPo();
            notepadPo.setNoteId(reqJson.getString("noteId"));
            notepadPo.setThridId(repairPoolPo.getRepairId()); // 关联报修ID
            flag = notepadV1InnerServiceSMOImpl.updateNotepad(notepadPo);
            if (flag < 1) {
                throw new CmdException("修改业主反馈失败");
            }
        }
    }

    /**
     * 保存报修图片
     * 处理报修相关的图片上传和关联关系
     * @param reqJson 请求JSON对象，包含图片信息
     * @param repairId 报修ID，用于关联图片
     */
    private void saveRepairPhoto(JSONObject reqJson, String repairId) {
        int flag;
        JSONArray photos = reqJson.getJSONArray("photos");
        if (ListUtil.isNull(photos)) {
            return; // 没有图片，直接返回
        }

        // 遍历所有图片
        for (int _photoIndex = 0; _photoIndex < photos.size(); _photoIndex++) {
            String _photo = photos.getString(_photoIndex);

            // 如果图片内容过长（base64编码），先保存到文件系统
            if (_photo.length() > 512) {
                FileDto fileDto = new FileDto();
                fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
                fileDto.setFileName(fileDto.getFileId());
                fileDto.setContext(_photo); // 图片内容
                fileDto.setSuffix("jpeg"); // 设置文件后缀
                fileDto.setCommunityId(reqJson.getString("communityId"));
                _photo = fileInnerServiceSMOImpl.saveFile(fileDto); // 保存文件并返回文件路径
            }

            // 创建文件关联记录
            JSONObject businessUnit = new JSONObject();
            businessUnit.put("fileRelId", GenerateCodeFactory.getGeneratorId("12"));
            businessUnit.put("relTypeCd", FileRelDto.REL_TYPE_CD_REPAIR); // 设置关联类型为报修
            businessUnit.put("saveWay", "ftp"); // 设置保存方式
            businessUnit.put("objId", repairId); // 关联报修ID
            businessUnit.put("fileRealName", _photo.toString());
            businessUnit.put("fileSaveName", _photo.toString());

            FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);
            flag = fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);
            if (flag < 1) {
                throw new CmdException("保存图片失败");
            }
        }
    }
}
