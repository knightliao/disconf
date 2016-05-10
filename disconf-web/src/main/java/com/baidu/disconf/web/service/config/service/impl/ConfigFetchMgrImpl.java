package com.baidu.disconf.web.service.config.service.impl;

import com.baidu.disconf.web.utils.CodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baidu.disconf.core.common.constants.Constants;
import com.baidu.disconf.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf.core.common.json.ValueVo;
import com.baidu.disconf.web.service.config.bo.Config;
import com.baidu.disconf.web.service.config.dao.ConfigDao;
import com.baidu.disconf.web.service.config.service.ConfigFetchMgr;
import com.baidu.disconf.web.service.config.utils.ConfigUtils;

/**
 * @author knightliao
 */
@Service
public class ConfigFetchMgrImpl implements ConfigFetchMgr {

    protected static final Logger LOG = LoggerFactory.getLogger(ConfigFetchMgrImpl.class);

    @Autowired
    private ConfigDao configDao;

    /**
     * 根据详细参数获取配置
     */
    @Override
    public Config getConfByParameter(Long appId, Long envId, String env, String key,
                                     DisConfigTypeEnum disConfigTypeEnum) {

        Config config = configDao.getByParameter(appId, envId, env, key, disConfigTypeEnum);
        config.setValue(CodeUtils.unicodeToUtf8(config.getValue()));
        return config;
    }

    /**
     * 根据详细参数获取配置返回
     */
    public ValueVo getConfItemByParameter(Long appId, Long envId, String version, String key) {

        Config config = configDao.getByParameter(appId, envId, version, key, DisConfigTypeEnum.ITEM);
        if (config == null) {
            return ConfigUtils.getErrorVo("cannot find this config");
        }
        config.setValue(CodeUtils.unicodeToUtf8(config.getValue()));

        ValueVo valueVo = new ValueVo();
        valueVo.setValue(config.getValue());
        valueVo.setStatus(Constants.OK);

        return valueVo;
    }
}
