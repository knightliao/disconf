package com.baidu.disconf.client.store.processor.impl;

import static com.baidu.disconf.client.store.inner.DisconfCenterStore.getInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.client.addons.properties.ReloadConfigurationMonitor;
import com.baidu.disconf.client.common.constants.SupportFileTypeEnum;
import com.baidu.disconf.client.common.model.DisConfCommonModel;
import com.baidu.disconf.client.common.model.DisconfCenterBaseModel;
import com.baidu.disconf.client.common.model.DisconfCenterFile;
import com.baidu.disconf.client.common.model.DisconfCenterFile.FileItemValue;
import com.baidu.disconf.client.common.update.IDisconfUpdate;
import com.baidu.disconf.client.store.DisconfStoreProcessor;
import com.baidu.disconf.client.store.processor.model.DisconfValue;

/**
 * 配置文件仓库实现器
 *
 * @author liaoqiqi
 * @version 2014-8-4
 */
public class DisconfStoreFileProcessorImpl implements DisconfStoreProcessor {

    protected static final Logger LOGGER = LoggerFactory.getLogger(DisconfStoreFileProcessorImpl.class);

    /**
     *
     */
    @Override
    public void addUpdateCallbackList(String keyName, List<IDisconfUpdate> iDisconfUpdateList) {

        if (getInstance().getConfFileMap().containsKey(keyName)) {

            getInstance().getConfFileMap().get(keyName).getDisconfCommonCallbackModel().getDisconfConfUpdates()
                    .addAll(iDisconfUpdateList);
        }
    }

    /**
     *
     */
    @Override
    public List<IDisconfUpdate> getUpdateCallbackList(String keyName) {

        if (getInstance().getConfFileMap().containsKey(keyName)) {

            return getInstance().getConfFileMap().get(keyName).getDisconfCommonCallbackModel().getDisconfConfUpdates();
        }

        return new ArrayList<IDisconfUpdate>();
    }

    /**
     *
     */
    @Override
    public DisConfCommonModel getCommonModel(String keyName) {

        DisconfCenterFile disconfCenterFile = getInstance().getConfFileMap().get(keyName);

        // 校验是否存在
        if (disconfCenterFile == null) {
            LOGGER.error("cannot find " + keyName + " in store....");
            return null;
        }

        return disconfCenterFile.getDisConfCommonModel();
    }

    /**
     *
     */
    @Override
    public boolean hasThisConf(String keyName) {

        // 配置文件
        if (!getInstance().getConfFileMap().containsKey(keyName)) {
            return false;
        }
        return true;
    }

    /**
     *
     */
    @Override
    public void inject2Instance(Object object, String fileName) {

        DisconfCenterFile disconfCenterFile = getInstance().getConfFileMap().get(fileName);

        // 校验是否存在
        if (disconfCenterFile == null) {
            LOGGER.error("cannot find " + fileName + " in store....");
            return;
        }

        //
        // 静态类
        //
        if (object != null) {
            // 设置object
            disconfCenterFile.setObject(object);
        }

        // 根据类型设置值
        //
        // 注入实体
        //
        Map<String, FileItemValue> keMap = disconfCenterFile.getKeyMaps();
        for (String fileItem : keMap.keySet()) {

            // 根据类型设置值
            try {

                //
                // 静态类
                //
                if (object == null) {

                    if (keMap.get(fileItem).isStatic()) {
                        LOGGER.debug(fileItem + " is a static field. ");
                        keMap.get(fileItem).setValue4StaticFileItem(keMap.get(fileItem).getValue());
                    }

                    //
                    // 非静态类
                    //
                } else {

                    LOGGER.debug(fileItem + " is a non-static field. ");

                    if (keMap.get(fileItem).getValue() == null) {

                        // 如果仓库值为空，则实例 直接使用默认值
                        Object defaultValue = keMap.get(fileItem).getFieldDefaultValue(object);
                        keMap.get(fileItem).setValue(defaultValue);

                    } else {

                        // 如果仓库里的值为非空，则实例使用仓库里的值
                        keMap.get(fileItem).setValue4FileItem(object, keMap.get(fileItem).getValue());
                    }
                }

            } catch (Exception e) {
                LOGGER.error("inject2Instance fileName " + fileName + " " + e.toString(), e);
            }
        }
    }

    /**
     *
     */
    @Override
    public Object getConfig(String fileName, String keyName) {

        DisconfCenterFile disconfCenterFile = getInstance().getConfFileMap().get(fileName);

        // 校验是否存在
        if (disconfCenterFile == null) {
            LOGGER.debug("cannot find " + fileName + " in store....");
            return null;
        }

        if (disconfCenterFile.getKeyMaps().get(keyName) == null) {
            LOGGER.debug("cannot find " + fileName + ", " + keyName + " in store....");
            return null;
        }

        return disconfCenterFile.getKeyMaps().get(keyName).getValue();
    }

    /**
     *
     */
    @Override
    public void inject2Store(String fileName, DisconfValue disconfValue) {

        DisconfCenterFile disconfCenterFile = getInstance().getConfFileMap().get(fileName);

        // 校验是否存在
        if (disconfCenterFile == null) {
            LOGGER.error("cannot find " + fileName + " in store....");
            return;
        }

        if (disconfValue == null || disconfValue.getFileData() == null) {
            LOGGER.error("value is null for {}", fileName);
            return;
        }

        // 存储
        Map<String, FileItemValue> keMap = disconfCenterFile.getKeyMaps();
        if (keMap.size() > 0) {
            for (String fileItem : keMap.keySet()) {

                Object object = disconfValue.getFileData().get(fileItem);
                if (object == null) {
                    LOGGER.error("cannot find {} to be injected. file content is: {}", fileItem,
                            disconfValue.getFileData().toString());
                    continue;
                }

                // 根据类型设置值
                try {

                    Object value = keMap.get(fileItem).getFieldValueByType(object);
                    keMap.get(fileItem).setValue(value);

                } catch (Exception e) {
                    LOGGER.error("inject2Store filename: " + fileName + " " + e.toString(), e);
                }
            }
        }

        // 使用过 XML式配置
        if (disconfCenterFile.isTaggedWithNonAnnotationFile()) {

            if (disconfCenterFile.getSupportFileTypeEnum().equals(SupportFileTypeEnum.PROPERTIES)) {
                // 如果是采用XML进行配置的，则需要利用spring的reload将数据reload到bean里
                ReloadConfigurationMonitor.reload();
            }
            disconfCenterFile.setAdditionalKeyMaps(disconfValue.getFileData());
        }
    }

    /**
     *
     */
    @Override
    public void transformScanData(List<DisconfCenterBaseModel> disconfCenterBaseModels) {

        for (DisconfCenterBaseModel disconfCenterFile : disconfCenterBaseModels) {
            transformScanData(disconfCenterFile);
        }
    }

    /**
     *
     */
    @Override
    public void transformScanData(DisconfCenterBaseModel disconfCenterBaseModel) {
        getInstance().storeOneFile(disconfCenterBaseModel);
    }

    /**
     *
     */
    @Override
    public DisconfCenterBaseModel getConfData(String key) {

        if (getInstance().getConfFileMap().containsKey(key)) {

            return getInstance().getConfFileMap().get(key);

        } else {

            return null;
        }
    }

    /**
     *
     */
    @Override
    public Set<String> getConfKeySet() {
        return getInstance().getConfFileMap().keySet();
    }

    /**
     *
     */
    @Override
    public String confToString() {

        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append("\n");
        Map<String, DisconfCenterFile> disMap = getInstance().getConfFileMap();
        for (String file : disMap.keySet()) {
            sBuffer.append("disconf-file:\t" + file + "\t");

            if (LOGGER.isDebugEnabled()) {
                sBuffer.append(disMap.get(file).toString());
            } else {
                sBuffer.append(disMap.get(file).infoString());
            }
            sBuffer.append("\n");
        }

        return sBuffer.toString();
    }

    @Override
    public void exclude(Set<String> keySet) {

        for (String key : keySet) {
            getInstance().excludeOneFile(key);
        }
    }
}
