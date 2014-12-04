package com.baidu.disconf.web.service.config.service;

import com.baidu.disconf.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf.core.common.json.ValueVo;
import com.baidu.disconf.web.service.config.bo.Config;

/**
 * 
 * @author knightliao
 * 
 */
public interface ConfigFetchMgr {

    /**
     * 
     * @param appId
     * @param envId
     * @param env
     * @param key
     * @param disConfigTypeEnum
     * @return
     */
    ValueVo getConfItemByParameter(Long appId, Long envId, String version, String key);

    /**
     * 
     * @param appId
     * @param envId
     * @param env
     * @param key
     * @param disConfigTypeEnum
     * @return
     */
    Config getConfByParameter(Long appId, Long envId, String env, String key, DisConfigTypeEnum disConfigTypeEnum);

}
