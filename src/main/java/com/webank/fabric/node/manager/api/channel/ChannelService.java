package com.webank.fabric.node.manager.api.channel;

import com.alibaba.fastjson.JSON;
import com.webank.fabric.node.manager.accessory.tablecreate.TableCreateService;
import com.webank.fabric.node.manager.common.exception.NodeMgrException;
import com.webank.fabric.node.manager.common.pojo.base.ConstantCode;
import com.webank.fabric.node.manager.common.pojo.channel.ChannelDO;
import com.webank.fabric.node.manager.common.pojo.channel.ChannelGeneral;
import com.webank.fabric.node.manager.common.pojo.channel.StatisticalChannelTransInfo;
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

    /**
     * save channel.
     */
    public ChannelDO saveChannel(String channelName, long countOfPeers) {
        ChannelDO channelDO = ChannelDO.builder().channelName(channelName).peerCount(countOfPeers).build();
        channelMapper.save(channelDO);

        if (channelDO.getChannelId() == null) {
            log.error("");
            throw new NodeMgrException(ConstantCode.SAVE_CHANNEL_FAIL);
        }


        //create table by channel id
        tableCreateService.newTableByChannelId(channelDO.getChannelId());

        return channelDO;
    }


    /**
     * query count of group.
     */
    public Integer countOfChannel(Integer channelId, Integer groupStatus) throws NodeMgrException {
        log.debug("start countOfChannel channelId:{}", channelId);
        try {
            Integer count = channelMapper.getCount(channelId, groupStatus);
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
    public List<ChannelDO> getChannelList(Integer groupStatus) throws NodeMgrException {
        log.debug("start getChannelList");
        try {
            List<ChannelDO> groupList = channelMapper.getList(groupStatus);

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
        log.debug("start queryLatestStatisticalTrans");
        try {
            // qurey list
            List<StatisticalChannelTransInfo> listStatisticalTrans = channelMapper
                    .queryLatestStatisticalTrans();
            log.debug("end queryLatestStatisticalTrans listStatisticalTrans:{}",
                    JSON.toJSONString(listStatisticalTrans));
            return listStatisticalTrans;
        } catch (RuntimeException ex) {
            log.error("fail queryLatestStatisticalTrans", ex);
            throw new NodeMgrException(ConstantCode.DB_EXCEPTION);
        }
    }


}
