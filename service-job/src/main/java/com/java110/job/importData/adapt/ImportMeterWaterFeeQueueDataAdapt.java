package com.java110.job.importData.adapt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.contract.ContractRoomDto;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.fee.SaveMeterWaterFeeDto;
import com.java110.dto.log.AssetImportLogDetailDto;
import com.java110.dto.meter.ImportExportMeterWaterDto;
import com.java110.dto.meter.MeterWaterDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.payFee.PayFeeDetailRefreshFeeMonthDto;
import com.java110.dto.room.RoomDto;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.*;
import com.java110.intf.store.IContractRoomInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.job.importData.DefaultImportData;
import com.java110.job.importData.IImportDataAdapt;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.meter.MeterWaterPo;
import com.java110.po.payFee.PayFeeRulePo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("importMeterWaterFeeQueueData")
public class ImportMeterWaterFeeQueueDataAdapt extends DefaultImportData implements IImportDataAdapt {

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IContractRoomInnerServiceSMO contractRoomInnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IPayFeeMonthInnerServiceSMO payFeeMonthInnerServiceSMOImpl;

    @Autowired
    private ISaveMeterWaterFeeInnerServiceSMO saveMeterWaterFeeInnerServiceSMOImpl;

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
        ImportExportMeterWaterDto importExportMeterWaterDto = BeanConvertUtil.covertBean(data, ImportExportMeterWaterDto.class);

        String communityId = importExportMeterWaterDto.getCommunityId();
        String storeId = importExportMeterWaterDto.getStoreId();
        String configId = importExportMeterWaterDto.getConfigId();
        String userId = importExportMeterWaterDto.getUserId();
        String feeTypeCd = importExportMeterWaterDto.getFeeTypeCd();
        String batchId = importExportMeterWaterDto.getBatchId();
        String meterType = importExportMeterWaterDto.getMeterType();

        List<FeeAttrPo> feeAttrPos = new ArrayList<>();

        SaveMeterWaterFeeDto saveMeterWaterFeeDto = dealImportExportMeterWater(importExportMeterWaterDto,
                communityId,
                storeId,
                configId,
                userId,
                feeTypeCd,
                feeAttrPos,
                batchId,
                meterType
        );

        if (saveMeterWaterFeeDto == null || saveMeterWaterFeeDto.getMeterWaterPo() == null
                || saveMeterWaterFeeDto.getPayFeeRulePo() == null) {
            throw new IllegalArgumentException("批量抄表失败");
        }

        //todo 统一走"抄表 -> 规则 -> 账单"标准链路（与手动抄表一致）
        int flag = saveMeterWaterFeeInnerServiceSMOImpl.saveMeterWaterAndFee(saveMeterWaterFeeDto);
        if (flag < 1) {
            throw new IllegalArgumentException("批量抄表失败");
        }

        //todo 按规则ID反查费用ID（rulesGeneratePayFees 生成 pay_fee，rule_id 与 meter_water.fee_id 一对一）
        FeeDto feeDto = new FeeDto();
        feeDto.setRuleId(saveMeterWaterFeeDto.getPayFeeRulePo().getRuleId());
        feeDto.setCommunityId(communityId);
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
        if (feeDtos == null || feeDtos.size() < 1) {
            throw new IllegalArgumentException("批量抄表失败,未查询到费用");
        }
        String feeId = feeDtos.get(0).getFeeId();

        //todo 保存导入特有属性（feeId 需落库反查后设置）
        if (feeAttrPos.size() > 0) {
            for (FeeAttrPo feeAttrPo : feeAttrPos) {
                feeAttrPo.setFeeId(feeId);
            }
            feeAttrInnerServiceSMOImpl.saveFeeAttrs(feeAttrPos);
        }

        // todo 这里异步的方式计算 月数据 和欠费数据
        PayFeeDetailRefreshFeeMonthDto payFeeDetailRefreshFeeMonthDto = new PayFeeDetailRefreshFeeMonthDto();
        payFeeDetailRefreshFeeMonthDto.setCommunityId(communityId);
        payFeeDetailRefreshFeeMonthDto.setFeeId(feeId);
        payFeeMonthInnerServiceSMOImpl.doGeneratorOrRefreshFeeMonth(payFeeDetailRefreshFeeMonthDto);
    }

    private SaveMeterWaterFeeDto dealImportExportMeterWater(ImportExportMeterWaterDto importExportMeterWaterDto, String communityId,
                                            String storeId, String configId, String userId, String feeTypeCd,
                                            List<FeeAttrPo> feeAttrPos,
                                            String batchId, String meterType) {

        RoomDto roomDto = new RoomDto();
        roomDto.setCommunityId(communityId);
        roomDto.setFloorNum(importExportMeterWaterDto.getFloorNum());
        roomDto.setUnitNum(importExportMeterWaterDto.getUnitNum());
        roomDto.setRoomNum(importExportMeterWaterDto.getRoomNum());
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

        Assert.listOnlyOne(roomDtos, "房屋未找到或找到多条" + importExportMeterWaterDto.getFloorNum() + "-" + importExportMeterWaterDto.getUnitNum() + "-" + importExportMeterWaterDto.getRoomNum());

        String roomName = importExportMeterWaterDto.getFloorNum() + "-" + importExportMeterWaterDto.getUnitNum() + "-" + importExportMeterWaterDto.getRoomNum();

        importExportMeterWaterDto.setMeterType(meterType);
        //查询房屋是否有合同
        ContractRoomDto contractRoomDto = new ContractRoomDto();

        contractRoomDto.setRoomId(roomDtos.get(0).getRoomId());

        List<ContractRoomDto> contractRoomDtos = contractRoomInnerServiceSMOImpl.queryContractRooms(contractRoomDto);

        //todo 生成规则ID（与手动抄表 SaveMeterWaterCmd 一致）
        String ruleId = GenerateCodeFactory.getGeneratorId("11");
        String objId = roomDtos.get(0).getRoomId();
        String payerObjType = FeeDto.PAYER_OBJ_TYPE_ROOM;
        String payerObjId = objId;

        if (contractRoomDtos != null && contractRoomDtos.size() > 0) {
            payerObjId = contractRoomDtos.get(0).getContractId();
            payerObjType = FeeDto.PAYER_OBJ_TYPE_CONTRACT;
            FeeAttrPo feeAttrPo = new FeeAttrPo();
            feeAttrPo.setCommunityId(communityId);
            feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId, true));
            feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_IMPORT_FEE_NAME);
            String feeName = roomName;
            if ("1010".equals(importExportMeterWaterDto.getMeterType())) {
                feeName += "水费";
            } else if ("2020".equals(importExportMeterWaterDto.getMeterType())) {
                feeName += "电费";
            } else {
                feeName += "燃气费";
            }
            feeAttrPo.setValue(feeName);
            feeAttrPos.add(feeAttrPo);
        }

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(communityId);
        ownerDto.setRoomId(roomDtos.get(0).getRoomId());
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnersByRoom(ownerDto);

        //todo 构建抄表记录（fee_id = ruleId，与手动抄表一致）
        MeterWaterPo meterWaterPo = new MeterWaterPo();
        meterWaterPo.setCommunityId(communityId);
        meterWaterPo.setCurDegrees(importExportMeterWaterDto.getCurDegrees());
        meterWaterPo.setCurReadingTime(importExportMeterWaterDto.getCurReadingTime());
        meterWaterPo.setFeeId(ruleId);
        meterWaterPo.setMeterType(importExportMeterWaterDto.getMeterType());
        meterWaterPo.setObjId(objId);
        meterWaterPo.setObjName(roomName);
        meterWaterPo.setObjType(MeterWaterDto.OBJ_TYPE_ROOM);
        meterWaterPo.setPreDegrees(importExportMeterWaterDto.getPreDegrees());
        meterWaterPo.setPreReadingTime(importExportMeterWaterDto.getPreReadingTime());
        meterWaterPo.setWaterId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_waterId, true));
        meterWaterPo.setRemark(importExportMeterWaterDto.getRemark());
        meterWaterPo.setPrice(importExportMeterWaterDto.getPrice());
        meterWaterPo.setbId("-1");

        //todo 构建费用规则（参照手动抄表 SaveMeterWaterCmd）
        String maxTime = importExportMeterWaterDto.getCurReadingTime();
        if (maxTime != null && maxTime.length() == 10) {
            maxTime += " 23:59:59";
        }
        PayFeeRulePo payFeeRulePo = new PayFeeRulePo();
        payFeeRulePo.setMaxTime(maxTime);
        payFeeRulePo.setAmount("-1");
        payFeeRulePo.setFeeTypeCd(feeTypeCd);
        payFeeRulePo.setCurYearMonth(importExportMeterWaterDto.getPreReadingTime().split(" ")[0]);
        payFeeRulePo.setBatchId(batchId);
        payFeeRulePo.setUserId(userId);
        payFeeRulePo.setIncomeObjId(storeId);
        payFeeRulePo.setConfigId(configId);
        payFeeRulePo.setFeeFlag(FeeDto.FEE_FLAG_ONCE);
        payFeeRulePo.setStartTime(importExportMeterWaterDto.getPreReadingTime());
        payFeeRulePo.setEndTime(importExportMeterWaterDto.getPreReadingTime());
        payFeeRulePo.setState(FeeDto.STATE_DOING);
        payFeeRulePo.setRuleId(ruleId);
        payFeeRulePo.setCommunityId(communityId);
        payFeeRulePo.setPayerObjType(payerObjType);
        payFeeRulePo.setPayerObjId(payerObjId);
        //todo 抄表建费补查业主信息，未绑定业主时置空
        if (ownerDtos != null && ownerDtos.size() > 0) {
            payFeeRulePo.setOwnerId(ownerDtos.get(0).getOwnerId());
            payFeeRulePo.setOwnerName(ownerDtos.get(0).getName());
        }
        payFeeRulePo.setPayerObjName(roomName);

        SaveMeterWaterFeeDto saveMeterWaterFeeDto = new SaveMeterWaterFeeDto();
        saveMeterWaterFeeDto.setMeterWaterPo(meterWaterPo);
        saveMeterWaterFeeDto.setPayFeeRulePo(payFeeRulePo);
        return saveMeterWaterFeeDto;
    }
}
