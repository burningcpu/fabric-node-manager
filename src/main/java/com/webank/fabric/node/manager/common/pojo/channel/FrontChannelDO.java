package com.webank.fabric.node.manager.common.pojo.channel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * front and channel mapping.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FrontChannelDO {
    private Integer mapId;
    private Integer frontId;
    private Integer channelId;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
}