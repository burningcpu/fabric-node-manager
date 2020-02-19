package com.webank.fabric.node.manager.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.temporal.TemporalUnit;

import static java.time.temporal.ChronoUnit.SECONDS;

@Configuration
public class BeanConfig {
    @Value("${constant.contractDeployTimeOut}")
    private long contractDeployTimeOut;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Bean
    public RestTemplate deployRestTemplate() {
        return restTemplateBuilder
                .setReadTimeout(Duration.ofMillis(contractDeployTimeOut))
                .setConnectTimeout(Duration.ofMillis(contractDeployTimeOut))
                .build();
    }

}
