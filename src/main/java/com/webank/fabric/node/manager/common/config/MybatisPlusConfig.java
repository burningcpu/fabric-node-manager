package com.webank.fabric.node.manager.common.config;

import com.baomidou.mybatisplus.extension.parsers.DynamicTableNameParser;
import com.baomidou.mybatisplus.extension.parsers.ITableNameHandler;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.HashMap;

/**
 * mybatis plus config.
 */
@Configuration
@MapperScan("com.webank.fabric.node.manager")
public class MybatisPlusConfig {
    public static ThreadLocal<String> myTableName = new ThreadLocal<>();


    /**
     * Paging plugin.
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        DynamicTableNameParser dynamicTableNameParser = dynamicTableNameParser();
        paginationInterceptor.setSqlParserList(Collections.singletonList(dynamicTableNameParser));
        return paginationInterceptor;
    }


    /**
     * get dynamicTableNameParser.
     */
    @Bean
    public DynamicTableNameParser dynamicTableNameParser() {
        DynamicTableNameParser dynamicTableNameParser = new DynamicTableNameParser();
        dynamicTableNameParser.setTableNameHandlerMap(new HashMap<String, ITableNameHandler>(2) {{
            put("tempTableName", (metaObject, sql, tableName) -> myTableName.get());
        }});
        return dynamicTableNameParser;
    }
}
