package com.baidu.disconf.core.common.utils;

import com.baidu.disconf.core.common.zookeeper.ZookeeperMgr;
import com.baidu.disconf.core.common.zookeeper.inner.ZKHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;
import java.util.List;
import java.util.regex.Pattern;

/**
 * MachineInfo
 *
 * @author liaoqiqi
 * @version 2014-7-30
 */
public final class MachineInfo {
    private static final Logger logger = LoggerFactory.getLogger(MachineInfo.class);
    /** 127的本地ip判断 */
    private static final Pattern LOCAL_IP_PATTERN = Pattern.compile("127(\\.\\d{1,3}){3}$");

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

    /**
     * 获取非127.xxx 、localhost、0.0.0.0 的ip地址，如果在调用该方法前未初始化
     *
     * @since 1.0 Created by lipangeng on 2017/5/23 上午10:47. Email:lipg@outlook.com.
     */
    public static String getNonLocalHostIp() throws Exception {
        String host = null;
        // 从环境变量获取配置
        try {
            host = System.getProperty("disconf.host");
            if (isInvalidLocal(host)) {
                host = System.getenv("disconf_host");
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        // 如果系统未配置host地址，则使用自动获取ip地址
        try {
            if (isInvalidLocal(host)) {
                host = InetAddress.getLocalHost().getHostAddress();
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        // 如果获取的地址无效
        if (isInvalidLocal(host)) {
            List<ZKHost> zkHostsList = ZookeeperMgr.getInstance().getZKHostsList();
            for (ZKHost zkHost : zkHostsList) {
                try {
                    Socket socket = new Socket();
                    try {
                        SocketAddress addr = new InetSocketAddress(zkHost.getHost(), zkHost.getPort());
                        socket.connect(addr, 1000);
                        host = socket.getLocalAddress().getHostAddress();
                        break;
                    } finally {
                        try {
                            socket.close();
                        } catch (Throwable e) {
                        }
                    }
                } catch (Exception e) {
                    logger.warn(e.getMessage(), e);
                }
            }
        }
        // 获取host之后再次进行判断，保证最终会有一个host注入进去
        if (isInvalidLocal(host)) {
            host = getHostIp();
        }
        return host;
    }

    /**
     * 判断获取的IP或者Host是否为本地的，127.xxx 、localhost、0.0.0.0
     *
     * @since 1.0 Created by lipangeng on 2017/5/23 上午10:43. Email:lipg@outlook.com.
     */
    public static boolean isInvalidLocal(String host) {
        return host == null || host.length() == 0 || host.equalsIgnoreCase("localhost") || host.equals("0.0.0.0") ||
               (LOCAL_IP_PATTERN.matcher(host).matches());
    }
}
