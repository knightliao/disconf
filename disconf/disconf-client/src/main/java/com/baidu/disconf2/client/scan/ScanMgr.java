package com.baidu.disconf2.client.scan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf2.client.scan.inner.ScanCoreAdapter;
import com.baidu.disconf2.client.scan.inner.ScanModel;
import com.baidu.disconf2.client.scan.inner.ScanPack;
import com.baidu.disconf2.client.watch.WatchMgr;

/**
 * 扫描模块, 依赖 WatchMgr 模块
 * 
 * @author liaoqiqi
 * @version 2014-6-6
 */
public class ScanMgr {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(ScanMgr.class);

    /**
     * 
     * 扫描并存储
     * 
     * @throws Exception
     */
    public static void init(String packageName) throws Exception {

        // 下载模块必须先初始化
        if (!WatchMgr.getInstance().isInit()) {
            throw new Exception("WatchMgr should be init before ScanMgr");
        }

        LOGGER.debug("start to scan package: " + packageName);

        // 获取扫描对象并分析整合
        ScanModel scanModel = ScanPack.scan(packageName);

        // 放进仓库
        ScanCoreAdapter.put2Store(scanModel);
    }

}
