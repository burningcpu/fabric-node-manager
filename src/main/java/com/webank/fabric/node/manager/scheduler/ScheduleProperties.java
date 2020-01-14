package com.webank.fabric.node.manager.scheduler;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigInteger;

/**
 * properties of front.
 */
@Data
@Configuration
@ConfigurationProperties("schedule")
public class ScheduleProperties {
    private Long pullBlockSleepTime;
    private Boolean isBlockPullFromZero;
    private BigInteger pullBlockInitCnts;
    private Long pullBlockTaskFixedDelay;
    private boolean isDeleteInfo;
    private BigInteger transRetainMax;
    private BigInteger blockRetainMax;
    private String deleteInfoCron;
    private String statisticsTransDailyCron;
}
