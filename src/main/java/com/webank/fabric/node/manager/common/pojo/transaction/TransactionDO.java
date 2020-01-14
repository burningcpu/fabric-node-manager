package com.webank.fabric.node.manager.common.pojo.transaction;

import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;


/**
 * channel info on db.
 */
@Data
@Builder
public class TransactionDO {
    private BigInteger transNumber;
    private String txId;
    private BigInteger blockNumber;
    private int actionCount;
    private LocalDateTime transTimestamp;
    private String envelopeType;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
}