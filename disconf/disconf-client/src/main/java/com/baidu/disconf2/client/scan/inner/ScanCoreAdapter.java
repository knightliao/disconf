package com.baidu.disconf2.client.scan.inner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf2.client.common.annotations.DisconfFile;
import com.baidu.disconf2.client.common.annotations.DisconfFileItem;
import com.baidu.disconf2.client.common.annotations.DisconfItem;
import com.baidu.disconf2.client.common.model.DisConfCommonModel;
import com.baidu.disconf2.client.common.model.DisconfCenterFile;
import com.baidu.disconf2.client.common.model.DisconfCenterItem;
import com.baidu.disconf2.client.config.inner.DisClientConfig;
import com.baidu.disconf2.client.core.DisconfCoreMgr;

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

        //
        // KEY & VALUE
        //
        Map<String, String> keyMaps = new HashMap<String, String>();
        for (Field field : disconfFileItemFields) {

            DisconfFileItem disconfFileItem = field
                    .getAnnotation(DisconfFileItem.class);

            keyMaps.put(disconfFileItem.key(), disconfFileItem.defaultValue());
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
        disconfCenterItem.setValue(disconfItem.version());

        //
        // disConfCommonModel
        DisConfCommonModel disConfCommonModel = makeDisConfCommonModel(
                disconfItem.env(), disconfItem.version());
        disconfCenterItem.setDisConfCommonModel(disConfCommonModel);

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

            disconfCenterItems.add(disconfCenterItem);
        }

        return disconfCenterItems;
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

    }
}
