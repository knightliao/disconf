package com.baidu.disconf2.service.env.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baidu.disconf2.service.env.bo.Env;
import com.baidu.disconf2.service.env.dao.EnvDao;
import com.baidu.disconf2.service.env.service.EnvMgr;

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

}
