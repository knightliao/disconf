package com.baidu.disconf2.web.service.zookeeper.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baidu.disconf2.web.service.zookeeper.config.ZooConfig;
import com.baidu.disconf2.web.service.zookeeper.service.ZooMgr;

/**
 * 
 * @author liaoqiqi
 * @version 2014-6-16
 */
@Service
public class ZooMgrImpl implements ZooMgr {

    @Autowired
    private ZooConfig zooConfig;

    @Override
    public String getHosts() {

        return zooConfig.getZooHosts();
    }
}
