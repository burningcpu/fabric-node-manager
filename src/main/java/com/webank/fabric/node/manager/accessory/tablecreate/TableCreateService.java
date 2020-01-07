/**
 * Copyright 2014-2019  the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.webank.fabric.node.manager.accessory.tablecreate;

import com.webank.fabric.node.manager.common.enums.TableName;
import com.webank.fabric.node.manager.common.exception.NodeMgrException;
import com.webank.fabric.node.manager.common.pojo.base.ConstantCode;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * service of table
 */
@Log4j2
@Service
public class TableCreateService {

    @Autowired
    private TableCreateMapper tableCreateMapper;
    @Value("${spring.datasource.url}")
    private String dbUrl;

    /**
     * create table by channelId
     */
    public void newTableByChannelId(int channelId) {
        if (channelId == 0) {
            return;
        }

        //tb_block_
        tableCreateMapper.createTbBlock(TableName.BLOCK.getTableName(channelId));
        //tb_trans_hash_
        tableCreateMapper.createTransHash(TableName.TRANS.getTableName(channelId));
    }

    /**
     * deop table.
     */
    public void dropTableByChannelId(int channelId) {
        Instant startTime = Instant.now();
        log.info("start dropTableByChannelId. startTime:{}", startTime.toEpochMilli());
        if (channelId == 0) {
            return;
        }
        for (TableName enumName : TableName.values()) {
            dropTableByName(enumName.getTableName(channelId));
        }
    }

    /**
     * drop table by tableName.
     */
    private void dropTableByName(String tableName) {
        log.info("start drop table. name:{}", tableName);
        if (StringUtils.isBlank(tableName)) {
            return;
        }
        List<String> tableNameList = tableCreateMapper.queryTables(getDbName(), tableName);
        if (tableNameList == null || tableNameList.isEmpty()) {
            log.warn("fail dropTableByName. not fount this table, tableName:{}", tableName);
            return;
        }
        int affectedRow = 1;
        while (affectedRow > 0) {
            affectedRow = tableCreateMapper.deleteByTableName(tableName);
            log.debug("delete table:{} affectedRow:{}", tableName, affectedRow);
        }

        //drop table
        tableCreateMapper.dropTable(getDbName(), tableName);
        log.info("end dropTableByName. name:{}", tableName);
    }

    /**
     * get db name.
     */
    private String getDbName() {
        if (StringUtils.isBlank(dbUrl)) {
            log.error("fail getDbName. dbUrl is null");
            throw new NodeMgrException(ConstantCode.SYSTEM_EXCEPTION);
        }
        String subUrl = dbUrl.substring(0, dbUrl.indexOf("?"));
        String dbName = subUrl.substring(subUrl.lastIndexOf("/") + 1);
        return dbName;
    }
}
