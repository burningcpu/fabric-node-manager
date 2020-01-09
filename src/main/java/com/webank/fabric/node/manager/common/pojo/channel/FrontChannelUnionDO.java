package com.webank.fabric.node.manager.common.pojo.channel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * front and channel mapping.
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FrontChannelUnionDO extends FrontChannelDO {
    private String frontIp;
    private Integer frontPort;
    private String channelName;
}
