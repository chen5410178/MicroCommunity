package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.data.ExportDataDto;
import com.java110.job.export.IExportDataAdapt;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service("exportCreateShopFeeTemplate")
public class ExportCreateShopFeeTemplateAdapt implements IExportDataAdapt {

    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) throws ParseException {
        JSONObject reqJson = exportDataDto.getReqJson();
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet("创建商铺费用");

        Row row = sheet.createRow(0);
        Cell cell0 = row.createCell(0);
        cell0.setCellValue("商铺编号: 填写系统中商铺编号；\n类型: 固定填写4444表示商铺；\n" +
            "费用项ID: 系统中的费用项ID；\n收费项目: 费用项名称；\n" +
            "建账时间: 格式YYYY-MM-DD；\n计费起始时间: 格式YYYY-MM-DD；\n" +
            "计费结束时间: 格式YYYY-MM-DD，周期性费用无结束时间可填2050-01-01；\n" +
            "注意：所有单元格式为文本");
        CellStyle cs = workbook.createCellStyle();
        cs.setWrapText(true);
        cell0.setCellStyle(cs);
        row.setHeight((short) (200 * 10));

        row = sheet.createRow(1);
        row.createCell(0).setCellValue("商铺编号");
        row.createCell(1).setCellValue("类型");
        row.createCell(2).setCellValue("费用项ID");
        row.createCell(3).setCellValue("收费项目");
        row.createCell(4).setCellValue("建账时间");
        row.createCell(5).setCellValue("计费起始时间");
        row.createCell(6).setCellValue("计费结束时间");

        CellRangeAddress region = new CellRangeAddress(0, 0, 0, 5);
        sheet.addMergedRegion(region);

        return workbook;
    }
}
