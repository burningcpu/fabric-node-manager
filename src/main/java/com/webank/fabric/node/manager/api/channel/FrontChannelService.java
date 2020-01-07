package com.webank.fabric.node.manager.api.channel;
/**
 * service of front.
 */

import com.webank.fabric.node.manager.common.pojo.channel.FrontChannelDO;
import com.webank.fabric.node.manager.common.pojo.channel.FrontChannelUnionDO;
import com.webank.fabric.node.manager.common.pojo.channel.MapListParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * service of frontChannel.
 */
@Slf4j
@Service
public class FrontChannelService {
    private FrontChannelMapper frontChannelMapper;


    private static List<FrontChannelUnionDO> mapList;


    /**
     * add new mapping
     */
    public FrontChannelDO newFrontChannel(int frontId, int channelId) {
        FrontChannelDO frontChannelDO = FrontChannelDO.builder().frontId(frontId).channelId(channelId).build();
        frontChannelMapper.add(frontChannelDO);
        return frontChannelDO;
    }

    /**
     * clear mapList.
     */
    public void clearMapList() {
        mapList = null;
    }

    /**
     * reset mapList.
     */
    public List<FrontChannelUnionDO> resetMapList() {
        mapList = frontChannelMapper.getList(new MapListParam());
        return mapList;
    }

    /**
     * get mapList.
     */
    public List<FrontChannelUnionDO> getMapListByChannelId(int channelId) {
        List<FrontChannelUnionDO> list = getAllMap();
        if (list == null) {
            return null;
        }
        List<FrontChannelUnionDO> map = list.stream().filter(m -> channelId == m.getChannelId())
                .collect(Collectors.toList());
        return map;
    }

    /**
     * get all mapList.
     */
    public List<FrontChannelUnionDO> getAllMap() {
        if (mapList == null || mapList.size() == 0) {
            mapList = resetMapList();
        }
        return mapList;
    }
}
