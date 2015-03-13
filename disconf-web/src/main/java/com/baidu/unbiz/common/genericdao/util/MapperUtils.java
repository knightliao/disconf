/**
 * unbiz-genericdao#com.baidu.unbiz.common.genericdao.util.MapperUtils.java
 * 上午11:09:16 created by Darwin(Tianxin)
 */
package com.baidu.unbiz.common.genericdao.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;

/**
 * @author Darwin(Tianxin)
 */
public class MapperUtils {

    /**
     * 在rs中获取column字段的typeClass型的值
     *
     * @param rs
     * @param column
     * @param paramClass
     *
     * @return
     *
     * @throws SQLException
     */
    public static Object getValue4Type(ResultSet rs, String column, Class<?> typeClass) throws SQLException {

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

}
