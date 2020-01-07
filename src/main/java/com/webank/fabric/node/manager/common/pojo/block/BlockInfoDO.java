package com.webank.fabric.node.manager.common.pojo.block;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.webank.fabric.node.manager.common.pojo.base.BaseDO;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * block info on db.
 */
@Data
@Builder
@TableName(value = "tb_block")
public class BlockInfoDO extends BaseDO implements Serializable {
    @TableId(value = "channel_id", type = IdType.NONE)
    private String pkHash;
    @TableField(value = "block_number")
    private BigInteger blockNumber;
    @TableField(value = "trans_count")
    private Integer transCount;
}