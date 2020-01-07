package com.webank.fabric.node.manager.common.pojo.channel;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * channel info on db.
 */
@Data
@Builder
public class ChannelDO implements Serializable {
    private Integer channelId;
    private String channelName;
    private Integer channelStatus;
    private long peerCount;
    private String channelDesc;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
}