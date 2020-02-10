package com.webank.fabric.node.manager.common.pojo.chaincode;

import lombok.Data;

/**
 * param of request for query chainCode.
 */
@Data
public class ReqChainCodeParamVO {
    private Integer chainCodePk;
    private Integer channelId;
    private String chainCodeName;
    private String chainCodeVersion;
    private String chainCodeId;
    private Integer chainCodeStatus;
    private Integer pageNumber;
    private Integer pageSize;
}
