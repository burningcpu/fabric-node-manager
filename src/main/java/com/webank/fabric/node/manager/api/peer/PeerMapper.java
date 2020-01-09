package com.webank.fabric.node.manager.api.peer;

import com.webank.fabric.node.manager.common.pojo.peer.PeerDO;
import com.webank.fabric.node.manager.common.pojo.peer.PeerParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * interface of peer in db.
 */
@Repository
public interface PeerMapper {

    /**
     * Add new node data.
     */
    Integer add(PeerDO peerDo);

    /**
     * Query the number of node according to some conditions.
     */
    Integer getCount(PeerParam peerParam);


    /**
     * Query node list according to some conditions.
     */
    List<PeerDO> getList(PeerParam peerParam);


    /**
     * query node info.
     */
    PeerDO queryByPeerId(@Param("peerId") String peerId);

    /**
     * update node info.
     */
    Integer update(PeerDO dbPeer);

    /**
     * query node info.
     */
    PeerDO queryPeerInfo(PeerParam peerParam);


    /**
     * delete by peerId and channelId.
     */
    Integer deleteByPeerAndChannel(@Param("peerId") String peerId, @Param("channelId") Integer channelId);

    /**
     * delete by channelId.
     */
    Integer deleteByChannelId(@Param("channelId") Integer channelId);


    /**
     * query by peerIp and peerPort.
     */
    PeerDO queryByIpAndPort(@Param("peerIp") String peerIp, @Param("peerPort") Integer peerPort);

    /**
     * query by channelId and peerName.
     */
    PeerDO queryByChannelIdAndPeerName(@Param("channelId") Integer channelId, @Param("peerName") String peerName);

}
