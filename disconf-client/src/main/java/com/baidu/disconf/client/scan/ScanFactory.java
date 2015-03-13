package com.baidu.disconf.client.scan;

import com.baidu.disconf.client.scan.impl.ScanMgrImpl;

/**
 * 扫描器工厂
 *
 * @author liaoqiqi
 * @version 2014-7-29
 */
public class ScanFactory {

    /**
     * @param fetcherMgr
     *
     * @return
     *
     * @throws Exception
     */
    public static ScanMgr getScanMgr() throws Exception {

        ScanMgr scanMgr = new ScanMgrImpl();
        return scanMgr;
    }
}
