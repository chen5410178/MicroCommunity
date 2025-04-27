package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.PageDto;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.reportCustom.ReportCustomComponentDto;
import com.java110.intf.report.IReportCustomComponentV1InnerServiceSMO;
import com.java110.job.export.IExportDataAdapt;
import com.java110.service.smo.IQueryServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service("exportCustomReportTableData")
public class ExportCustomReportTableDataAdapt implements IExportDataAdapt {

    @Autowired
    private IReportCustomComponentV1InnerServiceSMO reportCustomComponentV1InnerServiceSMOImpl;


    @Autowired
    private IQueryServiceSMO queryServiceSMOImpl;

    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) throws ParseException {

        JSONObject reqJson = exportDataDto.getReqJson();

        SXSSFWorkbook workbook = null;  //工作簿
        String userId = "";
        //工作表
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);
        Sheet sheet = workbook.createSheet("报表数据");

        ReportCustomComponentDto reportCustomComponentDto = new ReportCustomComponentDto();
        reportCustomComponentDto.setComponentId(reqJson.getString("componentId"));
        List<ReportCustomComponentDto> reportCustomComponentDtos = reportCustomComponentV1InnerServiceSMOImpl.queryReportCustomComponents(reportCustomComponentDto);
        Assert.listOnlyOne(reportCustomComponentDtos, "组件不存在，请联系开发人员");

        reportCustomComponentDto = reportCustomComponentDtos.get(0);

        JSONObject data = null;
        if (ReportCustomComponentDto.QUERY_MODEL_SQL.equals(reportCustomComponentDto.getQueryModel())) {
            data =  doDealSql(reqJson, reportCustomComponentDto);
        } else if (ReportCustomComponentDto.QUERY_MODEL_JAVA.equals(reportCustomComponentDto.getQueryModel())) {
            data = doDealJava(reqJson, reportCustomComponentDto);
        } else {
            throw new CmdException("组件实现方式不支持，请联系开发人员");
        }

        JSONArray th = data.getJSONArray("th");


        Row row = sheet.createRow(0);
        for (int thIndex = 0; thIndex < th.size(); thIndex++) {
            row.createCell(thIndex).setCellValue(th.getString(thIndex));
        }

        JSONArray td = data.getJSONArray("td");

        if (ListUtil.isNull(td)) {
            return workbook;
        }
        JSONObject tdObj = null;
        for (int tdIndex = 0; tdIndex < td.size(); tdIndex++) {
            row = sheet.createRow(tdIndex + 1);
            tdObj = td.getJSONObject(tdIndex);
            for (int thIndex = 0; thIndex < th.size(); thIndex++) {
                row.createCell(thIndex).setCellValue(tdObj.getString(th.getString(thIndex)));
            }
        }
        return workbook;
    }

    private JSONObject doDealJava(JSONObject reqJson, ReportCustomComponentDto reportCustomComponentDto) {
        //校验是否传了 分页信息
        String javaScript = reportCustomComponentDto.getJavaScript();
        int page = reqJson.getInteger("page");
        if (page != PageDto.DEFAULT_PAGE) {
            reqJson.put("page", (page - 1) * reqJson.getIntValue("row"));
        }
        JSONObject data = queryServiceSMOImpl.execJava(reqJson, javaScript);
        long total = 0;
        if (data.getLong("total") != null) {
            total = data.getLong("total");
        }
        return data;
    }

    private JSONObject doDealSql(JSONObject reqJson, ReportCustomComponentDto reportCustomComponentDto) {
        //校验是否传了 分页信息
        String sql = reportCustomComponentDto.getComponentSql();
        long total = reqJson.getIntValue("row");
        if (sql.trim().contains("test=\"count")) { // 如果包含 count(1) 求总数
            JSONObject reqJsonCount = new JSONObject();
            for (String key : reqJson.keySet()) {
                if ("row".equals(key) || "page".equals(key)) {
                    continue;
                }
                reqJsonCount.put(key, reqJson.get(key));
            }
            reqJsonCount.put("count", "1");
            JSONObject data = queryServiceSMOImpl.execQuerySql(reqJsonCount, sql);
            total = data.getJSONArray("td").getJSONObject(0).getIntValue("total");
        }
        reqJson.put("count", "0");
        int page = reqJson.getInteger("page");
        if (page != PageDto.DEFAULT_PAGE) {
            reqJson.put("page", (page - 1) * reqJson.getIntValue("row"));
        }
        JSONObject data = queryServiceSMOImpl.execQuerySql(reqJson, sql);
        if (!sql.trim().contains("test=\"count")) {
            total = JSONArray.parseArray(data.getString("td")).size();
        }
        if (!StringUtil.isEmpty(sql) && !sql.contains("limit #page#,#row#")) {
            sql = sql + " limit #page#,#row#";
            data = queryServiceSMOImpl.execQuerySql(reqJson, sql);
        }
        return data;
    }
}
