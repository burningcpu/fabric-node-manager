package com.webank.fabric.node.manager.common.pojo.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

/**
 * transaction info of view.
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class TransactionInfoVO {
    private BigInteger blockNumber;
    private String txId;
    private LocalDateTime transTimestamp;
    private String envelopeType;
    private int actionCount;
    private List<TransactionActionVO> transactionActionList;

    public TransactionInfoVO(TransactionDO transactionDO) {
        BeanUtils.copyProperties(transactionDO, this);
    }
}
