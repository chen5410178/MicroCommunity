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
package com.java110.user.dao;

import java.util.List;
import java.util.Map;

/**
 * 权限可分配范围 与数据库交互
 */
public interface IPrivilegeAssignServiceDao {

    /**
     * 查询操作人拥有的权限ID（权限组带来的权限 与 直接分配的权限）
     *
     * @param info userId、domain
     * @return 权限ID集合
     */
    List<Map> queryUserPrivilegeIds(Map info);

    /**
     * 查询该用户是否为商户管理员（s_store_user.rel_cd = 600311000001）
     *
     * @param info userId、storeId、relCd
     * @return 记录数
     */
    int queryStoreAdminCount(Map info);
}
