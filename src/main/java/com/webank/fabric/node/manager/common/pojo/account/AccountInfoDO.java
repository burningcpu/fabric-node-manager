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
package com.webank.fabric.node.manager.common.pojo.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity class of table tb_account.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfoDO {

    private String account;
    private String accountPwd;
    private Integer roleId;
    private String roleName;
    private String roleNameZh;
    private int loginFailTime;
    private Integer accountStatus;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;

    /**
     * init by account、accountPwd、roleId、description.
     */
    public AccountInfoDO(String account, String accountPwd, Integer roleId) {
        super();
        this.account = account;
        this.accountPwd = accountPwd;
        this.roleId = roleId;
    }
}
