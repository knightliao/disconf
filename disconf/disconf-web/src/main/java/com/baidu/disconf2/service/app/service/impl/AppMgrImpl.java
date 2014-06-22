package com.baidu.disconf2.service.app.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baidu.disconf2.service.app.bo.App;
import com.baidu.disconf2.service.app.dao.AppDao;
import com.baidu.disconf2.service.app.service.AppMgr;
import com.baidu.disconf2.service.app.vo.AppListVo;

/**
 * 
 * @author liaoqiqi
 * @version 2014-6-16
 */
@Service
public class AppMgrImpl implements AppMgr {

    @Autowired
    private AppDao appDao;

    /**
     * 
     */
    @Override
    public App getByName(String name) {

        return appDao.getByName(name);
    }

    /**
     * 
     */
    @Override
    public List<AppListVo> getList() {

        List<App> apps = appDao.findAll();

        List<AppListVo> appListVos = new ArrayList<AppListVo>();
        for (App app : apps) {
            AppListVo appListVo = new AppListVo();
            appListVo.setId(app.getId());
            appListVo.setName(app.getName());
            appListVos.add(appListVo);
        }

        return appListVos;
    }

}
