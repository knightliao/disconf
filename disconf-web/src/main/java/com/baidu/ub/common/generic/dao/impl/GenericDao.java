/**
 * beidou-core-493#com.baidu.beidou.common.dao.impl.BeidouGenericDao.java
 * 下午2:36:54 created by Darwin(Tianxin)
 */
package com.baidu.ub.common.generic.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.CollectionUtils;

import com.baidu.ub.common.commons.ThreadContext;
import com.baidu.ub.common.generic.annotation.Sequence;
import com.baidu.ub.common.generic.annotation.Table;
import com.baidu.ub.common.generic.bo.BaseObject;
import com.baidu.ub.common.generic.dao.BaseDao;
import com.baidu.ub.common.generic.dao.operator.DaoPage;
import com.baidu.ub.common.generic.dao.operator.Match;
import com.baidu.ub.common.generic.dao.operator.Modify;
import com.baidu.ub.common.generic.dao.operator.Order;
import com.baidu.ub.common.generic.dao.operator.Query;
import com.baidu.ub.common.generic.vo.DaoPageResult;
import com.baidu.ub.common.log.AopLogFactory;
import com.baidu.ub.common.sequence.SequenceStub;
import com.baidu.ub.common.utils.ClassUtils;

/**
 * 虚拟的父类
 * 
 * @author Darwin(Tianxin)
 */
public class GenericDao<KEY extends Serializable, ENTITY extends BaseObject<KEY>>
        implements BaseDao<KEY, ENTITY> {

    /**
     * Logger instance
     */
    protected final static Logger LOG = AopLogFactory
            .getLogger(GenericDao.class);

    /**
     * 用来执行查询的jdbcTemplate
     */
    protected JdbcTemplate jdbcTemplate = null;

    /**
     * 该DAO要操作的实体类
     */
    protected Class<ENTITY> entityClass = null;

    /**
     * 主键的对象类型
     */
    protected Class<KEY> keyClass = null;

    /**
     * 所对应的表名,千万不能直接调用
     */
    private String tableName = null;

    /**
     * 关联的Sequence名字
     */
    private String sequenceName = null;

    /**
     * 通过getTableName获取表名
     * 
     * @return the tableName
     */
    public String getTableName() {
        Number shardKey = ThreadContext.getShardKey();
        return getTableName(this.tableName, shardKey);
    }

    /**
     * 获取shardKey所在的表名
     * 
     * @return
     */
    public String getTableName(String table, Number shardKey) {
        if (shardCount <= 1 || shardKey == null || shardKey.longValue() <= 0) {
            return table;
        } else {
            return table + calShardNum(shardKey.longValue());
        }
    }

    protected final int calShardNum(long shardKey) {
        return (int) (shardKey % shardCount);
    }

    /**
     * 主键的名字
     */
    protected String keyColumnName = null;

    protected GenericMapper<ENTITY, KEY> defaultMapper = null;

    /**
     * 分为多少个表切片
     */
    protected int shardCount = 0;

    @SuppressWarnings("unchecked")
    public GenericDao() {

        Class<?> myClass;

        if (getClass().getGenericSuperclass() instanceof ParameterizedType) {
            myClass = getClass();
        } else {
            myClass = getClass().getSuperclass();
        }

        Type[] types = ClassUtils.getGenericTypes(myClass);
        entityClass = (Class<ENTITY>) types[1];
        keyClass = (Class<KEY>) types[0];
        defaultMapper = new GenericMapper<ENTITY, KEY>(entityClass, keyClass);

        analysisEntityClass(entityClass);
        analysisSequenceConfig(entityClass);

    }

    /**
     * 解析class
     * 
     * @param entity
     */
    protected void analysisEntityClass(Class<ENTITY> entityClass) {
        Table t = entityClass.getAnnotation(Table.class);
        if (t != null) {
            this.keyColumnName = t.keyColumn();
            this.tableName = t.db() + "." + t.name();
            this.shardCount = t.shardCount() == 0 ? 1 : t.shardCount();
        }
    }

    /**
     * 解析class
     * 
     * @param entity
     */
    protected void analysisSequenceConfig(Class<ENTITY> entityClass) {
        Sequence sequence = entityClass.getAnnotation(Sequence.class);
        if (sequence != null) {
            this.sequenceName = sequence.name();
            SequenceStub.initialSequence(sequenceName, sequence.size());
        }
    }

    /**
     * @param jdbcTemplate
     *            the jdbcTemplate to set
     */
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ENTITY get(KEY id) {

        if (id == null) {
            LOG.info(" param id is null!");
            return null;
        }

        // 构造SQL语句
        String sql = "select " + defaultMapper.getColumnsExclude() + " from "
                + getTableName() + " where " + keyColumnName + "=?";

        // 查询结果并装载对象
        List<ENTITY> entities = findBySQL(sql, Arrays.asList((Object) id));
        if (entities == null || entities.size() == 0) {
            return null;
        }
        return entities.get(0);
    }

    public List<ENTITY> get(Collection<KEY> ids) {
        if (ids == null || ids.size() == 0) {
            LOG.info(" param id is null!");
            return new ArrayList<ENTITY>();
        }
        return find(keyColumnName, ids);
    }

    /**
     * 查询符合条件的结果计数
     * 
     * @param column
     *            条件字段
     * @param value
     *            条件字段的值。支持集合对象，支持数组，会按照in 操作来组装条件
     * @return
     */
    public int count(String column, Object value) {
        return count(match(column, value));
    }

    /**
     * 查询符合条件组合的记录数
     * 
     * @param matches
     *            查询参数,该参数为"字段名,字段值,字段名,字段值..."的排列
     * @return
     */
    public int count(Match... matches) {
        // 构造SQL语句和参数列表的对象
        int maxSqlLength = matches == null ? 50 : (matches.length + 3) * 10;
        StringBuilder sb = new StringBuilder(maxSqlLength);
        List<Object> params = new LinkedList<Object>();

        // SQL语句和参数列表的构造
        sb.append("select count(*) from ").append(getTableName());

        // 如果条件不为空
        appendQuerytoSQL(Arrays.asList(matches), sb, params);

        // 执行操作
        String sql = sb.toString();
        LOG.info(sql);
        return countBySQL(sql, params);
    }

    /**
     * 累加询符合条件组合的某列的值
     * 
     * @param matches
     *            查询参数,该参数为"字段名,字段值,字段名,字段值..."的排列
     * @return
     */
    public Object sum(String column, Class<?> type, Match... matches) {

        // 构造SQL语句和参数列表的对象
        int maxSqlLength = matches == null ? 50 : (matches.length + 3) * 10;
        StringBuilder sb = new StringBuilder(maxSqlLength);
        List<Object> params = new LinkedList<Object>();

        // SQL语句和参数列表的构造
        sb.append("select sum(").append(column).append(") from ")
                .append(getTableName());

        // 如果条件不为空
        appendQuerytoSQL(Arrays.asList(matches), sb, params);

        // 执行操作
        String sql = sb.toString();
        LOG.info(sql);
        return sumBySQL(sql, params, type);
    }

    /**
     * 查询符合条件组合的记录数
     * 
     * @param matches
     * @return 2013-8-26 下午3:04:02 created by wangchongjie
     */
    public int count(List<Match> matches) {
        // 构造SQL语句和参数列表的对象
        int maxSqlLength = matches == null ? 50 : (matches.size() + 3) * 10;
        StringBuilder sb = new StringBuilder(maxSqlLength);
        List<Object> params = new LinkedList<Object>();

        // SQL语句和参数列表的构造
        sb.append("select count(*) from ").append(getTableName());

        // 如果条件不为空
        appendQuerytoSQL(matches, sb, params);

        // 执行操作
        String sql = sb.toString();
        LOG.info(sql);
        return countBySQL(sql, params);
    }

    @SuppressWarnings("rawtypes")
    private void analysisModify(List<Modify> modifies, StringBuilder sb,
            List<Object> params) {

        for (Modify modify : modifies) {

            String column = modify.getColumn();
            Object value = modify.getValue();
            if (value instanceof GenericDao.IncrParam) {
                sb.append(column).append("=(").append(column).append("+?),");
                params.add(((GenericDao.IncrParam) value).value);
            } else {
                sb.append(column).append("=?,");
                params.add(value);
            }
        }
        sb.deleteCharAt(sb.length() - 1);
    }

    /**
     * 将query变成一个查询条件的字符串，放到SQL中
     * 
     * @param matches
     * @param sb
     * @param params
     */
    protected void appendQuerytoSQL(List<Match> matches, StringBuilder sb,
            List<Object> params) {

        // 如果query为空，则直接返回
        if (matches == null || matches.size() == 0)
            return;

        sb.append(" where ");
        analysisQuery(matches, sb, params);
    }

    @SuppressWarnings("unchecked")
    private void analysisQuery(List<Match> matches, StringBuilder sb,
            List<Object> params) {
        for (Match match : matches) {

            Object value = match.getValue();
            String columns = match.getColumn();
            if (value == null) {
                sb.append(columns).append(" is null");
            } else if (value instanceof GenericDao.LikeParam) {
                sb.append(columns).append(" like ?");
                params.add('%' + ((LikeParam) value).word + '%');
            } else if (value instanceof GenericDao.BetweenParam) {
                BetweenParam bp = (BetweenParam) value;
                Object start = bp.start;
                Object end = bp.end;
                if (start == null && end == null) {
                    sb.append("1!=1");
                } else if (start == null) {
                    sb.append(columns).append(" <= ?");
                    params.add(end);
                } else if (end == null) {
                    sb.append(columns).append(" >= ?");
                    params.add(start);
                } else {
                    sb.append(columns).append(" between ? and ?");
                    params.add(start);
                    params.add(end);
                }
            } else if (value instanceof GenericDao.GreaterThanParam) {
                sb.append(columns).append(" > ?");
                params.add(((GreaterThanParam) value).value);
            } else if (value instanceof GenericDao.LessThanParam) {
                sb.append(columns).append(" < ?");
                params.add(((LessThanParam) value).value);
            } else if (value instanceof GenericDao.ExpressionParam) {
                sb.append(columns);
            } else {

                // 如果是取反的参数
                boolean reverse = value instanceof GenericDao.NotParam;
                if (reverse)
                    value = ((NotParam) value).value;

                if (value == null && reverse) {
                    sb.append(columns).append(" is not null");
                } else if (value instanceof Collection<?>) {
                    Collection<?> coll = (Collection<?>) value;
                    if (coll.size() == 0) {
                        sb.append(reverse ? "1=1" : "1!=1");
                    } else if (coll.size() == 1) {
                        sb.append(columns).append(reverse ? "!=?" : "=?");
                        params.add(coll.iterator().next());
                    } else {
                        sb.append(columns).append(reverse ? " not in " : " in");
                        sb.append(connectObjects(coll));
                    }
                } else if (value.getClass().isArray()) {
                    Object[] array = (Object[]) value;
                    if (array.length == 0) {
                        sb.append(reverse ? "1=1" : "1!=1");
                    } else if (array.length == 1) {
                        sb.append(columns).append(reverse ? "!=?" : "=?");
                        params.add(array[0]);
                    } else {
                        sb.append(columns).append(reverse ? " not in " : " in");
                        sb.append(connectObjects(array));
                    }
                } else {
                    sb.append(columns).append(reverse ? "!=?" : "=?");
                    params.add(value);
                }
            }
            sb.append(" and ");
        }

        sb.delete(sb.length() - 5, sb.length());
    }

    /**
     * 查询符合条件的结果计数
     * 
     * @param sql
     * @param params
     * @return
     */
    public int countBySQL(String sql, List<?> params) {
        Object[] args = null;
        if (params == null) {
            args = new Object[0];
        } else {
            args = params.toArray(new Object[params.size()]);
        }
        return jdbcTemplate.queryForInt(sql, args);
    }

    /**
     * 查询符合条件的结果计数
     * 
     * @param sql
     * @param params
     * @return
     */
    public Object sumBySQL(String sql, List<?> params, Class<?> type) {
        Object[] args = null;
        if (params == null) {
            args = new Object[0];
        } else {
            args = params.toArray(new Object[params.size()]);
        }
        return jdbcTemplate.queryForObject(sql, args, type);
    }

    /**
     * 根据条件查询若干个字段，加载到ENTITY里
     * 
     * @param matches
     * @param columns
     * @return
     */
    protected List<ENTITY> findColumns(List<Match> matches, String[] columns) {

        // SQL和参数
        int maxSqlLength = matches == null ? 50 : (matches.size() + 3) * 10;
        StringBuilder sb = new StringBuilder(maxSqlLength);
        List<Object> params = new LinkedList<Object>();

        // SQL语句和参数列表的构造
        sb.append("select ");
        for (String column : columns) {
            sb.append(column).append(',');
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" from ").append(getTableName());

        // 如果有条件
        appendQuerytoSQL(matches, sb, params);

        // 执行操作
        String sql = sb.toString();
        return findBySQL(sql, params);
    }

    /**
     * 删除符合条件组合的对象列表
     * 
     * @param params
     *            查询参数,该参数为"字段名,字段值,字段名,字段值..."的排列
     * @return
     */
    public int delete(Match... matches) {
        // 构造SQL语句和参数列表的对象
        int maxSqlLength = matches == null ? 50 : (matches.length + 3) * 10;
        StringBuilder sb = new StringBuilder(maxSqlLength);
        List<Object> params = new LinkedList<Object>();

        // SQL语句和参数列表的构造
        sb.append("delete from ").append(getTableName());

        // 如果有条件
        appendQuerytoSQL(Arrays.asList(matches), sb, params);

        // 执行操作
        String sql = sb.toString();
        return executeSQL(sql, params);
    }

    /**
     * 根据条件删除column=value
     * 
     * @param column
     * @param value
     * @return
     */
    public int delete(String column, Object value) {
        return delete(match(column, value));
    }

    public List<ENTITY> findAll() {
        return find();
    }

    /**
     * 获取查询结果
     * 
     * @param column
     *            条件字段
     * @param value
     *            条件字段的值。支持集合对象，支持数组，会按照in 操作来组装条件
     * @return
     */
    public List<ENTITY> find(String column, Object value) {
        return find(match(column, value));
    }

    /**
     * 查询符合条件组合的对象列表
     * 
     * @param matches
     *            查询参数,多个
     * @return
     */
    public List<ENTITY> find(Match... matches) {
        return find(Arrays.asList(matches), new ArrayList<Order>(0));
    }

    /**
     * 根据查询条件获取结果集列表
     * 
     * @param matches
     * @param order
     * @return
     */
    public List<ENTITY> find(List<Match> matches, Order order) {
        return find(matches, Arrays.asList(order), 0, 0);
    }

    /**
     * 根据查询条件获取结果集列表
     * 
     * @param matches
     * @param order
     * @return
     */
    public List<ENTITY> find(List<Match> matches, List<Order> order) {
        return find(matches, order, 0, 0);
    }

    /**
     * 根据查询条件获取结果集列表
     * 
     * @param matches
     * @param order
     * @param offset
     * @param limit
     * @return
     */
    public List<ENTITY> find(List<Match> matches, List<Order> order,
            int offset, int limit) {

        Query operate = generateSQL(matches, defaultMapper.getColumns(), order,
                offset, limit);

        // 执行操作
        return findBySQL(operate.getSql(), operate.getParams());
    }

    /**
     * 根据查询条件获取结果集列表
     * 
     * @param matches
     * @param order
     * @param curPage
     * @param pageSize
     * @return
     */
    public List<ENTITY> page(List<Match> matches, List<Order> order,
            int curPage, int pageSize) {

        if (curPage < 0 || pageSize < 0) {
            return find(matches, order);
        }

        int offset = (curPage) * pageSize;
        int limit = pageSize;
        return find(matches, order, offset, limit);
    }

    /**
     * 根据查询条件获取结果集列表
     * 
     * @param matches
     * @param order
     * @param curPage
     * @param pageSize
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

    /**
     * 新版的Page，返回TotalCount
     * 
     * @param matches
     * @param daoPage
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
     * sql新版的Page，根据sql返回分页数据
     * 
     * @param sql
     *            : 查询sql，完整的查询语句，e.g.：
     *            "select ecom_dsp.creativity.* from ecom_dsp.creativity, ecom_dsp.ad_creativity where *"
     * @param daoPage
     *            分页数据
     * @return
     */
    public DaoPageResult<ENTITY> pageSql(String sql, DaoPage daoPage) {

        DaoPageResult<ENTITY> daoPageResult = new DaoPageResult<ENTITY>();
        List<ENTITY> result = null;
        // 结果
        if (daoPage.getPageNo() < 1 || daoPage.getPageSize() < 0) {
            result = findBySQL(sql, null);
        }
        int offset = (daoPage.getPageNo() - 1) * daoPage.getPageSize();
        int limit = daoPage.getPageSize();

        // 数据
        String dataSql = sql + String.format(" limit %d, %d", offset, limit);
        result = findBySQL(dataSql, null);
        daoPageResult.setResult(result);

        // 总数
        // replaceAll正则替换："select .* from "
        // 替换"select"到"from "部分子串为"select count(*) from "
        String countSql = sql.toLowerCase().replaceAll("select .* from ",
                "select count(*) from ");
        int totalCount = countBySQL(countSql, null);
        daoPageResult.setTotalCount(totalCount);

        return daoPageResult;
    }

    /**
     * @param matches
     * @param order
     * @param offset
     * @param limit
     * @param params
     * @return 上午11:30:01 created by Darwin(Tianxin)
     */
    protected Query generateSQL(List<Match> matches,
            Collection<String> columnSet, List<Order> order, int offset,
            int limit) {
        // 构造SQL语句和参数列表的对象
        int maxSqlLength = matches == null ? 50 : (matches.size() + 3) * 10;
        StringBuilder sb = new StringBuilder(maxSqlLength);
        List<Object> params = new LinkedList<Object>();

        // SQL语句和参数列表的构造
        sb.append("select ");
        for (String column : columnSet) {
            sb.append(column).append(',');
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" from ").append(getTableName());

        // 如果有条件
        appendQuerytoSQL(matches, sb, params);

        // 加入排序
        if (order != null && order.size() > 0) {
            sb.append(" order by ");
            for (Order entry : order) {
                sb.append(entry.getColumn())
                        .append(entry.isAsc() ? " asc" : " desc").append(',');
            }
            sb.deleteCharAt(sb.length() - 1);
        }

        // 设置limit
        if (limit != 0) {
            sb.append(" limit ?, ?");
            params.add(offset);
            params.add(limit);
        }
        return new Query(sb.toString(), params);
    }

    /**
     * 
     * @param tableName
     * @param columns
     * @param matches
     * @return 上午11:34:03 created by Darwin(Tianxin)
     */
    protected Query generateSQL(String tableName, Collection<String> columns,
            Match... matches) {

        List<Object> params = new LinkedList<Object>();
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        for (String column : columns) {
            sb.append(column).append(',');
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" from ").append(tableName);

        // 如果有条件
        appendQuerytoSQL(Arrays.asList(matches), sb, params);

        return new Query(sb.toString(), params);
    }

    /**
     * 
     * @param tableName
     * @param columns
     * @param matches
     * @return 上午11:34:03 created by Darwin(Tianxin)
     */
    protected Query generateSQL(Collection<String> columns, Match... matches) {
        return generateSQL(getTableName(), columns, matches);
    }

    /**
     * 查询符合条件组合的对象
     * 
     * @param matches
     *            查询参数,该参数为"字段名,字段值,字段名,字段值..."的排列
     * @return
     */
    public ENTITY findOne(Match... matches) {
        List<ENTITY> os = find(Arrays.asList(matches), null, 0, 1);
        if (os == null || os.size() == 0) {
            return null;
        }
        return os.get(0);
    }

    /**
     * 查询符合条件组合的对象列表
     * 
     * @param matches
     *            查询参数,该参数为"字段名,字段值,字段名,字段值..."的排列
     * @return
     */
    public List<ENTITY> findIncludeColumns(Collection<String> includeColumns,
            Match... matches) {
        return findColumns(Arrays.asList(matches),
                includeColumns.toArray(new String[includeColumns.size()]));
    }

    /**
     * 查询符合条件组合的对象列表
     * 
     * @param matches
     *            查询参数,该参数为"字段名,字段值,字段名,字段值..."的排列
     * @return
     */
    public List<ENTITY> findExcludeColumns(Collection<String> excludeColumns,
            Match... matches) {
        Set<String> columns = defaultMapper.getColumns();
        for (String column : excludeColumns) {
            columns.remove(column);
        }
        return findIncludeColumns(columns, matches);
    }

    /**
     * 根据查询条件获取结果集列表
     * 
     * @param sql
     * @param params
     *            无参数时可以为null
     * @return
     */
    public List<ENTITY> findBySQL(String sql, List<?> params) {
        return findBySQL(sql, params, defaultMapper);
    }

    /**
     * 按照SQL语句查询并装在记录为一个列表
     * 
     * @param sql
     * @param params
     * @param mapper
     * @return
     */
    public <T extends Object, O extends Object> List<T> findBySQL(String sql,
            List<O> params, RowMapper<T> mapper) {

        if (sql == null) {
            LOG.info(" param sql is null!");
        }

        // 构造参数
        Object[] args = null;
        if (params == null) {
            args = new Object[0];
        } else {
            args = params.toArray(new Object[params.size()]);
        }

        // 查询
        LOG.info(sql + '[' + connectObjects(params) + ']');
        return (List<T>) jdbcTemplate.query(sql, args, mapper);
    }

    public <T> T findValue(String column, Class<T> tClass, Match... matches) {

        StringBuilder sb = new StringBuilder();
        sb.append("select ").append(column).append(" from ")
                .append(getTableName());

        List<Object> params = new ArrayList<Object>(matches.length + 4);
        appendQuerytoSQL(Arrays.asList(matches), sb, params);
        List<T> ts = findOneColumn(sb.toString(), params, 1, tClass);
        sb.append(" limit 1");
        if (CollectionUtils.isEmpty(ts)) {
            return null;
        } else {
            return ts.get(0);
        }
    }

    public <T> List<T> findOneColumn(String column, Class<T> tClass,
            Match... matches) {

        StringBuilder sb = new StringBuilder();
        sb.append("select ").append(column).append(" from ")
                .append(getTableName());

        List<Object> params = new ArrayList<Object>(matches.length + 4);
        appendQuerytoSQL(Arrays.asList(matches), sb, params);
        return findOneColumn(sb.toString(), params, 1, tClass);
    }

    public <T> List<T> findDistinctColumn(String column, Class<T> tClass,
            Match... matches) {
        return findOneColumn("distinct " + column, tClass, matches);
    }

    /**
     * SequenceServiceImpl.java 查询某一列的值
     * 
     * @param sql
     * @param params
     * @param mapperColumnIndex
     *            要返回的哪一列的值，由1开始。
     * @return
     */
    @SuppressWarnings("unchecked")
    public <N extends Object> List<N> findOneColumn(String sql, List<?> params,
            final int mapperColumnIndex) {

        if (sql == null) {
            LOG.info(" param sql is null!");
        }

        // 查询
        return findBySQL(sql, params, new RowMapper<N>() {

            public N mapRow(ResultSet rs, int count) throws SQLException {
                return (N) (rs.getObject(mapperColumnIndex));
            }
        });
    }

    /**
     * 查询某一列的值
     * 
     * @param sql
     * @param params
     * @param mapperColumnIndex
     *            要返回的那一列的值
     * @param resultClass
     *            要装载成的类型
     * @return
     */
    @SuppressWarnings("unchecked")
    public <N> List<N> findOneColumn(String sql, List<?> params,
            final int mapperColumnIndex, final Class<N> resultClass) {

        if (sql == null) {
            LOG.info(" param sql is null!");
        }

        if (Number.class.isAssignableFrom(resultClass)) {
            if (resultClass.equals(Long.class)) {
                return findBySQL(sql, params, new RowMapper<N>() {
                    public N mapRow(ResultSet rs, int count)
                            throws SQLException {
                        Number n = (Number) (rs.getObject(mapperColumnIndex));
                        return (N) Long.valueOf(n.longValue());
                    }
                });
            } else if (resultClass.equals(Integer.class)) {
                return findBySQL(sql, params, new RowMapper<N>() {
                    public N mapRow(ResultSet rs, int count)
                            throws SQLException {
                        Number n = (Number) (rs.getObject(mapperColumnIndex));
                        return (N) Integer.valueOf(n.intValue());
                    }
                });
            } else if (resultClass.equals(Float.class)) {
                return findBySQL(sql, params, new RowMapper<N>() {
                    public N mapRow(ResultSet rs, int count)
                            throws SQLException {
                        Number n = (Number) (rs.getObject(mapperColumnIndex));
                        return (N) Float.valueOf(n.floatValue());
                    }
                });
            } else if (resultClass.equals(Double.class)) {
                return findBySQL(sql, params, new RowMapper<N>() {
                    public N mapRow(ResultSet rs, int count)
                            throws SQLException {
                        Number n = (Number) (rs.getObject(mapperColumnIndex));
                        return (N) Double.valueOf(n.doubleValue());
                    }
                });
            } else if (resultClass.equals(Short.class)) {
                return findBySQL(sql, params, new RowMapper<N>() {
                    public N mapRow(ResultSet rs, int count)
                            throws SQLException {
                        Number n = (Number) (rs.getObject(mapperColumnIndex));
                        return (N) Short.valueOf(n.shortValue());
                    }
                });
            } else if (resultClass.equals(Byte.class)) {
                return findBySQL(sql, params, new RowMapper<N>() {
                    public N mapRow(ResultSet rs, int count)
                            throws SQLException {
                        Number n = (Number) (rs.getObject(mapperColumnIndex));
                        return (N) Byte.valueOf(n.byteValue());
                    }
                });
            } else {
                return findBySQL(sql, params, new RowMapper<N>() {
                    public N mapRow(ResultSet rs, int count)
                            throws SQLException {
                        Number n = (Number) (rs.getObject(mapperColumnIndex));
                        return (N) n;
                    }
                });
            }
        } else if (resultClass.equals(String.class)) {
            return findBySQL(sql, params, new RowMapper<N>() {
                public N mapRow(ResultSet rs, int count) throws SQLException {
                    return (N) (rs.getString(mapperColumnIndex));
                }
            });
        } else if (resultClass.equals(Date.class)) {
            return findBySQL(sql, params, new RowMapper<N>() {
                public N mapRow(ResultSet rs, int count) throws SQLException {
                    return (N) (rs.getDate(mapperColumnIndex));
                }
            });
        } else {
            throw new RuntimeException("not supported type");
        }
    }

    public boolean update(ENTITY entity) {

        if (entity == null) {
            LOG.info(" param entity is null!");
            return false;
        }

        // 生成SQL语句
        String sql = getUpdateSQL();

        // 获取参数
        List<Object> params = getUpdateParams(entity);
        return executeSQL(sql, params) == 1;
    }

    /**
     * 批量更新
     * 
     * @param entities
     * @return
     */
    public int update(List<ENTITY> entities) {

        if (entities == null || entities.size() == 0) {
            LOG.info(" param entity is null!");
            return 0;
        }

        // 生成SQL语句
        String sql = getUpdateSQL();
        LOG.info(sql);

        // 获取参数列表
        final List<List<Object>> paramsList = new ArrayList<List<Object>>(
                entities.size());
        for (ENTITY o : entities) {
            paramsList.add(getUpdateParams(o));
        }

        LOG.info(paramsList.toString());

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            public void setValues(PreparedStatement stmt, int index)
                    throws SQLException {
                int i = 1;
                List<Object> params = paramsList.get(index);
                for (Object o : params) {
                    stmt.setObject(i++, o);
                }
            }

            public int getBatchSize() {
                return paramsList.size();
            }
        });

        return entities.size();
    }

    public int update(Modify modify, Match... matches) {
        return update(Arrays.asList(modify), matches);
    }

    public int update(List<Modify> modifies, Match... matches) {

        StringBuilder sb = new StringBuilder(100);
        List<Object> params = new ArrayList<Object>(modifies.size()
                + matches.length + 4);

        sb.append("update ").append(getTableName()).append(" set ");
        analysisModify(modifies, sb, params);
        appendQuerytoSQL(Arrays.asList(matches), sb, params);
        return executeSQL(sb.toString(), params);
    }

    public boolean delete(ENTITY e) {
        return delete(e.getId());
    }

    public int delete(Collection<ENTITY> entities) {
        if (entities == null || entities.size() == 0) {
            return 0;
        }
        List<KEY> keys = new ArrayList<KEY>(entities.size());
        for (ENTITY entity : entities) {
            if (entity != null)
                keys.add(entity.getId());
        }

        return delete(keys);
    }

    public boolean delete(KEY id) {
        if (id == null) {
            LOG.info(" param id is null!");
            return false;
        }
        String sql = "delete from " + getTableName() + " where "
                + keyColumnName + "=?";
        return executeSQL(sql, Arrays.asList((Object) id)) == 1;
    }

    public int delete(List<KEY> ids) {
        if (ids == null || ids.size() == 0) {
            LOG.info(" param ids is empty!");
            return 0;
        } else if (Number.class.isAssignableFrom(keyClass)) {
            @SuppressWarnings("unchecked")
            String sql = "delete from " + getTableName() + " where "
                    + keyColumnName + " in " + connect((List<Number>) (ids));
            return executeSQL(sql, null);
        } else {
            String sql = "delete from " + getTableName() + " where "
                    + keyColumnName + " in " + connectObjects(ids);
            return executeSQL(sql, null);
        }
    }

    /**
     * 执行SQL语句
     * 
     * @param sql
     * @param params
     * @return
     */
    public <E extends Object> int executeSQL(String sql, List<E> params) {
        LOG.info(sql + '[' + connectObjects(params) + ']');
        if (params == null || params.size() == 0) {
            return jdbcTemplate.update(sql);
        } else {
            return jdbcTemplate.update(sql,
                    params.toArray(new Object[params.size()]));
        }
    }

    /**
     * 将数字集合append为(n1,n2,n3)这种格式的字符串
     * 
     * @param ns
     * @return
     */
    public static final <T extends Number> String connect(Collection<T> ns) {
        StringBuilder sb = new StringBuilder(ns.size() * 8);
        for (Number o : ns) {
            sb.append(',').append(o);
        }
        sb.setCharAt(0, '(');
        sb.append(')');
        return sb.toString();
    }

    /**
     * 将数字集合append为( 'n1','n2','n3')这种格式的字符串
     * 
     * @param ns
     * @return
     */
    public static final String connectObjects(Collection<?> ns) {

        if (ns == null || ns.size() == 0) {
            return "()";
        }

        StringBuilder sb = new StringBuilder(ns.size() * 8);
        boolean number = ns.iterator().next() instanceof Number;
        if (number) {
            for (Object n : ns) {
                sb.append(',').append(n);
            }
            sb.setCharAt(0, '(');
            sb.append(')');
        } else {
            for (Object o : ns) {
                sb.append("','").append(o);
            }
            sb.setCharAt(0, ' ');
            sb.setCharAt(1, '(');
            sb.append("')");

        }
        return sb.toString();
    }

    /**
     * 将数字集合append为( 'n1','n2','n3')这种格式的字符串
     * 
     * @param ns
     * @return
     */
    public static final String connectObjects(Object[] ns) {

        if (ns == null || ns.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder(ns.length * 8);

        boolean number = ns[0] instanceof Number;
        if (number) {
            for (Object n : ns) {
                sb.append(',').append(n);
            }
            sb.setCharAt(0, '(');
            sb.append(')');
        } else {
            for (Object o : ns) {
                sb.append("','").append(o);
            }
            sb.setCharAt(0, ' ');
            sb.setCharAt(1, '(');
            sb.append("')");

        }
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    public ENTITY create(ENTITY entity) {

        // 如果空值则返回
        if (entity == null) {
            LOG.info(" param entity is null!");
            return null;
        }

        // 如果需要DAO层代理生成新ID
        if (sequenceName != null) {
            entity.setId(getNextId());
        }

        // 判断是否为自动设置的ID
        KEY key = entity.getId();
        boolean autoGeneratedKey = (key == null || (Long.parseLong(key
                .toString()) == 0));

        // 获取insert语句
        final String sql = getInsertSQL(autoGeneratedKey, 1);
        final List<Object> params = getInsertParams(entity, autoGeneratedKey);

        // 如果非自动生成key则返回
        if (!autoGeneratedKey) {
            int rowCount = executeSQL(sql, params);
            return rowCount == 1 ? entity : null;

            // 如果自动生成key,生成KeyHolder
        } else {

            KeyHolder keyHolder = new GeneratedKeyHolder();
            LOG.info(sql + '[' + connectObjects(params) + ']');

            // 执行操作
            int rowCount = this.jdbcTemplate.update(
                    new PreparedStatementCreator() {
                        public PreparedStatement createPreparedStatement(
                                Connection connection) throws SQLException {
                            PreparedStatement ps = connection
                                    .prepareStatement(sql);
                            int index = 1;
                            for (Object param : params)
                                ps.setObject(index++, param);
                            return ps;
                        }
                    }, keyHolder);

            // 如果插入成功则获取keyHolder中的key
            if (rowCount != 0) {

                if (keyClass.equals(Integer.class)) {
                    entity.setId((KEY) Integer.valueOf(keyHolder.getKey()
                            .intValue()));
                } else if (keyClass.equals(Long.class)) {
                    entity.setId((KEY) Long.valueOf(keyHolder.getKey()
                            .longValue()));
                }
            }

            return rowCount == 1 ? entity : null;
        }
    }

    /**
     * @return 下午2:03:46 created by Darwin(Tianxin)
     */
    @SuppressWarnings("unchecked")
    protected KEY getNextId() {
        if (keyClass.equals(Integer.class)) {
            Integer nextId = SequenceStub.getIntKey(sequenceName);
            return (KEY) nextId;
        } else if (keyClass.equals(Long.class)) {
            Long nextId = SequenceStub.getKey(sequenceName);
            return (KEY) nextId;
        } else if (keyClass.equals(String.class)) {
            Long nextId = SequenceStub.getKey(sequenceName);
            return (KEY) nextId.toString();
        } else {
            throw new RuntimeException("not support sequence type!");
        }
    }

    public int create(List<ENTITY> entities) {

        if (entities == null || entities.size() == 0) {
            LOG.info(" param entities is empty!");
            return 0;
        }

        // 如果需要DAO层设置ID
        if (sequenceName != null) {
            for (ENTITY e : entities) {
                e.setId(getNextId());
            }
        }

        // 以第一个元素确定是否自动生成key
        KEY key = entities.get(0).getId();
        boolean autoGeneratedKey = (key == null || (Long.parseLong(key
                .toString()) == 0));

        // 获取insert语句
        String sql = getInsertSQL(autoGeneratedKey, entities.size());
        LOG.info(sql);

        // 获取参数列表
        List<Object> params = new ArrayList<Object>();
        for (ENTITY o : entities) {
            params.addAll(getInsertParams(o, autoGeneratedKey));
        }

        // 执行批量插入操作
        executeSQL(sql, params);
        return entities.size();
    }

    /**
     * 获取该ENTITY的插入语句
     * 
     * @param autoGenerateKey
     *            是否要插入主键字段
     * @param batchCount
     *            批量插入的记录数
     * @return
     */
    protected String getInsertSQL(boolean autoGenerateKey, int batchCount) {

        // 获取ENTITY的分析结果
        List<Map.Entry<String, MethodPair>> columns = defaultMapper
                .getEntries();

        // 分配存储SQL前半段和后半段的buf,与参数存储集合
        StringBuilder sb = new StringBuilder(columns.size() * 16);
        sb.append("insert into ").append(getTableName()).append('(');

        // 循环加载字段信息
        int propertyCount = 0;
        for (Entry<String, MethodPair> entry : columns) {

            // 如果不需要处理key字段
            String column = entry.getKey();
            if (autoGenerateKey && column.equalsIgnoreCase(keyColumnName)) {
                continue;
            }

            // 前半段为(column1, column2...)
            sb.append(column).append(',');
            propertyCount += 1;
        }

        // 将前半段的结尾的,替换为)，并将后半段append进来
        sb.setCharAt(sb.length() - 1, ')');
        sb.append(" values ");

        // 修正batchCount
        if (batchCount < 1) {
            batchCount = 1;
        }

        // 拼接后面的问号
        for (int i = 0; i < batchCount; i++) {
            sb.append('(');
            for (int j = 0; j < propertyCount; j++) {
                sb.append("?,");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("),");
        }
        sb.deleteCharAt(sb.length() - 1);

        // 构成SQL语句
        return sb.toString();
    }

    /**
     * 获取该ENTITY的update语句
     * 
     * @return
     */
    protected String getUpdateSQL() {
        // 获取ENTITY的分析结果
        List<Map.Entry<String, MethodPair>> columns = defaultMapper
                .getEntries();

        // 分配存储SQL前半段和后半段的buf,与参数存储集合
        StringBuilder sb = new StringBuilder(columns.size() * 16);

        // append前半段的内容
        sb.append("update ").append(getTableName()).append(" set ");

        // 循环加载字段信息
        for (Entry<String, MethodPair> entry : columns) {
            String column = entry.getKey();
            if (column.equals(keyColumnName)
                    || defaultMapper.getCantBeModifiedColumns()
                            .contains(column)) {
                continue;
            }
            sb.append("`").append(column).append("`").append("=?,");
        }

        sb.setCharAt(sb.length() - 1, ' ');
        sb.append("where ").append(keyColumnName).append("=?");

        // 构成SQL语句
        return sb.toString();
    }

    /**
     * 获取该ENTITY的插入语句的参数
     * 
     * @param entity
     *            插入记录时，是否插入主键字段
     * @return
     */
    protected List<Object> getUpdateParams(ENTITY entity) {
        // 获取ENTITY的分析结果
        List<Map.Entry<String, MethodPair>> columns = defaultMapper
                .getEntries();
        List<Object> params = new LinkedList<Object>();
        Object key = null;

        // 循环加载字段信息
        for (Entry<String, MethodPair> entry : columns) {

            // 如果不需要处理key字段
            String column = entry.getKey();
            if (column.equals(keyColumnName)) {
                try {
                    key = entry.getValue().getter.invoke(entity, new Object[0]);
                } catch (Exception e) {
                }
                continue;
            }

            // 不加入eclude的字段
            if (defaultMapper.getCantBeModifiedColumns().contains(column)) {
                continue;
            }

            // 将该字段对应的参数加入参数列表
            try {
                params.add(entry.getValue().getter
                        .invoke(entity, new Object[0]));
            } catch (Exception e) {
                throw new RuntimeException("利用反射执行get方法出错!", e);
            }
        }
        params.add(key);
        return params;
    }

    /**
     * 获取该ENTITY的插入语句的参数
     * 
     * @param entity
     * @param autoGenerateKey
     *            插入记录时，是否插入主键字段
     * @return
     */
    protected List<Object> getInsertParams(ENTITY entity,
            boolean autoGenerateKey) {
        // 获取ENTITY的分析结果
        List<Map.Entry<String, MethodPair>> columns = defaultMapper
                .getEntries();
        List<Object> params = new LinkedList<Object>();

        // 循环加载字段信息
        for (Entry<String, MethodPair> entry : columns) {

            // 如果不需要处理key字段
            String column = entry.getKey();
            if (autoGenerateKey && column.equalsIgnoreCase(keyColumnName)) {
                continue;
            }

            // 将该字段对应的参数加入参数列表
            try {
                params.add(entry.getValue().getter
                        .invoke(entity, new Object[0]));
            } catch (Exception e) {
                throw new RuntimeException("利用反射执行get方法出错!", e);
            }
        }
        return params;
    }

    /**
     * 构造like查询的参数
     * 
     * @param searchWord
     * @return
     */
    protected Object like(String searchWord) {
        return this.new LikeParam(searchWord);
    }

    /**
     * 封装like参数
     * 
     * @author Darwin(Tianxin)
     */
    private final class LikeParam {
        String word;

        public LikeParam(String word) {
            this.word = word;
        }
    }

    /**
     * 构造between查询的参数
     * 
     * @param start
     * @param end
     * @return 上午10:24:53 created by Darwin(Tianxin)
     */
    protected Object between(Object start, Object end) {
        return this.new BetweenParam(start, end);
    }

    /**
     * 封装between参数
     * 
     * @author Darwin(Tianxin)
     */
    private final class BetweenParam {
        Object start;
        Object end;

        public BetweenParam(Object start, Object end) {
            this.start = start;
            this.end = end;
        }
    }

    /**
     * 构造大于参数
     * 
     * @param value
     * @return 上午10:32:37 created by Darwin(Tianxin)
     */
    protected Object greaterThan(Object value) {
        return this.new GreaterThanParam(value);
    }

    /**
     * 封装大于的参数
     * 
     * @author Darwin(Tianxin)
     */
    private final class GreaterThanParam {
        Object value;

        public GreaterThanParam(Object value) {
            this.value = value;
        }
    }

    /**
     * 构造小于参数
     * 
     * @param value
     * @return 上午10:32:52 created by Darwin(Tianxin)
     */
    protected Object lessThan(Object value) {
        return this.new LessThanParam(value);
    }

    /**
     * 封装小于的参数
     * 
     * @author Darwin(Tianxin)
     */
    private final class LessThanParam {
        Object value;

        public LessThanParam(Object value) {
            this.value = value;
        }
    }

    /**
     * 构造表达式参数
     * 
     * @return 上午10:33:02 created by Darwin(Tianxin)
     */
    protected Object express() {
        return this.new ExpressionParam();
    }

    /**
     * 封装表达式的参数
     * 
     * @author Darwin(Tianxin)
     */
    private final class ExpressionParam {
        public ExpressionParam() {
        }
    }

    /**
     * 构造反向参数，包括not in !=
     * 
     * @param value
     * @return 上午10:33:11 created by Darwin(Tianxin)
     */
    protected Object not(Object value) {
        return this.new NotParam(value);
    }

    /**
     * 反向参数
     * 
     * @author Darwin(Tianxin)
     */
    private final class NotParam {
        Object value;

        public NotParam(Object value) {
            this.value = value;
        }
    }

    /**
     * 构造加法参数
     * 
     * @param value
     * @return
     */
    protected Object incr(Number value) {
        return this.new IncrParam(value);
    }

    /**
     * 加法参数
     */
    private final class IncrParam {
        Number value;

        public IncrParam(Number value) {
            this.value = value;
        }
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

}
