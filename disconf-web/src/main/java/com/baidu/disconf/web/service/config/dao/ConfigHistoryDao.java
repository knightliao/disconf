package com.baidu.disconf.web.service.config.dao;

import com.baidu.disconf.web.service.config.bo.ConfigHistory;
import com.baidu.disconf.web.service.config.vo.ConfHistoryListVo;
import com.baidu.dsp.common.form.RequestListBase;
import com.baidu.ub.common.db.DaoPageResult;
import com.baidu.unbiz.common.genericdao.dao.BaseDao;

/**
 * Created by knightliao on 15/12/25.
 */

public interface ConfigHistoryDao extends BaseDao<Long, ConfigHistory> {
    /**
     * @param appId
     * @param envId
     * @param version
     * @param configName
     * @return
     */
    DaoPageResult<ConfHistoryListVo> getConfigHistoryList(Long appId, Long envId, String version,String configName, RequestListBase.Page page);
}
