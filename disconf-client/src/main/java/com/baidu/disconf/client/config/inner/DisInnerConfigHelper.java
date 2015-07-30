package com.baidu.disconf.client.config.inner;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.client.config.DisClientConfig;
import com.baidu.disconf.client.config.DisClientSysConfig;
import com.github.knightliao.apollo.utils.common.StringUtil;
import com.github.knightliao.apollo.utils.config.ConfigLoaderUtils;
import com.github.knightliao.apollo.utils.io.OsUtil;

/**
 * 用户配置、系统配置 的校验
 *
 * @author liaoqiqi
 * @version 2014-6-6
 */
public class DisInnerConfigHelper {

    protected static final Logger LOGGER = LoggerFactory.getLogger(DisInnerConfigHelper.class);

    /**
     * @throws Exception
     * @Description: 校验用户配置文件是否正常
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
        DisClientConfig.getInstance()
            .setHostList(StringUtil.parseStringToStringList(DisClientConfig.getInstance().CONF_SERVER_HOST, ","));
        LOGGER.info("SERVER conf_server_host: " + DisClientConfig.getInstance().getHostList());

        //
        // version
        String version = System.getProperty(DisClientConfig.VERSION_NAME);
        if (version == null) {
            // 版本
            if (StringUtils.isEmpty(DisClientConfig.getInstance().VERSION)) {

                throw new Exception("settings: VERSION cannot find");
            }
        } else {
            DisClientConfig.getInstance().VERSION = version;
        }
        LOGGER.info("SERVER version: " + DisClientConfig.getInstance().VERSION);

        //
        // APP名
        String app = System.getProperty(DisClientConfig.APP_NAME);
        if (app == null) {
            if (StringUtils.isEmpty(DisClientConfig.getInstance().APP)) {

                throw new Exception("settings: APP cannot find");
            }
        } else {
            DisClientConfig.getInstance().APP = app;
        }
        LOGGER.info("SERVER APP: " + DisClientConfig.getInstance().APP);

        //
        // 环境
        String env = System.getProperty(DisClientConfig.ENV_NAME);
        if (env == null) {
            if (StringUtils.isEmpty(DisClientConfig.getInstance().ENV)) {

                throw new Exception("settings: ENV cannot find");
            }
        } else {
            DisClientConfig.getInstance().ENV = env;
        }
        LOGGER.info("SERVER ENV: " + DisClientConfig.getInstance().ENV);

        //
        // 是否使用远程的配置
        LOGGER.info("SERVER ENABLE_REMOTE_CONF: " + DisClientConfig.getInstance().ENABLE_DISCONF);

        //
        // debug mode
        LOGGER.info("SERVER DEBUG MODE: " + DisClientConfig.getInstance().DEBUG);

        // 用户下载文件夹
        if (StringUtils.isEmpty(DisClientConfig.getInstance().USER_DEFINE_DOWNLOAD_DIR)) {
            OsUtil.makeDirs(DisClientConfig.getInstance().USER_DEFINE_DOWNLOAD_DIR);
            LOGGER.info("SERVER USER DEFINE DOWNLOAD DIR: " + DisClientConfig.getInstance().USER_DEFINE_DOWNLOAD_DIR);
        }

        //
        // 忽略哪些分布式配置
        //
        List<String> ignoreDisconfList =
            StringUtil.parseStringToStringList(DisClientConfig.getInstance().IGNORE_DISCONF_LIST, ",");
        Set<String> keySet = new HashSet<String>();
        if (ignoreDisconfList != null) {
            for (String ignoreData : ignoreDisconfList) {
                keySet.add(ignoreData.trim());
            }
        }
        DisClientConfig.getInstance().setIgnoreDisconfKeySet(keySet);
        LOGGER.info("SERVER IGNORE_DISCONF_LIST: " + DisClientConfig.getInstance().getIgnoreDisconfKeySet());

        // 重试
        LOGGER
            .debug("SERVER CONF_SERVER_URL_RETRY_TIMES: " + DisClientConfig.getInstance().CONF_SERVER_URL_RETRY_TIMES);

        LOGGER.debug("SERVER CONF_SERVER_URL_RETRY_SLEEP_SECONDS: " +
                         DisClientConfig.getInstance().CONF_SERVER_URL_RETRY_SLEEP_SECONDS);
    }

    /**
     * @throws Exception
     * @Description: 校验系统配置文件是否正常
     * @date 2013-6-13
     */
    public static void verifySysConfig() throws Exception {

        //
        // 服务器相关
        //

        // CONF_SERVER_STORE_ACTION
        if (StringUtils.isEmpty(DisClientSysConfig.getInstance().CONF_SERVER_STORE_ACTION)) {

            throw new Exception("settings: CONF_SERVER_STORE_ACTION cannot find");
        }
        LOGGER.debug("SERVER CONF_SERVER_STORE_ACTION: " + DisClientSysConfig.getInstance().CONF_SERVER_STORE_ACTION);

        // CONF_SERVER_ZOO_ACTION
        if (StringUtils.isEmpty(DisClientSysConfig.getInstance().CONF_SERVER_ZOO_ACTION)) {

            throw new Exception("settings: CONF_SERVER_ZOO_ACTION cannot find");
        }
        LOGGER.debug("SERVER CONF_SERVER_ZOO_ACTION: " + DisClientSysConfig.getInstance().CONF_SERVER_ZOO_ACTION);

        // CONF_SERVER_MASTER_NUM_ACTION
        if (StringUtils.isEmpty(DisClientSysConfig.getInstance().CONF_SERVER_MASTER_NUM_ACTION)) {

            throw new Exception("settings: CONF_SERVER_MASTER_NUM_ACTION  cannot find");
        }
        LOGGER.debug("SERVER CONF_SERVER_MASTER_NUM_ACTION Action URL: " +
                         DisClientSysConfig.getInstance().CONF_SERVER_MASTER_NUM_ACTION);

        //
        // 本地相关
        //

        if (StringUtils.isEmpty(DisClientSysConfig.getInstance().LOCAL_DOWNLOAD_DIR)) {
            throw new Exception("settings: LOCAL_TMP_DIR cannot find");
        }

        // 是否将文件放在classpath目录下
        if (DisClientSysConfig.getInstance().ENABLE_LOCAL_DOWNLOAD_DIR_IN_CLASS_PATH) {

            String classpath = ConfigLoaderUtils.CLASS_PATH;

            if (classpath.isEmpty()) {
                LOGGER.warn("CLASSPATH is null. we will not transfer your config file to classpath in the following");
            } else {
                LOGGER.debug("classpath: " + classpath);
            }
        }

        // LOCAL_DOWNLOAD_DIR
        LOGGER.debug("SERVER LOCAL_DOWNLOAD_DIR: " + DisClientSysConfig.getInstance().LOCAL_DOWNLOAD_DIR);
        OsUtil.makeDirs(DisClientSysConfig.getInstance().LOCAL_DOWNLOAD_DIR);
    }

}
