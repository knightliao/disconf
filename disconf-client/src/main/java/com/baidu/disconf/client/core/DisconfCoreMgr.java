package com.baidu.disconf.client.core;

/**
 * 核心处理模块：处理 下载、注入、Watch三模块
 * 
 * @author liaoqiqi
 * @version 2014-6-10
 */
public interface DisconfCoreMgr {

    /**
     * 1. 获取远程的所有配置数据<br/>
     * 2. 注入到仓库中<br/>
     * 3. Watch 配置
     */
    void process();

    /**
     * 特殊的，将数据注入到 配置项、配置文件 的实体中
     */
    void inject2DisconfInstance();

    /**
     * 更新 配置
     */
    void updateOneConfItem(String keyName) throws Exception;

    /**
     * 更新 配置文件
     */
    void updateOneConfFile(String fileName) throws Exception;

    void release();
}
