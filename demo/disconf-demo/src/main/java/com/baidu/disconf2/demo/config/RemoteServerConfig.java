package com.baidu.disconf2.demo.config;

import com.baidu.disconf2.client.common.annotations.DisconfFile;
import com.baidu.disconf2.client.common.annotations.DisconfFileItem;

/**
 * 
 * @author liaoqiqi
 * @version 2014-6-17
 */
@DisconfFile(filename = RemoteServerConfig.filename)
public class RemoteServerConfig {

    public static final String filename = "remote.properties";

    // 代表连接地址
    private String remoteHost;

    // 代表连接port
    private int remotePort;

    /**
     * 地址, 分布式文件配置
     * 
     * @return
     */
    @DisconfFileItem
    public String getRemoteHost() {
        return remoteHost;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    /**
     * 端口, 分布式文件配置
     * 
     * @return
     */
    @DisconfFileItem
    public int getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

}
