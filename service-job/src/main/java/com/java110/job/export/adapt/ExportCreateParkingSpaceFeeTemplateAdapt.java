package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.fee.IPayFeeConfigV1InnerServiceSMO;
import com.java110.job.export.IExportDataAdapt;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

/**
 * 车位创建费用模板导出适配器
 * 费用挂在车位（psId）上，第一列=停车场编号-停车区域-车位编号
 */
@Service("exportCreateParkingSpaceFeeTemplate")
public class ExportCreateParkingSpaceFeeTemplateAdapt implements IExportDataAdapt {

    public static final String TYPE_PARKSPACE = "2002";

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IPayFeeConfigV1InnerServiceSMO payFeeConfigV1InnerServiceSMOImpl;

    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) throws ParseException {
        JSONObject reqJson = exportDataDto.getReqJson();
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        getParkspaceAndConfigs(reqJson, workbook);
        return workbook;
    }

    private void getParkspaceAndConfigs(JSONObject paramIn, SXSSFWorkbook workbook) {
        Sheet sheet = workbook.createSheet("创建费用");
        Row row = sheet.createRow(0);
        Cell cell0 = row.createCell(0);
        cell0.setCellValue("费用名称: 请填写系统中费用类型，如物业费，押金等 ；\n计费起始时间: " +
                "计费起始时间，格式为YYYY-MM-DD；\n计费结束时间，格式为YYYY-MM-DD；\n" +
                "建账时间: 建账时间，格式为YYYY-MM-DD； \n 类型：表明是合同 房屋 还是车辆 房屋 1001 车辆 2002 合同 3003" +
                "\n注意：所有单元格式为文本，计费结束时间,如果时周期性费没有结束时间可以填写2050-01-01");
        CellStyle cs = workbook.createCellStyle();
        cs.setWrapText(true);  //关键
        cell0.setCellStyle(cs);
        row.setHeight((short) (200 * 10));
        row = sheet.createRow(1);
        row.createCell(0).setCellValue("车位编号");
        row.createCell(1).setCellValue("类型");
        row.createCell(2).setCellValue("费用项ID");
        row.createCell(3).setCellValue("收费项目");
        row.createCell(4).setCellValue("建账时间");
        row.createCell(5).setCellValue("计费起始时间");
        row.createCell(6).setCellValue("计费结束时间");

        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setCommunityId(paramIn.getString("communityId"));
        parkingSpaceDto.setPaIds(paramIn.getString("paIds").split(","));
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);

        if (ListUtil.isNull(parkingSpaceDtos)) {
            return;
        }

        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setConfigIds(paramIn.getString("configIds").split(","));
        feeConfigDto.setCommunityId(paramIn.getString("communityId"));
        List<FeeConfigDto> feeConfigDtos = payFeeConfigV1InnerServiceSMOImpl.queryPayFeeConfigs(feeConfigDto);

        if (ListUtil.isNull(feeConfigDtos)) {
            return;
        }

        int roomIndex = 2;
        for (ParkingSpaceDto tmpParkingSpaceDto : parkingSpaceDtos) {
            for (FeeConfigDto tmpFeeConfigDto : feeConfigDtos) {
                row = sheet.createRow(roomIndex);
                row.createCell(0).setCellValue(getPsName(tmpParkingSpaceDto));
                row.createCell(1).setCellValue(TYPE_PARKSPACE);
                row.createCell(2).setCellValue(tmpFeeConfigDto.getConfigId());
                row.createCell(3).setCellValue(tmpFeeConfigDto.getFeeName());
                row.createCell(4).setCellValue("");
                row.createCell(5).setCellValue("");
                row.createCell(6).setCellValue("");
                roomIndex += 1;
            }
        }
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, 5);
        sheet.addMergedRegion(region);
    }

    /**
     * 车位编号（与前端 parkingSpaceTreeHasOwner.vue 展示格式一致）
     * sectionName 非空：areaNum-sectionName-num；为空：areaNum-num
     *
     * @param parkingSpaceDto
     * @return
     */
    private String getPsName(ParkingSpaceDto parkingSpaceDto) {
        if (StringUtil.isEmpty(parkingSpaceDto.getSectionName())) {
            return parkingSpaceDto.getAreaNum() + "-" + parkingSpaceDto.getNum();
        }
        return parkingSpaceDto.getAreaNum() + "-" + parkingSpaceDto.getSectionName() + "-" + parkingSpaceDto.getNum();
    }
}
