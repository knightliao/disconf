package com.baidu.disconf.web.service.config.dao;

import java.util.List;

import com.baidu.disconf.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf.web.service.config.bo.Config;
import com.baidu.dsp.common.form.RequestListBase.Page;
import com.baidu.ub.common.db.DaoPageResult;
import com.baidu.unbiz.common.genericdao.dao.BaseDao;

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
    public List<Config> getConfByAppEnv(Long appId, Long envId);

    /**
     * 
     * @param appId
     * @param envId
     * @param version
     * @return
     */
    public DaoPageResult<Config> getConfigList(Long appId, Long envId,
            String version, Page page);

    /**
     * 
     * @param configId
     * @return
     */
    public void updateValue(Long configId, String value);

    /**
     * 
     */
    public String getValue(Long configId);

    /**
     * 
     * @param appId
     * @param envId
     * @param version
     * @return
     */
    List<Config> getConfigList(Long appId, Long envId, String version);

}
