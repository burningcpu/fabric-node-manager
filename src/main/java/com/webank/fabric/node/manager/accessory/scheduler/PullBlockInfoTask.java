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
import com.webank.fabric.node.manager.api.channel.FrontChannelService;
import com.webank.fabric.node.manager.common.pojo.channel.FrontChannelUnionDO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * get block info from chain.
 */
@Log4j2
@Component
public class PullBlockInfoTask {

    @Autowired
    private BlockService blockService;
    @Autowired
    private FrontChannelService frontChannelService;

    //@Scheduled(fixedDelayString = "${schedule.pullBlockTaskFixedDelay}")
    public void taskStart() {
        pullBlockStart();
    }

    /**
     * task start
     */
    public synchronized void pullBlockStart() {
        Instant startTime = Instant.now();
        log.info("start pullBLock startTime:{}", startTime.toEpochMilli());
        List<FrontChannelUnionDO> channelList = frontChannelService.getAllMap();
        if (channelList == null || channelList.size() == 0) {
            log.warn("pullBlock jump over: not found any group");
            return;
        }

        CountDownLatch latch = new CountDownLatch(channelList.size());
        channelList.stream()
                .forEach(group -> blockService.pullBlockByChannelId(latch, group.getChannelId()));

        try {
            latch.await();//5min
        } catch (InterruptedException ex) {
            log.error("InterruptedException", ex);
            Thread.currentThread().interrupt();
        }

        log.info("end pullBLock useTime:{} ",
                Duration.between(startTime, Instant.now()).toMillis());
    }
}
