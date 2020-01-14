package com.webank.fabric.node.manager.api.transaction;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.protobuf.InvalidProtocolBufferException;
import com.webank.fabric.node.manager.api.front.FrontRestManager;
import com.webank.fabric.node.manager.common.enums.TableName;
import com.webank.fabric.node.manager.common.exception.NodeMgrException;
import com.webank.fabric.node.manager.common.pojo.base.ConstantCode;
import com.webank.fabric.node.manager.common.pojo.transaction.*;
import com.webank.fabric.node.manager.common.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.sdk.BlockInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
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
     * get TransactionDO from BlockInfo.EnvelopeInfo.
     */
    private TransactionDO getTransactionDO(BlockInfo.EnvelopeInfo envelopeInfo) {
        TransactionDO trans = null;
        if (envelopeInfo.getType() == TRANSACTION_ENVELOPE) {
            BlockInfo.TransactionEnvelopeInfo transactionEnvelopeInfo = (BlockInfo.TransactionEnvelopeInfo) envelopeInfo;


            trans = TransactionDO.builder()
                    .txId(envelopeInfo.getTransactionID())
                    .envelopeType(envelopeInfo.getType().toString())
                    .actionCount(transactionEnvelopeInfo.getTransactionActionInfoCount())
                    .transTimestamp(TimeUtils.LocalDateTimeFromDate(envelopeInfo.getTimestamp()))
                    .build();
        }
        return trans;
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

    /**
     * request front for transaction by hash.
     */
    public TransactionInfoVO getTransOnChainByTxId(Integer channelId, String txId)
            throws NodeMgrException, InvalidProtocolBufferException {
        log.info("start getTransOnChainByTxId. channelId:{}  txId:{}", channelId, txId);

        BlockInfo blockOnChain = frontRestManager.getBlockByTransactionId(channelId, txId);
        if (blockOnChain == null)
            return null;

        for (BlockInfo.EnvelopeInfo envelopeInfo : blockOnChain.getEnvelopeInfos()) {
            if (!txId.equals(envelopeInfo.getTransactionID()))
                continue;

            //convert BlockInfo.EnvelopeInfo to TransactionInfoVO
            TransactionInfoVO transactionInfoVO = getTransactionInfoVO(envelopeInfo);
            transactionInfoVO.setBlockNumber(BigInteger.valueOf(blockOnChain.getBlockNumber()));

            log.info("end getTransOnChainByTxId. transactionInfoVO:{}", JSON.toJSONString(transactionInfoVO));
            return transactionInfoVO;
        }
        return null;
    }

    /**
     * request front for transaction list by blockNumber.
     */
    public List<TransactionInfoVO> getTransListOnChainByBlockNumber(Integer channelId, BigInteger blockNumber)
            throws NodeMgrException, InvalidProtocolBufferException {
        log.info("start getTransOnChainByTxId. channelId:{}  blockNumber:{}", channelId, blockNumber);
        BlockInfo blockOnChain = frontRestManager.getBlockByNumber(channelId, blockNumber);
        List<TransactionInfoVO> transactionList = getTransactionInfoVOListByBlockInfo(blockOnChain);
        log.info("end getTransOnChainByTxId. transactionList:{}", JSON.toJSONString(transactionList));
        return transactionList;
    }

    /**
     * get List<TransactionInfoVO> from BlockInfo.
     */
    public List<TransactionInfoVO> getTransactionInfoVOListByBlockInfo(BlockInfo blockOnChain) {
        if (blockOnChain == null)
            return Lists.newArrayList();

        List<TransactionInfoVO> transactionList = new ArrayList<>(blockOnChain.getEnvelopeCount());
        for (BlockInfo.EnvelopeInfo envelopeInfo : blockOnChain.getEnvelopeInfos()) {
            //convert BlockInfo.EnvelopeInfo to TransactionInfoVO
            TransactionInfoVO transactionInfoVO = getTransactionInfoVO(envelopeInfo);
            transactionInfoVO.setBlockNumber(BigInteger.valueOf(blockOnChain.getBlockNumber()));
            transactionList.add(transactionInfoVO);
        }
        return transactionList;
    }


    /**
     * get TransactionInfoVO from BlockInfo.EnvelopeInfo.
     */
    public TransactionInfoVO getTransactionInfoVO(BlockInfo.EnvelopeInfo envelopeInfo) {
        //get action  list
        List<TransactionActionVO> actionList = getTransactionActionList(envelopeInfo);
        TransactionInfoVO transactionInfoVO = TransactionInfoVO.builder()
                .txId(envelopeInfo.getTransactionID())
                .envelopeType(envelopeInfo.getType().name())
                .transTimestamp(TimeUtils.LocalDateTimeFromDate(envelopeInfo.getTimestamp()))
                .transactionActionList(actionList)
                .actionCount(actionList.size())
                .build();


        return transactionInfoVO;
    }

    /**
     * get List<TransactionInfoVO> from BlockInfo.EnvelopeInfo.
     */
    private List<TransactionActionVO> getTransactionActionList(BlockInfo.EnvelopeInfo envelopeInfo) {
        if (envelopeInfo.getType() != TRANSACTION_ENVELOPE)
            return Lists.newArrayList();

        BlockInfo.TransactionEnvelopeInfo transactionEnvelopeInfo = (BlockInfo.TransactionEnvelopeInfo) envelopeInfo;
        List<TransactionActionVO> actionList = new ArrayList<>(transactionEnvelopeInfo.getTransactionActionInfoCount());
        for (BlockInfo.TransactionEnvelopeInfo.TransactionActionInfo actionInfo : transactionEnvelopeInfo.getTransactionActionInfos()) {
            actionList.add(getTransactionActionVO(actionInfo));
        }

        return actionList;
    }

    /**
     * Copy the attribute value of BlockInfo.TransactionEnvelopeInfo.TransactionActionInfo to TransactionActionVO.
     */
    private TransactionActionVO getTransactionActionVO(BlockInfo.TransactionEnvelopeInfo.TransactionActionInfo action) {
        List<Object> argList = Lists.newLinkedList();
        for (int i = 0; i < action.getChaincodeInputArgsCount(); i++) {
            Object str = action.getChaincodeInputArgs(i);
            argList.add(new String(action.getChaincodeInputArgs(i)));
        }
        TransactionActionVO actionVO = TransactionActionVO
                .builder()
                .chainCodeIDName(action.getChaincodeIDName())
                .chainCodeIDPath(action.getChaincodeIDPath())
                .chainCodeIDVersion(action.getChaincodeIDVersion())
                .chainCodeInputArgs(argList)
                .responseMessage(action.getResponseMessage())
                .responseStatus(action.getResponseStatus())
                .build();
        return actionVO;
    }

}
