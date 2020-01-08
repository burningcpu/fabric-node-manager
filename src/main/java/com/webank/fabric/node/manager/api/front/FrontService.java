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
import com.webank.fabric.node.manager.common.pojo.peer.PeerDO;
import com.webank.fabric.node.manager.common.pojo.peer.PeerDTO;
import com.webank.fabric.node.manager.common.utils.NodeMgrUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
        PeerDTO[] peerArr = frontRestManager.getPeersFromSpecificFront(frontIp, frontPort);
        List<PeerDTO> peerList = Arrays.asList(peerArr);
        //save front
        FrontDO frontDo = new FrontDO(reqFrontVO);
        frontMapper.add(frontDo);
        if (frontDo.getFrontId() == null || frontDo.getFrontId() == 0) {
            log.warn("fail newFront, after save, frontDo:{}", JSON.toJSONString(frontDo));
            throw new NodeMgrException(ConstantCode.SAVE_FRONT_FAIL);
        }

        //Filter duplicate nodes
        Map<String, PeerDTO> peerDtoMap = filterDuplicatePeers(peerList);
        //save channel
        Integer channelId = queryChannelId(peerDtoMap);
        ChannelDO channelDO;
        if (channelId == null) {
            channelDO = channelService.addChannel(channel, peerDtoMap.size());
        } else {
            channelDO = channelService.updatePeerCount(channelId, peerDtoMap.size());
        }
        frontChannelService.newFrontChannel(frontDo.getFrontId(), channelDO.getChannelId());
        //save peers
        peerService.savePeerInfo(frontIp, frontPort, channelDO.getChannelId(), peerDtoMap);
        //clear catch
        frontChannelService.clearMapList();
        return frontDo;
    }


    /**
     * Filter duplicate nodes
     */
    private Map<String, PeerDTO> filterDuplicatePeers(List<PeerDTO> peerList) {
        Map<String, PeerDTO> peerDtoMap = new HashMap<>();
        peerList.stream().forEach(p -> peerDtoMap.put(p.getPeerName(), NodeMgrUtils.object2JavaBean(p, PeerDTO.class)));
        for (PeerDTO peerDTO : peerList) {
            String peerName = peerDTO.getPeerName();
            if (peerDtoMap.containsKey(peerName) && StringUtils.isNotBlank(peerDTO.getPeerIp())) {
                peerDtoMap.put(peerName, peerDTO);
            }
        }
        return peerDtoMap;
    }

    /**
     * query channelId from databases.
     */
    private Integer queryChannelId(Map<String, PeerDTO> peerDtoMap) {
        Set<String> peerNameSet = peerDtoMap.keySet();
        for (String peerName : peerNameSet) {
            String peerIp = peerDtoMap.get(peerName).getPeerIp();
            int peerPort = peerDtoMap.get(peerName).getPeerPort();
            PeerDO peerDO = peerService.queryByIpPort(peerIp, peerPort);
            if (peerDO != null) {
                return peerDO.getChannelId();
            }
        }
        return null;
    }
}
