package com.baidu.disconf2.web.innerapi.zookeeper;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baidu.disconf2.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf2.core.common.zookeeper.ZookeeperMgr;
import com.baidu.disconf2.web.service.zookeeper.config.ZooConfig;

/**
 * 
 * @author liaoqiqi
 * @version 2014-6-24
 */
@Service
public class ZooKeeperDriver implements InitializingBean, DisposableBean {

    @Autowired
    private ZooConfig zooConfig;

    /**
     * 通知某个Node更新
     * 
     * @param app
     * @param env
     * @param version
     * @param disConfigTypeEnum
     */
    public void notifyNodeUpdate(String app, String env, String version,
            DisConfigTypeEnum disConfigTypeEnum) {

    }

    @Override
    public void destroy() throws Exception {

        ZookeeperMgr.getInstance().release();
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        ZookeeperMgr.getInstance().init(zooConfig.getZooHosts(),
                zooConfig.getZookeeperUrlPrefix());
    }
}
