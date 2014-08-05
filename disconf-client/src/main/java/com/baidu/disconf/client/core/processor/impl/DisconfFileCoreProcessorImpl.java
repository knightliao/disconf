package com.baidu.disconf.client.core.processor.impl;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.client.common.model.DisConfCommonModel;
import com.baidu.disconf.client.common.model.DisconfCenterFile;
import com.baidu.disconf.client.common.model.DisconfCenterFile.FileItemValue;
import com.baidu.disconf.client.core.processor.DisconfCoreProcessor;
import com.baidu.disconf.client.fetcher.FetcherMgr;
import com.baidu.disconf.client.store.DisconfStoreProcessor;
import com.baidu.disconf.client.store.DisconfStoreProcessorFactory;
import com.baidu.disconf.client.store.processor.model.DisconfValue;
import com.baidu.disconf.client.watch.WatchMgr;
import com.baidu.disconf.core.common.constants.DisConfigTypeEnum;
import com.baidu.ub.common.utils.ConfigLoaderUtils;
import com.baidu.ub.common.utils.GsonUtils;

/**
 * 配置文件处理器实现
 * 
 * @author liaoqiqi
 * @version 2014-8-4
 */
public class DisconfFileCoreProcessorImpl implements DisconfCoreProcessor {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(DisconfFileCoreProcessorImpl.class);

    // 监控器
    private WatchMgr watchMgr = null;

    // 抓取器
    private FetcherMgr fetcherMgr = null;

    // 仓库算子
    private DisconfStoreProcessor disconfStoreProcessor = DisconfStoreProcessorFactory
            .getDisconfStoreFileProcessor();

    public DisconfFileCoreProcessorImpl(WatchMgr watchMgr, FetcherMgr fetcherMgr) {

        this.fetcherMgr = fetcherMgr;
        this.watchMgr = watchMgr;
    }

    @Override
    public void processAllItems() {

        /**
         * 配置文件列表处理
         */
        for (String fileName : disconfStoreProcessor.getConfKeySet()) {

            LOGGER.info("==============\tstart to process disconf file: "
                    + fileName + "\t=============================");

            DisconfCenterFile disconfCenterFile = (DisconfCenterFile) disconfStoreProcessor
                    .getConfData(fileName);

            try {
                updateOneConfFile(fileName, disconfCenterFile);
            } catch (Exception e) {
                LOGGER.error(e.toString(), e);
            }
        }
    }

    /**
     * 更新 一個配置文件
     */
    private void updateOneConfFile(String fileName,
            DisconfCenterFile disconfCenterFile) throws Exception {

        if (disconfCenterFile == null) {
            throw new Exception("cannot find disconfCenterFile " + fileName);
        }

        //
        // 下载配置
        //
        String filePath = "";
        Properties properties = null;
        try {

            String url = disconfCenterFile.getRemoteServerUrl();
            filePath = fetcherMgr.downloadFileFromServer(url, fileName);

            // 读取配置
            properties = ConfigLoaderUtils.loadConfig(filePath);

        } catch (Exception e) {

            //
            // 下载失败了, 尝试使用本地的配置
            //

            LOGGER.error(e.toString());
            LOGGER.info("using local properties in class path: " + fileName);

            // 读取配置
            properties = ConfigLoaderUtils.loadConfig(fileName);
        }
        LOGGER.debug("download ok.");

        //
        // 注入到仓库中
        //
        Set<Entry<Object, Object>> set = properties.entrySet();
        for (Entry<Object, Object> entry : set) {
            LOGGER.info(entry.toString());
        }
        disconfStoreProcessor.inject2Store(fileName, new DisconfValue(null,
                properties));
        LOGGER.debug("inject ok.");

        //
        // Watch
        //
        DisConfCommonModel disConfCommonModel = disconfStoreProcessor
                .getCommonModel(fileName);
        if (watchMgr != null) {
            watchMgr.watchPath(this, disConfCommonModel, fileName,
                    DisConfigTypeEnum.FILE,
                    GsonUtils.toJson(disconfCenterFile.getKV()));
            LOGGER.debug("watch ok.");
        } else {
            LOGGER.warn("cannot monitor {} because watch mgr is null", fileName);
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
    @Override
    public void updateOneConf(String fileName) throws Exception {

        DisconfCenterFile disconfCenterFile = (DisconfCenterFile) disconfStoreProcessor
                .getConfData(fileName);

        if (disconfCenterFile != null) {
            updateOneConfFile(fileName, disconfCenterFile);
        }
    }

    @Override
    public void inject2Conf() {

        /**
         * 配置文件列表处理
         */
        for (String key : disconfStoreProcessor.getConfKeySet()) {

            LOGGER.info("==============\tstart to inject value to disconf file item instance: "
                    + key + "\t=============================");

            DisconfCenterFile disconfCenterFile = (DisconfCenterFile) disconfStoreProcessor
                    .getConfData(key);

            if (disconfCenterFile == null) {
                continue;
            }

            Map<String, FileItemValue> fileItemValueMap = disconfCenterFile
                    .getKeyMaps();

            for (String itemKey : fileItemValueMap.keySet()) {

                try {

                    FileItemValue fileItemValue = fileItemValueMap.get(itemKey);

                    // 设置Object实体(只注入一次哦)
                    Object object = disconfCenterFile.getObject();
                    if (object == null) {
                        object = DisconfCoreProcessUtils
                                .getSpringBean(fileItemValue.getField()
                                        .getDeclaringClass());
                    }

                    // 注入实体中
                    if (object != null) {
                        disconfStoreProcessor.inject2Instance(object, key);
                    }

                } catch (Exception e) {
                    LOGGER.warn(e.toString(), e);
                }
            }
        }
    }
}
