package com.webank.fabric.node.manager.common.pojo.block;

import com.webank.fabric.node.manager.common.pojo.transaction.TransactionInfoVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigInteger;
import java.util.List;

/**
 * block info of view.
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class BlockInfoVO {
    private String pkHash;
    private BigInteger blockNumber;
    private Integer transCount;
    private List<TransactionInfoVO> transList;
}
