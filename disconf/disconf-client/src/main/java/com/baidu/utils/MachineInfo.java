package com.baidu.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 机器相关数据获取
 * 
 * @author liaoqiqi
 * @version 2014-6-1
 */
public final class MachineInfo {

    private MachineInfo() {

    }

    /**
     * 
     * @Description: 获取机器名
     * @return
     */
    public static String getHostName() throws Exception {

        try {
            InetAddress addr = InetAddress.getLocalHost();
            String hostname = addr.getHostName();

            return hostname;

        } catch (UnknownHostException e) {

            throw new Exception();
        }
    }

    /**
     * 
     * @Description: 获取机器IP
     * @return
     */
    public static String getHostIp() throws Exception {

        try {

            InetAddress addr = InetAddress.getLocalHost();
            String ip = addr.getHostAddress();

            return ip;

        } catch (UnknownHostException e) {

            throw new Exception();
        }
    }

}
