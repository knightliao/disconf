package com.baidu.disconf2.service.config.dao.impl;

import org.springframework.stereotype.Service;

import com.baidu.disconf2.service.config.bo.Config;
import com.baidu.disconf2.service.config.dao.ConfigDao;
import com.baidu.dsp.common.dao.AbstractDao;

/**
 * 
 * @author liaoqiqi
 * @version 2014-6-16
 */
@Service
public class ConfigDaoImpl extends AbstractDao<Long, Config> implements
        ConfigDao {

}
