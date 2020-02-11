
/**
 * Copyright 2014-2019  the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.webank.fabric.node.manager.api.account;

import com.alibaba.fastjson.JSON;
import com.webank.fabric.node.manager.common.enums.SqlSortType;
import com.webank.fabric.node.manager.common.exception.NodeMgrException;
import com.webank.fabric.node.manager.common.pojo.account.AccountInfo;
import com.webank.fabric.node.manager.common.pojo.account.AccountInfoDO;
import com.webank.fabric.node.manager.common.pojo.account.AccountListParam;
import com.webank.fabric.node.manager.common.pojo.account.PasswordInfo;
import com.webank.fabric.node.manager.common.pojo.base.BasePageResponse;
import com.webank.fabric.node.manager.common.pojo.base.BaseResponse;
import com.webank.fabric.node.manager.common.pojo.base.ConstantCode;
import com.webank.fabric.node.manager.common.pojo.imagecode.ImageToken;
import com.webank.fabric.node.manager.common.utils.NodeMgrUtils;
import com.webank.fabric.node.manager.imagecode.TokenImgGenerator;
import com.webank.fabric.node.manager.token.TokenService;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Log4j2
@RestController
@RequestMapping(value = "account")
@Api(value = "account", tags = "about login account")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private TokenService tokenService;
    private static final int PICTURE_CHECK_CODE_CHAR_NUMBER = 4;

    /**
     * 获取验证码
     */
    @GetMapping(value = "pictureCheckCode")
    public BaseResponse getPictureCheckCode() throws Exception {
        log.info("start getPictureCheckCode");

        // random code
        String checkCode = NodeMgrUtils.randomString(PICTURE_CHECK_CODE_CHAR_NUMBER);

        String token = tokenService.createToken(checkCode, 2);
        log.info("new checkCode:" + checkCode);

        BaseResponse baseResponse = new BaseResponse(ConstantCode.SUCCESS);
        try {
            // 得到图形验证码并返回给页面
            String base64Image = TokenImgGenerator.getBase64Image(checkCode);
            ImageToken tokenData = new ImageToken();
            tokenData.setToken(token);
            tokenData.setBase64Image(base64Image);
            baseResponse.setData(tokenData);
            log.info("end getPictureCheckCode. baseResponse:{}", JSON.toJSONString(baseResponse));
            return baseResponse;
        } catch (Exception ex) {
            log.error("fail getPictureCheckCode", ex);
            throw new NodeMgrException(ConstantCode.SYSTEM_EXCEPTION,ex);
        }
    }


    /**
     * add account info.
     */
    @PostMapping(value = "/accountInfo")
    ////@PreAuthorize(ConstantProperties.HAS_ROLE_ADMIN)
    public BaseResponse addAccountInfo(@RequestBody @Valid AccountInfo info)
            throws NodeMgrException {
        BaseResponse baseResponse = new BaseResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info("start addAccountInfo. startTime:{} accountInfo:{}", startTime.toEpochMilli(),
                JSON.toJSONString(info));

        // add account row
        accountService.addAccountRow(info);

        // query row
        AccountInfoDO tbAccount = accountService.queryByAccount(info.getAccount());
        tbAccount.setAccountPwd(null);
        baseResponse.setData(tbAccount);

        log.info("end addAccountInfo useTime:{} result:{}",
                Duration.between(startTime, Instant.now()).toMillis(), JSON.toJSONString(baseResponse));
        return baseResponse;
    }

    /**
     * update account info.
     */
    @PutMapping(value = "/accountInfo")
    //@PreAuthorize(ConstantProperties.HAS_ROLE_ADMIN)
    public BaseResponse updateAccountInfo(@RequestBody @Valid AccountInfo info, HttpServletRequest request) throws NodeMgrException {
        BaseResponse baseResponse = new BaseResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info("start updateAccountInfo startTime:{} accountInfo:{}", startTime.toEpochMilli(),
                JSON.toJSONString(info));

        // current
        String currentAccount = getCurrentAccount(request);

        // update account row
        accountService.updateAccountRow(currentAccount, info);

        // query row
        AccountInfoDO tbAccount = accountService.queryByAccount(info.getAccount());
        tbAccount.setAccountPwd(null);
        baseResponse.setData(tbAccount);

        log.info("end updateAccountInfo useTime:{} result:{}",
                Duration.between(startTime, Instant.now()).toMillis(),
                JSON.toJSONString(baseResponse));
        return baseResponse;
    }

    /**
     * query account list.
     */
    @GetMapping(value = "/accountList/{pageNumber}/{pageSize}")
    ////@PreAuthorize(ConstantProperties.HAS_ROLE_ADMIN)
    public BasePageResponse queryAccountList(@PathVariable("pageNumber") Integer pageNumber,
                                             @PathVariable("pageSize") Integer pageSize,
                                             @RequestParam(value = "account", required = false) String account) throws NodeMgrException {
        BasePageResponse pagesponse = new BasePageResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info("start queryAccountList.  startTime:{} pageNumber:{} pageSize:{} account:{} ",
                startTime.toEpochMilli(), pageNumber, pageSize, account);

        int count = accountService.countOfAccount(account);
        if (count > 0) {
            Integer start = Optional.ofNullable(pageNumber).map(page -> (page - 1) * pageSize)
                    .orElse(0);
            AccountListParam param = new AccountListParam(start, pageSize, account,
                    SqlSortType.DESC.getValue());
            List<AccountInfoDO> listOfAccount = accountService.listOfAccount(param);
            listOfAccount.stream().forEach(accountData -> accountData.setAccountPwd(null));
            pagesponse.setData(listOfAccount);
            pagesponse.setTotalCount(count);
        }

        log.info("end queryAccountList useTime:{} result:{}",
                Duration.between(startTime, Instant.now()).toMillis(), JSON.toJSONString(pagesponse));
        return pagesponse;
    }

    /**
     * delete contract by id.
     */
    @DeleteMapping(value = "/{account}")
    ////@PreAuthorize(ConstantProperties.HAS_ROLE_ADMIN)
    public BaseResponse deleteAccount(@PathVariable("account") String account)
            throws NodeMgrException {
        BaseResponse baseResponse = new BaseResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info("start deleteAccount. startTime:{} account:{}", startTime.toEpochMilli(), account);

        accountService.deleteAccountRow(account);

        log.info("end deleteAccount. useTime:{} result:{}",
                Duration.between(startTime, Instant.now()).toMillis(), JSON.toJSONString(baseResponse));
        return baseResponse;
    }

    /**
     * update password.
     */
    @PutMapping(value = "/passwordUpdate")
    public BaseResponse updatePassword(@RequestBody @Valid PasswordInfo info, HttpServletRequest request) throws NodeMgrException {
        BaseResponse baseResponse = new BaseResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info("start updatePassword startTime:{} passwordInfo:{}", startTime.toEpochMilli(),
                JSON.toJSONString(info));

        String targetAccount = getCurrentAccount(request);

        // update account row
        accountService
                .updatePassword(targetAccount, info.getOldAccountPwd(), info.getNewAccountPwd());

        log.info("end updatePassword useTime:{} result:{}",
                Duration.between(startTime, Instant.now()).toMillis(), JSON.toJSONString(baseResponse));
        return baseResponse;
    }

    /**
     * get current account.
     */
    private String getCurrentAccount(HttpServletRequest request) {
        String token = NodeMgrUtils.getToken(request);
        return tokenService.getValueFromToken(token);
    }
}
