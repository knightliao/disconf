package com.baidu.disconf.web.service.app.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.baidu.disconf.web.service.app.bo.App;
import com.baidu.disconf.web.service.app.dao.AppDao;
import com.baidu.disconf.web.service.app.form.AppNewForm;
import com.baidu.disconf.web.service.app.service.AppMgr;
import com.baidu.disconf.web.service.app.vo.AppListVo;
import com.baidu.disconf.web.service.role.constant.RoleConstant;
import com.baidu.disconf.web.service.user.dto.Visitor;
import com.baidu.disconf.web.service.user.service.UserInnerMgr;
import com.baidu.disconf.web.service.user.service.UserMgr;
import com.baidu.dsp.common.constant.DataFormatConstants;
import com.baidu.ub.common.commons.ThreadContext;
import com.github.knightliao.apollo.utils.time.DateUtils;

/**
 * @author liaoqiqi
 * @version 2014-6-16
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class AppMgrImpl implements AppMgr {

    @Autowired
    private AppDao appDao;

    @Autowired
    private UserInnerMgr userInnerMgr;

    @Autowired
    private UserMgr userMgr;

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
    public List<AppListVo> getAuthAppVoList() {

        Visitor visitor = ThreadContext.getSessionVisitor();
        List<AppListVo> appListVos = new ArrayList<AppListVo>();
        Set<Long> appIds = userInnerMgr.getVisitorAppIds();
        if (CollectionUtils.isEmpty(appIds) && (visitor.getRoleId() != RoleConstant.MANAGER)) {
            return appListVos;
        }
        List<App> apps = appDao.getByIds(appIds);

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

        if (ids.size() == 0) {
            return new HashMap<Long, App>();
        }

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
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public App create(AppNewForm appNew) {

        // new app
        App app = new App();
        app.setName(appNew.getApp());
        app.setDesc(appNew.getDesc());
        app.setEmails(appNew.getEmails());

        String curTime = DateUtils.format(new Date(), DataFormatConstants.COMMON_TIME_FORMAT);
        app.setCreateTime(curTime);
        app.setUpdateTime(curTime);

        app = appDao.create(app);
        //建立用户和app关系
        userMgr.addOneAppForUser(userMgr.getCurVisitor().getId(), app.getId());
        return app;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(Long appId) {
        appDao.delete(appId);
    }

    @Override
    public String getEmails(Long id) {

        App app = getById(id);

        if (app == null) {
            return "";
        }
        else {
            return app.getEmails();
        }
    }

    @Override
    public List<App> getAppList() {

        return appDao.findAll();
    }
}
