package com.baidu.disconf2.client.scan;

import com.baidu.disconf2.client.scan.inner.ScanCoreAdapter;
import com.baidu.disconf2.client.scan.inner.ScanModel;
import com.baidu.disconf2.client.scan.inner.ScanPack;

/**
 * 扫描模块, 依赖 WatchMgr 模块
 * 
 * @author liaoqiqi
 * @version 2014-6-6
 */
public class ScanMgr {

    /**
     * 
     * 扫描并存储
     * 
     * @throws Exception
     */
    public static void scanAndStore(String packageName) throws Exception {

        // 获取扫描对象
        ScanModel scanModel = ScanPack.scan(packageName);

        // 放进仓库
        ScanCoreAdapter.put2Store(scanModel);
    }

}
