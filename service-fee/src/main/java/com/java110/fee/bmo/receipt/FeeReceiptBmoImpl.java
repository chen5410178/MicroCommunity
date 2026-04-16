package com.java110.fee.bmo.receipt;

import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.payFee.PayFeeRuleDto;
import com.java110.dto.room.RoomDto;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IFeeReceiptDetailInnerServiceSMO;
import com.java110.intf.fee.IFeeReceiptInnerServiceSMO;
import com.java110.intf.fee.IPayFeeRuleV1InnerServiceSMO;
import com.java110.po.fee.FeeReceiptDetailPo;
import com.java110.po.fee.FeeReceiptPo;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.prePayFee.PrePayFeePo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class FeeReceiptBmoImpl implements IFeeReceiptBmo {

    @Autowired
    private IFeeReceiptDetailInnerServiceSMO feeReceiptDetailInnerServiceSMOImpl;

    @Autowired
    private IFeeReceiptInnerServiceSMO feeReceiptInnerServiceSMOImpl;

    @Autowired
    private IPayFeeRuleV1InnerServiceSMO payFeeRuleV1InnerServiceSMOImpl;

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    @Override
    public void savePrePayFeeReceipt(PrePayFeePo prePayFeePo, String receiptCode) {
        // 生成receiptId
        String receiptId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_receiptId);

        // 保存fee_receipt 表
        FeeReceiptPo feeReceiptPo = new FeeReceiptPo();
        feeReceiptPo.setReceiptId(receiptId);
        feeReceiptPo.setReceiptCode(receiptCode);
        feeReceiptPo.setAmount(prePayFeePo.getReceivedAmount());
        feeReceiptPo.setReceivableAmount(prePayFeePo.getReceivableAmount());
        feeReceiptPo.setObjId(prePayFeePo.getPayerObjId());
        feeReceiptPo.setObjName(prePayFeePo.getPayerObjName());
        feeReceiptPo.setObjType("3333"); // 预付费类型
        feeReceiptPo.setCommunityId(prePayFeePo.getCommunityId());
        feeReceiptPo.setRemark(prePayFeePo.getRemark());
        feeReceiptPo.setPayObjId(prePayFeePo.getOwnerId());
        feeReceiptPo.setPayObjName(prePayFeePo.getOwnerName());
        feeReceiptInnerServiceSMOImpl.saveFeeReceipt(feeReceiptPo);

        // 保存fee_receipt_detail 表
        FeeReceiptDetailPo feeReceiptDetailPo = new FeeReceiptDetailPo();
        feeReceiptDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        feeReceiptDetailPo.setReceiptId(receiptId);
        feeReceiptDetailPo.setCommunityId(prePayFeePo.getCommunityId());
        feeReceiptDetailPo.setAmount(prePayFeePo.getReceivedAmount());
        feeReceiptDetailPo.setFeeName(prePayFeePo.getFeeName());
        feeReceiptDetailPo.setRemark(prePayFeePo.getRemark());
        feeReceiptDetailPo.setFeeId(prePayFeePo.getRuleId());
        feeReceiptDetailPo.setStartTime(prePayFeePo.getStartTime());
        feeReceiptDetailPo.setEndTime(prePayFeePo.getEndTime());
        feeReceiptDetailPo.setPayWay(prePayFeePo.getPrimeRate()); // 收款方式
        feeReceiptDetailPo.setDeductionAmount(prePayFeePo.getDiscountAmount()); // 优惠金额
        feeReceiptDetailPo.setLateAmount("0"); // 滞纳金
        feeReceiptDetailPo.setStartDegree(""); // 开始度数
        feeReceiptDetailPo.setEndDegree(""); // 结束度数
        feeReceiptDetailPo.setCycle("1"); // 周期
        feeReceiptDetailPo.setSquarePrice(""); // 单价
        feeReceiptDetailPo.setArea("1"); // 面积
        feeReceiptDetailPo.setFtName("预收" + prePayFeePo.getFtName());
        feeReceiptDetailPo.setAcctAmount("0");

        //取开始时间，因为预存款但没有意义，这个只有在 费用上有意义 所以这里取开始时间的就可以
        feeReceiptDetailPo.setFeeMonth(DateUtil.getFormatTimeStringQ(DateUtil.getDateFromStringB(prePayFeePo.getStartTime()))); // 应计日期

        PayFeeRuleDto payFeeRuleDto = new PayFeeRuleDto();
        payFeeRuleDto.setRuleId(prePayFeePo.getRuleId());
        payFeeRuleDto.setFtId(prePayFeePo.getFtId());
        List<PayFeeRuleDto> payFeeRuleDtos = payFeeRuleV1InnerServiceSMOImpl.queryPayFeeRules(payFeeRuleDto);
        if (!ListUtil.isNull(payFeeRuleDtos)) {
            feeReceiptDetailPo.setSquarePrice(Double.parseDouble(payFeeRuleDtos.get(0).getSquarePrice())+""); // 单价
        }
        RoomDto roomDto = new RoomDto();
        roomDto.setRoomId(prePayFeePo.getPayerObjId());
        List<RoomDto> roomDtos = roomV1InnerServiceSMOImpl.queryRooms(roomDto);
        if (!ListUtil.isNull(roomDtos)) {
            feeReceiptDetailPo.setArea(roomDtos.get(0).getBuiltUpArea());
        }

        feeReceiptDetailPo.setPrintCount("0"); // 打印次数

        feeReceiptDetailInnerServiceSMOImpl.saveFeeReceiptDetail(feeReceiptDetailPo);
    }

    @Override
    public void savePayFeeReceipt(PayFeeDetailPo payFeeDetailPo, String receiptCode) {
        FeeDto feeDto = new FeeDto();
        feeDto.setFeeId(payFeeDetailPo.getFeeId());
        feeDto.setCommunityId(payFeeDetailPo.getCommunityId());
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
        Assert.listOnlyOne(feeDtos, "未查询到费用信息");
        feeDto = feeDtos.get(0);
        //处理 小数点后 0
        feeDto.setSquarePrice(Double.parseDouble(feeDto.getSquarePrice()) + "");
        feeDto.setAdditionalAmount(Double.parseDouble(feeDto.getAdditionalAmount()) + "");

        //添加收据和收据详情
        FeeReceiptPo feeReceiptPo = new FeeReceiptPo();
        feeReceiptPo.setAmount(payFeeDetailPo.getReceivedAmount()); //实收金额
        feeReceiptPo.setReceivableAmount(payFeeDetailPo.getReceivableAmount()); //应收金额
        feeReceiptPo.setCommunityId(payFeeDetailPo.getCommunityId());
        feeReceiptPo.setReceiptId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_receiptId));
        feeReceiptPo.setObjType(feeDto.getPayerObjType());
        feeReceiptPo.setObjId(feeDto.getPayerObjId());
        feeReceiptPo.setObjName(feeDto.getPayerObjName());
        feeReceiptPo.setPayObjId(feeDto.getOwnerId());
        feeReceiptPo.setPayObjName(feeDto.getOwnerName());
        feeReceiptPo.setCreateTime(payFeeDetailPo.getCreateTime());
        feeReceiptPo.setReceiptCode(receiptCode);
        feeReceiptPo.setRemark(payFeeDetailPo.getRemark());
        feeReceiptInnerServiceSMOImpl.saveFeeReceipt(feeReceiptPo);

        FeeReceiptDetailPo feeReceiptDetailPo = new FeeReceiptDetailPo();
        feeReceiptDetailPo.setAmount(payFeeDetailPo.getReceivedAmount());
        feeReceiptDetailPo.setCommunityId(payFeeDetailPo.getCommunityId());
        feeReceiptDetailPo.setCycle(payFeeDetailPo.getCycles());
        feeReceiptDetailPo.setDetailId(payFeeDetailPo.getDetailId());
        feeReceiptDetailPo.setEndTime(payFeeDetailPo.getEndTime());
        feeReceiptDetailPo.setFeeId(payFeeDetailPo.getFeeId());
        feeReceiptDetailPo.setFeeName(StringUtil.isEmpty(feeDto.getImportFeeName()) ? feeDto.getFeeName() : feeDto.getImportFeeName());
        feeReceiptDetailPo.setStartTime(payFeeDetailPo.getStartTime());
        feeReceiptDetailPo.setReceiptId(feeReceiptPo.getReceiptId());
        // Set additional fields that may not be set by freshFeeReceiptDetail
        feeReceiptDetailPo.setRemark(payFeeDetailPo.getRemark());
        feeReceiptDetailPo.setPayWay(payFeeDetailPo.getPrimeRate());
        feeReceiptDetailPo.setLateAmount(payFeeDetailPo.getLateAmount());
        BigDecimal deductionAmount = new BigDecimal(payFeeDetailPo.getDeductionAmount());
        deductionAmount = deductionAmount.add(new BigDecimal(payFeeDetailPo.getDiscountAmount()));
        feeReceiptDetailPo.setDeductionAmount(deductionAmount.doubleValue() + "");
        feeReceiptDetailPo.setStartDegree(feeDto.getPreDegrees());
        feeReceiptDetailPo.setEndDegree(feeDto.getCurDegrees());
        feeReceiptDetailPo.setFtName(feeDto.getFtName());
        feeReceiptDetailPo.setAcctAmount(payFeeDetailPo.getAcctAmount());

        // Initialize fields that might not be set by freshFeeReceiptDetail
        String feeMonth = "";
        if (feeDto.getFeeTime() != null) {
            feeMonth = DateUtil.getFormatTimeStringQ(feeDto.getFeeTime());
        }
        feeReceiptDetailPo.setFeeMonth(feeMonth);
        feeReceiptDetailPo.setPrintCount("0");
        computeFeeSMOImpl.freshFeeReceiptDetail(feeDto, feeReceiptDetailPo);

        feeReceiptDetailInnerServiceSMOImpl.saveFeeReceiptDetail(feeReceiptDetailPo);
    }
}
