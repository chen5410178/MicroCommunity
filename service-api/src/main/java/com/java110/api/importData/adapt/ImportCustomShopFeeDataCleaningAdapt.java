package com.java110.api.importData.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.importData.DefaultImportDataAdapt;
import com.java110.api.importData.IImportDataCleaningAdapt;
import com.java110.dto.importData.ImportCustomCreateFeeDto;
import com.java110.dto.system.ComponentValidateResult;
import com.java110.intf.fee.IPayFeeBatchV1InnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ImportExcelUtils;
import com.java110.utils.util.StringUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("importCustomShopFeeDataCleaning")
public class ImportCustomShopFeeDataCleaningAdapt extends DefaultImportDataAdapt implements IImportDataCleaningAdapt {

    @Autowired
    private IPayFeeBatchV1InnerServiceSMO payFeeBatchV1InnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List analysisExcel(Workbook workbook, JSONObject paramIn, ComponentValidateResult result) throws Exception {
        List<ImportCustomCreateFeeDto> importCustomCreateFeeDtos = new ArrayList<ImportCustomCreateFeeDto>();
        getImportCustomShopCreateFeeDtos(workbook, importCustomCreateFeeDtos, result);
        generatorBatch(paramIn);
        for (ImportCustomCreateFeeDto importShopFee : importCustomCreateFeeDtos) {
            importShopFee.setBatchId(paramIn.getString("batchId"));
            importShopFee.setUserId(paramIn.getString("userId"));
            importShopFee.setStoreId(paramIn.getString("storeId"));
            importShopFee.setCommunityId(paramIn.getString("communityId"));
        }
        return importCustomCreateFeeDtos;
    }

    private void getImportCustomShopCreateFeeDtos(Workbook workbook, List<ImportCustomCreateFeeDto> importCustomCreateFeeDtos, ComponentValidateResult result) {
        Sheet sheet = ImportExcelUtils.getSheet(workbook, "创建商铺费用");
        List<Object[]> oList = ImportExcelUtils.listFromSheet(sheet);
        ImportCustomCreateFeeDto importShopFee = null;
        for (int osIndex = 0; osIndex < oList.size(); osIndex++) {
            Object[] os = oList.get(osIndex);
            if (osIndex == 0 || osIndex == 1) {
                continue;
            }
            if (StringUtil.isNullOrNone(os[0])) {
                continue;
            }
            if (StringUtil.isNullOrNone(os[5])) {
                continue;
            }

            Assert.hasValue(os[0], (osIndex + 1) + "行商铺编号不能为空");
            Assert.hasValue(os[2], (osIndex + 1) + "行费用项ID不能为空");
            Assert.hasValue(os[3], (osIndex + 1) + "行收费项目不能为空");
            Assert.hasValue(os[4], (osIndex + 1) + "行建账时间不能为空");
            Assert.hasValue(os[5], (osIndex + 1) + "行计费起始时间不能为空");

            String createTime = excelDoubleToDate(os[4].toString());
            String startTime = excelDoubleToDate(os[5].toString());
            Assert.isDate(createTime, DateUtil.DATE_FORMATE_STRING_B, (osIndex + 1) + "行建账时间格式错误 请填写YYYY-MM-DD 文本格式");
            Assert.isDate(startTime, DateUtil.DATE_FORMATE_STRING_B, (osIndex + 1) + "行计费起始时间格式错误 请填写YYYY-MM-DD 文本格式");
            String endTime = "";
            if (os.length > 6 && os[6] != null) {
                endTime = excelDoubleToDate(os[6].toString());
                Assert.isDate(endTime, DateUtil.DATE_FORMATE_STRING_B, (osIndex + 1) + "行计费结束时间格式错误 请填写YYYY-MM-DD 文本格式");
            }

            importShopFee = new ImportCustomCreateFeeDto();
            importShopFee.setObjName(os[0].toString());
            importShopFee.setObjType(ImportCustomCreateFeeDto.TYPE_SHOP);
            importShopFee.setConfigId(os[2].toString());
            importShopFee.setConfigName(os[3].toString());
            importShopFee.setCreateTime(createTime);
            importShopFee.setStartTime(startTime);
            importShopFee.setEndTime(endTime);
            importShopFee.setCommunityId(result.getCommunityId());
            importCustomCreateFeeDtos.add(importShopFee);
        }
    }
}
