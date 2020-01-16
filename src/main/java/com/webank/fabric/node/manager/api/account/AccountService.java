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

import com.alibaba.fastjson.JSON;
import com.webank.fabric.node.manager.api.role.RoleService;
import com.webank.fabric.node.manager.common.enums.AccountStatus;
import com.webank.fabric.node.manager.common.exception.NodeMgrException;
import com.webank.fabric.node.manager.common.pojo.account.AccountInfo;
import com.webank.fabric.node.manager.common.pojo.account.AccountInfoDO;
import com.webank.fabric.node.manager.common.pojo.account.AccountListParam;
import com.webank.fabric.node.manager.common.pojo.account.LoginInfo;
import com.webank.fabric.node.manager.common.pojo.base.ConstantCode;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * services for account data.
 */
@Log4j2
@Service
public class AccountService {

    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private RoleService roleService;
    @Qualifier(value = "bCryptPasswordEncoder")
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * login.
     */
    public AccountInfoDO login(LoginInfo loginInfo) throws NodeMgrException {
        log.info("start login. loginInfo:{}", JSON.toJSONString(loginInfo));
        String accountStr = loginInfo.getAccount();
        String passwordStr = loginInfo.getAccountPwd();

        // check account
        accountExist(accountStr);

        // check pwd
        if (StringUtils.isBlank(passwordStr)) {
            log.info("fail login. password is null");
            throw new NodeMgrException(ConstantCode.PASSWORD_ERROR);
        }
        // encode by bCryptPasswordEncoder
        AccountInfoDO accountRow = accountMapper.queryByAccount(accountStr);
        if (!passwordEncoder.matches(passwordStr, accountRow.getAccountPwd())) {
            // reset login fail time
            int loginFailTime = accountRow.getLoginFailTime() + 1;
            log.info("fail login. password error,loginFailTime:{}", loginFailTime);
            accountRow.setLoginFailTime(loginFailTime);
            accountMapper.updateAccountRow(accountRow);
            throw new NodeMgrException(ConstantCode.PASSWORD_ERROR);
        }

        return accountRow;
    }

    /**
     * add account row.
     */
    public void addAccountRow(AccountInfo accountInfo) throws NodeMgrException {
        log.debug("start addAccountRow.  AccountInfo:{} ", JSON.toJSONString(accountInfo));

        String accountStr = accountInfo.getAccount();
        Integer roleId = accountInfo.getRoleId();
        // check account
        accountNotExist(accountStr);
        // check role id
        roleService.roleIdExist(roleId);
        // encode password
        String encryptStr = passwordEncoder.encode(accountInfo.getAccountPwd());
        // add account row
        AccountInfoDO rowInfo = new AccountInfoDO(accountStr, encryptStr, roleId);
        Integer affectRow = accountMapper.addAccountRow(rowInfo);

        // check result
        checkDbAffectRow(affectRow);

        log.debug("end addAccountRow. affectRow:{}", affectRow);
    }

    /**
     * update account info.
     */
    public void updateAccountRow(String currentAccount, AccountInfo accountInfo)
            throws NodeMgrException {
        log.debug("start updateAccountRow.  currentAccount:{} AccountInfo:{} ", currentAccount,
                JSON.toJSONString(accountInfo));

        String accountStr = accountInfo.getAccount();
        // check account
        accountExist(accountStr);

        // query by account
        AccountInfoDO accountRow = accountMapper.queryByAccount(accountStr);

        // encode password
        if (StringUtils.isNoneBlank(accountInfo.getAccountPwd())) {
            String encryptStr = passwordEncoder.encode(accountInfo.getAccountPwd());
            accountRow.setAccountPwd(encryptStr);
            // the current user is admin
            if (!currentAccount.equals(accountStr)) {
                accountRow.setAccountStatus(AccountStatus.UNMODIFIEDPWD.getValue());
            }
        }
        accountRow.setRoleId(accountInfo.getRoleId());
        //accountRow.setDescription(accountInfo.getDescription());

        // update account info
        Integer affectRow = accountMapper.updateAccountRow(accountRow);

        // check result
        checkDbAffectRow(affectRow);

        log.debug("end updateAccountRow. affectRow:{}", affectRow);
    }

    /**
     * update password.
     */
    public void updatePassword(String targetAccount, String oldAccountPwd, String newAccountPwd)
            throws NodeMgrException {
        log.debug("start updatePassword. targetAccount:{} oldAccountPwd:{} newAccountPwd:{}",
                targetAccount, oldAccountPwd, newAccountPwd);

        // query target account info
        AccountInfoDO targetRow = accountMapper.queryByAccount(targetAccount);
        if (targetRow == null) {
            log.warn("fail updatePassword. not found target account row. targetAccount:{}",
                    targetAccount);
            throw new NodeMgrException(ConstantCode.ACCOUNT_NOT_EXISTS);
        }

        if (StringUtils.equals(oldAccountPwd, newAccountPwd)) {
            log.warn("fail updatePassword. the new password cannot be same as old ");
            throw new NodeMgrException(ConstantCode.NOW_PWD_EQUALS_OLD);
        }

        // check old password
        if (!passwordEncoder.matches(oldAccountPwd, targetRow.getAccountPwd())) {
            throw new NodeMgrException(ConstantCode.PASSWORD_ERROR);
        }

        // update password
        targetRow.setAccountPwd(passwordEncoder.encode(newAccountPwd));
        targetRow.setAccountStatus(AccountStatus.NORMAL.getValue());
        Integer affectRow = accountMapper.updateAccountRow(targetRow);

        // check result
        checkDbAffectRow(affectRow);

        log.debug("end updatePassword. affectRow:{}", affectRow);

    }

    /**
     * query account info by acountName.
     */
    public AccountInfoDO queryByAccount(String accountStr) {
        log.debug("start queryByAccount. accountStr:{} ", accountStr);
        AccountInfoDO accountRow = accountMapper.queryByAccount(accountStr);
        log.debug("end queryByAccount. accountRow:{} ", JSON.toJSONString(accountRow));
        return accountRow;
    }

    /**
     * query count of account.
     */
    public int countOfAccount(String account) {
        log.debug("start countOfAccount. account:{} ", account);
        Integer accountCount = accountMapper.countOfAccount(account);
        int count = accountCount == null ? 0 : accountCount.intValue();
        log.debug("end countOfAccount. count:{} ", count);
        return count;
    }

    /**
     * query account list.
     */
    public List<AccountInfoDO> listOfAccount(AccountListParam param) {
        log.debug("start listOfAccount. param:{} ", JSON.toJSONString(param));
        List<AccountInfoDO> list = accountMapper.listOfAccount(param);
        log.debug("end listOfAccount. list:{} ", JSON.toJSONString(list));
        return list;
    }

    /**
     * delete account info.
     */
    public void deleteAccountRow(String account) throws NodeMgrException {
        log.debug("start deleteAccountRow. account:{} ", account);

        // check account
        accountExist(account);

        // delete account row
        Integer affectRow = accountMapper.deleteAccountRow(account);

        // check result
        checkDbAffectRow(affectRow);

        log.debug("end deleteAccountRow. affectRow:{} ", affectRow);

    }


    /**
     * boolean account is exist.
     */
    public void accountExist(String account) throws NodeMgrException {
        if (StringUtils.isBlank(account)) {
            log.warn("fail isAccountExit. account:{}", account);
            throw new NodeMgrException(ConstantCode.ACCOUNT_NAME_EMPTY);
        }
        int count = countOfAccount(account);
        if (count == 0) {
            throw new NodeMgrException(ConstantCode.ACCOUNT_NOT_EXISTS);
        }
    }

    /**
     * boolean account is not exit.
     */
    public void accountNotExist(String account) throws NodeMgrException {
        if (StringUtils.isBlank(account)) {
            log.warn("fail isAccountExit. account:{}", account);
            throw new NodeMgrException(ConstantCode.ACCOUNT_NAME_EMPTY);
        }
        int count = countOfAccount(account);
        if (count > 0) {
            throw new NodeMgrException(ConstantCode.ACCOUNT_EXISTS);
        }
    }

    /**
     * check db affect row.
     */
    private void checkDbAffectRow(Integer affectRow) throws NodeMgrException {
        if (affectRow == 0) {
            log.warn("affect 0 rows of tb_account");
            throw new NodeMgrException(ConstantCode.DB_EXCEPTION);
        }
    }

}