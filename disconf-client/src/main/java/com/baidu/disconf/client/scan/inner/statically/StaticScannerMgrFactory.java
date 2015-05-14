package com.baidu.disconf.client.scan.inner.statically;

import com.baidu.disconf.client.scan.inner.statically.impl.StaticScannerFileMgrImpl;
import com.baidu.disconf.client.scan.inner.statically.impl.StaticScannerItemMgrImpl;
import com.baidu.disconf.client.scan.inner.statically.impl.StaticScannerNonAnnotationFileMgrImpl;

/**
 * @author liaoqiqi
 * @version 2014-9-9
 */
public class StaticScannerMgrFactory {

    /**
     * 配置文件处理

     */
    public static StaticScannerMgr getDisconfFileStaticScanner() {
        return new StaticScannerFileMgrImpl();
    }

    /**
     * 配置项处理

     */
    public static StaticScannerMgr getDisconfItemStaticScanner() {
        return new StaticScannerItemMgrImpl();
    }

    /**
     * 非注解的配置文件处理

     */
    public static StaticScannerMgr getDisconfNonAnnotationFileStaticScanner() {
        return new StaticScannerNonAnnotationFileMgrImpl();
    }

}
