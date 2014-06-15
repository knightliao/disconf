package com.baidu.disconf2.core.common.utils;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public static String getZooDirValue() {

        try {
            return DateUtils.formatDate(new Date(), "yyyyMMddHHmmss") + "\t"
                    + MachineInfo.getHostName() + "\t"
                    + MachineInfo.getHostIp();
        } catch (Exception e) {
            LOGGER.error("cannot get host info", e);
            return "";
        }
    }

    /**
     * 一个可读性良好的路径Value
     * 
     * @return
     */
    public static String getZooDirValueByDate() {

        try {
            return DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
        } catch (Exception e) {
            LOGGER.error("cannot get host info", e);
            return "";
        }
    }

}
