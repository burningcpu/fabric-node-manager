package com.webank.fabric.node.manager.common.pojo.peer;

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

/**
 * channel info on db.
 */
@Data
@Builder
@TableName(value = "tb_peer")
@EqualsAndHashCode(callSuper = false)
public class PeerDO extends BaseDO implements Serializable {
    @TableId(value = "peer_id", type = IdType.AUTO)
    private Integer peerId;
    @TableField(value = "channel_id")
    private Integer channelId;
    @TableField(value = "`peer_name`")
    private String peerName;
    @TableField(value = "peer_address")
    private String peerAddress;
    @TableField(value = "block_height")
    private BigInteger blockHeight;
    @TableField(value = "description")
    private String description;
}

