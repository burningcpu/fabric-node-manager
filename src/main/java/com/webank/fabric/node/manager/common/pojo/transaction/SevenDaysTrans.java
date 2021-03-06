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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Entity class of Trading within seven days.
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class SevenDaysTrans implements Comparable<SevenDaysTrans> {
    private LocalDate day;
    private int channelId;
    private BigInteger transCount;

    @Override
    public int compareTo(SevenDaysTrans target) {
        if (Objects.isNull(this.day) || this.day.equals(target.getDay()))
            return 0;
        if (this.day.isBefore(target.getDay()))
            return -1;
        return 1;
    }
}
