package com.webank.fabric.node.manager.api.channel;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.webank.fabric.node.manager.common.pojo.channel.FrontChannelDO;
import com.webank.fabric.node.manager.common.pojo.channel.FrontChannelUnionDO;
import com.webank.fabric.node.manager.common.pojo.peer.PeerDO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * interface of frontChannelMap in db.
 */
public interface FrontChannelMapper extends BaseMapper<FrontChannelDO> {
    List<FrontChannelDO> list();

    @Select("SELECT b.front_ip frontIp,b.front_port frontPort,a.* FROM tb_front_channel_map a LEFT JOIN tb_front b ON a.front_id = b.front_id")
    List<FrontChannelUnionDO> selectFrontChannelUnion();

    // 根据updateWrapper尝试更新，否继续执行saveOrUpdate(T)方法
    boolean saveOrUpdate(FrontChannelDO entity, Wrapper<FrontChannelDO> updateWrapper);
}
