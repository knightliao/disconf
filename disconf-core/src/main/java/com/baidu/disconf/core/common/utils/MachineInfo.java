package com.baidu.disconf.core.common.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * MachineInfo
 *
 * @author liaoqiqi
 * @version 2014-7-30
 */
public final class MachineInfo {

    private MachineInfo() {

    }

    /**
     * @return
     *
     * @Description: 获取机器名
     */
    public static String getHostName() throws Exception {

        try {
            InetAddress addr = InetAddress.getLocalHost();
            String hostname = addr.getHostName();

            return hostname;

        } catch (UnknownHostException e) {

            throw new Exception(e);
        }
    }

    /**
     * @return
     *
     * @Description: 获取机器名
     */
    public static String getHostIp() throws Exception {

        try {
            InetAddress addr = InetAddress.getLocalHost();
            String ip = addr.getHostAddress();

            return ip;

        } catch (UnknownHostException e) {

            throw new Exception(e);
        }
    }

}
