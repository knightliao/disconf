package com.baidu.disconf.web.service.config.dao.impl;

import org.springframework.stereotype.Service;

import com.baidu.disconf.web.service.config.bo.ConfigHistory;
import com.baidu.disconf.web.service.config.dao.ConfigHistoryDao;
import com.baidu.dsp.common.dao.AbstractDao;

/**
 * Created by knightliao on 15/12/25.
 */
@Service
public class ConfigHistoryDaoImpl extends AbstractDao<Long, ConfigHistory> implements ConfigHistoryDao {
}
