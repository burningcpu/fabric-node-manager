package com.webank.fabric.node.manager.api.front;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.InvalidProtocolBufferException;
import com.webank.fabric.node.manager.api.channel.FrontChannelService;
import com.webank.fabric.node.manager.common.exception.NodeMgrException;
import com.webank.fabric.node.manager.common.pojo.base.ConstantCode;
import com.webank.fabric.node.manager.common.pojo.channel.FrontChannelUnionDO;
import com.webank.fabric.node.manager.common.pojo.front.FrontProperties;
import com.webank.fabric.node.manager.common.pojo.peer.PeerDTO;
import com.webank.fabric.node.manager.common.utils.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hyperledger.fabric.protos.common.Common;
import org.hyperledger.fabric.sdk.BlockInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.*;


/**
 * interface of frontInfo in db.
 */
@Slf4j
@Repository
public class FrontRestManager {
    public static final String URI_GET_CHANNEL_NAME = "sdk/channelName";
    public static final String URI_GET_PEER_LIST = "sdk/peers";
    public static final String URI_GET_PEER_BLOCK_NUMBER = "sdk/peerBlockNumber?peerUrl=%1s";
    public static final String URI_GET_CHANNEL_BLOCK_NUMBER = "sdk/channelBlockNumber";
    public static final String URI_BLOCK_BY_NUMBER = "sdk/queryBlockByNumber/%1d";
    public static final String URI_BLOCK_BY_HASH = "sdk/queryBlockByHash/%1s";
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private FrontProperties frontProperties;
    @Autowired
    private FrontChannelService frontChannelService;


//------------------------------------------------------------------
//------------------------public method-----------------------------
//------------------------------------------------------------------

    /**
     * get channelName from specific front.
     */
    public String getChannelNameFromSpecificFront(String frontIp, Integer frontPort) {
        return getFromSpecificFront(frontIp, frontPort, URI_GET_CHANNEL_NAME, String.class);
    }

    /**
     * get channelName from specific front.
     */
    public PeerDTO[] getPeersFromSpecificFront(String frontIp, Integer frontPort) {
        return getFromSpecificFront(frontIp, frontPort, URI_GET_PEER_LIST, PeerDTO[].class);
    }


    /**
     * get blockHeight from specific front.
     */
    public BigInteger getBlockNumberFromSpecificFront(String nodeIp, Integer frontPort, String peerUrl) {
        String uri = String.format(URI_GET_PEER_BLOCK_NUMBER, peerUrl);
        return getFromSpecificFront(nodeIp, frontPort, uri, BigInteger.class);
    }

    /**
     * get blockHeight from specific front.
     */
    public BigInteger getChannelBlockNumberFromSpecificFront(String nodeIp, Integer frontPort, String peerUrl) {
        String uri = String.format(URI_GET_CHANNEL_BLOCK_NUMBER, peerUrl);
        return getFromSpecificFront(nodeIp, frontPort, uri, BigInteger.class);
    }


    /**
     * get latest blockNumber of channel.
     */
    public BigInteger getLatestChannelBlockNumber(int channelId) {
        log.debug("start getLatestBlockNumber. channelId:{}", channelId);
        BigInteger latestBlockNumber = getForEntity(channelId, URI_GET_CHANNEL_BLOCK_NUMBER, BigInteger.class);
        log.debug("end getLatestBlockNumber. latestBlockNumber:{}", latestBlockNumber);
        return latestBlockNumber;
    }


    /**
     * get transaction.
     */
    public BlockInfo getBlockByNumber(int channelId, BigInteger blockNumber) throws InvalidProtocolBufferException {
        log.debug("start getBlockByNumber. channelId:{} blockNumber:{} ", channelId, blockNumber);
        String uri = String.format(URI_BLOCK_BY_NUMBER, blockNumber);
        byte[] blockByteArray = getForEntity(channelId, uri, byte[].class);
        BlockInfo blockInfo = blockInfoFromByteArray(blockByteArray);
        log.debug("end getBlockByNumber.");
        return blockInfo;
    }


    /**
     * get transaction.
     */
    public BlockInfo getBlockByHash(int channelId, String blockHash) throws InvalidProtocolBufferException {
        log.debug("start getBlockByHash. channelId:{}  blockHash:{}", channelId, blockHash);
        String uri = String.format(URI_BLOCK_BY_HASH, blockHash);
        byte[] blockByteArray = getForEntity(channelId, uri, byte[].class);
        BlockInfo blockInfo = blockInfoFromByteArray(blockByteArray);
        log.debug("end getBlockByHash.");
        return blockInfo;
    }

    /**
     * convert byteArray to blockInfo.
     */
    private BlockInfo blockInfoFromByteArray(byte[] blockByteArray) throws InvalidProtocolBufferException {
        if (null == blockByteArray)
            return null;

        Common.Block block = Common.Block.parseFrom(blockByteArray);
        return BeanUtils.getInstanceByReflection(BlockInfo.class, Arrays.asList(block));
    }


//------------------------------------------------------------------
//-----------------------private method-----------------------------
//------------------------------------------------------------------

    /**
     * request from specific front.
     */
    private <T> T requestSpecificFront(String frontIp, Integer frontPort,
                                       HttpMethod method, String uri, Object param, Class<T> clazz) {
        log.debug("start requestSpecificFront.  frontIp:{} frontPort:{} httpMethod:{} uri:{}", frontIp, frontPort, method.toString(), uri);

        String url = String.format(frontProperties.getServerAddress(), frontIp, frontPort, uri);
        log.debug("requestSpecificFront. url:{}", url);

        try {
            HttpEntity entity = buildHttpEntity(param);// build entity
            ResponseEntity<T> response = restTemplate.exchange(url, method, entity, clazz);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            JSONObject error = JSONObject.parseObject(e.getResponseBodyAsString());
            log.error("http request fail. error:{}", JSON.toJSONString(error));
            if (error.containsKey("code") && error.containsKey("errorMessage")) {
                throw new NodeMgrException(error.getInteger("code"),
                        error.getString("errorMessage"));
            }
            throw new NodeMgrException(ConstantCode.REQUEST_FRONT_FAIL);
        }
    }


    /**
     * get from specific front.
     */
    private <T> T getFromSpecificFront(String frontIp, Integer frontPort, String uri,
                                       Class<T> clazz) {
        log.debug("start getFromSpecificFront.  frontIp:{} frontPort:{}  uri:{}", frontIp, frontPort.toString(), uri);
        String url = String.format(frontProperties.getServerAddress(), frontIp, frontPort, uri);
        log.debug("getFromSpecificFront. url:{}", url);
        return requestSpecificFront(frontIp, frontPort, HttpMethod.GET, uri, null, clazz);
    }


    /**
     * get from front for entity.
     */
    private <T> T getForEntity(Integer channelId, String uri, Class<T> clazz) {
        return restTemplateExchange(channelId, uri, HttpMethod.GET, null, clazz);
    }

    /**
     * post from front for entity.
     */
    private <T> T postForEntity(Integer channelId, String uri, Object params, Class<T> clazz) {
        return restTemplateExchange(channelId, uri, HttpMethod.POST, params, clazz);
    }

    /**
     * delete from front for entity.
     */
    private <T> T deleteForEntity(Integer channelId, String uri, Object params, Class<T> clazz) {
        return restTemplateExchange(channelId, uri, HttpMethod.DELETE, params, clazz);
    }


    /**
     * append channelId to uri.
     */
    private static String uriAddChannelName(String channelName, String uri) {
        if (StringUtils.isBlank(channelName) || StringUtils.isBlank(uri)) {
            return null;
        }

        return channelName + "/" + uri;
    }


    /**
     * build  url of front service.
     */
    private String buildFrontUrl(ArrayList<FrontChannelUnionDO> list, String uri, HttpMethod httpMethod) {
        Collections.shuffle(list);//random one
        Iterator<FrontChannelUnionDO> iterator = list.iterator();
        while (iterator.hasNext()) {
            FrontChannelUnionDO frontChannelDO = iterator.next();
            //uri = uriAddChannelName(frontChannelDO.getChannelName(), uri);//append channelId to uri
            String url = String
                    .format(frontProperties.getServerAddress(), frontChannelDO.getFrontIp(),
                            frontChannelDO.getFrontPort(), uri)
                    .replaceAll(" ", "");
            iterator.remove();

            return url;
        }
        log.info("end buildFrontUrl. url is null");
        return null;
    }

    /**
     * build httpEntity
     */
    private HttpEntity buildHttpEntity(Object param) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String paramStr = null;
        if (Objects.nonNull(param)) {
            paramStr = JSON.toJSONString(param);
        }
        HttpEntity requestEntity = new HttpEntity(paramStr, headers);
        return requestEntity;
    }


    /**
     * restTemplate exchange.
     */
    private <T> T restTemplateExchange(int channelId, String uri, HttpMethod method,
                                       Object param, Class<T> clazz) {
        List<FrontChannelUnionDO> frontList = frontChannelService.getMapListByChannelId(channelId);
        if (frontList == null || frontList.size() == 0) {
            log.error("fail restTemplateExchange. frontList is empty");
            throw new NodeMgrException(ConstantCode.FRONT_LIST_NOT_FOUNT);
        }
        ArrayList<FrontChannelUnionDO> list = new ArrayList<>(frontList);

        while (list != null && list.size() > 0) {
            String url = buildFrontUrl(list, uri, method);//build url
            try {
                HttpEntity entity = buildHttpEntity(param);// build entity
                if (null == restTemplate) {
                    log.error("fail restTemplateExchange, rest is null. channelId:{} uri:{}", channelId, uri);
                    throw new NodeMgrException(ConstantCode.SYSTEM_EXCEPTION);
                }
                ResponseEntity<T> response = restTemplate.exchange(url, method, entity, clazz);
                return response.getBody();
            } catch (ResourceAccessException ex) {
                log.warn("fail restTemplateExchange,try next front", ex);
                continue;
            } catch (HttpStatusCodeException e) {
                JSONObject error = JSONObject.parseObject(e.getResponseBodyAsString());
                log.error("http request fail. error:{}", JSON.toJSONString(error));
                if (error.containsKey("code") && error.containsKey("errorMessage")) {
                    throw new NodeMgrException(error.getInteger("code"),
                            error.getString("errorMessage"));
                }
                throw new NodeMgrException(ConstantCode.REQUEST_FRONT_FAIL);
            }
        }
        return null;
    }
}
