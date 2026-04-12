package com.java110.job.importData.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.importData.ImportShopDto;
import com.java110.dto.log.AssetImportLogDetailDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.shop.BuildingShopDto;
import com.java110.dto.shop.ShopLocationDto;
import com.java110.intf.community.IBuildingShopOwnerInnerServiceSMO;
import com.java110.intf.community.IBuildingShopV1InnerServiceSMO;
import com.java110.intf.community.IShopLocationInnerServiceSMO;
import com.java110.intf.community.IShopLocationV1InnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.job.importData.DefaultImportData;
import com.java110.job.importData.IImportDataAdapt;
import com.java110.po.owner.OwnerPo;
import com.java110.po.shop.BuildingShopOwnerPo;
import com.java110.po.shop.BuildingShopPo;
import com.java110.po.shop.ShopLocationPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service("importShopQueueData")
public class ImportShopQueueDataAdapt extends DefaultImportData implements IImportDataAdapt {

    @Autowired
    private IBuildingShopV1InnerServiceSMO buildingShopV1InnerServiceSMOImpl;

    @Autowired
    private IBuildingShopOwnerInnerServiceSMO buildingShopOwnerInnerServiceSMOImpl;

    @Autowired
    private IShopLocationInnerServiceSMO shopLocationInnerServiceSMOImpl;

    @Autowired
    private IShopLocationV1InnerServiceSMO shopLocationV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Override
    public void importData(List<AssetImportLogDetailDto> assetImportLogDetailDtos) {
        importDatas(assetImportLogDetailDtos);
    }

    private void importDatas(List<AssetImportLogDetailDto> infos) {
        for (AssetImportLogDetailDto assetImportLogDetailDto : infos) {
            try {
                doImportData(assetImportLogDetailDto);
                updateImportLogDetailState(assetImportLogDetailDto.getDetailId());
            } catch (Exception e) {
                e.printStackTrace();
                updateImportLogDetailState(assetImportLogDetailDto.getDetailId(), e);
            }
        }
    }

    private void doImportData(AssetImportLogDetailDto assetImportLogDetailDto) throws ParseException {
        JSONObject data = JSONObject.parseObject(assetImportLogDetailDto.getContent());
        ImportShopDto importShopDto = BeanConvertUtil.covertBean(data, ImportShopDto.class);

        String locationId = getOrCreateShopLocation(importShopDto.getLocationName(), importShopDto.getCommunityId());

        BuildingShopPo buildingShopPo = new BuildingShopPo();
        buildingShopPo.setShopId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_shopId));
        buildingShopPo.setShopNum(importShopDto.getShopNum());
        buildingShopPo.setLocationId(locationId);
        buildingShopPo.setBuiltUpArea(importShopDto.getBuiltUpArea());
        buildingShopPo.setShopArea(importShopDto.getShopArea());
        buildingShopPo.setState(StringUtil.isEmpty(importShopDto.getState()) ? BuildingShopDto.STATE_FREE : importShopDto.getState());
        buildingShopPo.setShopRent(StringUtil.isEmpty(importShopDto.getShopRent()) ? "0.00" : importShopDto.getShopRent());
        buildingShopPo.setCommunityId(importShopDto.getCommunityId());

        int flag = buildingShopV1InnerServiceSMOImpl.saveBuildingShop(buildingShopPo);
        if (flag < 1) {
            throw new IllegalArgumentException("导入失败");
        }

        if (!StringUtil.isEmpty(importShopDto.getOwnerName())) {
            String ownerId = getOrCreateOwner(importShopDto);
            saveShopOwner(importShopDto, buildingShopPo.getShopId(), ownerId);
        }
    }

    private String getOrCreateOwner(ImportShopDto importShopDto) throws ParseException {
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(importShopDto.getCommunityId());
        ownerDto.setLink(importShopDto.getLink());
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);

        if (ownerDtos != null && !ownerDtos.isEmpty()) {
            return ownerDtos.get(0).getOwnerId();
        }

        OwnerPo ownerPo = new OwnerPo();
        ownerPo.setOwnerId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ownerId));
        ownerPo.setName(importShopDto.getOwnerName());
        ownerPo.setLink(importShopDto.getLink());
        ownerPo.setIdCard(StringUtil.isEmpty(importShopDto.getIdCard()) ? "" : importShopDto.getIdCard());
        ownerPo.setOwnerTypeCd(StringUtil.isEmpty(importShopDto.getOwnerTypeCd()) ? "1002" : importShopDto.getOwnerTypeCd());
        ownerPo.setSex(StringUtil.isEmpty(importShopDto.getSex()) ? "0" : ("女".equals(importShopDto.getSex()) ? "1" : "0"));
        ownerPo.setCommunityId(importShopDto.getCommunityId());
        ownerPo.setMemberId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ownerId));
        ownerPo.setPersonType(importShopDto.getPersonType());
        ownerPo.setPersonRole(importShopDto.getPersonRole());
        ownerPo.setConcactPerson(importShopDto.getContactPerson());
        ownerPo.setConcactLink(importShopDto.getReserveLink());
        ownerPo.setRemark("商铺导入");
        ownerV1InnerServiceSMOImpl.saveOwner(ownerPo);

        return ownerPo.getOwnerId();
    }

    private void saveShopOwner(ImportShopDto importShopDto, String shopId, String ownerId) throws ParseException {
        BuildingShopOwnerPo buildingShopOwnerPo = new BuildingShopOwnerPo();
        buildingShopOwnerPo.setShopOwnerId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_shopId));
        buildingShopOwnerPo.setShopId(shopId);
        buildingShopOwnerPo.setOwnerId(ownerId);
        buildingShopOwnerPo.setOwnerName(importShopDto.getOwnerName());
        buildingShopOwnerPo.setLink(importShopDto.getLink());
        buildingShopOwnerPo.setState(importShopDto.getState());
        buildingShopOwnerPo.setCommunityId(importShopDto.getCommunityId());
        if (!StringUtil.isEmpty(importShopDto.getStartTime())) {
            buildingShopOwnerPo.setStartTime(parseDate(importShopDto.getStartTime()));
        }
        if (!StringUtil.isEmpty(importShopDto.getEndTime())) {
            buildingShopOwnerPo.setEndTime(parseDate(importShopDto.getEndTime()));
        }
        buildingShopOwnerInnerServiceSMOImpl.saveBuildingShopOwner(buildingShopOwnerPo);
    }

    private Date parseDate(String dateStr) throws ParseException {
        if (StringUtil.isEmpty(dateStr)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(dateStr);
    }

    private String getOrCreateShopLocation(String locationName, String communityId) {
        String[] parts = locationName.split("-");
        if (parts.length < 2) {
            throw new IllegalArgumentException("商铺位置格式不正确，应为 楼栋-单元 格式");
        }

        String parentId = "-1";

        for (int i = 0; i < parts.length; i++) {
            String name = parts[i];
            parentId = getOrCreateLocation(name, parentId, communityId, i + 1);
        }

        return parentId;
    }

    private String getOrCreateLocation(String locationName, String parentId, String communityId, int level) {
        ShopLocationDto shopLocationDto = new ShopLocationDto();
        shopLocationDto.setLocationName(locationName);
        shopLocationDto.setParentId(parentId);
        shopLocationDto.setCommunityId(communityId);
        List<ShopLocationDto> locations = shopLocationInnerServiceSMOImpl.queryShopLocations(shopLocationDto);

        if (locations != null && !locations.isEmpty()) {
            return locations.get(0).getLocationId();
        }

        ShopLocationPo shopLocationPo = new ShopLocationPo();
        shopLocationPo.setLocationId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_locationId));
        shopLocationPo.setLocationName(locationName);
        shopLocationPo.setParentId(parentId);
        shopLocationPo.setCommunityId(communityId);
        shopLocationPo.setLevel(level);
        shopLocationPo.setSeq(1);
        shopLocationV1InnerServiceSMOImpl.saveShopLocation(shopLocationPo);

        return shopLocationPo.getLocationId();
    }
}
