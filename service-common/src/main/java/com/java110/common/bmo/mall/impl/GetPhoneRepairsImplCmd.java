package com.java110.common.bmo.mall.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.mall.IMallCommonApiBmo;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.intf.community.IRepairUserInnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.*;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service("getPhoneRepairsImpl")
public class GetPhoneRepairsImplCmd implements IMallCommonApiBmo {
    /**
     * 报修服务接口
     */
    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    /**
     * 报修用户关系服务接口
     */
    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMOImpl;

    /**
     * 菜单服务接口
     */
    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    /**
     * 用户服务接口
     */
    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Override
    public void validate(ICmdDataFlowContext context, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "请求中未包含小区ID");
        Assert.jsonObjectHaveKey(reqJson, "page", "请求中未包含page信息");
        Assert.jsonObjectHaveKey(reqJson, "row", "请求中未包含row信息");
    }

    @Override
    public void doCmd(ICmdDataFlowContext context, JSONObject reqJson) {
// 构建用户查询条件
        UserDto userDto = new UserDto();
        userDto.setTel(reqJson.getString("mallTel"));
        userDto.setLevelCd(UserDto.LEVEL_CD_USER);

        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        //Assert.listOnlyOne(userDtos, "用户未登录");
        if(ListUtil.isNull(userDtos)){
            throw new CmdException("物业系统用户不存在");
        }

        // 将请求参数转换为报修DTO对象
        RepairDto ownerRepairDto = BeanConvertUtil.covertBean(reqJson, RepairDto.class);

        // 设置查询条件中的电话号码为当前用户的电话号码
        ownerRepairDto.setTel(userDtos.get(0).getTel());
        // 如果请求中包含报修状态参数，设置状态查询条件
        if (reqJson.containsKey("repairStates")) {
            String[] states = reqJson.getString("repairStates").split(",");
            ownerRepairDto.setStatess(states);
        }

        // 查询符合条件的报修记录总数
        int count = repairInnerServiceSMOImpl.queryRepairsCount(ownerRepairDto);
        List<RepairDto> repairDtos;

        // 如果有查询结果，获取报修列表并计算评分
        if (count > 0) {
            repairDtos = repairInnerServiceSMOImpl.queryRepairs(ownerRepairDto);
            computeRepairScore(repairDtos);
        } else {
            // 没有查询结果时返回空列表
            repairDtos = new ArrayList<>();
        }

        // 构建响应实体，包含分页信息和查询结果
        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(
                (int) Math.ceil((double) count / (double) reqJson.getInteger("row")), // 计算总页数
                count, // 总记录数
                repairDtos // 报修数据列表
        );
        context.setResponseEntity(responseEntity);
    }

    /**
     * 计算报修评分方法
     *
     * 对报修记录进行综合评分计算，包括：
     * 1. 综合评分、上门速度评分、维修员服务评分的计算和格式化
     * 2. 平均分的计算和四舍五入处理
     * 3. 处理时长计算和超时判断
     *
     * @param repairDtos 报修数据对象列表
     */
    private void computeRepairScore(List<RepairDto> repairDtos) {
        // 如果报修列表为空，直接返回
        if (ListUtil.isNull(repairDtos)) {
            return;
        }

        Date finishTime = null;
        String submitHour;
        Date timeout = null;

        // 遍历所有报修记录，逐条计算评分
        for (RepairDto repairDto : repairDtos) {
            // 获取综合评价得分并转换为整数（向上取整）
            String appraiseScoreNumber = repairDto.getAppraiseScore();
            Double appraiseScoreNum = 0.0;
            if (!StringUtil.isEmpty(appraiseScoreNumber)) {
                appraiseScoreNum = Double.parseDouble(appraiseScoreNumber);
            }
            int appraiseScore = (int) Math.ceil(appraiseScoreNum);

            // 获取上门速度评分并转换为整数（向上取整）
            String doorSpeedScoreNumber = repairDto.getDoorSpeedScore();
            Double doorSpeedScoreNum = 0.0;
            if (!StringUtil.isEmpty(doorSpeedScoreNumber)) {
                doorSpeedScoreNum = Double.parseDouble(doorSpeedScoreNumber);
            }
            int doorSpeedScore = (int) Math.ceil(doorSpeedScoreNum);

            // 获取维修员服务评分并转换为整数（向上取整）
            String repairmanServiceScoreNumber = repairDto.getRepairmanServiceScore();
            Double repairmanServiceScoreNum = 0.0;
            if (!StringUtil.isEmpty(repairmanServiceScoreNumber)) {
                repairmanServiceScoreNum = Double.parseDouble(repairmanServiceScoreNumber);
            }
            int repairmanServiceScore = (int) Math.ceil(repairmanServiceScoreNum);

            // 计算三个评分的平均值，保留两位小数（四舍五入）
            double averageNumber = (appraiseScoreNum + doorSpeedScoreNum + repairmanServiceScoreNum) / 3.0;
            BigDecimal averageNum = new BigDecimal(averageNumber);
            Double average = averageNum.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

            // 将计算后的评分设置回报修对象
            repairDto.setAppraiseScore(String.valueOf(appraiseScore));
            repairDto.setDoorSpeedScore(String.valueOf(doorSpeedScore));
            repairDto.setRepairmanServiceScore(String.valueOf(repairmanServiceScore));
            repairDto.setAverage(String.valueOf(average));

            // 计算提单时长和处理超时判断
            finishTime = DateUtil.getCurrentDate(); // 获取当前时间作为完成时间
            repairDto.setTimeoutFlag("N"); // 默认设置为未超时

            // 如果报修记录有完成时间，使用完成时间计算
            if (!StringUtil.isEmpty(repairDto.getFinishTime())) {
                finishTime = DateUtil.getDateFromStringA(repairDto.getFinishTime());
            } else {
                // 如果没有完成时间，判断是否超时
                timeout = DateUtil.getDateFromStringA(repairDto.getTimeout());
                // 如果当前时间超过超时时间，标记为超时
                if (finishTime.getTime() > timeout.getTime()) {
                    repairDto.setStateName(repairDto.getStateName() + "(超时)");
                    repairDto.setTimeoutFlag("Y");
                }
            }

            // 计算从创建时间到完成时间的时长差
            submitHour = DateUtil.calculateTimeDifference(repairDto.getCreateTime(), finishTime);
            repairDto.setSubmitHours(submitHour);
        }
    }
}
