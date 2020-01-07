package com.webank.fabric.node.manager.api.block;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.webank.fabric.node.manager.common.pojo.block.BlockInfoDO;
import com.webank.fabric.node.manager.common.pojo.channel.FrontChannelDO;
import org.springframework.stereotype.Repository;

/**
 * interface of blockInfo in db.
 */
@Repository
public interface BlockMapper extends BaseMapper<BlockInfoDO> {
    // 根据updateWrapper尝试更新，否继续执行saveOrUpdate(T)方法
    boolean saveOrUpdate(BlockInfoDO entity, Wrapper<BlockInfoDO> updateWrapper);
}
