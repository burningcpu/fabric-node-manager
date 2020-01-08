package com.webank.fabric.node.manager;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Slf4j
@EnableSwagger2
@EnableScheduling
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.webank.fabric.node.manager")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        log.info("main run success...");
    }


}
