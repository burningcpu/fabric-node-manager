package com.webank.fabric.node.manager.common.pojo.transaction;

import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDate;

/**
 * result of latest transCount.
 */
@Data
public class LatestTransCountBO {
    private BigInteger transNumber;
    private BigInteger transCount;
    private LocalDate transDay;
}
