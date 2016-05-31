package com.baidu.unbiz.common.genericdao.mapper;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.baidu.unbiz.common.genericdao.bo.InsertOption;
import com.baidu.unbiz.common.genericdao.dao.GenericDao;
import com.baidu.unbiz.common.genericdao.operator.Match;
import com.baidu.unbiz.common.genericdao.operator.Modify;
import com.baidu.unbiz.common.genericdao.operator.Order;
import com.baidu.unbiz.common.genericdao.operator.Query;
import com.baidu.unbiz.common.genericdao.param.BetweenParam;
import com.baidu.unbiz.common.genericdao.param.ExpressionParam;
import com.baidu.unbiz.common.genericdao.param.GreaterThanParam;
import com.baidu.unbiz.common.genericdao.param.GteParam;
import com.baidu.unbiz.common.genericdao.param.IncrParam;
import com.baidu.unbiz.common.genericdao.param.LessThanParam;
import com.baidu.unbiz.common.genericdao.param.LikeParam;
import com.baidu.unbiz.common.genericdao.param.LteParam;
import com.baidu.unbiz.common.genericdao.param.NotParam;
import com.github.knightliao.apollo.db.bo.BaseObject;

/**
 * @author Darwin(Tianxin)
 */
public class QueryGenerator<ENTITY extends BaseObject<KEY>, KEY extends Serializable> {

    /**
     * @param orMapping
     * @param genericDao
     */
    public QueryGenerator(ORMapping<ENTITY, KEY> orMapping, GenericDao<KEY, ENTITY> genericDao) {
        this.orMapping = orMapping;
        this.dao = genericDao;
    }

    private GenericDao<KEY, ENTITY> dao = null;
    private ORMapping<ENTITY, KEY> orMapping;

    public Query getSelectQuery(Collection<Match> matches) {
        // 此处修改以便灵活定义对象
        return getMiniSelectQuery(orMapping.getAllColumns(), matches);
    }

    public Query getSelectQuery(Collection<Match> matches, Order... orders) {

        Query query = getSelectQuery(matches);
        if (orders == null || orders.length == 0) {
            return query;
        }

        // 加入排序
        StringBuilder sb = new StringBuilder(query.getSql());

        sb.append(" order by ");
        for (Order entry : orders) {
            sb.append(entry.getColumn()).append(entry.isAsc() ? " asc" : " desc").append(',');
        }
        sb.deleteCharAt(sb.length() - 1);

        query.setSql(sb.toString());
        return query;
    }

    public Query getPageQuery(int curPage, int pageSize, Collection<Match> matches, Order... orders) {

        Query query = getSelectQuery(matches, orders);
        if (curPage < 0 || pageSize <= 0) {
            return query;
        }

        StringBuilder sb = new StringBuilder(query.getSql());
        List<Object> params = query.getParams();

        // 设置limit
        int offset = (curPage) * pageSize;
        int limit = pageSize;

        sb.append(" limit ?, ?");
        params.add(offset);
        params.add(limit);

        query.setSql(sb.toString());
        return query;
    }

    public Query getMiniSelectQuery(Collection<String> columns, Match... matches) {
        return getMiniSelectQuery(columns, Arrays.asList(matches));
    }

    /**
     * 構造一個查詢的SQL語句。當columns為null或size為0時，即為select * ...
     *
     * @param columns
     * @param matches
     *
     * @return 下午1:14:54
     */
    public Query getMiniSelectQuery(Collection<String> columns, Collection<Match> matches) {
        int matchSize = matches == null ? 0 : matches.size();

        int maxSqlLength = matches == null ? 50 : (matchSize + 3) * 10;
        StringBuilder sb = new StringBuilder(maxSqlLength);

        // SQL语句和参数列表的构造
        sb.append("select ");
        if (columns == null || columns.size() == 0) {
            sb.append("*,");
        } else {
            for (String column : columns) {
                sb.append(column).append(',');
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" from ").append(dao.getTableName());

        if (matchSize == 0) {
            return new Query(sb.toString(), new ArrayList<Object>(0));
        }

        // 如果有条件
        List<Object> params = new ArrayList<Object>(matchSize + 1);
        appendQuerytoSQL(matches, sb, params);
        return new Query(sb.toString(), params);
    }

    public Query getCountQuery(Match... matches) {
        return getCountQuery(Arrays.asList(matches));
    }

    public Query getCountQuery(List<Match> matches) {
        return getMiniSelectQuery(Arrays.asList("count(*)"), matches);
    }

    public Query getUpdateQuery(Modify modify, Match... matches) {
        return getUpdateQuery(Arrays.asList(modify), matches);
    }

    public Query getUpdateQuery(List<Modify> modifies, Match... matches) {
        return getUpdateQuery(modifies, Arrays.asList(matches));
    }

    private Query getUpdateQuery(List<Modify> modifies, List<Match> matches) {

        StringBuilder sb = new StringBuilder(100);
        List<Object> params = new ArrayList<Object>(modifies.size() + matches.size());

        sb.append("update ").append(dao.getTableName()).append(" set ");
        appendModifytoSQL(modifies, sb, params);

        sb.deleteCharAt(sb.length() - 1);
        appendQuerytoSQL(matches, sb, params);

        return new Query(sb.toString(), params);
    }

    /**
     * @param matches
     *
     * @return 下午3:15:10 created by Darwin(Tianxin)
     */
    public Query getDeleteQuery(Match... matches) {
        StringBuilder sb = new StringBuilder(100);
        List<Object> params = new ArrayList<Object>(matches.length);

        sb.append("delete from ").append(dao.getTableName());
        appendQuerytoSQL(Arrays.asList(matches), sb, params);

        return new Query(sb.toString(), params);
    }

    /**
     * @param modifies
     * @param sb
     * @param params
     */
    private void appendModifytoSQL(List<Modify> modifies, StringBuilder sb, List<Object> params) {
        for (Modify modify : modifies) {
            sb.append(modify.getColumn());

            Object value = modify.getValue();
            if (value instanceof IncrParam) {
                sb.append("=(").append(modify.getColumn()).append("+?),");
                params.add(((IncrParam) value).getValue());
            } else if (value instanceof ExpressionParam) {
                sb.append(',');
            } else {
                sb.append("=?,");
                params.add(value);
            }
        }
    }

    public Query getUpdateQuery(ENTITY entity) {
        List<ENTITY> entities = new ArrayList<ENTITY>(0);
        entities.add(entity);
        return getUpdateQuery(entities).get(0);
    }

    public List<Query> getUpdateQuery(List<ENTITY> entities) {
        List<Query> queries = new ArrayList<Query>(entities.size());
        Set<String> modifiableColumns = orMapping.getModifiableColumns();
        for (ENTITY entity : entities) {
            List<Match> matches = getKeyMatches(entity.getId());

            List<Modify> modifies = new ArrayList<Modify>(modifiableColumns.size());
            for (String column : modifiableColumns) {
                Object value = getColumnValue(column, entity);
                modifies.add(new Modify(column, value));
            }
            queries.add(getUpdateQuery(modifies, matches));
        }
        return queries;
    }

    /**
     * 構造一個create語句的query
     *
     * @param generateKey 由數據庫autoincrement生成key，還是應用端指定key
     * @param quick       時候用快速方式，快速方式無法將自動生成key填充到對象中。一般此參數為false，只有當批量創建，且由數據庫生成key時該處可選。
     * @param entities
     *
     * @return 下午1:26:37 created by Darwin(Tianxin)
     */
    public Query getCreateQuery(boolean generateKey, boolean quick, ENTITY... entities) {
        return getCreateQuery(Arrays.asList(entities), generateKey, quick, null);
    }

    // FIXME
    public Query getCreateQuery(boolean generateKey, boolean quick, InsertOption option, ENTITY... entities) {
        return getCreateQuery(Arrays.asList(entities), generateKey, quick, option);
    }

    public Query getCreateQuery(List<ENTITY> entities, boolean generateKey, boolean quick, InsertOption option) {

        Set<String> allColumns = orMapping.getAllColumns();
        Set<String> keyColumn = orMapping.getKeyColumn();
        StringBuilder sb = new StringBuilder(entities.size() * 32);
        List<Object> params = new ArrayList<Object>(allColumns.size() * entities.size());
        // bug fixed
        sb.append("insert " + ((option != null) ? option : "") + " into ").append(dao.getTableName()).append(" (");

        StringBuilder sbParams = new StringBuilder((allColumns.size() + 2) * 2);
        sbParams.append('(');

        // 如果自動生成key，並且不用快速方式
        for (String column : allColumns) {
            sb.append(column).append(',');
            sbParams.append("?,");
        }

        sb.deleteCharAt(sb.length() - 1).append(") values");
        sbParams.setCharAt(sbParams.length() - 1, ')');
        String sParams = sbParams.toString();

        if (quick && generateKey) {
            for (ENTITY entity : entities) {
                sb.append(sParams).append(',');
                for (String column : allColumns) {
                    if (keyColumn.contains(column)) {
                        params.add(getKeyColumnValue(column, entity.getId()));
                    } else {
                        params.add(getColumnValue(column, entity));
                    }
                }
            }
        } else {
            sb.append(sParams).append(',');
            for (ENTITY entity : entities) {
                for (String column : allColumns) {
                    if (keyColumn.contains(column)) {
                        params.add(getKeyColumnValue(column, entity.getId()));
                    } else {
                        params.add(getColumnValue(column, entity));
                    }
                }
            }
        }

        return new Query(sb.deleteCharAt(sb.length() - 1).toString(), params);
    }

    /**
     * @param column
     * @param entity
     *
     * @return 上午9:51:49
     */
    private Object getColumnValue(String column, ENTITY entity) {
        Method getter = orMapping.getGetter(column);
        try {
            return getter.invoke(entity);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private Object getKeyColumnValue(String column, KEY key) {
        if (!orMapping.isComplexKey()) {
            return key;
        }
        if (key == null) {
            return null;
        }
        Method getter = orMapping.getGetter(column);
        try {
            return getter.invoke(key);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * 获取主键匹配的条件
     *
     * @param id
     *
     * @return 上午9:45:33 created by Darwin(Tianxin)
     */
    public List<Match> getKeyMatches(KEY id) {
        Set<String> keyColumn = orMapping.getKeyColumn();
        List<Match> matches = new ArrayList<Match>(keyColumn.size());
        for (String column : keyColumn) {
            Object value = getKeyColumnValue(column, id);
            matches.add(new Match(column, value));
        }
        return matches;
    }

    /**
     * 将query变成一个查询条件的字符串，放到SQL中
     *
     * @param matches
     * @param sb
     * @param params
     */
    public void appendQuerytoSQL(Collection<Match> matches, StringBuilder sb, List<Object> params) {

        // 如果query为空，则直接返回
        if (matches == null || matches.size() == 0) {
            return;
        }

        sb.append(" where ");
        appendMatches(matches, sb, params);
    }

    public void appendMatches(Collection<Match> matches, StringBuilder sb, List<Object> params) {
        for (Match match : matches) {

            Object value = match.getValue();
            String column = match.getColumn();
            if (value == null) {
                sb.append(column).append(" is null");
            } else if (value instanceof LikeParam) {
                sb.append(column).append(" like ?");
                params.add('%' + ((LikeParam) value).getWord() + '%');
            } else if (value instanceof BetweenParam) {
                BetweenParam bp = (BetweenParam) value;
                Object start = bp.getStart();
                Object end = bp.getEnd();
                if (start == null && end == null) {
                    sb.append("1!=1");
                } else if (start == null) {
                    sb.append(column).append(" <= ?");
                    params.add(end);
                } else if (end == null) {
                    sb.append(column).append(" >= ?");
                    params.add(start);
                } else {
                    sb.append(column).append(" between ? and ?");
                    params.add(start);
                    params.add(end);
                }
            } else if (value instanceof GteParam) {
                sb.append(column).append(" >= ?");
                params.add(((GteParam) value).getValue());
            } else if (value instanceof GreaterThanParam) {
                sb.append(column).append(" > ?");
                params.add(((GreaterThanParam) value).getValue());
            } else if (value instanceof LteParam) {
                sb.append(column).append(" <= ?");
                params.add(((LteParam) value).getValue());
            } else if (value instanceof LessThanParam) {
                sb.append(column).append(" < ?");
                params.add(((LessThanParam) value).getValue());
            } else if (value instanceof ExpressionParam) {
                sb.append('(').append(column).append(')');
            } else {

                // 如果是取反的参数
                boolean reverse = value instanceof NotParam;
                if (reverse) {
                    value = ((NotParam) value).getValue();
                }

                if (value == null && reverse) {
                    sb.append(column).append(" is not null");
                } else if (value instanceof Collection<?>) {
                    Collection<?> coll = (Collection<?>) value;
                    if (coll.size() == 0) {
                        sb.append(reverse ? "1=1" : "1!=1");
                    } else if (coll.size() == 1) {
                        sb.append(column).append(reverse ? "!=?" : "=?");
                        params.add(coll.iterator().next());
                    } else {
                        sb.append(column).append(reverse ? " not in " : " in");
                        sb.append(connectObjects(coll));
                        params.addAll(coll);
                    }
                } else if (value.getClass().isArray()) {
                    Object[] array = (Object[]) value;
                    if (array.length == 0) {
                        sb.append(reverse ? "1=1" : "1!=1");
                    } else if (array.length == 1) {
                        sb.append(column).append(reverse ? "!=?" : "=?");
                        params.add(array[0]);
                    } else {
                        sb.append(column).append(reverse ? " not in " : " in");
                        sb.append(connectObjects(Arrays.asList(array)));
                        for (Object o : array) {
                            params.add(o);
                        }

                    }
                } else {
                    sb.append(column).append(reverse ? "!=?" : "=?");
                    params.add(value);
                }
            }
            sb.append(" and ");
        }

        sb.delete(sb.length() - 5, sb.length());
    }

    /**
     * 将数字集合append为( 'n1','n2','n3')这种格式的字符串
     *
     * @param ns
     *
     * @return
     */
    public static final String connectObjects(Collection<?> ns) {

        if (ns == null || ns.size() == 0) {
            return "()";
        }

        StringBuilder sb = new StringBuilder(ns.size() * 8);
        int size = ns.size();
        for (int i = 0; i < size; i++) {
            sb.append(",?");
        }

        sb.setCharAt(0, '(');
        sb.append(')');
        return sb.toString();
    }

    public static void main(String[] args) {
        int i = 0;
        System.out.println(i += 1);
    }
}
