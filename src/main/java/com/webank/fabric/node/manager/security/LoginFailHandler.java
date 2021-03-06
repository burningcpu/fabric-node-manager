/**
 * Copyright 2014-2019 the original author or authors.
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
package com.webank.fabric.node.manager.security;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.webank.fabric.node.manager.common.pojo.base.BaseResponse;
import com.webank.fabric.node.manager.common.pojo.base.ConstantCode;
import com.webank.fabric.node.manager.common.pojo.base.RetCode;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@Component("loginFailHandler")
public class LoginFailHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException ex) throws IOException {
        log.info("login fail", ex);
        String errorMsg = ex.getMessage();
        RetCode retCode = ConstantCode.PASSWORD_ERROR; // default password fail
        if (errorMsg.contains("code")) {
            retCode = JSONObject.parseObject(errorMsg, RetCode.class);
        }

        BaseResponse baseResponse = new BaseResponse(retCode);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(baseResponse));
    }
}
