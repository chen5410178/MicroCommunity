package com.java110.common.bmo.mall.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.mall.IMallCommonApiBmo;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.data.DatabusDataDto;
import com.java110.dto.privilege.BasePrivilegeDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.dto.repairEvent.RepairEventDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.community.IRepairEventV1InnerServiceSMO;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.intf.community.IRepairUserInnerServiceSMO;
import com.java110.intf.job.IDataBusInnerServiceSMO;
import com.java110.intf.order.IPrivilegeInnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.repairEvent.RepairEventPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("phoneUrgeRepairImpl")
public class PhoneUrgeRepairImpl implements IMallCommonApiBmo {
    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Autowired
    private IRepairEventV1InnerServiceSMO repairEventV1InnerServiceSMOImpl;

    @Autowired
    private IPrivilegeInnerServiceSMO privilegeInnerServiceSMOImpl;

    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMOImpl;

    @Autowired
    private IDataBusInnerServiceSMO dataBusInnerServiceSMOImpl;
    @Override
    public void validate(ICmdDataFlowContext context, JSONObject reqJson) {
        // 校验请求参数中必须包含工单ID
        Assert.hasKeyAndValue(reqJson, "repairId", "未包含工单ID");
        // 校验请求参数中必须包含小区ID
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
    }

    @Override
    public void doCmd(ICmdDataFlowContext context, JSONObject reqJson) {
// 查询用户信息
        UserDto userDto = new UserDto();
        userDto.setTel(reqJson.getString("mallTel"));
        userDto.setLevelCd(UserDto.LEVEL_CD_USER);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        // 验证用户信息唯一性
        Assert.listOnlyOne(userDtos, "用户未登录");

        // 根据工单ID和用户电话查询报修工单
        RepairDto repairDto = new RepairDto();
        repairDto.setRepairId(reqJson.getString("repairId"));
        repairDto.setTel(userDtos.get(0).getTel());
        List<RepairDto> repairDtos = repairInnerServiceSMOImpl.queryRepairs(repairDto);

        // 验证报修工单存在且唯一
        Assert.listOnlyOne(repairDtos, "报修不存在");

        // 创建催单事件对象
        RepairEventPo repairEventPo = new RepairEventPo();
        // 生成事件ID
        repairEventPo.setEventId(GenerateCodeFactory.getGeneratorId("11"));
        // 设置工单ID
        repairEventPo.setRepairId(repairDtos.get(0).getRepairId());
        // 设置催单备注信息
        repairEventPo.setRemark("用户着急催单了，请尽快处理！");
        // 设置事件类型为催单类型
        repairEventPo.setEventType(RepairEventDto.EVENT_TYPE_URGE);
        // 设置小区ID
        repairEventPo.setCommunityId(repairDtos.get(0).getCommunityId());

        // 查询并设置处理人员信息
        queryStaff(repairEventPo, repairDtos.get(0));

        // 保存催单事件到数据库
        repairEventV1InnerServiceSMOImpl.saveRepairEvent(repairEventPo);

        // 发送数据总线通知，通知相关人员有催单事件
        dataBusInnerServiceSMOImpl.databusData(new DatabusDataDto(DatabusDataDto.BUSINESS_TYPE_REPAIR_URGE, BeanConvertUtil.beanCovertJson(repairEventPo)));
    }

    /**
     * 查询处理人员信息
     * 根据报修工单状态查询对应的处理人员（派单人员或维修师傅）
     *
     * @param repairEventPo 报修事件对象，用于设置处理人员信息
     * @param repairDto 报修工单对象，包含工单状态等信息
     */
    private void queryStaff(RepairEventPo repairEventPo, RepairDto repairDto) {

        // 如果工单状态为待处理状态，查询派单人员
        if (RepairDto.STATE_WAIT.equals(repairDto.getState())) {
            BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
            basePrivilegeDto.setResource("/wechatRepairRegistration");
            basePrivilegeDto.setCommunityId(repairDto.getCommunityId());

            // 查询具有报修登记权限的用户
            List<UserDto> userDtos = privilegeInnerServiceSMOImpl.queryPrivilegeUsers(basePrivilegeDto);

            // 如果没有找到有权限的用户，设置默认值
            if (ListUtil.isNull(userDtos)) {
                repairEventPo.setStaffId("-1");
                repairEventPo.setStaffName("未找到派单人员");
                return;
            }

            // 查询用户详细信息
            UserDto userDto = new UserDto();
            userDto.setUserId(userDtos.get(0).getUserId());
            List<UserDto> tmpUserDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

            // 如果用户详细信息不存在，设置默认值
            if (ListUtil.isNull(tmpUserDtos)) {
                repairEventPo.setStaffId("-1");
                repairEventPo.setStaffName("未找到派单人员");
                return;
            }

            // 设置派单人员信息
            repairEventPo.setStaffId(tmpUserDtos.get(0).getUserId());
            repairEventPo.setStaffName("派单人员-" + tmpUserDtos.get(0).getName());
            return;
        }

        // 如果工单不是待处理状态，查询正在处理的维修师傅
        RepairUserDto repairUserDto = new RepairUserDto();
        repairUserDto.setRepairId(repairDto.getRepairId());
        repairUserDto.setState(RepairUserDto.STATE_DOING);
        List<RepairUserDto> repairUserDtos = repairUserInnerServiceSMOImpl.queryRepairUsers(repairUserDto);

        // 如果没有找到正在处理的维修师傅，设置默认值
        if(ListUtil.isNull(repairUserDtos)){
            repairEventPo.setStaffId("-1");
            repairEventPo.setStaffName("未找到报修师傅");
            return;
        }

        // 设置维修师傅信息
        repairEventPo.setStaffId(repairUserDtos.get(0).getStaffId());
        repairEventPo.setStaffName(repairUserDtos.get(0).getStaffName());
    }
}
