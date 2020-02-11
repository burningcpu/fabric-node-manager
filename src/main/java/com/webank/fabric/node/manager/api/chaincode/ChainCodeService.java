package com.webank.fabric.node.manager.api.chaincode;

import com.alibaba.fastjson.JSON;
import com.webank.fabric.node.manager.api.front.FrontRestManager;
import com.webank.fabric.node.manager.common.enums.ChainCodeStatus;
import com.webank.fabric.node.manager.common.exception.NodeMgrException;
import com.webank.fabric.node.manager.common.pojo.base.ConstantCode;
import com.webank.fabric.node.manager.common.pojo.chaincode.*;
import com.webank.fabric.node.manager.common.pojo.front.TransactionParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.*;

/**
 * service of chainCode.
 */
@Slf4j
@Service
public class ChainCodeService {

    @Autowired
    private ChainCodeMapper contractMapper;
    @Autowired
    private FrontRestManager frontRestManager;

    /**
     * add new chainCode data.
     */
    public ChainCodeDO saveChainCode(ReqChainCodeVO chainCode) throws NodeMgrException {
        log.debug("start addChainCodeInfo ChainCode:{}", JSON.toJSONString(chainCode));
        ChainCodeDO chainCodeDO;
        if (chainCode.getChainCodePk() == null) {
            chainCodeDO = newChainCode(chainCode);//new
        } else {
            chainCodeDO = updateChainCode(chainCode);//update
        }
        return chainCodeDO;
    }


    /**
     * save new chainCode.
     */
    private ChainCodeDO newChainCode(ReqChainCodeVO chainCode) {
        //check chainCode not exist.
        verifyChainCodePktExist(chainCode.getChannelId(), chainCode.getChainCodeName(),
                chainCode.getChainCodeVersion());

        //add to database.
        ChainCodeDO chainCodeDO = new ChainCodeDO();
        BeanUtils.copyProperties(chainCode, chainCodeDO);
        contractMapper.add(chainCodeDO);
        return chainCodeDO;
    }


    /**
     * update chainCode.
     */
    private ChainCodeDO updateChainCode(ReqChainCodeVO chainCode) {
        //check not deploy
        ChainCodeDO chainCodeDO = verifyChainCodePktDeploy(chainCode.getChainCodePk(),
                chainCode.getChannelId());
        //check chainCodeName
        verifyChainCodeNameNotExist(chainCode.getChannelId(), chainCode.getChainCodeVersion(),
                chainCode.getChainCodeName(), chainCode.getChainCodePk());
        BeanUtils.copyProperties(chainCode, chainCodeDO);
        contractMapper.update(chainCodeDO);
        return chainCodeDO;
    }


    /**
     * delete chainCode by chainCodePk.
     */
    public void deleteChainCode(Integer chainCodePk, int channelId) throws NodeMgrException {
        log.debug("start deleteChainCode chainCodePk:{} channelId:{}", chainCodePk, channelId);
        // check chainCode id
        verifyChainCodePktDeploy(chainCodePk, channelId);
        //remove
        contractMapper.remove(chainCodePk);
        log.debug("end deleteChainCode");
    }

    /**
     * query chainCode list.
     */
    public List<ChainCodeDO> queryChainCodeList(ChainCodeParam param) throws NodeMgrException {
        log.debug("start queryChainCodeList ChainCodeListParam:{}", JSON.toJSONString(param));

        // query chainCode list
        List<ChainCodeDO> listOfChainCode = contractMapper.listOfChainCode(param);

        log.debug("end queryChainCodeList listOfChainCode:{}", JSON.toJSONString(listOfChainCode));
        return listOfChainCode;
    }


    /**
     * query count of chainCode.
     */
    public int countOfChainCode(ChainCodeParam param) throws NodeMgrException {
        log.debug("start countOfChainCode ChainCodeListParam:{}", JSON.toJSONString(param));
        try {
            return contractMapper.countOfChainCode(param);
        } catch (RuntimeException ex) {
            log.error("fail countOfChainCode", ex);
            throw new NodeMgrException(ConstantCode.DB_EXCEPTION);
        }
    }

    /**
     * query chainCode by chainCodePk.
     */
    public ChainCodeDO queryByChainCodePk(Integer chainCodePk) throws NodeMgrException {
        log.debug("start queryChainCode chainCodePk:{}", chainCodePk);
        try {
            ChainCodeDO contractRow = contractMapper.queryByChainCodePk(chainCodePk);
            log.debug("start queryChainCode chainCodePk:{} contractRow:{}", chainCodePk,
                    JSON.toJSONString(contractRow));
            return contractRow;
        } catch (RuntimeException ex) {
            log.error("fail countOfChainCode", ex);
            throw new NodeMgrException(ConstantCode.DB_EXCEPTION);
        }

    }


    /**
     * deploy chainCode.
     */
    public ChainCodeDO deployChainCode(DeployInputParam inputParam) throws NodeMgrException {
        log.info("start deployChainCode. inputParam:{}", JSON.toJSONString(inputParam));
        int channelId = inputParam.getChannelId();
        //check chainCode
        verifyChainCodePktDeploy(inputParam.getChainCodePk(), channelId);
        //check contractName
        verifyChainCodeNameNotExist(inputParam.getChannelId(), inputParam.getChainCodeVersion(),
                inputParam.getChainCodeName(), inputParam.getChainCodePk());

        // deploy param
        Map<String, Object> params = new HashMap<>();
        params.put("channelName", channelId);
        params.put("chainCodeName", inputParam.getChainCodeName());
        params.put("chainCodeLang", inputParam.getChainCodeLang());
        params.put("chainCodeSource", inputParam.getChainCodeSource());
        params.put("version", inputParam.getChainCodeVersion());
        params.put("initParams", inputParam.getInitParams());



        //deploy
        String chainCodeId = frontRestManager.deployChainCode(channelId, params);
        if (StringUtils.isBlank(chainCodeId)) {
            log.error("fail deploy, chainCodeId is empty");
            throw new NodeMgrException(ConstantCode.CHAIN_CODE_DEPLOY_FAIL);
        }

        //save chainCode
        ChainCodeDO chainCodeDO = new ChainCodeDO();
        BeanUtils.copyProperties(inputParam, chainCodeDO);
        chainCodeDO.setChainCodeId(chainCodeId);
        chainCodeDO.setChainCodeStatus(ChainCodeStatus.DEPLOYED.getValue());
        chainCodeDO.setChainCodeVersion(inputParam.getChainCodeVersion());
        chainCodeDO.setDeployTime(LocalDateTime.now());
        contractMapper.update(chainCodeDO);

        log.debug("end deployChainCode. chainCodePk:{} channelId:{} chainCodeId:{}",
                chainCodeDO.getChainCodePk(), channelId, chainCodeId);
        return chainCodeDO;
    }

    /**
     * query chainCode info.
     */
    public ChainCodeDO queryChainCode(ChainCodeParam queryParam) {
        log.debug("start queryChainCode. queryParam:{}", JSON.toJSONString(queryParam));
        ChainCodeDO chainCodeDO = contractMapper.queryChainCode(queryParam);
        log.debug("end queryChainCode. queryParam:{} chainCodeDO:{}", JSON.toJSONString(queryParam),
                JSON.toJSONString(chainCodeDO));
        return chainCodeDO;
    }


    /**
     * send transaction.
     */
    public Object sendTransaction(TransactionInputParam param) throws NodeMgrException {
        log.debug("start sendTransaction. param:{}", JSON.toJSONString(param));

        //check chainCodePk
        verifyChainCodeIdExist(param.getChainCodePk(), param.getChannelId());
        //check chainCode deploy
        verifyChainCodeDeploy(param.getChainCodePk(), param.getChannelId());

        //send transaction
        TransactionParam transParam = new TransactionParam();
        BeanUtils.copyProperties(param, transParam);

        Object frontRsp = frontRestManager.sendTransaction(param.getChannelId(), transParam);
        log.debug("end sendTransaction. frontRsp:{}", JSON.toJSONString(frontRsp));
        return frontRsp;
    }


    /**
     * verify that the chainCode does not exist.
     */
    private void verifyChainCodePktExist(Integer channelId, String name, String version) {
        ChainCodeParam param = ChainCodeParam.builder()
                .channelId(channelId)
                .chainCodeName(name)
                .chainCodeVersion(version).build();
        ChainCodeDO chainCode = queryChainCode(param);
        if (Objects.nonNull(chainCode)) {
            log.warn("chainCode is exist. channelId:{} name:{} version:{}", channelId, name, version);
            throw new NodeMgrException(ConstantCode.CHAIN_CODE_EXISTS);
        }
    }

    /**
     * verify that the chainCode had not deployed.
     */
    private ChainCodeDO verifyChainCodePktDeploy(int chainCodePk, int channelId) {
        ChainCodeDO chainCode = verifyChainCodeIdExist(chainCodePk, channelId);
        if (ChainCodeStatus.DEPLOYED.getValue() == chainCode.getChainCodeStatus()) {
            log.info("chainCode had bean deployed chainCodePk:{}", chainCodePk);
            throw new NodeMgrException(ConstantCode.CHAIN_CODE_HAS_BEAN_DEPLOYED);
        }
        return chainCode;
    }

    /**
     * verify that the chainCode had bean deployed.
     */
    private ChainCodeDO verifyChainCodeDeploy(int chainCodePk, int channelId) {
        ChainCodeDO chainCode = verifyChainCodeIdExist(chainCodePk, channelId);
        if (ChainCodeStatus.DEPLOYED.getValue() != chainCode.getChainCodeStatus()) {
            log.info("chainCode had not deployed chainCodePk:{}", chainCodePk);
            throw new NodeMgrException(ConstantCode.CHAIN_CODE_HAS_NOT_DEPLOYED);
        }
        return chainCode;
    }

    /**
     * verify that the chainCodePk is exist.
     */
    private ChainCodeDO verifyChainCodeIdExist(int chainCodePk, int channelId) {
        ChainCodeParam param = ChainCodeParam.builder()
                .chainCodePk(chainCodePk)
                .channelId(channelId)
                .build();
        ChainCodeDO chainCode = queryChainCode(param);
        if (Objects.isNull(chainCode)) {
            log.info("chainCodePk is invalid. chainCodePk:{}", chainCodePk);
            throw new NodeMgrException(ConstantCode.INVALID_CHAIN_CODE_NO);
        }
        return chainCode;
    }

    /**
     * chainCode name can not be repeated.
     */
    private void verifyChainCodeNameNotExist(int channelId, String version, String name, int chainCodePk) {
        ChainCodeParam param = ChainCodeParam.builder()
                .channelId(channelId)
                .chainCodeName(name)
                .chainCodeVersion(version)
                .build();
        ChainCodeDO localChainCode = queryChainCode(param);
        if (Objects.isNull(localChainCode)) {
            return;
        }
        if (chainCodePk != localChainCode.getChainCodePk()) {
            throw new NodeMgrException(ConstantCode.CHAIN_CODE_NAME_REPEAT);
        }
    }

}
