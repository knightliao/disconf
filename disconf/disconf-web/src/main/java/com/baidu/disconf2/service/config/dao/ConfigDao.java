package com.baidu.disconf2.service.config.dao;

import java.util.List;

import com.baidu.disconf2.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf2.service.config.bo.Config;
import com.baidu.ub.common.generic.dao.BaseDao;

/**
 * 
 * @author liaoqiqi
 * @version 2014-6-16
 */
public interface ConfigDao extends BaseDao<Long, Config> {

    public Config getByParameter(Long appId, Long envId, String env,
            String key, DisConfigTypeEnum disConfigTypeEnum);

    /**
     * 
     * @param appName
     * @return
     */
    public List<Config> getConfByAppname(Long appId);
}
