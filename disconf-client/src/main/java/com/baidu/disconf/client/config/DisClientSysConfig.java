package com.baidu.disconf.client.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.client.config.inner.DisInnerConfigAnnotation;
import com.baidu.disconf.client.support.DisconfAutowareConfig;

/**
 * Disconf 系统自带的配置
 *
 * @author liaoqiqi
 * @version 2014-6-6
 */
public class DisClientSysConfig {

    protected static final Logger LOGGER = LoggerFactory.getLogger(DisClientSysConfig.class);

    protected static final DisClientSysConfig INSTANCE = new DisClientSysConfig();

    public static DisClientSysConfig getInstance() {
        return INSTANCE;
    }

    protected static final String filename = "disconf_sys.properties";

    private boolean isLoaded = false;

    private DisClientSysConfig() {

    }

    public synchronized boolean isLoaded() {
        return isLoaded;
    }

    /**
     * load config normal
     */
    public synchronized void loadConfig(String filePath) throws Exception {

        if (isLoaded) {
            return;
        }

        String filePathInternal = filename;

        if (filePath != null) {

            filePathInternal = filePath;
        }

        DisconfAutowareConfig.autowareConfig(INSTANCE, filePathInternal);

        isLoaded = true;
    }

    /**
     * STORE URL
     *
     * @author
     * @since 1.0.0
     */
    @DisInnerConfigAnnotation(name = "disconf.conf_server_store_action")
    public String CONF_SERVER_STORE_ACTION;

    /**
     * STORE URL
     *
     * @author
     * @since 1.0.0
     */
    @DisInnerConfigAnnotation(name = "disconf.conf_server_zoo_action")
    public String CONF_SERVER_ZOO_ACTION;

    /**
     * 获取远程主机个数的URL
     *
     * @author
     * @since 1.0.0
     */
    @DisInnerConfigAnnotation(name = "disconf.conf_server_master_num_action")
    public String CONF_SERVER_MASTER_NUM_ACTION;

    /**
     * 下载文件夹, 远程文件下载后会放在这里
     *
     * @author
     * @since 1.0.0
     */
    @DisInnerConfigAnnotation(name = "disconf.local_download_dir")
    public String LOCAL_DOWNLOAD_DIR;

}
