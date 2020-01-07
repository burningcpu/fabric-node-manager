package com.webank.fabric.node.manager.common.pojo.front;

import lombok.*;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

/**
 * front info on db.
 */
@Data
@NoArgsConstructor
public class FrontDO {
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