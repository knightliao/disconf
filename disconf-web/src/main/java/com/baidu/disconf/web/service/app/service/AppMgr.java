package com.baidu.disconf.web.service.app.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baidu.disconf.web.service.app.bo.App;
import com.baidu.disconf.web.service.app.form.AppNewForm;
import com.baidu.disconf.web.service.app.vo.AppListVo;

/**
 * @author liaoqiqi
 * @version 2014-6-16
 */
public interface AppMgr {

    /**
     * 根据APP名获取APP
     *
     * @param name
     */
    App getByName(String name);

    /**
     * 获取原始APP LIST
     *
     * @return
     */
    List<App> getAppList();

    /**
     * 获取VO结构的APPLIST，包含权限控制
     *
     * @return
     */
    List<AppListVo> getAuthAppVoList();

    /**
     * 获取一堆 APP
     *
     * @param ids
     *
     * @return
     */
    Map<Long, App> getByIds(Set<Long> ids);

    /**
     * 获取一个APP
     *
     * @param id
     *
     * @return
     */
    App getById(Long id);

    /**
     * 生成一个APP
     *
     * @param appNew
     *
     * @return
     */
    App create(AppNewForm appNew);

    /**
     * 删除此APP
     *
     * @param appId
     */
    void delete(Long appId);

    /**
     * 获取此APP的EMAIL
     *
     * @param id
     *
     * @return
     */
    String getEmails(Long id);
}
