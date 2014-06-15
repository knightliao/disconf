package com.baidu.disconf2.client.config.inner;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf2.core.common.constants.Constants;
import com.baidu.utils.DisconfAutowareConfig;

/**
 * Disconf Client的用户配置文件
 * 
 * @author liaoqiqi
 * @version 2014-6-6
 */
public final class DisClientConfig {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(DisClientConfig.class);

    protected static final DisClientConfig INSTANCE = new DisClientConfig();

    public static DisClientConfig getInstance() {
        return INSTANCE;
    }

    protected static final String filename = "disconf.properties";

    private boolean isLoaded = false;

    private DisClientConfig() {

    }

    public synchronized boolean isLoaded() {
        return isLoaded;
    }

    /**
     * load config normal
     * 
     * @param bundle
     * @param filePath
     */
    public synchronized void loadConfig(String filePath) throws Exception {

        if (isLoaded == true) {
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
     * 配置文件服务器 HOST
     */
    @DisInnerConfigAnnotation(name = "conf_server_host")
    public String CONF_SERVER_HOST;
    private List<String> hostList;

    /**
     * zookeeper HOST
     * 
     * @author
     * @since 1.0.0
     */
    @DisInnerConfigAnnotation(name = "zookeeper_host")
    public String ZOOKEEPER_HOST;

    /**
     * app
     * 
     * @author
     * @since 1.0.0
     */
    @DisInnerConfigAnnotation(name = "app")
    public String APP;

    /**
     * 版本
     * 
     * @author
     * @since 1.0.0
     */
    @DisInnerConfigAnnotation(name = "version", defaultValue = Constants.DEFAULT_VERSION)
    public String VERSION = Constants.DEFAULT_VERSION;

    /**
     * 主或备
     * 
     * @author
     * @since 1.0.0
     */
    @DisInnerConfigAnnotation(name = "maintype")
    public String MAIN_TYPE;

    /**
     * 部署环境
     * 
     * @author
     * @since 1.0.0
     */
    @DisInnerConfigAnnotation(name = "env", defaultValue = Constants.DEFAULT_ENV)
    public String ENV = Constants.DEFAULT_ENV;

    /**
     * 是否从云端下载配置
     * 
     * @author
     * @since 1.0.0
     */
    @DisInnerConfigAnnotation(name = "enable.remote.conf", defaultValue = "false")
    public boolean ENABLE_REMOTE_CONF = false;

    public List<String> getHostList() {
        return hostList;
    }

    public void setHostList(List<String> hostList) {
        this.hostList = hostList;
    }

}
