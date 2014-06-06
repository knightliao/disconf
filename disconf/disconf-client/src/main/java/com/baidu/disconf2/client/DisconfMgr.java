package com.baidu.disconf2.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf2.client.config.ConfigMgr;

/**
 * 
 * 
 * 配置服务统一管理器
 * 
 * @author liaoqiqi
 * @version 2014-5-23
 */
public class DisconfMgr {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(DisconfMgr.class);

    // 本实例不能初始化两次
    private static boolean isInit = false;

    /**
     * 
     * @param scanPackage
     */
    public static void run(String scanPackage) {

        // 该函数不能调用两次
        if (isInit == true) {
            LOGGER.info("DisConfMgr has been init, ignore........");
            return;
        }

        LOGGER.info("================================= DISCONF START ======================================");

        try {

            // 导入配置
            ConfigMgr.init();

        } catch (Exception e) {

            LOGGER.error(e.toString());
        }

        LOGGER.info("================================= DISCONF END ======================================");
    }
}
