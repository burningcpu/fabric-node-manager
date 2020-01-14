package com.webank.fabric.node.manager.api.transaction;

import com.webank.fabric.node.manager.common.pojo.transaction.LatestTransCountBO;
import com.webank.fabric.node.manager.common.pojo.transaction.MinMaxBlock;
import com.webank.fabric.node.manager.common.pojo.transaction.TransListParam;
import com.webank.fabric.node.manager.common.pojo.transaction.TransactionDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

/**
 * interface of Transaction in db.
 */
@Repository
public interface TransactionMapper {

    Integer add(@Param("tableName") String tableName, @Param("trans") TransactionDO transactionDO);

    Integer getCount(@Param("tableName") String tableName, @Param("param") TransListParam param);

    Integer getCountByMinMax(@Param("tableName") String tableName);

    List<TransactionDO> getList(@Param("tableName") String tableName, @Param("param") TransListParam param);

    List<MinMaxBlock> queryMinMaxBlock(@Param("tableName") String tableName);

    Integer remove(@Param("tableName") String tableName,
                   @Param("transRetainMax") BigInteger transRetainMax,
                   @Param("channelId") Integer channelId);

    List<LatestTransCountBO> queryLatestTransCount(@Param("tableName") String tableName, @Param("channelId") Integer channelId);
}
