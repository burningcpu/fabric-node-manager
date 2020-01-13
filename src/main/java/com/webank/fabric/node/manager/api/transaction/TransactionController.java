package com.webank.fabric.node.manager.api.transaction;

import com.alibaba.fastjson.JSON;
import com.google.protobuf.InvalidProtocolBufferException;
import com.webank.fabric.node.manager.api.channel.ChannelService;
import com.webank.fabric.node.manager.common.enums.SqlSortType;
import com.webank.fabric.node.manager.common.exception.NodeMgrException;
import com.webank.fabric.node.manager.common.pojo.base.BasePageResponse;
import com.webank.fabric.node.manager.common.pojo.base.BaseResponse;
import com.webank.fabric.node.manager.common.pojo.base.ConstantCode;
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


    /**
     * query trans list.
     */
    @GetMapping(value = "/transList/{channelId}/{pageNumber}/{pageSize}")
    public BasePageResponse queryTransList(@PathVariable("channelId") Integer channelId,
                                           @PathVariable("pageNumber") Integer pageNumber,
                                           @PathVariable("pageSize") Integer pageSize,
                                           @RequestParam(value = "transactionId", required = false) String transactionId,
                                           @RequestParam(value = "blockNumber", required = false) BigInteger blockNumber) throws InvalidProtocolBufferException {
        BasePageResponse pageResponse = new BasePageResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info(
                "start queryTransList. startTime:{} channelId:{} pageNumber:{} pageSize:{} "
                        + "transaction:{}",
                startTime.toEpochMilli(), channelId, pageNumber, pageSize, transactionId);

        Integer count = null;
        List<TransactionDO> transList = Lists.newArrayList();
        if (StringUtils.isNotBlank(transactionId)) {
            TransactionInfoVO transInfo = transactionService.getTransOnChainByTxId(channelId, transactionId);

        } else if (Objects.nonNull(blockNumber)) {
            return null;
        } else if ((count = transactionService.queryCountOfTranByMinus(channelId)) != null && count > 0) {
            //query trans list form database
            //param
            Integer start = Optional.ofNullable(pageNumber).map(page -> (page - 1) * pageSize)
                    .orElse(null);
            TransListParam queryParam = new TransListParam();
            queryParam.setStart(start);
            queryParam.setPageSize(pageSize);
            queryParam.setFlagSortedByBlock(SqlSortType.DESC.getValue());

            //query from database
            transList = transactionService.queryTransList(channelId, queryParam);
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
    @GetMapping("/transDetail/{channelId}/{transactionId}")
    public BaseResponse getTransactionByTxId(@PathVariable("channelId") Integer channelId,
                                             @PathVariable("transactionId") String transactionId)
            throws NodeMgrException, InvalidProtocolBufferException {
        Instant startTime = Instant.now();
        log.info("start getTransactionByTxId startTime:{} channelId:{} transactionId:{}",
                startTime.toEpochMilli(), channelId, transactionId);
        BaseResponse baseResponse = new BaseResponse(ConstantCode.SUCCESS);
        TransactionInfoVO transInfo = transactionService.getTransOnChainByTxId(channelId, transactionId);
        baseResponse.setData(transInfo);
        log.info("end getTransactionByTxId useTime:{} result:{}",
                Duration.between(startTime, Instant.now()).toMillis(), JSON.toJSONString(baseResponse));
        return baseResponse;
    }
}
