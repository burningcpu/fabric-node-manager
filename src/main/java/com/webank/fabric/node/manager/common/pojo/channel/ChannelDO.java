package com.webank.fabric.node.manager.common.pojo.channel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * channel info on db.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelDO {
    private Integer channelId;
    private String channelName;
    private Integer channelStatus;
    private long peerCount;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;

    public ChannelDO(String channelName, long peerCount) {
        this.channelName = channelName;
        this.peerCount = peerCount;
    }
}