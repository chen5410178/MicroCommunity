package com.java110.job.importData.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.importData.ImportFeeDto;
import com.java110.dto.importData.ImportRoomFee;
import com.java110.dto.log.AssetImportLogDetailDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.*;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.job.importData.DefaultImportData;
import com.java110.job.importData.IImportDataAdapt;
import com.java110.po.importFee.ImportFeeDetailPo;
import com.java110.po.importFee.ImportFeePo;
import com.java110.po.payFee.PayFeeRulePo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("importRoomFeeQueueData")
public class ImportRoomFeeQueueDataAdapt extends DefaultImportData implements IImportDataAdapt {

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IPayFeeConfigV1InnerServiceSMO payFeeConfigV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IImportFeeDetailInnerServiceSMO importFeeDetailInnerServiceSMOImpl;

    @Autowired
    private IImportFeeInnerServiceSMO importFeeInnerServiceSMOImpl;

    @Autowired
    private IPayFeeRuleV1InnerServiceSMO payFeeRuleV1InnerServiceSMOImpl;

    @Autowired
    private IRuleGeneratorPayFeeBillV1InnerServiceSMO ruleGeneratorPayFeeBillV1InnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

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
        ImportRoomFee importRoomFee = BeanConvertUtil.covertBean(data, ImportRoomFee.class);

        String communityId = importRoomFee.getCommunityId();
        String batchId = importRoomFee.getBatchId();

        doImportRoomCreateFee(importRoomFee, batchId);
        doImportCarCreateFee(importRoomFee, batchId);


    }

    /**
     * 房屋创建费用
     *
     * @param importRoomFee
     * @param batchId
     */
    private void doImportRoomCreateFee(ImportRoomFee importRoomFee, String batchId) {
        if (!FeeDto.PAYER_OBJ_TYPE_ROOM.equals(importRoomFee.getObjType())) {
            return;
        }
        List<ImportRoomFee> rooms = new ArrayList<>();

        String[] objNames;
        objNames = importRoomFee.getPayerObjName().split("-", 3);
        if (objNames.length != 3) {
            throw new IllegalArgumentException("收费对象格式错误：" + importRoomFee.getPayerObjName());
        }
        importRoomFee.setFloorNum(objNames[0]);
        importRoomFee.setUnitNum(objNames[1]);
        importRoomFee.setRoomNum(objNames[2]);

        rooms.add(importRoomFee);


        List<ImportRoomFee> importRoomFees = roomInnerServiceSMOImpl.freshRoomIds(rooms);
        List<String> roomIds = new ArrayList<>();
        for (ImportRoomFee tmpImportRoomFee : importRoomFees) {
            if (StringUtil.isEmpty(tmpImportRoomFee.getRoomId())) {
                throw new IllegalArgumentException("房屋不存在，" + importRoomFee.getFloorNum() + "-" + importRoomFee.getUnitNum() + "-" + importRoomFee.getRoomNum());
            }
            roomIds.add(tmpImportRoomFee.getRoomId());
        }
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(importRoomFee.getCommunityId());
        ownerDto.setRoomIds(roomIds.toArray(new String[roomIds.size()]));
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnersByRoom(ownerDto);
        for (ImportRoomFee tmpImportRoomFee : importRoomFees) {
            for (OwnerDto tmpOwnerDto : ownerDtos) {
                if (tmpImportRoomFee.getRoomId().equals(tmpOwnerDto.getRoomId())) {
                    tmpImportRoomFee.setOwnerId(tmpOwnerDto.getOwnerId());
                    tmpImportRoomFee.setOwnerName(tmpOwnerDto.getName());
                    tmpImportRoomFee.setOwnerLink(tmpOwnerDto.getLink());
                }
            }
        }
        doCreateFeeAndAttrs(importRoomFees.get(0), batchId);
    }


    /**
     * 创建车辆费用
     *
     * @param importRoomFee
     * @param batchId
     */
    private void doImportCarCreateFee(ImportRoomFee importRoomFee, String batchId) {
        if (!FeeDto.PAYER_OBJ_TYPE_CAR.equals(importRoomFee.getObjType())) {
            return;
        }
        List<ImportRoomFee> cars = new ArrayList<>();
        importRoomFee.setCarNum(importRoomFee.getPayerObjName());
        cars.add(importRoomFee);


        List<ImportRoomFee> importRoomFees = ownerCarInnerServiceSMOImpl.freshCarIds(cars);
        for (ImportRoomFee tmpImportRoomFee : importRoomFees) {
            if (StringUtil.isEmpty(tmpImportRoomFee.getCarId())) {
                throw new IllegalArgumentException(tmpImportRoomFee.getCarNum() + "不存在");
            }
        }
        //todo 车辆导入补刷业主信息（queryOwnerCars join building_owner 真查询，禁止前端透传）
        List<String> carIds = new ArrayList<>();
        for (ImportRoomFee tmpImportRoomFee : importRoomFees) {
            carIds.add(tmpImportRoomFee.getCarId());
        }
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCommunityId(importRoomFee.getCommunityId());
        ownerCarDto.setCarIds(carIds.toArray(new String[carIds.size()]));
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
        for (ImportRoomFee tmpImportRoomFee : importRoomFees) {
            for (OwnerCarDto tmpOwnerCarDto : ownerCarDtos) {
                if (tmpImportRoomFee.getCarId().equals(tmpOwnerCarDto.getCarId())) {
                    tmpImportRoomFee.setOwnerId(tmpOwnerCarDto.getOwnerId());
                    tmpImportRoomFee.setOwnerName(tmpOwnerCarDto.getOwnerName());
                }
            }
        }
        doCreateFeeAndAttrs(importRoomFees.get(0), batchId);
    }


    private void doCreateFeeAndAttrs(ImportRoomFee importRoomFee, String batchId) {
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(importRoomFee.getCommunityId());
        if (!StringUtil.isEmpty(importRoomFee.getConfigId())) {
            feeConfigDto.setConfigId(importRoomFee.getConfigId());
        } else {
            feeConfigDto.setFeeNameEq(importRoomFee.getFeeName());
        }
        feeConfigDto.setComputingFormula(FeeConfigDto.COMPUTING_FORMULA_DYNAMIC);
        List<FeeConfigDto> feeConfigDtos = payFeeConfigV1InnerServiceSMOImpl.queryPayFeeConfigs(feeConfigDto);
        if (ListUtil.isNull(feeConfigDtos)) {
            throw new IllegalArgumentException("费用项不存在,或者费用项公式不是动态费用");
        }

        PayFeeRulePo payFeeRulePo = createPayFeeRulePo(importRoomFee, batchId, feeConfigDtos.get(0));

        List<PayFeeRulePo> payFeeRulePos = new ArrayList<>();
        payFeeRulePos.add(payFeeRulePo);

        payFeeRuleV1InnerServiceSMOImpl.savePayFeeRules(payFeeRulePos);

        ruleGeneratorPayFeeBillV1InnerServiceSMOImpl.rulesGeneratePayFees(payFeeRulePos);

        FeeDto feeDto = new FeeDto();
        feeDto.setCommunityId(importRoomFee.getCommunityId());
        feeDto.setBatchId(batchId);
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
        String feeId = "";
        if (!ListUtil.isNull(feeDtos)) {
            feeId = feeDtos.get(0).getFeeId();
        }

        saveImportFee(importRoomFee, feeId);
    }

    private PayFeeRulePo createPayFeeRulePo(ImportRoomFee importRoomFee, String batchId, FeeConfigDto feeConfigDto) {
        String time = importRoomFee.getStartTime();

        PayFeeRulePo payFeeRulePo = new PayFeeRulePo();
        payFeeRulePo.setRuleId(GenerateCodeFactory.getGeneratorId("11"));
        payFeeRulePo.setConfigId(feeConfigDto.getConfigId());
        payFeeRulePo.setCommunityId(importRoomFee.getCommunityId());
        payFeeRulePo.setFeeTypeCd(feeConfigDto.getFeeTypeCd());
        payFeeRulePo.setFeeFlag(feeConfigDto.getFeeFlag());
        payFeeRulePo.setAmount(importRoomFee.getAmount());
        payFeeRulePo.setStartTime(time);
        payFeeRulePo.setEndTime(time);
        payFeeRulePo.setMaxTime(importRoomFee.getEndTime());
        payFeeRulePo.setBatchId(batchId);
        payFeeRulePo.setUserId("-1");
        payFeeRulePo.setIncomeObjId(importRoomFee.getStoreId());
        payFeeRulePo.setState(FeeDto.STATE_DOING);
        payFeeRulePo.setDeductFrom(feeConfigDto.getDeductFrom());
        payFeeRulePo.setOwnerId(importRoomFee.getOwnerId());
        payFeeRulePo.setOwnerName(importRoomFee.getOwnerName());
        payFeeRulePo.setPayerObjName(StringUtil.isEmpty(importRoomFee.getPayerObjName())
                ? importRoomFee.getFloorNum() + "-" + importRoomFee.getUnitNum() + "-" + importRoomFee.getRoomNum()
                : importRoomFee.getPayerObjName());

        if (FeeDto.PAYER_OBJ_TYPE_CONTRACT.equals(importRoomFee.getObjType())) {
            payFeeRulePo.setPayerObjId(importRoomFee.getContractId());
            payFeeRulePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_CONTRACT);
        } else if (FeeDto.PAYER_OBJ_TYPE_CAR.equals(importRoomFee.getObjType())) {
            payFeeRulePo.setPayerObjId(importRoomFee.getCarId());
            payFeeRulePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_CAR);
        } else {
            payFeeRulePo.setPayerObjId(importRoomFee.getRoomId());
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

    private void saveImportFee(ImportRoomFee importRoomFee, String feeId) {
        ImportFeeDetailPo importFeeDetailPo;
        List<ImportFeeDetailPo> importFeeDetailPos = new ArrayList<>();
        importFeeDetailPo = new ImportFeeDetailPo();
        importFeeDetailPo.setAmount(importRoomFee.getAmount());
        importFeeDetailPo.setCommunityId(importRoomFee.getCommunityId());
        importFeeDetailPo.setEndTime(importRoomFee.getEndTime());
        importFeeDetailPo.setFeeId(feeId);
        importFeeDetailPo.setFeeName(importRoomFee.getFeeName());

        if (FeeDto.PAYER_OBJ_TYPE_CONTRACT.equals(importRoomFee.getObjType())) {
            importFeeDetailPo.setFloorNum("无");
            importFeeDetailPo.setUnitNum("无");
            importFeeDetailPo.setRoomNum(importRoomFee.getContractId());
            importFeeDetailPo.setRoomId(importRoomFee.getContractId());
            importFeeDetailPo.setObjId(importRoomFee.getContractId());
            importFeeDetailPo.setObjName(importRoomFee.getContractId());
        } else if (FeeDto.PAYER_OBJ_TYPE_CAR.equals(importRoomFee.getObjType())) {
            importFeeDetailPo.setFloorNum("无");
            importFeeDetailPo.setUnitNum("无");
            importFeeDetailPo.setRoomNum(importRoomFee.getCarNum());
            importFeeDetailPo.setRoomId(importRoomFee.getCarId());
            importFeeDetailPo.setObjId(importRoomFee.getCarId());
            importFeeDetailPo.setObjName(importRoomFee.getCarNum());
        } else {
            importFeeDetailPo.setFloorNum(importRoomFee.getFloorNum());
            importFeeDetailPo.setUnitNum(importRoomFee.getUnitNum());
            importFeeDetailPo.setRoomNum(importRoomFee.getRoomNum());
            importFeeDetailPo.setRoomId(importRoomFee.getRoomId());
            importFeeDetailPo.setObjId(importRoomFee.getRoomId());
            importFeeDetailPo.setObjName(!"0".equals(importRoomFee.getUnitNum())
                    ? importRoomFee.getFloorNum() + "栋" + importRoomFee.getUnitNum() + "单元" + importRoomFee.getRoomNum() + "室" :
                    importRoomFee.getFloorNum() + "栋" + importRoomFee.getRoomNum() + "室"
            );
        }

        importFeeDetailPo.setObjType(importRoomFee.getObjType());

        importFeeDetailPo.setStartTime(importRoomFee.getStartTime());
        importFeeDetailPo.setIfdId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_IfdId, true));
        importFeeDetailPo.setState("1000");
        importFeeDetailPo.setImportFeeId(importRoomFee.getBatchId());
        importFeeDetailPos.add(importFeeDetailPo);

        ImportFeeDto importFeeDto = new ImportFeeDto();
        importFeeDto.setCommunityId(importRoomFee.getCommunityId());
        importFeeDto.setImportFeeId(importRoomFee.getBatchId());

        List<ImportFeeDto> importRoomFeess = importFeeInnerServiceSMOImpl.queryImportFees(importFeeDto);

        if (ListUtil.isNull(importRoomFeess)) {
            //保存日志
            ImportFeePo importFeePo = new ImportFeePo();
            importFeePo.setCommunityId(importRoomFee.getCommunityId());
            importFeePo.setFeeTypeCd(importRoomFee.getFeeTypeCd());
            importFeePo.setImportFeeId(importRoomFee.getBatchId());
            importFeeInnerServiceSMOImpl.saveImportFee(importFeePo);
        }


        importFeeDetailInnerServiceSMOImpl.saveImportFeeDetails(importFeeDetailPos);
    }
}
