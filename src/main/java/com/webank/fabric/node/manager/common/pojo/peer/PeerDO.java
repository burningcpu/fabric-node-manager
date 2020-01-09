package com.webank.fabric.node.manager.common.pojo.peer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * channel info on db.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PeerDO {
    private Integer peerId;
    private Integer channelId;
    private String peerName;
    private Integer peerPort;
    private String peerIp;
    private String peerUrl;
    private BigInteger blockNumber;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
}

