package com.webank.fabric.node.manager.common.pojo.front;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * front info of view.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReqFrontVO {
    @NotBlank
    private String frontIp;
    @NotNull
    private Integer frontPort;

}
