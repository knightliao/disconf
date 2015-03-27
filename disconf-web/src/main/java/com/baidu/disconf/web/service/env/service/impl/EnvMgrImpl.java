package com.baidu.disconf.web.service.env.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baidu.disconf.web.service.env.bo.Env;
import com.baidu.disconf.web.service.env.dao.EnvDao;
import com.baidu.disconf.web.service.env.service.EnvMgr;
import com.baidu.disconf.web.service.env.vo.EnvListVo;

/**
 * @author liaoqiqi
 * @version 2014-6-16
 */
@Service
public class EnvMgrImpl implements EnvMgr {

    @Autowired
    private EnvDao envDao;

    @Override
    public Env getByName(String name) {

        return envDao.getByName(name);
    }

    /**
     *
     */
    @Override
    public List<EnvListVo> getVoList() {

        List<Env> envs = envDao.findAll();

        List<EnvListVo> envListVos = new ArrayList<EnvListVo>();
        for (Env env : envs) {
            EnvListVo envListVo = new EnvListVo();
            envListVo.setId(env.getId());
            envListVo.setName(env.getName());

            envListVos.add(envListVo);
        }

        return envListVos;
    }

    @Override
    public Map<Long, Env> getByIds(Set<Long> ids) {

        if (ids.size() == 0) {
            return new HashMap<Long, Env>();
        }

        List<Env> envs = envDao.get(ids);

        Map<Long, Env> map = new HashMap<Long, Env>();
        for (Env env : envs) {
            map.put(env.getId(), env);
        }

        return map;
    }

    @Override
    public Env getById(Long id) {
        return envDao.get(id);
    }

    /**
     *
     */
    @Override
    public List<Env> getList() {
        return envDao.findAll();
    }

}
