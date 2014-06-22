package com.baidu.disconf2.service.app.dao;

import java.util.List;
import java.util.Set;

import com.baidu.disconf2.service.app.bo.App;
import com.baidu.ub.common.generic.dao.BaseDao;

/**
 * 
 * @author liaoqiqi
 * @version 2014-6-16
 */
public interface AppDao extends BaseDao<Long, App> {

    public App getByName(String name);

    List<App> getByIds(Set<Long> ids);
}
