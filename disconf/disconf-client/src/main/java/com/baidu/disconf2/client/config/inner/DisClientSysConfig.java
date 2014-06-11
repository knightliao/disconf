package com.baidu.disconf2.client.config.inner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.utils.DisconfAutowareConfig;
import com.baidu.utils.OsUtil;

/**
 * Disconf 系统自带的配置
 * 
 * @author liaoqiqi
 * @version 2014-6-6
 */
public class DisClientSysConfig {
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(DisClientSysConfig.class);

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
     * 远程配置管理服务的URL前缀
     * 
     * @author
     * @since 1.0.0
     */
    @DisInnerConfigAnnotation(name = "conf_server_action")
    public String CONF_SERVER_ACTION;

    /**
     * 获取远程主机个数的URL
     * 
     * @author
     * @since 1.0.0
     */
    @DisInnerConfigAnnotation(name = "conf_server_master_num_action")
    public String CONF_SERVER_MASTER_NUM_ACTION;

    /**
     * 下载文件夹, 远程文件下载后会放在这里
     * 
     * @author
     * @since 1.0.0
     */
    @DisInnerConfigAnnotation(name = "local_dowload_dir")
    public String LOCAL_DOWNLOAD_DIR;

    /**
     * 让下载文件夹放在 classpath目录 下
     * 
     * @author
     * @since 1.0.0
     */
    @DisInnerConfigAnnotation(name = "enable_local_download_dir_in_class_path", defaultValue = "true")
    public boolean ENABLE_LOCAL_DOWNLOAD_DIR_IN_CLASS_PATH = true;

    /**
     * zookeeper的前缀路径名
     * 
     * @author
     * @since 1.0.0
     */
    @DisInnerConfigAnnotation(name = "zookeeper_url_prefix")
    public String ZOOKEEPER_URL_PREFIX;

    /**
     * 获取远程配置 重试次数，默认是3次
     * 
     * @author
     * @since 1.0.0
     */
    @DisInnerConfigAnnotation(name = "conf_server_url_retry_times", defaultValue = "3")
    public int CONF_SERVER_URL_RETRY_TIMES = 3;

    /**
     * 获取远程配置 重试时休眠时间，默认是2秒
     * 
     * @author
     * @since 1.0.0
     */
    @DisInnerConfigAnnotation(name = "conf_server_url_retry_sleep_seconds", defaultValue = "2")
    public int CONF_SERVER_URL_RETRY_SLEEP_SECONDS = 2;

    /**
     * 
     * @Description: 下载的临时文件夹
     * 
     * @return
     * @return String
     * @author liaoqiqi
     * @date 2013-6-14
     */
    public String getDownloadTmpDir() {

        return OsUtil.pathJoin(getInstance().LOCAL_DOWNLOAD_DIR, "tmp");
    }

}
