package com.baidu.disconf.web.service.config.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baidu.disconf.web.service.config.bo.ConfigHistory;
import com.baidu.disconf.web.service.config.dao.ConfigHistoryDao;
import com.baidu.disconf.web.service.config.service.ConfigHistoryMgr;
import com.baidu.dsp.common.constant.DataFormatConstants;
import com.github.knightliao.apollo.utils.time.DateUtils;

/**
 * Created by knightliao on 15/12/25.
 */
@Service
public class ConfigHistoryMgrImpl implements ConfigHistoryMgr {

    @Autowired
    private ConfigHistoryDao configHistoryDao;

    @Override
    public void createOne(Long configId, String oldValue, String newValue) {

        ConfigHistory configHistory = new ConfigHistory();

        configHistory.setConfigId(configId);
        configHistory.setOldValue(oldValue);
        configHistory.setNewValue(newValue);

        String curTime = DateUtils.format(new Date(), DataFormatConstants.COMMON_TIME_FORMAT);
        configHistory.setCreateTime(curTime);

        configHistoryDao.create(configHistory);
    }
}
