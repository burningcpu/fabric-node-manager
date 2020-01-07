package com.webank.fabric.node.manager.api.front;

import com.webank.fabric.node.manager.common.pojo.front.FrontDO;
import com.webank.fabric.node.manager.common.pojo.front.FrontParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * interface of front in db.
 */
@Repository
public interface FrontMapper {
    int add(FrontDO tbFront);

    int remove(@Param("frontId") int frontId);

    Integer getCount(FrontParam param);

    List<FrontDO> getList(FrontParam param);

    FrontDO getById(@Param("frontId") int frontId);
}
