package com.baidu.disconf2.service.config.service;

import com.baidu.disconf2.core.common.json.ValueVo;
import com.baidu.disconf2.service.config.bo.Config;

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
    public ValueVo getConfItemByParameter(Long appId, Long envId, String env,
            String key);

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
            String key);
}
