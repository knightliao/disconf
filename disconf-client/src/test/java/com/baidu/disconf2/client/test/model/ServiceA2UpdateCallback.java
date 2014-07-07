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
 * 1. 使用了分布式配置文件 @DisconfUpdateService
 * 
 * @author liaoqiqi
 * @version 2014-5-22
 */
@Service
@DisconfUpdateService(keys = { ConfA.filename, ServiceA.keyA })
public class ServiceA2UpdateCallback implements IDisconfUpdate {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(ServiceAUpdateCallback.class);

    @Autowired
    private ServiceA serviceA;

    /**
     * 
     */
    public void reload() throws Exception {

        LOGGER.info(String.valueOf(serviceA.calcMoneyA2()));
    }

}
