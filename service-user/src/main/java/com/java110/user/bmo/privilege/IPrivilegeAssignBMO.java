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
package com.java110.user.bmo.privilege;

import com.alibaba.fastjson.JSONArray;

import java.util.List;
import java.util.Set;

/**
 * 权限可分配范围业务接口
 */
public interface IPrivilegeAssignBMO {

    /**
     * 是否商户管理员（创建物业公司生成的账号）
     *
     * @param storeId 商户ID
     * @param userId  用户ID
     * @return true 管理员
     */
    boolean isStoreAdmin(String storeId, String userId);

    /**
     * 操作人拥有的权限ID集合（组权限 与 直授权限，按域过滤）
     *
     * @param userId 用户ID
     * @param domain 商户类型
     * @return 权限ID集合
     */
    Set<String> queryOwnPrivilegeIds(String userId, String domain);

    /**
     * 非管理员时，就地剔除权限树中操作人不拥有的节点（管理员不处理）
     *
     * @param privileges 权限树数据
     * @param storeId    商户ID
     * @param userId     用户ID
     * @param domain     商户类型
     */
    void filterAssignable(JSONArray privileges, String storeId, String userId, String domain);

    /**
     * 校验待分配权限是否都在操作人权限范围内，越权则抛 CmdException
     *
     * @param storeId 商户ID
     * @param userId  用户ID
     * @param domain  商户类型
     * @param pIds    待分配权限ID
     */
    void validateAssignable(String storeId, String userId, String domain, List<String> pIds);
}
