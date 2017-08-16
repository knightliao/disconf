package com.baidu.disconf.web.service.config.service;

import com.baidu.disconf.web.service.config.form.ConfHistoryListForm;
import com.baidu.disconf.web.service.config.vo.ConfHistoryListVo;
import com.baidu.ub.common.db.DaoPageResult;

/**
 * Created by Chen Hui.
 */
public interface ConfigHistoryMgr {

    void createOne(Long configId, String oldValue, String newValue);

    DaoPageResult<ConfHistoryListVo> getConfigHistoryList(ConfHistoryListForm confHistoryListForm);

    String getConfigHistoryValue(long id, int type);
}
