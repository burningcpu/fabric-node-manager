package com.webank.fabric.node.manager.api.peer;

import com.alibaba.fastjson.JSON;
import com.webank.fabric.node.manager.common.exception.NodeMgrException;
import com.webank.fabric.node.manager.common.pojo.base.BasePageResponse;
import com.webank.fabric.node.manager.common.pojo.base.BaseResponse;
import com.webank.fabric.node.manager.common.pojo.base.ConstantCode;
import com.webank.fabric.node.manager.common.pojo.peer.PeerDO;
import com.webank.fabric.node.manager.common.pojo.peer.PeerParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * controller of peer.
 */
@Log4j2
@RestController
@RequestMapping("peer")
@Api(value = "peer", tags = "about peer info")
public class PeerController {

    @Autowired
    private PeerService peerService;

    /**
     * qurey peer info list.
     */
    @GetMapping(value = "/peerList/{channelId}/{pageNumber}/{pageSize}")
    @ApiOperation(value = "peerList", notes = "get peer list")
    public BasePageResponse queryPeerList(@PathVariable("channelId") Integer channelId,
                                          @PathVariable("pageNumber") Integer pageNumber,
                                          @PathVariable("pageSize") Integer pageSize,
                                          @RequestParam(value = "peerName", required = false) String peerName)
            throws NodeMgrException {

        Instant startTime = Instant.now();
        log.info(
                "start queryPeerList startTime:{} channelId:{}  pageNumber:{} pageSize:{} peerName:{}",
                startTime.toEpochMilli(), channelId, pageNumber,
                pageSize, peerName);

        // param
        PeerParam queryParam = new PeerParam();
        queryParam.setChannelId(channelId);
        queryParam.setPageSize(pageSize);
        queryParam.setPeerName(peerName);

        BasePageResponse pageResponse = new BasePageResponse(ConstantCode.SUCCESS);
        Integer count = peerService.countOfPeer(queryParam);
        if (count != null && count > 0) {
            Integer start = Optional.ofNullable(pageNumber).map(page -> (page - 1) * pageSize)
                    .orElse(null);
            queryParam.setStart(start);

            List<PeerDO> listOfpeer = peerService.qureyPeerList(queryParam);
            pageResponse.setData(listOfpeer);
            pageResponse.setTotalCount(count);

        }

        log.info("end queryPeerList useTime:{} result:{}",
                Duration.between(startTime, Instant.now()).toMillis(), JSON.toJSONString(pageResponse));
        return pageResponse;
    }

    /**
     * get peer info.
     */
    @GetMapping(value = "/peerInfo/{channelId}")
    @ApiOperation(value = "getPeerInfo", notes = "get peer info by channelId")
    public BaseResponse getPeerInfo(@PathVariable("channelId") Integer channelId)
            throws NodeMgrException {

        Instant startTime = Instant.now();
        log.info("start getPeerInfo startTime:{} channelId:{}",
                startTime.toEpochMilli(), channelId);

        // param
        PeerParam param = new PeerParam();
        param.setChannelId(channelId);

        // query peer row
        PeerDO tbPeer = peerService.queryPeerInfo(param);

        BaseResponse baseResponse = new BaseResponse(ConstantCode.SUCCESS);
        baseResponse.setData(tbPeer);

        log.info("end getPeerInfo useTime:{} result:{}",
                Duration.between(startTime, Instant.now()).toMillis(), JSON.toJSONString(baseResponse));
        return baseResponse;
    }

}
