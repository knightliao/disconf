package com.baidu.disconf.web.service.env.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baidu.disconf.web.service.env.bo.Env;
import com.baidu.disconf.web.service.env.vo.EnvListVo;

/**
 * @author liaoqiqi
 * @version 2014-6-16
 */
public interface EnvMgr {
    /**
     * @param name
     */
    Env getByName(String name);

    /**
     * @return
     */
    List<Env> getList();

    /**
     * @return
     */
    List<EnvListVo> getVoList();

    /**
     * @param ids
     *
     * @return
     */
    Map<Long, Env> getByIds(Set<Long> ids);

    /**
     * @param id
     *
     * @return
     */
    Env getById(Long id);
}
