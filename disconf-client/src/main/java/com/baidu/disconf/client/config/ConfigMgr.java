package com.baidu.disconf.client.config;

import com.baidu.disconf.client.config.inner.DisInnerConfigHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 配置模块
 *
 * @author liaoqiqi
 * @version 2014-6-6
 */
public class ConfigMgr {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ConfigMgr.class);

    private static boolean isInit = false;

    /**
     * 初始化配置
     *
     * @throws Exception
     */
    public synchronized static void init() throws Exception {

        LOGGER.info("--------------- LOAD CONFIG START ---------------");

        // 在使用时才初始化指纹，保证在Zookeeper链接初始化之后进行指纹获取操作。
        //LOGGER.info("Finer print: " + DisClientComConfig.getInstance().getInstanceFingerprint());

        // 导入系统配置
        DisClientSysConfig.getInstance().loadConfig(null);

        // 校验 系统配置
        DisInnerConfigHelper.verifySysConfig();

        // 导入用户配置
        DisClientConfig.getInstance().loadConfig(null);

        // 校验 用户配置
        DisInnerConfigHelper.verifyUserConfig();

        isInit = true;

        LOGGER.info("--------------- LOAD CONFIG END ---------------");
    }

    /**
     */
    public synchronized static boolean isInit() {
        return isInit;
    }

    /**
     */
    public static void main(String[] args) {

        try {

            ConfigMgr.init();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

}
