package com.webank.fabric.node.manager.api.block;

import com.alibaba.fastjson.JSON;
import com.google.protobuf.InvalidProtocolBufferException;
import com.webank.fabric.node.manager.common.enums.SqlSortType;
import com.webank.fabric.node.manager.common.exception.NodeMgrException;
import com.webank.fabric.node.manager.common.pojo.base.BasePageResponse;
import com.webank.fabric.node.manager.common.pojo.base.BaseResponse;
import com.webank.fabric.node.manager.common.pojo.base.ConstantCode;
import com.webank.fabric.node.manager.common.pojo.block.BlockInfoDO;
import com.webank.fabric.node.manager.common.pojo.block.BlockInfoVO;
import com.webank.fabric.node.manager.common.pojo.block.BlockListParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.hyperledger.fabric.sdk.BlockInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * controller of blockInfo.
 */
@Log4j2
@RestController
@RequestMapping(value = "block")
@Api(value = "block", tags = "about blockInfo")
public class BlockController {

    @Autowired
    private BlockService blockService;

    /**
     * query block list.
     */
    @GetMapping(value = "/blockList/{channelId}/{pageNumber}/{pageSize}")
    @ApiOperation(value = "blockList", notes = "get block list")
    public BasePageResponse queryBlockList(@PathVariable("channelId") Integer channelId,
                                           @PathVariable("pageNumber") Integer pageNumber,
                                           @PathVariable("pageSize") Integer pageSize,
                                           @RequestParam(value = "pkHash", required = false) String pkHash,
                                           @RequestParam(value = "blockNumber", required = false) BigInteger blockNumber)
            throws NodeMgrException, Exception {
        BasePageResponse pageResponse = new BasePageResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info(
                "start queryBlockList startTime:{} channelId:{} pageNumber:{} pageSize:{} "
                        + "pkHash:{} blockNumber:{}",
                startTime.toEpochMilli(), channelId,
                pageNumber, pageSize, pkHash, blockNumber);
        Integer count;
        // if query all block's count
        if (StringUtils.isEmpty(pkHash) && blockNumber == null) {
            count = blockService.queryCountOfBlockByMinus(channelId);
        } else {
            count = blockService.queryCountOfBlock(channelId, pkHash, blockNumber);
        }
        if (count != null && count > 0) {
            Integer start = Optional.ofNullable(pageNumber).map(page -> (page - 1) * pageSize)
                    .orElse(null);
            BlockListParam queryParam = new BlockListParam(start, pageSize, pkHash,
                    blockNumber, SqlSortType.DESC.getValue());
            List<BlockInfoDO> blockList = blockService.queryBlockList(channelId, queryParam);
            pageResponse.setData(blockList);
            pageResponse.setTotalCount(count);
        } else {
            BlockInfo blockInfo = null;
            if (blockNumber != null) {
                log.debug("did not find block, request from front. blockNumber:{} channelId:{}",
                        blockNumber, channelId);
                blockInfo = blockService.getBlockOnChainByNumber(channelId, blockNumber);
            } else if (StringUtils.isNotBlank(pkHash)) {
                log.debug(
                        "did not find block,request from front. pkHash:{} channelId:{}",
                        pkHash, channelId);
                try {
                    blockInfo = blockService.getblockFromFrontByHash(channelId, pkHash);
                } catch (NodeMgrException e) {
                    log.debug("queryBlockList did not find block from front(chain).e:[]", e);
                    pageResponse.setData(null);
                    pageResponse.setTotalCount(0);
                }
            }
            if (blockInfo != null) {
                BlockInfoDO blckInfoDo = BlockService.chainBlock2BlockInfoDO(blockInfo);
                pageResponse.setData(new BlockInfoDO[]{blckInfoDo});
                pageResponse.setTotalCount(1);
            }
        }

        log.info("end queryBlockList useTime:{} result:{}",
                Duration.between(startTime, Instant.now()).toMillis(), JSON.toJSONString(pageResponse));
        return pageResponse;
    }


    /**
     * get block by number.
     */
    @GetMapping("/blockByNumber/{channelId}/{blockNumber}")
    @ApiOperation(value = "blockByNumber", notes = "get block by number")
    public BaseResponse getBlockByNumber(@PathVariable("channelId") Integer channelId,
                                         @PathVariable("blockNumber") BigInteger blockNumber)
            throws NodeMgrException, InvalidProtocolBufferException {
        Instant startTime = Instant.now();
        log.info("start getBlockByNumber startTime:{} channelId:{} blockNumber:{}",
                startTime.toEpochMilli(), channelId, blockNumber);
        BaseResponse baseResponse = new BaseResponse(ConstantCode.SUCCESS);
        BlockInfoVO blockInfo = blockService.getBlockInfoVOOnChainByNumber(channelId, blockNumber);
        baseResponse.setData(blockInfo);
        log.info("end getBlockByNumber useTime:{} result:{}",
                Duration.between(startTime, Instant.now()).toMillis(), JSON.toJSONString(baseResponse));
        return baseResponse;
    }
}
