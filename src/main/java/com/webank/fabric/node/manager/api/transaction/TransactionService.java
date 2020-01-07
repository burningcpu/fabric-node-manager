package com.webank.fabric.node.manager.api.transaction;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.webank.fabric.node.manager.common.pojo.transaction.TransactionDO;
import org.hyperledger.fabric.sdk.BlockInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

/**
 * service of Transaction.
 */
@Service
public class TransactionService {


    @Autowired
    private TransactionMapper transactionMapper;

    /**
     * save transaction.
     */
    public TransactionDO saveTransaction(BlockInfo.EnvelopeInfo envelopeInfo, BigInteger blockNumber) {
        // save transaction
        TransactionDO trans = TransactionDO.builder()
                .txId(envelopeInfo.getTransactionID())
                .blockNumber(blockNumber)
                .creator(envelopeInfo.getCreator().getId().substring(0, 60))
                .envelopeType(envelopeInfo.getType().toString())
                .build();

        QueryWrapper<TransactionDO> wrapper = Wrappers.query();
        wrapper.eq("trans_id", envelopeInfo.getTransactionID());
        transactionMapper.saveOrUpdate(trans, wrapper);
        return trans;
    }

}
