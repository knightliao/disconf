package com.baidu.disconf2.service.config.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baidu.disconf2.core.common.constants.Constants;
import com.baidu.disconf2.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf2.core.common.json.ValueVo;
import com.baidu.disconf2.service.app.bo.App;
import com.baidu.disconf2.service.app.service.AppMgr;
import com.baidu.disconf2.service.config.bo.Config;
import com.baidu.disconf2.service.config.dao.ConfigDao;
import com.baidu.disconf2.service.config.service.ConfigMgr;
import com.baidu.disconf2.service.config.utils.ConfigUtils;

/**
 * 
 * @author liaoqiqi
 * @version 2014-6-16
 */
@Service
public class ConfigMgrImpl implements ConfigMgr {

    @Autowired
    private ConfigDao configDao;

    @Autowired
    private AppMgr appMgr;

    /**
     * 获取配置
     */
    public ValueVo getConfItemByParameter(Long appId, Long envId, String env,
            String key) {

        Config config = configDao.getByParameter(appId, envId, env, key,
                DisConfigTypeEnum.ITEM);
        if (config == null) {
            return ConfigUtils.getErrorVo("cannot find this config");
        }

        ValueVo valueVo = new ValueVo();
        valueVo.setValue(config.getValue());
        valueVo.setStatus(Constants.OK);

        return valueVo;
    }

    /**
     * 
     */
    @Override
    public Config getConfByParameter(Long appId, Long envId, String env,
            String key) {

        Config config = configDao.getByParameter(appId, envId, env, key,
                DisConfigTypeEnum.FILE);
        return config;
    }

    /**
     * 
     */
    @Override
    public List<String> getConfByAppname(String appName) {

        List<String> versionList = new ArrayList<String>();

        App app = appMgr.getByName(appName);
        if (app == null) {
            return versionList;
        }

        List<Config> configs = configDao.getConfByAppname(app.getId());

        for (Config config : configs) {

            versionList.add(config.getVersion());
        }

        return versionList;
    }
}
