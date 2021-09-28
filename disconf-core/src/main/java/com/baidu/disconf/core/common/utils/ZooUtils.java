package com.baidu.disconf.core.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ZooUtils
 *
 * @author liaoqiqi
 * @version 2014-7-30
 */
public final class ZooUtils {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ZooUtils.class);

    private ZooUtils() {

    }

    /**
     * 一个可读性良好的路径Value
     *
     * @return
     */
    public static String getIp() {

        try {
            return MachineInfo.getHostIp();
        } catch (Exception e) {
            LOGGER.error("cannot get host info", e);
            return "";
        }
    }

    /**
     * 获取非127.xxx 、localhost、0.0.0.0 的Host地址,力求注入一个有效可区分的地址
     *
     * @since 1.0 Created by lipangeng on 2017/5/23 上午10:58. Email:lipg@outlook.com.
     */
    public static String getNonLocalIp(String host) {
        try {
            if (! MachineInfo.isInvalidLocal(host)) {
                return host;
            }
            return MachineInfo.getNonLocalHostIp();
        } catch (Exception e) {
            LOGGER.error("cannot get host info", e);
            return "";
        }
    }

}
