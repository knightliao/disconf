package com.baidu.disconf.client.test.mockserver;

/*
 * 
 * 远程MOck测试服务器的一些常量
 */
public class DisconfWebMockServer {

    public static final Integer PORT = 8089;

    public static final String LOCAL_URL = "127.0.0.1:8089";

    //
    // 配置文件
    //
    public static final String FILE_URL = "/api/config/file?app=disconf_demo&env=rd&type=0&key=confA.properties&version=1_0_0_0";
    public static final String FILE_NAME = "confA.properties";

    //
    // 配置项
    //
    public static final String ITEM_URL = "/api/config/file?app=disconf_demo&env=rd&type=0&key=keyA&version=1_0_0_0";
    public static final String CONTENT_TYPE = "application/json";
    public static final String DEFAULT_ITEM_VALUE = "1000";
}
