package com.webank.fabric.node.manager.api.block;

import com.webank.fabric.node.manager.common.pojo.block.BlockInfoDO;
import com.webank.fabric.node.manager.common.pojo.block.BlockListParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

/**
 * interface of blockInfo in db.
 */
@Repository
public interface BlockMapper {
    /**
     * query latest block number
     */
    BigInteger getLatestBlockNumber(@Param("tableName") String tableName);

    /**
     * Add new block data.
     */
    Integer add(@Param("tableName") String tableName, @Param("block") BlockInfoDO blockInfoDO);


    /**
     * query list of block by page.
     */
    List<BlockInfoDO> getList(@Param("tableName") String tableName,
                              @Param("param") BlockListParam param);

    /**
     * query block count.
     */
    int getCount(@Param("tableName") String tableName, @Param("pkHash") String pkHash,
                 @Param("blockNumber") BigInteger blockNumber);

    /**
     * get block count by max minux min
     */
    int getBlockCountByMinMax(@Param("tableName") String tableName);

    /**
     * Delete block height.
     */
    Integer remove(@Param("tableName") String tableName,
                   @Param("blockRetainMax") BigInteger blockRetainMax);
}
