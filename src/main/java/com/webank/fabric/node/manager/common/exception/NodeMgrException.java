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
package com.webank.fabric.node.manager.common.exception;

import com.webank.fabric.node.manager.common.pojo.response.CodeAndMsg;

/**
 * business exception.
 */
public class NodeMgrException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private CodeAndMsg codeAndMsg;

    public NodeMgrException(CodeAndMsg codeAndMsg) {
        super(codeAndMsg.getMessage());
        this.codeAndMsg = codeAndMsg;
    }

    public NodeMgrException(CodeAndMsg codeAndMsg, Throwable cause) {
        super(codeAndMsg.getMessage(),cause);
        this.codeAndMsg = codeAndMsg;
    }

    public NodeMgrException(int code, String msg) {
        super(msg);
        this.codeAndMsg = new CodeAndMsg(code, msg);
    }

    public CodeAndMsg getCodeAndMsg() {
        return codeAndMsg;
    }
}
