package com.baidu.disconf.core.common.zookeeper.inner;

/**
 * ZK链接的Hosts
 * @author lipangeng, Email:lipg@outlook.com
 * @version 1.0 on 2017/5/23 上午11:20
 * @since 1.0 Created by lipangeng on 2017/5/23 上午11:20. Email:lipg@outlook.com.
 */
public class ZKHost {
    private String host;
    private Integer port;

    public ZKHost() {
    }

    public ZKHost(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
