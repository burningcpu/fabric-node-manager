package com.webank.fabric.node.manager.common.pojo.chaincode;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

/**
 * chainCode info on db.
 */
@Data
@NoArgsConstructor
public class ChainCodeDO {
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
    public ChainCodeDO(ReqChainCodeVO reqChainCodeVO) {
        BeanUtils.copyProperties(reqChainCodeVO, this);
    }
}