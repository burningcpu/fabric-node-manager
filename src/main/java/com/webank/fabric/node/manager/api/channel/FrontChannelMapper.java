package com.webank.fabric.node.manager.api.channel;

import com.webank.fabric.node.manager.common.pojo.channel.FrontChannelDO;
import com.webank.fabric.node.manager.common.pojo.channel.FrontChannelUnionDO;
import com.webank.fabric.node.manager.common.pojo.channel.MapListParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * interface of frontChannelMap in db.
 */
public interface FrontChannelMapper {
    int add(FrontChannelDO frontChannelDO);

    int getCount(MapListParam mapListParam);

    int removeByChannelId(@Param("groupId") Integer channelId);

    int removeByFrontId(@Param("frontId") Integer frontId);

    List<FrontChannelUnionDO> getList(MapListParam mapListParam);
}
