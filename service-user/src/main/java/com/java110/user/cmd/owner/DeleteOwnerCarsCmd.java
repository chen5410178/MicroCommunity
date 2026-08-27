package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.Environment;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.fee.PayFeeDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IPayFeeDetailMonthInnerServiceSMO;
import com.java110.intf.fee.IPayFeeDetailV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeV1InnerServiceSMO;
import com.java110.intf.report.IReportOweFeeInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerCarV1InnerServiceSMO;
import com.java110.po.car.OwnerCarPo;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.payFee.PayFeeDetailMonthPo;
import com.java110.po.reportFee.ReportOweFeePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Java110Cmd(serviceCode = "owner.deleteOwnerCars")
public class DeleteOwnerCarsCmd extends Cmd {

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarV1InnerServiceSMO ownerCarV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeDetailV1InnerServiceSMO payFeeDetailV1InnerServiceSMOImpl;

    @Autowired
    private IReportOweFeeInnerServiceSMO reportOweFeeInnerServiceSMOImpl;

    @Autowired
    private IPayFeeDetailMonthInnerServiceSMO payFeeDetailMonthInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Environment.isDevEnv();

        Assert.hasKeyAndValue(reqJson, "carId", "carId不能为空");
        Assert.hasKeyAndValue(reqJson, "memberId", "memberId不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        FeeDto feeDto = new FeeDto();
        feeDto.setPayerObjId(reqJson.getString("memberId"));
        feeDto.setCommunityId(reqJson.getString("communityId"));
        feeDto.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_PARKING_SPACE);
        List<FeeDto> feeDtoList = feeInnerServiceSMOImpl.queryFees(feeDto);

        for (FeeDto tmpFeeDto : feeDtoList) {
            if (!FeeDto.STATE_FINISH.equals(tmpFeeDto.getState())) {
                throw new IllegalArgumentException("存在 未结束费用 不能删除");
            }
        }
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        OwnerCarPo ownerCarPo = new OwnerCarPo();
        ownerCarPo.setCommunityId(reqJson.getString("communityId"));
        ownerCarPo.setCarId(reqJson.getString("carId"));
        ownerCarPo.setMemberId(reqJson.getString("memberId"));
        int flag = ownerCarV1InnerServiceSMOImpl.deleteOwnerCar(ownerCarPo);
        if (flag < 1) {
            throw new IllegalArgumentException("删除车辆出错");
        }
        // todo 删除车辆费用
        deleteCarFee(reqJson);
    }

    /**
     * 删除房屋费用
     *
     * @param reqJson
     */
    private void deleteCarFee(JSONObject reqJson) {

        if (!reqJson.getString("carId").equals(reqJson.getString("memberId"))) {
            return;
        }

        if (StringUtil.isEmpty(reqJson.getString("carId"))) {
            throw new CmdException("车联不能为空");
        }
        PayFeeDto feeDto = new PayFeeDto();
        feeDto.setPayerObjId(reqJson.getString("carId"));
        feeDto.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_PARKING_SPACE);
        feeDto.setCommunityId(reqJson.getString("communityId"));
        List<PayFeeDto> feeDtos = payFeeV1InnerServiceSMOImpl.queryPayFees(feeDto);

        if (feeDtos == null || feeDtos.size() < 1) {
            return;
        }

        for (PayFeeDto payFeeDto : feeDtos) {
            doDeleteFee(payFeeDto);
        }

    }

    /**
     * @param payFeeDto
     */
    private void doDeleteFee(PayFeeDto payFeeDto) {
        //todo 删除缴费记录

        PayFeeDetailPo payFeeDetailPo = new PayFeeDetailPo();
        payFeeDetailPo.setFeeId(payFeeDto.getFeeId());
        payFeeDetailPo.setCommunityId(payFeeDto.getCommunityId());
        payFeeDetailV1InnerServiceSMOImpl.deletePayFeeDetailNew(payFeeDetailPo);

        //todo 删除费用
        PayFeePo payFeePo = new PayFeePo();
        payFeePo.setFeeId(payFeeDto.getFeeId());
        payFeePo.setCommunityId(payFeeDto.getCommunityId());
        payFeeV1InnerServiceSMOImpl.deletePayFee(payFeePo);

        //todo 删除欠费
        ReportOweFeePo reportOweFeePo = new ReportOweFeePo();
        reportOweFeePo.setFeeId(payFeeDto.getFeeId());
        reportOweFeePo.setCommunityId(payFeeDto.getCommunityId());
        reportOweFeeInnerServiceSMOImpl.deleteReportOweFee(reportOweFeePo);

        //todo 删除 离散月数据
        PayFeeDetailMonthPo payFeeDetailMonthPo = new PayFeeDetailMonthPo();
        payFeeDetailMonthPo.setFeeId(payFeeDto.getFeeId());
        payFeeDetailMonthPo.setCommunityId(payFeeDto.getCommunityId());
        payFeeDetailMonthInnerServiceSMOImpl.deletePayFeeDetailMonth(payFeeDetailMonthPo);

    }

}
