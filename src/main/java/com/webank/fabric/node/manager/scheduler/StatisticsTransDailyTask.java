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
package com.webank.fabric.node.manager.scheduler;

import com.webank.fabric.node.manager.api.channel.ChannelService;
import com.webank.fabric.node.manager.api.transaction.TransDailyService;
import com.webank.fabric.node.manager.api.transaction.TransactionService;
import com.webank.fabric.node.manager.common.enums.DataStatus;
import com.webank.fabric.node.manager.common.exception.NodeMgrException;
import com.webank.fabric.node.manager.common.pojo.channel.ChannelDO;
import com.webank.fabric.node.manager.common.pojo.transaction.LatestTransCountBO;
import com.webank.fabric.node.manager.common.pojo.transaction.TransDailyDO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * Timed tasks for counting daily transaction data.
 * using in web's charts
 */
@Log4j2
@Component
public class StatisticsTransDailyTask {
    @Autowired
    private ChannelService channelService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private TransDailyService transDailyService;


    @Scheduled(cron = "${schedule.statisticsTransDailyCron}")
    public void taskStart() {
        updateTransDailyData();
    }

    /**
     * counting daily transaction data.
     */
    private synchronized void updateTransDailyData() {
        Instant startTime = Instant.now();
        log.info("start updateTransDailyData. startTime:{}", startTime.toEpochMilli());
        //get group list
        List<ChannelDO> channelList = channelService.getChannelList(DataStatus.NORMAL.getValue());
        if (channelList == null || channelList.size() == 0) {
            log.info("updateTransDailyData jump over .not found any group");
            return;
        }

        channelList.stream().forEach(g -> statisticByChannelId(g.getChannelId()));

        log.info("end updateTransDailyData useTime:{}",
                Duration.between(startTime, Instant.now()).toMillis());
    }

    /**
     * static transCount by channelId.
     */
    private void statisticByChannelId(Integer channelId) throws NodeMgrException {
        //statistic latest transactionCount
        List<LatestTransCountBO> latestTransCountList = transactionService.queryLatestTransCount(channelId);
        if (latestTransCountList == null || latestTransCountList.size() == 0) {
            log.debug("updateTransDailyData jump over, latestTransCountList is null,channelId:{} ", channelId);
            return;
        }
        latestTransCountList.stream().forEach(ltc -> saveLatestTransCount(ltc, channelId));
    }

    /**
     * save latest transaction count.
     */
    private void saveLatestTransCount(LatestTransCountBO latestTransCountBO, Integer channelId) {
        TransDailyDO transDailyDO = TransDailyDO.builder().channelId(channelId).build();
        BeanUtils.copyProperties(latestTransCountBO, transDailyDO);
        transDailyService.addTransDailyDO(transDailyDO);
    }
}
