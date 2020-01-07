package com.webank.fabric.node.manager.common.pojo.channel;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * channel info on db.
 */
@Data
@Builder
public class ChannelDO {
    private Integer channelId;
    private String channelName;
    private Integer channelStatus;
    private long peerCount;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
}