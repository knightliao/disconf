package com.baidu.disconf2.client;

/**
 * 可用于Spring的配置
 * 
 * @author liaoqiqi
 * @version 2014-6-17
 */
public class DisconfMgrBean {

    /**
     * 
     */
    private String scanPackage = null;

    /**
     * 
     */
    public void init() {

        DisconfMgr.start(scanPackage);
    }

    /**
     * 
     */
    public void destory() {

        DisconfMgr.close();
    }

    public String getScanPackage() {
        return scanPackage;
    }

    public void setScanPackage(String scanPackage) {
        this.scanPackage = scanPackage;
    }
}
