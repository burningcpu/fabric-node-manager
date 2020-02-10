package com.webank.fabric.node.manager.common.enums;

/**
 * Enumeration of chainCode status.
 */
public enum ChainCodeStatus {

    NOTDEPLOYED(1), DEPLOYED(2), DEPLOYMENTFAILED(3);

    private int value;

    private ChainCodeStatus(Integer dataStatus) {
        this.value = dataStatus;
    }

    public int getValue() {
        return this.value;
    }
}
