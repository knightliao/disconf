package com.example.disconf.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.baidu.disconf.client.common.annotations.DisconfUpdateService;
import com.baidu.disconf.client.common.update.IDisconfUpdate;
import com.example.disconf.demo.config.Coefficients;
import com.example.disconf.demo.config.JedisConfig;

/**
 * 更新Redis配置时的回调函数
 * 
 * @author liaoqiqi
 * @version 2014-6-17
 */
@Service
@Scope("singleton")
@DisconfUpdateService(classes = { JedisConfig.class }, itemKeys = { Coefficients.key })
public class SimpleRedisServiceUpdateCallback implements IDisconfUpdate {

    protected static final Logger LOGGER = LoggerFactory.getLogger(SimpleRedisServiceUpdateCallback.class);

    @Autowired
    private SimpleRedisService simpleRedisService;

    /**
     * 
     */
    public void reload() throws Exception {

        simpleRedisService.changeJedis();
    }

}
