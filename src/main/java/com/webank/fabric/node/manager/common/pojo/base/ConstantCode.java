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
package com.webank.fabric.node.manager.common.pojo.base;

/**
 * A-BB-CCC A:error level. <br/>
 * 1:system exception <br/>
 * 2:business exception <br/>
 * B:project number <br/>
 * fabric-node-manager:04 <br/>
 * C: error code <br/>
 */
public class ConstantCode {

    /* return success */
    public static final RetCode SUCCESS = RetCode.mark(0, "success");

    /* system exception */
    public static final RetCode SYSTEM_EXCEPTION = RetCode.mark(104000, "system exception");

    /**
     * Business exception.
     */
    public static final RetCode REQUEST_FRONT_FAIL = RetCode.mark(204000, "request front service failed");

    public static final RetCode SERVER_CONNECT_FAIL = RetCode.mark(204001, "wrong host or port");

    public static final RetCode FRONT_LIST_NOT_FOUNT = RetCode.mark(204002, "not fount any front");

    public static final RetCode SAVE_FRONT_FAIL = RetCode.mark(204003, "save front fail");

    public static final RetCode DB_EXCEPTION = RetCode.mark(204004, "database exception");

    public static final RetCode SAVE_CHANNEL_FAIL = RetCode.mark(204005, "save channel fail");

    public static final RetCode CHANNEL_ID_NULL = RetCode.mark(204006, "channel id is null");

    public static final RetCode INVALID_CHANNEL_ID = RetCode.mark(204007, "invalid channel id");

    public static final RetCode FRONT_EXISTS = RetCode.mark(204008, "front already exists");

    public static final RetCode INVALID_FRONT_ID = RetCode.mark(204009, "invalid front id");

    public static final RetCode TOKEN_EXPIRE = RetCode.mark(204010, "token expire");

    public static final RetCode INVALID_TOKEN = RetCode.mark(204011, "invalid token");

    public static final RetCode ROLE_ID_EMPTY = RetCode.mark(204012, "role id cannot be empty");

    public static final RetCode INVALID_ROLE_ID = RetCode.mark(204013, "invalid role id");

    public static final RetCode ACCOUNT_EXISTS = RetCode.mark(204014, "account info already exists");

    public static final RetCode ACCOUNT_NOT_EXISTS = RetCode.mark(204015, "account info not exists");

    public static final RetCode NOW_PWD_EQUALS_OLD = RetCode.mark(204016, "the new password cannot be same as old");

    public static final RetCode ACCOUNT_NAME_EMPTY = RetCode.mark(204017, "account name empty");

    public static final RetCode INVALID_ACCOUNT_NAME = RetCode.mark(204018, "invalid account name");

    public static final RetCode PASSWORD_ERROR = RetCode.mark(204019, "password error");


    /* auth */
    public static final RetCode USER_NOT_LOGGED_IN = RetCode.mark(304000, "user not logged in");
    public static final RetCode ACCESS_DENIED = RetCode.mark(304001, "access denied");


    /* param exception */
    public static final RetCode PARAM_EXCEPTION = RetCode.mark(404000, "param exception");

}
