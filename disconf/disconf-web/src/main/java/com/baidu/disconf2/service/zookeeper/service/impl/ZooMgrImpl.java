package com.baidu.disconf2.service.zookeeper.service.impl;

import com.baidu.disconf2.service.zookeeper.service.ZooMgr;

/**
 * 
 * @author liaoqiqi
 * @version 2014-6-16
 */
public class ZooMgrImpl implements ZooMgr {

    private String zooHosts = "";

    public String getZooHosts() {
        return zooHosts;
    }

    public void setZooHosts(String zooHosts) {
        this.zooHosts = zooHosts;
    }

    @Override
    public String getHosts() {

        return zooHosts;
    }
}
