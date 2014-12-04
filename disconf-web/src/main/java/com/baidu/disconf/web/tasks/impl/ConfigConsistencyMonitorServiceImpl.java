package com.baidu.disconf.web.tasks.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.baidu.disconf.web.config.ApplicationPropertyConfig;
import com.baidu.disconf.web.service.app.bo.App;
import com.baidu.disconf.web.service.app.service.AppMgr;
import com.baidu.disconf.web.service.zookeeper.service.ZkDeployMgr;
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

    @Autowired
    private ZkDeployMgr zkDeployMgr;

    @Autowired
    private AppMgr appMgr;

    // 每3分钟执行一次自动化校验
    @Scheduled(fixedDelay = 3 * 60 * 1000)
    @Override
    public void myTest() {
        LOG.info("task schedule just testing, every 1 min");
    }

    /**
     * 
     */
    // 每30分钟执行一次自动化校验
    @Scheduled(fixedDelay = 30 * 60 * 1000)
    @Override
    public void check() {

        /**
         * 
         */
        if (!applicationPropertyConfig.isCheckConsistencyOn()) {
            return;
        }

        return;
    }

    /**
     * 主check MGR
     */
    private void checkMgr() {

        appMgr.getList();
    }

    /**
     * 校验APP 一致性
     */
    private void checkAppConfigConsistency(App app) {

    }

    /**
     * 校验APP/ENV/VERSION 一致性
     */
    private void checkAppEnvVersionConfigConsistency() {

    }

}
