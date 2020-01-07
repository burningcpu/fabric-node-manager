/**
 * Copyright 2014-2019  the original author or authors.
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
package com.webank.fabric.node.manager.api.transaction;

import com.alibaba.fastjson.JSON;
import com.webank.fabric.node.manager.api.channel.ChannelService;
import com.webank.fabric.node.manager.common.exception.NodeMgrException;
import com.webank.fabric.node.manager.common.pojo.base.ConstantCode;
import com.webank.fabric.node.manager.common.pojo.transaction.SeventDaysTrans;
import com.webank.fabric.node.manager.common.pojo.transaction.TransDailyDO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
public class TransDailyService {

    @Autowired
    private ChannelService channelService;
    @Autowired
    private TransDailyMapper transDailyMapper;


    /**
     * query Trading within seven days.
     */
    public List<SeventDaysTrans> listSeventDayOfTrans(Integer channelId) throws NodeMgrException {
        log.debug("start listSeventDayOfTrans channelId:{}", channelId);
        try {
            // qurey
            List<SeventDaysTrans> transList = transDailyMapper
                    .listSeventDayOfTransDaily(channelId);
            log.debug("end listSeventDayOfTrans transList:{}", JSON.toJSONString(transList));
            return transList;
        } catch (RuntimeException ex) {
            log.debug("fail listSeventDayOfTrans channelId:{}", channelId, ex);
            throw new NodeMgrException(ConstantCode.DB_EXCEPTION);
        }
    }

    /**
     * update trans daily info.
     */
    public void updateTransDaily(Integer channelId, LocalDate transDay, BigInteger oldBlockNumber,
                                 BigInteger latestBlockNumber, BigInteger transCount)
            throws NodeMgrException {
        log.debug(
                "start updateTransDaily channelId:{} transDay:{} oldBlockNumber:{} "
                        + "latestBlockNumber:{} transCount:{}", channelId, JSON.toJSONString(transDay),
                oldBlockNumber, latestBlockNumber, transCount);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("channelId", channelId);
        paramMap.put("oldBlockNumber", oldBlockNumber);
        paramMap.put("transDay", transDay);
        paramMap.put("latestBlockNumber", latestBlockNumber);
        paramMap.put("transCount", transCount);
        Integer affectRow = 0;
        try {
            affectRow = transDailyMapper.updateTransDaily(paramMap);
        } catch (RuntimeException ex) {
            log.error(
                    "fail updateTransDaily channelId:{} transDay:{} oldBlockNumber:{}"
                            + " latestBlockNumber:{} transCount:{}",
                    channelId, transDay, oldBlockNumber, latestBlockNumber, transCount, ex);
            throw new NodeMgrException(ConstantCode.DB_EXCEPTION);
        }

        log.debug("end updateTransDaily affectRow:{}", affectRow);
    }

    /**
     * add trans daily info.
     */
    public void addTbTransDailyInfo(Integer channelId, LocalDate transDay, Integer transCount,
                                    BigInteger blockNumber) throws NodeMgrException {
        log.debug("start addTbTransDailyInfo channelId:{} transDay:{} transCount:{} blockNumber:{}",
                channelId, JSON.toJSONString(transDay),
                transCount, blockNumber);

        // check group id
        channelService.checkChannelId(channelId);

        // add row
        TransDailyDO rowParam = new TransDailyDO(channelId, transDay, transCount, blockNumber);
        try {
            transDailyMapper.addTransDailyRow(rowParam);
        } catch (RuntimeException ex) {
            log.error(
                    "start addTbTransDailyInfo channelId:{} transDay:{} transCount:{} blockNumber:{}",
                    channelId, JSON.toJSONString(transDay),
                    transCount, blockNumber, ex);
            throw new NodeMgrException(ConstantCode.DB_EXCEPTION);
        }

        log.debug("end addNodeInfo");
    }


    /**
     * delete by channelId.
     */
    public void deleteByChannelId(int channelId) {
        if (channelId == 0) {
            return;
        }
        transDailyMapper.deleteByChannelId(channelId);
    }
}