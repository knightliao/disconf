package com.baidu.unbiz.common.genericdao.dao;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.CollectionUtils;

import com.baidu.ub.common.commons.ThreadContext;
import com.baidu.unbiz.common.genericdao.annotation.Sequence;
import com.baidu.unbiz.common.genericdao.bo.InsertOption;
import com.baidu.unbiz.common.genericdao.mapper.GenericMapper;
import com.baidu.unbiz.common.genericdao.mapper.ORMapping;
import com.baidu.unbiz.common.genericdao.mapper.QueryGenerator;
import com.baidu.unbiz.common.genericdao.operator.Match;
import com.baidu.unbiz.common.genericdao.operator.Modify;
import com.baidu.unbiz.common.genericdao.operator.Operators;
import com.baidu.unbiz.common.genericdao.operator.Order;
import com.baidu.unbiz.common.genericdao.operator.Query;
import com.baidu.unbiz.common.genericdao.sequence.SequenceGenerator;
import com.baidu.unbiz.common.genericdao.util.ClassUtils;
import com.github.knightliao.apollo.db.bo.BaseObject;

/**
 * 虚拟的父类
 */
public abstract class GenericDao<KEY extends Serializable, ENTITY extends BaseObject<KEY>> implements
        BaseDao<KEY, ENTITY> {

    private ORMapping<ENTITY, KEY> orMapping = null;
    private QueryGenerator<ENTITY, KEY> queryGenerator = null;

    private String sequenceName = null;

    // 以下两个需要用spring注入
    private SequenceGenerator sequenceGenerator;
    protected JdbcTemplate jdbcTemplate = null;

    @SuppressWarnings("unchecked")
    public GenericDao() {

        // 获取该类配置的实体类与主键类型
        Type[] types = ClassUtils.getGenericTypes(getClass());
        Class<ENTITY> entityClass = (Class<ENTITY>) types[1];
        Class<KEY> keyClass = (Class<KEY>) types[0];

        orMapping = new ORMapping<ENTITY, KEY>(entityClass, keyClass);
        queryGenerator = new QueryGenerator<ENTITY, KEY>(orMapping, this);

        analysisSequence(entityClass);
    }

    /**
     * 直接传入<code>keyClass和<code>entityClass</code>
     *
     * @param keyClass    主键类型
     * @param entityClass 实体类
     */
    public GenericDao(Class<KEY> keyClass, Class<ENTITY> entityClass) {

        orMapping = new ORMapping<ENTITY, KEY>(entityClass, keyClass);
        queryGenerator = new QueryGenerator<ENTITY, KEY>(orMapping, this);

        analysisSequence(entityClass);
    }

    /**
     * 解析映射的实体类，获取主键名、表名、分片数、sequence配置
     *
     * @param
     */
    protected void analysisSequence(Class<ENTITY> entityClass) {
        Sequence sequence = entityClass.getAnnotation(Sequence.class);
        if (sequence != null) {
            this.sequenceName = sequence.name();
            sequenceGenerator.initialSequence(sequenceName, sequence.size());
        }
    }

    /**
     * 通过getTableName获取表名
     *
     * @return the tableName
     */
    public String getTableName() {

        recordLog("----" + ThreadContext.getShardKey() + "\t" +
                getTableName(orMapping.getTable(), (Number) ThreadContext.getShardKey()));
        Number shardKey = ThreadContext.getShardKey();
        return getTableName(orMapping.getTable(), shardKey);
    }

    /**
     * 获取shardKey所在的表名
     *
     * @return
     */
    public String getTableName(String table, Number shardKey) {
        if (orMapping.getShardCount() <= 1 || shardKey == null || shardKey.longValue() <= 0) {
            return table;
        }

        return table + calShardNum(shardKey.longValue());
    }

    protected final int calShardNum(long shardKey) {
        return (int) (shardKey % orMapping.getShardCount());
    }

    public ENTITY get(KEY id) {
        if (id == null) {
            throw new RuntimeException("param is null!");
        }

        // 查询结果并装载对象
        List<KEY> keys = new ArrayList<KEY>(1);
        keys.add(id);
        List<ENTITY> entities = get(keys);
        if (CollectionUtils.isEmpty(entities)) {
            return null;
        }
        return entities.get(0);
    }

    public List<ENTITY> get(Collection<KEY> ids) {
        if (ids == null) {
            throw new RuntimeException("param is null!");
        }

        if (ids.isEmpty()) {
            return new ArrayList<ENTITY>();
        }

        if (orMapping.isComplexKey()) {
            return getByComplexKeys(ids);
        }
        return find(orMapping.getSKeyColumn(), ids);
    }

    /**
     * @param ids
     *
     * @return 下午9:03:31 created by Darwin(Tianxin)
     */
    private List<ENTITY> getByComplexKeys(Collection<KEY> ids) {
        List<ENTITY> entities = new ArrayList<ENTITY>(ids.size());
        for (KEY key : ids) {
            List<Match> matches = queryGenerator.getKeyMatches(key);
            entities.addAll(find(matches.toArray(new Match[matches.size()])));

        }
        return entities;
    }

    /**
     * 查询符合条件的结果计数
     *
     * @param column 条件字段
     * @param value  条件字段的值。支持集合对象，支持数组，会按照in 操作来组装条件
     *
     * @return
     */
    public int count(String column, Object value) {
        return count(Operators.match(column, value));
    }

    /**
     * 查询符合条件组合的记录数
     *
     * @param matches 查询参数,该参数为"字段名,字段值,字段名,字段值..."的排列
     *
     * @return
     */
    public int count(Match... matches) {
        return count(Arrays.asList(matches));
    }

    /**
     * 查询符合条件组合的记录数
     *
     * @param matches
     *
     * @return 2013-8-26 下午3:04:02 created by wangchongjie
     */
    public int count(List<Match> matches) {

        Query query = queryGenerator.getCountQuery(matches);
        // 执行操作
        String sql = query.getSql();
        return countBySQL(sql, query.getParams());
    }

    /**
     * 查询符合条件的结果计数
     *
     * @param sql
     * @param params
     *
     * @return
     */
    public int countBySQL(String sql, List<?> params) {
        Object[] args = null;
        if (params == null) {
            args = new Object[0];
        } else {
            args = params.toArray(new Object[params.size()]);
        }
        return jdbcTemplate.queryForObject(sql, Integer.class, args);
    }

    /**
     * 根据条件查询若干个字段，加载到ENTITY里
     *
     * @param matches
     * @param columns
     *
     * @return
     */
    protected List<ENTITY> findColumns(List<Match> matches, String[] columns) {

        Query query = queryGenerator.getMiniSelectQuery(Arrays.asList(columns), matches);
        return findBySQL(query.getSql(), query.getParams());
    }

    /**
     * 删除符合条件组合的对象列表
     * <p/>
     * 该参数为"字段名,字段值,字段名,字段值..."的排列
     *
     * @return
     */
    public int delete(Match... matches) {
        Query query = queryGenerator.getDeleteQuery(matches);

        // 执行操作
        return executeSQL(query.getSql(), query.getParams());
    }

    /**
     * 根据条件删除column=value
     *
     * @param column
     * @param value
     *
     * @return
     */
    public int delete(String column, Object value) {
        return delete(Operators.match(column, value));
    }

    public List<ENTITY> findAll() {
        return find();
    }

    /**
     * 获取查询结果
     *
     * @param column 条件字段
     * @param value  条件字段的值。支持集合对象，支持数组，会按照in 操作来组装条件
     *
     * @return
     */
    public List<ENTITY> find(String column, Object value) {
        return find(Operators.match(column, value));
    }

    /**
     * 查询符合条件组合的对象列表
     *
     * @param matches 查询参数,多个
     *
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
     *
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
     *
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
     *
     * @return
     */
    public List<ENTITY> find(List<Match> matches, List<Order> order, int offset, int limit) {
        // FIXME
        Query operate =
                queryGenerator.getSelectQuery(matches, (order == null) ? null : order.toArray(new Order[order.size()]));
        String sql = operate.getSql();
        List<Object> params = operate.getParams();

        if (offset != 0 || limit != 0) {
            sql = sql + " limit ?, ?";
            params.add(offset);
            params.add(limit);
        }

        // 执行操作
        return findBySQL(sql, operate.getParams());
    }

    /**
     * 根据查询条件获取结果集列表
     *
     * @param matches
     * @param order
     * @param curPage
     * @param pageSize
     *
     * @return
     */
    public List<ENTITY> page(List<Match> matches, List<Order> order, int curPage, int pageSize) {

        if (curPage < 0 || pageSize < 0) {
            return find(matches, order);
        }

        int offset = (curPage) * pageSize;
        int limit = pageSize;
        return find(matches, order, offset, limit);
    }

    /**
     * 查询符合条件组合的对象
     *
     * @param matches 查询参数,该参数为"字段名,字段值,字段名,字段值..."的排列
     *
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
     * @param matches 查询参数,该参数为"字段名,字段值,字段名,字段值..."的排列
     *
     * @return
     */
    public List<ENTITY> findIncludeColumns(Collection<String> includeColumns, Match... matches) {
        return findColumns(Arrays.asList(matches), includeColumns.toArray(new String[includeColumns.size()]));
    }

    /**
     * 查询符合条件组合的对象列表
     *
     * @param matches 查询参数,该参数为"字段名,字段值,字段名,字段值..."的排列
     *
     * @return
     */
    public List<ENTITY> findExcludeColumns(Collection<String> excludeColumns, Match... matches) {
        Set<String> columns = orMapping.getAllColumns();
        for (String column : excludeColumns) {
            columns.remove(column);
        }
        return findIncludeColumns(columns, matches);
    }

    /**
     * 根据查询条件获取结果集列表
     *
     * @param sql
     * @param params 无参数时可以为null
     *
     * @return
     */
    public List<ENTITY> findBySQL(String sql, List<?> params) {
        RowMapper<ENTITY> mapper = new GenericMapper<ENTITY, KEY>(orMapping);
        return findBySQL(sql, params, mapper);
    }

    /**
     * 按照SQL语句查询并装在记录为一个列表
     *
     * @param sql
     * @param params
     * @param mapper
     *
     * @return
     */
    public <T extends Object, O extends Object> List<T> findBySQL(String sql, List<O> params, RowMapper<T> mapper) {

        if (sql == null) {
            recordLog(" param sql is null!");
        }

        // 构造参数
        Object[] args = null;
        if (params == null) {
            args = new Object[0];
        } else {
            args = params.toArray(new Object[params.size()]);
        }

        // 查询
        if (params != null) {
            recordLog(sql + "\t" + params.toString());
        } else {
            recordLog(sql);
        }
        return (List<T>) jdbcTemplate.query(sql, args, mapper);
    }

    public <T> T findValue(String column, Class<T> tClass, Match... matches) {

        Query query = queryGenerator.getMiniSelectQuery(Arrays.asList(column), matches);
        List<T> ts = findOneColumn(query.getSql() + " limit 1", query.getParams(), 1, tClass);
        if (CollectionUtils.isEmpty(ts)) {
            return null;
        } else {
            return ts.get(0);
        }
    }

    public <T> List<T> findOneColumn(String column, Class<T> tClass, Match... matches) {
        Query query = queryGenerator.getMiniSelectQuery(Arrays.asList(column), matches);

        return findOneColumn(query.getSql(), query.getParams(), 1, tClass);
    }

    public <T> List<T> findDistinctColumn(String column, Class<T> tClass, Match... matches) {
        return findOneColumn("distinct " + column, tClass, matches);
    }

    /**
     * 查询某一列的值
     *
     * @param sql
     * @param params
     * @param mapperColumnIndex 要返回的哪一列的值，由1开始。
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public <N extends Object> List<N> findOneColumn(String sql, List<?> params, final int mapperColumnIndex) {

        if (sql == null) {
            recordLog(" param sql is null!");
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
     * @param mapperColumnIndex 要返回的那一列的值
     * @param resultClass       要装载成的类型
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public <N> List<N> findOneColumn(String sql, List<?> params, final int mapperColumnIndex,
                                     final Class<N> resultClass) {

        if (sql == null) {
            recordLog(" param sql is null!");
        }

        if (Number.class.isAssignableFrom(resultClass)) {
            if (resultClass.equals(Long.class)) {
                return findBySQL(sql, params, new RowMapper<N>() {
                    public N mapRow(ResultSet rs, int count) throws SQLException {
                        Number n = (Number) (rs.getObject(mapperColumnIndex));
                        if (n == null) {
                            return null;
                        }
                        return (N) Long.valueOf(n.longValue());
                    }
                });
            } else if (resultClass.equals(Integer.class)) {
                return findBySQL(sql, params, new RowMapper<N>() {
                    public N mapRow(ResultSet rs, int count) throws SQLException {
                        Number n = (Number) (rs.getObject(mapperColumnIndex));
                        if (n == null) {
                            return null;
                        }
                        return (N) Integer.valueOf(n.intValue());
                    }
                });
            } else if (resultClass.equals(Float.class)) {
                return findBySQL(sql, params, new RowMapper<N>() {
                    public N mapRow(ResultSet rs, int count) throws SQLException {
                        Number n = (Number) (rs.getObject(mapperColumnIndex));
                        if (n == null) {
                            return null;
                        }
                        return (N) Float.valueOf(n.floatValue());
                    }
                });
            } else if (resultClass.equals(Double.class)) {
                return findBySQL(sql, params, new RowMapper<N>() {
                    public N mapRow(ResultSet rs, int count) throws SQLException {
                        Number n = (Number) (rs.getObject(mapperColumnIndex));
                        if (n == null) {
                            return null;
                        }
                        return (N) Double.valueOf(n.doubleValue());
                    }
                });
            } else if (resultClass.equals(Short.class)) {
                return findBySQL(sql, params, new RowMapper<N>() {
                    public N mapRow(ResultSet rs, int count) throws SQLException {
                        Number n = (Number) (rs.getObject(mapperColumnIndex));
                        if (n == null) {
                            return null;
                        }
                        return (N) Short.valueOf(n.shortValue());
                    }
                });
            } else if (resultClass.equals(Byte.class)) {
                return findBySQL(sql, params, new RowMapper<N>() {
                    public N mapRow(ResultSet rs, int count) throws SQLException {
                        Number n = (Number) (rs.getObject(mapperColumnIndex));
                        if (n == null) {
                            return null;
                        }
                        return (N) Byte.valueOf(n.byteValue());
                    }
                });
            } else {
                return findBySQL(sql, params, new RowMapper<N>() {
                    public N mapRow(ResultSet rs, int count) throws SQLException {
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
            recordLog(" param entity is null!");
            return false;
        }

        Query query = queryGenerator.getUpdateQuery(entity);
        return executeSQL(query.getSql(), query.getParams()) == 1;
    }

    /**
     * 批量更新
     *
     * @param entities
     *
     * @return
     */
    public int update(List<ENTITY> entities) {

        if (entities == null || entities.size() == 0) {
            recordLog(" param entity is null!");
            return 0;
        }

        final List<Query> queries = queryGenerator.getUpdateQuery(entities);
        String sql = queries.get(0).getSql();
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            public void setValues(PreparedStatement stmt, int index) throws SQLException {
                int i = 1;
                List<Object> params = queries.get(index).getParams();
                for (Object o : params) {
                    stmt.setObject(i++, o);
                }
            }

            public int getBatchSize() {
                return queries.size();
            }
        });

        return entities.size();
    }

    public int update(Modify modify, Match... matches) {
        return update(Arrays.asList(modify), matches);
    }

    public int update(List<Modify> modifies, Match... matches) {

        Query query = queryGenerator.getUpdateQuery(modifies, matches);
        return executeSQL(query.getSql(), query.getParams());
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
            if (entity != null) {
                keys.add(entity.getId());
            }
        }

        return delete(keys);
    }

    public boolean delete(KEY id) {
        if (id == null) {
            recordLog(" param id is null!");
            return false;
        }
        List<KEY> keys = new ArrayList<KEY>();
        keys.add(id);
        return delete(keys) > 0;
    }

    public int delete(List<KEY> ids) {
        if (ids == null || ids.size() == 0) {
            recordLog(" param ids is empty!");
            return 0;
        } else if (orMapping.isComplexKey()) {
            return deleteByComplexIndex(ids);
        } else {
            return delete(orMapping.getSKeyColumn(), ids);
        }
    }

    /**
     * @param ids
     */
    private int deleteByComplexIndex(List<KEY> ids) {
        int count = 0;
        for (KEY key : ids) {
            List<Match> matches = queryGenerator.getKeyMatches(key);
            count += delete(matches.toArray(new Match[matches.size()]));
        }
        return count;
    }

    /**
     * 执行SQL语句
     *
     * @param sql
     * @param params
     *
     * @return
     */
    public <E extends Object> int executeSQL(String sql, List<E> params) {
        if (params == null || params.size() == 0) {
            return jdbcTemplate.update(sql);
        } else {
            return jdbcTemplate.update(sql, params.toArray(new Object[params.size()]));
        }
    }

    /**
     * 将数字集合append为(n1,n2,n3)这种格式的字符串
     *
     * @param ns
     *
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

    public static final String connectObjects(Object[] ns) {
        int size = 0;
        if (ns == null || (size = ns.length) == 0) {
            return null;
        }

        StringBuilder sb = new StringBuilder(size * 3);
        for (int i = 0; i < size; i++) {
            sb.append(",?");
        }

        sb.setCharAt(0, '(');
        sb.append(')');
        return sb.toString();
    }

    public ENTITY create(ENTITY entity) {
        return create(entity, null);
    }

    public ENTITY create(ENTITY entity, InsertOption option) {

        // 如果空值则返回
        if (entity == null) {
            recordLog(" param entity is null!");
            return null;
        }

        // 如果需要DAO层代理生成新ID
        if (sequenceName != null) {
            entity.setId(getNextId());
        }

        // 判断是否为自动设置的ID
        KEY key = entity.getId();
        boolean autoGeneratedKey = (Number.class.isAssignableFrom(orMapping.getKeyClass()) &&
                                            (key == null || (Long.parseLong(key.toString()) == 0)));

        // 获取insert语句
        @SuppressWarnings("unchecked")
        Query query = queryGenerator.getCreateQuery(autoGeneratedKey, false, option, entity);
        final String sql = query.getSql();
        final List<Object> params = query.getParams();

        // 如果非自动生成key则返回
        if (!autoGeneratedKey) {
            int rowCount = executeSQL(sql, params);
            return rowCount == 1 ? entity : null;

            // 如果自动生成key,生成KeyHolder
        } else {
            return createAndFetchKey(entity, sql, params);
        }
    }

    /**
     * @param entity
     * @param sql
     * @param params
     */
    @SuppressWarnings("unchecked")
    private ENTITY createAndFetchKey(ENTITY entity, final String sql, final List<Object> params) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        recordLog(sql);

        // 执行操作
        int rowCount = this.jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql);
                int index = 1;
                for (Object param : params) {
                    ps.setObject(index++, param);
                }
                return ps;
            }
        }, keyHolder);

        // 如果插入成功则获取keyHolder中的key
        if (rowCount != 0 && keyHolder.getKey() != null) {
            Class<KEY> keyClass = orMapping.getKeyClass();
            if (keyClass.equals(Integer.class)) {
                entity.setId((KEY) Integer.valueOf(keyHolder.getKey().intValue()));
            } else if (keyClass.equals(Long.class)) {
                entity.setId((KEY) Long.valueOf(keyHolder.getKey().longValue()));
            }
        }

        return rowCount == 1 ? entity : null;
    }

    // FIXME
    @SuppressWarnings("unchecked")
    private int createAndFetchUpdateRow(ENTITY entity, final String sql, final List<Object> params) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        recordLog(sql);

        // 执行操作
        int rowCount = this.jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql);
                int index = 1;
                for (Object param : params) {
                    ps.setObject(index++, param);
                }
                return ps;
            }
        }, keyHolder);

        // 如果插入成功则获取keyHolder中的key
        if (rowCount != 0) {
            Class<KEY> keyClass = orMapping.getKeyClass();
            if (keyClass.equals(Integer.class)) {
                entity.setId((KEY) Integer.valueOf(keyHolder.getKey().intValue()));
            } else if (keyClass.equals(Long.class)) {
                entity.setId((KEY) Long.valueOf(keyHolder.getKey().longValue()));
            }
        }

        return rowCount;
    }

    /**
     * @return 下午5:39:05 created by Darwin(Tianxin)
     */
    public abstract KEY getNextId();

    public int create(List<ENTITY> entities) {
        return create(entities, null);
    }

    public int[] insert(List<ENTITY> entities) {
        return insert(entities, null);
    }

    public int create(List<ENTITY> entities, InsertOption option) {

        if (entities == null || entities.size() == 0) {
            recordLog(" param entities is empty!");
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
        // boolean autoGenerateKey = key == null;

        // 获取insert语句
        Query query = queryGenerator.getCreateQuery(entities, key == null, false, option);

        // FIXME 先临时这样改下
        int size = query.getParams().size();
        int paramPerEntity = size / entities.size();
        int i = 0;
        for (ENTITY entity : entities) {
            createAndFetchKey(entity, query.getSql(), query.getParams().subList(i, i += paramPerEntity));
        }
        return entities.size();
        // if(!autoGenerateKey){
        // // 执行批量插入操作
        // executeSQL(query.getSql(), query.getParams());
        // return entities.size();
        // }else{
        // int size = query.getParams().size();
        // int paramPerEntity = size / entities.size();
        // int i = 0;
        // for(ENTITY entity : entities){
        // createAndFetchKey(entity, query.getSql(),
        // query.getParams().subList(i, i += paramPerEntity));
        // }
        // return entities.size();
        // }
    }

    public int[] insert(List<ENTITY> entities, InsertOption option) {
        if (entities == null || entities.size() == 0) {
            recordLog(" param entities is empty!");
            return null;
        }

        // 如果需要DAO层设置ID
        if (sequenceName != null) {
            for (ENTITY e : entities) {
                e.setId(getNextId());
            }
        }

        // 以第一个元素确定是否自动生成key
        KEY key = entities.get(0).getId();
        // boolean autoGenerateKey = key == null;

        // 获取insert语句
        Query query = queryGenerator.getCreateQuery(entities, key == null, false, option);

        // FIXME 先临时这样改下
        int size = query.getParams().size();
        int paramPerEntity = size / entities.size();
        int i = 0;
        int[] result = new int[entities.size()];
        for (int index = 0, length = entities.size(); index < length; index++) {
            int updated = createAndFetchUpdateRow(entities.get(index), query.getSql(),
                    query.getParams().subList(i, i += paramPerEntity));
            result[index] = updated;
        }

        return result;
    }

    public int createBatchQuick(List<ENTITY> entities) {

        if (entities == null || entities.size() == 0) {
            recordLog(" param entities is empty!");
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

        // 获取insert语句
        Query query = queryGenerator.getCreateQuery(entities, key == null, true, null);

        // 执行批量插入操作
        executeSQL(query.getSql(), query.getParams());
        return entities.size();
    }

    // 以下为spring注入的两个属性
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setSequenceGenerator(SequenceGenerator sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    /**
     * 记录日志的方法
     *
     * @param sLog
     */
    public abstract void recordLog(String sLog);
}
