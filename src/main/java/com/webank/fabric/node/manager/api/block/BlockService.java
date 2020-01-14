package com.webank.fabric.node.manager.api.block;

import com.alibaba.fastjson.JSON;
import com.google.protobuf.InvalidProtocolBufferException;
import com.webank.fabric.node.manager.accessory.scheduler.ScheduleProperties;
import com.webank.fabric.node.manager.api.front.FrontRestManager;
import com.webank.fabric.node.manager.api.transaction.TransactionService;
import com.webank.fabric.node.manager.common.enums.TableName;
import com.webank.fabric.node.manager.common.exception.NodeMgrException;
import com.webank.fabric.node.manager.common.pojo.base.ConstantCode;
import com.webank.fabric.node.manager.common.pojo.block.BlockInfoDO;
import com.webank.fabric.node.manager.common.pojo.block.BlockInfoVO;
import com.webank.fabric.node.manager.common.pojo.block.BlockListParam;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.binary.Hex;
import org.hyperledger.fabric.sdk.BlockInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * service of block.
 */
@Log4j2
@Service
public class BlockService {

    BigInteger numberOne = new BigInteger("1");
    @Autowired
    private FrontRestManager frontRestManager;
    @Autowired
    private BlockMapper blockmapper;
    @Autowired
    private ScheduleProperties scheduleProperties;
    @Autowired
    private TransactionService transactionService;
    private static final Long SAVE_TRANS_SLEEP_TIME = 5L;


    /**
     * get block from chain by channelId
     */
    @Async(value = "mgrAsyncExecutor")
    public void pullBlockByChannelId(CountDownLatch latch, int channelId) {
        log.debug("start pullBlockByChannelId channelId:{}", channelId);
        try {
            //max block in chain
            BigInteger maxChainBlock = frontRestManager.getLatestChannelBlockNumber(channelId);
            //next block
            BigInteger nextBlock = getNextBlockNumber(channelId);

            //pull block
            while (Objects.nonNull(maxChainBlock) && maxChainBlock.compareTo(nextBlock) > 0) {
                log.debug("continue pull block. maxChainBlock:{} nextBlock:{}", maxChainBlock,
                        nextBlock);
                Thread.sleep(scheduleProperties.getPullBlockSleepTime());
                pullBlockByNumber(channelId, nextBlock);
                nextBlock = getNextBlockNumber(channelId);

                //reset maxChainBlock
                if (maxChainBlock.compareTo(nextBlock) < 0) {
                    maxChainBlock = frontRestManager.getLatestChannelBlockNumber(channelId);
                }
            }
        } catch (Exception ex) {
            log.error("fail pullBlockByChannelId. channelId:{} ", channelId, ex);
        } finally {
            latch.countDown();
        }
        log.debug("end pullBlockByChannelId channelId:{}", channelId);
    }


    /**
     * pull block by number.
     */
    private void pullBlockByNumber(int channelId, BigInteger blockNumber) throws InvalidProtocolBufferException {
        //get block by number
        BlockInfo blockInfo = getBlockOnChainByNumber(channelId, blockNumber);
        if (blockInfo == null) {
            log.info("pullBlockByNumber jump over. not found new block.");
            return;
        }

        //save block info
        saveBLockInfo(blockInfo, channelId);
    }

    /**
     * get next blockNumber
     */
    private BigInteger getNextBlockNumber(int channelId) {
        //get max blockNumber in table
        BigInteger localMaxBlockNumber = getLatestBlockNumberFromDB(channelId);
        if (Objects.nonNull(localMaxBlockNumber)) {
            return localMaxBlockNumber.add(BigInteger.ONE);
        }
        if (scheduleProperties.getIsBlockPullFromZero()) {
            return BigInteger.ZERO;
        } else {
            BigInteger initBlock = frontRestManager.getLatestChannelBlockNumber(channelId);
            if (initBlock.compareTo(scheduleProperties.getPullBlockInitCnts()) > 0) {
                initBlock = initBlock.subtract(scheduleProperties.getPullBlockInitCnts().
                        subtract(BigInteger.valueOf(1)));
            } else {
                initBlock = BigInteger.ZERO;
            }
            log.info("=== getNextBlockNumber init channelId:{} initBlock:{}", channelId, initBlock);
            return initBlock;
        }
    }


    /**
     * save report block info.
     */
    @Transactional
    public void saveBLockInfo(BlockInfo blockInfo, Integer channelId) throws NodeMgrException {

        // save block info
        BlockInfoDO blockInfoDO = chainBlock2BlockInfoDO(blockInfo);
        addBlockInfo(blockInfoDO, channelId);
        for (BlockInfo.EnvelopeInfo envelopeInfo : blockInfo.getEnvelopeInfos()) {
            transactionService.saveTransInfo(channelId, blockInfoDO.getBlockNumber(), envelopeInfo);
            try {
                Thread.sleep(SAVE_TRANS_SLEEP_TIME);
            } catch (InterruptedException ex) {
                log.error("saveBLockInfo", ex);
                Thread.currentThread().interrupt();
            }
        }
    }


    /**
     * copy chainBlock properties;
     */
    public static BlockInfoDO chainBlock2BlockInfoDO(BlockInfo blockInfo) {
        if (blockInfo == null)
            return null;

        String hash = Hex.encodeHexString(blockInfo.getDataHash());
        BigInteger blockNumber = BigInteger.valueOf(blockInfo.getBlockNumber());
        int transCount = blockInfo.getTransactionCount();

        // to BlockInfoDO
        BlockInfoDO blockInfoDO = BlockInfoDO.builder()
                .pkHash(hash)
                .blockNumber(blockNumber)
                .transCount(transCount)
                .build();

        return blockInfoDO;
    }


    /**
     * add block info to db.
     */
    @Transactional
    public void addBlockInfo(BlockInfoDO blockInfoDO, int channelId) throws NodeMgrException {
        log.debug("start addBlockInfo blockInfoDO:{}", JSON.toJSONString(blockInfoDO));
        String tableName = TableName.BLOCK.getTableName(channelId);
        //check newBLock == dbMaxBLock +1
        BigInteger dbMaxBLock = getLatestBlockNumberFromDB(channelId);
        BigInteger pullBlockNumber = blockInfoDO.getBlockNumber();
        if (dbMaxBLock != null && !(pullBlockNumber.compareTo(dbMaxBLock.add(numberOne)) == 0)) {
            log.info("fail addBlockInfo.  dbMaxBLock:{} pullBlockNumber:{}", dbMaxBLock,
                    pullBlockNumber);
            return;
        }

        // save block info
        blockmapper.add(tableName, blockInfoDO);
    }

    /**
     * query block info list.
     */
    public List<BlockInfoDO> queryBlockList(int channelId, BlockListParam queryParam)
            throws NodeMgrException {
        log.debug("start queryBlockList channelId:{},queryParam:{}", channelId,
                JSON.toJSONString(queryParam));

        List<BlockInfoDO> listOfBlock = blockmapper
                .getList(TableName.BLOCK.getTableName(channelId), queryParam);

        log.debug("end queryBlockList listOfBlockSize:{}", listOfBlock.size());
        return listOfBlock;
    }

    /**
     * query count of block.
     */
    public int queryCountOfBlock(Integer channelId, String pkHash, BigInteger blockNumber)
            throws NodeMgrException {
        log.debug("start countOfBlock channelId:{} pkHash:{} blockNumber:{}", channelId, pkHash,
                blockNumber);
        try {
            int count = blockmapper
                    .getCount(TableName.BLOCK.getTableName(channelId), pkHash, blockNumber);
            log.info("end countOfBlock channelId:{} pkHash:{} count:{}", channelId, pkHash, count);
            return count;
        } catch (RuntimeException ex) {
            log.error("fail countOfBlock channelId:{} pkHash:{}", channelId, pkHash, ex);
            throw new NodeMgrException(ConstantCode.DB_EXCEPTION);
        }
    }


    /**
     * remove block into.
     */
    public Integer remove(Integer channelId, BigInteger blockRetainMax)
            throws NodeMgrException {
        String tableName = TableName.BLOCK.getTableName(channelId);
        Integer affectRow = blockmapper.remove(tableName, blockRetainMax);
        return affectRow;
    }

    /**
     * get latest block number.
     */
    public BigInteger getLatestBlockNumberFromDB(int channelId) {
        return blockmapper.getLatestBlockNumber(TableName.BLOCK.getTableName(channelId));
    }

    /**
     * get block by block from front server
     */
    public BlockInfo getBlockOnChainByNumber(int channelId, BigInteger blockNumber) throws InvalidProtocolBufferException {
        return frontRestManager.getBlockByNumber(channelId, blockNumber);
    }

    /**
     * get BlockInfoVO by blockNumber on chain.
     */
    public BlockInfoVO getBlockInfoVOOnChainByNumber(int channelId, BigInteger blockNumber) throws InvalidProtocolBufferException {
        BlockInfo blockOnChain = getBlockOnChainByNumber(channelId, blockNumber);

        BlockInfoVO blockInfoVO = BlockInfoVO
                .builder()
                .pkHash(Hex.encodeHexString(blockOnChain.getDataHash()))
                .blockNumber(BigInteger.valueOf(blockOnChain.getBlockNumber()))
                .transCount(blockOnChain.getEnvelopeCount())
                .transList(transactionService.getTransactionInfoVOListByBlockInfo(blockOnChain))
                .build();
        return blockInfoVO;
    }

    /**
     * get block by block from front server
     */
    public BlockInfo getblockFromFrontByHash(int channelId, String pkHash) throws InvalidProtocolBufferException {
        return frontRestManager.getBlockByHash(channelId, pkHash);
    }

    public int queryCountOfBlockByMinus(Integer groupId) {
        log.debug("start queryCountOfBlockByMinus groupId:{}", groupId);
        try {
            int count = blockmapper
                    .getBlockCountByMinMax(TableName.BLOCK.getTableName(groupId));
            log.info("end queryCountOfBlockByMinus groupId:{} count:{}", groupId, count);
            return count;
        } catch (RuntimeException ex) {
            log.error("fail queryCountOfBlockByMinus groupId:{},exception:{}", groupId, ex);
            throw new NodeMgrException(ConstantCode.DB_EXCEPTION);
        }
    }

}
