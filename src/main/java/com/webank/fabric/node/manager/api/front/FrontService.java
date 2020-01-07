package com.webank.fabric.node.manager.api.front;

import com.alibaba.fastjson.JSON;
import com.webank.fabric.node.manager.api.channel.ChannelService;
import com.webank.fabric.node.manager.api.channel.FrontChannelService;
import com.webank.fabric.node.manager.api.peer.PeerService;
import com.webank.fabric.node.manager.common.exception.NodeMgrException;
import com.webank.fabric.node.manager.common.pojo.base.ConstantCode;
import com.webank.fabric.node.manager.common.pojo.channel.ChannelDO;
import com.webank.fabric.node.manager.common.pojo.front.FrontDO;
import com.webank.fabric.node.manager.common.pojo.front.ReqFrontVO;
import com.webank.fabric.node.manager.common.utils.NodeMgrUtils;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.sdk.Peer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Collection;

/**
 * service of front.
 */
@Slf4j
@Service
public class FrontService {

    @Autowired
    private FrontRestManager frontRestManager;
    @Autowired
    private FrontMapper frontMapper;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private PeerService peerService;
    @Autowired
    private FrontChannelService frontChannelService;

    @Transactional
    public FrontDO newFront(ReqFrontVO reqFrontVO) {
        String frontIp = reqFrontVO.getFrontIp();
        Integer frontPort = reqFrontVO.getFrontPort();
        //check front ip and port
        NodeMgrUtils.checkServerConnect(frontIp, frontPort);
        //query channel
        String channel = frontRestManager.getChannelNameFromSpecificFront(frontIp, frontPort);
        //query peer list
        Collection<Peer> peers = frontRestManager.getPeersFromSpecificFront(frontIp, frontPort);
        //save front
        FrontDO frontDo = new FrontDO(reqFrontVO);
        frontMapper.add(frontDo);
        if (frontDo.getFrontId() == null || frontDo.getFrontId() == 0) {
            log.warn("fail newFront, after save, frontDo:{}", JSON.toJSONString(frontDo));
            throw new NodeMgrException(ConstantCode.SAVE_FRONT_FAIL);
        }


        long peerCount = peers.stream().filter(p -> p.getName().contains(":")).count();
        //save channel
        ChannelDO channelDO = channelService.saveChannel(channel, peerCount);
        frontChannelService.newFrontChannel(frontDo.getFrontId(), channelDO.getChannelId());

        //save peers
        for (Peer peer : peers) {
            BigInteger blockHeight = frontRestManager.getBlockHeightFromSpecificFront(frontIp, frontPort, peer.getUrl());
            peerService.addPeerInfo(peer.getName(), channelDO.getChannelId(), peer.getUrl(), blockHeight);
        }


        //clear catch
        frontChannelService.clearMapList();
        return frontDo;
    }


}
