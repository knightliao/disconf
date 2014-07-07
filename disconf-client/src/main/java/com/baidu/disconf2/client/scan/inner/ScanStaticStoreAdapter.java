package com.baidu.disconf2.client.scan.inner;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
import com.baidu.disconf2.client.common.model.DisconfCenterFile.FileItemValue;
import com.baidu.disconf2.client.common.model.DisconfCenterItem;
import com.baidu.disconf2.client.config.inner.DisClientConfig;
import com.baidu.disconf2.client.config.inner.DisClientSysConfig;
import com.baidu.disconf2.client.scan.inner.model.ScanStaticModel;
import com.baidu.disconf2.client.store.DisconfStoreMgr;
import com.baidu.disconf2.client.watch.WatchMgr;
import com.baidu.disconf2.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf2.core.common.path.DisconfWebPathMgr;
import com.baidu.disconf2.core.common.path.ZooPathMgr;

/**
 * 
 * Static Scan模块与Store模块的数据转换
 * 
 * @author liaoqiqi
 * @version 2014-6-9
 */
public class ScanStaticStoreAdapter {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(ScanStaticStoreAdapter.class);

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
                ZooPathMgr.joinPath(WatchMgr.getInstance()
                        .getClientDisconfFileZooPath(), disconfFileAnnotation
                        .filename()));
        disconfCenterFile.setDisConfCommonModel(disConfCommonModel);

        // Remote URL
        String url = DisconfWebPathMgr.getRemoteUrlParameter(
                DisClientSysConfig.getInstance().CONF_SERVER_STORE_ACTION,
                disConfCommonModel.getApp(), disConfCommonModel.getVersion(),
                disConfCommonModel.getEnv(), disconfCenterFile.getFileName(),
                DisConfigTypeEnum.FILE);
        disconfCenterFile.setRemoteServerUrl(url);

        // fields
        Field[] expectedFields = disconfFileClass.getDeclaredFields();

        //
        // KEY & VALUE
        //
        Map<String, FileItemValue> keyMaps = new HashMap<String, FileItemValue>();

        for (Method method : methods) {

            // 获取指定的域
            Field field = ScanVerify.getFieldFromMethod(method, expectedFields,
                    DisConfigTypeEnum.FILE);
            if (field == null) {
                continue;
            }

            //
            DisconfFileItem disconfFileItem = method
                    .getAnnotation(DisconfFileItem.class);
            String keyName = disconfFileItem.name();

            // access
            field.setAccessible(true);

            // static 则直接获取其值
            if (Modifier.isStatic(field.getModifiers())) {
                try {

                    FileItemValue fileItemValue = new FileItemValue(
                            field.get(null), field);
                    keyMaps.put(keyName, fileItemValue);

                } catch (Exception e) {
                    LOGGER.error(e.toString());
                }

            } else {

                // 非static则为Null, 这里我们没有必要获取其Bean的值
                FileItemValue fileItemValue = new FileItemValue(null, field);
                keyMaps.put(keyName, fileItemValue);
            }
        }

        // 设置
        disconfCenterFile.setKeyMaps(keyMaps);

        return disconfCenterFile;
    }

    /**
     * env/version 默认是应用整合设置的，但用户可以在配置中更改它
     * 
     * @return
     */
    private static DisConfCommonModel makeDisConfCommonModel(String env,
            String version, String zookeeperUrl) {

        DisConfCommonModel disConfCommonModel = new DisConfCommonModel();

        // app
        disConfCommonModel.setApp(DisClientConfig.getInstance().APP);

        // env
        if (!env.isEmpty()) {
            disConfCommonModel.setEnv(env);
        } else {
            disConfCommonModel.setEnv(DisClientConfig.getInstance().ENV);
        }

        // version
        if (!version.isEmpty()) {
            disConfCommonModel.setVersion(version);
        } else {
            disConfCommonModel
                    .setVersion(DisClientConfig.getInstance().VERSION);
        }

        // 设置Zookeeper的URL
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

        // class
        Class<?> cls = method.getDeclaringClass();

        // fields
        Field[] expectedFields = cls.getDeclaredFields();

        // field
        Field field = ScanVerify.getFieldFromMethod(method, expectedFields,
                DisConfigTypeEnum.ITEM);

        if (field == null) {
            return null;
        }

        // 获取标注
        DisconfItem disconfItem = method.getAnnotation(DisconfItem.class);

        // 去掉空格
        String key = disconfItem.key().replace(" ", "");

        // field
        disconfCenterItem.setField(field);

        // key
        disconfCenterItem.setKey(key);

        // access
        field.setAccessible(true);

        // object
        disconfCenterItem.setObject(null);

        // value
        if (Modifier.isStatic(field.getModifiers())) {
            try {
                disconfCenterItem.setValue(field.get(null));
            } catch (Exception e) {
                LOGGER.error(e.toString());
                disconfCenterItem.setValue(null);
            }
        } else {
            disconfCenterItem.setValue(null);
        }

        //
        // disConfCommonModel
        DisConfCommonModel disConfCommonModel = makeDisConfCommonModel(
                disconfItem.env(), disconfItem.version(), ZooPathMgr.joinPath(
                        WatchMgr.getInstance().getClientDisconfItemZooPath(),
                        key));
        disconfCenterItem.setDisConfCommonModel(disConfCommonModel);

        // Disconf-web url
        String url = DisconfWebPathMgr.getRemoteUrlParameter(
                DisClientSysConfig.getInstance().CONF_SERVER_STORE_ACTION,
                disConfCommonModel.getApp(), disConfCommonModel.getVersion(),
                disConfCommonModel.getEnv(), key, DisConfigTypeEnum.ITEM);
        disconfCenterItem.setRemoteServerUrl(url);

        return disconfCenterItem;
    }

    /**
     * 获取配置文件数据
     * 
     * @return
     */
    private static List<DisconfCenterFile> getDisconfFiles(
            ScanStaticModel scanModel) {

        List<DisconfCenterFile> disconfCenterFiles = new ArrayList<DisconfCenterFile>();

        Set<Class<?>> classSet = scanModel.getDisconfFileClassSet();
        for (Class<?> disconfFile : classSet) {

            Set<Method> methods = scanModel.getDisconfFileItemMap().get(
                    disconfFile);

            DisconfCenterFile disconfCenterFile = transformScanFile(
                    disconfFile, methods);

            disconfCenterFiles.add(disconfCenterFile);
        }

        return disconfCenterFiles;
    }

    /**
     * 转换配置项
     * 
     * @return
     */
    private static List<DisconfCenterItem> getDisconfItems(
            ScanStaticModel scanModel) {

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
     * 将数据放入到仓库中
     * 
     * @return
     */
    public static void put2Store(ScanStaticModel scanModel) {

        // 转换配置文件
        List<DisconfCenterFile> disconfCenterFiles = getDisconfFiles(scanModel);
        DisconfStoreMgr.getInstance().transformScanFiles(disconfCenterFiles);

        // 转换配置项
        List<DisconfCenterItem> disconfCenterItems = getDisconfItems(scanModel);
        DisconfStoreMgr.getInstance().transformScanItems(disconfCenterItems);
    }

}
