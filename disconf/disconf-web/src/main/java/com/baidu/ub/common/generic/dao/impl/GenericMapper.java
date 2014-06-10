/**
 * beidou-core-493#com.baidu.beidou.common.dao.impl.BeidouMapper.java
 * 下午5:16:56 created by Darwin(Tianxin)
 */
package com.baidu.ub.common.generic.dao.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.jdbc.core.RowMapper;

import com.baidu.ub.common.generic.annotation.Column;
import com.baidu.ub.common.generic.annotation.Table;
import com.baidu.ub.common.generic.bo.BaseObject;
import com.baidu.ub.common.utils.ClassUtils;

/**
 * 北斗的对象映射对象
 * 
 * @author Darwin(Tianxin)
 */
public class GenericMapper<ENTITY extends BaseObject<K>, K extends Serializable>
        implements RowMapper<ENTITY> {

    private final static Map<Class<?>, GenericMapper<?, ?>> map = new HashMap<Class<?>, GenericMapper<?, ?>>();

    private Set<String> cantBeModifiedColumns = new HashSet<String>();

    public Set<String> getCantBeModifiedColumns() {
        return cantBeModifiedColumns;
    }

    /**
     * 获取该类所对应的所有数据库列名
     * 
     * @return
     */
    public Set<String> getColumns() {
        Set<String> columnSet = new HashSet<String>();
        for (Entry<String, MethodPair> e : entries) {
            columnSet.add(e.getKey());
        }
        return columnSet;
    }

    /**
     * 获取该实体的字段名,除掉几个不需要的字段
     * 
     * @return
     */
    public String getColumnsExclude(String... excColumns) {
        Set<String> columns = getColumns();
        StringBuilder sb = new StringBuilder();
        for (String column : columns) {
            sb.append(column).append(',');
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * 该映射的目标class
     */
    protected Class<ENTITY> entityClass = null;

    /**
     * 装在对象时候要执行的setter方法的映射关系，key为column，value为setter方法
     */
    protected List<Map.Entry<String, MethodPair>> entries = null;

    /**
     * mapper的字段关系存储
     */
    protected Map<String, MethodPair> methodMap = null;

    private Class<K> keyClass = null;

    public GenericMapper(Class<ENTITY> entityClass, Class<K> keyClass) {
        this.entityClass = entityClass;
        this.keyClass = keyClass;
        generateMapper();
        map.put(entityClass, this);
    }

    /**
     * 生成映射方法
     * 
     * @param entity
     */
    protected void generateMapper() {

        // 获取该实体类的解析结果
        Set<Field> fields = ClassUtils.getAllFiled(entityClass);
        Set<Method> methods = ClassUtils.getAllMethod(entityClass);
        Map<String, Method> gsetterMap = ClassUtils.filter2Map(methods);

        Table table = entityClass.getAnnotation(Table.class);
        String keyColumn = table.keyColumn();

        methodMap = new HashMap<String, MethodPair>();

        // 循环处理所有字段，过滤出该类加载为对象时需要调用的setter方法map
        for (Field f : fields) {

            // 静态字段则自动pass
            if (Modifier.isStatic(f.getModifiers())) {
                continue;
            }

            // 不做关联加载的工作
            Class<?> fType = f.getType();
            if (fType.getName().startsWith("com.baidu.")
                    || Collection.class.isAssignableFrom(fType)
                    || fType.isArray()) {
                continue;
            }

            // 字段名字
            String name = f.getName().toLowerCase();

            // 其他字段获取field，getter，setter
            String setter = "set" + name;
            Method set = gsetterMap.get(setter);

            String getter = "get" + name;
            Method get = gsetterMap.get(getter);
            if (get == null) {
                get = gsetterMap.get("is" + name);
            }

            // 如果是ID属性，则直接使用table上映射的column
            if (name.equals("id")) {
                methodMap.put(keyColumn, new MethodPair(set, get));
                continue;
            }

            // 获取字段的注解，如果没有，则从getter或者setter上获取注解
            Column column = getColumnAnnotation(f, set, get);

            // 如果数据库映射字段不为空，则按照映射关系设置字段
            if (column == null) {
                methodMap.put(name, new MethodPair(set, get));
            } else if (column.value().equals("ignore")) {
            } else {
                methodMap.put(column.value(), new MethodPair(set, get));
                if (!column.maybeModified()) {
                    cantBeModifiedColumns.add(column.value());
                }
            }
        }
        entries = new ArrayList<Map.Entry<String, MethodPair>>(methodMap.size());
        entries.addAll(methodMap.entrySet());
    }

    /**
     * 获取某个字段的annotation，从继承链最下面获取
     * 
     * @param f
     * @param set
     * @param get
     * @return
     */
    private Column getColumnAnnotation(Field f, Method set, Method get) {

        // 三个地方都有可能出现column
        Column column = f.getAnnotation(Column.class);
        Column gColumn = get.getAnnotation(Column.class);
        Column sColumn = set.getAnnotation(Column.class);

        // 预先获取出get与set所在的类
        Class<?> sClass = set.getDeclaringClass();
        Class<?> gClass = get.getDeclaringClass();

        // 如果get上定义了annotation，且get定义的地方是子类
        if (gColumn != null && !gClass.isAssignableFrom(sClass)) {
            return gColumn;
        }

        // 如果是set上定义的annotation，且set方法不在父类中定义
        if (sColumn != null && !sClass.isAssignableFrom(gClass)) {
            return sColumn;
        }

        return column;
    }

    /**
     * <b>根据反射加载对象的方法。</b><br/>
     * <br/>
     * <b>工作步骤：</b><br/>
     * <br/>
     * <b>1，分析实体类，将解析的结果存入缓存。</b> <br/>
     * &nbsp;&nbsp;解析过程首先找到所有的字段，找到每个字段对应的getter与setter. <br/>
     * &nbsp;&nbsp;如果字段有annotation则从注释中获取column，否则从getter中取注释，依然不存在则从setter中获取注释
     * . <br/>
     * &nbsp;&nbsp;如果注释都不存在，则按照filed的名字作为数据库中的字段名字. <br/>
     * &nbsp;&nbsp;将数据库字段名，以及对应的setter方法放入缓存，一边装载对象时执行.<br/>
     * <br/>
     * <b>2，根据分析结果利用反射加载对象。</b><br/>
     * <br/>
     * <b>3，如果数据库中没有对应的字段，则不加载。</b>
     * 
     * @param rs
     *            ResultSet 查询结果集合
     * @param rowIndex
     * @return
     * @throws SQLException
     */
    public ENTITY mapRow(ResultSet rs, int rowIndex) throws SQLException {

        try {
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = rs.getMetaData().getColumnCount();
            ENTITY entity = entityClass.newInstance();
            for (int i = 1; i <= columnCount; i++) {
                String column = meta.getColumnName(i);
                MethodPair methods = methodMap.get(column);
                methods = (methods == null) ? methodMap.get((column = column
                        .toLowerCase())) : methods;
                if (methods == null) {
                    System.out.println("Error in " + column
                            + " , no setter found in class: "
                            + entityClass.getName());
                    continue;
                }

                Method setter = methods.setter;
                Class<?> paramClass = setter.getParameterTypes()[0];
                if (setter.getName().equals("setId")) {
                    paramClass = keyClass;
                }
                Object value = getValue4Type(rs, column, paramClass);

                try {
                    if (value != null) {
                        setter.invoke(entity, value);
                    }
                } catch (Exception e) {
                    System.out
                            .println("Error in " + setter.getName()
                                    + " invoke with param :" + value == null ? "null"
                                    : value.toString() + " type is " + value == null ? "null"
                                            : value.getClass().toString());
                    throw e;
                }
            }
            return entity;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("error in loadEntity");
        }
    }

    /**
     * 在rs中获取column字段的typeClass型的值
     * 
     * @param rs
     * @param column
     * @param paramClass
     * @return
     * @throws SQLException
     */
    protected Object getValue4Type(ResultSet rs, String column,
            Class<?> typeClass) throws SQLException {

        if (Collection.class.isAssignableFrom(typeClass)) {
            return null;
        }

        try {
            rs.findColumn(column);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (typeClass.equals(Integer.class) || typeClass.equals(Integer.TYPE)) {
            return rs.getInt(column);
        }

        if (typeClass.equals(Long.class) || typeClass.equals(Long.TYPE)) {
            return rs.getLong(column);
        }

        if (typeClass.equals(Boolean.class) || typeClass.equals(Boolean.TYPE)) {
            return rs.getBoolean(column);
        }

        if (typeClass.equals(Float.class) || typeClass.equals(Float.TYPE)) {
            return rs.getFloat(column);
        }

        if (typeClass.equals(Double.class) || typeClass.equals(Double.TYPE)) {
            return rs.getDouble(column);
        }

        if (typeClass.equals(Byte.class) || typeClass.equals(Byte.TYPE)) {
            return rs.getByte(column);
        }

        if (typeClass.equals(String.class)) {
            return rs.getString(column);
        }

        if (Date.class.isAssignableFrom(typeClass)) {
            return rs.getTimestamp(column);
        }

        if (java.sql.Date.class.isAssignableFrom(typeClass)) {
            return rs.getDate(column);
        }

        return rs.getObject(column);
    }

    public List<Map.Entry<String, MethodPair>> getEntries() {
        return entries;
    }
}

/**
 * 一个get与set方法的配对
 * 
 * @author Darwin(Tianxin)
 */
class MethodPair {

    public MethodPair(Method setterMethod, Method getterMethod) {
        this.getter = getterMethod;
        this.setter = setterMethod;
    }

    protected Method getter;

    protected Method setter;
}
