package com.baidu.disconf2.service.config.dao.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baidu.disconf2.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf2.service.config.bo.Config;
import com.baidu.disconf2.service.config.dao.ConfigDao;
import com.baidu.dsp.common.dao.AbstractDao;
import com.baidu.dsp.common.dao.Columns;
import com.baidu.ub.common.generic.dao.operator.Match;

/**
 * 
 * @author liaoqiqi
 * @version 2014-6-16
 */
@Service
public class ConfigDaoImpl extends AbstractDao<Long, Config> implements
        ConfigDao {

    /**
     * 
     */
    @Override
    public Config getByParameter(Long appId, Long envId, String version,
            String key, DisConfigTypeEnum disConfigTypeEnum) {

        return findOne(new Match(Columns.APP_ID, appId), new Match(
                Columns.ENV_ID, envId), new Match(Columns.VERSION, version),
                new Match(Columns.TYPE, disConfigTypeEnum.getType()),
                new Match(Columns.NAME, key));
    }

    @Override
    public List<Config> getConfByAppname(Long appId) {

        return find(new Match(Columns.APP_ID, appId));

    }
}
