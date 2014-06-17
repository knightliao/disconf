package com.baidu.disconf2.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baidu.disconf2.client.common.annotations.DisconfUpdateService;
import com.baidu.disconf2.client.common.inter.IDisconfUpdate;
import com.baidu.disconf2.demo.config.JedisConfig;

/**
 * 更新Redis配置时的回调函数
 * 
 * @author liaoqiqi
 * @version 2014-6-17
 */
@Service
@DisconfUpdateService(keys = { JedisConfig.filename })
public class SimpleRedisServiceUpdateCallback implements IDisconfUpdate {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(SimpleRedisServiceUpdateCallback.class);

    @Autowired
    private SimpleRedisService simpleRedisService;

    /**
     * 
     */
    public void reload() throws Exception {

        simpleRedisService.changeJedis();
    }

}
