package com.webank.fabric.node.manager.api.transaction;

import com.alibaba.fastjson.JSON;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.webank.fabric.node.manager.api.front.FrontRestManager;
import com.webank.fabric.node.manager.common.enums.TableName;
import com.webank.fabric.node.manager.common.exception.NodeMgrException;
import com.webank.fabric.node.manager.common.pojo.base.ConstantCode;
import com.webank.fabric.node.manager.common.pojo.transaction.MinMaxBlock;
import com.webank.fabric.node.manager.common.pojo.transaction.TransListParam;
import com.webank.fabric.node.manager.common.pojo.transaction.TransactionDO;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.protos.common.Common;
import org.hyperledger.fabric.sdk.BlockInfo;
import org.hyperledger.fabric.sdk.TransactionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
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
    @Autowired
    private FrontRestManager frontRestManager;


    /**
     * save transaction info.
     */
    public void saveTransInfo(int channelId, BigInteger blockNumber, BlockInfo.EnvelopeInfo envelopeInfo) {

        TransactionDO trans = getTransactionDO(envelopeInfo);
        if (trans != null) {
            trans.setBlockNumber(blockNumber);
            String tableName = TableName.TRANS.getTableName(channelId);
            transactionMapper.add(tableName, trans);
        }
    }

    /**
     * build TransactionDO from BlockInfo.EnvelopeInfo.
     */
    private TransactionDO getTransactionDO(BlockInfo.EnvelopeInfo envelopeInfo) {
        TransactionDO trans = null;
        if (envelopeInfo.getType() == TRANSACTION_ENVELOPE) {
            BlockInfo.TransactionEnvelopeInfo transactionEnvelopeInfo = (BlockInfo.TransactionEnvelopeInfo) envelopeInfo;


            trans = TransactionDO.builder()
                    .txId(envelopeInfo.getTransactionID())
                    .creator(envelopeInfo.getCreator().getId().substring(0, 60))
                    .envelopeType(envelopeInfo.getType().toString())
                    .actionCount(transactionEnvelopeInfo.getTransactionActionInfoCount())
                    .transTimestamp(LocalDateTime.from(LocalDateTime.ofInstant(envelopeInfo.getTimestamp().toInstant(), ZoneId.systemDefault())))
                    .build();
        }
        return trans;
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
    public List<TransactionDO> queryTransList(int channelId, TransListParam param)
            throws NodeMgrException {
        log.debug("start queryTransList. TransListParam:{}", JSON.toJSONString(param));
        String tableName = TableName.TRANS.getTableName(channelId);
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
    public Integer queryCountOfTran(int channelId, TransListParam queryParam)
            throws NodeMgrException {
        log.debug("start queryCountOfTran. queryParam:{}", JSON.toJSONString(queryParam));
        String tableName = TableName.TRANS.getTableName(channelId);
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
    public Integer queryCountOfTranByMinus(int channelId)
            throws NodeMgrException {
        log.debug("start queryCountOfTranByMinus.");
        String tableName = TableName.TRANS.getTableName(channelId);
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
    public List<MinMaxBlock> queryMinMaxBlock(int channelId) throws NodeMgrException {
        log.debug("start queryMinMaxBlock");
        String tableName = TableName.TRANS.getTableName(channelId);
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
    public Integer remove(Integer channelId, Integer subTransNum) {
        String tableName = TableName.TRANS.getTableName(channelId);
        Integer affectRow = transactionMapper.remove(tableName, subTransNum, channelId);
        return affectRow;
    }

//    /**
//     * request front for transaction by hash.
//     */
//    public TbTransHash getTbTransFromFrontById(Integer channelId, String txId)
//            throws NodeMgrException, InvalidProtocolBufferException {
//        log.info("start getTransFromFrontByHash. channelId:{}  txId:{}", channelId, txId);
//
//        TransactionInfo transOnChain = frontRestManager.getTransactionById(channelId, txId);
//
//        Common.Envelope envelopeInfo = transOnChain.getEnvelope();
//        envelopeInfo.
//        ByteString str = envelopeInfo.getPayload();
//        BlockInfo.EnvelopeInfo.
//        TransactionDO trans = getTransactionDO(envelopeInfo);
//        TransactionDO transactionDO = null;
//        if (trans != null) {
//            transactionDO = TransactionDO.builder()
//                    .transTimestamp(trans.getProcessedTransaction().)
//                    .build();
//        }
//        log.info("end getTransFromFrontByHash. tbTransHash:{}", JSON.toJSONString(tbTransHash));
//        return tbTransHash;
//    }
//
//
//    /**
//     * get tbTransInfo from chain
//     */
//    public List<TbTransHash> getTransListFromChain(Integer channelId, String txId,
//                                                   BigInteger blockNumber) {
//        log.debug("start getTransListFromChain.");
//        List<TbTransHash> transList = new ArrayList<>();
//        //find by txId
//        if (txId != null) {
//            TbTransHash tbTransHash = getTbTransFromFrontByHash(channelId, txId);
//            if (tbTransHash != null) {
//                transList.add(tbTransHash);
//            }
//        }
//        //find trans by block number
//        if (transList.size() == 0 && blockNumber != null) {
//            List<TransactionInfo> transInBlock = frontInterface
//                    .getTransByBlockNumber(channelId, blockNumber);
//            if (transInBlock != null && transInBlock.size() != 0) {
//                transInBlock.stream().forEach(tran -> {
//                    TbTransHash tbTransHash = new TbTransHash(tran.getHash(), tran.getFrom(),
//                            tran.getTo(), tran.getBlockNumber(),
//                            null);
//                    transList.add(tbTransHash);
//                });
//            }
//        }
//        log.debug("end getTransListFromChain.");
//        return transList;
//    }
}
