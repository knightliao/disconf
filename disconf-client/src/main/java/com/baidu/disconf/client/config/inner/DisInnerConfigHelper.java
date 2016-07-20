package com.baidu.disconf.client.config.inner;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.client.config.DisClientConfig;
import com.baidu.disconf.client.config.DisClientSysConfig;
import com.baidu.disconf.client.support.utils.StringUtil;
import com.baidu.disconf.core.common.utils.ClassLoaderUtil;
import com.baidu.disconf.core.common.utils.OsUtil;

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

            throw new Exception("settings: " + DisClientConfig.CONF_SERVER_HOST_NAME + " cannot find");
        }

        DisClientConfig.getInstance()
                .setHostList(StringUtil.parseStringToStringList(DisClientConfig.getInstance().CONF_SERVER_HOST, ","));
        LOGGER.info(
                "SERVER " + DisClientConfig.CONF_SERVER_HOST_NAME + ": " + DisClientConfig.getInstance().getHostList());

        //
        // 版本

        if (StringUtils.isEmpty(DisClientConfig.getInstance().VERSION)) {

            throw new Exception("settings: " + DisClientConfig.VERSION_NAME + " cannot find");
        }
        LOGGER.info("SERVER " + DisClientConfig.VERSION_NAME + ": " + DisClientConfig.getInstance().VERSION);

        //
        // APP名

        if (StringUtils.isEmpty(DisClientConfig.getInstance().APP)) {

            throw new Exception("settings: " + DisClientConfig.APP_NAME + " cannot find");
        }
        LOGGER.info("SERVER " + DisClientConfig.APP_NAME + ": " + DisClientConfig.getInstance().APP);

        //
        // 环境

        if (StringUtils.isEmpty(DisClientConfig.getInstance().ENV)) {

            throw new Exception("settings: " + DisClientConfig.ENV_NAME + "  cannot find");
        }
        LOGGER.info("SERVER " + DisClientConfig.ENV_NAME + ": " + DisClientConfig.getInstance().ENV);

        //
        // 是否使用远程的配置
        LOGGER.info("SERVER disconf.enable.remote.conf: " + DisClientConfig.getInstance().ENABLE_DISCONF);

        //
        // debug mode
        LOGGER.info("SERVER disconf.debug: " + DisClientConfig.getInstance().DEBUG);

        // 用户下载文件夹
        if (!StringUtils.isEmpty(DisClientConfig.getInstance().userDefineDownloadDir)) {
            OsUtil.makeDirs(DisClientConfig.getInstance().userDefineDownloadDir);
            LOGGER.info("SERVER disconf.user_define_download_dir: " + DisClientConfig.getInstance()
                    .userDefineDownloadDir);
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
        LOGGER.info("SERVER disconf.ignore: " + DisClientConfig.getInstance().getIgnoreDisconfKeySet());

        // 重试
        LOGGER.debug("SERVER disconf.conf_server_url_retry_times: " + DisClientConfig
                .getInstance().CONF_SERVER_URL_RETRY_TIMES);

        LOGGER.debug("SERVER disconf.conf_server_url_retry_sleep_seconds: " +
                DisClientConfig.getInstance().confServerUrlRetrySleepSeconds);

        LOGGER.debug("SERVER disconf.enable_local_download_dir_in_class_path: " + DisClientConfig
                .getInstance().enableLocalDownloadDirInClassPath);
        // 是否将文件放在classpath目录下
        if (DisClientConfig.getInstance().enableLocalDownloadDirInClassPath) {

            String classpath = ClassLoaderUtil.getClassPath();

            if (classpath.isEmpty()) {
                LOGGER.warn("CLASSPATH is null. we will not transfer your config file to classpath in the following");
            } else {
                LOGGER.debug("classpath: " + classpath);
            }
        }
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
        LOGGER.debug("SERVER disconf.conf_server_store_action: " + DisClientSysConfig
                .getInstance().CONF_SERVER_STORE_ACTION);

        // CONF_SERVER_ZOO_ACTION
        if (StringUtils.isEmpty(DisClientSysConfig.getInstance().CONF_SERVER_ZOO_ACTION)) {

            throw new Exception("settings: CONF_SERVER_ZOO_ACTION cannot find");
        }
        LOGGER.debug(
                "SERVER disconf.conf_server_zoo_action: " + DisClientSysConfig.getInstance().CONF_SERVER_ZOO_ACTION);

        // CONF_SERVER_MASTER_NUM_ACTION
        if (StringUtils.isEmpty(DisClientSysConfig.getInstance().CONF_SERVER_MASTER_NUM_ACTION)) {

            throw new Exception("settings: CONF_SERVER_MASTER_NUM_ACTION  cannot find");
        }
        LOGGER.debug("SERVER disconf.conf_server_master_num_action: " +
                DisClientSysConfig.getInstance().CONF_SERVER_MASTER_NUM_ACTION);

        //
        // 本地相关
        //

        if (StringUtils.isEmpty(DisClientSysConfig.getInstance().LOCAL_DOWNLOAD_DIR)) {
            throw new Exception("settings: LOCAL_TMP_DIR cannot find");
        }

        // LOCAL_DOWNLOAD_DIR
        LOGGER.debug("SERVER disconf.local_download_dir: " + DisClientSysConfig.getInstance().LOCAL_DOWNLOAD_DIR);
        OsUtil.makeDirs(DisClientSysConfig.getInstance().LOCAL_DOWNLOAD_DIR);
    }

}
