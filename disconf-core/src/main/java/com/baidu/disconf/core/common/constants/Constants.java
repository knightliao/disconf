package com.baidu.disconf.core.common.constants;

/**
 * @author liaoqiqi
 * @version 2014-6-6
 */
public class Constants {

    public static final String DEFAULT_VERSION = "DEFAULT_VERSION";

    public static final String DEFAULT_ENV = "DEFAULT_ENV";

    /**
     * 配置的常量定义
     */
    public final static String VERSION = "version";
    public final static String APP = "app";
    public final static String MAINTYPE = "maintype";
    public final static String ENV = "env";
    public final static String KEY = "key";
    public final static String TYPE = "type";

    /**
     * Disconf-web返回的常量
     */
    public static final Integer OK = 1;
    public static final Integer NOTOK = 0;

    /**
     * zookeeper的一些常量设置
     */
    public final static String STORE_FILE_URL_KEY = "file";
    public final static String STORE_ITEM_URL_KEY = "item";
    public final static String ZOO_HOSTS_URL_KEY = "hosts";
    public final static String ZOO_HOSTS_URL_PREFIX_KEY = "prefix";

    // 通知Zookeeper更新配置的消息
    public final static String ZOO_UPDATE_STRING = "UPDATE-NOTIFYING";

    public final static String SEP_STRING = "/";
}
