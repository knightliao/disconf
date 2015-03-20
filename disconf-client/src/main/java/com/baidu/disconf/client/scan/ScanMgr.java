package com.baidu.disconf.client.scan;

/**
 * @author liaoqiqi
 * @version 2014-7-29
 */
public interface ScanMgr {

    /**
     * @param packageName
     *
     * @throws Exception
     */
    void firstScan(String packageName) throws Exception;

    /**
     * @throws Exception
     */
    void secondScan() throws Exception;

    /**
     * reloadable for specify files
     *
     * @throws Exception
     */
    void reloadableScan(String fileName) throws Exception;
}
