package com.baidu.disconf.client.scan.inner.statically.impl;

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

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import com.baidu.disconf.client.common.constants.SupportFileTypeEnum;
import com.baidu.disconf.client.common.model.DisConfCommonModel;
import com.baidu.disconf.client.common.model.DisconfCenterBaseModel;
import com.baidu.disconf.client.common.model.DisconfCenterFile;
import com.baidu.disconf.client.common.model.DisconfCenterFile.FileItemValue;
import com.baidu.disconf.client.config.DisClientSysConfig;
import com.baidu.disconf.client.scan.inner.statically.StaticScannerMgr;
import com.baidu.disconf.client.scan.inner.statically.model.ScanStaticModel;
import com.baidu.disconf.client.store.DisconfStoreProcessorFactory;
import com.baidu.disconf.client.support.utils.MethodUtils;
import com.baidu.disconf.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf.core.common.path.DisconfWebPathMgr;

/**
 * 配置文件的静态扫描
 *
 * @author liaoqiqi
 * @version 2014-9-9
 */
public class StaticScannerFileMgrImpl extends StaticScannerMgrImplBase implements StaticScannerMgr {

    protected static final Logger LOGGER = LoggerFactory.getLogger(StaticScannerFileMgrImpl.class);

    /**
     *
     */
    @Override
    public void scanData2Store(ScanStaticModel scanModel) {

        // 转换配置文件
        List<DisconfCenterBaseModel> disconfCenterFiles = getDisconfFiles(scanModel);
        DisconfStoreProcessorFactory.getDisconfStoreFileProcessor().transformScanData(disconfCenterFiles);
    }

    /**
     *
     */
    @Override
    public void exclude(Set<String> keySet) {
        DisconfStoreProcessorFactory.getDisconfStoreFileProcessor().exclude(keySet);
    }

    /**
     * 获取配置文件数据
     */
    private static List<DisconfCenterBaseModel> getDisconfFiles(ScanStaticModel scanModel) {

        List<DisconfCenterBaseModel> disconfCenterFiles = new ArrayList<DisconfCenterBaseModel>();

        Set<Class<?>> classSet = scanModel.getDisconfFileClassSet();
        for (Class<?> disconfFile : classSet) {

            Set<Method> methods = scanModel.getDisconfFileItemMap().get(disconfFile);
            if (methods == null) {
                continue;
            }

            DisconfCenterFile disconfCenterFile = transformScanFile(disconfFile, methods);

            disconfCenterFiles.add(disconfCenterFile);
        }

        return disconfCenterFiles;
    }

    /**
     * 转换配置文件
     */
    private static DisconfCenterFile transformScanFile(Class<?> disconfFileClass, Set<Method> methods) {

        DisconfCenterFile disconfCenterFile = new DisconfCenterFile();

        //
        // class
        disconfCenterFile.setCls(disconfFileClass);

        DisconfFile disconfFileAnnotation = disconfFileClass.getAnnotation(DisconfFile.class);

        //
        // file name
        disconfCenterFile.setFileName(disconfFileAnnotation.filename());

        // config file target dir path
        disconfCenterFile.setTargetDirPath(disconfFileAnnotation.targetDirPath().trim());

        // file type
        disconfCenterFile.setSupportFileTypeEnum(SupportFileTypeEnum.getByFileName(disconfFileAnnotation.filename()));

        //
        // disConfCommonModel
        DisConfCommonModel disConfCommonModel =
                makeDisConfCommonModel(disconfFileAnnotation.app(), disconfFileAnnotation.env(), disconfFileAnnotation
                        .version());
        disconfCenterFile.setDisConfCommonModel(disConfCommonModel);

        // Remote URL
        String url = DisconfWebPathMgr.getRemoteUrlParameter(DisClientSysConfig.getInstance().CONF_SERVER_STORE_ACTION,
                disConfCommonModel.getApp(),
                disConfCommonModel.getVersion(),
                disConfCommonModel.getEnv(),
                disconfCenterFile.getFileName(),
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
            Field field = MethodUtils.getFieldFromMethod(method, expectedFields, DisConfigTypeEnum.FILE);
            if (field == null) {
                continue;
            }

            //
            DisconfFileItem disconfFileItem = method.getAnnotation(DisconfFileItem.class);
            String keyName = disconfFileItem.name();

            // access
            field.setAccessible(true);

            // get setter method
            Method setterMethod = MethodUtils.getSetterMethodFromField(disconfFileClass, field);

            // static 则直接获取其值
            if (Modifier.isStatic(field.getModifiers())) {

                try {
                    FileItemValue fileItemValue = new FileItemValue(field.get(null), field, setterMethod);
                    keyMaps.put(keyName, fileItemValue);

                } catch (Exception e) {
                    LOGGER.error(e.toString());
                }

            } else {

                // 非static则为Null, 这里我们没有必要获取其Bean的值
                FileItemValue fileItemValue = new FileItemValue(null, field, setterMethod);
                keyMaps.put(keyName, fileItemValue);
            }
        }

        // 设置
        disconfCenterFile.setKeyMaps(keyMaps);

        return disconfCenterFile;
    }

}
