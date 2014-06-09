package com.baidu.disconf2.client.core;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf2.client.common.model.DisconfCenterFile;
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

}
