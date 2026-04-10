/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.fee.smo.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.PageDto;
import com.java110.dto.fee.*;
import com.java110.fee.dao.IPayFeeDetailNewV1ServiceDao;
import com.java110.intf.fee.*;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeeConfigPo;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.fee.PayFeePo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 类表述： 服务之前调用的接口实现类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2021-12-06 21:10:16 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class PayFeeDetailV1InnerServiceSMOImpl extends BaseServiceSMO implements IPayFeeDetailV1InnerServiceSMO {

    @Autowired
    private IPayFeeDetailNewV1ServiceDao payFeeDetailNewV1ServiceDaoImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeConfigV1InnerServiceSMO payFeeConfigV1InnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Override
    @Java110Transactional
    public int savePayFeeDetailNew(@RequestBody PayFeeDetailPo payFeeDetailPo) {
        int saveFlag = payFeeDetailNewV1ServiceDaoImpl.savePayFeeDetailNewInfo(BeanConvertUtil.beanCovertMap(payFeeDetailPo));



        return saveFlag;
    }

    /**
     * 生成欠费
     *
     * @param payFeeDetailPo
     * @param oweAmount
     */
    private void generatorOweFee(PayFeeDetailPo payFeeDetailPo, double oweAmount) {

        FeeDto feeDto = new FeeDto();
        feeDto.setFeeId(payFeeDetailPo.getFeeId());
        feeDto.setCommunityId(payFeeDetailPo.getCommunityId());
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
        if (ListUtil.isNull(feeDtos)) {
            return;
        }

        FeeConfigDto feeConfigDto = getFeeConfigDto(payFeeDetailPo.getFeeId(), payFeeDetailPo.getCommunityId(), feeDtos.get(0));

        if (feeConfigDto == null) {
            return;
        }
        PayFeePo payFeePo = new PayFeePo();
        payFeePo.setCommunityId(payFeeDetailPo.getCommunityId());
        payFeePo.setConfigId(feeConfigDto.getConfigId());
        payFeePo.setPayerObjType(feeDtos.get(0).getPayerObjType());
        payFeePo.setStartTime(DateUtil.getFormatTimeStringA(feeDtos.get(0).getStartTime()));
        payFeePo.setEndTime(payFeeDetailPo.getStartTime());
        payFeePo.setAmount(oweAmount + "");
        payFeePo.setFeeFlag(feeConfigDto.getFeeFlag());
        payFeePo.setFeeTypeCd(feeConfigDto.getFeeTypeCd());
        payFeePo.setIncomeObjId(feeDtos.get(0).getIncomeObjId());
        payFeePo.setBatchId("-1");
        payFeePo.setState(FeeDto.STATE_DOING);

        payFeePo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
        payFeePo.setPayerObjId(feeDtos.get(0).getPayerObjId());
        payFeePo.setUserId("-1");
        payFeePo.setCreateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));


        List<FeeAttrDto> feeAttrDtos = feeDtos.get(0).getFeeAttrDtos();
        if (ListUtil.isNull(feeAttrDtos)) {
            return;
        }
        List<FeeAttrPo> feeAttrsPos = new ArrayList<>();

        for (FeeAttrDto feeAttrDto : feeAttrDtos) {
            if(FeeAttrDto.SPEC_CD_ONCE_FEE_DEADLINE_TIME.equals(feeAttrDto.getSpecCd())){
                feeAttrsPos.add(addFeeAttr(payFeePo, feeAttrDto.getSpecCd(), payFeeDetailPo.getEndTime()));
                continue;
            }
            feeAttrsPos.add(addFeeAttr(payFeePo, feeAttrDto.getSpecCd(), feeAttrDto.getValue()));
        }

        feeAttrInnerServiceSMOImpl.saveFeeAttrs(feeAttrsPos);


        payFeeV1InnerServiceSMOImpl.savePayFee(payFeePo);


    }

    private FeeConfigDto getFeeConfigDto(String feeId, String communityId, FeeDto feeDto) {


        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setConfigId(feeDto.getConfigId());
        feeConfigDto.setCommunityId(communityId);
        List<FeeConfigDto> feeConfigDtos = payFeeConfigV1InnerServiceSMOImpl.queryPayFeeConfigs(feeConfigDto);
        if (ListUtil.isNull(feeConfigDtos)) {
            return null;
        }

        FeeConfigDto newFeeConfigDto = new FeeConfigDto();
        newFeeConfigDto.setFeeName(feeConfigDtos.get(0).getFeeName());
        newFeeConfigDto.setCommunityId(communityId);
        newFeeConfigDto.setFeeTypeCd(feeConfigDtos.get(0).getFeeTypeCd());
        newFeeConfigDto.setFtId(feeConfigDtos.get(0).getFtId());
        newFeeConfigDto.setFeeFlag(FeeDto.FEE_FLAG_ONCE);
        newFeeConfigDto.setComputingFormula(FeeConfigDto.COMPUTING_FORMULA_DYNAMIC);
        List<FeeConfigDto> newFeeConfigDtos = payFeeConfigV1InnerServiceSMOImpl.queryPayFeeConfigs(newFeeConfigDto);

        if (!ListUtil.isNull(newFeeConfigDtos)) {
            return newFeeConfigDtos.get(0);
        }

        PayFeeConfigPo payFeeConfigPo = new PayFeeConfigPo();
        payFeeConfigPo.setConfigId(GenerateCodeFactory.getGeneratorId("11"));
        payFeeConfigPo.setCommunityId(communityId);
        payFeeConfigPo.setFeeTypeCd(feeConfigDtos.get(0).getFeeTypeCd());
        payFeeConfigPo.setFtId(feeConfigDtos.get(0).getFtId());
        payFeeConfigPo.setSquarePrice("0");
        payFeeConfigPo.setAdditionalAmount("0");
        payFeeConfigPo.setIsDefault(FeeConfigDto.DEFAULT_FEE_CONFIG);
        payFeeConfigPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        payFeeConfigPo.setEndTime(DateUtil.getLastTime());
        payFeeConfigPo.setFeeFlag(FeeDto.FEE_FLAG_ONCE);
        payFeeConfigPo.setFeeName(feeConfigDtos.get(0).getFeeName());
        payFeeConfigPo.setComputingFormula(FeeConfigDto.COMPUTING_FORMULA_DYNAMIC);
        payFeeConfigPo.setBillType(FeeConfigDto.BILL_TYPE_YEAR);
        payFeeConfigPo.setPaymentCd(FeeConfigDto.PAYMENT_CD_PRE);
        payFeeConfigPo.setPaymentCycle("1");
        payFeeConfigPo.setComputingFormulaText("");
        payFeeConfigPo.setDeductFrom(FeeConfigDto.DEDUCT_FROM_N);
        payFeeConfigPo.setPayOnline("Y");
        payFeeConfigPo.setScale("1");
        payFeeConfigPo.setDecimalPlace("2");
        payFeeConfigPo.setUnits("元");
        payFeeConfigPo.setPrepaymentPeriod("1");
        payFeeConfigPo.setState("Y");
        payFeeConfigV1InnerServiceSMOImpl.savePayFeeConfig(payFeeConfigPo);

        newFeeConfigDto = new FeeConfigDto();
        newFeeConfigDto.setConfigId(payFeeConfigPo.getConfigId());
        newFeeConfigDtos = payFeeConfigV1InnerServiceSMOImpl.queryPayFeeConfigs(newFeeConfigDto);

        return newFeeConfigDtos.get(0);

    }

    public FeeAttrPo addFeeAttr(PayFeePo payFeePo, String specCd, String value) {
        FeeAttrPo feeAttrPo = new FeeAttrPo();
        feeAttrPo.setCommunityId(payFeePo.getCommunityId());
        feeAttrPo.setSpecCd(specCd);
        feeAttrPo.setValue(value);
        feeAttrPo.setFeeId(payFeePo.getFeeId());
        feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
        return feeAttrPo;

    }

    @Override
    public int updatePayFeeDetailNew(@RequestBody PayFeeDetailPo PayFeeDetailPo) {
        int saveFlag = payFeeDetailNewV1ServiceDaoImpl.updatePayFeeDetailNewInfo(BeanConvertUtil.beanCovertMap(PayFeeDetailPo));
        return saveFlag;
    }

    @Override
    public int deletePayFeeDetailNew(@RequestBody PayFeeDetailPo PayFeeDetailPo) {
        PayFeeDetailPo.setStatusCd("1");
        int saveFlag = payFeeDetailNewV1ServiceDaoImpl.updatePayFeeDetailNewInfo(BeanConvertUtil.beanCovertMap(PayFeeDetailPo));
        return saveFlag;
    }

    @Override
    public List<PayFeeDetailDto> queryPayFeeDetailNew(@RequestBody PayFeeDetailDto payFeeDetailDto) {
        //校验是否传了 分页信息
        int page = payFeeDetailDto.getPage();
        if (page != PageDto.DEFAULT_PAGE) {
            payFeeDetailDto.setPage((page - 1) * payFeeDetailDto.getRow());
        }
        List<PayFeeDetailDto> payFeeDetailNews = BeanConvertUtil.covertBeanList(payFeeDetailNewV1ServiceDaoImpl.queryPayFeeDetailNewInfo(BeanConvertUtil.beanCovertMap(payFeeDetailDto)), PayFeeDetailDto.class);
        return payFeeDetailNews;
    }

    @Override
    public List<PayFeeDetailDto> queryPayFeeDetailSimple(@RequestBody PayFeeDetailDto payFeeDetailDto) {
        //校验是否传了 分页信息
        int page = payFeeDetailDto.getPage();
        if (page != PageDto.DEFAULT_PAGE) {
            payFeeDetailDto.setPage((page - 1) * payFeeDetailDto.getRow());
        }
        List<PayFeeDetailDto> payFeeDetailNews = BeanConvertUtil.covertBeanList(payFeeDetailNewV1ServiceDaoImpl.queryPayFeeDetailSimple(BeanConvertUtil.beanCovertMap(payFeeDetailDto)), PayFeeDetailDto.class);
        return payFeeDetailNews;
    }

    @Override
    public int queryPayFeeDetailNewCount(@RequestBody PayFeeDetailDto payFeeDetailDto) {
        return payFeeDetailNewV1ServiceDaoImpl.queryPayFeeDetailNewCount(BeanConvertUtil.beanCovertMap(payFeeDetailDto));
    }

    @Override
    public List<PayFeeDetailDto> queryPayFeeDetailNewSum(@RequestBody PayFeeDetailDto payFeeDetailDto) {
        //校验是否传了 分页信息
        int page = payFeeDetailDto.getPage();
        if (page != PageDto.DEFAULT_PAGE) {
            payFeeDetailDto.setPage((page - 1) * payFeeDetailDto.getRow());
        }
        List<PayFeeDetailDto> payFeeDetailNews = BeanConvertUtil.covertBeanList(payFeeDetailNewV1ServiceDaoImpl.queryPayFeeDetailNewSumInfo(BeanConvertUtil.beanCovertMap(payFeeDetailDto)), PayFeeDetailDto.class);
        return payFeeDetailNews;
    }

    @Override
    public int queryFeeDepositCount(@RequestBody FeeDetailDto feeDetailDto) {
        return payFeeDetailNewV1ServiceDaoImpl.queryFeeDepositCount(BeanConvertUtil.beanCovertMap(feeDetailDto));
    }

    @Override
    public List<FeeDetailDto> queryFeeDeposit(@RequestBody FeeDetailDto feeDetailDto) {
        //校验是否传了 分页信息
        int page = feeDetailDto.getPage();
        if (page != PageDto.DEFAULT_PAGE) {
            feeDetailDto.setPage((page - 1) * feeDetailDto.getRow());
        }
        List<FeeDetailDto> feeDetailDtos = BeanConvertUtil.covertBeanList(payFeeDetailNewV1ServiceDaoImpl.queryFeeDeposit(BeanConvertUtil.beanCovertMap(feeDetailDto)), FeeDetailDto.class);
        return feeDetailDtos;
    }
//
//    @Override
//    public List<PayFeeDetailNewDto> queryPayFeeDetailNews(@RequestBody  PayFeeDetailNewDto payFeeDetailNewDto) {
//
//        //校验是否传了 分页信息
//
//        int page = payFeeDetailNewDto.getPage();
//
//        if (page != PageDto.DEFAULT_PAGE) {
//            payFeeDetailNewDto.setPage((page - 1) * payFeeDetailNewDto.getRow());
//        }
//
//        List<PayFeeDetailNewDto> payFeeDetailNews = BeanConvertUtil.covertBeanList(payFeeDetailNewV1ServiceDaoImpl.getPayFeeDetailNewInfo(BeanConvertUtil.beanCovertMap(payFeeDetailNewDto)), PayFeeDetailNewDto.class);
//
//        return payFeeDetailNews;
//    }
//
//
//    @Override
//    public int queryPayFeeDetailNewsCount(@RequestBody PayFeeDetailNewDto payFeeDetailNewDto) {
//        return payFeeDetailNewV1ServiceDaoImpl.queryPayFeeDetailNewsCount(BeanConvertUtil.beanCovertMap(payFeeDetailNewDto));    }

}
