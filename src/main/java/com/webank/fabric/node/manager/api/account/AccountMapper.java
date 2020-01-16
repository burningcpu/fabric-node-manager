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
package com.webank.fabric.node.manager.api.account;

import com.webank.fabric.node.manager.common.pojo.account.AccountInfoDO;
import com.webank.fabric.node.manager.common.pojo.account.AccountListParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * mapper about account.
 */
@Repository
public interface AccountMapper {

    Integer addAccountRow(AccountInfoDO tbAccount);

    Integer updateAccountRow(AccountInfoDO tbAccount);

    AccountInfoDO queryByAccount(@Param("account") String account);

    Integer countOfAccount(@Param("account") String account);

    List<AccountInfoDO> listOfAccount(@Param("param") AccountListParam param);

    Integer deleteAccountRow(@Param("account") String account);
}
