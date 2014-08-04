package com.baidu.disconf.client.store.processor.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.client.common.inter.IDisconfUpdate;
import com.baidu.disconf.client.common.model.DisConfCommonModel;
import com.baidu.disconf.client.common.model.DisconfCenterBaseModel;
import com.baidu.disconf.client.common.model.DisconfCenterItem;
import com.baidu.disconf.client.store.DisconfStoreProcessor;
import com.baidu.disconf.client.store.inner.DisconfCenterStore;
import com.baidu.disconf.client.store.processor.model.DisconfValue;
import com.baidu.ub.common.utils.ClassUtils;

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

            return DisconfCenterStore.getInstance().getConfFileMap()
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
    public void inject2Instance(String key) {

        DisconfCenterItem disconfCenterItem = DisconfCenterStore.getInstance()
                .getConfItemMap().get(key);

        // 校验是否存在
        if (disconfCenterItem == null) {
            LOGGER.error("canot find " + key + " in store....");
            return;
        }

        // 无实例无值则 无法注入
        if (disconfCenterItem.getObject() == null) {
            LOGGER.warn(key + " 's oboject is null");
            return;
        }

        // 根据类型设置值
        //
        // 注入实体
        //
        try {

            // 默认值
            Object defaultValue = disconfCenterItem.getField().get(
                    disconfCenterItem.getObject());

            if (disconfCenterItem.getValue() == null) {

                // 如果值为空，则直接使用默认值
                disconfCenterItem.getField().set(disconfCenterItem.getObject(),
                        defaultValue);

            } else {

                disconfCenterItem.getField().set(disconfCenterItem.getObject(),
                        disconfCenterItem.getValue());
            }

        } catch (Exception e) {
            LOGGER.error(e.toString(), e);
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
            LOGGER.info("canot find " + keyName + " in store....");
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
            LOGGER.error(e.toString(), e);
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
        return DisconfCenterStore.getInstance().getConfItemMap().toString();
    }

}
