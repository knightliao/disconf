package com.baidu.unbiz.common.genericdao.mapper;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.baidu.unbiz.common.genericdao.util.MapperUtils;
import com.github.knightliao.apollo.db.bo.BaseObject;

/**
 * GenericMapper the mapper
 */
public class GenericMapper<ENTITY extends BaseObject<K>, K extends Serializable> implements RowMapper<ENTITY> {

    public GenericMapper(ORMapping<ENTITY, K> mapping) {
        this.orMapping = mapping;
        this.isComplexKey = orMapping.isComplexKey();
        this.keyClass = orMapping.getKeyClass();
        this.entityClass = orMapping.getEntityClass();
    }

    private boolean isComplexKey = false;
    private Class<K> keyClass = null;
    private Class<ENTITY> entityClass = null;
    private ORMapping<ENTITY, K> orMapping = null;

    private List<Setter> setters = null;

    public ENTITY mapRow(ResultSet rs, int rowIndex) throws SQLException {

        if (setters == null) {
            ResultSetMetaData meta = rs.getMetaData();
            setters = getSetters(meta);
        }
        try {
            ENTITY entity = entityClass.newInstance();
            K key = isComplexKey ? keyClass.newInstance() : null;
            // FIXME
            if (key != null) {
                entity.setId(key);
            }

            for (Setter setter : setters) {
                String column = setter.columnName;
                Method setMethod = setter.method;
                Class<?> paramType = setter.setterParamType;

                if (setMethod.getName().equals("setId")) {
                    paramType = keyClass;
                }

                Object value = MapperUtils.getValue4Type(rs, column, paramType);
                // FIXME
                if (orMapping.isKeyColumn(column) && key != null) {
                    load2Entity(key, setMethod, value);
                } else {
                    load2Entity(entity, setMethod, value);
                }
            }
            return entity;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("error in loadEntity");
        }
    }

    /**
     * @param entity
     * @param setMethod
     * @param value
     *
     * @throws Exception 下午4:06:04 created by Darwin(Tianxin)
     */
    private void load2Entity(Object entity, Method setMethod, Object value) throws Exception {
        try {
            if (value != null) {
                setMethod.invoke(entity, value);
            }
        } catch (Exception e) {
            String sValue = value.toString();
            String sValueClass = value.getClass().toString();
            System.out.println("Error in " + setMethod.getName() + " invoke with param :" + sValue + " type is " +
                    sValueClass);
            throw e;
        }
    }

    /**
     * @param meta
     * @param genericMapper2
     *
     * @return 下午3:45:38 created by Darwin(Tianxin)
     *
     * @throws SQLException
     */
    private List<Setter> getSetters(ResultSetMetaData meta) throws SQLException {

        int columnCount = meta.getColumnCount();
        List<Setter> setters = new ArrayList<Setter>(columnCount);
        for (int i = 1; i <= columnCount; i++) {
            String column = meta.getColumnName(i);
            Method setMethod = orMapping.getSetter(column);

            if (setMethod != null) {
                Setter setter = new Setter(setMethod, column, setMethod.getParameterTypes()[0]);
                setters.add(setter);
            }
        }
        return setters;
    }

}

class Setter {
    Method method;
    String columnName;
    Class<?> setterParamType;

    public Setter(Method method, String columnName, Class<?> setterParamType) {
        this.method = method;
        this.columnName = columnName;
        this.setterParamType = setterParamType;
    }

}
