package com.baidu.disconf2.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf2.client.config.ConfigMgr;

/**
 * 
 * Disconf Client 总入口
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

        //
        //
        //

        LOGGER.info("================================= DISCONF START ======================================");

        try {

            // 导入配置
            ConfigMgr.init();

            // 扫描

        } catch (Exception e) {

            LOGGER.error(e.toString());
        }

        LOGGER.info("================================= DISCONF END ======================================");
    }

    /**
     * 
     * @param args
     */
    public static void main(String[] args) {

        try {

            DisconfMgr.run("");

        } catch (Exception e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
