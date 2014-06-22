package com.baidu.disconf2.service.config.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<String> getConfByAppId(Long appId) {

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
     * 
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

                        ConfListVo confListVo = new ConfListVo();

                        String appNameString = appMap.get(input.getAppId())
                                .getName();
                        String envName = envMap.get(input.getEnvId()).getName();

                        confListVo.setAppId(input.getAppId());
                        confListVo.setAppName(appNameString);
                        confListVo.setEnvName(envName);
                        confListVo.setEnvId(input.getEnvId());
                        confListVo.setCreateTime(input.getCreateTime());
                        confListVo.setModifyTime(input.getUpdateTime());
                        confListVo.setKey(input.getName());
                        confListVo.setValue(input.getValue());
                        confListVo.setVersion(input.getVersion());
                        confListVo.setType(DisConfigTypeEnum.getByType(
                                input.getType()).getModelName());

                        return confListVo;
                    }
                });

        return configListVo;
    }
}
