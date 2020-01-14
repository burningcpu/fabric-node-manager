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
package com.webank.fabric.node.manager.api.transaction;

import com.webank.fabric.node.manager.common.pojo.transaction.SevenDaysTrans;
import com.webank.fabric.node.manager.common.pojo.transaction.TransDailyDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * trans_daily data interface.
 */
@Repository
public interface TransDailyMapper {

    /**
     * query Trading within seven days.
     */
    List<SevenDaysTrans> listSevenDayOfTransDaily(@Param("channelId") Integer channelId);

    /**
     * update tb_trans_daily.
     */
    Integer updateTransDaily(Map<String, Object> paramMap);

    /**
     * add new tb_trans_daily data.
     */
    Integer addTransDailyRow(TransDailyDO tbTransDaily);

    /**
     * query max block number by channel id.
     */
    BigInteger queryMaxBlockByChannel(@Param("channelId") Integer channelId);

    /**
     * delete by channelId.
     */
    Integer deleteByChannelId(@Param("channelId") Integer channelId);
}
