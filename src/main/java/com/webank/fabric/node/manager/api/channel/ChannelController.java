package com.webank.fabric.node.manager.api.channel;

import com.alibaba.fastjson.JSON;
import com.webank.fabric.node.manager.common.enums.DataStatus;
import com.webank.fabric.node.manager.common.exception.NodeMgrException;
import com.webank.fabric.node.manager.common.pojo.base.BasePageResponse;
import com.webank.fabric.node.manager.common.pojo.base.ConstantCode;
import com.webank.fabric.node.manager.common.pojo.channel.ChannelDO;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * controller of channel.
 */
@Log4j2
@RestController
@RequestMapping(value = "channel")
@Api(value = "channel", tags = "about channelInfo")
public class ChannelController {
    @Autowired
    private ChannelService channelService;
    @Autowired
    private FrontChannelService frontChannelService;

    /**
     * query all group.
     */
    @GetMapping("/all")
    public BasePageResponse getAllChannel() throws NodeMgrException {
        BasePageResponse pagesponse = new BasePageResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info("start getAllChannel startTime:{}", startTime.toEpochMilli());

        // get group list
        int count = channelService.countOfChannel(null, DataStatus.NORMAL.getValue());
        if (count > 0) {
            List<ChannelDO> groupList = channelService.getChannelList(DataStatus.NORMAL.getValue());
            pagesponse.setTotalCount(count);
            pagesponse.setData(groupList);
        }

        // reset group
        frontChannelService.resetMapList();

        log.info("end getAllChannel useTime:{} result:{}",
                Duration.between(startTime, Instant.now()).toMillis(),
                JSON.toJSONString(pagesponse));
        return pagesponse;
    }

}
