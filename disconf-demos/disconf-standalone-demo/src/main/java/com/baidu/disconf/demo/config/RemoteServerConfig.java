package com.baidu.disconf.demo.config;

import org.springframework.stereotype.Service;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;

/**
 * 
 * @author liaoqiqi
 * @version 2014-6-17
 */
@Service
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
    @DisconfFileItem(name = "remoteHost")
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
    @DisconfFileItem(name = "remotePort")
    public int getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

}
