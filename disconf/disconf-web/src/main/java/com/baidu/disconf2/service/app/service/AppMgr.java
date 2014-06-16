package com.baidu.disconf2.service.app.service;

import com.baidu.disconf2.service.app.bo.App;

/**
 * 
 * @author liaoqiqi
 * @version 2014-6-16
 */
public interface AppMgr {

    /**
     * 
     * @param name
     */
    public App getByName(String name);
}
