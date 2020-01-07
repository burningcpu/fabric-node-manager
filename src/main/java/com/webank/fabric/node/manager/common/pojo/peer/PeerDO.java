package com.webank.fabric.node.manager.common.pojo.peer;

import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * channel info on db.
 */
@Data
@Builder
public class PeerDO {
    private Integer peerId;
    private Integer channelId;
    private String peerName;
    private String peerAddress;
    private BigInteger blockNumber;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
}

