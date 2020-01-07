package com.webank.fabric.node.manager.common.pojo.channel;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * front and channel mapping.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FrontChannelUnionDO extends FrontChannelDO {
    private String frontIp;
    private Integer frontPort;
}
