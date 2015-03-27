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
     * 处理one配置
     */
    void processOneItem(String key);

    /**
     * 更新指定的配置并进行回调
     */
    void updateOneConfAndCallback(String key) throws Exception;

    /**
     * 特殊的，将数据注入到配置实体中
     */
    void inject2Conf();
}
