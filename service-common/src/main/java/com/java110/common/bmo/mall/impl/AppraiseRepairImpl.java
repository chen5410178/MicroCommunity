package com.java110.common.bmo.mall.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.mall.IMallCommonApiBmo;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.dto.appraise.AppraiseDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.common.IAppraiseInnerServiceSMO;
import com.java110.intf.community.IRepairPoolV1InnerServiceSMO;
import com.java110.intf.community.IRepairUserV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.appraise.AppraisePo;
import com.java110.po.owner.RepairPoolPo;
import com.java110.po.owner.RepairUserPo;
import com.java110.po.user.UserPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service("appraiseRepairImpl")
public class AppraiseRepairImpl implements IMallCommonApiBmo {

    @Autowired
    private IAppraiseInnerServiceSMO appraiseInnerServiceSMOImpl;

    @Autowired
    private IRepairPoolV1InnerServiceSMO repairPoolV1InnerServiceSMOImpl;

    @Autowired
    private IRepairUserV1InnerServiceSMO repairUserV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Override
    public void validate(ICmdDataFlowContext context, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "objId", "报修ID不能为空");
        Assert.hasKeyAndValue(reqJson, "context", "评价内容不能为空");
    }

    @Override
    @Java110Transactional
    public void doCmd(ICmdDataFlowContext context, JSONObject reqJson) {
        String repairId = reqJson.getString("objId");

        RepairUserDto repairUserDto = new RepairUserDto();
        repairUserDto.setRepairId(repairId);
        repairUserDto.setState(RepairUserDto.STATE_EVALUATE);
        List<RepairUserDto> repairUserDtos = repairUserV1InnerServiceSMOImpl.queryRepairUserNews(repairUserDto);
        Assert.listOnlyOne(repairUserDtos, "该用户没有待评价的报修单");

        RepairUserPo repairUserPo = new RepairUserPo();
        repairUserPo.setRuId(repairUserDtos.get(0).getRuId());
        repairUserPo.setState(RepairUserDto.STATE_FINISH);
        repairUserPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        repairUserPo.setContext(reqJson.getString("context"));
        repairUserPo.setStatusCd("0");
        int flag = repairUserV1InnerServiceSMOImpl.updateRepairUserNew(repairUserPo);
        if (flag < 1) {
            throw new CmdException("更新报修用户状态失败");
        }

        RepairPoolPo repairPoolPo = new RepairPoolPo();
        repairPoolPo.setRepairId(repairId);
        repairPoolPo.setState(RepairDto.STATE_RETURN_VISIT);
        repairPoolPo.setStatusCd("0");
        flag = repairPoolV1InnerServiceSMOImpl.updateRepairPoolNew(repairPoolPo);
        if (flag < 1) {
            throw new CmdException("更新工单状态失败");
        }

        AppraisePo appraisePo = BeanConvertUtil.covertBean(reqJson, AppraisePo.class);
        appraisePo.setParentAppraiseId("-1");
        appraisePo.setObjType(AppraiseDto.OBJ_TYPE_REPAIR);
        appraisePo.setAppraiseType(AppraiseDto.APPRAISE_TYPE_PUBLIC);
        appraiseInnerServiceSMOImpl.saveAppraise(appraisePo);

        RepairUserDto closeUserDto = new RepairUserDto();
        closeUserDto.setRepairId(repairId);
        closeUserDto.setState(RepairUserDto.STATE_CLOSE);
        List<RepairUserDto> closeUsers = repairUserV1InnerServiceSMOImpl.queryRepairUserNews(closeUserDto);
        Assert.listOnlyOne(closeUsers, "查询结单用户错误");

        String staffId = closeUsers.get(0).getStaffId();

        UserDto staffUser = new UserDto();
        staffUser.setUserId(staffId);
        List<UserDto> staffUsers = userV1InnerServiceSMOImpl.queryUsers(staffUser);
        Assert.listOnlyOne(staffUsers, "查询用户信息错误");

        double appraiseScore = 0.0;
        if (!StringUtil.isEmpty(appraisePo.getAppraiseScore())) {
            appraiseScore = Double.parseDouble(appraisePo.getAppraiseScore());
        }
        double doorSpeedScore = 0.0;
        if (!StringUtil.isEmpty(appraisePo.getDoorSpeedScore())) {
            doorSpeedScore = Double.parseDouble(appraisePo.getDoorSpeedScore());
        }
        double repairmanServiceScore = 0.0;
        if (!StringUtil.isEmpty(appraisePo.getRepairmanServiceScore())) {
            repairmanServiceScore = Double.parseDouble(appraisePo.getRepairmanServiceScore());
        }

        double averageNumber = (appraiseScore + doorSpeedScore + repairmanServiceScore) / 3.0;
        double average;
        if (StringUtil.isEmpty(staffUsers.get(0).getScore())) {
            BigDecimal averageNum = new BigDecimal(averageNumber);
            average = averageNum.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        } else {
            double scoreNumber = Double.parseDouble(staffUsers.get(0).getScore());
            double score = (averageNumber + scoreNumber) / 2.0;
            BigDecimal averageNum = new BigDecimal(score);
            average = averageNum.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }

        UserPo userPo = new UserPo();
        userPo.setUserId(staffId);
        userPo.setScore(String.valueOf(average));
        userV1InnerServiceSMOImpl.updateUser(userPo);
    }
}
