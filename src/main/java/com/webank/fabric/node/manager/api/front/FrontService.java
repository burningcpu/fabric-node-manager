package com.webank.fabric.node.manager.api.front;

import com.alibaba.fastjson.JSON;
import com.webank.fabric.node.manager.api.channel.ChannelService;
import com.webank.fabric.node.manager.api.channel.FrontChannelService;
import com.webank.fabric.node.manager.api.peer.PeerService;
import com.webank.fabric.node.manager.common.exception.NodeMgrException;
import com.webank.fabric.node.manager.common.pojo.base.ConstantCode;
import com.webank.fabric.node.manager.common.pojo.channel.ChannelDO;
import com.webank.fabric.node.manager.common.pojo.front.FrontDO;
import com.webank.fabric.node.manager.common.pojo.front.FrontParam;
import com.webank.fabric.node.manager.common.pojo.front.ReqFrontVO;
import com.webank.fabric.node.manager.common.pojo.peer.PeerDO;
import com.webank.fabric.node.manager.common.pojo.peer.PeerDTO;
import com.webank.fabric.node.manager.common.utils.NodeMgrUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
        //Confirm that the front has not been saved
        FrontParam param = FrontParam.builder().frontIp(frontIp).frontPort(frontPort).build();
        int count = frontMapper.getCount(param);
        if (count > 0) {
            log.error("font ip:{} port:{} already exists", frontIp, frontPort);
            throw new NodeMgrException(ConstantCode.FRONT_EXISTS);
        }
        //save front
        FrontDO frontDo = new FrontDO(reqFrontVO);
        frontMapper.add(frontDo);
        if (frontDo.getFrontId() == null || frontDo.getFrontId() == 0) {
            log.warn("fail newFront, after save, frontDo:{}", JSON.toJSONString(frontDo));
            throw new NodeMgrException(ConstantCode.SAVE_FRONT_FAIL);
        }

        //save channel
        Integer channelId = queryChannelId(peerList);
        ChannelDO channelDO;
        if (channelId == null) {
            channelDO = channelService.addChannel(channel, peerList.size());
        } else {
            channelDO = channelService.updatePeerCount(channelId, peerList.size());
        }
        frontChannelService.newFrontChannel(frontDo.getFrontId(), channelDO.getChannelId());
        //save peers
        peerService.savePeerInfo(frontIp, frontPort, channelDO.getChannelId(), peerList);
        //clear catch
        frontChannelService.clearMapList();
        return frontDo;
    }



    /**
     * query channelId from databases.
     */
    private Integer queryChannelId(List<PeerDTO> peerList) {
        for (PeerDTO peerDTO : peerList) {
            PeerDO peerDO = peerService.queryByIpAndPort(peerDTO.getPeerIp(), peerDTO.getPeerPort());
            if (peerDO != null) {
                return peerDO.getChannelId();
            }
        }
        return null;
    }

    /**
     * get front count
     */
    public int getFrontCount(FrontParam param) {
        Integer count = frontMapper.getCount(param);
        return count == null ? 0 : count;
    }

    /**
     * get front list
     */
    public List<FrontDO> getFrontList(FrontParam param) {
        return frontMapper.getList(param);
    }


    /**
     * remove front
     */
    public void removeFront(int frontId) {
        //check frontId
        FrontParam param = new FrontParam();
        param.setFrontId(frontId);
        int count = getFrontCount(param);
        if (count == 0) {
            throw new NodeMgrException(ConstantCode.INVALID_FRONT_ID);
        }

        //remove front
        frontMapper.remove(frontId);
        //remove map
        frontChannelService.removeByFrontId(frontId);
        //reset group list  TODO 需要加上这个逻辑
        //resetGroupListTask.asyncResetGroupList();
        //clear cache
        frontChannelService.clearMapList();
    }

    /**
     * query front by frontId.
     */
    public FrontDO getById(int frontId) {
        if (frontId == 0) {
            return null;
        }
        return frontMapper.getById(frontId);
    }
}
