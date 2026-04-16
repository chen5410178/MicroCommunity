package com.java110.report.cmd.reportFeeMonthStatistics;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.report.QueryStatisticsDto;
import com.java110.report.statistics.IBaseDataStatistics;
import com.java110.report.statistics.IFeeStatistics;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.MoneyUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 查询费用汇总单项统计命令类
 *
 * <p>该类用于处理费用月统计报表的单项查询请求，根据前端传递的查询类型参数，
 * 只查询指定的统计项，减少数据库压力，提高查询效率</p>
 *
 * <p>主要功能：</p>
 * <ul>
 *   <li>验证查询参数的有效性</li>
 *   <li>根据queryType参数查询指定的统计项</li>
 *   <li>串行执行查询，避免数据库连接池压力</li>
 *   <li>返回单个统计项结果</li>
 * </ul>
 *
 * <p>支持的查询类型（queryType）：</p>
 * <ul>
 *   <li>hisOweFee - 历史欠费</li>
 *   <li>curOweFee - 当月欠费</li>
 *   <li>curReceivableFee - 当月应收</li>
 *   <li>hisReceivedFee - 欠费追回</li>
 *   <li>preReceivedFee - 预交费用</li>
 *   <li>receivedFee - 实收费用</li>
 *   <li>roomCount - 房屋总数</li>
 *   <li>feeRoomCount - 收费房屋数</li>
 *   <li>oweRoomCount - 欠费户数</li>
 * </ul>
 *
 * @author wuxw
 * @version 1.0
 * @serviceCode reportFeeMonthStatistics.queryReportFeeSummaryItem
 */
@Java110Cmd(serviceCode = "reportFeeMonthStatistics.queryReportFeeSummary")
public class QueryReportFeeSummaryCmd extends Cmd {

    /**
     * 费用统计服务接口
     */
    @Autowired
    private IFeeStatistics feeStatisticsImpl;

    /**
     * 基础数据统计服务接口
     */
    @Autowired
    private IBaseDataStatistics baseDataStatisticsImpl;

    /**
     * 校验查询参数的有效性
     *
     * <p>验证请求参数中必须包含开始日期、结束日期、小区信息和查询类型等必要字段</p>
     *
     * @param event   命令事件对象，包含请求上下文信息
     * @param context 命令数据流上下文，用于获取和设置请求响应数据
     * @param reqJson 请求参数的JSON对象
     * @throws CmdException 当参数验证失败时抛出异常
     */
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        // 调用父类方法验证分页信息
        super.validatePageInfo(reqJson);
        // 验证必须包含开始日期
        Assert.hasKeyAndValue(reqJson, "startDate", "未包含开始日期");
        // 验证必须包含结束日期
        Assert.hasKeyAndValue(reqJson, "endDate", "未包含结束日期");
        // 验证必须包含小区信息
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
        // 验证必须包含查询类型
        Assert.hasKeyAndValue(reqJson, "queryType", "未包含查询类型");
    }

    /**
     * 执行费用汇总单项查询命令
     *
     * <p>根据queryType参数，只查询指定的统计项，串行执行避免数据库压力</p>
     *
     * @param event   命令事件对象
     * @param context 命令数据流上下文
     * @param reqJson 请求参数的JSON对象
     * @throws CmdException 当命令执行过程中发生错误时抛出异常
     */
    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        // 创建查询统计数据传输对象
        QueryStatisticsDto queryStatisticsDto = new QueryStatisticsDto();
        // 设置小区ID
        queryStatisticsDto.setCommunityId(reqJson.getString("communityId"));

        // 处理开始日期格式，如果日期不包含时间部分，则添加默认时间
        if (reqJson.containsKey("startDate") && !reqJson.getString("startDate").contains(":")) {
            queryStatisticsDto.setStartDate(reqJson.getString("startDate") + " 00:00:00");
        }
        // 处理结束日期格式，如果日期不包含时间部分，则添加默认时间
        if (reqJson.containsKey("endDate") && !reqJson.getString("endDate").contains(":")) {
            queryStatisticsDto.setEndDate(reqJson.getString("endDate") + " 23:59:59");
        }

        // 设置其他查询条件参数
        queryStatisticsDto.setConfigId(reqJson.getString("configId"));
        queryStatisticsDto.setFloorId(reqJson.getString("floorId"));
        queryStatisticsDto.setObjName(reqJson.getString("objName"));
        queryStatisticsDto.setFeeTypeCd(reqJson.getString("feeTypeCd"));
        queryStatisticsDto.setOwnerName(reqJson.getString("ownerName"));
        queryStatisticsDto.setLink(reqJson.getString("link"));

        // 处理费用配置ID数组
        if (reqJson.containsKey("configIds")) {
            queryStatisticsDto.setConfigIds(reqJson.getString("configIds").split(","));
        }

        // 获取查询类型参数
        String queryType = reqJson.getString("queryType");

        // 创建结果数据对象
        JSONObject data = new JSONObject();

        // 根据查询类型执行对应的查询（串行执行，避免数据库压力）
        switch (queryType) {
            case "hisOweFee":
                // 查询历史月份欠费金额
                double hisOweFee = feeStatisticsImpl.getHisMonthOweFee(queryStatisticsDto);
                data.put("hisOweFee", MoneyUtil.computePriceScale(hisOweFee));
                break;

            case "curOweFee":
                // 查询当前月份欠费金额
                double curOweFee = feeStatisticsImpl.getCurMonthOweFee(queryStatisticsDto);
                data.put("curOweFee", MoneyUtil.computePriceScale(curOweFee));
                break;

            case "curReceivableFee":
                // 查询当前月份应收费用金额
                double curReceivableFee = feeStatisticsImpl.getCurReceivableFee(queryStatisticsDto);
                data.put("curReceivableFee", MoneyUtil.computePriceScale(curReceivableFee));
                break;

            case "hisReceivedFee":
                // 查询历史欠费追回金额
                double hisReceivedFee = feeStatisticsImpl.getHisReceivedFee(queryStatisticsDto);
                data.put("hisReceivedFee", MoneyUtil.computePriceScale(hisReceivedFee));
                break;

            case "preReceivedFee":
                // 查询预交费用金额
                double preReceivedFee = feeStatisticsImpl.getPreReceivedFee(queryStatisticsDto);
                data.put("preReceivedFee", MoneyUtil.computePriceScale(preReceivedFee));
                break;

            case "receivedFee":
                // 查询实际收到费用金额
                double receivedFee = feeStatisticsImpl.getReceivedFee(queryStatisticsDto);
                data.put("receivedFee", MoneyUtil.computePriceScale(receivedFee));
                break;

            case "roomCount":
                // 查询符合条件的房屋总数
                long roomCount = baseDataStatisticsImpl.getRoomCount(queryStatisticsDto);
                data.put("roomCount", roomCount);
                break;

            case "feeRoomCount":
                // 查询需要收费的房屋数量
                long feeRoomCount = feeStatisticsImpl.getFeeRoomCount(queryStatisticsDto);
                data.put("feeRoomCount", feeRoomCount);
                break;

            case "oweRoomCount":
                // 查询欠费房屋数量
                int oweRoomCount = feeStatisticsImpl.getOweRoomCount(queryStatisticsDto);
                data.put("oweRoomCount", oweRoomCount);
                break;

            default:
                throw new CmdException("不支持的查询类型: " + queryType +
                        "，支持的查询类型：hisOweFee, curOweFee, curReceivableFee, " +
                        "hisReceivedFee, preReceivedFee, receivedFee, roomCount, " +
                        "feeRoomCount, oweRoomCount");
        }

        // 将结果数据封装为数组格式
        JSONArray datas = new JSONArray();
        datas.add(data);
        // 设置响应实体返回给前端
        context.setResponseEntity(ResultVo.createResponseEntity(datas));
    }
}

