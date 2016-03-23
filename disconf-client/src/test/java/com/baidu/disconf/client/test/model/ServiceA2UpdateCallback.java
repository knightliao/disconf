package com.baidu.disconf.client.test.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baidu.disconf.client.common.annotations.DisconfUpdateService;
import com.baidu.disconf.client.common.update.IDisconfUpdate;

/**
 * 分布式配置服务回调函数<br/>
 * <p/>
 * 1. 使用了分布式配置文件 @DisconfUpdateService
 *
 * @author liaoqiqi
 * @version 2014-5-22
 */
@Service
@DisconfUpdateService(classes = {ConfA.class})
public class ServiceA2UpdateCallback implements IDisconfUpdate {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ServiceAUpdateCallback.class);

    @Autowired
    private ServiceA serviceA;

    /**
     *
     */
    public void reload() throws Exception {

        LOGGER.info(String.valueOf(serviceA.calcMoneyA2()));
    }

}
