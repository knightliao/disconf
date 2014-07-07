package com.baidu.disconf.web.service.user.dao;

import com.baidu.disconf.web.service.user.bo.User;
import com.baidu.ub.common.generic.dao.BaseDao;

/**
 * 
 * @author liaoqiqi
 * @version 2013-11-28
 */
public interface UserDao extends BaseDao<Integer, User> {

    void executeSql(String sql);

    User getUserByName(String name);
}
