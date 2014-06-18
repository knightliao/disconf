package com.baidu.disconf2.client.scan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf2.client.scan.inner.ScanDynamicStoreAdapter;
import com.baidu.disconf2.client.scan.inner.ScanStatic;
import com.baidu.disconf2.client.scan.inner.ScanStaticStoreAdapter;
import com.baidu.disconf2.client.scan.inner.model.ScanStaticModel;
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

    // 扫描对象
    private static ScanStaticModel scanModel = null;

    /**
     * 
     * 扫描并存储(静态)
     * 
     * @throws Exception
     */
    public static void firstScan(String packageName) throws Exception {

        // 下载模块必须先初始化
        if (!WatchMgr.getInstance().isInit()) {
            throw new Exception("WatchMgr should be init before ScanMgr");
        }

        LOGGER.debug("start to scan package: " + packageName);

        // 获取扫描对象并分析整合
        scanModel = ScanStatic.scan(packageName);

        // 放进仓库
        ScanStaticStoreAdapter.put2Store(scanModel);
    }

    /**
     * 第二次扫描(动态)
     */
    public static void secondScan() throws Exception {

        // 下载模块必须先初始化
        if (scanModel == null) {
            throw new Exception("You should run first scan before second Scan");
        }

        // 将回调函数实例化并写入仓库
        ScanDynamicStoreAdapter.scanUpdateCallbacks(scanModel);
    }

}
