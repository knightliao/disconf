package com.baidu.disconf.client.store.processor.impl;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.client.common.model.DisConfCommonModel;
import com.baidu.disconf.client.common.model.DisconfCenterBaseModel;
import com.baidu.disconf.client.common.model.DisconfCenterItem;
import com.baidu.disconf.client.common.update.IDisconfUpdate;
import com.baidu.disconf.client.store.DisconfStoreProcessor;
import com.baidu.disconf.client.store.inner.DisconfCenterStore;
import com.baidu.disconf.client.store.processor.model.DisconfValue;
import com.github.knightliao.apollo.utils.common.ClassUtils;

/**
 * 配置项仓库算子实现器
 * 
 * @author liaoqiqi
 * @version 2014-8-4
 */
public class DisconfStoreItemProcessorImpl implements DisconfStoreProcessor {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(DisconfStoreItemProcessorImpl.class);

    /**
     * 
     */
    @Override
    public void addUpdateCallbackList(String keyName,
            List<IDisconfUpdate> iDisconfUpdateList) {

        if (DisconfCenterStore.getInstance().getConfItemMap()
                .containsKey(keyName)) {

            DisconfCenterStore.getInstance().getConfItemMap().get(keyName)
                    .getDisconfCommonCallbackModel().getDisconfConfUpdates()
                    .addAll(iDisconfUpdateList);
        }

    }

    /**
     * 
     */
    @Override
    public List<IDisconfUpdate> getUpdateCallbackList(String keyName) {

        if (DisconfCenterStore.getInstance().getConfItemMap()
                .containsKey(keyName)) {

            return DisconfCenterStore.getInstance().getConfItemMap()
                    .get(keyName).getDisconfCommonCallbackModel()
                    .getDisconfConfUpdates();
        }

        return new ArrayList<IDisconfUpdate>();
    }

    /**
     * 
     */
    @Override
    public DisConfCommonModel getCommonModel(String keyName) {

        DisconfCenterItem disconfCenterItem = DisconfCenterStore.getInstance()
                .getConfItemMap().get(keyName);

        // 校验是否存在
        if (disconfCenterItem == null) {
            LOGGER.error("canot find " + keyName + " in store....");
            return null;
        }

        return disconfCenterItem.getDisConfCommonModel();
    }

    /**
     * 
     */
    @Override
    public boolean hasThisConf(String keyName) {

        // 配置项
        if (DisconfCenterStore.getInstance().getConfItemMap()
                .containsKey(keyName)) {
            return true;
        }

        return false;
    }

    /**
     * 
     */
    @Override
    public void inject2Instance(Object object, String key) {

        DisconfCenterItem disconfCenterItem = DisconfCenterStore.getInstance()
                .getConfItemMap().get(key);

        // 校验是否存在
        if (disconfCenterItem == null) {
            LOGGER.error("canot find " + key + " in store....");
            return;
        }

        //
        // 非静态类
        //
        if (object != null) {
            disconfCenterItem.setObject(object);
        }

        // 根据类型设置值
        //
        // 注入实体
        //
        try {

            if (object != null) {

                // 默认值
                Object defaultValue = disconfCenterItem.getField().get(object);

                LOGGER.debug(disconfCenterItem.getKey()
                        + " is a non-static field. ");

                if (disconfCenterItem.getValue() == null) {

                    // 如果仓库里的值为空，则实例直接使用默认值,
                    disconfCenterItem.getField().set(object, defaultValue);

                    // 仓库里也使用此值
                    disconfCenterItem.setValue(defaultValue);

                } else {

                    // 如果仓库里的值为非空，则实例使用仓库里的值
                    disconfCenterItem.getField().set(object,
                            disconfCenterItem.getValue());
                }

            } else {

                //
                // 静态类
                //
                if (Modifier.isStatic(disconfCenterItem.getField()
                        .getModifiers())) {
                    LOGGER.debug(disconfCenterItem.getKey()
                            + " is a static field. ");
                    disconfCenterItem.getField().set(null,
                            disconfCenterItem.getValue());
                }
            }

        } catch (Exception e) {
            LOGGER.error("inject2Instance key: " + key + " " + e.toString(), e);
            return;
        }
    }

    /**
     * 
     */
    @Override
    public Object getConfig(String fileName, String keyName) {

        DisconfCenterItem disconfCenterItem = DisconfCenterStore.getInstance()
                .getConfItemMap().get(keyName);

        // 校验是否存在
        if (disconfCenterItem == null) {
            LOGGER.debug("canot find " + keyName + " in store....");
            return null;
        }

        return disconfCenterItem.getValue();
    }

    /**
     * 
     */
    @Override
    public void inject2Store(String key, DisconfValue disconfValue) {

        DisconfCenterItem disconfCenterItem = DisconfCenterStore.getInstance()
                .getConfItemMap().get(key);

        // 校验是否存在
        if (disconfCenterItem == null) {
            LOGGER.error("canot find " + key + " in store....");
            return;
        }

        if (disconfValue == null || disconfValue.getValue() == null) {
            LOGGER.error("value is null for {}", key);
            return;
        }

        // 存储
        Class<?> typeClass = disconfCenterItem.getField().getType();

        // 根据类型设置值
        //
        // 注入仓库
        //
        try {

            Object newValue = ClassUtils.getValeByType(typeClass,
                    disconfValue.getValue());
            disconfCenterItem.setValue(newValue);

            // 如果Object非null,则顺便也注入
            if (disconfCenterItem.getObject() != null) {
                disconfCenterItem.getField().set(disconfCenterItem.getObject(),
                        disconfCenterItem.getValue());
            }

        } catch (Exception e) {
            LOGGER.error("key: " + key + " " + e.toString(), e);
            return;
        }

    }

    /**
     * 
     */
    @Override
    public void transformScanData(
            List<DisconfCenterBaseModel> disconfCenterBaseModels) {

        for (DisconfCenterBaseModel disconfCenterItem : disconfCenterBaseModels) {
            DisconfCenterStore.getInstance().storeOneItem(disconfCenterItem);
        }
    }

    /**
     * 
     */
    @Override
    public DisconfCenterBaseModel getConfData(String key) {

        if (DisconfCenterStore.getInstance().getConfItemMap().containsKey(key)) {

            return DisconfCenterStore.getInstance().getConfItemMap().get(key);

        } else {

            return null;
        }
    }

    /**
     * 
     */
    @Override
    public Set<String> getConfKeySet() {
        return DisconfCenterStore.getInstance().getConfItemMap().keySet();
    }

    /**
     * 
     */
    @Override
    public String confToString() {

        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append("\n");
        Map<String, DisconfCenterItem> disMap = DisconfCenterStore
                .getInstance().getConfItemMap();
        for (String file : disMap.keySet()) {
            sBuffer.append("disItem:\t" + file + "\t"
                    + disMap.get(file).toString());
            sBuffer.append("\n");
        }

        return sBuffer.toString();
    }

}
