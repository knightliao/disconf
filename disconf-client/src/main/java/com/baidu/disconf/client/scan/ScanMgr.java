package com.baidu.disconf.client.scan;

import java.util.List;

/**
 * @author liaoqiqi
 * @version 2014-7-29
 */
public interface ScanMgr {

    /**
     * @throws Exception
     */
    void firstScan(List<String> packageNameLit) throws Exception;

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
