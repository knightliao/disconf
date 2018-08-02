package com.baidu.disconf.web.service.config.service.impl;

import com.baidu.disconf.web.service.config.bo.ConfigHistory;
import com.baidu.disconf.web.service.config.dao.ConfigHistoryDao;
import com.baidu.disconf.web.service.config.form.ConfHistoryListForm;
import com.baidu.disconf.web.service.config.service.ConfigHistoryMgr;
import com.baidu.disconf.web.service.config.vo.ConfHistoryListVo;
import com.baidu.disconf.web.service.user.dto.Visitor;
import com.baidu.dsp.common.constant.DataFormatConstants;
import com.baidu.ub.common.commons.ThreadContext;
import com.baidu.ub.common.db.DaoPageResult;
import com.github.knightliao.apollo.utils.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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

        Visitor visitor = ThreadContext.getSessionVisitor();
        if (visitor != null) {
            configHistory.setUpdateBy(visitor.getLoginUserId());
        }

        String curTime = DateUtils.format(new Date(), DataFormatConstants.COMMON_TIME_FORMAT);
        configHistory.setCreateTime(curTime);

        configHistoryDao.create(configHistory);
    }


    /**
     * 历史配置列表
     */
    @Override
    public DaoPageResult<ConfHistoryListVo> getConfigHistoryList(ConfHistoryListForm confHistoryListForm) {

        DaoPageResult<ConfHistoryListVo> configHistoryList = configHistoryDao.getConfigHistoryList(confHistoryListForm.getAppId(), confHistoryListForm.getEnvId(),
                confHistoryListForm.getVersion(), confHistoryListForm.getConfigName(), confHistoryListForm.getPage());

        return configHistoryList;
    }

    @Override
    public String getConfigHistoryValue(long id, int type){
        ConfigHistory configHistory = configHistoryDao.get(id);
        if(type==1){
            return configHistory.getOldValue();
        }else{
            return configHistory.getNewValue();
        }
    }

}
