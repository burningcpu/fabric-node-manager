package com.webank.fabric.node.manager.common.pojo.front;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * front info of view.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RspFrontVO implements Serializable {
    private Integer frontId;
    private String frontIp;
    private Integer frontPort;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
}
