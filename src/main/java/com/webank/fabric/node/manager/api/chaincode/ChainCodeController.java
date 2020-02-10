package com.webank.fabric.node.manager.api.chaincode;

import com.alibaba.fastjson.JSON;
import com.webank.fabric.node.manager.common.enums.SqlSortType;
import com.webank.fabric.node.manager.common.exception.NodeMgrException;
import com.webank.fabric.node.manager.common.pojo.base.BasePageResponse;
import com.webank.fabric.node.manager.common.pojo.base.BaseResponse;
import com.webank.fabric.node.manager.common.pojo.base.ConstantCode;
import com.webank.fabric.node.manager.common.pojo.chaincode.*;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * controller of chainCode.
 */
@Log4j2
@RestController
@RequestMapping("chainCode")
@Api(value = "/chainCode", tags = "about chainCode")
public class ChainCodeController {

    @Autowired
    private ChainCodeService chainCodeService;

    /**
     * add new chainCode info.
     */
    @PostMapping
    public BaseResponse saveChainCode(@RequestBody @Valid ReqChainCodeVO chainCode) throws NodeMgrException {
        BaseResponse baseResponse = new BaseResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info("start saveChainCode startTime:{} chainCode:{}", startTime.toEpochMilli(),
                JSON.toJSONString(chainCode));

        // add chainCode row
        ChainCodeDO chainCodeDo = chainCodeService.saveChainCode(chainCode);

        baseResponse.setData(chainCodeDo);

        log.info("end saveChainCode useTime:{} result:{}",
                Duration.between(startTime, Instant.now()).toMillis(), JSON.toJSONString(baseResponse));
        return baseResponse;
    }


    /**
     * delete chainCode by id.
     */
    @DeleteMapping(value = "/{channelId}/{chainCodePk}")
    public BaseResponse deleteChainCode(@PathVariable("channelId") Integer channelId,
                                        @PathVariable("chainCodePk") Integer chainCodePk)
            throws NodeMgrException, Exception {
        BaseResponse baseResponse = new BaseResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info("start deleteChainCode startTime:{} chainCodePk:{} channelId:{}",
                startTime.toEpochMilli(),
                chainCodePk, channelId);

        chainCodeService.deleteChainCode(chainCodePk, channelId);

        log.info("end deleteChainCode useTime:{} result:{}",
                Duration.between(startTime, Instant.now()).toMillis(), JSON.toJSONString(baseResponse));
        return baseResponse;
    }


    /**
     * query chainCode info list.
     */
    @PostMapping(value = "/chainCodeList")
    public BasePageResponse queryChainCodeList(@RequestBody ReqChainCodeParamVO inputParam)
            throws NodeMgrException {
        BasePageResponse pagesponse = new BasePageResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info("start chainCodeList. startTime:{} inputParam:{}",
                startTime.toEpochMilli(), JSON.toJSONString(inputParam));

        //param
        ChainCodeParam queryParam = new ChainCodeParam();
        BeanUtils.copyProperties(inputParam, queryParam);

        int count = chainCodeService.countOfChainCode(queryParam);
        if (count > 0) {
            Integer start = Optional.ofNullable(inputParam.getPageNumber())
                    .map(page -> (page - 1) * inputParam.getPageSize()).orElse(0);
            queryParam.setStart(start);
            queryParam.setFlagSortedByTime(SqlSortType.DESC.getValue());
            // query list
            List<ChainCodeDO> listOfChainCode = chainCodeService.queryChainCodeList(queryParam);

            pagesponse.setData(listOfChainCode);
            pagesponse.setTotalCount(count);
        }

        log.info("end chainCodeList. useTime:{} result:{}",
                Duration.between(startTime, Instant.now()).toMillis(), JSON.toJSONString(pagesponse));
        return pagesponse;
    }


    /**
     * query by chainCodePk.
     */
    @GetMapping(value = "/{chainCodePk}")
    public BaseResponse queryChainCode(@PathVariable("chainCodePk") Integer chainCodePk)
            throws NodeMgrException, Exception {
        BaseResponse baseResponse = new BaseResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info("start queryChainCode startTime:{} chainCodePk:{}", startTime.toEpochMilli(),
                chainCodePk);

        ChainCodeDO contractRow = chainCodeService.queryByChainCodePk(chainCodePk);
        baseResponse.setData(contractRow);

        log.info("end queryChainCode useTime:{} result:{}",
                Duration.between(startTime, Instant.now()).toMillis(), JSON.toJSONString(baseResponse));
        return baseResponse;
    }

    /**
     * deploy deployInputParam.
     */
    @PostMapping(value = "/deploy")
    public BaseResponse deployChainCode(@RequestBody @Valid DeployInputParam deployInputParam) throws NodeMgrException {
        BaseResponse baseResponse = new BaseResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info("start queryChainCode startTime:{} deployInputParam:{}", startTime.toEpochMilli(),
                JSON.toJSONString(deployInputParam));

        ChainCodeDO chainCodeDo = chainCodeService.deployChainCode(deployInputParam);
        baseResponse.setData(chainCodeDo);

        log.info("end deployChainCode useTime:{} result:{}",
                Duration.between(startTime, Instant.now()).toMillis(), JSON.toJSONString(baseResponse));

        return baseResponse;
    }

    /**
     * send transaction.
     */
    @PostMapping(value = "/transaction")
    public BaseResponse sendTransaction(@RequestBody @Valid TransactionInputParam param) throws NodeMgrException {
        Instant startTime = Instant.now();
        log.info("start sendTransaction startTime:{} param:{}", startTime.toEpochMilli(),
                JSON.toJSONString(param));
        BaseResponse baseResponse = new BaseResponse(ConstantCode.SUCCESS);
        Object transRsp = chainCodeService.sendTransaction(param);
        baseResponse.setData(transRsp);
        log.info("end sendTransaction useTime:{} result:{}",
                Duration.between(startTime, Instant.now()).toMillis(), JSON.toJSONString(baseResponse));

        return baseResponse;
    }
}
