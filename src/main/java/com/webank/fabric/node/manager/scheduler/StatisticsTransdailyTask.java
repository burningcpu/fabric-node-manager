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
package com.webank.fabric.node.manager.scheduler;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

/**
 * Timed tasks for counting daily transaction data.
 * using in web's charts
 */
@Log4j2
@Component
public class StatisticsTransdailyTask {
// TODO 定期从交易信息表统计

//    @Autowired
//    private ChannelService groupService;
//    @Autowired
//    private BlockService blockService;
//    @Autowired
//    private TransDailyService transDailyService;
//
//
//    @Scheduled(cron = "${constant.statisticsTransDailyCron}")
//    public void taskStart() {
//        updateTransdailyData();
//    }
//
//    /**
//     * counting daily transaction data.
//     */
//    public synchronized void updateTransdailyData() {
//        Instant startTime = Instant.now();
//        log.info("start updateTransdailyData startTime:{}", startTime.toEpochMilli());
//        try {
//
//            // query all group statistical info
//            List<StatisticalChannelTransInfo> groupStatisticalList = groupService
//                .queryLatestStatisticalTrans();
//
//            // traverse group list
//            traverseNetList(groupStatisticalList);
//
//        } catch (Exception ex) {
//            log.error("fail updateTransdailyData", ex);
//        }
//        log.info("end updateTransdailyData useTime:{}",
//            Duration.between(startTime, Instant.now()).toMillis());
//    }
//
//    /**
//     * traverse group list.
//     */
//    private void traverseNetList(List<StatisticalChannelTransInfo> groupStatisticalList)
//        throws NodeMgrException {
//        if (groupStatisticalList == null | groupStatisticalList.size() == 0) {
//            log.warn("updateTransdailyData jump over: no group information exists");
//            return;
//        }
//
//        // traverse group list
//        for (StatisticalChannelTransInfo statisticalInfo : groupStatisticalList) {
//            LocalDate latestSaveDay = statisticalInfo.getMaxDay();
//            BigInteger latestSaveBlockNumber = Optional.ofNullable(statisticalInfo.getBlockNumber())
//                .orElse(BigInteger.ZERO);
//            BigInteger netTransCount = Optional.ofNullable(statisticalInfo.getTransCount())
//                .orElse(BigInteger.ZERO);
//            BigInteger maxBlockNumber = latestSaveBlockNumber;
//            Integer groupId = statisticalInfo.getChannelId();
//
//            // query block list
//            BlockListParam queryParam = new BlockListParam(maxBlockNumber, latestSaveDay,
//                SqlSortType.ASC.getValue());
//            List<BlockInfoDO> blockList = blockService.queryBlockList(groupId,queryParam);
//
//            // Traversing block list
//            if (blockList == null | blockList.size() == 0) {
//                log.info("updateTransdailyData jump over .This chain [{}] did not find new block",
//                    groupId);
//                continue;
//            }
//            for (int i = 0; i < blockList.size(); i++) {
//                BlockInfoDO tbBlock = blockList.get(i);
//                LocalDate blockDate = tbBlock.getBlockTimestamp() == null ? null
//                    : tbBlock.getBlockTimestamp().toLocalDate();
//                if (blockDate == null) {
//                    log.warn("updateTransdailyData jump over . blockDate is null");
//                    continue;
//                }
//
//                BigInteger blockTransCount = new BigInteger(
//                    String.valueOf(tbBlock.getTransCount()));
//                if (blockDate.equals(latestSaveDay)) {
//                    netTransCount = netTransCount.add(blockTransCount);
//                    maxBlockNumber = tbBlock.getBlockNumber();
//                } else {
//                    if (netTransCount.intValue() > 0 && latestSaveDay != null) {
//                        transDailyService
//                            .updateTransDaily(groupId, latestSaveDay, latestSaveBlockNumber,
//                                maxBlockNumber, netTransCount);
//                    }
//
//                    transDailyService
//                        .addTbTransDailyInfo(groupId, blockDate, tbBlock.getTransCount(),
//                            tbBlock.getBlockNumber());
//
//                    latestSaveBlockNumber = tbBlock.getBlockNumber();
//                    latestSaveDay = blockDate;
//                    netTransCount = blockTransCount;
//                    maxBlockNumber = tbBlock.getBlockNumber();
//                }
//
//                //latest block of list
//                if (i == (blockList.size() - 1)) {
//                    transDailyService
//                        .updateTransDaily(groupId, latestSaveDay, latestSaveBlockNumber,
//                            maxBlockNumber, netTransCount);
//                }
//            }
//        }
//    }
}
