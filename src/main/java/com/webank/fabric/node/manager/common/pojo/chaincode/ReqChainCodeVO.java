package com.webank.fabric.node.manager.common.pojo.chaincode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hyperledger.fabric.sdk.TransactionRequest;

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
    @Builder.Default
    private String chainCodeLang = TransactionRequest.Type.GO_LANG.toString();
    @NotBlank
    private String chainCodeName;
    @NotBlank
    private String chainCodeVersion;
    @NotNull
    private String chainCodeSourceBase64;
}
