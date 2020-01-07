package com.webank.fabric.node.manager.common.pojo.front;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * properties of front.
 */
@Data
@Configuration
@ConfigurationProperties("front")
public class FrontProperties {
    private String serverAddress;
}
