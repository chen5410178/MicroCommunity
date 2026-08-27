package com.java110.fee.bmo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.importData.ImportFeeDto;
import com.java110.dto.importData.ImportRoomFee;
import com.java110.dto.owner.OwnerDto;
import com.java110.fee.bmo.IImportRoomFee;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.*;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeeConfigPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.importFee.ImportFeePo;
import com.java110.po.importFee.ImportFeeDetailPo;
import com.java110.po.payFee.PayFeeRulePo;
import com.java110.utils.util.*;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 欠费缴费实现类
 */
@Service
public class ImportRoomFeeImpl implements IImportRoomFee {

    private static Logger logger = LoggerFactory.getLogger(ImportRoomFeeImpl.class);


    private static final String IMPORT_FEE_NAME = "导入费用";


    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IPayFeeRuleV1InnerServiceSMO payFeeRuleV1InnerServiceSMOImpl;


    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IImportFeeDetailInnerServiceSMO importFeeDetailInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IImportFeeInnerServiceSMO importFeeInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    /**
     * 欠费缴费
     *
     * @param reqJson
     * @return
     */
    @Override
    public ResponseEntity<String> importFee(JSONObject reqJson) {

        int successCount = 0;
        int errorCount = 0;

        //小区ID
        String communityId = reqJson.getString("communityId");
        String importFeeId = reqJson.getString("importFeeId");
        String feeTypeCd = reqJson.getString("feeTypeCd");//费用大类
        String storeId = reqJson.getString("storeId");
        String userId = reqJson.getString("userId");
        String feeName = reqJson.getString("feeName");
        String batchId = reqJson.getString("batchId");


        JSONArray importRoomFees = reqJson.getJSONArray("importRoomFees");

        List<ImportRoomFee> tmpImportRoomFees = importRoomFees.toJavaList(ImportRoomFee.class);

        if (ListUtil.isNull(tmpImportRoomFees)) {
            throw new IllegalArgumentException("未包含导入费用");
        }

        if (StringUtil.isEmpty(feeName)) {
            feeName = tmpImportRoomFees.get(0).getFeeName();
        }

        if (StringUtil.isEmpty(feeName)) {
            feeName = IMPORT_FEE_NAME;
        }

        if (!StringUtil.isEmpty(feeName)) {
            feeName = feeName.trim();
        }

        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setFeeTypeCd(feeTypeCd);
        if (!StringUtil.isEmpty(tmpImportRoomFees.get(0).getConfigId())) {
            feeConfigDto.setConfigId(tmpImportRoomFees.get(0).getConfigId());
        } else {
            feeConfigDto.setFeeNameEq(feeName);
        }
        feeConfigDto.setComputingFormula(FeeConfigDto.COMPUTING_FORMULA_DYNAMIC);
        feeConfigDto.setCommunityId(communityId);
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        // 根据费用大类 判断是否有存在 费用导入收入项
        if (ListUtil.isNull(feeConfigDtos)) {
            throw new IllegalArgumentException("费用项不存在");
        }
        feeConfigDto.setConfigId(feeConfigDtos.get(0).getConfigId());

        for (ImportRoomFee tmpImportRoomFee : tmpImportRoomFees) {
            tmpImportRoomFee.setCommunityId(communityId);
        }

        Assert.isNotNull(tmpImportRoomFees, "参数错误，未包含处理费用");

        //todo 旧导入链路补刷房屋ID与业主信息，禁止前端透传业主
        List<ImportRoomFee> freshRoomFees = roomInnerServiceSMOImpl.freshRoomIds(tmpImportRoomFees);
        List<String> roomIds = new ArrayList<>();
        for (ImportRoomFee tmpImportRoomFee : freshRoomFees) {
            if (StringUtil.isEmpty(tmpImportRoomFee.getRoomId())) {
                throw new IllegalArgumentException("房屋不存在，" + tmpImportRoomFee.getFloorNum() + "-"
                        + tmpImportRoomFee.getUnitNum() + "-" + tmpImportRoomFee.getRoomNum());
            }
            roomIds.add(tmpImportRoomFee.getRoomId());
        }
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(communityId);
        ownerDto.setRoomIds(roomIds.toArray(new String[roomIds.size()]));
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnersByRoom(ownerDto);
        for (ImportRoomFee tmpImportRoomFee : freshRoomFees) {
            for (OwnerDto tmpOwnerDto : ownerDtos) {
                if (tmpImportRoomFee.getRoomId().equals(tmpOwnerDto.getRoomId())) {
                    tmpImportRoomFee.setOwnerId(tmpOwnerDto.getOwnerId());
                    tmpImportRoomFee.setOwnerName(tmpOwnerDto.getName());
                    tmpImportRoomFee.setOwnerLink(tmpOwnerDto.getLink());
                }
            }
        }

        List<PayFeeRulePo> payFeeRulePos = new ArrayList<>();
        List<ImportFeeDetailPo> importFeeDetailPos = new ArrayList<>();
        PayFeeRulePo payFeeRulePo = null;
        ImportFeeDetailPo importFeeDetailPo = null;
        String endTime = "";
        for (ImportRoomFee importRoomFee : tmpImportRoomFees) {
            endTime = importRoomFee.getEndTime();
            if (!endTime.contains(":")) {
                endTime += " 23:59:59";
            }
            successCount++;
            payFeeRulePo = new PayFeeRulePo();
            payFeeRulePo.setMaxTime(endTime);
            payFeeRulePo.setAmount(importRoomFee.getAmount());
            payFeeRulePo.setFeeTypeCd(feeTypeCd);
            payFeeRulePo.setCurYearMonth(importRoomFee.getStartTime());
            payFeeRulePo.setBatchId(batchId);
            payFeeRulePo.setUserId(userId);
            payFeeRulePo.setIncomeObjId(storeId);
            payFeeRulePo.setConfigId(feeConfigDto.getConfigId());
            payFeeRulePo.setFeeFlag(FeeDto.FEE_FLAG_ONCE);
            payFeeRulePo.setStartTime(importRoomFee.getStartTime());
            payFeeRulePo.setEndTime(importRoomFee.getStartTime());
            payFeeRulePo.setState(FeeDto.STATE_DOING);
            payFeeRulePo.setRuleId(GenerateCodeFactory.getGeneratorId("11"));
            payFeeRulePo.setCommunityId(communityId);
            payFeeRulePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
            payFeeRulePo.setPayerObjId(importRoomFee.getRoomId());
            //todo 业主信息已通过 queryOwnersByRoom 批量刷入，禁止前端透传
            payFeeRulePo.setOwnerId(importRoomFee.getOwnerId());
            payFeeRulePo.setOwnerName(importRoomFee.getOwnerName());
            payFeeRulePo.setPayerObjName(StringUtil.isEmpty(importRoomFee.getPayerObjName())
                    ? (!"0".equals(importRoomFee.getUnitNum())
                        ? importRoomFee.getFloorNum() + "栋" + importRoomFee.getUnitNum() + "单元" + importRoomFee.getRoomNum() + "室"
                        : importRoomFee.getFloorNum() + "栋" + importRoomFee.getRoomNum() + "室")
                    : importRoomFee.getPayerObjName());

            payFeeRulePos.add(payFeeRulePo);

            importFeeDetailPo = new ImportFeeDetailPo();
            importFeeDetailPo.setAmount(importRoomFee.getAmount());
            importFeeDetailPo.setCommunityId(communityId);
            importFeeDetailPo.setEndTime(importRoomFee.getEndTime());
            importFeeDetailPo.setFeeId(payFeeRulePo.getRuleId());
            importFeeDetailPo.setFeeName(importRoomFee.getFeeName());
            importFeeDetailPo.setFloorNum(importRoomFee.getFloorNum());
            importFeeDetailPo.setUnitNum(importRoomFee.getUnitNum());
            importFeeDetailPo.setRoomNum(importRoomFee.getRoomNum());
            importFeeDetailPo.setRoomId(importRoomFee.getRoomId());
            importFeeDetailPo.setObjId(importRoomFee.getRoomId());
            importFeeDetailPo.setObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
            importFeeDetailPo.setObjName(!"0".equals(importRoomFee.getUnitNum())
                    ? importRoomFee.getFloorNum() + "栋" + importRoomFee.getUnitNum() + "单元" + importRoomFee.getRoomNum() + "室" :
                    importRoomFee.getFloorNum() + "栋" + importRoomFee.getRoomNum() + "室"
            );
            importFeeDetailPo.setStartTime(importRoomFee.getStartTime());
            importFeeDetailPo.setIfdId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_IfdId, true));
            importFeeDetailPo.setState("1000");
            importFeeDetailPo.setImportFeeId(importFeeId);
            importFeeDetailPos.add(importFeeDetailPo);
        }

        payFeeRuleV1InnerServiceSMOImpl.savePayFeeRules(payFeeRulePos);

        // databus 通知 生成账单



        ImportFeeDto importFeeDto = new ImportFeeDto();
        importFeeDto.setCommunityId(communityId);
        importFeeDto.setImportFeeId(importFeeId);

        List<ImportFeeDto> importRoomFeess = importFeeInnerServiceSMOImpl.queryImportFees(importFeeDto);

        if (ListUtil.isNull(importRoomFeess)) {
            //保存日志
            ImportFeePo importFeePo = new ImportFeePo();
            importFeePo.setCommunityId(communityId);
            importFeePo.setFeeTypeCd(feeTypeCd);
            importFeePo.setImportFeeId(importFeeId);
            importFeeInnerServiceSMOImpl.saveImportFee(importFeePo);
        }


        importFeeDetailInnerServiceSMOImpl.saveImportFeeDetails(importFeeDetailPos);


        JSONObject data = new JSONObject();
        data.put("successCount", successCount);
        data.put("errorCount", errorCount);

        return ResultVo.createResponseEntity(data);
    }

    /**
     * 车位费用导入
     *
     * @param reqJson
     * @return
     */
    @Override
    public ResponseEntity<String> importParkingSpaceFee(JSONObject reqJson) {

        int successCount = 0;
        int errorCount = 0;

        //小区ID
        String communityId = reqJson.getString("communityId");
        String importFeeId = reqJson.getString("importFeeId");
        String feeTypeCd = reqJson.getString("feeTypeCd");//费用大类
        String storeId = reqJson.getString("storeId");
        String userId = reqJson.getString("userId");
        String feeName = reqJson.getString("feeName");
        String batchId = reqJson.getString("batchId");
        JSONArray importParkingSpaceFees = reqJson.getJSONArray("importParkingSpaceFees");

        List<ImportRoomFee> tmpImportParkingSpaceFees = importParkingSpaceFees.toJavaList(ImportRoomFee.class);
        if (StringUtil.isEmpty(feeName)) {
            feeName = IMPORT_FEE_NAME;
        }
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setFeeTypeCd(feeTypeCd);
        if (!StringUtil.isEmpty(tmpImportParkingSpaceFees.get(0).getConfigId())) {
            feeConfigDto.setConfigId(tmpImportParkingSpaceFees.get(0).getConfigId());
        } else {
            feeConfigDto.setFeeNameEq(feeName);
        }
        feeConfigDto.setComputingFormula(FeeConfigDto.COMPUTING_FORMULA_DYNAMIC);
        feeConfigDto.setCommunityId(communityId);
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        // 根据费用大类 判断是否有存在 费用导入收入项
        if (ListUtil.isNull(feeConfigDtos)) {
            //生成导入费
            feeConfigDto.setConfigId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_configId));
            saveFeeConfig(feeConfigDto);
        } else {
            feeConfigDto.setConfigId(feeConfigDtos.get(0).getConfigId());
        }


        for (ImportRoomFee tmpImportParkingSpaceFee : tmpImportParkingSpaceFees) {
            tmpImportParkingSpaceFee.setCommunityId(communityId);
        }

        Assert.isNotNull(tmpImportParkingSpaceFees, "参数错误，未包含处理费用");

        tmpImportParkingSpaceFees = ownerCarInnerServiceSMOImpl.freshCarIds(tmpImportParkingSpaceFees);

        List<PayFeePo> payFeePos = new ArrayList<>();
        List<FeeAttrPo> feeAttrPos = new ArrayList<>();
        List<ImportFeeDetailPo> importFeeDetailPos = new ArrayList<>();
        PayFeePo payFeePo = null;
        ImportFeeDetailPo importFeeDetailPo = null;
        String endTime = "";
        for (ImportRoomFee importParkingSpaceFee : tmpImportParkingSpaceFees) {
            if (StringUtil.isEmpty(importParkingSpaceFee.getCarId()) || importParkingSpaceFee.getCarId().startsWith("-") ||
                    StringUtil.isEmpty(importParkingSpaceFee.getCarNum()) || importParkingSpaceFee.getCarNum().startsWith("-")
            ) {
                errorCount++;
                continue;
            }
            endTime = importParkingSpaceFee.getEndTime();
            if (!endTime.contains(":")) {
                endTime += " 23:59:59";
            }
            successCount++;
            payFeePo = new PayFeePo();
            payFeePo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
            payFeePo.setEndTime(importParkingSpaceFee.getStartTime());
            payFeePo.setState(FeeDto.STATE_DOING);
            payFeePo.setCommunityId(communityId);
            payFeePo.setConfigId(feeConfigDto.getConfigId());
            payFeePo.setPayerObjId(importParkingSpaceFee.getCarId());
            payFeePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_PARKING_SPACE);
            payFeePo.setUserId(userId);
            payFeePo.setIncomeObjId(storeId);
            payFeePo.setFeeTypeCd(feeTypeCd);
            payFeePo.setFeeFlag(FeeDto.FEE_FLAG_ONCE);
            payFeePo.setAmount(importParkingSpaceFee.getAmount());
            payFeePo.setBatchId(batchId);
            payFeePo.setStartTime(importParkingSpaceFee.getCreateTime());
           // payFeePo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            payFeePo.setFeeTime(DateUtil.getFormatTimeStringB(DateUtil.getDateFromStringA(importParkingSpaceFee.getCreateTime())));

            payFeePos.add(payFeePo);

            FeeAttrPo feeAttrPo = new FeeAttrPo();
            feeAttrPo.setCommunityId(communityId);
            feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
            feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_IMPORT_FEE_NAME);
            feeAttrPo.setValue(feeName);
            feeAttrPo.setFeeId(payFeePo.getFeeId());
            feeAttrPos.add(feeAttrPo);


            feeAttrPo = new FeeAttrPo();
            feeAttrPo.setCommunityId(communityId);
            feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
            feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_ONCE_FEE_DEADLINE_TIME);
            feeAttrPo.setValue(endTime);
            feeAttrPo.setFeeId(payFeePo.getFeeId());
            feeAttrPos.add(feeAttrPo);


            if (!StringUtil.isEmpty(importParkingSpaceFee.getOwnerId())) {
                feeAttrPo = new FeeAttrPo();
                feeAttrPo.setCommunityId(communityId);
                feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
                feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_ID);
                feeAttrPo.setValue(importParkingSpaceFee.getOwnerId());
                feeAttrPo.setFeeId(payFeePo.getFeeId());
                feeAttrPos.add(feeAttrPo);

                feeAttrPo = new FeeAttrPo();
                feeAttrPo.setCommunityId(communityId);
                feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
                feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_NAME);
                feeAttrPo.setValue(importParkingSpaceFee.getOwnerName());
                feeAttrPo.setFeeId(payFeePo.getFeeId());
                feeAttrPos.add(feeAttrPo);

                feeAttrPo = new FeeAttrPo();
                feeAttrPo.setCommunityId(communityId);
                feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
                feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_LINK);
                feeAttrPo.setValue(importParkingSpaceFee.getOwnerLink());
                feeAttrPo.setFeeId(payFeePo.getFeeId());
                feeAttrPos.add(feeAttrPo);
            }

            importFeeDetailPo = new ImportFeeDetailPo();
            importFeeDetailPo.setAmount(importParkingSpaceFee.getAmount());
            importFeeDetailPo.setCommunityId(communityId);
            importFeeDetailPo.setEndTime(importParkingSpaceFee.getEndTime());
            importFeeDetailPo.setFeeId(payFeePo.getFeeId());
            importFeeDetailPo.setFeeName(importParkingSpaceFee.getFeeName());
            importFeeDetailPo.setFloorNum("-");
            importFeeDetailPo.setUnitNum("-");
            importFeeDetailPo.setRoomNum("-");
            importFeeDetailPo.setRoomId("-");
            importFeeDetailPo.setObjId(importParkingSpaceFee.getCarId());
            importFeeDetailPo.setObjType(FeeDto.PAYER_OBJ_TYPE_PARKING_SPACE);
            importFeeDetailPo.setObjName(importParkingSpaceFee.getCarNum());
            importFeeDetailPo.setStartTime(importParkingSpaceFee.getCreateTime());
            importFeeDetailPo.setIfdId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_IfdId));
            importFeeDetailPo.setState("1000");
            importFeeDetailPo.setImportFeeId(importFeeId);
            importFeeDetailPos.add(importFeeDetailPo);
        }

        feeInnerServiceSMOImpl.saveFee(payFeePos);

        feeAttrInnerServiceSMOImpl.saveFeeAttrs(feeAttrPos);

        ImportFeeDto importFeeDto = new ImportFeeDto();
        importFeeDto.setCommunityId(communityId);
        importFeeDto.setImportFeeId(importFeeId);

        List<ImportFeeDto> importRoomFeess = importFeeInnerServiceSMOImpl.queryImportFees(importFeeDto);

        if (importRoomFeess == null || importRoomFeess.size() < 1) {
            //保存日志
            ImportFeePo importFeePo = new ImportFeePo();
            importFeePo.setCommunityId(communityId);
            importFeePo.setFeeTypeCd(feeTypeCd);
            importFeePo.setImportFeeId(importFeeId);
            importFeeInnerServiceSMOImpl.saveImportFee(importFeePo);
        }


        importFeeDetailInnerServiceSMOImpl.saveImportFeeDetails(importFeeDetailPos);


        JSONObject data = new JSONObject();
        data.put("successCount", successCount);
        data.put("errorCount", errorCount);

        return ResultVo.createResponseEntity(data);
    }

    /**
     * 合同费用导入
     *
     * @param reqJson
     * @return
     */
    @Override
    @Java110Transactional
    public ResponseEntity<String> importContractFees(JSONObject reqJson) {

        int successCount = 0;
        int errorCount = 0;

        //小区ID
        String communityId = reqJson.getString("communityId");
        String importFeeId = reqJson.getString("importFeeId");
        String feeTypeCd = reqJson.getString("feeTypeCd");//费用大类
        String storeId = reqJson.getString("storeId");
        String userId = reqJson.getString("userId");
        String feeName = reqJson.getString("feeName");
        String batchId = reqJson.getString("batchId");

        JSONArray importContractFees = reqJson.getJSONArray("importRoomFees");

        List<ImportRoomFee> tmpImportContractFees = importContractFees.toJavaList(ImportRoomFee.class);

        if (StringUtil.isEmpty(feeName)) {
            feeName = IMPORT_FEE_NAME;
        }
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setFeeTypeCd(feeTypeCd);
        //feeConfigDto.setFeeNameEq(feeName);
        if (!StringUtil.isEmpty(tmpImportContractFees.get(0).getConfigId())) {
            feeConfigDto.setConfigId(tmpImportContractFees.get(0).getConfigId());
        } else {
            feeConfigDto.setFeeNameEq(feeName);
        }
        feeConfigDto.setComputingFormula(FeeConfigDto.COMPUTING_FORMULA_DYNAMIC);
        feeConfigDto.setCommunityId(communityId);
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        // 根据费用大类 判断是否有存在 费用导入收入项
        if (feeConfigDtos == null || feeConfigDtos.size() < 1) {
            //生成导入费
            feeConfigDto.setConfigId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_configId));
            saveFeeConfig(feeConfigDto);
        } else {
            feeConfigDto.setConfigId(feeConfigDtos.get(0).getConfigId());
        }


        for (ImportRoomFee tmpImportContractFee : tmpImportContractFees) {
            tmpImportContractFee.setCommunityId(communityId);
        }

        Assert.isNotNull(tmpImportContractFees, "参数错误，未包含处理费用");

        List<PayFeePo> payFeePos = new ArrayList<>();
        List<FeeAttrPo> feeAttrPos = new ArrayList<>();
        List<ImportFeeDetailPo> importFeeDetailPos = new ArrayList<>();
        PayFeePo payFeePo = null;
        ImportFeeDetailPo importFeeDetailPo = null;
        for (ImportRoomFee importContractFee : tmpImportContractFees) {
            if (StringUtil.isEmpty(importContractFee.getContractId()) || importContractFee.getContractId().startsWith("-")
            ) {
                errorCount++;
                continue;
            }
            successCount++;
            payFeePo = new PayFeePo();
            payFeePo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
            payFeePo.setEndTime(importContractFee.getStartTime());
            payFeePo.setState(FeeDto.STATE_DOING);
            payFeePo.setCommunityId(communityId);
            payFeePo.setConfigId(feeConfigDto.getConfigId());
            payFeePo.setPayerObjId(importContractFee.getContractId());
            payFeePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_CONTRACT);
            payFeePo.setUserId(userId);
            payFeePo.setIncomeObjId(storeId);
            payFeePo.setFeeTypeCd(feeTypeCd);
            payFeePo.setFeeFlag(FeeDto.FEE_FLAG_ONCE);
            payFeePo.setAmount(importContractFee.getAmount());
            payFeePo.setStartTime(importContractFee.getCreateTime());
            payFeePo.setBatchId(batchId);
            //payFeePo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            payFeePo.setFeeTime(DateUtil.getFormatTimeStringB(DateUtil.getDateFromStringA(importContractFee.getCreateTime())));

            payFeePos.add(payFeePo);

            FeeAttrPo feeAttrPo = new FeeAttrPo();
            feeAttrPo.setCommunityId(communityId);
            feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
            feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_IMPORT_FEE_NAME);
            feeAttrPo.setValue(importContractFee.getFeeName());
            feeAttrPo.setFeeId(payFeePo.getFeeId());
            feeAttrPos.add(feeAttrPo);


            feeAttrPo = new FeeAttrPo();
            feeAttrPo.setCommunityId(communityId);
            feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
            feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_ONCE_FEE_DEADLINE_TIME);
            feeAttrPo.setValue(importContractFee.getEndTime());
            feeAttrPo.setFeeId(payFeePo.getFeeId());
            feeAttrPos.add(feeAttrPo);


            if (!StringUtil.isEmpty(importContractFee.getOwnerId())) {
                feeAttrPo = new FeeAttrPo();
                feeAttrPo.setCommunityId(communityId);
                feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
                feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_ID);
                feeAttrPo.setValue(importContractFee.getOwnerId());
                feeAttrPo.setFeeId(payFeePo.getFeeId());
                feeAttrPos.add(feeAttrPo);

                feeAttrPo = new FeeAttrPo();
                feeAttrPo.setCommunityId(communityId);
                feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
                feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_NAME);
                feeAttrPo.setValue(importContractFee.getOwnerName());
                feeAttrPo.setFeeId(payFeePo.getFeeId());
                feeAttrPos.add(feeAttrPo);

                feeAttrPo = new FeeAttrPo();
                feeAttrPo.setCommunityId(communityId);
                feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
                feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_LINK);
                feeAttrPo.setValue(importContractFee.getOwnerLink());
                feeAttrPo.setFeeId(payFeePo.getFeeId());
                feeAttrPos.add(feeAttrPo);
            }

            importFeeDetailPo = new ImportFeeDetailPo();
            importFeeDetailPo.setAmount(importContractFee.getAmount());
            importFeeDetailPo.setCommunityId(communityId);
            importFeeDetailPo.setEndTime(importContractFee.getEndTime());
            importFeeDetailPo.setFeeId(payFeePo.getFeeId());
            importFeeDetailPo.setFeeName(importContractFee.getFeeName());
            importFeeDetailPo.setFloorNum("-");
            importFeeDetailPo.setUnitNum("-");
            importFeeDetailPo.setRoomNum("-");
            importFeeDetailPo.setRoomId("-");
            importFeeDetailPo.setObjId(importContractFee.getContractId());
            importFeeDetailPo.setObjType(FeeDto.PAYER_OBJ_TYPE_CONTRACT);
            importFeeDetailPo.setObjName("合同");
            importFeeDetailPo.setStartTime(importContractFee.getStartTime());
            importFeeDetailPo.setIfdId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_IfdId));
            importFeeDetailPo.setState("1000");
            importFeeDetailPo.setImportFeeId(importFeeId);
            importFeeDetailPos.add(importFeeDetailPo);
        }

        feeInnerServiceSMOImpl.saveFee(payFeePos);

        feeAttrInnerServiceSMOImpl.saveFeeAttrs(feeAttrPos);

        ImportFeeDto importFeeDto = new ImportFeeDto();
        importFeeDto.setCommunityId(communityId);
        importFeeDto.setImportFeeId(importFeeId);

        List<ImportFeeDto> importRoomFeess = importFeeInnerServiceSMOImpl.queryImportFees(importFeeDto);

        if (importRoomFeess == null || importRoomFeess.size() < 1) {
            //保存日志
            ImportFeePo importFeePo = new ImportFeePo();
            importFeePo.setCommunityId(communityId);
            importFeePo.setFeeTypeCd(feeTypeCd);
            importFeePo.setImportFeeId(importFeeId);
            importFeeInnerServiceSMOImpl.saveImportFee(importFeePo);
        }


        importFeeDetailInnerServiceSMOImpl.saveImportFeeDetails(importFeeDetailPos);


        JSONObject data = new JSONObject();
        data.put("successCount", successCount);
        data.put("errorCount", errorCount);

        return ResultVo.createResponseEntity(data);
    }

    /**
     * 保存保存导入费用配置
     *
     * @param feeConfigDto
     */
    private void saveFeeConfig(FeeConfigDto feeConfigDto) {

        PayFeeConfigPo payFeeConfigPo = BeanConvertUtil.covertBean(feeConfigDto, PayFeeConfigPo.class);
        payFeeConfigPo.setAdditionalAmount("0");
        payFeeConfigPo.setBillType(FeeConfigDto.BILL_TYPE_MONTH);
        payFeeConfigPo.setComputingFormula(FeeConfigDto.COMPUTING_FORMULA_DYNAMIC);
        payFeeConfigPo.setEndTime(DateUtil.getLastTime());
        payFeeConfigPo.setFeeFlag("2006012");
        payFeeConfigPo.setIsDefault("F");
        payFeeConfigPo.setPaymentCd("2100");
        payFeeConfigPo.setFeeName(feeConfigDto.getFeeNameEq());
        payFeeConfigPo.setSquarePrice("0");
        payFeeConfigPo.setPaymentCycle("1");
        payFeeConfigPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        payFeeConfigPo.setDeductFrom(FeeConfigDto.DEDUCT_FROM_N);
        payFeeConfigPo.setDecimalPlace("2");
        payFeeConfigPo.setScale("1");
        payFeeConfigPo.setUnits("元");
        payFeeConfigPo.setPayOnline("Y");
        int saveFlag = feeConfigInnerServiceSMOImpl.saveFeeConfig(payFeeConfigPo);

        if (saveFlag < 1) {
            throw new IllegalArgumentException("创建导入费用失败");
        }
    }


}
