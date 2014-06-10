package com.baidu.disconf2.client.core;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf2.client.common.inter.IDisconfUpdate;
import com.baidu.disconf2.client.common.model.DisconfCenterFile;
import com.baidu.disconf2.client.common.model.DisconfCenterItem;
import com.baidu.disconf2.client.core.inner.DisconfCenterStore;

/**
 * 仓库模块
 * 
 * @author liaoqiqi
 * @version 2014-6-6
 */
public class DisconfCoreMgr {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(DisconfCoreMgr.class);

    protected static final DisconfCoreMgr INSTANCE = new DisconfCoreMgr();

    public static DisconfCoreMgr getInstance() {
        return INSTANCE;
    }

    private DisconfCoreMgr() {

    }

    // 核心仓库
    private DisconfCenterStore disconfCenterStore = new DisconfCenterStore();

    /**
     * 批量添加配置文件
     * 
     * @return
     */
    public void transformScanFiles(List<DisconfCenterFile> disconfCenterFiles) {

        for (DisconfCenterFile disconfCenterFile : disconfCenterFiles) {
            disconfCenterStore.storeOneFile(disconfCenterFile);
        }
    }

    /**
     * 获取配置文件的数据
     * 
     * @return
     */
    public Map<String, DisconfCenterFile> getConfFileMap() {
        return disconfCenterStore.getConfFileMap();
    }

    /**
     * 批量添加配置项
     * 
     * @return
     */
    public void transformScanItems(List<DisconfCenterItem> disconfCenterItems) {

        for (DisconfCenterItem disconfCenterItem : disconfCenterItems) {
            disconfCenterStore.storeOneItem(disconfCenterItem);
        }
    }

    /**
     * 获取配置项的数据
     * 
     * @return
     */
    public Map<String, DisconfCenterItem> getConfItemMap() {
        return disconfCenterStore.getConfItemMap();
    }

    /**
     * 是否有这个配置
     * 
     * @return
     */
    public boolean hasThisConf(String key) {

        // 配置文件
        if (disconfCenterStore.getConfFileMap().containsKey(key)) {
            return true;
        }

        // 配置项
        if (disconfCenterStore.getConfItemMap().containsKey(key)) {
            return true;
        }

        return false;
    }

    /**
     * 添加一个更新 的回调函数
     */
    public void addUpdateCallback(String key, IDisconfUpdate iDisconfUpdate) {

        if (disconfCenterStore.getConfFileMap().containsKey(key)) {

            disconfCenterStore.getConfFileMap().get(key)
                    .getDisconfCommonCallbackModel().getDisconfConfUpdates()
                    .add(iDisconfUpdate);
        }
    }

    /**
     * 添加一个更新 的回调函数
     */
    public void addUpdateCallbackList(String key,
            List<IDisconfUpdate> iDisconfUpdateList) {

        if (disconfCenterStore.getConfFileMap().containsKey(key)) {

            disconfCenterStore.getConfFileMap().get(key)
                    .getDisconfCommonCallbackModel().getDisconfConfUpdates()
                    .addAll(iDisconfUpdateList);

        } else {

            if (disconfCenterStore.getConfItemMap().containsKey(key)) {

                disconfCenterStore.getConfItemMap().get(key)
                        .getDisconfCommonCallbackModel()
                        .getDisconfConfUpdates().addAll(iDisconfUpdateList);
            }
        }
    }
}
