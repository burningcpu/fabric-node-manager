package com.webank.fabric.node.manager.common.utils;

import com.webank.fabric.node.manager.common.exception.NodeMgrException;
import com.webank.fabric.node.manager.common.pojo.base.ConstantCode;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Objects;

/**
 * common method.
 */
@Slf4j
public class NodeMgrUtils {
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
}
