package com.baidu.disconf2.client.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf2.client.config.inner.DisClientConfig;
import com.baidu.disconf2.client.config.inner.DisClientSysConfig;
import com.baidu.disconf2.client.config.inner.DisInnerConfigHelper;

/**
 * 管理配置
 * 
 * @author liaoqiqi
 * @version 2014-6-6
 */
public class ConfigMgr {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(ConfigMgr.class);

    /**
     * 初始化配置
     * 
     * @throws Exception
     */
    public static void init() throws Exception {

        LOGGER.info("=== LOAD CONFIG START ===");

        // 导入系统配置
        DisClientSysConfig.getInstance().loadConfig(null);

        // 校验
        DisInnerConfigHelper.verifySysConfig();

        // 导入用户配置
        DisClientConfig.getInstance().loadConfig(null);

        // 校验
        DisInnerConfigHelper.verifyUserConfig();

        LOGGER.info("=== LOAD CONFIG END ===");
    }

    /**
     * 
     * @param args
     */
    public static void main(String[] args) {

        try {

            ConfigMgr.init();

        } catch (Exception e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
