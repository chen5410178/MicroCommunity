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
package com.java110.user.dao.impl;

import com.java110.core.base.dao.BaseServiceDao;
import com.java110.user.dao.IPrivilegeAssignServiceDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 权限可分配范围 与数据库交互
 */
@Service("privilegeAssignServiceDaoImpl")
public class PrivilegeAssignServiceDaoImpl extends BaseServiceDao implements IPrivilegeAssignServiceDao {

    @Override
    public List<Map> queryUserPrivilegeIds(Map info) {
        return sqlSessionTemplate.selectList("privilegeAssignServiceDaoImpl.queryUserPrivilegeIds", info);
    }

    @Override
    public int queryStoreAdminCount(Map info) {
        return sqlSessionTemplate.selectOne("privilegeAssignServiceDaoImpl.queryStoreAdminCount", info);
    }
}
