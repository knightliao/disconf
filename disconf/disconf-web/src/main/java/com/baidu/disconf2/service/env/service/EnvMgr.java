package com.baidu.disconf2.service.env.service;

import com.baidu.disconf2.service.env.bo.Env;

/**
 * 
 * @author liaoqiqi
 * @version 2014-6-16
 */
public interface EnvMgr {
    /**
     * 
     * @param name
     */
    public Env getByName(String name);
}
