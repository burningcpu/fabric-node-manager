package com.webank.fabric.node.manager.api.transaction;

import com.alibaba.fastjson.JSON;
import com.webank.fabric.node.manager.api.channel.ChannelService;
import com.webank.fabric.node.manager.common.enums.SqlSortType;
import com.webank.fabric.node.manager.common.exception.NodeMgrException;
import com.webank.fabric.node.manager.common.pojo.base.BasePageResponse;
import com.webank.fabric.node.manager.common.pojo.base.BaseResponse;
import com.webank.fabric.node.manager.common.pojo.base.ConstantCode;
import com.webank.fabric.node.manager.common.pojo.transaction.TransListParam;
import com.webank.fabric.node.manager.common.pojo.transaction.TransactionDO;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
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
    @GetMapping(value = "/transList/{groupId}/{pageNumber}/{pageSize}")
    public BasePageResponse queryTransList(@PathVariable("groupId") Integer groupId,
                                           @PathVariable("pageNumber") Integer pageNumber,
                                           @PathVariable("pageSize") Integer pageSize,
                                           @RequestParam(value = "transactionHash", required = false) String transHash,
                                           @RequestParam(value = "blockNumber", required = false) BigInteger blockNumber) {
        BasePageResponse pageResponse = new BasePageResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info(
                "start queryTransList. startTime:{} groupId:{} pageNumber:{} pageSize:{} "
                        + "transaction:{}",
                startTime.toEpochMilli(), groupId, pageNumber, pageSize, transHash);
        TransListParam queryParam = new TransListParam(transHash, blockNumber);
        Integer count;
        // if param's empty, getCount by minus between max and min
        if (StringUtils.isEmpty(transHash) && blockNumber == null) {
            count = transactionService.queryCountOfTranByMinus(groupId);
        } else {
            // TODO select count(1) in InnoDb is slow when data gets large
            count = transactionService.queryCountOfTran(groupId, queryParam);
        }
        if (count != null && count > 0) {
            Integer start = Optional.ofNullable(pageNumber).map(page -> (page - 1) * pageSize)
                    .orElse(null);
            queryParam.setStart(start);
            queryParam.setPageSize(pageSize);
            queryParam.setFlagSortedByBlock(SqlSortType.DESC.getValue());
            List<TransactionDO> transList = transactionService.queryTransList(groupId, queryParam);
            pageResponse.setData(transList);
            // on chain tx count
            pageResponse.setTotalCount(count);

            // TODO 需要加上这个逻辑：数据库没有就从链上查询
//        } else {
//            List<TransactionDO> transList = new ArrayList<>();
//            transList = transactionService.getTransListFromChain(groupId, transHash, blockNumber);
//            //result
//            if (transList.size() > 0) {
//                pageResponse.setData(transList);
//                pageResponse.setTotalCount(transList.size());
//            }
        }

        log.info("end queryBlockList useTime:{} result:{}",
                Duration.between(startTime, Instant.now()).toMillis(), JSON.toJSONString(pageResponse));
        return pageResponse;
    }


//    /**
//     * get transaction by hash.
//     */
//    @GetMapping("/transInfo/{groupId}/{transHash}")
//    public BaseResponse getTransaction(@PathVariable("groupId") Integer groupId,
//                                       @PathVariable("transHash") String transHash)
//            throws NodeMgrException {
//        Instant startTime = Instant.now();
//        log.info("start getTransaction startTime:{} groupId:{} transaction:{}",
//                startTime.toEpochMilli(), groupId, transHash);
//        BaseResponse baseResponse = new BaseResponse(ConstantCode.SUCCESS);
//        TransactionInfo transInfo = transactionService.getTransaction(groupId, transHash);
//        baseResponse.setData(transInfo);
//        log.info("end getTransaction useTime:{} result:{}",
//                Duration.between(startTime, Instant.now()).toMillis(), JSON.toJSONString(baseResponse));
//        return baseResponse;
//    }
}
