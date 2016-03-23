package com.baidu.disconf.client.common.update;

/**
 * 通用型的配置更新接口。当配置更新 时，用户可以实现此接口，用以来实现回调函数.
 */
public interface IDisconfUpdatePipeline {

    /**
     * 配置文件
     *
     * @param key
     * @param filePath
     *
     * @throws Exception
     */
    void reloadDisconfFile(String key, String filePath) throws Exception;

    /**
     * 配置项
     *
     * @param key
     * @param content
     *
     * @throws Exception
     */
    void reloadDisconfItem(String key, Object content) throws Exception;
}
