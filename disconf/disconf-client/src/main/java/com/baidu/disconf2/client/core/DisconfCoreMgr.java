package com.baidu.disconf2.client.core;

import java.util.Map;

import com.baidu.disconf2.client.common.model.DisconfCenterFile;
import com.baidu.disconf2.client.store.DisconfStoreMgr;

/**
 * 管理 下载、注入、Watch三模块
 * 
 * @author liaoqiqi
 * @version 2014-6-10
 */
public class DisconfCoreMgr {

    /**
     * 1. 获取远程的所有配置数据<br/>
     * 2. 注入到仓库中，并注入到CONF实体中
     */
    public static void run() {

    }

    /**
     * 处理配置文件
     */
    private static void processConfFile() {

        Map<String, DisconfCenterFile> disConfCenterFileMap = DisconfStoreMgr
                .getInstance().getConfFileMap();

        /**
         * 配置文件列表处理
         */
        for (String fileName : disConfCenterFileMap.keySet()) {

            DisconfCenterFile disconfCenterFile = disConfCenterFileMap
                    .get(fileName);
        }

    }
}
