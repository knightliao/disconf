package com.baidu.disconf2.service.config.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baidu.disconf2.core.common.constants.Constants;
import com.baidu.disconf2.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf2.core.common.json.ValueVo;
import com.baidu.disconf2.service.app.bo.App;
import com.baidu.disconf2.service.app.service.AppMgr;
import com.baidu.disconf2.service.config.bo.Config;
import com.baidu.disconf2.service.config.dao.ConfigDao;
import com.baidu.disconf2.service.config.form.ConfListForm;
import com.baidu.disconf2.service.config.service.ConfigMgr;
import com.baidu.disconf2.service.config.utils.ConfigUtils;
import com.baidu.disconf2.service.config.vo.ConfListVo;
import com.baidu.disconf2.service.env.bo.Env;
import com.baidu.disconf2.service.env.service.EnvMgr;
import com.baidu.dsp.common.utils.DataTransfer;
import com.baidu.dsp.common.utils.ServiceUtil;
import com.baidu.ub.common.generic.vo.DaoPageResult;

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

    @Autowired
    private EnvMgr envMgr;

    /**
     * 根据详细参数获取配置返回
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
     * 根据详细参数获取配置
     */
    @Override
    public Config getConfByParameter(Long appId, Long envId, String env,
            String key) {

        Config config = configDao.getByParameter(appId, envId, env, key,
                DisConfigTypeEnum.FILE);
        return config;
    }

    /**
     * 根据APPid获取其版本列表
     */
    @Override
    public List<String> getVersionListByAppId(Long appId) {

        List<String> versionList = new ArrayList<String>();

        List<Config> configs = configDao.getConfByAppId(appId);

        for (Config config : configs) {

            if (!versionList.contains(config.getVersion())) {
                versionList.add(config.getVersion());
            }
        }

        return versionList;
    }

    /**
     * 配置列表
     */
    @Override
    public DaoPageResult<ConfListVo> getConfigList(ConfListForm confListForm) {

        //
        // 数据据结果
        //
        DaoPageResult<Config> configList = configDao.getConfigList(
                confListForm.getAppId(), confListForm.getEnvId(),
                confListForm.getVersion(), confListForm.getPage());

        Set<Long> appIdSet = new HashSet<Long>();
        Set<Long> envIdSet = new HashSet<Long>();
        for (Config config : configList.getResult()) {
            appIdSet.add(config.getAppId());
            envIdSet.add(config.getEnvId());
        }

        //
        // map
        //
        final Map<Long, App> appMap = appMgr.getByIds(appIdSet);
        final Map<Long, Env> envMap = envMgr.getByIds(envIdSet);

        //
        // 进行转换
        //
        DaoPageResult<ConfListVo> configListVo = ServiceUtil.getResult(
                configList, new DataTransfer<Config, ConfListVo>() {

                    @Override
                    public ConfListVo transfer(Config input) {

                        String appNameString = appMap.get(input.getAppId())
                                .getName();
                        String envName = envMap.get(input.getEnvId()).getName();

                        ConfListVo configListVo = convert(input, appNameString,
                                envName);

                        return configListVo;
                    }
                });

        return configListVo;
    }

    /**
     * 转换成配置返回
     * 
     * @param config
     * @return
     */
    private ConfListVo convert(Config config, String appNameString,
            String envName) {

        ConfListVo confListVo = new ConfListVo();

        confListVo.setConfigId(config.getId());
        confListVo.setAppId(config.getAppId());
        confListVo.setAppName(appNameString);
        confListVo.setEnvName(envName);
        confListVo.setEnvId(config.getEnvId());
        confListVo.setCreateTime(config.getCreateTime());
        confListVo.setModifyTime(config.getUpdateTime());
        confListVo.setKey(config.getName());
        confListVo.setValue(config.getValue());
        confListVo.setVersion(config.getVersion());
        confListVo.setType(DisConfigTypeEnum.getByType(config.getType())
                .getModelName());

        return confListVo;
    }

    /**
     * 根据 配置ID获取配置返回
     */
    @Override
    public ConfListVo getConfVo(Long configId) {
        Config config = configDao.get(configId);

        App app = appMgr.getById(config.getAppId());
        Env env = envMgr.getById(config.getEnvId());

        return convert(config, app.getName(), env.getName());
    }

    /**
     * 根据配置ID获取配置
     */
    @Override
    public Config getConfigById(Long configId) {

        return configDao.get(configId);
    }

    /**
     * 更新 配置项的值
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void updateItemValue(Long configId, String value) {

        configDao.updateValue(configId, value);
    }

    /**
     * 获取配置值
     */
    @Override
    public String getValue(Long configId) {
        return configDao.getValue(configId);
    }
}
