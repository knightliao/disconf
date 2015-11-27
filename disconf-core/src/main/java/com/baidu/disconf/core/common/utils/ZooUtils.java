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

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(ZooUtils.class);

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

}
