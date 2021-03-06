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
package com.webank.fabric.node.manager.api.performance;

import com.alibaba.fastjson.JSON;
import com.webank.fabric.node.manager.api.front.FrontRestManager;
import com.webank.fabric.node.manager.api.front.FrontService;
import com.webank.fabric.node.manager.common.exception.NodeMgrException;
import com.webank.fabric.node.manager.common.pojo.base.ConstantCode;
import com.webank.fabric.node.manager.common.pojo.front.FrontDO;
import com.webank.fabric.node.manager.common.utils.NodeMgrUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Log4j2
@Service
public class PerformanceService {

    @Autowired
    private FrontService frontService;
    @Autowired
    private FrontRestManager frontRestManager;

    /**
     * get ratio of performance.
     */
    public Object getPerformanceRatio(Integer frontId, LocalDateTime beginDate,
                                      LocalDateTime endDate, LocalDateTime contrastBeginDate,
                                      LocalDateTime contrastEndDate, int gap) {
        log.debug(
                "start getPerformanceRatio.  frontId:{} beginDate:{} endDate:{}"
                        + " contrastBeginDate:{} contrastEndDate:{} gap:{}",
                frontId, beginDate, endDate, contrastBeginDate, contrastEndDate, gap);

        List<String> nameList = Arrays
                .asList("beginDate", "endDate", "contrastBeginDate", "contrastEndDate", "gap");
        List<Object> valueList = Arrays
                .asList(beginDate, endDate, contrastBeginDate, contrastEndDate, gap);

        // request param to str
        String urlParam = NodeMgrUtils.convertUrlParam(nameList, valueList);

        // query by front Id
        FrontDO frontDO = frontService.getById(frontId);
        if (frontDO == null) {
            throw new NodeMgrException(ConstantCode.INVALID_FRONT_ID);
        }

        Object rspObj = frontRestManager.getPerformanceRatio(frontDO.getFrontIp(), frontDO.getFrontPort(), urlParam);

        log.debug("end getPerformanceRatio. rspObj:{}", JSON.toJSONString(rspObj));
        return rspObj;

    }

    /**
     * get config of performance.
     */
    public Object getPerformanceConfig(int frontId) {
        log.debug("start getPerformanceConfig.  frontId:{} ", frontId);

        // query by front Id
        FrontDO frontDO = frontService.getById(frontId);
        if (frontDO == null) {
            throw new NodeMgrException(ConstantCode.INVALID_FRONT_ID);
        }

        Object rspObj = frontRestManager.getPerformanceConfigFromSpecificFront(frontDO.getFrontIp(), frontDO.getFrontPort());
        log.debug("end getPerformanceConfig. frontRsp:{}", JSON.toJSONString(rspObj));
        return rspObj;
    }

}
