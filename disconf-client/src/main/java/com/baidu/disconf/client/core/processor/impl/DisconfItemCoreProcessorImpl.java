package com.baidu.disconf.client.core.processor.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.client.common.model.DisConfCommonModel;
import com.baidu.disconf.client.common.model.DisconfCenterItem;
import com.baidu.disconf.client.common.update.IDisconfUpdatePipeline;
import com.baidu.disconf.client.config.DisClientConfig;
import com.baidu.disconf.client.core.processor.DisconfCoreProcessor;
import com.baidu.disconf.client.fetcher.FetcherMgr;
import com.baidu.disconf.client.store.DisconfStoreProcessor;
import com.baidu.disconf.client.store.DisconfStoreProcessorFactory;
import com.baidu.disconf.client.store.inner.DisconfCenterStore;
import com.baidu.disconf.client.store.processor.model.DisconfValue;
import com.baidu.disconf.client.support.registry.Registry;
import com.baidu.disconf.client.watch.WatchMgr;
import com.baidu.disconf.core.common.constants.DisConfigTypeEnum;

/**
 * 配置项处理器实现
 *
 * @author liaoqiqi
 * @version 2014-8-4
 */
public class DisconfItemCoreProcessorImpl implements DisconfCoreProcessor {

    protected static final Logger LOGGER = LoggerFactory.getLogger(DisconfItemCoreProcessorImpl.class);

    // 监控器
    private WatchMgr watchMgr = null;

    // 抓取器
    private FetcherMgr fetcherMgr = null;

    // Registry
    private Registry registry = null;

    // 仓库算子
    private DisconfStoreProcessor disconfStoreProcessor = DisconfStoreProcessorFactory.getDisconfStoreItemProcessor();

    public DisconfItemCoreProcessorImpl(WatchMgr watchMgr, FetcherMgr fetcherMgr, Registry registry) {
        this.registry = registry;
        this.fetcherMgr = fetcherMgr;
        this.watchMgr = watchMgr;
    }

    /**
     *
     */
    @Override
    public void processAllItems() {

        /**
         * 配置ITEM列表处理
         */
        for (String key : disconfStoreProcessor.getConfKeySet()) {
            processOneItem(key);
        }
    }

    @Override
    public void processOneItem(String key) {
        LOGGER.debug("==============\tstart to process disconf item: " + key + "\t=============================");

        DisconfCenterItem disconfCenterItem = (DisconfCenterItem) disconfStoreProcessor.getConfData(key);
        if (disconfCenterItem != null) {
            try {
                updateOneConfItem(key, disconfCenterItem);
            } catch (Exception e) {
                LOGGER.error(e.toString(), e);
            }
        }
    }

    /**
     * 更新 一个配置
     */
    private void updateOneConf(String keyName) throws Exception {

        DisconfCenterItem disconfCenterItem = (DisconfCenterItem) disconfStoreProcessor.getConfData(keyName);
        if (disconfCenterItem != null) {

            // 更新仓库
            updateOneConfItem(keyName, disconfCenterItem);

            // 更新实例
            inject2OneConf(keyName, disconfCenterItem);
        }
    }

    /**
     * 更新一个配置
     */
    private void updateOneConfItem(String keyName, DisconfCenterItem disconfCenterItem) throws Exception {

        if (disconfCenterItem == null) {
            throw new Exception("cannot find disconfCenterItem " + keyName);
        }

        String value = null;

        //
        // 开启disconf才需要远程下载, 否则就用默认值
        //
        if (DisClientConfig.getInstance().ENABLE_DISCONF) {
            //
            // 下载配置
            //
            try {
                String url = disconfCenterItem.getRemoteServerUrl();
                value = fetcherMgr.getValueFromServer(url);
                if (value != null) {
                    LOGGER.debug("value: " + value);
                }
            } catch (Exception e) {
                LOGGER.error("cannot use remote configuration: " + keyName, e);
                LOGGER.info("using local variable: " + keyName);
            }
            LOGGER.debug("download ok.");
        }

        //
        // 注入到仓库中
        //
        disconfStoreProcessor.inject2Store(keyName, new DisconfValue(value, null));
        LOGGER.debug("inject ok.");

        //
        // Watch
        //
        if (DisClientConfig.getInstance().ENABLE_DISCONF) {
            if (watchMgr != null) {
                DisConfCommonModel disConfCommonModel = disconfStoreProcessor.getCommonModel(keyName);
                watchMgr.watchPath(this, disConfCommonModel, keyName, DisConfigTypeEnum.ITEM, value);
                LOGGER.debug("watch ok.");
            } else {
                LOGGER.warn("cannot monitor {} because watch mgr is null", keyName);
            }
        }
    }

    /**
     * 更新消息:
     */
    @Override
    public void updateOneConfAndCallback(String key) throws Exception {

        // 更新 配置
        updateOneConf(key);

        // 回调
        DisconfCoreProcessUtils.callOneConf(disconfStoreProcessor, key);
        callUpdatePipeline(key);
    }

    /**
     * @param key
     */
    private void callUpdatePipeline(String key) {

        Object object = disconfStoreProcessor.getConfData(key);
        if (object != null) {
            DisconfCenterItem disconfCenterItem = (DisconfCenterItem) object;

            IDisconfUpdatePipeline iDisconfUpdatePipeline =
                    DisconfCenterStore.getInstance().getiDisconfUpdatePipeline();
            if (iDisconfUpdatePipeline != null) {
                try {
                    iDisconfUpdatePipeline.reloadDisconfItem(key, disconfCenterItem.getValue());
                } catch (Exception e) {
                    LOGGER.error(e.toString(), e);
                }
            }
        }
    }

    /**
     * 某个配置项：注入到实例中
     */
    private void inject2OneConf(String key, DisconfCenterItem disconfCenterItem) {

        if (disconfCenterItem == null) {
            return;
        }

        try {

            Object object = null;

            //
            // 静态
            //
            if (!disconfCenterItem.isStatic()) {

                object = registry.getFirstByType(disconfCenterItem.getDeclareClass(), false, true);
            }

            disconfStoreProcessor.inject2Instance(object, key);

        } catch (Exception e) {
            LOGGER.warn(e.toString(), e);
        }
    }

    /**
     *
     */
    @Override
    public void inject2Conf() {

        /**
         * 配置ITEM列表处理
         */
        for (String key : disconfStoreProcessor.getConfKeySet()) {

            LOGGER.debug("==============\tstart to inject value to disconf item instance: " + key +
                    "\t=============================");

            DisconfCenterItem disconfCenterItem = (DisconfCenterItem) disconfStoreProcessor.getConfData(key);

            inject2OneConf(key, disconfCenterItem);
        }
    }

}
