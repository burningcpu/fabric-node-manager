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
package com.webank.fabric.node.manager.common.exception;

import com.webank.fabric.node.manager.common.pojo.base.RetCode;

/**
 * business exception.
 */

public class NodeMgrException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private RetCode retCode;

    public NodeMgrException(RetCode RetCode) {
        super(RetCode.getMessage());
        this.retCode = RetCode;
    }

    public NodeMgrException(RetCode RetCode, Throwable cause) {
        super(RetCode.getMessage(), cause);
        this.retCode = RetCode;
    }

    public NodeMgrException(int code, String msg) {
        super(msg);
        this.retCode = new RetCode(code, msg);
    }

    public NodeMgrException(int code, String msg, Throwable cause) {
        super(msg, cause);
        this.retCode = new RetCode(code, msg);
    }

    public RetCode getCodeAndMsg() {
        return retCode;
    }

}
