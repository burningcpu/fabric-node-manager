package com.webank.fabric.node.manager.common.pojo.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * transaction info of view.
 */
@Data
@Builder
@Accessors(chain= true)
public class TransactionInfoVO {
    private String channel;
    private Long blockNumber;
    private String txId;
    private String creator;
    private LocalDateTime timestamp;
    private String envelopeType;
    private int actionCount;
    private List<TransactionActionVO> transactionActionList;
}
