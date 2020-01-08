package com.webank.fabric.node.manager.api.channel;

import com.webank.fabric.node.manager.common.pojo.channel.ChannelDO;
import com.webank.fabric.node.manager.common.pojo.channel.ChannelGeneral;
import com.webank.fabric.node.manager.common.pojo.channel.StatisticalChannelTransInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * interface of channel in db.
 */
@Repository
public interface ChannelMapper {
    /**
     * add channel info.
     */
    int add(ChannelDO channelDO);

    /**
     * update channel info.
     */
    void update(ChannelDO channelDO);

    /**
     * query by channelId.
     */
    ChannelDO queryByChannelId(@Param("channelId") Integer channelId);

    /**
     * query channel count.
     */
    int getCount(@Param("channelId") Integer channelId, @Param("channelStatus") Integer channelStatus);

    /**
     * get all channel.
     */
    List<ChannelDO> getList(@Param("channelStatus") Integer channelStatus);

    /**
     * query the latest statistics trans on all channels.
     */
    List<StatisticalChannelTransInfo> queryLatestStatisticalTrans();

    /**
     * query general info.
     */
    ChannelGeneral getGeneral(@Param("channelId") Integer channelId);
}
