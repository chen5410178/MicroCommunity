package com.java110.job.task.fee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.client.RestTemplate;
import com.java110.core.factory.CallApiServiceFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.account.AccountDto;
import com.java110.dto.app.AppDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.fee.PayFeeDto;
import com.java110.dto.log.LogSystemErrorDto;
import com.java110.dto.prePayFee.PrePayFeeDto;
import com.java110.dto.store.StoreUserDto;
import com.java110.dto.task.TaskDto;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.intf.common.ILogSystemErrorInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IPayFeeV1InnerServiceSMO;
import com.java110.intf.fee.IPrePayFeeV1InnerServiceSMO;
import com.java110.intf.fee.IRuleGeneratorPayFeeBillV1InnerServiceSMO;
import com.java110.intf.store.IStoreUserV1InnerServiceSMO;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.po.account.AccountDetailPo;
import com.java110.po.log.LogSystemErrorPo;
import com.java110.po.prePayFee.PrePayFeePo;
import com.java110.service.smo.ISaveSystemErrorSMO;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName GenerateOwnerBillTemplate
 * @Description TODO  预付款扣款缴费
 * @Author wuxw
 * @Date 2020/6/4 8:33
 * @Version 1.0
 * add by wuxw 2020/6/4
 **/
@Component
public class AccountDeductionPayFeeTemplate extends TaskSystemQuartz {


    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    @Autowired
    private IPrePayFeeV1InnerServiceSMO prePayFeeV1InnerServiceSMOImpl;
    @Autowired
    private ILogSystemErrorInnerServiceSMO logSystemErrorInnerServiceSMOImpl;

    @Autowired
    private ISaveSystemErrorSMO saveSystemErrorSMOImpl;


    @Autowired
    private IRuleGeneratorPayFeeBillV1InnerServiceSMO ruleGeneratorPayFeeBillV1InnerServiceSMOImpl;

    @Autowired
    private IStoreUserV1InnerServiceSMO storeUserV1InnerServiceSMOImpl;

    @Autowired
    RestTemplate restTemplate;

    @Override
    protected void process(TaskDto taskDto) throws Exception {

        // 获取小区
        List<CommunityDto> communityDtos = getAllCommunity();

        for (CommunityDto communityDto : communityDtos) {
            doDeductionPayFeeFee(taskDto, communityDto);

//            if("20250530986313016".equals(communityDto.getCommunityId())) {
//            }
        }

    }

    /**
     * 根据小区生成账单
     *
     * @param communityDto
     */
    private void doDeductionPayFeeFee(TaskDto taskDto, CommunityDto communityDto) {

        //查询费用项
        PrePayFeeDto prePayFeeDto = new PrePayFeeDto();
        prePayFeeDto.setCommunityId(communityDto.getCommunityId());
        prePayFeeDto.setHasMoney("1");
        prePayFeeDto.setWay(PrePayFeeDto.WAY_YES);
        List<PrePayFeeDto> prePayFeeDtos = prePayFeeV1InnerServiceSMOImpl.queryPrePayFees(prePayFeeDto);

        if (ListUtil.isNull(prePayFeeDtos)) {
            return;
        }

        for (PrePayFeeDto tmpPrePayFeeDto : prePayFeeDtos) {
            try {
                ruleGeneratorPayFeeBillV1InnerServiceSMOImpl.deductionPayFee(tmpPrePayFeeDto);
            } catch (Exception e) {
                LogSystemErrorPo logSystemErrorPo = new LogSystemErrorPo();
                logSystemErrorPo.setErrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_errId));
                logSystemErrorPo.setErrType(LogSystemErrorDto.ERR_TYPE_JOB);
                logSystemErrorPo.setMsg(ExceptionUtil.getStackTrace(e));
                saveSystemErrorSMOImpl.saveLog(logSystemErrorPo);
                logger.error("生成费用失败", e);
            }
        }

    }


}
