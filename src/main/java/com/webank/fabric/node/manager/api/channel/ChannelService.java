package com.webank.fabric.node.manager.api.channel;

import com.alibaba.fastjson.JSON;
import com.webank.fabric.node.manager.api.front.FrontRestManager;
import com.webank.fabric.node.manager.common.enums.TableName;
import com.webank.fabric.node.manager.common.exception.NodeMgrException;
import com.webank.fabric.node.manager.common.pojo.base.ConstantCode;
import com.webank.fabric.node.manager.common.pojo.channel.ChannelDO;
import com.webank.fabric.node.manager.common.pojo.channel.ChannelGeneral;
import com.webank.fabric.node.manager.common.pojo.channel.StatisticalChannelTransInfo;
import com.webank.fabric.node.manager.tablecreate.TableCreateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * service of front.
 */
@Slf4j
@Service
public class ChannelService {

    @Autowired
    private ChannelMapper channelMapper;
    @Autowired
    private TableCreateService tableCreateService;
    @Autowired
    private FrontRestManager frontRestManager;


    /**
     * new channel.
     */
    public ChannelDO addChannel(String channelName, long countOfPeers) {
        ChannelDO channelDO = new ChannelDO(channelName, countOfPeers);
        channelMapper.add(channelDO);

        if (channelDO.getChannelId() == null) {
            log.error("saveChannel fail:channelId is null");
            throw new NodeMgrException(ConstantCode.SAVE_CHANNEL_FAIL);
        }

        //create table by channel id
        tableCreateService.newTableByChannelId(channelDO.getChannelId());

        return channelDO;
    }

    /**
     * update peer count.
     */
    public ChannelDO updatePeerCount(int channelId, int peerCount) {
        ChannelDO channelDO = channelMapper.queryByChannelId(channelId); 
        if (channelDO == null) {
            log.error("update updatePeerCount fail:invalid channelId[{}]", channelId);
            throw new NodeMgrException(ConstantCode.SAVE_CHANNEL_FAIL);
        }
        channelDO.setPeerCount(peerCount);
        channelMapper.update(channelDO);
        return channelDO;
    }

    /**
     * query by channelId
     */
    public ChannelDO queryByChannelId(int channelId) {
        return channelMapper.queryByChannelId(channelId);
    }

    /**
     * query count of group.
     */
    public Integer countOfChannel(Integer channelId, Integer channelStatus) throws NodeMgrException {
        log.debug("start countOfChannel channelId:{}", channelId);
        try {
            Integer count = channelMapper.getCount(channelId, channelStatus);
            log.debug("end countOfChannel channelId:{} count:{}", channelId, count);
            return count;
        } catch (RuntimeException ex) {
            log.error("fail countOfChannel", ex);
            throw new NodeMgrException(ConstantCode.DB_EXCEPTION);
        }
    }

    /**
     * query all group info.
     */
    public List<ChannelDO> getChannelList(Integer channelStatus) throws NodeMgrException {
        log.debug("start getChannelList");
        try {
            List<ChannelDO> groupList = channelMapper.getList(channelStatus);

            log.debug("end getChannelList groupList:{}", JSON.toJSONString(groupList));
            return groupList;
        } catch (RuntimeException ex) {
            log.error("fail getChannelList", ex);
            throw new NodeMgrException(ConstantCode.DB_EXCEPTION);
        }
    }


    /**
     * Check the validity of the channelId.
     */
    public void checkChannelId(Integer channelId) throws NodeMgrException {
        log.debug("start checkChannelId channelId:{}", channelId);

        if (channelId == null) {
            log.error("fail checkChannelId channelId is null");
            throw new NodeMgrException(ConstantCode.CHANNEL_ID_NULL);
        }

        Integer groupCount = countOfChannel(channelId, null);
        log.debug("checkChannelId channelId:{} groupCount:{}", channelId, groupCount);
        if (groupCount == null || groupCount == 0) {
            throw new NodeMgrException(ConstantCode.INVALID_CHANNEL_ID);
        }
        log.debug("end checkChannelId");
    }

    /**
     * query latest statistical trans.
     */
    public List<StatisticalChannelTransInfo> queryLatestStatisticalTrans() throws NodeMgrException {
        return channelMapper.queryLatestStatisticalTrans();
    }

    /**
     * get general of channel.
     */
    public ChannelGeneral queryChannelGeneral(Integer channelId) throws NodeMgrException {
        //get chainCodeNameList from chain
        List<String> chainCodeNameList = frontRestManager.getChainCodeNameList(channelId);
        String tableName = TableName.TRANS.getTableName(channelId);
        ChannelGeneral channelGeneral = channelMapper.getGeneral(tableName, channelId);
        channelGeneral.setChainCodeCount(chainCodeNameList.size());
        return channelGeneral;
    }

}
