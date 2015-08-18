package com.example.disconf.service;

import com.example.disconf.config.SimpleConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class SimpleDemoService implements InitializingBean, DisposableBean {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(SimpleDemoService.class);


    /**
     * 分布式配置
     */
    @Autowired
    private SimpleConfig simpleConfig;

    /**
     * 关闭
     */
    public void destroy() throws Exception {

        LOGGER.info("destroy ==> ");
    }

    /**
     * 进行连接
     */
    public void afterPropertiesSet() throws Exception {

        LOGGER.info("connect ==> ");
    }

    /**
     * 后台更改值
     */
    public void changeConfig() {

        LOGGER.info("start to change hosts to: " + simpleConfig.getHost() + " : " + simpleConfig.getPort());

        LOGGER.info("change ok.");
    }
}