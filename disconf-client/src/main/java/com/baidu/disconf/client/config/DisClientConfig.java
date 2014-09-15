package com.baidu.disconf.client.config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.client.config.inner.DisInnerConfigAnnotation;
import com.baidu.disconf.client.utils.DisconfAutowareConfig;
import com.baidu.disconf.core.common.constants.Constants;

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
    public boolean ENABLE_DISCONF = false;

    /**
     * 忽略哪些分布式配置
     * 
     * @author
     * @since 1.0.0
     */
    @DisInnerConfigAnnotation(name = "ignore", defaultValue = "")
    public String IGNORE_DISCONF_LIST = "";
    private Set<String> ignoreDisconfKeySet = new HashSet<String>();

    /**
     * 获取远程配置 重试次数，默认是3次
     * 
     * @author
     * @since 1.0.0
     */
    @DisInnerConfigAnnotation(name = "conf_server_url_retry_times", defaultValue = "3")
    public int CONF_SERVER_URL_RETRY_TIMES = 3;

    /**
     * 获取远程配置 重试时休眠时间，默认是5秒
     * 
     * @author
     * @since 1.0.0
     */
    @DisInnerConfigAnnotation(name = "conf_server_url_retry_sleep_seconds", defaultValue = "2")
    public int CONF_SERVER_URL_RETRY_SLEEP_SECONDS = 5;

    public List<String> getHostList() {
        return hostList;
    }

    public void setHostList(List<String> hostList) {
        this.hostList = hostList;
    }

    public Set<String> getIgnoreDisconfKeySet() {
        return ignoreDisconfKeySet;
    }

    public void setIgnoreDisconfKeySet(Set<String> ignoreDisconfKeySet) {
        this.ignoreDisconfKeySet = ignoreDisconfKeySet;
    }

}
