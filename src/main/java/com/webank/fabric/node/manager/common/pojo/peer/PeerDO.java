package com.webank.fabric.node.manager.common.pojo.peer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * channel info on db.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PeerDO {
    private Integer peerId;
    private Integer channelId;
    private String peerName;
    private String peerPort;
    private String peerIp;
    private String peerUrl;
    private BigInteger blockNumber;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;

    public PeerDO(String peerName, Integer channelId, String peerUrl, BigInteger blockNumber) {
        this.peerName = peerName;
        this.channelId = channelId;
        this.peerUrl = peerUrl;
        this.blockNumber = blockNumber;
    }
}

