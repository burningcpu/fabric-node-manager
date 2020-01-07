package com.webank.fabric.node.manager.common.pojo.transaction;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.webank.fabric.node.manager.common.pojo.base.BaseDO;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;


/**
 * channel info on db.
 */
@Data
@Builder
@TableName(value = "tb_transaction")
@EqualsAndHashCode(callSuper = false)
public class TransactionDO extends BaseDO implements Serializable {
    @TableId(value = "trans_id", type = IdType.NONE)
    private String txId;
    @TableField(value = "block_number")
    private BigInteger blockNumber;
    @TableField(value = "creator")
    private String creator;
    @TableField(value = "action_count")
    private String actionCount;
    @TableField(value = "trans_timestamp")
    private LocalDateTime transTimestamp;
    @TableField(value = "envelope_type")
    private String envelopeType;
}