package com.baidu.disconf.client.common.model;

/**
 * 实例指纹
 *
 * @author liaoqiqi
 * @version 2014-6-27
 */
public class InstanceFingerprint {

    // 本实例所在机器的IP
    private String host = "";

    // 可以表示本实例的PORT
    private int port = 0;

    // 一个实例固定的UUID
    private String uuid = "";

    public String getHost() {
        return host;
    }

    public InstanceFingerprint(String host, int port, String uuid) {
        super();
        this.host = host;
        this.port = port;
        this.uuid = uuid;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "InstanceFingerprint [host=" + host + ", port=" + port + "]";
    }

}
