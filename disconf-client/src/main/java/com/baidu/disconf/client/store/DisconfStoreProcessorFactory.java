package com.baidu.disconf.client.store;

import com.baidu.disconf.client.store.processor.impl.DisconfStoreFileProcessorImpl;
import com.baidu.disconf.client.store.processor.impl.DisconfStoreItemProcessorImpl;
import com.baidu.disconf.client.store.processor.impl.DisconfStorePipelineProcessorImpl;

/**
 * 仓库算子仓库
 *
 * @author liaoqiqi
 * @version 2014-8-4
 */
public class DisconfStoreProcessorFactory {

    /**
     * 获取配置文件仓库算子
     */
    public static DisconfStoreProcessor getDisconfStoreFileProcessor() {

        return new DisconfStoreFileProcessorImpl();
    }

    /**
     * 获取配置项仓库算子
     */
    public static DisconfStoreProcessor getDisconfStoreItemProcessor() {

        return new DisconfStoreItemProcessorImpl();
    }

    /**
     *
     */
    public static DisconfStorePipelineProcessor getDisconfStorePipelineProcessor() {

        return new DisconfStorePipelineProcessorImpl();
    }

}
