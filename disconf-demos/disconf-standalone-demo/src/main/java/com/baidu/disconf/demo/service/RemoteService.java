package com.baidu.disconf.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.baidu.disconf.demo.config.RemoteServerConfig;

/**
 * 一个未知远程服务, 这里也不使用注解的 @Service
 * 
 * @author liaoqiqi
 * @version 2014-6-17
 */
public class RemoteService implements InitializingBean, DisposableBean {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(RemoteService.class);

    private List<String> list = new ArrayList<String>();

    @Autowired
    private RemoteServerConfig remoteServerConfig;

    public void destroy() throws Exception {

    }

    public void afterPropertiesSet() throws Exception {

        list.add(remoteServerConfig.getRemoteHost());
        list.add(String.valueOf(remoteServerConfig.getRemoteHost()));
    }

    /**
     * 更改Jedis
     */
    public void reload() {

        LOGGER.info("start to reload remote service to: "
                + remoteServerConfig.getRemoteHost() + " : "
                + remoteServerConfig.getRemoteHost());

        list.add(remoteServerConfig.getRemoteHost());
        list.add(String.valueOf(remoteServerConfig.getRemoteHost()));

        LOGGER.info("reload ok.");
    }
}
