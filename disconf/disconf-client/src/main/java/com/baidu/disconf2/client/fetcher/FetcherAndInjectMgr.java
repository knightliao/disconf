package com.baidu.disconf2.client.fetcher;

import java.util.Map;

import com.baidu.disconf2.client.common.model.DisconfCenterFile;
import com.baidu.disconf2.client.core.DisconfCoreMgr;

/**
 * 下载并注入模块
 * 
 * @author liaoqiqi
 * @version 2014-6-10
 */
public class FetcherAndInjectMgr {

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

        Map<String, DisconfCenterFile> disConfCenterFileMap = DisconfCoreMgr
                .getInstance().getConfFileMap();
        
        

    }
}
