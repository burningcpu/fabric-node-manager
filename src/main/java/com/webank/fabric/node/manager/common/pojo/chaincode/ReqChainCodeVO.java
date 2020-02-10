package com.webank.fabric.node.manager.common.pojo.chaincode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * chainCode info of view.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReqChainCodeVO {
    private Integer chainCodePk;
    @NotNull
    private Integer channelId;
    private String chainCodeLang;
    @NotBlank
    private String chainCodeName;
    @NotBlank
    private String chainCodeVersion;
    @NotNull
    private String chainCodeSource;
}
