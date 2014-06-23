package com.baidu.disconf2.service.env.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public Map<Long, Env> getByIds(Set<Long> ids);

    public Env getById(Long id);
}
