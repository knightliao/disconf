package com.baidu.disconf2.service.env.service;

import java.util.List;

import com.baidu.disconf2.service.env.bo.Env;
import com.baidu.disconf2.service.env.vo.EnvListVo;

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

    /**
     * 
     * @return
     */
    public List<EnvListVo> getList();
}
