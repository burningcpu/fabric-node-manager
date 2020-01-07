package com.webank.fabric.node.manager.api.channel;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.webank.fabric.node.manager.accessory.tablecreate.TableCreateService;
import com.webank.fabric.node.manager.common.pojo.channel.ChannelDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * service of front.
 */
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

        //create table by group id
        tableService.newTableByGroupId(groupId);
        //create table
        tableCreateService.newTableByChanneld(channelDO.getChannelId());

        return channelDO;
    }

}
