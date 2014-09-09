package com.baidu.disconf.client.scan.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.client.scan.ScanMgr;
import com.baidu.disconf.client.scan.inner.dynamic.ScanDynamicStoreAdapter;
import com.baidu.disconf.client.scan.inner.statically.ScanStatic;
import com.baidu.disconf.client.scan.inner.statically.StaticScannerMgr;
import com.baidu.disconf.client.scan.inner.statically.StaticScannerMgrFactory;
import com.baidu.disconf.client.scan.inner.statically.model.ScanStaticModel;

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

    private List<StaticScannerMgr> staticScannerMgrs = new ArrayList<StaticScannerMgr>();

    /**
     * 
     */
    public ScanMgrImpl() {

        staticScannerMgrs.add(StaticScannerMgrFactory
                .getDisconfFileStaticScanner());
        staticScannerMgrs.add(StaticScannerMgrFactory
                .getDisconfItemStaticScanner());
        staticScannerMgrs.add(StaticScannerMgrFactory
                .getDisconfNonAnnotationFileStaticScanner());
    }

    /**
     * 
     * 扫描并存储(静态)
     * 
     * @throws Exception
     */
    public void firstScan(String packageName, Set<String> fileSet)
            throws Exception {

        LOGGER.debug("start to scan package: " + packageName);

        // 获取扫描对象并分析整合
        scanModel = ScanStatic.scan(packageName);

        // 增加非注解的配置
        scanModel.setNonAnnotationFileSet(fileSet);

        // 放进仓库
        for (StaticScannerMgr scannerMgr : staticScannerMgrs) {
            scannerMgr.scanData2Store(scanModel);
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
                    throw new Exception(
                            "You should run first scan before second Scan");
                }
            }
        }

        // 将回调函数实例化并写入仓库
        ScanDynamicStoreAdapter.scanUpdateCallbacks(scanModel);
    }
}
