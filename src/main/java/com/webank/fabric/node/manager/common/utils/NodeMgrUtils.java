package com.webank.fabric.node.manager.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.webank.fabric.node.manager.common.exception.NodeMgrException;
import com.webank.fabric.node.manager.common.pojo.base.BaseResponse;
import com.webank.fabric.node.manager.common.pojo.base.ConstantCode;
import com.webank.fabric.node.manager.common.pojo.base.RetCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.security.MessageDigest;
import java.util.Objects;
import java.util.Random;

/**
 * common method.
 */
@Slf4j
public class NodeMgrUtils {
    private static final String TOKEN_HEADER_NAME = "Authorization";
    private static final String TOKEN_START = "Token";
    private static final char[] CHARS = {'2', '3', '4', '5', '6', '7', '8', '9', 'a',
            'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's',
            't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J',
            'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};


    /**
     * 获取指定位数的数字和字母组合的字符串
     *
     * @param length 字符串长度
     */
    public static String randomString(int length) {
        if (length > CHARS.length) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(CHARS[random.nextInt(CHARS.length)]);
        }
        return sb.toString();
    }


    /**
     * check host an port.
     */
    public static void checkServerConnect(String serverHost, int serverPort) {
        // check host
        // checkServerHostConnect(serverHost);

        Socket socket = null;
        try {
            //check port
            socket = new Socket();
            socket.setReceiveBufferSize(8193);
            socket.setSoTimeout(500);
            SocketAddress address = new InetSocketAddress(serverHost, serverPort);
            socket.connect(address, 1000);
        } catch (Exception ex) {
            log.error("fail checkServerConnect", ex);
            throw new NodeMgrException(ConstantCode.SERVER_CONNECT_FAIL);
        } finally {
            if (Objects.nonNull(socket)) {
                try {
                    socket.close();
                } catch (IOException e) {
                    log.error("fail close socket", e);
                }
            }
        }
    }

    /**
     * convert object to java bean.
     */
    public static <T> T object2JavaBean(Object obj, Class<T> clazz) {
        if (obj == null || clazz == null) {
            log.warn("object2JavaBean. obj or clazz null");
            return null;
        }
        String jsonStr = JSON.toJSONString(obj);

        return JSON.parseObject(jsonStr, clazz);
    }

    /**
     * encode String by sha.
     */
    public static String shaEncode(String inStr) {

        byte[] byteArray = new byte[0];
        try {
            byteArray = inStr.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.warn("shaEncode fail:", e);
            return null;
        }
        byte[] hashValue = getHashValue(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < hashValue.length; i++) {
            int val = ((int) hashValue[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    /**
     * get hash value
     * type: sha256 or sm3
     */
    public static byte[] getHashValue(byte[] byteArray) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] hashResult = sha.digest(byteArray);
            return hashResult;
        } catch (Exception e) {
            log.error("shaEncode getHashValue fail:", e);
            return null;
        }
    }

    /**
     * get token.
     */
    public static synchronized String getToken(HttpServletRequest request) {
        String header = request.getHeader(TOKEN_HEADER_NAME);
        if (StringUtils.isBlank(header)) {
            log.error("not found token");
            throw new NodeMgrException(ConstantCode.INVALID_TOKEN);
        }

        String token = StringUtils.removeStart(header, TOKEN_START).trim();
        if (StringUtils.isBlank(token)) {
            log.error("token is empty");
            throw new NodeMgrException(ConstantCode.INVALID_TOKEN);
        }
        return token;
    }

    /**
     * response string.
     */
    public static void responseString(HttpServletResponse response, String str) {
        BaseResponse baseResponse = new BaseResponse(ConstantCode.SYSTEM_EXCEPTION);
        if (StringUtils.isNotBlank(str)) {
            baseResponse.setMessage(str);
        }

        RetCode retCode;
        if (isJSON(str) && (retCode = JSONObject.parseObject(str, RetCode.class)) != null) {
            baseResponse = new BaseResponse(retCode);
        }

        try {
            response.getWriter().write(JSON.toJSONString(baseResponse));
        } catch (IOException e) {
            log.error("fail responseRetCodeException", e);
        }
    }

    /**
     * is json.
     */
    public static boolean isJSON(String str) {
        boolean result;
        try {
            JSON.parse(str);
            result = true;
        } catch (Exception e) {
            result = false;
        }
        return result;
    }
}
