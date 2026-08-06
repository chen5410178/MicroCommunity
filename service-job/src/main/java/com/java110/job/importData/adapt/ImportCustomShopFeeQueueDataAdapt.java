package com.java110.job.importData.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.importData.ImportCustomCreateFeeDto;
import com.java110.dto.log.AssetImportLogDetailDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.shop.BuildingShopDto;
import com.java110.dto.shop.BuildingShopOwnerDto;
import com.java110.intf.community.IBuildingShopOwnerInnerServiceSMO;
import com.java110.intf.community.IBuildingShopV1InnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IPayFeeRuleV1InnerServiceSMO;
import com.java110.intf.fee.IRuleGeneratorPayFeeBillV1InnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.job.importData.DefaultImportData;
import com.java110.job.importData.IImportDataAdapt;
import com.java110.po.payFee.PayFeeRulePo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("importCustomShopFeeQueueData")
public class ImportCustomShopFeeQueueDataAdapt extends DefaultImportData implements IImportDataAdapt {

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IBuildingShopV1InnerServiceSMO buildingShopV1InnerServiceSMOImpl;

    @Autowired
    private IBuildingShopOwnerInnerServiceSMO buildingShopOwnerInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IPayFeeRuleV1InnerServiceSMO payFeeRuleV1InnerServiceSMOImpl;

    @Autowired
    private IRuleGeneratorPayFeeBillV1InnerServiceSMO ruleGeneratorPayFeeBillV1InnerServiceSMOImpl;

    @Override
    public void importData(List<AssetImportLogDetailDto> assetImportLogDetailDtos) {
        for (AssetImportLogDetailDto assetImportLogDetailDto : assetImportLogDetailDtos) {
            try {
                doImportData(assetImportLogDetailDto);
                updateImportLogDetailState(assetImportLogDetailDto.getDetailId());
            } catch (Exception e) {
                e.printStackTrace();
                updateImportLogDetailState(assetImportLogDetailDto.getDetailId(), e);
            }
        }
    }

    private void doImportData(AssetImportLogDetailDto assetImportLogDetailDto) {
        JSONObject data = JSONObject.parseObject(assetImportLogDetailDto.getContent());
        ImportCustomCreateFeeDto importDto = new ImportCustomCreateFeeDto();
        importDto.setObjName(data.getString("objName"));
        importDto.setObjType(data.getString("objType"));
        importDto.setConfigId(data.getString("configId"));
        importDto.setConfigName(data.getString("configName"));
        importDto.setCreateTime(data.getString("createTime"));
        importDto.setStartTime(data.getString("startTime"));
        importDto.setEndTime(data.getString("endTime"));
        importDto.setCommunityId(data.getString("communityId"));
        importDto.setBatchId(data.getString("batchId"));
        importDto.setUserId(data.getString("userId"));
        importDto.setStoreId(data.getString("storeId"));

        if (!importDto.getEndTime().contains(":")) {
            importDto.setEndTime(importDto.getEndTime() + " 23:59:59");
        }

        doImportShopCreateFee(importDto);
    }

    private void doImportShopCreateFee(ImportCustomCreateFeeDto importDto) {
        BuildingShopDto shopDto = new BuildingShopDto();
        shopDto.setCommunityId(importDto.getCommunityId());
        shopDto.setShopNum(importDto.getObjName());
        List<BuildingShopDto> shopDtos = buildingShopV1InnerServiceSMOImpl.queryBuildingShops(shopDto);

        if (shopDtos == null || shopDtos.size() < 1) {
            throw new IllegalArgumentException("商铺不存在：" + importDto.getObjName());
        }

        BuildingShopDto shop = shopDtos.get(0);

        //todo 商铺补查业主信息，未绑定业主时置空
        BuildingShopOwnerDto shopOwnerDto = new BuildingShopOwnerDto();
        shopOwnerDto.setShopId(shop.getShopId());
        List<BuildingShopOwnerDto> shopOwnerDtos = buildingShopOwnerInnerServiceSMOImpl.queryBuildingShopOwners(shopOwnerDto);
        if (!ListUtil.isNull(shopOwnerDtos)) {
            OwnerDto ownerDto = new OwnerDto();
            ownerDto.setOwnerId(shopOwnerDtos.get(0).getOwnerId());
            List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
            if (!ListUtil.isNull(ownerDtos)) {
                shop.setOwnerId(ownerDtos.get(0).getOwnerId());
                shop.setOwnerName(ownerDtos.get(0).getName());
            }
        }

        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setConfigId(importDto.getConfigId());
        feeConfigDto.setCommunityId(importDto.getCommunityId());
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        Assert.listOnlyOne(feeConfigDtos, "查询费用项错误！");

        PayFeeRulePo payFeeRulePo = createPayFeeRulePo(shop, importDto, feeConfigDtos.get(0));

        List<PayFeeRulePo> payFeeRulePos = new ArrayList<>();
        payFeeRulePos.add(payFeeRulePo);

        payFeeRuleV1InnerServiceSMOImpl.savePayFeeRules(payFeeRulePos);

        ruleGeneratorPayFeeBillV1InnerServiceSMOImpl.rulesGeneratePayFees(payFeeRulePos);
    }

    private PayFeeRulePo createPayFeeRulePo(BuildingShopDto shopDto, ImportCustomCreateFeeDto importDto, FeeConfigDto feeConfigDto) {
        String time = importDto.getStartTime();

        PayFeeRulePo payFeeRulePo = new PayFeeRulePo();
        payFeeRulePo.setMaxTime(importDto.getEndTime());
        payFeeRulePo.setAmount("-1");
        payFeeRulePo.setFeeTypeCd(feeConfigDto.getFeeTypeCd());
        payFeeRulePo.setBatchId(importDto.getBatchId());
        payFeeRulePo.setUserId("-1");
        payFeeRulePo.setIncomeObjId(importDto.getStoreId());
        payFeeRulePo.setConfigId(feeConfigDto.getConfigId());
        payFeeRulePo.setFeeFlag(feeConfigDto.getFeeFlag());
        payFeeRulePo.setStartTime(time);
        payFeeRulePo.setEndTime(time);
        payFeeRulePo.setDeductFrom(feeConfigDto.getDeductFrom());
        payFeeRulePo.setState(FeeDto.STATE_DOING);
        payFeeRulePo.setRuleId(GenerateCodeFactory.getGeneratorId("11"));
        payFeeRulePo.setCommunityId(importDto.getCommunityId());
        payFeeRulePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_SHOP);
        payFeeRulePo.setPayerObjId(shopDto.getShopId());
        payFeeRulePo.setOwnerId(shopDto.getOwnerId());
        payFeeRulePo.setOwnerName(shopDto.getOwnerName());
        payFeeRulePo.setPayerObjName(importDto.getObjName());

        String curYearMonth = DateUtil.getFormatTimeStringB(DateUtil.getDateFromStringB(payFeeRulePo.getEndTime()));
        if (FeeConfigDto.PAYMENT_CD_AFTER.equals(feeConfigDto.getPaymentCd())) {
            int ruleCycle = Integer.parseInt(feeConfigDto.getPaymentCycle());
            java.util.Date endTime = DateUtil.getNextMonthDateB(payFeeRulePo.getEndTime(), ruleCycle);
            curYearMonth = DateUtil.getFormatTimeStringB(endTime);
        }
        payFeeRulePo.setCurYearMonth(curYearMonth);

        return payFeeRulePo;
    }
}
