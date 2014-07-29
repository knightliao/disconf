package com.baidu.disconf.client.scan.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.client.scan.ScanMgr;
import com.baidu.disconf.client.scan.inner.ScanDynamicStoreAdapter;
import com.baidu.disconf.client.scan.inner.ScanStatic;
import com.baidu.disconf.client.scan.inner.ScanStaticStoreAdapter;
import com.baidu.disconf.client.scan.inner.model.ScanStaticModel;

/**
 * 扫描模块
 * 
 * @author liaoqiqi
 * @version 2014-6-6
 */
public class ScanMgrImpl implements ScanMgr {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(ScanMgrImpl.class);

    // 扫描对象
    private volatile ScanStaticModel scanModel = null;

    /**
     * 
     * 扫描并存储(静态)
     * 
     * @throws Exception
     */
    public void firstScan(String packageName) throws Exception {

        LOGGER.debug("start to scan package: " + packageName);

        // 获取扫描对象并分析整合
        scanModel = ScanStatic.scan(packageName);

        // 放进仓库
        ScanStaticStoreAdapter.put2Store(scanModel);
    }

    /**
     * 第二次扫描(动态)
     */
    public void secondScan() throws Exception {

        if (scanModel == null) {
            synchronized (scanModel) {
                // 下载模块必须先初始化
                if (scanModel == null) {
                    throw new Exception(
                            "You should run first scan before second Scan");
                }
            }
        }

        // 将回调函数实例化并写入仓库
        ScanDynamicStoreAdapter.scanUpdateCallbacks(scanModel);
    }
}
