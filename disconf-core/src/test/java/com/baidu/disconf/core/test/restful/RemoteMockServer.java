package com.baidu.disconf.core.test.restful;

import java.util.List;

import com.google.common.collect.Lists;

/*
 * 
 * 远程MOck测试服务器的一些常量
 */
public class RemoteMockServer {

    // 远程PORT
    public static final Integer PORT = 9289;

    // 远程URL
    public static final List<String> LOCAL_HOST_LIST = Lists.newArrayList("127.0.0.1:" + String.valueOf(PORT));

    //
    // 配置文件
    //
    public static final String FILE_URL =
            "/api/config/file?version=1_0_0_0&app=disconf_testcase&env=rd&key=confA.properties&type=0";
    public static final String FILE_NAME = "confA.properties";
    public static final String FILE_CONTENT = "confa.varA=1000\r\nconfa.varA2=2000";

    //
    // 空配置文件
    //
    public static final String EMPTY_FILE_URL =
            "/api/config/file?version=1_0_0_0&app=disconf_testcase&env=rd&key=empty.properties&type=0";
    public static final String EMPTY_FILE_NAME = "empty.properties";

    //
    // 静态配置文件
    //
    public static final String STATIC_FILE_URL =
            "/api/config/file?version=1_0_0_0&app=disconf_testcase&env=rd&key=staticConf.properties&type=0";
    public static final String STATIC_FILE_NAME = "staticConf.properties";
    public static final String STATIC_FILE_CONTENT = "staticvar=50\r\nstaticvar2=100";

    //
    // 非注解 方式1
    //
    public static final String NON_ANOTATION_FILE_URL =
            "/api/config/file?version=1_0_0_0&app=disconf_testcase&env=rd&key=atomserverl.properties&type=0";
    public static final String NON_ANOTATION_FILE_NAME = "atomserverl.properties";
    public static final String NON_ANOTATTION_FILE_CONTENT =
            "server=10.81.11.243:16600,10.81.11.243:16602,10.81.11.243:16603\r\nretry=3";

    //
    // 非注解 方式2
    //
    public static final String NON_ANOTATION_FILE_URL2 =
            "/api/config/file?version=1_0_0_0&app=disconf_testcase&env=rd&key=atomserverm_slave.properties&type=0";
    public static final String NON_ANOTATION_FILE_NAME2 = "atomserverm_slave.properties";
    public static final String NON_ANOTATTION_FILE_CONTENT2 =
            "#online\r\nserver=10.23.247.6:16700,10.23.247.22:16700,10.65.25.6:16700,10.65.25.7:16700\r\nretry=3";

    //
    // 配置项
    //
    public static final String ITEM_URL =
            "/api/config/item?version=1_0_0_0&app=disconf_testcase&env=rd&key=keyA&type=1";
    public static final String CONTENT_TYPE = "application/json";
    public static final String DEFAULT_ITEM_VALUE = "1000";

    //
    public static final String LOCAL_DOWNLOAD_DIR = "./disconf/download";
    public static final String LOCAL_DOWNLOAD_DIR_TEMP = "./disconf/tmp/download";
    public static final String LOCAL_TARGET_DOWNLOAD_DIR = "./disconf/download/target";

    //
    // zoo
    //
    public static final String ZOO_URL = "/api/zoo/hosts";
    public static final String ZOO_HOSTS = "127.0.0.1:8581,127.0.0.1:8582,127.0.0.1:8583";
    public static final String ZOO_PREFIX_URL = "/api/zoo/prefix";
    public static final String ZOO_PREFIX_VALUE = "/disconf";
}
