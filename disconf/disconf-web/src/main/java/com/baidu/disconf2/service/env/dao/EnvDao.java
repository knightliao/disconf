package com.baidu.disconf2.service.env.dao;

import com.baidu.disconf2.service.env.bo.Env;
import com.baidu.ub.common.generic.dao.BaseDao;

/**
 * 
 * @author liaoqiqi
 * @version 2014-6-16
 */
public interface EnvDao extends BaseDao<Long, Env> {

    public Env getByName(String name);
}
