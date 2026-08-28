package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.dict.DictDto;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.report.QueryStatisticsDto;
import com.java110.intf.dev.IDictV1InnerServiceSMO;
import com.java110.intf.report.IBaseDataStatisticsInnerServiceSMO;
import com.java110.intf.report.IReportFeeStatisticsInnerServiceSMO;
import com.java110.job.export.IExportDataAdapt;
import com.java110.utils.util.StringUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 车位费用明细导出
 */
@Service("reportFeeDetailParkingSpace")
public class ReportFeeDetailParkingSpaceAdapt implements IExportDataAdapt {

    private static final int MAX_ROW = 60000;

    @Autowired
    private IDictV1InnerServiceSMO dictV1InnerServiceSMOImpl;

    @Autowired
    private IBaseDataStatisticsInnerServiceSMO baseDataStatisticsInnerServiceSMOImpl;

    @Autowired
    private IReportFeeStatisticsInnerServiceSMO reportFeeStatisticsInnerServiceSMOImpl;

    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) {
        SXSSFWorkbook workbook = null;  //工作簿
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);

        Sheet sheet = workbook.createSheet("车位费用明细");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("车位");
        row.createCell(1).setCellValue("业主");
        row.createCell(2).setCellValue("欠费");
        row.createCell(3).setCellValue("实收");

        DictDto dictDto = new DictDto();
        dictDto.setTableName("pay_fee_config");
        dictDto.setTableColumns("fee_type_cd_show");
        List<DictDto> dictDtos = dictV1InnerServiceSMOImpl.queryDicts(dictDto);

        for (int dictIndex = 0; dictIndex < dictDtos.size(); dictIndex++) {
            row.createCell(4 + dictIndex * 2).setCellValue(dictDtos.get(dictIndex).getName());
        }


        JSONObject reqJson = exportDataDto.getReqJson();

        //todo 查询数据
        doReportData(sheet, reqJson, dictDtos);

        for (int dictIndex = 0; dictIndex < dictDtos.size(); dictIndex++) {
            row.createCell(4 + dictIndex * 2).setCellValue(dictDtos.get(dictIndex).getName());
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 4 + dictIndex * 2, 5 + dictIndex * 2));
        }
        return workbook;
    }

    private void doReportData(Sheet sheet, JSONObject reqJson, List<DictDto> dictDtos) {

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
        queryStatisticsDto.setObjType(FeeDto.PAYER_OBJ_TYPE_PARKING_SPACE);
        long count = getParkingSpaceCount(queryStatisticsDto);
        List<ParkingSpaceDto> parkingSpaceDtos = null;
        int totalPage = (int) Math.ceil((double) count / (double) MAX_ROW);
        for (int page = 1; page <= totalPage; page++) {
            queryStatisticsDto.setPage(page);
            queryStatisticsDto.setRow(MAX_ROW);
            parkingSpaceDtos = getParkingSpace(queryStatisticsDto);
            // todo 计算 车位欠费实收数据
            JSONArray datas = computeParkingSpaceOweReceivedFee(parkingSpaceDtos, queryStatisticsDto);
            appendData(datas, sheet, (page - 1) * MAX_ROW, dictDtos);
        }

    }


    private void appendData(JSONArray datas, Sheet sheet, int step, List<DictDto> dictDtos) {
        Row row = null;
        JSONObject dataObj = null;
        String oweFee = "";
        String receivedFee = "";
        for (int roomIndex = 0; roomIndex < datas.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + step + 1);
            dataObj = datas.getJSONObject(roomIndex);
            row.createCell(0).setCellValue(dataObj.getString("paName") + "-" + dataObj.getString("psNum"));
            row.createCell(1).setCellValue(dataObj.getString("ownerName") + "(" + dataObj.getString("link") + ")");
            row.createCell(2).setCellValue(dataObj.getString("oweFee"));
            row.createCell(3).setCellValue(dataObj.getString("receivedFee"));

            for (int dictIndex = 0; dictIndex < dictDtos.size(); dictIndex++) {
                oweFee = dataObj.getString("oweFee" + dictDtos.get(dictIndex).getStatusCd());
                if (StringUtil.isEmpty(oweFee)) {
                    oweFee = "0";
                }
                receivedFee = dataObj.getString("receivedFee" + dictDtos.get(dictIndex).getStatusCd());
                if (StringUtil.isEmpty(receivedFee)) {
                    receivedFee = "0";
                }
                row.createCell(4 + dictIndex * 2).setCellValue(oweFee);
                row.createCell(4 + dictIndex * 2 + 1).setCellValue(receivedFee);
            }
        }

    }


    public long getParkingSpaceCount(QueryStatisticsDto queryStatisticsDto) {
        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setCommunityId(queryStatisticsDto.getCommunityId());
        parkingSpaceDto.setNum(queryStatisticsDto.getObjName());
        parkingSpaceDto.setPaName(queryStatisticsDto.getPaName());
        parkingSpaceDto.setOwnerName(queryStatisticsDto.getOwnerName());
        parkingSpaceDto.setLink(queryStatisticsDto.getLink());
        return baseDataStatisticsInnerServiceSMOImpl.getParkingSpaceCount(parkingSpaceDto);
    }

    public List<ParkingSpaceDto> getParkingSpace(QueryStatisticsDto queryStatisticsDto) {
        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setCommunityId(queryStatisticsDto.getCommunityId());
        parkingSpaceDto.setNum(queryStatisticsDto.getObjName());
        parkingSpaceDto.setPaName(queryStatisticsDto.getPaName());
        parkingSpaceDto.setOwnerName(queryStatisticsDto.getOwnerName());
        parkingSpaceDto.setLink(queryStatisticsDto.getLink());
        parkingSpaceDto.setPage(queryStatisticsDto.getPage());
        parkingSpaceDto.setRow(queryStatisticsDto.getRow());
        return baseDataStatisticsInnerServiceSMOImpl.getParkingSpaceInfo(parkingSpaceDto);
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
        List<Map> infos = reportFeeStatisticsInnerServiceSMOImpl.getObjFeeSummary(queryStatisticsDto);

        if (infos == null || infos.size() < 1) {
            return datas;
        }

        BigDecimal oweFee = null;
        BigDecimal receivedFee = null;
        for (int dataIndex = 0; dataIndex < datas.size(); dataIndex++) {
            oweFee = new BigDecimal(0.00);
            receivedFee = new BigDecimal(0.00);
            data = datas.getJSONObject(dataIndex);
            for (Map info : infos) {
                if (!data.get("psId").toString().equals(info.get("objId"))) {
                    continue;
                }

                oweFee = oweFee.add(new BigDecimal(info.get("oweFee").toString()));
                receivedFee = receivedFee.add(new BigDecimal(info.get("receivedFee").toString()));
                data.put("oweFee" + info.get("feeTypeCd").toString(), info.get("oweFee"));
                data.put("receivedFee" + info.get("feeTypeCd").toString(), info.get("receivedFee"));
            }
            data.put("oweFee", oweFee.doubleValue());
            data.put("receivedFee", receivedFee.doubleValue());
        }

        return datas;
    }
}
