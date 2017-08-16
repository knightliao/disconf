package com.baidu.disconf.web.service.config.dao.impl;

import com.baidu.disconf.web.common.Constants;
import com.baidu.disconf.web.service.config.bo.ConfigHistory;
import com.baidu.disconf.web.service.config.dao.ConfigHistoryDao;
import com.baidu.disconf.web.service.config.vo.ConfHistoryListVo;
import com.baidu.dsp.common.dao.AbstractDao;
import com.baidu.dsp.common.dao.Columns;
import com.baidu.dsp.common.form.RequestListBase;
import com.baidu.dsp.common.utils.DaoUtils;
import com.baidu.ub.common.db.DaoPage;
import com.baidu.ub.common.db.DaoPageResult;
import com.baidu.unbiz.common.genericdao.operator.Match;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chen Hui
 */
@Service
public class ConfigHistoryDaoImpl extends AbstractDao<Long, ConfigHistory> implements ConfigHistoryDao {

    @Override
    public DaoPageResult<ConfHistoryListVo> getConfigHistoryList(Long appId, Long envId, String version, String configName, RequestListBase.Page page) {
        int offset = 0;
        int limit = 20;
        if (page.getPageNo() >= 1 && page.getPageSize() > 0) {
            offset = (page.getPageNo() - 1) * page.getPageSize();
            limit = page.getPageSize();
        }

        String sql = "select ch.id,a.name as appName, e.name as envName,c.version,c.name as configName,ch.old_value as oldValue,ch.new_value as newValue,ur.name as operator,ch.create_time as updateTime " +
                "from config_history ch " +
                "left join config c " +
                "on ch.config_id = c.config_id " +
                "left join env e " +
                "on c.env_id = e.env_id " +
                "left join app a " +
                "on c.app_id = a.app_id " +
                "left join user_role ur " +
                "on ch.update_by = ur.user_id " +
                "where " + ((appId == null||appId == 0)?"1=1 ":"a.app_id = '" + appId + "' ")+
                "and " + ((envId == null||envId == 0)?"1=1 ":"e.env_id = '" + envId + "' ")+
                "and " + (StringUtils.isBlank(version)?"1=1 ":"c.version = '" + version + "' ")+
                "and " + (StringUtils.isBlank(configName)?"1=1 ":"c.name like '%" + configName + "%' ")+
                "order by ch.create_time desc "+
                "limit "+offset+","+ limit+" ";
        String sqlCount = "select count(1) " +
                "from config_history ch " +
                "left join config c " +
                "on ch.config_id = c.config_id " +
                "left join env e " +
                "on c.env_id = e.env_id " +
                "left join app a " +
                "on c.app_id = a.app_id " +
                "left join user_role ur " +
                "on ch.update_by = ur.user_id " +
                "where " + ((appId == null||appId == 0)?"1=1 ":"a.app_id = '" + appId + "' ")+
                "and " + ((envId == null||envId == 0)?"1=1 ":"e.env_id = '" + envId + "' ")+
                "and " + (StringUtils.isBlank(version)?"1=1 ":"c.version = '" + version + "' ")+
                "and " + (StringUtils.isBlank(configName)?"1=1 ":"c.name like' " + configName + "' ");
        List<ConfHistoryListVo> configHistoryList = jdbcTemplate.query(sql, new RowMapper<ConfHistoryListVo>() {
            @Override
            public ConfHistoryListVo mapRow(ResultSet resultSet, int i) throws SQLException {
                ConfHistoryListVo confHistoryListVo = new ConfHistoryListVo();
                confHistoryListVo.setId(resultSet.getLong("id"));
                confHistoryListVo.setAppName(resultSet.getString("appName"));
                confHistoryListVo.setEnvName(resultSet.getString("envName"));
                confHistoryListVo.setVersion(resultSet.getString("version"));
                confHistoryListVo.setConfigName(resultSet.getString("configName"));
                confHistoryListVo.setOldValue(resultSet.getString("oldValue"));
                confHistoryListVo.setNewValue(resultSet.getString("newValue"));
                confHistoryListVo.setUpdateBy(resultSet.getString("operator"));
                confHistoryListVo.setUpdateTime(resultSet.getString("updateTime"));
                return confHistoryListVo;
            }
        });
        int count = jdbcTemplate.queryForObject(sqlCount, Integer.class);
        DaoPageResult<ConfHistoryListVo> daoPageResult = new DaoPageResult<>();

        // 结果
        daoPageResult.setResult(configHistoryList);
        // 总数
        daoPageResult.setTotalCount(count);
        return daoPageResult;
    }
}
