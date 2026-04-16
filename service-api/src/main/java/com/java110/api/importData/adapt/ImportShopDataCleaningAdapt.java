package com.java110.api.importData.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.importData.DefaultImportDataAdapt;
import com.java110.api.importData.IImportDataCleaningAdapt;
import com.java110.dto.importData.ImportShopDto;
import com.java110.dto.shop.BuildingShopDto;
import com.java110.dto.system.ComponentValidateResult;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ImportExcelUtils;
import com.java110.utils.util.StringUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("importShopDataCleaning")
public class ImportShopDataCleaningAdapt extends DefaultImportDataAdapt implements IImportDataCleaningAdapt {

    @Override
    public List analysisExcel(Workbook workbook, JSONObject paramIn, ComponentValidateResult result) throws Exception {
        List<ImportShopDto> shops = new ArrayList<>();
        getShops(workbook, shops, result);
        validateShopInfo(shops);
        return shops;
    }

    private void getShops(Workbook workbook, List<ImportShopDto> shops, ComponentValidateResult result) {
        Sheet sheet = ImportExcelUtils.getSheet(workbook, "资产信息");
        List<Object[]> oList = ImportExcelUtils.listFromSheet(sheet);
        for (int osIndex = 0; osIndex < oList.size(); osIndex++) {
            Object[] os = oList.get(osIndex);
            if (osIndex == 0) {
                continue;
            }
            if (os == null || StringUtil.isNullOrNone(os[0])) {
                continue;
            }
            Assert.hasValue(os[0], (osIndex + 1) + "行商铺编号不能为空");
            Assert.hasValue(os[1], (osIndex + 1) + "行商铺位置不能为空");
            Assert.hasValue(os[2], (osIndex + 1) + "行建筑面积不能为空");
            Assert.hasValue(os[6], (osIndex + 1) + "行状态不能为空");

            ImportShopDto importShopDto = new ImportShopDto();
            importShopDto.setCommunityId(result.getCommunityId());
            importShopDto.setUserId(result.getUserId());
            importShopDto.setShopNum(os[0].toString().trim());
            importShopDto.setLocationName(os[1].toString().trim());
            importShopDto.setBuiltUpArea(os[2].toString().trim());
            if (os.length > 3 && !StringUtil.isNullOrNone(os[3])) {
                importShopDto.setShopArea(os[3].toString().trim());
            }
            if (os.length > 4 && !StringUtil.isNullOrNone(os[4])) {
                importShopDto.setStartTime(os[4].toString().trim());
            }
            if (os.length > 5 && !StringUtil.isNullOrNone(os[5])) {
                importShopDto.setEndTime(os[5].toString().trim());
            }
            importShopDto.setState(os[6].toString().trim());
            if (os.length > 7 && !StringUtil.isNullOrNone(os[7])) {
                importShopDto.setShopRent(os[7].toString().trim());
            }
            if (os.length > 8 && !StringUtil.isNullOrNone(os[8])) {
                importShopDto.setOwnerName(os[8].toString().trim());
            }
            if (os.length > 9 && !StringUtil.isNullOrNone(os[9])) {
                importShopDto.setSex(os[9].toString().trim());
            }
            if (os.length > 10 && !StringUtil.isNullOrNone(os[10])) {
                importShopDto.setLink(os[10].toString().trim());
            }
            if (os.length > 11 && !StringUtil.isNullOrNone(os[11])) {
                importShopDto.setIdCard(os[11].toString().trim());
            }
            if (os.length > 12 && !StringUtil.isNullOrNone(os[12])) {
                importShopDto.setOwnerTypeCd(os[12].toString().trim());
            }
            if (os.length > 13 && !StringUtil.isNullOrNone(os[13])) {
                importShopDto.setPersonType(os[13].toString().trim());
            }
            if (os.length > 14 && !StringUtil.isNullOrNone(os[14])) {
                importShopDto.setPersonRole(os[14].toString().trim());
            }
            if (os.length > 15 && !StringUtil.isNullOrNone(os[15])) {
                importShopDto.setContactPerson(os[15].toString().trim());
            }
            if (os.length > 16 && !StringUtil.isNullOrNone(os[16])) {
                importShopDto.setReserveLink(os[16].toString().trim());
            }
            shops.add(importShopDto);
        }
    }

    private void validateShopInfo(List<ImportShopDto> shops) {
        for (int shopIndex = 0; shopIndex < shops.size(); shopIndex++) {
            ImportShopDto importShopDto = shops.get(shopIndex);

            if (hasSpecialCharacters(importShopDto.getShopNum())) {
                throw new IllegalArgumentException((shopIndex + 2) + "行商铺编号中包含特殊符号 -  #  ？ & 请删除！");
            }

            if (StringUtil.isEmpty(importShopDto.getLocationName())) {
                throw new IllegalArgumentException((shopIndex + 2) + "行商铺位置不能为空");
            }

            if (StringUtil.isEmpty(importShopDto.getBuiltUpArea())) {
                throw new IllegalArgumentException((shopIndex + 2) + "行建筑面积不能为空");
            }

            String state = importShopDto.getState();
            if (!BuildingShopDto.STATE_RENT.equals(state) && !BuildingShopDto.STATE_SELL.equals(state) && !BuildingShopDto.STATE_FREE.equals(state)) {
                throw new IllegalArgumentException((shopIndex + 2) + "行状态不正确，只能是空闲(2008)、已出租(2006)或已出售(2007)");
            }

            for (int preIndex = 0; preIndex < shopIndex; preIndex++) {
                ImportShopDto preDto = shops.get(preIndex);
                if (preDto.getShopNum().equals(importShopDto.getShopNum())
                        && preDto.getLocationName().equals(importShopDto.getLocationName())) {
                    throw new IllegalArgumentException((shopIndex + 2) + "行商铺编号 " + importShopDto.getShopNum() + " 在商铺位置 " + importShopDto.getLocationName() + " 下已存在");
                }
            }
        }
    }
}
