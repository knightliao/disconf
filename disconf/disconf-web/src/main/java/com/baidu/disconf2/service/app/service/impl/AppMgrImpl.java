package com.baidu.disconf2.service.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baidu.disconf2.service.app.bo.App;
import com.baidu.disconf2.service.app.dao.AppDao;
import com.baidu.disconf2.service.app.service.AppMgr;

/**
 * 
 * @author liaoqiqi
 * @version 2014-6-16
 */
@Service
public class AppMgrImpl implements AppMgr {

    @Autowired
    private AppDao appDao;

    @Override
    public App getByName(String name) {

        return appDao.getByName(name);
    }

}
