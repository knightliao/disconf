package com.baidu.disconf.client.core.processor;

/**
 * 处理算子
 * 
 * @author liaoqiqi
 * @version 2014-8-4
 */
public interface DisconfCoreProcessor {

    /**
     * 处理所有配置
     */
    void processAllItems();

    /**
     * 更新指定的配置并进行回调
     */
    void updateOneConfAndCallback(String key) throws Exception;

    /**
     * 更新 一个配置(没有回调)
     */
    void updateOneConf(String keyName) throws Exception;

    /**
     * 特殊的，将数据注入到配置实体中
     */
    void inject2Conf();
}
