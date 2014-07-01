package com.baidu.disconf2.client;

/**
 * 第二次扫描，动态扫描
 * 
 * @author liaoqiqi
 * @version 2014-6-18
 */
public class DisconfMgrBeanSecond {

    /**
     * 
     */
    public void init() {

        DisconfMgr.secondScan();
    }

    /**
     * 
     */
    public void destory() {

        DisconfMgr.close();
    }
}
