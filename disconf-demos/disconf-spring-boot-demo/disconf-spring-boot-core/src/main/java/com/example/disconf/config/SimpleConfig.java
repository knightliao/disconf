package com.example.disconf.config;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
@DisconfFile(filename = "simple.properties")
public class SimpleConfig {
    // 代表连接地址
    private String host;

    // 代表连接port
    private int port;

    /**
     * 地址
     *
     * @return
     */
    @DisconfFileItem(name = "host")
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    /**
     * 端口
     *
     * @return
     */
    @DisconfFileItem(name = "port")
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
