package com.webank.fabric.node.manager.api.chaincode;

import com.webank.fabric.node.manager.common.pojo.chaincode.ChainCodeDO;
import com.webank.fabric.node.manager.common.pojo.chaincode.ChainCodeParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * interface of chainCode in db.
 */
@Repository
public interface ChainCodeMapper {

    Integer add(ChainCodeDO chainCodeDO);

    Integer remove(@Param("chainCodePk") Integer chainCodePk);

    Integer update(ChainCodeDO chainCodeDO);

    int countOfChainCode(ChainCodeParam param);

    List<ChainCodeDO> listOfChainCode(ChainCodeParam param);

    ChainCodeDO queryByChainCodePk(@Param("chainCodePk") Integer chainCodePk);

    ChainCodeDO queryChainCode(ChainCodeParam queryParam);
}
