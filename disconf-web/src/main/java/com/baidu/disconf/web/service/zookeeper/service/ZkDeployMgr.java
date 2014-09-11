package com.baidu.disconf.web.service.zookeeper.service;

/**
 * 
 * 
 * @author liaoqiqi
 * @version 2014-9-11
 */
public interface ZkDeployMgr {

    /**
     * 
     * @param appId
     * @param envId
     * @param version
     * @return
     */
    String getDeployInfo(String app, String env, String version);
}
