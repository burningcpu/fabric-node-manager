package com.webank.fabric.node.manager.common.pojo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * properties of schedule.
 */
@Data
@Configuration
@ConfigurationProperties("security")
public class SecurityProperties {
    private Boolean isUseSecurity;
    private Long verificationCodeMaxAge;
    private Long authTokenMaxAge;
}
