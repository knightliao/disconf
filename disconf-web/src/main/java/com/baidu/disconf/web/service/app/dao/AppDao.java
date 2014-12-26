package com.baidu.disconf.web.service.app.dao;

import java.util.List;
import java.util.Set;

import com.baidu.disconf.web.service.app.bo.App;
import com.baidu.unbiz.common.genericdao.dao.BaseDao;

/**
 * @author liaoqiqi
 * @version 2014-6-16
 */
public interface AppDao extends BaseDao<Long, App> {

    /**
     * @param name
     *
     * @return
     */
    App getByName(String name);

    /**
     * @param ids
     *
     * @return
     */
    List<App> getByIds(Set<Long> ids);

}
