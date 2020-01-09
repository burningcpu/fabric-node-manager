package com.webank.fabric.node.manager.common.pojo.block;

import com.webank.fabric.node.manager.common.pojo.base.BaseQueryParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigInteger;
import java.time.LocalDate;

/**
 * param of query block list.
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BlockListParam extends BaseQueryParam {
    private String pkHash;
    private BigInteger blockNumber;
    private BigInteger minBlockNumber;
    private LocalDate minDay;
    private String flagSortedByBlock;

    /**
     * init by start、pageSize、pkHash、blockNumber.
     */
    public BlockListParam(Integer start, Integer pageSize, String pkHash,
                          BigInteger blockNumber,String flagSortedByBlock) {
        super(start, pageSize);
        this.pkHash = pkHash;
        this.blockNumber = blockNumber;
        this.flagSortedByBlock = flagSortedByBlock;
    }

    /**
     * init by minBlockNumber、minDay、flagSortedByBlock.
     */
    public BlockListParam(BigInteger minBlockNumber, LocalDate minDay,
                          String flagSortedByBlock) {
        this.minBlockNumber = minBlockNumber;
        this.minDay = minDay;
        this.flagSortedByBlock = flagSortedByBlock;
    }

}