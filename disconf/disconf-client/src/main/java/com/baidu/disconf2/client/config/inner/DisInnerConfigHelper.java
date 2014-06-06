package com.baidu.disconf2.client.config.inner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.utils.ConfigLoaderUtils;
import com.baidu.utils.OsUtil;
import com.baidu.utils.StringNUtils;

/**
 * 配置的校验
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
        // host
        if (StringNUtils
                .isEmpty(DisClientConfig.getInstance().CONF_SERVER_HOST)) {

            throw new Exception("settings: CONF_SERVER_HOST cannot find");
        }
        LOGGER.info("conf_server_host: "
                + DisClientConfig.getInstance().CONF_SERVER_HOST);

        //
        // version
        if (StringNUtils.isEmpty(DisClientConfig.getInstance().VERSION)) {

            throw new Exception("settings: VERSION cannot find");
        }
        LOGGER.info("version: " + DisClientConfig.getInstance().VERSION);

        //
        // app
        if (StringNUtils.isEmpty(DisClientConfig.getInstance().APP)) {

            throw new Exception("settings: APP cannot find");
        }
        LOGGER.info("SERVER APP: " + DisClientConfig.getInstance().APP);

        //
        // env
        if (StringNUtils.isEmpty(DisClientConfig.getInstance().ENV)) {

            throw new Exception("settings: ENV cannot find");
        }
        LOGGER.info("SERVER ENV: " + DisClientConfig.getInstance().ENV);

        // auto dis conf & maintype
        LOGGER.info("SERVER ENABLE_DIS_AUTOCONF: "
                + DisClientConfig.getInstance().ENABLE_DIS_AUTOCONF);
        if (DisClientConfig.getInstance().ENABLE_DIS_AUTOCONF) {
            // main
            if (StringNUtils.isEmpty(DisClientConfig.getInstance().MAIN_TYPE)) {

                throw new Exception("settings: MAIN_TYPE cannot find");
            }
            LOGGER.info("SERVER MAIN_TYPE: "
                    + DisClientConfig.getInstance().MAIN_TYPE);
        }

        // enable.remote.conf
        LOGGER.info("SERVER ENABLE_REMOTE_CONF: "
                + DisClientConfig.getInstance().ENABLE_REMOTE_CONF);

        // maintype.change.timeout
        LOGGER.debug("SERVER MAINTYPE_CHANGE_TIMEOUT: "
                + DisClientConfig.getInstance().MAINTYPE_CHANGE_TIMEOUT);

        //
        // zookeeper相关
        //

        if (StringNUtils.isEmpty(DisClientConfig.getInstance().ZOOKEEPER_HOST)) {
            throw new Exception("settings: ZOOKEEPER_HOST cannot find");
        }
        LOGGER.info("SERVER ZOOKEEPER_HOST: "
                + DisClientConfig.getInstance().ZOOKEEPER_HOST);
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

        // conf_server_action
        if (StringNUtils
                .isEmpty(DisClientSysConfig.getInstance().CONF_SERVER_ACTION)) {

            throw new Exception("settings: CONF_SERVER_BASE_URL cannot find");
        }
        LOGGER.debug("SERVER Action URL: "
                + DisClientSysConfig.getInstance().CONF_SERVER_ACTION);

        // conf_server_master_num_action
        if (StringNUtils
                .isEmpty(DisClientSysConfig.getInstance().CONF_SERVER_MASTER_NUM_ACTION)) {

            throw new Exception(
                    "settings: CONF_SERVER_MASTER_NUM_ACTION  cannot find");
        }
        LOGGER.debug("SERVER CONF_SERVER_MASTER_NUM_ACTION Action URL: "
                + DisClientSysConfig.getInstance().CONF_SERVER_MASTER_NUM_ACTION);

        //
        // 本地相关
        //

        if (StringNUtils
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

        // local_dowload_dir
        LOGGER.debug("SERVER LOCAL_DOWNLOAD_DIR: "
                + DisClientSysConfig.getInstance().LOCAL_DOWNLOAD_DIR);
        OsUtil.makeDirs(DisClientSysConfig.getInstance().LOCAL_DOWNLOAD_DIR);
        OsUtil.makeDirs(DisClientSysConfig.getInstance().getDownloadTmpDir());

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
