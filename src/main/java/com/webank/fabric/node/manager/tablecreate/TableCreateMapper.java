package com.webank.fabric.node.manager.tablecreate;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * interface of create table.
 */
@Repository
public interface TableCreateMapper {

    List<String> queryTables(@Param("dbName") java.lang.String dbName, @Param("tableName") java.lang.String tableName);

    int dropTable(@Param("dbName") String dbName, @Param("tableName") String tableName);

    int deleteByTableName(@Param("tableName") String tableName);

    int createTbBlock(@Param("tableName") String tableName);

    int createTransHash(@Param("tableName") String tableName);

}
