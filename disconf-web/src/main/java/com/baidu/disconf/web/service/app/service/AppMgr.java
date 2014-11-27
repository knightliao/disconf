package com.baidu.disconf.web.service.app.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baidu.disconf.web.service.app.bo.App;
import com.baidu.disconf.web.service.app.form.AppNewForm;
import com.baidu.disconf.web.service.app.vo.AppListVo;

/**
 * 
 * @author liaoqiqi
 * @version 2014-6-16
 */
public interface AppMgr {

    /**
     * 
     * @param name
     */
    public App getByName(String name);

    /**
     * 
     * @return
     */
    public List<AppListVo> getList();

    public Map<Long, App> getByIds(Set<Long> ids);

    /**
     * 
     * @param id
     * @return
     */
    public App getById(Long id);

    App create(AppNewForm appNew);

    void delete(Long appId);

    String getEmails(Long id);
}
