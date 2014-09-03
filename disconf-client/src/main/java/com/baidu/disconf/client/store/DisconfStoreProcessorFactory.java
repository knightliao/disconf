package com.baidu.disconf.client.store;

import com.baidu.disconf.client.store.processor.impl.DisconfStoreFileProcessorImpl;
import com.baidu.disconf.client.store.processor.impl.DisconfStoreItemProcessorImpl;

/**
 * 仓库算子仓库
 * 
 * @author liaoqiqi
 * @version 2014-8-4
 */
public class DisconfStoreProcessorFactory {

    /**
     * 获取配置文件仓库算子
     * 
     * @return
     */
    public static DisconfStoreProcessor getDisconfStoreFileProcessor() {

        return new DisconfStoreFileProcessorImpl();
    }

    /**
     * 获取配置项仓库算子
     * 
     * @return
     */
    public static DisconfStoreProcessor getDisconfStoreItemProcessor() {

        return new DisconfStoreItemProcessorImpl();
    }
}
