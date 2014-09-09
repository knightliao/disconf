package com.baidu.disconf.client.scan;

import java.util.Set;

/**
 * 
 * @author liaoqiqi
 * @version 2014-7-29
 */
public interface ScanMgr {

    void firstScan(String packageName, Set<String> fileSet) throws Exception;

    void secondScan() throws Exception;
}
