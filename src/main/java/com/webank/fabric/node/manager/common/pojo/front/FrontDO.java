package com.webank.fabric.node.manager.common.pojo.front;

import lombok.Builder;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * front info on db.
 */
@Data
@Builder
public class FrontDO implements Serializable {
    private Integer frontId;
    private String agency;
    private String frontIp;
    private Integer frontPort;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;

    public FrontDO(ReqFrontVO reqFrontVO) {
        BeanUtils.copyProperties(reqFrontVO, this);
    }
}