package com.baidu.disconf2.client.core;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf2.client.common.inter.IDisconfUpdate;
import com.baidu.disconf2.client.common.model.DisconfCenterFile;
import com.baidu.disconf2.client.common.model.DisconfCenterItem;
import com.baidu.disconf2.client.common.model.DisconfCommonCallbackModel;
import com.baidu.disconf2.client.fetcher.FetcherMgr;
import com.baidu.disconf2.client.store.DisconfStoreMgr;
import com.baidu.disconf2.client.watch.WatchMgr;
import com.baidu.disconf2.core.common.constants.DisConfigTypeEnum;
import com.baidu.utils.ConfigLoaderUtils;

/**
 * 管理 下载、注入、Watch三模块
 * 
 * @author liaoqiqi
 * @version 2014-6-10
 */
public class DisconfCoreMgr {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(DisconfCoreMgr.class);

    /**
     * 1. 获取远程的所有配置数据<br/>
     * 2. 注入到仓库中<br/>
     * 3. Watch
     */
    public static void init() {

        //
        // 处理配置文件
        //
        updateConfFile();

        //
        // 处理配置项
        //
        updateConfItem();
    }

    /**
     * 更新 配置
     */
    private static void updateConfItem() {

        Map<String, DisconfCenterItem> confItemMap = DisconfStoreMgr
                .getInstance().getConfItemMap();

        /**
         * 配置ITEM列表处理
         */
        for (String key : confItemMap.keySet()) {

            LOGGER.info("==============\tstart to process disconf item: " + key
                    + "\t=============================");

            DisconfCenterItem disconfCenterItem = confItemMap.get(key);

            try {
                updateOneConfItem(key, disconfCenterItem);
            } catch (Exception e) {
                LOGGER.error(e.toString(), e);
            }
        }
    }

    /**
     * 更新 配置
     */
    public static void updateOneConfItem(String keyName) throws Exception {

        Map<String, DisconfCenterItem> confItemMap = DisconfStoreMgr
                .getInstance().getConfItemMap();

        DisconfCenterItem disconfCenterItem = confItemMap.get(keyName);

        updateOneConfItem(keyName, disconfCenterItem);
    }

    /**
     * 更新 配置
     */
    private static void updateOneConfItem(String keyName,
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
            value = FetcherMgr.getValueFromServer(url);
            if (value != null) {
                LOGGER.info("value: " + value);
            }
        } catch (Exception e) {
            throw new Exception("cannot use remote configuration: " + keyName,
                    e);
        }
        LOGGER.info("download ok.");

        //
        // 注入到仓库中
        //
        DisconfStoreMgr.getInstance().injectItem2Store(keyName, value);
        LOGGER.info("inject ok.");

        //
        // Watch
        //
        String zooUrl = DisconfStoreMgr.getInstance().getItemZooPath(keyName);
        WatchMgr.getInstance().watchPath(zooUrl, keyName,
                DisConfigTypeEnum.ITEM);
        LOGGER.info("watch ok.");
    }

    /**
     * 调用此配置影响的回调函数
     */
    public static void callOneConfItem(String keyName) throws Exception {

        Map<String, DisconfCenterItem> confItemMap = DisconfStoreMgr
                .getInstance().getConfItemMap();

        DisconfCenterItem disconfCenterItem = confItemMap.get(keyName);
        if (disconfCenterItem == null) {
            throw new Exception("cannot find disconfCenterItem " + keyName);
        }

        //
        // 获取回调函数列表
        //
        DisconfCommonCallbackModel disconfCommonCallbackModel = disconfCenterItem
                .getDisconfCommonCallbackModel();
        callFunctions(disconfCommonCallbackModel);
    }

    /**
     * 更新 配置文件
     */
    private static void updateConfFile() {

        Map<String, DisconfCenterFile> disConfCenterFileMap = DisconfStoreMgr
                .getInstance().getConfFileMap();

        /**
         * 配置文件列表处理
         */
        for (String fileName : disConfCenterFileMap.keySet()) {

            LOGGER.info("==============\tstart to process disconf file: "
                    + fileName + "\t=============================");

            DisconfCenterFile disconfCenterFile = disConfCenterFileMap
                    .get(fileName);

            try {
                updateOneConfFile(fileName, disconfCenterFile);
            } catch (Exception e) {
                LOGGER.error(e.toString(), e);
            }
        }

    }

    /**
     * 更新 配置文件
     */
    private static void updateOneConfFile(String fileName,
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
            filePath = FetcherMgr.downloadFileFromServer(url, fileName);

            // 读取配置
            properties = ConfigLoaderUtils.loadConfig(filePath);

        } catch (Exception e) {
            throw new Exception("cannot use remote configuration: " + fileName,
                    e);
        }
        LOGGER.info("download ok.");

        //
        // 注入到仓库中
        //
        Set<Entry<Object, Object>> set = properties.entrySet();
        for (Entry<Object, Object> entry : set) {
            LOGGER.info(entry.toString());
        }
        DisconfStoreMgr.getInstance().injectFile2Store(fileName, properties);
        LOGGER.info("inject ok.");

        //
        // Watch
        //
        String zooUrl = DisconfStoreMgr.getInstance().getFileZooPath(fileName);
        WatchMgr.getInstance().watchPath(zooUrl, fileName,
                DisConfigTypeEnum.FILE);
        LOGGER.info("watch ok.");
    }

    /**
     * 更新 配置文件
     */
    public static void updateOneConfFile(String fileName) throws Exception {

        Map<String, DisconfCenterFile> disConfCenterFileMap = DisconfStoreMgr
                .getInstance().getConfFileMap();

        DisconfCenterFile disconfCenterFile = disConfCenterFileMap
                .get(fileName);

        updateOneConfFile(fileName, disconfCenterFile);
    }

    /**
     * 调用此配置文件影响的回调函数
     */
    public static void callOneConfFile(String fileName) throws Exception {

        Map<String, DisconfCenterFile> disConfCenterFileMap = DisconfStoreMgr
                .getInstance().getConfFileMap();

        DisconfCenterFile disconfCenterFile = disConfCenterFileMap
                .get(fileName);
        if (disconfCenterFile == null) {
            throw new Exception("cannot find disconfCenterFile " + fileName);
        }

        //
        // 获取回调函数列表
        //
        DisconfCommonCallbackModel disconfCommonCallbackModel = disconfCenterFile
                .getDisconfCommonCallbackModel();
        callFunctions(disconfCommonCallbackModel);
    }

    /**
     * 进行回调
     * 
     * @param disconfCommonCallbackModel
     */
    private static void callFunctions(
            DisconfCommonCallbackModel disconfCommonCallbackModel) {

        List<IDisconfUpdate> iDisconfUpdates = disconfCommonCallbackModel
                .getDisconfConfUpdates();

        // CALL
        for (IDisconfUpdate iDisconfUpdate : iDisconfUpdates) {

            if (iDisconfUpdate != null) {

                LOGGER.info("start to call " + iDisconfUpdate.getClass());

                try {

                    iDisconfUpdate.reload();

                } catch (Exception e) {

                    LOGGER.error(e.toString(), e);
                }
            }
        }
    }

}
