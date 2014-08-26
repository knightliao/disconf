package com.baidu.disconf.client.core.processor.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.client.common.model.DisConfCommonModel;
import com.baidu.disconf.client.common.model.DisconfCenterItem;
import com.baidu.disconf.client.core.processor.DisconfCoreProcessor;
import com.baidu.disconf.client.fetcher.FetcherMgr;
import com.baidu.disconf.client.store.DisconfStoreProcessor;
import com.baidu.disconf.client.store.DisconfStoreProcessorFactory;
import com.baidu.disconf.client.store.processor.model.DisconfValue;
import com.baidu.disconf.client.watch.WatchMgr;
import com.baidu.disconf.core.common.constants.DisConfigTypeEnum;

/**
 * 配置项处理器实现
 * 
 * @author liaoqiqi
 * @version 2014-8-4
 */
public class DisconfItemCoreProcessorImpl implements DisconfCoreProcessor {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(DisconfItemCoreProcessorImpl.class);

    // 监控器
    private WatchMgr watchMgr = null;

    // 抓取器
    private FetcherMgr fetcherMgr = null;

    // 仓库算子
    private DisconfStoreProcessor disconfStoreProcessor = DisconfStoreProcessorFactory
            .getDisconfStoreItemProcessor();

    public DisconfItemCoreProcessorImpl(WatchMgr watchMgr, FetcherMgr fetcherMgr) {

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

            LOGGER.debug("==============\tstart to process disconf item: "
                    + key + "\t=============================");

            DisconfCenterItem disconfCenterItem = (DisconfCenterItem) disconfStoreProcessor
                    .getConfData(key);
            if (disconfCenterItem != null) {
                try {
                    updateOneConfItem(key, disconfCenterItem);
                } catch (Exception e) {
                    LOGGER.error(e.toString(), e);
                }
            }
        }
    }

    /**
     * 更新 一个配置
     */
    public void updateOneConf(String keyName) throws Exception {

        DisconfCenterItem disconfCenterItem = (DisconfCenterItem) disconfStoreProcessor
                .getConfData(keyName);
        if (disconfCenterItem != null) {
            updateOneConfItem(keyName, disconfCenterItem);
        }
    }

    /**
     * 更新一个配置
     */
    private void updateOneConfItem(String keyName,
            DisconfCenterItem disconfCenterItem) throws Exception {

        if (disconfCenterItem == null) {
            throw new Exception("cannot find disconfCenterItem " + keyName);
        }

        //
        // 下载配置
        //
        String value = null;
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

        //
        // 注入到仓库中
        //
        disconfStoreProcessor.inject2Store(keyName, new DisconfValue(value,
                null));
        LOGGER.debug("inject ok.");

        //
        // Watch
        //
        if (watchMgr != null) {
            DisConfCommonModel disConfCommonModel = disconfStoreProcessor
                    .getCommonModel(keyName);
            watchMgr.watchPath(this, disConfCommonModel, keyName,
                    DisConfigTypeEnum.ITEM, value);
            LOGGER.debug("watch ok.");
        } else {
            LOGGER.warn("cannot monitor {} because watch mgr is null", keyName);
        }
    }

    /**
     *
     */
    @Override
    public void updateOneConfAndCallback(String key) throws Exception {

        // 更新 配置
        updateOneConf(key);

        // 回调
        DisconfCoreProcessUtils.callOneConf(disconfStoreProcessor, key);
    }

    /**
     * 
     */

    /**
     * 
     */
    @Override
    public void inject2Conf() {

        /**
         * 配置ITEM列表处理
         */
        for (String key : disconfStoreProcessor.getConfKeySet()) {

            LOGGER.debug("==============\tstart to inject value to disconf item instance: "
                    + key + "\t=============================");

            DisconfCenterItem disconfCenterItem = (DisconfCenterItem) disconfStoreProcessor
                    .getConfData(key);

            if (disconfCenterItem == null) {
                continue;
            }

            try {

                Object object = null;

                Field field = disconfCenterItem.getField();

                //
                // 静态
                //
                if (Modifier.isStatic(field.getModifiers())) {

                } else {

                    object = DisconfCoreProcessUtils.getSpringBean(field
                            .getDeclaringClass());
                }

                disconfStoreProcessor.inject2Instance(object, key);

            } catch (Exception e) {
                LOGGER.warn(e.toString(), e);
            }
        }
    }

}
