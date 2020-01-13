package com.webank.fabric.node.manager.common.pojo.transaction;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * transaction action info of view.
 */
@Data
@Builder
public class TransactionActionVO {
    private String chainCodeIDName;
    private String chainCodeIDPath;
    private String chainCodeIDVersion;
    private List<Object> chainCodeInputArgs;
    private String responseMessage;
    private int responseStatus;
}