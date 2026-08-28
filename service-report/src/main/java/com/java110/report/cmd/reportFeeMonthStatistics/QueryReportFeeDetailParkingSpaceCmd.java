package com.java110.report.cmd.reportFeeMonthStatistics;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.report.QueryStatisticsDto;
import com.java110.report.statistics.IBaseDataStatistics;
import com.java110.report.statistics.IFeeStatistics;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.MoneyUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 查询车位费用明细表
 * 以车位为维度统计 车位的各个费用项 欠费和实收
 */
@Java110Cmd(serviceCode = "reportFeeMonthStatistics.queryReportFeeDetailParkingSpace")
public class QueryReportFeeDetailParkingSpaceCmd extends Cmd {

    @Autowired
    private IFeeStatistics feeStatisticsImpl;

    @Autowired
    private IBaseDataStatistics baseDataStatisticsImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "startDate", "未包含开始日期");
        Assert.hasKeyAndValue(reqJson, "endDate", "未包含结束日期");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        String storeId = context.getReqHeaders().get("store-id");

        QueryStatisticsDto queryStatisticsDto = new QueryStatisticsDto();
        queryStatisticsDto.setCommunityId(reqJson.getString("communityId"));
        queryStatisticsDto.setStartDate(reqJson.getString("startDate"));
        queryStatisticsDto.setEndDate(reqJson.getString("endDate"));
        queryStatisticsDto.setConfigId(reqJson.getString("configId"));
        queryStatisticsDto.setObjName(reqJson.getString("psNum"));
        queryStatisticsDto.setPaName(reqJson.getString("paNum"));
        queryStatisticsDto.setFeeTypeCd(reqJson.getString("feeTypeCd"));
        queryStatisticsDto.setOwnerName(reqJson.getString("ownerName"));
        queryStatisticsDto.setLink(reqJson.getString("link"));
        queryStatisticsDto.setPage(reqJson.getInteger("page"));
        queryStatisticsDto.setRow(reqJson.getInteger("row"));
        queryStatisticsDto.setStoreId(storeId);
        long count = baseDataStatisticsImpl.getParkingSpaceCount(queryStatisticsDto);
        List<ParkingSpaceDto> parkingSpaceDtos = null;
        if (count > 0) {
            parkingSpaceDtos = baseDataStatisticsImpl.getParkingSpace(queryStatisticsDto);
        } else {
            parkingSpaceDtos = new ArrayList<>();
        }

        // todo 计算 车位欠费实收数据
        JSONArray datas = computeParkingSpaceOweReceivedFee(parkingSpaceDtos, queryStatisticsDto);

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) queryStatisticsDto.getRow()), count, datas);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
        context.setResponseEntity(responseEntity);

    }

    /**
     * 计算车位欠费 实收费用
     *
     * @param parkingSpaceDtos
     * @return
     */
    private JSONArray computeParkingSpaceOweReceivedFee(List<ParkingSpaceDto> parkingSpaceDtos, QueryStatisticsDto queryStatisticsDto) {
        if (parkingSpaceDtos == null || parkingSpaceDtos.size() < 1) {
            return new JSONArray();
        }

        JSONArray datas = new JSONArray();
        JSONObject data = null;

        List<String> objIds = new ArrayList<>();
        for (ParkingSpaceDto parkingSpaceDto : parkingSpaceDtos) {
            objIds.add(parkingSpaceDto.getPsId());
            data = new JSONObject();
            data.put("psId", parkingSpaceDto.getPsId());
            data.put("psNum", parkingSpaceDto.getNum());
            data.put("paName", parkingSpaceDto.getPaName());
            data.put("ownerName", parkingSpaceDto.getOwnerName());
            data.put("ownerId", parkingSpaceDto.getOwnerId());
            data.put("link", parkingSpaceDto.getLink());
            datas.add(data);
        }

        queryStatisticsDto.setObjIds(objIds.toArray(new String[objIds.size()]));
        queryStatisticsDto.setObjType(FeeDto.PAYER_OBJ_TYPE_PARKING_SPACE);
        List<Map> infos = feeStatisticsImpl.getObjFeeSummary(queryStatisticsDto);

        if (infos == null || infos.size() < 1) {
            return datas;
        }

        BigDecimal oweFee = null;
        BigDecimal receivedFee = null;
        double oweFeeD = 0;
        double receivedFeeD = 0;
        for (int dataIndex = 0; dataIndex < datas.size(); dataIndex++) {
            oweFee = new BigDecimal(0.00);
            receivedFee = new BigDecimal(0.00);
            data = datas.getJSONObject(dataIndex);
            for (Map info : infos) {
                if (!data.get("psId").toString().equals(info.get("objId"))) {
                    continue;
                }

                oweFeeD = Double.parseDouble(info.get("oweFee").toString());
                receivedFeeD = Double.parseDouble(info.get("receivedFee").toString());

                oweFee = oweFee.add(new BigDecimal(oweFeeD + ""));
                receivedFee = receivedFee.add(new BigDecimal(receivedFeeD + ""));
                data.put("oweFee" + info.get("feeTypeCd").toString(), MoneyUtil.computePriceScale(oweFeeD));
                data.put("receivedFee" + info.get("feeTypeCd").toString(), MoneyUtil.computePriceScale(receivedFeeD));
            }
            data.put("oweFee", MoneyUtil.computePriceScale(oweFee.doubleValue()));
            data.put("receivedFee", MoneyUtil.computePriceScale(receivedFee.doubleValue()));
        }

        return datas;
    }
}
