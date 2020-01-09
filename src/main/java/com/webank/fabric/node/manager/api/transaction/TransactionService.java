package com.webank.fabric.node.manager.api.transaction;

import com.alibaba.fastjson.JSON;
import com.webank.fabric.node.manager.common.enums.TableName;
import com.webank.fabric.node.manager.common.exception.NodeMgrException;
import com.webank.fabric.node.manager.common.pojo.base.ConstantCode;
import com.webank.fabric.node.manager.common.pojo.transaction.MinMaxBlock;
import com.webank.fabric.node.manager.common.pojo.transaction.TransListParam;
import com.webank.fabric.node.manager.common.pojo.transaction.TransactionDO;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.sdk.BlockInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.hyperledger.fabric.sdk.BlockInfo.EnvelopeType.TRANSACTION_ENVELOPE;

/**
 * service of Transaction.
 */
@Slf4j
@Service
public class TransactionService {


    @Autowired
    private TransactionMapper transactionMapper;


    /**
     * save transaction info.
     */
    public void saveTransInfo(int channelId, BigInteger blockNumber, BlockInfo.EnvelopeInfo envelopeInfo) {
        // save transaction to db
        if (envelopeInfo.getType() == TRANSACTION_ENVELOPE) {
            BlockInfo.TransactionEnvelopeInfo transactionEnvelopeInfo = (BlockInfo.TransactionEnvelopeInfo) envelopeInfo;

            TransactionDO trans = TransactionDO.builder()
                    .txId(envelopeInfo.getTransactionID())
                    .blockNumber(blockNumber)
                    .creator(envelopeInfo.getCreator().getId().substring(0, 60))
                    .envelopeType(envelopeInfo.getType().toString())
                    .actionCount(transactionEnvelopeInfo.getTransactionActionInfoCount())
                    .transTimestamp(LocalDateTime.from(LocalDateTime.ofInstant(envelopeInfo.getTimestamp().toInstant(), ZoneId.systemDefault())))
                    .build();

            String tableName = TableName.TRANS.getTableName(channelId);
            transactionMapper.add(tableName, trans);
        }

    }


    /**
     * add trans hash info.
     */
    private void saveTransactionActionInfo(int channelId, TransactionDO trans) throws NodeMgrException {
        String tableName = TableName.TRANS.getTableName(channelId);
        transactionMapper.add(tableName, trans);
    }


    /**
     * query trans list.
     */
    public List<TransactionDO> queryTransList(int groupId, TransListParam param)
            throws NodeMgrException {
        log.debug("start queryTransList. TransListParam:{}", JSON.toJSONString(param));
        String tableName = TableName.TRANS.getTableName(groupId);
        List<TransactionDO> listOfTran = null;
        try {
            listOfTran = transactionMapper.getList(tableName, param);
        } catch (RuntimeException ex) {
            log.error("fail queryBlockList. TransListParam:{} ", JSON.toJSONString(param), ex);
            throw new NodeMgrException(ConstantCode.DB_EXCEPTION);
        }

        log.debug("end queryBlockList. listOfTran:{}", JSON.toJSONString(listOfTran));
        return listOfTran;
    }

    /**
     * query count of trans hash.
     */
    public Integer queryCountOfTran(int groupId, TransListParam queryParam)
            throws NodeMgrException {
        log.debug("start queryCountOfTran. queryParam:{}", JSON.toJSONString(queryParam));
        String tableName = TableName.TRANS.getTableName(groupId);
        try {
            Integer count = transactionMapper.getCount(tableName, queryParam);
            log.info("end queryCountOfTran. queryParam:{} count:{}", JSON.toJSONString(queryParam),
                    count);
            return count;
        } catch (RuntimeException ex) {
            log.error("fail queryCountOfTran. queryParam:{}", JSON.toJSONString(queryParam), ex);
            throw new NodeMgrException(ConstantCode.DB_EXCEPTION);
        }
    }

    /**
     * query count of trans by minus max and min trans_number
     */
    public Integer queryCountOfTranByMinus(int groupId)
            throws NodeMgrException {
        log.debug("start queryCountOfTranByMinus.");
        String tableName = TableName.TRANS.getTableName(groupId);
        try {
            Integer count = transactionMapper.getCountByMinMax(tableName);
            log.info("end queryCountOfTranByMinus. count:{}", count);
            if (count == null) {
                return 0;
            }
            return count;
        } catch (RuntimeException ex) {
            log.error("fail queryCountOfTranByMinus. ", ex);
            throw new NodeMgrException(ConstantCode.DB_EXCEPTION);
        }
    }


    /**
     * query min and max block number.
     */
    public List<MinMaxBlock> queryMinMaxBlock(int groupId) throws NodeMgrException {
        log.debug("start queryMinMaxBlock");
        String tableName = TableName.TRANS.getTableName(groupId);
        try {
            List<MinMaxBlock> listMinMaxBlock = transactionMapper.queryMinMaxBlock(tableName);
            int listSize = Optional.ofNullable(listMinMaxBlock).map(list -> list.size()).orElse(0);
            log.info("end queryMinMaxBlock listMinMaxBlockSize:{}", listSize);
            return listMinMaxBlock;
        } catch (RuntimeException ex) {
            log.error("fail queryMinMaxBlock", ex);
            throw new NodeMgrException(ConstantCode.DB_EXCEPTION);
        }
    }

    /**
     * Remove trans info.
     */
    public Integer remove(Integer groupId, Integer subTransNum) {
        String tableName = TableName.TRANS.getTableName(groupId);
        Integer affectRow = transactionMapper.remove(tableName, subTransNum, groupId);
        return affectRow;
    }


    /**
     * get tbTransInfo from chain
     */
//    public List<TransactionDO> getTransListFromChain(Integer groupId, String transHash,
//                                                   BigInteger blockNumber) {
//        log.debug("start getTransListFromChain.");
//        List<TransactionDO> transList = new ArrayList<>();
//        //find by transHash
//        if (transHash != null) {
//            TransactionDO tbTransHash = getTbTransFromFrontByHash(groupId, transHash);
//            if (tbTransHash != null) {
//                transList.add(tbTransHash);
//            }
//        }
//        //find trans by block number
//        if (transList.size() == 0 && blockNumber != null) {
//            List<TransactionInfo> transInBlock = frontInterface
//                    .getTransByBlockNumber(groupId, blockNumber);
//            if(transInBlock != null && transInBlock.size() != 0) {
//                transInBlock.stream().forEach(tran -> {
//                    TransactionDO tbTransHash = new TransactionDO(tran.getHash(), tran.getFrom(),
//                            tran.getTo(), tran.getBlockNumber(),
//                            null);
//                    transList.add(tbTransHash);
//                });
//            }
//        }
//        log.debug("end getTransListFromChain.");
//        return transList;
//    }


    /**
     * request front for transaction by hash.
     */
   /* public TransactionDO getTbTransFromFrontByHash(Integer groupId, String transHash)
            throws NodeMgrException {
        log.info("start getTransFromFrontByHash. groupId:{}  transaction:{}", groupId,
                transHash);
        TransactionInfo trans = frontInterface.getTransaction(groupId, transHash);
        TransactionDO tbTransHash = null;
        if (trans != null) {
            tbTransHash = new TransactionDO(transHash, trans.getFrom(), trans.getTo(),
                    trans.getBlockNumber(), null);
        }
        log.info("end getTransFromFrontByHash. tbTransHash:{}", JSON.toJSONString(tbTransHash));
        return tbTransHash;
    }

    *//**
     * get transaction receipt
     *//*
    public TransReceipt getTransReceipt(int groupId, String transHash) {
        return frontInterface.getTransReceipt(groupId, transHash);
    }


    *//**
     * get transaction info
     *//*
    public TransactionInfo getTransaction(int groupId, String transHash) {
        return frontInterface.getTransaction(groupId, transHash);
    }*/
}
