package com.baidu.disconf2.demo.model;

import com.baidu.disconf2.client.common.annotations.DisconfFile;
import com.baidu.disconf2.client.common.annotations.DisconfFileItem;

/**
 * 
 * @author liaoqiqi
 * @version 2014-6-17
 */
@DisconfFile(filename = JedisConfig.filename)
public class JedisConfig {

    public static final String filename = "redis.properties";

    // 代表连接地址
    private String host;

    // 代表连接port
    private int port;

    @DisconfFileItem
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @DisconfFileItem
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
