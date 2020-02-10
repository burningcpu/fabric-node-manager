package com.webank.fabric.node.manager.common.pojo.chaincode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * chainCode info of view.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RspChainCodeVO implements Serializable {
    private Integer chainCodePk;
    private Integer channelId;
    private String chainCodeLang;
    private String chainCodeName;
    private String chainCodeVersion;
    private String chainCodeSource;
    private String chainCodeId;
    private LocalDateTime deployTime;
    private Integer chainCodeStatus;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
}
