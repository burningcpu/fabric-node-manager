package com.webank.fabric.node.manager.api.peer;

import com.alibaba.fastjson.JSON;
import com.webank.fabric.node.manager.common.exception.NodeMgrException;
import com.webank.fabric.node.manager.common.pojo.base.ConstantCode;
import com.webank.fabric.node.manager.common.pojo.peer.PeerDO;
import com.webank.fabric.node.manager.common.pojo.peer.PeerParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

/**
 * service of peer.
 */
@Slf4j
@Service
public class PeerService {


    @Autowired
    private PeerMapper peerMapper;

    /**
     * save channel.
     */
    public PeerDO addPeerInfo(String peerName, int channelId, String peerAddress, BigInteger blockHeight) {
        PeerDO peerDO = PeerDO.builder()
                .peerName(peerName)
                .channelId(channelId)
                .peerAddress(peerAddress)
                .blockNumber(blockHeight)
                .build();
        peerMapper.add(peerDO);
        return peerDO;
    }

    /**
     * query count of node.
     */
    public Integer countOfPeer(PeerParam queryParam) throws NodeMgrException {
        log.debug("start countOfPeer queryParam:{}", JSON.toJSONString(queryParam));
        try {
            Integer nodeCount = peerMapper.getCount(queryParam);
            log.debug("end countOfPeer nodeCount:{} queryParam:{}", nodeCount,
                    JSON.toJSONString(queryParam));
            return nodeCount;
        } catch (RuntimeException ex) {
            log.error("fail countOfPeer . queryParam:{}", queryParam, ex);
            throw new NodeMgrException(ConstantCode.DB_EXCEPTION);
        }
    }


    /**
     * query node list by page.
     */
    public List<PeerDO> qureyPeerList(PeerParam queryParam) throws NodeMgrException {
        log.debug("start qureyPeerList queryParam:{}", JSON.toJSONString(queryParam));

        // query node list
        List<PeerDO> listOfPeer = peerMapper.getList(queryParam);

        log.debug("end qureyPeerList listOfPeer:{}", JSON.toJSONString(listOfPeer));
        return listOfPeer;
    }

    /**
     * query node by groupId
     */
    public List<PeerDO> queryByChannelId(int groupId) {
        PeerParam nodeParam = new PeerParam();
        nodeParam.setChannelId(groupId);
        return qureyPeerList(nodeParam);
    }

    /**
     * query all node list
     */
    public List<PeerDO> getAll() {
        return qureyPeerList(new PeerParam());
    }

    /**
     * query node info.
     */
    public PeerDO queryByPeerId(String nodeId) throws NodeMgrException {
        log.debug("start queryPeer nodeId:{}", nodeId);
        try {
            PeerDO nodeRow = peerMapper.queryByPeerId(nodeId);
            log.debug("end queryPeer nodeId:{} PeerDO:{}", nodeId, JSON.toJSONString(nodeRow));
            return nodeRow;
        } catch (RuntimeException ex) {
            log.error("fail queryPeer . nodeId:{}", nodeId, ex);
            throw new NodeMgrException(ConstantCode.DB_EXCEPTION);
        }
    }


    /**
     * update node info.
     */
    public void updatePeer(PeerDO tbPeer) throws NodeMgrException {
        log.debug("start updatePeerInfo  param:{}", JSON.toJSONString(tbPeer));
        Integer affectRow = 0;
        try {

            affectRow = peerMapper.update(tbPeer);
        } catch (RuntimeException ex) {
            log.error("updatePeerInfo exception", ex);
            throw new NodeMgrException(ConstantCode.DB_EXCEPTION);
        }

        if (affectRow == 0) {
            log.warn("affect 0 rows of tb_node");
            throw new NodeMgrException(ConstantCode.DB_EXCEPTION);
        }
        log.debug("end updatePeerInfo");
    }

    /**
     * query node info.
     */
    public PeerDO queryPeerInfo(PeerParam nodeParam) {
        log.debug("start queryPeerInfo nodeParam:{}", JSON.toJSONString(nodeParam));
        PeerDO tbPeer = peerMapper.queryPeerInfo(nodeParam);
        log.debug("end queryPeerInfo result:{}", tbPeer);
        return tbPeer;
    }

    /**
     * delete by node and group.
     */
    public void deleteByPeerAndChannelId(String nodeId, int groupId) throws NodeMgrException {
        log.debug("start deleteByPeerAndChannelId nodeId:{} groupId:{}", nodeId, groupId);
        peerMapper.deleteByPeerAndChannel(nodeId, groupId);
        log.debug("end deleteByPeerAndChannelId");
    }

    /**
     * delete by groupId.
     */
    public void deleteByChannelId(int groupId) {
        if (groupId == 0) {
            return;
        }
        peerMapper.deleteByChannelId(groupId);
    }


    /**
     * get latest number of peer on chain.
     */
//    private BigInteger getBlockNumberOfPeerOnChain(int groupId, String nodeId) {
//        SyncStatus syncStatus = frontInterface.getSyncStatus(groupId);
//        if (nodeId.equals(syncStatus.getPeerId())) {
//            return syncStatus.getBlockNumber();
//        }
//        List<PeerOfSyncStatus> peerList = syncStatus.getPeers();
//        BigInteger latestNumber = peerList.stream().filter(peer -> nodeId.equals(peer.getPeerId()))
//                .map(s -> s.getBlockNumber()).findFirst().orElse(BigInteger.ZERO);//blockNumber
//        return latestNumber;
//    }


}
