package com.baidu.disconf.client;

/**
 * 第二次扫描，动态扫描
 *
 * @author liaoqiqi
 * @version 2014-6-18
 */
public class DisconfMgrBeanSecond {

    public void init() {

        DisconfMgr.getInstance().secondScan();
    }

    public void destroy() {
        DisconfMgr.getInstance().close();
    }
}
