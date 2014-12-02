package com.baidu.disconf.web.tasks.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.baidu.disconf.web.config.ApplicationPropertyConfig;
import com.baidu.disconf.web.tasks.IConfigConsistencyMonitorService;

/**
 * http://blog.csdn.net/sd4000784/article/details/7745947 <br/>
 * http://blog.sina.com.cn/s/blog_6925c03c0101d1hi.html
 * 
 * @author knightliao
 * 
 */
@Component
public class ConfigConsistencyMonitorServiceImpl implements IConfigConsistencyMonitorService {

    protected static final Logger LOG = LoggerFactory.getLogger(ConfigConsistencyMonitorServiceImpl.class);

    @Autowired
    private ApplicationPropertyConfig applicationPropertyConfig;

    // 每1分钟执行一次自动化校验
    @Scheduled(fixedDelay = 1 * 60 * 1000)
    @Override
    public void myTest() {
        LOG.info("task schedule just testing, every 1 min");
    }

    /**
     * 
     */
    // 每5分钟执行一次自动化校验
    @Scheduled(fixedDelay = 5 * 60 * 1000)
    @Override
    public void check() {

        /**
         * 
         */
        if (!applicationPropertyConfig.isCheckConsistencyOn()) {
            return;
        }

    }
}
