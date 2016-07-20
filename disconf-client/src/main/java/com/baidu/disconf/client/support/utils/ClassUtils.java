package com.baidu.disconf.client.support.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * 类工具
 *
 * @author liaoqiqi
 * @version 2014-6-11
 */
public class ClassUtils {

    /**
     * 由Get Method名称获取Field名
     *
     * @return
     */
    public static String getFieldNameByGetMethodName(String methodName) {

        int prefixIndex = 0;

        // 必须以get或is开始的
        if (methodName.startsWith("get")) {
            prefixIndex = 3;

        } else if (methodName.startsWith("is")) {

            prefixIndex = 2;

        } else {

            return null;
        }

        String fieldName = methodName.substring(prefixIndex);
        if (fieldName.length() >= 1) {
            String firstCharStr = String.valueOf(fieldName.charAt(0))
                    .toLowerCase();
            if (fieldName.length() > 1) {
                fieldName = firstCharStr + fieldName.substring(1);
            } else {
                fieldName = firstCharStr.toLowerCase();
            }
        }

        return fieldName;
    }

    /**
     * 根据Field类型设置值
     *
     * @param field
     */
    public static void setFieldValeByType(Field field, Object obj, String value)
            throws Exception {

        Class<?> type = field.getType();

        String typeName = type.getName();

        if (typeName.equals("int")) {

            if (value.equals("")) {
                value = "0";
            }
            field.set(obj, Integer.valueOf(value));

        } else if (typeName.equals("long")) {

            if (value.equals("")) {
                value = "0";
            }
            field.set(obj, Long.valueOf(value));

        } else if (typeName.equals("boolean")) {

            if (value.equals("")) {
                value = "false";
            }
            field.set(obj, Boolean.valueOf(value));

        } else if (typeName.equals("double")) {

            if (value.equals("")) {
                value = "0.0";
            }
            field.set(obj, Double.valueOf(value));

        } else {

            field.set(obj, value);
        }
    }

    /**
     * 根据Field类型返回值
     *
     * @param type
     * @param value
     *
     * @return
     *
     * @throws Exception
     */
    public static Object getValeByType(Class<?> type, Object value)
            throws Exception {

        // 预处理
        if (!(value instanceof String)) {
            value = "";
        }

        // trim
        String dataValue = (String) value;
        dataValue = dataValue.trim();

        // process
        String typeName = type.getName();
        typeName = typeName.toLowerCase();

        if (typeName.equals("int") || typeName.equals("java.lang.integer")) {

            if (value.equals("")) {
                value = "0";
            }

            return Integer.valueOf(dataValue);

        } else if (typeName.equals("long") || typeName.equals("java.lang.long")) {

            if (value.equals("")) {
                value = "0";
            }

            return Long.valueOf(dataValue);

        } else if (typeName.equals("boolean")
                || typeName.equals("java.lang.boolean")) {

            if (value.equals("")) {
                value = "false";
            }

            return Boolean.valueOf(dataValue);

        } else if (typeName.equals("double")
                || typeName.equals("java.lang.double")) {

            if (value.equals("")) {
                value = "0.0";
            }

            return Double.valueOf(dataValue);

        } else {

            return value;
        }
    }

    /**
     * 获取一个类的所有方法
     *
     * @param entityClass
     *
     * @return
     */
    public static Set<Method> getAllMethod(Class<?> entityClass) {

        // 获取本类的所有的方法
        Set<Method> ms = new HashSet<Method>();
        for (Method m : entityClass.getMethods()) {
            ms.add(m);
        }
        for (Method m : entityClass.getDeclaredMethods()) {
            ms.add(m);
        }

        // 递归获取父类的所有方法
        Class<?> superClass = entityClass.getSuperclass();
        if (!superClass.equals(Object.class)) {
            Set<Method> superFields = getAllMethod(superClass);
            ms.addAll(superFields);
        }

        return ms;
    }

}
