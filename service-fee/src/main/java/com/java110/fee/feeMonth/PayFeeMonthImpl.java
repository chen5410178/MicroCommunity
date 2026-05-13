package com.java110.fee.feeMonth;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.log.LoggerFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.payFee.PayFeeMonthOwnerDto;
import com.java110.intf.fee.IFeeDetailInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IPayFeeConfigV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeDetailMonthInnerServiceSMO;
import com.java110.intf.report.IGeneratorOweFeeInnerServiceSMO;
import com.java110.po.payFee.PayFeeDetailMonthPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 费用离散为月 实现类
 * V2
 * <p>
 * case 1 定时任务 调用
 */
@Service
public class PayFeeMonthImpl implements IPayFeeMonth {
    private static Logger logger = LoggerFactory.getLogger(PayFeeMonthImpl.class);

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IPayFeeConfigV1InnerServiceSMO payFeeConfigV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeDetailMonthInnerServiceSMO payFeeDetailMonthInnerServiceSMOImpl;

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Autowired
    private IPayFeeMonthHelp payFeeMonthHelp;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    @Autowired
    private IGeneratorOweFeeInnerServiceSMO generatorOweFeeInnerServiceSMOImpl;

    public static final int DEFAULT_DEAL_COUNT = 1000;

    /**
     * 1.0 定时任务离散 小区数据 离散为 月数据
     *
     * @param communityId
     */
    @Async
    @Override
    public void doGeneratorOrRefreshAllFeeMonth(String communityId) {

        //todo 清理 pay_fee_detail_month
        PayFeeDetailMonthPo payFeeDetailMonthPo = new PayFeeDetailMonthPo();
        payFeeDetailMonthPo.setCommunityId(communityId);
        payFeeDetailMonthInnerServiceSMOImpl.deletePayFeeDetailMonth(payFeeDetailMonthPo);


        FeeDto feeDto = new FeeDto();
        feeDto.setCommunityId(communityId);
        int count = feeInnerServiceSMOImpl.queryFeesCount(feeDto);

        int page = 1;
        int max = 15;
        if (count < DEFAULT_DEAL_COUNT) {
            page = 1;
            max = count;
        } else {
            page = (int) Math.ceil((double) count / (double) DEFAULT_DEAL_COUNT);
            max = DEFAULT_DEAL_COUNT;
        }

        //todo  每次按200条处理
        for (int pageIndex = 0; pageIndex < page; pageIndex++) {
            feeDto.setPage(pageIndex + 1);
            feeDto.setRow(max);
            List<FeeDto> tmpFeeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
            // 离散费用
            doTmpFeeDtoMonths(communityId, tmpFeeDtos);
        }
    }

    /**
     * 2.0 物业缴费时离散 报表数据
     *
     * @param feeId
     * @param detailId
     * @param communityId
     */
    @Async
    @Override
    public void payFeeDetailRefreshFeeMonth(String feeId, String detailId, String communityId) {
        // todo 查询费用
//        FeeDto feeDto = new FeeDto();
//        feeDto.setCommunityId(communityId);
//        feeDto.setFeeId(feeId);
//        List<FeeDto> tmpFeeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
//
//        Assert.listOnlyOne(tmpFeeDtos, "费用不存在");
//
//        // todo 准备离散的基础数据
//        PayFeeMonthOwnerDto payFeeMonthOwnerDto = payFeeMonthHelp.generatorOwnerRoom(feeDto);
//
//        payFeeMonthHelp.generatorPayFeeDetailMonth(feeDto, payFeeMonthOwnerDto);

    }

    /**
     * 生成单个费用 并 离散到月
     *
     * @param feeId
     * @param communityId
     */
    @Override
    public void doGeneratorOrRefreshFeeMonth(String feeId, String communityId) {

        // todo 查询费用
        FeeDto feeDto = new FeeDto();
        feeDto.setCommunityId(communityId);
        feeDto.setFeeId(feeId);
        List<FeeDto> tmpFeeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        Assert.listOnlyOne(tmpFeeDtos, "费用不存在");

        doGeneratorOrRefreshFeeMonth(tmpFeeDtos.get(0), communityId);
    }

    /**
     * 生成多个费用 并 离散到月
     *
     * @param feeIds
     * @param communityId
     */
    @Override
    @Async
    public void doGeneratorFeeMonths(List<String> feeIds, String communityId) {
//        for (String feeId : feeIds) {
//            doGeneratorOrRefreshFeeMonth(feeId, communityId);
//        }
    }

    @Override
    @Async
    public void doGeneratorOweFees(List<String> feeIds, String communityId) {
//        for (String feeId : feeIds) {
//            // todo 查询费用
//            FeeDto feeDto = new FeeDto();
//            feeDto.setCommunityId(communityId);
//            feeDto.setFeeId(feeId);
//            List<FeeDto> tmpFeeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
//
//           if(ListUtil.isNull(tmpFeeDtos)){
//               continue;
//           }
//            generatorOweFeeInnerServiceSMOImpl.computeOweFee(tmpFeeDtos.get(0));
//        }
    }


    /**
     * 删除月费用
     *
     * @param feeId
     * @param communityId
     */
    @Override
    public void deleteFeeMonth(String feeId, String communityId) {

        PayFeeDetailMonthPo payFeeDetailMonthPo = new PayFeeDetailMonthPo();
        payFeeDetailMonthPo.setFeeId(feeId);
        payFeeDetailMonthPo.setCommunityId(communityId);
        payFeeDetailMonthInnerServiceSMOImpl.deletePayFeeDetailMonth(payFeeDetailMonthPo);
    }

    private void doGeneratorOrRefreshFeeMonth(FeeDto feeDto, String communityId) {

        // todo 准备离散的基础数据
        PayFeeMonthOwnerDto payFeeMonthOwnerDto = payFeeMonthHelp.generatorOwnerRoom(feeDto);

        //todo 离散start_time 或者 pay_fee_detail_month 最大月份 到  deadlineTime 的数据
        payFeeMonthHelp.generatorPayFeeDetailMonth(feeDto, payFeeMonthOwnerDto);


    }


    private void doTmpFeeDtoMonths(String communityId, List<FeeDto> tmpFeeDtos) {
        for (FeeDto tmpFeeDto : tmpFeeDtos) {
            try {
                doGeneratorOrRefreshFeeMonth(tmpFeeDto, communityId);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("生成费用报表失败" + JSONObject.toJSONString(tmpFeeDto), e);
            }
        }
    }
}
