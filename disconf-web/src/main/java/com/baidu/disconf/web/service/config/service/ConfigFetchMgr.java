package com.baidu.disconf.web.service.config.service;

import com.baidu.disconf.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf.core.common.json.ValueVo;
import com.baidu.disconf.web.service.config.bo.Config;

import java.util.List;

/**
 * @author knightliao
 */
public interface ConfigFetchMgr {

    /**
     * @param appId
     * @param envId
     * @param envId
     * @param key
     *
     * @return
     */
    ValueVo getConfItemByParameter(Long appId, Long envId, String version, String key);

    /**
     * @param appId
     * @param envId
     * @param version
     * @param key
     * @param disConfigTypeEnum
     *
     * @return
     */
    Config getConfByParameter(Long appId, Long envId, String version, String key, DisConfigTypeEnum disConfigTypeEnum);


    /**
     * @param appId
     * @param envId
     * @param env
     *
     * @return
     */
    List<Config> getConfListByParameter(Long appId, Long envId, String env, Boolean hasValue);

}
