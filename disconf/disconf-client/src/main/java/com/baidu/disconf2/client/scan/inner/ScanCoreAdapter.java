package com.baidu.disconf2.client.scan.inner;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.baidu.disconf2.client.common.model.DisconfCenterItem;
import com.baidu.disconf2.client.config.inner.DisClientConfig;
import com.baidu.disconf2.client.config.inner.DisClientSysConfig;
import com.baidu.disconf2.client.core.DisconfCoreMgr;
import com.baidu.disconf2.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf2.core.common.path.PathMgr;

/**
 * 
 * Scan模块与Core模块的转换
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
            Class<?> disconfFileClass, Set<Field> disconfFileItemFields) {

        DisconfCenterFile disconfCenterFile = new DisconfCenterFile();

        //
        // fields
        disconfCenterFile.setFields(disconfFileItemFields);

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
                disconfFileAnnotation.env(), disconfFileAnnotation.version());
        disconfCenterFile.setDisConfCommonModel(disConfCommonModel);

        // Remote URL
        String url = PathMgr.getRemoteUrlParameter(
                DisClientSysConfig.getInstance().CONF_SERVER_ACTION,
                disConfCommonModel.getApp(), disConfCommonModel.getVersion(),
                disConfCommonModel.getEnv(), disconfCenterFile.getFileName(),
                DisConfigTypeEnum.FILE);
        disconfCenterFile.setRemoteServerUrl(url);

        //
        // KEY & VALUE
        //
        Map<String, Object> keyMaps = new HashMap<String, Object>();
        for (Field field : disconfFileItemFields) {

            field.setAccessible(true);

            if (Modifier.isStatic(field.getModifiers())) {
                try {

                    keyMaps.put(field.getName(), field.get(null));
                } catch (Exception e) {
                    LOGGER.error(e.toString());
                }
            } else {
                keyMaps.put(field.getName(), null);
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
            String version) {

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

        return disConfCommonModel;
    }

    /**
     * 转换配置项
     * 
     * @return
     */
    private static DisconfCenterItem transformScanFile(Field field) {

        DisconfCenterItem disconfCenterItem = new DisconfCenterItem();

        // field
        disconfCenterItem.setField(field);

        DisconfItem disconfItem = field.getAnnotation(DisconfItem.class);

        // key
        disconfCenterItem.setKey(disconfItem.key());
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

        //
        // disConfCommonModel
        DisConfCommonModel disConfCommonModel = makeDisConfCommonModel(
                disconfItem.env(), disconfItem.version());
        disconfCenterItem.setDisConfCommonModel(disConfCommonModel);

        // Remote URL
        String url = PathMgr.getRemoteUrlParameter(
                DisClientSysConfig.getInstance().CONF_SERVER_ACTION,
                disConfCommonModel.getApp(), disConfCommonModel.getVersion(),
                disConfCommonModel.getEnv(), disconfCenterItem.getKey(),
                DisConfigTypeEnum.ITEM);
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

            Set<Field> fieldSet = scanModel.getDisconfFileItemMap().get(
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

        Set<Field> fields = scanModel.getDisconfItemFieldSet();
        for (Field field : fields) {

            DisconfCenterItem disconfCenterItem = transformScanFile(field);

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
            if (!DisconfCoreMgr.getInstance().hasThisConf(key)) {

                StringBuffer sb = new StringBuffer();
                sb.append("cannot find " + key + "for: ");
                for (IDisconfUpdate serClass : disconfUpdateServiceInverseIndexMap
                        .get(key)) {
                    sb.append(serClass.toString() + "\t");
                }
                LOGGER.error(sb.toString());
            } else {

                // 配置正常
                DisconfCoreMgr.getInstance().addUpdateCallbackList(key,
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
        DisconfCoreMgr.getInstance().transformScanFiles(disconfCenterFiles);

        // 转换配置项
        List<DisconfCenterItem> disconfCenterItems = getDisconfItems(scanModel);
        DisconfCoreMgr.getInstance().transformScanItems(disconfCenterItems);

        //
        transformUpdateService(scanModel
                .getDisconfUpdateServiceInverseIndexMap());
    }
}
