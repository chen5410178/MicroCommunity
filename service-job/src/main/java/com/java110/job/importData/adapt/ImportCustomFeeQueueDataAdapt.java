package com.java110.job.importData.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.contract.ContractDto;
import com.java110.dto.contract.ContractRoomDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.importData.ImportCustomCreateFeeDto;
import com.java110.dto.log.AssetImportLogDetailDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.*;
import com.java110.intf.store.IContractInnerServiceSMO;
import com.java110.intf.store.IContractRoomInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.job.importData.DefaultImportData;
import com.java110.job.importData.IImportDataAdapt;
import com.java110.po.payFee.PayFeeRulePo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("importCustomFeeQueueData")
public class ImportCustomFeeQueueDataAdapt extends DefaultImportData implements IImportDataAdapt {

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IPayFeeConfigV1InnerServiceSMO payFeeConfigV1InnerServiceSMOImpl;

    @Autowired
    private IContractInnerServiceSMO contractInnerServiceSMOImpl;

    @Autowired
    private IContractRoomInnerServiceSMO contractRoomInnerServiceSMOImpl;

    @Autowired
    private IPayFeeRuleV1InnerServiceSMO payFeeRuleV1InnerServiceSMOImpl;

    @Autowired
    private IRuleGeneratorPayFeeBillV1InnerServiceSMO ruleGeneratorPayFeeBillV1InnerServiceSMOImpl;

    //    @Autowired
//    private IPayFeeMonth payFeeMonthImpl;
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
        ImportCustomCreateFeeDto importExportMeterWaterDto = BeanConvertUtil.covertBean(data, ImportCustomCreateFeeDto.class);

        //todo 处理结束时间
        if (!importExportMeterWaterDto.getEndTime().contains(":")) {
            importExportMeterWaterDto.setEndTime(importExportMeterWaterDto.getEndTime() + " 23:59:59");
        }

        String communityId = importExportMeterWaterDto.getCommunityId();
        String batchId = importExportMeterWaterDto.getBatchId();

        doImportRoomCreateFee(importExportMeterWaterDto, batchId);
        doImportCarCreateFee(importExportMeterWaterDto, batchId);
        doImportContractCreateFee(importExportMeterWaterDto, batchId);


    }

    /**
     * 房屋创建费用
     *
     * @param importCustomCreateFeeDto
     * @param batchId
     */
    private void doImportRoomCreateFee(ImportCustomCreateFeeDto importCustomCreateFeeDto, String batchId) {
        if (!ImportCustomCreateFeeDto.TYPE_ROOM.equals(importCustomCreateFeeDto.getObjType())) {
            return;
        }
        List<ImportCustomCreateFeeDto> rooms = new ArrayList<>();
        String[] objNames;
        objNames = importCustomCreateFeeDto.getObjName().split("-", 3);
        if (objNames.length != 3) {
            throw new IllegalArgumentException("收费对象格式错误：" + importCustomCreateFeeDto.getObjName());
        }
        importCustomCreateFeeDto.setFloorNum(objNames[0]);
        importCustomCreateFeeDto.setUnitNum(objNames[1]);
        importCustomCreateFeeDto.setRoomNum(objNames[2]);
        rooms.add(importCustomCreateFeeDto);


        List<ImportCustomCreateFeeDto> importCustomCreateFeeDtos = roomInnerServiceSMOImpl.freshRoomIdsByImportCustomCreateFee(rooms);
        List<String> roomIds = new ArrayList<>();
        for (ImportCustomCreateFeeDto importRoomFee : importCustomCreateFeeDtos) {
            roomIds.add(importRoomFee.getPayObjId());
        }
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(importCustomCreateFeeDtos.get(0).getCommunityId());
        ownerDto.setRoomIds(roomIds.toArray(new String[roomIds.size()]));
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnersByRoom(ownerDto);
        for (ImportCustomCreateFeeDto importRoomFee : importCustomCreateFeeDtos) {
            for (OwnerDto tmpOwnerDto : ownerDtos) {
                if (importRoomFee.getPayObjId().equals(tmpOwnerDto.getRoomId())) {
                    importRoomFee.setOwnerId(tmpOwnerDto.getOwnerId());
                    importRoomFee.setOwnerName(tmpOwnerDto.getName());
                    importRoomFee.setOwnerLink(tmpOwnerDto.getLink());
                }
            }
        }
        doCreateFeeAndAttrs(importCustomCreateFeeDtos.get(0), batchId);
    }


    /**
     * 创建车位费用（objType=2002）
     * 按 停车场-停车区域-车位编号 定位车位，费用挂在车位（psId）上
     *
     * @param importCustomCreateFeeDto
     * @param batchId
     */
    private void doImportCarCreateFee(ImportCustomCreateFeeDto importCustomCreateFeeDto, String batchId) {
        if (!ImportCustomCreateFeeDto.TYPE_CAR.equals(importCustomCreateFeeDto.getObjType())) {
            return;
        }
        List<ImportCustomCreateFeeDto> parkingSpaces = new ArrayList<>();

        String[] objNames = importCustomCreateFeeDto.getObjName().split("-");
        if (objNames.length == 3) {
            importCustomCreateFeeDto.setAreaNum(objNames[0]);
            importCustomCreateFeeDto.setSectionName(objNames[1]);
            importCustomCreateFeeDto.setNum(objNames[2]);
        } else if (objNames.length == 2) {
            importCustomCreateFeeDto.setAreaNum(objNames[0]);
            importCustomCreateFeeDto.setNum(objNames[1]);
        } else {
            throw new IllegalArgumentException("收费对象格式错误：" + importCustomCreateFeeDto.getObjName());
        }

        parkingSpaces.add(importCustomCreateFeeDto);

        List<ImportCustomCreateFeeDto> importCustomCreateFeeDtos = parkingSpaceInnerServiceSMOImpl.freshPsIdsByImportCustomCreateFee(parkingSpaces);

        doCreateFeeAndAttrs(importCustomCreateFeeDtos.get(0), batchId);
    }

    /**
     * 创建合同费用（objType=3003）
     * 业主信息经 queryContractRooms 查合同关联房屋冗余字段，payerObjName 取合同名称
     *
     * @param importCustomCreateFeeDto
     * @param batchId
     */
    private void doImportContractCreateFee(ImportCustomCreateFeeDto importCustomCreateFeeDto, String batchId) {
        if (!"3003".equals(importCustomCreateFeeDto.getObjType())) {
            return;
        }
        String contractId = importCustomCreateFeeDto.getPayObjId();
        if (StringUtil.isEmpty(contractId)) {
            throw new IllegalArgumentException("合同不存在");
        }
        ContractRoomDto contractRoomDto = new ContractRoomDto();
        contractRoomDto.setContractId(contractId);
        List<ContractRoomDto> contractRoomDtos = contractRoomInnerServiceSMOImpl.queryContractRooms(contractRoomDto);
        if (!ListUtil.isNull(contractRoomDtos)) {
            importCustomCreateFeeDto.setOwnerId(contractRoomDtos.get(0).getOwnerId());
            importCustomCreateFeeDto.setOwnerName(contractRoomDtos.get(0).getOwnerName());
        }
        ContractDto contractDto = new ContractDto();
        contractDto.setContractId(contractId);
        List<ContractDto> contractDtos = contractInnerServiceSMOImpl.queryContracts(contractDto);
        if (ListUtil.isNull(contractDtos)) {
            throw new IllegalArgumentException("合同不存在，" + contractId);
        }
        importCustomCreateFeeDto.setObjName(contractDtos.get(0).getContractName());
        doCreateFeeAndAttrs(importCustomCreateFeeDto, batchId);
    }


    private void doCreateFeeAndAttrs(ImportCustomCreateFeeDto importCustomCreateFeeDto, String batchId) {
        if (StringUtil.isEmpty(importCustomCreateFeeDto.getPayObjId())) {
            throw new IllegalArgumentException("收费对象不存在");
        }
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(importCustomCreateFeeDto.getCommunityId());
        feeConfigDto.setConfigId(importCustomCreateFeeDto.getConfigId());
        List<FeeConfigDto> feeConfigDtos = payFeeConfigV1InnerServiceSMOImpl.queryPayFeeConfigs(feeConfigDto);
        if (ListUtil.isNull(feeConfigDtos)) {
            throw new IllegalArgumentException("费用项不存在");
        }

        PayFeeRulePo payFeeRulePo = createPayFeeRulePo(importCustomCreateFeeDto, batchId, feeConfigDtos.get(0));

        List<PayFeeRulePo> payFeeRulePos = new ArrayList<>();
        payFeeRulePos.add(payFeeRulePo);

        payFeeRuleV1InnerServiceSMOImpl.savePayFeeRules(payFeeRulePos);

        ruleGeneratorPayFeeBillV1InnerServiceSMOImpl.rulesGeneratePayFees(payFeeRulePos);
    }

    private PayFeeRulePo createPayFeeRulePo(ImportCustomCreateFeeDto importDto, String batchId, FeeConfigDto feeConfigDto) {
        String time = importDto.getStartTime();

        PayFeeRulePo payFeeRulePo = new PayFeeRulePo();
        payFeeRulePo.setRuleId(GenerateCodeFactory.getGeneratorId("11"));
        payFeeRulePo.setConfigId(feeConfigDto.getConfigId());
        payFeeRulePo.setCommunityId(importDto.getCommunityId());
        payFeeRulePo.setFeeTypeCd(feeConfigDto.getFeeTypeCd());
        payFeeRulePo.setFeeFlag(feeConfigDto.getFeeFlag());
        payFeeRulePo.setAmount("-1");
        payFeeRulePo.setStartTime(time);
        payFeeRulePo.setEndTime(time);
        if (!StringUtil.isEmpty(importDto.getEndTime())) {
            payFeeRulePo.setMaxTime(importDto.getEndTime());
        } else {
            payFeeRulePo.setMaxTime(feeConfigDto.getEndTime());
        }
        payFeeRulePo.setBatchId(batchId);
        payFeeRulePo.setUserId("-1");
        payFeeRulePo.setIncomeObjId(importDto.getStoreId());
        payFeeRulePo.setState(FeeDto.STATE_DOING);
        payFeeRulePo.setDeductFrom(feeConfigDto.getDeductFrom());
        payFeeRulePo.setOwnerId(importDto.getOwnerId());
        payFeeRulePo.setOwnerName(importDto.getOwnerName());
        payFeeRulePo.setPayerObjName(importDto.getObjName());

        payFeeRulePo.setPayerObjId(importDto.getPayObjId());
        if ("3003".equals(importDto.getObjType())) {
            payFeeRulePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_CONTRACT);
        } else if ("2002".equals(importDto.getObjType())) {
            payFeeRulePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_PARKING_SPACE);
        } else {
            payFeeRulePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
        }

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
