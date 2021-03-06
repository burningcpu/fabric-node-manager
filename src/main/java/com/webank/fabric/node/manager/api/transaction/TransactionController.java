package com.webank.fabric.node.manager.api.transaction;

import com.alibaba.fastjson.JSON;
import com.google.protobuf.InvalidProtocolBufferException;
import com.webank.fabric.node.manager.api.channel.ChannelService;
import com.webank.fabric.node.manager.common.enums.SqlSortType;
import com.webank.fabric.node.manager.common.exception.NodeMgrException;
import com.webank.fabric.node.manager.common.pojo.base.BasePageResponse;
import com.webank.fabric.node.manager.common.pojo.base.BaseResponse;
import com.webank.fabric.node.manager.common.pojo.base.ConstantCode;
import com.webank.fabric.node.manager.common.pojo.transaction.SevenDaysTrans;
import com.webank.fabric.node.manager.common.pojo.transaction.TransListParam;
import com.webank.fabric.node.manager.common.pojo.transaction.TransactionDO;
import com.webank.fabric.node.manager.common.pojo.transaction.TransactionInfoVO;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * controller of transaction.
 */
@Log4j2
@RestController
@RequestMapping(value = "transaction")
@Api(value = "transaction", tags = "about transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private TransDailyService transDailyService;


    /**
     * query trans list.
     */
    @GetMapping(value = "/transList/{channelId}/{pageNumber}/{pageSize}")
    public BasePageResponse queryTransList(@PathVariable("channelId") Integer channelId,
                                           @PathVariable("pageNumber") Integer pageNumber,
                                           @PathVariable("pageSize") Integer pageSize,
                                           @RequestParam(value = "txId", required = false) String txId,
                                           @RequestParam(value = "blockNumber", required = false) BigInteger blockNumber) throws InvalidProtocolBufferException {
        BasePageResponse pageResponse = new BasePageResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info(
                "start queryTransList. startTime:{} channelId:{} pageNumber:{} pageSize:{} "
                        + "txId:{}",
                startTime.toEpochMilli(), channelId, pageNumber, pageSize, txId);

        Integer count = null;
        List<TransactionInfoVO> transList = Lists.newArrayList();
        if (StringUtils.isNotBlank(txId)) {
            TransactionInfoVO transInfo = transactionService.getTransOnChainByTxId(channelId, txId);
            if (Objects.nonNull(transInfo)) {
                count = 1;
                transList.add(transInfo);
            }
        } else if (Objects.nonNull(blockNumber)) {
            transList = transactionService.getTransListOnChainByBlockNumber(channelId, blockNumber);
            count = transList.size();
        } else if ((count = transactionService.queryCountOfTranByMinus(channelId)) != null && count > 0) {
            //query trans list form database
            //param
            Integer start = Optional.ofNullable(pageNumber).map(page -> (page - 1) * pageSize)
                    .orElse(null);
            TransListParam queryParam = new TransListParam();
            queryParam.setStart(start);
            queryParam.setPageSize(pageSize);
            queryParam.setFlagSortedByTransNumber(SqlSortType.DESC.getValue());

            //query from database
            List<TransactionDO> transDoList = transactionService.queryTransList(channelId, queryParam);
            for (TransactionDO transDo : transDoList)
                transList.add(new TransactionInfoVO(transDo));
        }

        if (count != null && count > 0) {
            pageResponse.setData(transList);
            pageResponse.setTotalCount(count);
        }

        log.info("end queryBlockList useTime:{} result:{}", Duration.between(startTime, Instant.now()).toMillis(), JSON.toJSONString(pageResponse));
        return pageResponse;
    }


    /**
     * get transaction by hash.
     */
    @GetMapping("/detail/{channelId}/{txId}")
    public BaseResponse getTransactionByTxId(@PathVariable("channelId") Integer channelId,
                                             @PathVariable("txId") String txId)
            throws NodeMgrException, InvalidProtocolBufferException {
        Instant startTime = Instant.now();
        log.info("start getTransactionByTxId startTime:{} channelId:{} txId:{}",
                startTime.toEpochMilli(), channelId, txId);
        BaseResponse baseResponse = new BaseResponse(ConstantCode.SUCCESS);
        TransactionInfoVO transInfo = transactionService.getTransOnChainByTxId(channelId, txId);
        baseResponse.setData(transInfo);
        log.info("end getTransactionByTxId useTime:{} result:{}",
                Duration.between(startTime, Instant.now()).toMillis(), JSON.toJSONString(baseResponse));
        return baseResponse;
    }

    /**
     * get trans daily.
     */
    @GetMapping("/transDaily/{channelId}")
    public BaseResponse getTransDaily(@PathVariable("channelId") Integer channelId) throws Exception {
        BaseResponse pagesponse = new BaseResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info("start getTransDaily startTime:{} channelId:{}", startTime.toEpochMilli(), channelId);

        // query trans daily
        List<SevenDaysTrans> listTrans = transDailyService.listSevenDayOfTrans(channelId);
        pagesponse.setData(listTrans);

        log.info("end getTransDaily useTime:{} result:{}",
                Duration.between(startTime, Instant.now()).toMillis(),
                JSON.toJSONString(pagesponse));
        return pagesponse;
    }
}
