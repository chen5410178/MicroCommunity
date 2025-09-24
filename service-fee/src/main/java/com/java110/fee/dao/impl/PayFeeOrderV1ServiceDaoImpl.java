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
package com.java110.fee.dao.impl;

import com.java110.core.base.dao.BaseServiceDao;
import com.java110.fee.dao.IPayFeeOrderV1ServiceDao;
import com.java110.utils.exception.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 类表述：
 * add by 吴学文 at 2025-07-16 16:05:34 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Service("payFeeOrderV1ServiceDaoImpl")
public class PayFeeOrderV1ServiceDaoImpl extends BaseServiceDao implements IPayFeeOrderV1ServiceDao {

    private static Logger logger = LoggerFactory.getLogger(PayFeeOrderV1ServiceDaoImpl.class);





    /**
     * 保存缴费订单信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public int savePayFeeOrderInfo(Map info) throws DAOException {
        logger.debug("保存 savePayFeeOrderInfo 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("payFeeOrderV1ServiceDaoImpl.savePayFeeOrderInfo",info);

        return saveFlag;
    }


    /**
     * 查询缴费订单信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getPayFeeOrderInfo(Map info) throws DAOException {
        logger.debug("查询 getPayFeeOrderInfo 入参 info : {}",info);

        List<Map> infos = sqlSessionTemplate.selectList("payFeeOrderV1ServiceDaoImpl.getPayFeeOrderInfo",info);

        return infos;
    }


    /**
     * 修改缴费订单信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public int updatePayFeeOrderInfo(Map info) throws DAOException {
        logger.debug("修改 updatePayFeeOrderInfo 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("payFeeOrderV1ServiceDaoImpl.updatePayFeeOrderInfo",info);

        return saveFlag;
    }

     /**
     * 查询缴费订单数量
     * @param info 缴费订单信息
     * @return 缴费订单数量
     */
    @Override
    public Map queryPayFeeOrdersCount(Map info) {
        logger.debug("查询 queryPayFeeOrdersCount 入参 info : {}",info);

        List<Map> infos = sqlSessionTemplate.selectList("payFeeOrderV1ServiceDaoImpl.queryPayFeeOrdersCount", info);
        if (infos.size() < 1) {
            return null;
        }

        return infos.get(0);
    }


}
