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
package com.webank.fabric.node.manager.common.enums;

/**
 * table name.
 */
public enum TableName {
    BLOCK("tb_block_"), TRANS("tb_transaction_");
    String value;

    TableName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getTableName(int i) {
        return value + i;
    }

    ;
}
