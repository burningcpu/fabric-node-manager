/**
 * Copyright 2014-2019  the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.webank.fabric.node.manager.api.role;

import com.alibaba.fastjson.JSON;
import com.webank.fabric.node.manager.common.exception.NodeMgrException;
import com.webank.fabric.node.manager.common.pojo.base.ConstantCode;
import com.webank.fabric.node.manager.common.pojo.role.RoleListParam;
import com.webank.fabric.node.manager.common.pojo.role.RoleDO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * services for role data.
 */
@Log4j2
@Service
public class RoleService {

    @Autowired
    private RoleMapper roleMapper;

    /**
     * query role count.
     */
    public int countOfRole(RoleListParam param) {
        log.debug("start countOfRole. param:{} ", JSON.toJSONString(param));
        Integer roleCount = roleMapper.countOfRole(param);
        int count = roleCount == null ? 0 : roleCount.intValue();
        log.debug("end countOfRole. count:{} ", count);
        return count;
    }

    /**
     * query role .
     */
    public List<RoleDO> listOfRole(RoleListParam param) {
        log.debug("start listOfRole. param:{} ", JSON.toJSONString(param));
        List<RoleDO> list = roleMapper.listOfRole(param);
        log.debug("end listOfRole. list:{} ", JSON.toJSONString(list));
        return list;
    }

    /**
     * query role by roleId.
     */
    public RoleDO queryRoleById(Integer roleId) {
        log.debug("start queryRoleById. roleId:{} ", roleId);
        RoleDO roleInfo = roleMapper.queryRoleById(roleId);
        log.debug("end queryRoleById. roleInfo:{} ", JSON.toJSONString(roleInfo));
        return roleInfo;
    }

    /**
     * check roleId.
     */
    public void roleIdExist(Integer roleId) throws NodeMgrException {
        log.debug("start roleIdExist. roleId:{} ", roleId);
        if (roleId == null) {
            log.info("fail roleIdExist. roleId is null ");
            throw new NodeMgrException(ConstantCode.ROLE_ID_EMPTY);
        }
        RoleDO roleInfo = roleMapper.queryRoleById(roleId);
        if (roleInfo == null) {
            log.info("fail roleIdExist. did not found role row by id");
            throw new NodeMgrException(ConstantCode.INVALID_ROLE_ID);
        }
        log.debug("end roleIdExist. ");
    }
}
