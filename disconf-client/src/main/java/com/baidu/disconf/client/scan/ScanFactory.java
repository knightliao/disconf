package com.baidu.disconf.client.scan;

import com.baidu.disconf.client.scan.impl.ScanMgrImpl;
import com.baidu.disconf.client.support.registry.Registry;

/**
 * 扫描器工厂
 *
 * @author liaoqiqi
 * @version 2014-7-29
 */
public class ScanFactory {

    /**
     * @throws Exception
     */
    public static ScanMgr getScanMgr(Registry registry) throws Exception {

        ScanMgr scanMgr = new ScanMgrImpl(registry);
        return scanMgr;
    }
}
