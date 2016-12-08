package com.baidu.disconf.web.service.config.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.baidu.disconf.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf.web.common.Constants;
import com.baidu.disconf.web.service.config.bo.Config;
import com.baidu.disconf.web.service.config.dao.ConfigDao;
import com.baidu.disconf.web.service.config.form.ConfForm;
import com.baidu.dsp.common.constant.DataFormatConstants;
import com.baidu.dsp.common.dao.AbstractDao;
import com.baidu.dsp.common.dao.Columns;
import com.baidu.dsp.common.form.RequestListBase.Page;
import com.baidu.dsp.common.utils.DaoUtils;
import com.baidu.ub.common.db.DaoPage;
import com.baidu.ub.common.db.DaoPageResult;
import com.baidu.unbiz.common.genericdao.operator.Match;
import com.baidu.unbiz.common.genericdao.operator.Modify;
import com.baidu.unbiz.common.genericdao.operator.Order;
import com.github.knightliao.apollo.utils.time.DateUtils;

/**
 * @author liaoqiqi
 * @version 2014-6-16
 */
@Service
public class ConfigDaoImpl extends AbstractDao<Long, Config> implements ConfigDao {

	
    /**
     *
     */
    @Override
    public Config getByParameter(Long appId, Long envId, String version, String key,
                                 DisConfigTypeEnum disConfigTypeEnum) {

        return findOne(new Match(Columns.APP_ID, appId), new Match(Columns.ENV_ID, envId),
                new Match(Columns.VERSION, version), new Match(Columns.TYPE, disConfigTypeEnum.getType()),
                new Match(Columns.NAME, key), new Match(Columns.STATUS, Constants.STATUS_NORMAL));
    }

    /**
     *
     */
    @Override
    public List<Config> getConfByAppEnv(Long appId, Long envId) {
        if (envId == null) {
            return find(new Match(Columns.APP_ID, appId), new Match(Columns.STATUS, Constants.STATUS_NORMAL));
        } else {

            return find(new Match(Columns.APP_ID, appId), new Match(Columns.ENV_ID, envId),
                    new Match(Columns.STATUS, Constants.STATUS_NORMAL));

        }
    }

    /**
     *
     */
    @Override
    public DaoPageResult<Config> getConfigList(Long appId, Long envId, String version, Page page) {

        DaoPage daoPage = DaoUtils.daoPageAdapter(page);
        List<Match> matchs = new ArrayList<Match>();

        matchs.add(new Match(Columns.APP_ID, appId));

        matchs.add(new Match(Columns.ENV_ID, envId));

        matchs.add(new Match(Columns.VERSION, version));

        matchs.add(new Match(Columns.STATUS, Constants.STATUS_NORMAL));

        return page2(matchs, daoPage);
    }

    /**
     *
     */
    @Override
    public List<Config> getConfigList(Long appId, Long envId, String version, Boolean hasValue) {

        List<Match> matchs = new ArrayList<Match>();
        matchs.add(new Match(Columns.APP_ID, appId));
        matchs.add(new Match(Columns.ENV_ID, envId));
        matchs.add(new Match(Columns.VERSION, version));
        matchs.add(new Match(Columns.STATUS, Constants.STATUS_NORMAL));
        if (hasValue) {
            return find(matchs, new ArrayList<Order>());
        } else {
            return findColumns(matchs, new String[] {Columns.CONFIG_ID, Columns.TYPE, Columns.NAME, Columns.CREATE_TIME
                    , Columns.UPDATE_TIME, Columns.STATUS, Columns.APP_ID, Columns.ENV_ID, Columns.VERSION});
        }
    }

    /**
     * @param configId
     */
    @Override
    public void deleteItem(Long configId) {
        String curTime = DateUtils.format(new Date(), DataFormatConstants.COMMON_TIME_FORMAT);
        List<Modify> modifyList = new ArrayList<Modify>();
        modifyList.add(modify(Columns.STATUS, Constants.STATUS_DELETE));
        modifyList.add(modify(Columns.UPDATE_TIME, curTime));

        update(modifyList, match(Columns.CONFIG_ID, configId));
    }

    /**
     *
     */
    @Override
    public void updateValue(Long configId, String value) {

        // 时间
        String curTime = DateUtils.format(new Date(), DataFormatConstants.COMMON_TIME_FORMAT);

        List<Modify> modifyList = new ArrayList<Modify>();
        modifyList.add(modify(Columns.VALUE, value));
        modifyList.add(modify(Columns.UPDATE_TIME, curTime));

        update(modifyList, match(Columns.CONFIG_ID, configId));
    }

    @Override
    public String getValue(Long configId) {
        Config config = get(configId);
        return config.getValue();
    }

	@Override
	public List<Config> getConfigList(ConfForm confForm) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select t1.* from");
		sql.append(" config t1");
		sql.append(" left join app t2 on t1.app_id = t2.app_id");
		sql.append(" left join env t3 on t1.env_id = t3.env_id");
		sql.append(" where t1.version=?");
		sql.append(" and t1.name=?");
		sql.append(" and t2.name=?");
		sql.append(" and t3.name=?");
		return this.jdbcTemplate.query(sql.toString(),new Object[]{confForm.getVersion(),confForm.getKey(),confForm.getApp(),confForm.getEnv()}, new BeanPropertyRowMapper<Config>(Config.class));
	}
}
