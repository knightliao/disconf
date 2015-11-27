package com.baidu.dsp.common.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.baidu.ub.common.db.DaoPage;
import com.baidu.ub.common.db.DaoPageResult;
import com.baidu.unbiz.common.genericdao.dao.GenericDao;
import com.baidu.unbiz.common.genericdao.operator.Match;
import com.baidu.unbiz.common.genericdao.operator.Modify;
import com.baidu.unbiz.common.genericdao.operator.Order;
import com.baidu.unbiz.common.genericdao.param.BetweenParam;
import com.baidu.unbiz.common.genericdao.param.ExpressionParam;
import com.baidu.unbiz.common.genericdao.param.GreaterThanParam;
import com.baidu.unbiz.common.genericdao.param.IncrParam;
import com.baidu.unbiz.common.genericdao.param.LessThanParam;
import com.baidu.unbiz.common.genericdao.param.LikeParam;
import com.baidu.unbiz.common.genericdao.param.NotParam;
import com.github.knightliao.apollo.db.bo.BaseObject;

/**
 */
public abstract class AbstractDao<KEY extends Serializable, ENTITY extends BaseObject<KEY>> extends
        GenericDao<KEY, ENTITY> {

    protected static final Logger LOG = LoggerFactory.getLogger(AbstractDao.class);

    @Override
    @Resource(name = "onedbJdbcTemplate")
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        super.setJdbcTemplate(jdbcTemplate);
    }

    public KEY getNextId() {
        // 好像没用到@Sequence注解
        return null;
    }

    /**
     * 该类是否有dspId一列
     */
    public AbstractDao() {
        super();
    }

    @Override
    public void recordLog(String sLog) {
        LOG.debug(sLog);
    }

    // modified by liqingyun
    protected Object like(String searchWord) {
        return new LikeParam(searchWord);
    }

    protected Object between(Object start, Object end) {
        return new BetweenParam(start, end);
    }

    protected Object greaterThan(Object value) {
        return new GreaterThanParam(value);
    }

    protected Object lessThan(Object value) {
        return new LessThanParam(value);
    }

    protected Object express() {
        return new ExpressionParam();
    }

    protected Object not(Object value) {
        return new NotParam(value);
    }

    protected Object incr(Number value) {
        return new IncrParam(value);
    }

    public Order order(final String column, final boolean asc) {
        return new Order(column, asc);
    }

    public Match match(final String column, final Object value) {
        return new Match(column, value);
    }

    public Modify modify(String column, Object value) {
        return new Modify(column, value);
    }

    public final List<Object> toList(Object... os) {
        if (os == null || os.length == 0) {
            return new ArrayList<Object>(0);
        }
        ArrayList<Object> list = new ArrayList<Object>(os.length);
        for (Object o : os) {
            list.add(o);
        }
        return list;
    }

    /**
     * 新版的Page，返回TotalCount
     *
     * @param matches
     * @param daoPage
     *
     * @return
     */
    public DaoPageResult<ENTITY> page2(List<Match> matches, DaoPage daoPage) {

        DaoPageResult<ENTITY> daoPageResult = new DaoPageResult<ENTITY>();

        // 结果
        List<ENTITY> result = page(matches, daoPage);
        daoPageResult.setResult(result);

        // 总数
        int totalCount = count(matches);
        daoPageResult.setTotalCount(totalCount);

        return daoPageResult;
    }

    /**
     * 根据查询条件获取结果集列表
     *
     * @param matches
     *
     * @return
     */
    private List<ENTITY> page(List<Match> matches, DaoPage daoPage) {

        if (daoPage.getPageNo() < 1 || daoPage.getPageSize() < 0) {
            return find(matches, daoPage.getOrderList());
        }

        int offset = (daoPage.getPageNo() - 1) * daoPage.getPageSize();
        int limit = daoPage.getPageSize();
        return find(matches, daoPage.getOrderList(), offset, limit);
    }

}
