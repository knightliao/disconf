package com.baidu.disconf.client.config.inner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.client.config.DisClientConfig;
import com.baidu.disconf.client.config.DisClientSysConfig;
import com.baidu.ub.common.utils.ConfigLoaderUtils;
import com.baidu.ub.common.utils.OsUtil;
import com.baidu.ub.common.utils.StringUtils;

/**
 * 用户配置、系统配置 的校验
 * 
 * @author liaoqiqi
 * @version 2014-6-6
 */
public class DisInnerConfigHelper {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(DisInnerConfigHelper.class);

    /**
     * 
     * @Description: 校验用户配置文件是否正常
     * 
     * @throws Exception
     * @return void
     * @author liaoqiqi
     * @date 2013-6-13
     */
    public static void verifyUserConfig() throws Exception {

        //
        // 服务器相关
        //

        //
        // 服务器地址
        if (StringUtils.isEmpty(DisClientConfig.getInstance().CONF_SERVER_HOST)) {

            throw new Exception("settings: CONF_SERVER_HOST cannot find");
        }
        DisClientConfig.getInstance().setHostList(
                StringUtils.parseStringToStringList(
                        DisClientConfig.getInstance().CONF_SERVER_HOST, ","));
        LOGGER.info("SERVER conf_server_host: "
                + DisClientConfig.getInstance().getHostList());

        //
        // 版本
        if (StringUtils.isEmpty(DisClientConfig.getInstance().VERSION)) {

            throw new Exception("settings: VERSION cannot find");
        }
        LOGGER.info("SERVER version: " + DisClientConfig.getInstance().VERSION);

        //
        // APP名
        if (StringUtils.isEmpty(DisClientConfig.getInstance().APP)) {

            throw new Exception("settings: APP cannot find");
        }
        LOGGER.info("SERVER APP: " + DisClientConfig.getInstance().APP);

        //
        // 环境
        if (StringUtils.isEmpty(DisClientConfig.getInstance().ENV)) {

            throw new Exception("settings: ENV cannot find");
        }
        LOGGER.info("SERVER ENV: " + DisClientConfig.getInstance().ENV);

        //
        // 是否使用远程的配置
        LOGGER.info("SERVER ENABLE_REMOTE_CONF: "
                + DisClientConfig.getInstance().ENABLE_REMOTE_CONF);

    }

    /**
     * 
     * @Description: 校验系统配置文件是否正常
     * 
     * @throws Exception
     * @return void
     * @author liaoqiqi
     * @date 2013-6-13
     */
    public static void verifySysConfig() throws Exception {

        //
        // 服务器相关
        //

        // CONF_SERVER_STORE_ACTION
        if (StringUtils
                .isEmpty(DisClientSysConfig.getInstance().CONF_SERVER_STORE_ACTION)) {

            throw new Exception(
                    "settings: CONF_SERVER_STORE_ACTION cannot find");
        }
        LOGGER.debug("SERVER CONF_SERVER_STORE_ACTION: "
                + DisClientSysConfig.getInstance().CONF_SERVER_STORE_ACTION);

        // CONF_SERVER_ZOO_ACTION
        if (StringUtils
                .isEmpty(DisClientSysConfig.getInstance().CONF_SERVER_ZOO_ACTION)) {

            throw new Exception("settings: CONF_SERVER_ZOO_ACTION cannot find");
        }
        LOGGER.debug("SERVER CONF_SERVER_ZOO_ACTION: "
                + DisClientSysConfig.getInstance().CONF_SERVER_ZOO_ACTION);

        // CONF_SERVER_MASTER_NUM_ACTION
        if (StringUtils
                .isEmpty(DisClientSysConfig.getInstance().CONF_SERVER_MASTER_NUM_ACTION)) {

            throw new Exception(
                    "settings: CONF_SERVER_MASTER_NUM_ACTION  cannot find");
        }
        LOGGER.debug("SERVER CONF_SERVER_MASTER_NUM_ACTION Action URL: "
                + DisClientSysConfig.getInstance().CONF_SERVER_MASTER_NUM_ACTION);

        //
        // 本地相关
        //

        if (StringUtils
                .isEmpty(DisClientSysConfig.getInstance().LOCAL_DOWNLOAD_DIR)) {

            throw new Exception("settings: LOCAL_TMP_DIR cannot find");
        }

        // 是否将文件放在classpath目录下
        if (DisClientSysConfig.getInstance().ENABLE_LOCAL_DOWNLOAD_DIR_IN_CLASS_PATH == true) {

            String classpath = ConfigLoaderUtils.CLASS_PATH;

            if (classpath.isEmpty()) {
                LOGGER.warn("CLASSPATH is null. we will not transfer your config file to classpath in the following");
            } else {
                LOGGER.debug("classpath: " + classpath);
            }
        }

        // LOCAL_DOWNLOAD_DIR
        LOGGER.debug("SERVER LOCAL_DOWNLOAD_DIR: "
                + DisClientSysConfig.getInstance().LOCAL_DOWNLOAD_DIR);
        OsUtil.makeDirs(DisClientSysConfig.getInstance().LOCAL_DOWNLOAD_DIR);

        //
        // zookeeper相关
        //

        // 重试
        LOGGER.debug("SERVER CONF_SERVER_URL_RETRY_TIMES: "
                + DisClientSysConfig.getInstance().CONF_SERVER_URL_RETRY_TIMES);

        LOGGER.debug("SERVER CONF_SERVER_URL_RETRY_SLEEP_SECONDS: "
                + DisClientSysConfig.getInstance().CONF_SERVER_URL_RETRY_SLEEP_SECONDS);

    }

}
