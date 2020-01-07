/**
 * Copyright 2014-2019  the original author or authors.
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
package com.webank.fabric.node.manager.common.pojo.response;

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
    public static final CodeAndMsg SUCCESS = CodeAndMsg.mark(0, "success");

    /* system exception */
    public static final CodeAndMsg SYSTEM_EXCEPTION = CodeAndMsg.mark(104000, "system exception");

    /**
     * Business exception.
     */
    public static final CodeAndMsg REQUEST_FRONT_FAIL = CodeAndMsg.mark(204000,"request front fail");

    public static final CodeAndMsg SERVER_CONNECT_FAIL = CodeAndMsg.mark(204001, "wrong host or port");

    public static final CodeAndMsg FRONT_LIST_NOT_FOUNT = CodeAndMsg.mark(204002, "not fount any front");

    public static final CodeAndMsg SAVE_FRONT_FAIL = CodeAndMsg.mark(204003, "save front fail");

    /* param exception */
    public static final CodeAndMsg PARAM_EXCEPTION = CodeAndMsg.mark(404000, "param exception");

}
