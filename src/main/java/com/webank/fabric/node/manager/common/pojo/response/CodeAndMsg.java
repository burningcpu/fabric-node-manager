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

import lombok.Data;

/**
 * class about exception code and message.
 */
@Data
public class CodeAndMsg {

    private Integer code;
    private String message;

    public CodeAndMsg(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static CodeAndMsg mark(int code, String message) {
        return new CodeAndMsg(code, message);
    }

    public static CodeAndMsg mark(Integer code) {
        return new CodeAndMsg(code, null);
    }
}
