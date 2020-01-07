package com.webank.fabric.node.manager.api.peer;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.webank.fabric.node.manager.common.pojo.peer.PeerDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

/**
 * service of peer.
 */
@Service
public class PeerService {


    @Autowired
    private PeerkMapper peerkMapper;

    /**
     * save channel.
     */
    public PeerDO saveChannel(String peerName, int channelId, String peerAddress, BigInteger blockHeight) {
        PeerDO peerDO = PeerDO.builder()
                .peerName(peerName)
                .channelId(channelId)
                .peerAddress(peerAddress)
                .blockHeight(blockHeight)
                .build();
        UpdateWrapper<PeerDO> wrapper = Wrappers.update();
        wrapper.eq("peer_address", peerAddress);
        peerkMapper.saveOrUpdate(peerDO, wrapper);
        return peerDO;
    }

}
