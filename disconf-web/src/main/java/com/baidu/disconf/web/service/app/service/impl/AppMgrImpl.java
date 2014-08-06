package com.baidu.disconf.web.service.app.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baidu.disconf.ub.common.utils.DateUtils;
import com.baidu.disconf.web.service.app.bo.App;
import com.baidu.disconf.web.service.app.dao.AppDao;
import com.baidu.disconf.web.service.app.form.AppNewForm;
import com.baidu.disconf.web.service.app.service.AppMgr;
import com.baidu.disconf.web.service.app.vo.AppListVo;
import com.baidu.dsp.common.constant.DataFormatConstants;

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

    @Override
    public Map<Long, App> getByIds(Set<Long> ids) {

        List<App> apps = appDao.get(ids);

        Map<Long, App> map = new HashMap<Long, App>();
        for (App app : apps) {
            map.put(app.getId(), app);
        }

        return map;
    }

    @Override
    public App getById(Long id) {

        return appDao.get(id);
    }

    @Override
    public App create(AppNewForm appNew) {

        App app = new App();
        app.setName(appNew.getApp());
        app.setDesc(appNew.getDesc());

        // 时间
        String curTime = DateUtils.format(new Date(),
                DataFormatConstants.COMMON_TIME_FORMAT);
        app.setCreateTime(curTime);
        app.setUpdateTime(curTime);

        return appDao.create(app);
    }

    @Override
    public void delete(Long appId) {
        appDao.delete(appId);
    }
}
