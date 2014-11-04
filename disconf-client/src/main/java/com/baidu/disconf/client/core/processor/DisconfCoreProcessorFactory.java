package com.baidu.disconf.client.core.processor;

import com.baidu.disconf.client.core.processor.impl.DisconfFileCoreProcessorImpl;
import com.baidu.disconf.client.core.processor.impl.DisconfItemCoreProcessorImpl;
import com.baidu.disconf.client.fetcher.FetcherMgr;
import com.baidu.disconf.client.watch.WatchMgr;

/**
 * 核心处理器工厂
 * 
 * @author liaoqiqi
 * @version 2014-8-4
 */
public class DisconfCoreProcessorFactory {

    /**
     * 获取配置文件核心处理器
     * 
     * @return
     */
    public static DisconfCoreProcessor getDisconfCoreProcessorFile(
            WatchMgr watchMgr, FetcherMgr fetcherMgr) {

        return new DisconfFileCoreProcessorImpl(watchMgr, fetcherMgr);
    }

    /**
     * 获取配置项核心 处理器
     * 
     * @return
     */
    public static DisconfCoreProcessor getDisconfCoreProcessorItem(
            WatchMgr watchMgr, FetcherMgr fetcherMgr) {

        return new DisconfItemCoreProcessorImpl(watchMgr, fetcherMgr);
    }
}
