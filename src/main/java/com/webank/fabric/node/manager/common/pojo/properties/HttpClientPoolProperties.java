package com.webank.fabric.node.manager.common.pojo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * properties of httpClient.
 */
@Data
@Configuration
@ConfigurationProperties("http-client.pool")
public class HttpClientPoolProperties {
    private int maxTotalConnect;
    private int maxConnectPerRoute;
    private int connectTimeout = 2 * 1000;
    private int readTimeout = 30 * 1000;
    private String charset = "UTF-8";
    private int retryTimes = 2;
    private int connectionRequestTimeout = 200;
    private Map<String, Integer> keepAliveTargetHost;
    private int keepAliveTime = 60;
}
