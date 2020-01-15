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
import com.webank.fabric.node.manager.common.pojo.transaction.SevenDaysTrans;
import com.webank.fabric.node.manager.common.pojo.transaction.TransDailyDO;
import com.webank.fabric.node.manager.common.utils.TimeUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;

@Log4j2
@Service
public class TransDailyService {
    private static final int COUNT_DATE_OF_WEEK = 7;
    private static final int DATE_MINUS_FLAG = -1;

    @Autowired
    private ChannelService channelService;
    @Autowired
    private TransDailyMapper transDailyMapper;


    /**
     * query Trading within seven days.
     */
    public List<SevenDaysTrans> listSevenDayOfTrans(Integer channelId) throws NodeMgrException {
        log.debug("start listSevenDayOfTrans channelId:{}", channelId);

        // qurey
        List<SevenDaysTrans> transDailyList = transDailyMapper.listSevenDayOfTransDaily(channelId);
        List<LocalDate> dateListOfWeek = TimeUtils.getDateList(LocalDate.now(), COUNT_DATE_OF_WEEK, DATE_MINUS_FLAG);
        for (LocalDate date : dateListOfWeek) {
            Optional<SevenDaysTrans> dailyTransOptional = transDailyList.stream().filter(td -> td.getDay().equals(date)).findAny();
            if (!dailyTransOptional.isPresent()) {
                transDailyList.add(SevenDaysTrans.builder().channelId(channelId).day(date).build());
            }
        }
        //sort
        Collections.sort(transDailyList);
        log.debug("end listSevenDayOfTrans transDailyList:{}", JSON.toJSONString(transDailyList));
        return transDailyList;

    }

    /**
     * update trans daily info.
     */
    public void updateTransDaily(Integer channelId, LocalDate transDay, BigInteger oldTransNumber,
                                 BigInteger latestTransNumber, BigInteger transCount)
            throws NodeMgrException {
        log.debug(
                "start updateTransDaily channelId:{} transDay:{} oldTransNumber:{} "
                        + "latestTransNumber:{} transCount:{}", channelId, JSON.toJSONString(transDay),
                oldTransNumber, latestTransNumber, transCount);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("channelId", channelId);
        paramMap.put("oldTransNumber", oldTransNumber);
        paramMap.put("transDay", transDay);
        paramMap.put("latestTransNumber", latestTransNumber);
        paramMap.put("transCount", transCount);
        Integer affectRow = 0;
        try {
            affectRow = transDailyMapper.updateTransDaily(paramMap);
        } catch (RuntimeException ex) {
            log.error(
                    "fail updateTransDaily channelId:{} transDay:{} oldTransNumber:{}"
                            + " latestTransNumber:{} transCount:{}",
                    channelId, transDay, oldTransNumber, latestTransNumber, transCount, ex);
            throw new NodeMgrException(ConstantCode.DB_EXCEPTION);
        }

        log.debug("end updateTransDaily affectRow:{}", affectRow);
    }

    /**
     * add trans daily info.
     */
    public void addTransDailyDO(TransDailyDO transDailyDO) throws NodeMgrException {
        transDailyMapper.addTransDailyRow(transDailyDO);
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