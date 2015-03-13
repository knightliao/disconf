package com.baidu.disconf.client.scan;

/**
 * @author liaoqiqi
 * @version 2014-7-29
 */
public interface ScanMgr {

    void firstScan(String packageName) throws Exception;

    void secondScan() throws Exception;
}
