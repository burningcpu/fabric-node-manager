package com.webank.fabric.node.manager.common.pojo.peer;

import lombok.Data;

/**
 * peer information returned in front。
 */
@Data
public class PeerDTO {
    private String peerUrl;
    private String peerName;
    private Integer peerPort;
    private String peerIp;
}
