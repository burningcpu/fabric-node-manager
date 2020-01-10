package com.webank.fabric.node.manager.api.front;

import com.alibaba.fastjson.JSON;
import com.webank.fabric.node.manager.common.exception.NodeMgrException;
import com.webank.fabric.node.manager.common.pojo.base.BasePageResponse;
import com.webank.fabric.node.manager.common.pojo.base.BaseResponse;
import com.webank.fabric.node.manager.common.pojo.base.ConstantCode;
import com.webank.fabric.node.manager.common.pojo.front.FrontDO;
import com.webank.fabric.node.manager.common.pojo.front.FrontParam;
import com.webank.fabric.node.manager.common.pojo.front.ReqFrontVO;
import com.webank.fabric.node.manager.common.pojo.front.RspFrontVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * controller of front.
 */
@Log4j2
@RestController
@RequestMapping("front")
@Api(value = "/front", tags = "about front")
public class FrontController {
    @Autowired
    private FrontService frontService;

    /**
     * add new front
     */
    @PostMapping("/new")
    @ApiOperation(value = "new", notes = "new front")
    public RspFrontVO newFront(@RequestBody @Valid ReqFrontVO frontInfo) {
        FrontDO frontDO = frontService.newFront(frontInfo);
        RspFrontVO rsp = new RspFrontVO();
        BeanUtils.copyProperties(frontDO, rsp);
        return rsp;
    }

    /**
     * qurey front info list.
     */
    @GetMapping(value = "/frontList")
    @ApiOperation(value = "frontList", notes = "query front list")
    public BasePageResponse queryFrontList(
            @RequestParam(value = "frontId", required = false) Integer frontId,
            @RequestParam(value = "channelId", required = false) Integer channelId)
            throws NodeMgrException {
        BasePageResponse pagesponse = new BasePageResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info("start queryFrontList startTime:{} frontId:{} channelId:{}",
                startTime.toEpochMilli(), frontId, channelId);

        //param
        FrontParam param = new FrontParam();
        param.setFrontId(frontId);
        param.setChannelId(channelId);

        //query front info
        int count = frontService.getFrontCount(param);
        pagesponse.setTotalCount(count);
        if (count > 0) {
            List<FrontDO> list = frontService.getFrontList(param);
            pagesponse.setData(list);
        }

        log.info("end queryFrontList useTime:{} result:{}",
                Duration.between(startTime, Instant.now()).toMillis(), JSON.toJSONString(pagesponse));
        return pagesponse;
    }

    /**
     * delete by frontId
     */
    @DeleteMapping(value = "/{frontId}")
    @ApiOperation(value = "removeFront", notes = "delete by front id")
    public BaseResponse removeFront(@PathVariable("frontId") Integer frontId) {
        Instant startTime = Instant.now();
        log.info("start removeFront startTime:{} frontId:{}",
                startTime.toEpochMilli(), frontId);
        BaseResponse baseResponse = new BaseResponse(ConstantCode.SUCCESS);

        //remove
        frontService.removeFront(frontId);

        log.info("end removeFront useTime:{} result:{}",
                Duration.between(startTime, Instant.now()).toMillis(), JSON.toJSONString(baseResponse));
        return baseResponse;
    }

}
