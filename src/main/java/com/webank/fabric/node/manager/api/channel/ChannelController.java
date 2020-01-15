package com.webank.fabric.node.manager.api.channel;

import com.alibaba.fastjson.JSON;
import com.webank.fabric.node.manager.api.transaction.TransDailyService;
import com.webank.fabric.node.manager.common.enums.DataStatus;
import com.webank.fabric.node.manager.common.exception.NodeMgrException;
import com.webank.fabric.node.manager.common.pojo.base.BasePageResponse;
import com.webank.fabric.node.manager.common.pojo.base.BaseResponse;
import com.webank.fabric.node.manager.common.pojo.base.ConstantCode;
import com.webank.fabric.node.manager.common.pojo.channel.ChannelDO;
import com.webank.fabric.node.manager.common.pojo.channel.ChannelGeneral;
import com.webank.fabric.node.manager.common.pojo.transaction.SevenDaysTrans;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    @Autowired
    private TransDailyService transDailyService;

    /**
     * query all channel.
     */
    @GetMapping("/all")
    public BasePageResponse getAllChannel() throws NodeMgrException {
        BasePageResponse pagesponse = new BasePageResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info("start getAllChannel startTime:{}", startTime.toEpochMilli());

        // get channel list
        int count = channelService.countOfChannel(null, DataStatus.NORMAL.getValue());
        if (count > 0) {
            List<ChannelDO> groupList = channelService.getChannelList(DataStatus.NORMAL.getValue());
            pagesponse.setTotalCount(count);
            pagesponse.setData(groupList);
        }

        // reset channel
        frontChannelService.resetMapList();

        log.info("end getAllChannel useTime:{} result:{}",
                Duration.between(startTime, Instant.now()).toMillis(),
                JSON.toJSONString(pagesponse));
        return pagesponse;
    }

    /**
     * get trans daily.
     */
    @GetMapping("/transDaily/{channelId}")
    public BaseResponse getTransDaily(@PathVariable("channelId") Integer channelId) throws Exception {
        BaseResponse pagesponse = new BaseResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info("start getTransDaily startTime:{} channelId:{}", startTime.toEpochMilli(), channelId);

        // query trans daily
        List<SevenDaysTrans> listTrans = transDailyService.listSevenDayOfTrans(channelId);
        pagesponse.setData(listTrans);

        log.info("end getTransDaily useTime:{} result:{}",
                Duration.between(startTime, Instant.now()).toMillis(),
                JSON.toJSONString(pagesponse));
        return pagesponse;
    }


    /**
     * get channel general.
     */
    @GetMapping("/general/{channelId}")
    public BaseResponse getChannelGeneral(@PathVariable("channelId") Integer channelId)
            throws NodeMgrException {
        Instant startTime = Instant.now();
        BaseResponse baseResponse = new BaseResponse(ConstantCode.SUCCESS);
        log.info("start getChannelGeneral startTime:{} channelId:{}", startTime.toEpochMilli(),
                channelId);
        ChannelGeneral channelGeneral = channelService.queryChannelGeneral(channelId);

        baseResponse.setData(channelGeneral);
        log.info("end getChannelGeneral useTime:{} result:{}",
                Duration.between(startTime, Instant.now()).toMillis(),
                JSON.toJSONString(baseResponse));
        return baseResponse;
    }
}
