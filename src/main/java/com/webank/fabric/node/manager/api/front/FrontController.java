package com.webank.fabric.node.manager.api.front;

import com.webank.fabric.node.manager.common.pojo.front.FrontDO;
import com.webank.fabric.node.manager.common.pojo.front.ReqFrontVO;
import com.webank.fabric.node.manager.common.pojo.front.RspFrontVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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

}
