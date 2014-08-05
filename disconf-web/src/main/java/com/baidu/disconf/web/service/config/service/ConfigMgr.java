package com.baidu.disconf.web.service.config.service;

import java.util.List;

import com.baidu.disconf.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf.core.common.json.ValueVo;
import com.baidu.disconf.web.service.config.bo.Config;
import com.baidu.disconf.web.service.config.form.ConfListForm;
import com.baidu.disconf.web.service.config.form.ConfNewItemForm;
import com.baidu.disconf.web.service.config.vo.ConfListVo;
import com.baidu.ub.common.generic.vo.DaoPageResult;

/**
 * 
 * @author liaoqiqi
 * @version 2014-6-16
 */
public interface ConfigMgr {

    /**
     * 
     * @param appId
     * @param envId
     * @param env
     * @param key
     * @param disConfigTypeEnum
     * @return
     */
    public ValueVo getConfItemByParameter(Long appId, Long envId,
            String version, String key);

    /**
     * 
     * @param appId
     * @param envId
     * @param env
     * @param key
     * @param disConfigTypeEnum
     * @return
     */
    public Config getConfByParameter(Long appId, Long envId, String env,
            String key, DisConfigTypeEnum disConfigTypeEnum);

    /**
     * 
     * @param appName
     * @return
     */
    public List<String> getVersionListByAppId(Long appId);

    /**
     * 
     * @param appId
     * @param envId
     * @param version
     * @return
     */
    public DaoPageResult<ConfListVo> getConfigList(ConfListForm confListForm);

    /**
     * 
     * @param configId
     * @return
     */
    public ConfListVo getConfVo(Long configId);

    /**
     * 
     * @param configId
     * @return
     */
    public Config getConfigById(Long configId);

    /**
     * 更新 配置项/配置文件
     * 
     * @param configId
     * @return
     */
    public void updateItemValue(Long configId, String value);

    /**
     * 
     * @param configId
     * @return
     */
    public String getValue(Long configId);

    void notifyZookeeper(Long configId);

    /**
     * 
     * @param confNewForm
     * @param disConfigTypeEnum
     */
    void newConfig(ConfNewItemForm confNewForm,
            DisConfigTypeEnum disConfigTypeEnum);

    void delete(Long configId);
}
