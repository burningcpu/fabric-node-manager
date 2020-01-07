package com.webank.fabric.node.manager.common.pojo.channel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.webank.fabric.node.manager.common.pojo.base.BaseDO;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * front and channel mapping.
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@TableName(value = "tb_front_channel_map")
public class FrontChannelDO extends BaseDO implements Serializable {
    @TableId(value = "map_id", type = IdType.AUTO)
    private Integer mapId;
    @TableField(value = "front_id")
    private Integer frontId;
    @TableField(value = "channel_id")
    private Integer channelId;
}