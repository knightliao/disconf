package com.baidu.disconf2.service.config.dao;

import java.util.List;

import com.baidu.disconf2.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf2.service.config.bo.Config;
import com.baidu.dsp.common.form.RequestListBase.Page;
import com.baidu.ub.common.generic.dao.BaseDao;
import com.baidu.ub.common.generic.vo.DaoPageResult;

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
    public List<Config> getConfByAppId(Long appId);

    /**
     * 
     * @param appId
     * @param envId
     * @param version
     * @return
     */
    public DaoPageResult<Config> getConfigList(Long appId, Long envId,
            String version, Page page);
}
