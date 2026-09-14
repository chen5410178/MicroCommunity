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
package com.java110.user.bmo.privilege.impl;

import com.alibaba.fastjson.JSONArray;
import com.java110.user.bmo.privilege.IPrivilegeAssignBMO;
import com.java110.user.dao.IPrivilegeAssignServiceDao;
import com.java110.utils.constant.StoreUserRelConstant;
import com.java110.utils.exception.CmdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 权限可分配范围业务实现，仅依赖自研 DAO，不经公共查询
 */
@Service(value = "privilegeAssignBMOImpl")
public class PrivilegeAssignBMOImpl implements IPrivilegeAssignBMO {

    @Autowired
    private IPrivilegeAssignServiceDao privilegeAssignServiceDaoImpl;

    @Override
    public boolean isStoreAdmin(String storeId, String userId) {
        Map param = new HashMap();
        param.put("userId", userId);
        param.put("storeId", storeId);
        param.put("relCd", StoreUserRelConstant.REL_ADMIN);
        return privilegeAssignServiceDaoImpl.queryStoreAdminCount(param) > 0;
    }

    @Override
    public Set<String> queryOwnPrivilegeIds(String userId, String domain) {
        Map info = new HashMap();
        info.put("userId", userId);
        info.put("domain", domain);
        List<Map> rows = privilegeAssignServiceDaoImpl.queryUserPrivilegeIds(info);
        Set<String> ownPIds = new HashSet<>();
        for (Map row : rows) {
            if (row.get("pId") != null) {
                ownPIds.add(row.get("pId").toString());
            }
        }
        return ownPIds;
    }

    @Override
    public void filterAssignable(JSONArray privileges, String storeId, String userId, String domain) {
        if (privileges == null) {
            return;
        }
        if (isStoreAdmin(storeId, userId)) {
            return;
        }
        Set<String> ownPIds = queryOwnPrivilegeIds(userId, domain);
        for (int i = privileges.size() - 1; i >= 0; i--) {
            if (!ownPIds.contains(privileges.getJSONObject(i).getString("pId"))) {
                privileges.remove(i);
            }
        }
    }

    @Override
    public void validateAssignable(String storeId, String userId, String domain, List<String> pIds) {
        if (pIds == null || pIds.isEmpty()) {
            return;
        }
        if (isStoreAdmin(storeId, userId)) {
            return;
        }
        Set<String> ownPIds = queryOwnPrivilegeIds(userId, domain);
        for (String pId : pIds) {
            if (!ownPIds.contains(pId)) {
                throw new CmdException("只能分配自己拥有的权限");
            }
        }
    }
}
