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
package com.webank.fabric.node.manager.accessory.scheduler;


import com.webank.fabric.node.manager.api.block.BlockService;
import com.webank.fabric.node.manager.api.channel.ChannelService;
import com.webank.fabric.node.manager.api.transaction.TransactionService;
import com.webank.fabric.node.manager.common.enums.DataStatus;
import com.webank.fabric.node.manager.common.pojo.channel.ChannelDO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * delete block/trans/monitorTrans data task
 */
@Log4j2
@Component
@ConditionalOnProperty(name = "constant.isDeleteInfo", havingValue = "true")
public class DeleteInfoTask {

    @Autowired
    private ChannelService groupService;
    @Autowired
    private BlockService blockService;
    @Autowired
    private TransactionService transHashService;
    @Autowired
    private ScheduleProperties scheduleProperties;


    @Scheduled(cron = "${schedule.deleteInfoCron}")
    public void taskStart() {
        deleteInfoStart();
    }

    /**
     * start.
     */
    public void deleteInfoStart() {
        Instant startTime = Instant.now();
        log.info("start deleteInfoStart. startTime:{}", startTime.toEpochMilli());
        //get group list
        List<ChannelDO> channelList = groupService.getChannelList(DataStatus.NORMAL.getValue());
        if (channelList == null || channelList.size() == 0) {
            log.info("DeleteInfoTask jump over .not found any group");
            return;
        }

        channelList.stream().forEach(g -> deleteByChannelId(g.getChannelId()));

        log.info("end deleteInfoStart useTime:{}",
                Duration.between(startTime, Instant.now()).toMillis());
    }

    /**
     * delete by channelId.
     */
    private void deleteByChannelId(int channelId) {
        //delete block
        deleteBlock(channelId);
        //delete transHash
        deleteTransHash(channelId);
    }


    /**
     * delete block.
     */
    private void deleteBlock(int channelId) {
        log.info("start deleteBlock. channelId:{}", channelId);
        try {
            int removeCount = 1;
            while (removeCount > 0) {
                removeCount = blockService.remove(channelId, scheduleProperties.getBlockRetainMax());
                log.info("end deleteBlock. channelId:{} removeCount:{}", channelId, removeCount);
                Thread.sleep(3);
            }
        } catch (Exception ex) {
            log.info("fail deleteBlock. channelId:{}", channelId, ex);
        }
    }

    /**
     * delete transHash.
     */
    private void deleteTransHash(int channelId) {
        // TODO 循环删除：根据scheduleProperties.getTransRetainMax().intValue() 和 trans_number
        log.info("start deleteTransHash. channelId:{}", channelId);
        try {
            int count;
            while ((count= transHashService.queryCountOfTranByMinus(channelId)) > scheduleProperties.getTransRetainMax().intValue()) {
                Integer subTransNum = count - scheduleProperties.getTransRetainMax().intValue();
                int removeCount = transHashService.remove(channelId, subTransNum);
                log.info("end deleteTransHash. channelId:{} removeCount:{}", channelId, removeCount);
            }

        } catch (Exception ex) {
            log.info("fail deleteTransHash. channelId:{}", channelId, ex);
        }
    }

}
