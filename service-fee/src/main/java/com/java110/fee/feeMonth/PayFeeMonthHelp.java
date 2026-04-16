package com.java110.fee.feeMonth;

import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.contract.ContractDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.fee.PayFeeDetailDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.payFee.PayFeeMonthOwnerDto;
import com.java110.dto.room.RoomDto;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IPayFeeDetailMonthInnerServiceSMO;
import com.java110.intf.fee.IPayFeeDetailV1InnerServiceSMO;
import com.java110.intf.store.IContractInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.po.payFee.PayFeeDetailMonthPo;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class PayFeeMonthHelp implements IPayFeeMonthHelp {

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IContractInnerServiceSMO contractInnerServiceSMOImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    @Autowired
    private IPayFeeDetailMonthInnerServiceSMO payFeeDetailMonthInnerServiceSMOImpl;

    @Autowired
    private IPayFeeDetailV1InnerServiceSMO payFeeDetailV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;


    public PayFeeMonthOwnerDto generatorOwnerRoom(FeeDto feeDto) {

        PayFeeMonthOwnerDto payFeeMonthOwnerDto = new PayFeeMonthOwnerDto();
        payFeeMonthOwnerDto.setObjName(feeDto.getPayerObjName());
        payFeeMonthOwnerDto.setObjId(feeDto.getPayerObjId());
        payFeeMonthOwnerDto.setOwnerId(feeDto.getOwnerId());
        payFeeMonthOwnerDto.setOwnerName(feeDto.getOwnerName());
        payFeeMonthOwnerDto.setObjFpcId("-1");

        if (StringUtil.isEmpty(feeDto.getPayerObjId())) {
            return payFeeMonthOwnerDto;
        }

        if (!StringUtil.isEmpty(feeDto.getOwnerId())) {
            OwnerDto ownerDto = new OwnerDto();
            ownerDto.setMemberId(feeDto.getOwnerId());
            ownerDto.setCommunityId(feeDto.getCommunityId());
            List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);
            if (!ListUtil.isNull(ownerDtos)) {
                payFeeMonthOwnerDto.setLink(ownerDtos.get(0).getLink());
            }
        }

        // 如果是房屋
        if (FeeDto.PAYER_OBJ_TYPE_ROOM.equals(feeDto.getPayerObjType())) {

            RoomDto roomDto = new RoomDto();
            roomDto.setRoomId(feeDto.getPayerObjId());
            roomDto.setCommunityId(feeDto.getCommunityId());
            List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
            if (!ListUtil.isNull(roomDtos)) {
                payFeeMonthOwnerDto.setObjFpcId(roomDtos.get(0).getFloorId());
            }
        } else if (FeeDto.PAYER_OBJ_TYPE_CAR.equals(feeDto.getPayerObjType())) {

            OwnerCarDto ownerCarDto = new OwnerCarDto();
            ownerCarDto.setMemberId(feeDto.getPayerObjId());
            ownerCarDto.setCommunityId(feeDto.getCommunityId());
            List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
            if (!ListUtil.isNull(ownerCarDtos)) {
                payFeeMonthOwnerDto.setObjFpcId(ownerCarDtos.get(0).getPaId());
            }
        } else {
            ContractDto contractDto = new ContractDto();
            contractDto.setContractId(feeDto.getPayerObjId());
            List<ContractDto> contractDtos = contractInnerServiceSMOImpl.queryContracts(contractDto);
            if (!ListUtil.isNull(contractDtos)) {
                payFeeMonthOwnerDto.setObjFpcId(contractDtos.get(0).getContractType());
            }
        }
        return payFeeMonthOwnerDto;
    }


    /**
     * 处理 欠费 离散
     *
     * @param feeDto
     * @param payFeeMonthOwnerDto
     */
    @Override
    public void generatorPayFeeDetailMonth(FeeDto feeDto, PayFeeMonthOwnerDto payFeeMonthOwnerDto) {

        // todo 清理
        PayFeeDetailMonthPo payFeeDetailMonthPo = new PayFeeDetailMonthPo();
        payFeeDetailMonthPo.setCommunityId(feeDto.getCommunityId());
        payFeeDetailMonthPo.setFeeId(feeDto.getFeeId());
        payFeeDetailMonthInnerServiceSMOImpl.deletePayFeeDetailMonth(payFeeDetailMonthPo);

        PayFeeDetailDto payFeeDetailDto = new PayFeeDetailDto();
        payFeeDetailDto.setCommunityId(feeDto.getCommunityId());
        payFeeDetailDto.setFeeId(feeDto.getFeeId());
        payFeeDetailDto.setPage(1);
        payFeeDetailDto.setRow(100);
        List<PayFeeDetailDto> payFeeDetailDtos = payFeeDetailV1InnerServiceSMOImpl.queryPayFeeDetailNew(payFeeDetailDto);

        BigDecimal receivedAmount = new BigDecimal("0");
        BigDecimal discountAmount = new BigDecimal("0");
        String payFeeTime = null;
        String detailId = "-1";
        if (ListUtil.isNull(payFeeDetailDtos)) {
            for (PayFeeDetailDto tFeeDetailDto : payFeeDetailDtos) {
                detailId = tFeeDetailDto.getDetailId();
                payFeeTime = tFeeDetailDto.getCreateTime();
                receivedAmount = receivedAmount.add(new BigDecimal(tFeeDetailDto.getReceivedAmount()));
                receivedAmount = receivedAmount.add(new BigDecimal(tFeeDetailDto.getLateAmount())); // 滞纳金
                discountAmount = discountAmount.add(new BigDecimal(tFeeDetailDto.getDiscountAmount()));
                discountAmount = discountAmount.add(new BigDecimal(tFeeDetailDto.getAcctAmount())); //账户
                discountAmount = discountAmount.add(new BigDecimal(tFeeDetailDto.getDeductionAmount()));
            }
        }

        String yearMonth = "";
        if (feeDto.getFeeTime() != null) {
            yearMonth = DateUtil.getFormatTimeStringQ(feeDto.getFeeTime());
        }

        int feeYear = Integer.parseInt(yearMonth.split("-")[0]);
        int feeMonth = Integer.parseInt(yearMonth.split("-")[1]);


        List<PayFeeDetailMonthPo> payFeeDetailMonthPos = new ArrayList<>();
        // todo 处理 开始时间和结束时间

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(feeDto.getStartTime());
        calendar.set(Calendar.YEAR, feeYear);
        calendar.set(Calendar.MONTH, feeMonth - 1);
        String curTime = DateUtil.getFormatTimeStringB(calendar.getTime());

        PayFeeDetailMonthPo tmpPayFeeDetailMonthPo = new PayFeeDetailMonthPo();
        tmpPayFeeDetailMonthPo.setFeeId(feeDto.getFeeId());
        tmpPayFeeDetailMonthPo.setCommunityId(feeDto.getCommunityId());

        tmpPayFeeDetailMonthPo.setDetailId(detailId);

        tmpPayFeeDetailMonthPo.setDetailYear(feeYear + "");
        tmpPayFeeDetailMonthPo.setDetailMonth(feeMonth + "");

        tmpPayFeeDetailMonthPo.setReceivableAmount(feeDto.getReceivableAmount());
        tmpPayFeeDetailMonthPo.setReceivedAmount(receivedAmount.doubleValue() + "");
        tmpPayFeeDetailMonthPo.setDiscountAmount(discountAmount.doubleValue() + "");
        tmpPayFeeDetailMonthPo.setMonthId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_monthId, true));
        tmpPayFeeDetailMonthPo.setRemark("程序计算生成");
        tmpPayFeeDetailMonthPo.setObjName(payFeeMonthOwnerDto.getObjName());
        tmpPayFeeDetailMonthPo.setObjId(payFeeMonthOwnerDto.getObjId());
        tmpPayFeeDetailMonthPo.setOwnerId(payFeeMonthOwnerDto.getOwnerId());
        tmpPayFeeDetailMonthPo.setOwnerName(payFeeMonthOwnerDto.getOwnerName());
        tmpPayFeeDetailMonthPo.setLink(payFeeMonthOwnerDto.getLink());
        tmpPayFeeDetailMonthPo.setCurMonthTime(curTime);
        tmpPayFeeDetailMonthPo.setDeadlineTime(DateUtil.getFormatTimeStringA(feeDto.getEndTime()));
        tmpPayFeeDetailMonthPo.setPayFeeTime(payFeeTime);
        tmpPayFeeDetailMonthPo.setState("W"); // todo 这里暂时写死，目前用不到，算是预留字段
        tmpPayFeeDetailMonthPo.setFeeName(feeDto.getFeeName());
        tmpPayFeeDetailMonthPo.setConfigId(feeDto.getConfigId());
        tmpPayFeeDetailMonthPo.setFeeTypeCd(feeDto.getFeeTypeCd());
        tmpPayFeeDetailMonthPo.setFtId(feeDto.getFtId());
        //todo 查询obj_fpc_id
        tmpPayFeeDetailMonthPo.setObjFpcId(payFeeMonthOwnerDto.getObjFpcId());
        payFeeDetailMonthPos.add(tmpPayFeeDetailMonthPo);
        // todo 保存数据到pay_fee_detail_month
        payFeeDetailMonthInnerServiceSMOImpl.savePayFeeDetailMonths(payFeeDetailMonthPos);
    }


}
