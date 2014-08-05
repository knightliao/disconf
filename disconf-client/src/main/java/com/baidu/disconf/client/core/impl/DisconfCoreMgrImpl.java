package com.baidu.disconf.client.core.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.client.core.DisconfCoreMgr;
import com.baidu.disconf.client.core.processor.DisconfCoreProcessor;
import com.baidu.disconf.client.core.processor.DisconfCoreProcessorFactory;
import com.baidu.disconf.client.fetcher.FetcherMgr;
import com.baidu.disconf.client.watch.WatchMgr;

/**
 * 核心处理器
 * 
 * @author liaoqiqi
 * @version 2014-6-10
 */
public class DisconfCoreMgrImpl implements DisconfCoreMgr {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(DisconfCoreMgrImpl.class);

    // file核心处理
    private DisconfCoreProcessor disconfCoreProcessorFile = null;

    // item核心 处理
    private DisconfCoreProcessor disconfCoreProcessorItem = null;

    // 监控器
    private WatchMgr watchMgr = null;

    // 抓取器
    private FetcherMgr fetcherMgr = null;

    public DisconfCoreMgrImpl(WatchMgr watchMgr, FetcherMgr fetcherMgr) {

        this.disconfCoreProcessorFile = DisconfCoreProcessorFactory
                .getDisconfCoreProcessorFile(watchMgr, fetcherMgr);
        this.disconfCoreProcessorItem = DisconfCoreProcessorFactory
                .getDisconfCoreProcessorItem(watchMgr, fetcherMgr);
    }

    /**
     * 1. 获取远程的所有配置数据<br/>
     * 2. 注入到仓库中<br/>
     * 3. Watch 配置 <br/>
     * 
     * 更新 所有配置数据
     */
    public void process() {

        //
        // 处理配置文件
        //
        disconfCoreProcessorFile.processAllItems();

        //
        // 处理配置项
        //
        disconfCoreProcessorItem.processAllItems();
    }

    /**
     * 特殊的，将仓库里的数据注入到 配置项、配置文件 的实体中
     */
    public void inject2DisconfInstance() {

        // 配置文件
        disconfCoreProcessorFile.inject2Conf();

        // 配置项
        disconfCoreProcessorItem.inject2Conf();
    }

    @Override
    public void release() {

        if (fetcherMgr != null) {
            fetcherMgr.release();
        }

        if (watchMgr != null) {
            watchMgr.release();
        }
    }
}
