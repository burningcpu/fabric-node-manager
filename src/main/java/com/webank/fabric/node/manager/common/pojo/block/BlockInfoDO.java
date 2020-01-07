package com.webank.fabric.node.manager.common.pojo.block;

import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * block info on db.
 */
@Data
@Builder
public class BlockInfoDO {
    private String pkHash;
    private BigInteger blockNumber;
    private Integer transCount;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
}