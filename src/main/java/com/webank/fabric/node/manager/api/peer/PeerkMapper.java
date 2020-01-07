package com.webank.fabric.node.manager.api.peer;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.webank.fabric.node.manager.common.pojo.block.BlockInfoDO;
import com.webank.fabric.node.manager.common.pojo.channel.ChannelDO;
import com.webank.fabric.node.manager.common.pojo.peer.PeerDO;
import org.springframework.stereotype.Repository;

/**
 * interface of peer in db.
 */
@Repository
public interface PeerkMapper extends BaseMapper<PeerDO> {
    // 根据updateWrapper尝试更新，否继续执行saveOrUpdate(T)方法
    boolean saveOrUpdate(PeerDO entity, Wrapper<PeerDO> updateWrapper);
}
