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
package com.webank.fabric.node.manager.common.pojo.transaction;

import com.webank.fabric.node.manager.common.pojo.base.BaseQueryParam;
import lombok.*;

import java.math.BigInteger;

/**
 * param of query transaction list.
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TransListParam extends BaseQueryParam {
    private BigInteger blockNumber;
    private String txId;
    private String flagSortedByBlock;

    public TransListParam(String txId, BigInteger blockNumber) {
        this.txId = txId;
        this.blockNumber = blockNumber;
    }
}
