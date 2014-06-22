package com.baidu.disconf2.service.env.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baidu.disconf2.service.env.bo.Env;
import com.baidu.disconf2.service.env.dao.EnvDao;
import com.baidu.disconf2.service.env.service.EnvMgr;
import com.baidu.disconf2.service.env.vo.EnvListVo;

/**
 * 
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
    public List<EnvListVo> getList() {

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

}
