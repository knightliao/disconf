package com.baidu.disconf2.client.scan.inner;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf2.client.common.annotations.DisconfFile;
import com.baidu.disconf2.client.common.annotations.DisconfItem;
import com.baidu.disconf2.client.common.inter.IDisconfUpdate;
import com.baidu.disconf2.client.common.model.DisConfCommonModel;
import com.baidu.disconf2.client.common.model.DisconfCenterFile;
import com.baidu.disconf2.client.common.model.DisconfCenterFile.FileItemValue;
import com.baidu.disconf2.client.common.model.DisconfCenterItem;
import com.baidu.disconf2.client.config.inner.DisClientConfig;
import com.baidu.disconf2.client.config.inner.DisClientSysConfig;
import com.baidu.disconf2.client.store.DisconfStoreMgr;
import com.baidu.disconf2.client.watch.WatchMgr;
import com.baidu.disconf2.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf2.core.common.path.PathMgr;

/**
 * 
 * Scan模块与Store模块的数据转换
 * 
 * @author liaoqiqi
 * @version 2014-6-9
 */
public class ScanCoreAdapter {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(ScanCoreAdapter.class);

    /**
     * 转换配置文件
     * 
     * @return
     */
    private static DisconfCenterFile transformScanFile(
            Class<?> disconfFileClass, Set<Method> methods) {

        DisconfCenterFile disconfCenterFile = new DisconfCenterFile();

        //
        // class
        disconfCenterFile.setCls(disconfFileClass);

        DisconfFile disconfFileAnnotation = disconfFileClass
                .getAnnotation(DisconfFile.class);

        //
        // file name
        disconfCenterFile.setFileName(disconfFileAnnotation.filename());

        //
        // disConfCommonModel
        DisConfCommonModel disConfCommonModel = makeDisConfCommonModel(
                disconfFileAnnotation.env(), disconfFileAnnotation.version(),
                PathMgr.joinPath(WatchMgr.getInstance()
                        .getClientDisconfFileZooPath(), disconfFileAnnotation
                        .filename()));
        disconfCenterFile.setDisConfCommonModel(disConfCommonModel);

        // Remote URL
        String url = PathMgr.getRemoteUrlParameter(
                DisClientSysConfig.getInstance().CONF_SERVER_STORE_ACTION,
                disConfCommonModel.getApp(), disConfCommonModel.getVersion(),
                disConfCommonModel.getEnv(), disconfCenterFile.getFileName(),
                DisConfigTypeEnum.FILE);
        disconfCenterFile.setRemoteServerUrl(url);

        //
        // KEY & VALUE
        //
        Map<String, FileItemValue> keyMaps = new HashMap<String, FileItemValue>();
        for (Field field : getFieldsFromMethods(methods)) {

            field.setAccessible(true);

            if (Modifier.isStatic(field.getModifiers())) {
                try {

                    FileItemValue fileItemValue = new FileItemValue(
                            field.get(null), field.getType());
                    keyMaps.put(field.getName(), fileItemValue);

                } catch (Exception e) {
                    LOGGER.error(e.toString());
                }
            } else {
                FileItemValue fileItemValue = new FileItemValue(null,
                        field.getType());
                keyMaps.put(field.getName(), fileItemValue);
            }

        }
        disconfCenterFile.setKeyMaps(keyMaps);

        return disconfCenterFile;
    }

    /**
     * 
     * @return
     */
    private static DisConfCommonModel makeDisConfCommonModel(String env,
            String version, String zookeeperUrl) {

        DisConfCommonModel disConfCommonModel = new DisConfCommonModel();

        disConfCommonModel.setApp(DisClientConfig.getInstance().APP);

        if (!env.isEmpty()) {
            disConfCommonModel.setEnv(env);
        } else {
            disConfCommonModel.setEnv(DisClientConfig.getInstance().ENV);
        }

        if (!version.isEmpty()) {
            disConfCommonModel.setVersion(version);
        } else {
            disConfCommonModel
                    .setVersion(DisClientConfig.getInstance().VERSION);
        }

        disConfCommonModel.setZookeeperUrl(zookeeperUrl);

        return disConfCommonModel;
    }

    /**
     * 转换配置项
     * 
     * @return
     */
    private static DisconfCenterItem transformScanFile(Method method) {

        DisconfCenterItem disconfCenterItem = new DisconfCenterItem();

        // field
        Field field = ScanVerify.getFieldFromMethod(method);
        if (field == null) {
            return null;
        }

        // 获取标注
        DisconfItem disconfItem = method.getAnnotation(DisconfItem.class);

        // 去掉空格
        String key = disconfItem.key().replace(" ", "");

        // key
        disconfCenterItem.setKey(key);
        // value
        field.setAccessible(true);
        if (Modifier.isStatic(field.getModifiers())) {
            try {
                disconfCenterItem.setValue(field.get(null));
            } catch (Exception e) {
                LOGGER.error(e.toString());
                return null;
            }
        } else {
            disconfCenterItem.setValue(null);
        }

        // key type
        disconfCenterItem.setKeyType(field.getType());

        //
        // disConfCommonModel
        DisConfCommonModel disConfCommonModel = makeDisConfCommonModel(
                disconfItem.env(), disconfItem.version(), PathMgr.joinPath(
                        WatchMgr.getInstance().getClientDisconfItemZooPath(),
                        key));
        disconfCenterItem.setDisConfCommonModel(disConfCommonModel);

        // Remote URL
        String url = PathMgr.getRemoteUrlParameter(
                DisClientSysConfig.getInstance().CONF_SERVER_STORE_ACTION,
                disConfCommonModel.getApp(), disConfCommonModel.getVersion(),
                disConfCommonModel.getEnv(), key, DisConfigTypeEnum.ITEM);
        disconfCenterItem.setRemoteServerUrl(url);

        return disconfCenterItem;
    }

    /**
     * 转换配置文件
     * 
     * @return
     */
    private static List<DisconfCenterFile> getDisconfFiles(ScanModel scanModel) {

        List<DisconfCenterFile> disconfCenterFiles = new ArrayList<DisconfCenterFile>();

        Set<Class<?>> classSet = scanModel.getDisconfFileClassSet();
        for (Class<?> disconfFile : classSet) {

            Set<Method> fieldSet = scanModel.getDisconfFileItemMap().get(
                    disconfFile);

            DisconfCenterFile disconfCenterFile = transformScanFile(
                    disconfFile, fieldSet);

            disconfCenterFiles.add(disconfCenterFile);
        }

        return disconfCenterFiles;
    }

    /**
     * 转换配置项
     * 
     * @return
     */
    private static List<DisconfCenterItem> getDisconfItems(ScanModel scanModel) {

        List<DisconfCenterItem> disconfCenterItems = new ArrayList<DisconfCenterItem>();

        Set<Method> methods = scanModel.getDisconfItemMethodSet();
        for (Method method : methods) {

            DisconfCenterItem disconfCenterItem = transformScanFile(method);

            if (disconfCenterItem != null) {
                disconfCenterItems.add(disconfCenterItem);
            }
        }

        return disconfCenterItems;
    }

    /**
     * 转换 更新 回调函数
     * 
     * @return
     */
    private static void transformUpdateService(
            Map<String, List<IDisconfUpdate>> disconfUpdateServiceInverseIndexMap) {

        for (String key : disconfUpdateServiceInverseIndexMap.keySet()) {

            // 找不到回调对应的配置，这是用户配置 错误了
            if (!DisconfStoreMgr.getInstance().hasThisConf(key)) {

                StringBuffer sb = new StringBuffer();
                sb.append("cannot find " + key + "for: ");
                for (IDisconfUpdate serClass : disconfUpdateServiceInverseIndexMap
                        .get(key)) {
                    sb.append(serClass.toString() + "\t");
                }
                LOGGER.error(sb.toString());
            } else {

                // 配置正常
                DisconfStoreMgr.getInstance().addUpdateCallbackList(key,
                        disconfUpdateServiceInverseIndexMap.get(key));
            }
        }
    }

    /**
     * 
     * @return
     */
    public static void put2Store(ScanModel scanModel) {

        // 转换配置文件
        List<DisconfCenterFile> disconfCenterFiles = getDisconfFiles(scanModel);
        DisconfStoreMgr.getInstance().transformScanFiles(disconfCenterFiles);

        // 转换配置项
        List<DisconfCenterItem> disconfCenterItems = getDisconfItems(scanModel);
        DisconfStoreMgr.getInstance().transformScanItems(disconfCenterItems);

        //
        transformUpdateService(scanModel
                .getDisconfUpdateServiceInverseIndexMap());
    }

    /**
     * 
     * @return
     */
    private static Set<Field> getFieldsFromMethods(Set<Method> methods) {

        Set<Field> fields = new HashSet<Field>();

        for (Method method : methods) {

            Field field = ScanVerify.getFieldFromMethod(method);
            if (field != null) {
                fields.add(field);
            }
        }

        return fields;
    }

}
