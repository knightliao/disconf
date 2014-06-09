package com.baidu.disconf2.client.test.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baidu.disconf2.client.common.annotations.DisconfUpdateService;
import com.baidu.disconf2.client.common.inter.IDisconfUpdate;

/**
 * 分布式配置服务回调函数<br/>
 * 
 * 1. 使用了分布式配置文件 @DisconfUpdateService <br/>
 * 2. 主备切换分布式服务 @DisconfUpdateService
 * 
 * @author liaoqiqi
 * @version 2014-5-22
 */
@Service
@DisconfUpdateService(keys = { ConfA.filename, ConfA.keyA })
public class ServiceBUpdateCallback implements IDisconfUpdate {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(ServiceBUpdateCallback.class);

    @Autowired
    private ServiceB serviceB;

    /**
     * 
     */
    public void reload() throws Exception {

        LOGGER.info(String.valueOf(serviceB.getAValue()));
    }

}
