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


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.prePayFeeDetail.PrePayFeeDetailDto;
import com.java110.fee.dao.IPrePayFeeDetailV1ServiceDao;
import com.java110.intf.fee.IPrePayFeeDetailV1InnerServiceSMO;
import com.java110.po.prePayFeeDetail.PrePayFeeDetailPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 类表述： 服务之前调用的接口实现类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2025-06-25 10:34:36 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class PrePayFeeDetailV1InnerServiceSMOImpl extends BaseServiceSMO implements IPrePayFeeDetailV1InnerServiceSMO {

    @Autowired
    private IPrePayFeeDetailV1ServiceDao prePayFeeDetailV1ServiceDaoImpl;


    @Override
    public int savePrePayFeeDetail(@RequestBody  PrePayFeeDetailPo prePayFeeDetailPo) {
        int saveFlag = prePayFeeDetailV1ServiceDaoImpl.savePrePayFeeDetailInfo(BeanConvertUtil.beanCovertMap(prePayFeeDetailPo));
        return saveFlag;
    }

     @Override
    public int updatePrePayFeeDetail(@RequestBody  PrePayFeeDetailPo prePayFeeDetailPo) {
        int saveFlag = prePayFeeDetailV1ServiceDaoImpl.updatePrePayFeeDetailInfo(BeanConvertUtil.beanCovertMap(prePayFeeDetailPo));
        return saveFlag;
    }

     @Override
    public int deletePrePayFeeDetail(@RequestBody  PrePayFeeDetailPo prePayFeeDetailPo) {
       prePayFeeDetailPo.setStatusCd("1");
       int saveFlag = prePayFeeDetailV1ServiceDaoImpl.updatePrePayFeeDetailInfo(BeanConvertUtil.beanCovertMap(prePayFeeDetailPo));
       return saveFlag;
    }

    @Override
    public List<PrePayFeeDetailDto> queryPrePayFeeDetails(@RequestBody  PrePayFeeDetailDto prePayFeeDetailDto) {

        //校验是否传了 分页信息

        int page = prePayFeeDetailDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            prePayFeeDetailDto.setPage((page - 1) * prePayFeeDetailDto.getRow());
        }

        List<PrePayFeeDetailDto> prePayFeeDetails = BeanConvertUtil.covertBeanList(prePayFeeDetailV1ServiceDaoImpl.getPrePayFeeDetailInfo(BeanConvertUtil.beanCovertMap(prePayFeeDetailDto)), PrePayFeeDetailDto.class);

        return prePayFeeDetails;
    }


    @Override
    public int queryPrePayFeeDetailsCount(@RequestBody PrePayFeeDetailDto prePayFeeDetailDto) {
        return prePayFeeDetailV1ServiceDaoImpl.queryPrePayFeeDetailsCount(BeanConvertUtil.beanCovertMap(prePayFeeDetailDto));    }

}
