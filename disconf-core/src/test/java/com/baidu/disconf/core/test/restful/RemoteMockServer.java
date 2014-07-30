package com.baidu.disconf.core.test.restful;

import java.util.List;

import com.google.common.collect.Lists;

/*
 * 
 * 远程MOck测试服务器的一些常量
 */
public class RemoteMockServer {

    // 远程PORT
    public static final Integer PORT = 8089;

    // 远程URL
    public static final List<String> LOCAL_HOST_LIST = Lists
            .newArrayList("127.0.0.1:" + String.valueOf(PORT));

    //
    // 配置文件
    //
    public static final String FILE_URL = "/api/config/file?app=disconf_demo&env=rd&type=0&key=confA.properties&version=1_0_0_0";
    public static final String FILE_NAME = "confA.properties";

    //
    // 配置项
    //
    public static final String ITEM_URL = "/api/config/item?app=disconf_demo&env=rd&type=1&key=moneyInvest&version=1_0_0_0";
    public static final String CONTENT_TYPE = "application/json";
    public static final String DEFAULT_ITEM_VALUE = "1000";

    //
    public static final String LOCAL_DOWNLOAD_DIR = "./disconf/download";
}
