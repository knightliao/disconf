package com.baidu.disconf.web.service.zookeeper.config;

/**
 * @author liaoqiqi
 * @version 2014-6-24
 */
public class ZooConfig {

    private String zooHosts = "";

    public String zookeeperUrlPrefix = "";

    public String getZooHosts() {
        return zooHosts;
    }

    public void setZooHosts(String zooHosts) {
        this.zooHosts = zooHosts;
    }

    public String getZookeeperUrlPrefix() {
        return zookeeperUrlPrefix;
    }

    public void setZookeeperUrlPrefix(String zookeeperUrlPrefix) {
        this.zookeeperUrlPrefix = zookeeperUrlPrefix;
    }
}
