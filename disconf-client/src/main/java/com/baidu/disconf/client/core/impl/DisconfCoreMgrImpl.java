package com.baidu.disconf.client.core.impl;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.client.common.inter.IDisconfUpdate;
import com.baidu.disconf.client.common.model.DisConfCommonModel;
import com.baidu.disconf.client.common.model.DisconfCenterFile;
import com.baidu.disconf.client.common.model.DisconfCenterFile.FileItemValue;
import com.baidu.disconf.client.common.model.DisconfCenterItem;
import com.baidu.disconf.client.common.model.DisconfCommonCallbackModel;
import com.baidu.disconf.client.core.DisconfCoreMgr;
import com.baidu.disconf.client.core.watch.WatchMgr;
import com.baidu.disconf.client.fetcher.FetcherMgr;
import com.baidu.disconf.client.store.DisconfStoreMgr;
import com.baidu.disconf.client.utils.MyBeanUtils;
import com.baidu.disconf.client.utils.SpringContextUtil;
import com.baidu.disconf.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf.core.utils.ConfigLoaderUtils;
import com.baidu.disconf.core.utils.GsonUtils;

/**
 * 管理 下载、注入、Watch三模块
 * 
 * @author liaoqiqi
 * @version 2014-6-10
 */
public class DisconfCoreMgrImpl implements DisconfCoreMgr {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(DisconfCoreMgrImpl.class);

    private WatchMgr watchMgr = null;

    private FetcherMgr fetcherMgr = null;

    public DisconfCoreMgrImpl(WatchMgr watchMgr, FetcherMgr fetcherMgr) {

        this.fetcherMgr = fetcherMgr;
        this.watchMgr = watchMgr;
    }

    /**
     * 1. 获取远程的所有配置数据<br/>
     * 2. 注入到仓库中<br/>
     * 3. Watch 配置
     */
    public void process() {

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
     * 特殊的，将数据注入到 配置项、配置文件 的实体中
     */
    public void inject2DisconfInstance() {

        // 配置项
        inject2DisconfItmes();

        // 配置文件
        inject2DisconfFileItmes();
    }

    /**
     * 特殊的，将数据注入到配置项实体中
     */
    private static void inject2DisconfItmes() {

        Map<String, DisconfCenterItem> confItemMap = DisconfStoreMgr
                .getInstance().getConfItemMap();

        /**
         * 配置ITEM列表处理
         */
        for (String key : confItemMap.keySet()) {

            LOGGER.info("==============\tstart to inject value to disconf item instance: "
                    + key + "\t=============================");

            DisconfCenterItem disconfCenterItem = confItemMap.get(key);

            try {

                Object object = null;

                Field field = disconfCenterItem.getField();

                object = getSpringBean(field.getDeclaringClass());
                if (object != null) {
                    disconfCenterItem.setObject(object);
                    DisconfStoreMgr.getInstance().injectItem2Instance(key);
                }

            } catch (Exception e) {
                LOGGER.warn(e.toString(), e);
            }
        }
    }

    /**
     * 特殊的，将数据注入到配置文件实体中
     */
    private static void inject2DisconfFileItmes() {

        Map<String, DisconfCenterFile> confFileMap = DisconfStoreMgr
                .getInstance().getConfFileMap();

        /**
         * 配置ITEM列表处理
         */
        for (String key : confFileMap.keySet()) {

            LOGGER.info("==============\tstart to inject value to disconf file item instance: "
                    + key + "\t=============================");

            DisconfCenterFile disconfCenterFile = confFileMap.get(key);

            Map<String, FileItemValue> fileItemValueMap = disconfCenterFile
                    .getKeyMaps();

            for (String itemKey : fileItemValueMap.keySet()) {

                try {

                    FileItemValue fileItemValue = fileItemValueMap.get(itemKey);

                    // 设置Object实体(只注入一次哦)
                    Object object = disconfCenterFile.getObject();
                    if (object == null) {
                        object = getSpringBean(fileItemValue.getField()
                                .getDeclaringClass());
                        disconfCenterFile.setObject(object);
                    }

                    // 注入实体中
                    if (object != null) {
                        DisconfStoreMgr.getInstance().injectFileItem2Instance(
                                key);
                    }

                } catch (Exception e) {
                    LOGGER.warn(e.toString(), e);
                }
            }
        }
    }

    /**
     * 更新 配置
     */
    private void updateConfItem() {

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
    public void updateOneConfItem(String keyName) throws Exception {

        Map<String, DisconfCenterItem> confItemMap = DisconfStoreMgr
                .getInstance().getConfItemMap();

        DisconfCenterItem disconfCenterItem = confItemMap.get(keyName);

        updateOneConfItem(keyName, disconfCenterItem);
    }

    /**
     * 更新 配置
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
                LOGGER.info("value: " + value);
            }
        } catch (Exception e) {
            throw new Exception("cannot use remote configuration: " + keyName,
                    e);
        }
        LOGGER.debug("download ok.");

        //
        // 注入到仓库中
        //
        DisconfStoreMgr.getInstance().injectItem2Store(keyName, value);
        LOGGER.debug("inject ok.");

        //
        // Watch
        //
        DisConfCommonModel disConfCommonModel = DisconfStoreMgr.getInstance()
                .getItemCommonModel(keyName);
        watchMgr.watchPath(this, disConfCommonModel, keyName,
                DisConfigTypeEnum.ITEM, value);
        LOGGER.debug("watch ok.");
    }

    /**
     * 调用此配置影响的回调函数
     */
    public void callOneConfItem(String keyName) throws Exception {

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
    private void updateConfFile() {

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
            throw new Exception("cannot use remote configuration: " + fileName,
                    e);
        }
        LOGGER.debug("download ok.");

        //
        // 注入到仓库中
        //
        Set<Entry<Object, Object>> set = properties.entrySet();
        for (Entry<Object, Object> entry : set) {
            LOGGER.info(entry.toString());
        }
        DisconfStoreMgr.getInstance().injectFile2Store(fileName, properties);
        LOGGER.debug("inject ok.");

        //
        // Watch
        //
        DisConfCommonModel disConfCommonModel = DisconfStoreMgr.getInstance()
                .getFileCommonModel(fileName);
        watchMgr.watchPath(this, disConfCommonModel, fileName,
                DisConfigTypeEnum.FILE,
                GsonUtils.toJson(disconfCenterFile.getKV()));
        LOGGER.debug("watch ok.");
    }

    /**
     * 更新 配置文件
     */
    public void updateOneConfFile(String fileName) throws Exception {

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

    /**
     * 获取Spring Bean
     * 
     * @return
     */
    private static Object getSpringBean(Class<?> cls) throws Exception {

        if (SpringContextUtil.getApplicationContext() == null) {
            LOGGER.error("Spring Context is null. Cannot autowire "
                    + cls.getCanonicalName());
            return null;
        }

        // spring 方式
        Object object = SpringContextUtil.getBean(cls);

        return MyBeanUtils.getTargetObject(object, cls);
    }

    @Override
    public void release() {

        fetcherMgr.release();
        watchMgr.release();
    }
}
