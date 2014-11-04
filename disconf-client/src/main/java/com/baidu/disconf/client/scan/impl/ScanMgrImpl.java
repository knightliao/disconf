package com.baidu.disconf.client.scan.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.client.config.DisClientConfig;
import com.baidu.disconf.client.scan.ScanMgr;
import com.baidu.disconf.client.scan.inner.dynamic.ScanDynamicStoreAdapter;
import com.baidu.disconf.client.scan.inner.statically.ScanStatic;
import com.baidu.disconf.client.scan.inner.statically.StaticScannerMgr;
import com.baidu.disconf.client.scan.inner.statically.StaticScannerMgrFactory;
import com.baidu.disconf.client.scan.inner.statically.model.ScanStaticModel;
import com.baidu.disconf.client.store.inner.DisconfCenterHostFilesStore;

/**
 * 扫描模块
 * 
 * @author liaoqiqi
 * @version 2014-6-6
 */
public class ScanMgrImpl implements ScanMgr {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ScanMgrImpl.class);

    // 扫描对象
    private volatile ScanStaticModel scanModel = null;

    private List<StaticScannerMgr> staticScannerMgrs = new ArrayList<StaticScannerMgr>();

    /**
     * 
     */
    public ScanMgrImpl() {

        // 配置文件
        staticScannerMgrs.add(StaticScannerMgrFactory.getDisconfFileStaticScanner());

        // 配置项
        staticScannerMgrs.add(StaticScannerMgrFactory.getDisconfItemStaticScanner());

        // 非注解 托管的配置文件
        staticScannerMgrs.add(StaticScannerMgrFactory.getDisconfNonAnnotationFileStaticScanner());
    }

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

        // 增加非注解的配置
        scanModel.setJustHostFiles(DisconfCenterHostFilesStore.getInstance().getJustHostFiles());

        // 放进仓库
        for (StaticScannerMgr scannerMgr : staticScannerMgrs) {

            // 扫描进入仓库
            scannerMgr.scanData2Store(scanModel);

            // 忽略哪些KEY
            scannerMgr.exclude(DisClientConfig.getInstance().getIgnoreDisconfKeySet());
        }
    }

    /**
     * 第二次扫描(动态)
     */
    public void secondScan() throws Exception {

        if (scanModel == null) {
            synchronized (scanModel) {
                // 下载模块必须先初始化
                if (scanModel == null) {
                    throw new Exception("You should run first scan before second Scan");
                }
            }
        }

        // 将回调函数实例化并写入仓库
        ScanDynamicStoreAdapter.scanUpdateCallbacks(scanModel);
    }
}
