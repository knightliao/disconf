package com.baidu.disconf.client.common.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.baidu.disconf.client.support.utils.ClassUtils;

/**
 * 配置项表示
 *
 * @author liaoqiqi
 * @version 2014-5-20
 */
public class DisconfCenterItem extends DisconfCenterBaseModel {

    // 文件项的KEY
    private String key;
    private Object value;

    // Field
    private Field field;
    private Method setMethod;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setField(Field field) {
        this.field = field;
    }

    /**
     * 是否是静态域
     *
     * @return
     */
    public boolean isStatic() {
        return Modifier.isStatic(field.getModifiers());
    }

    public Class<?> getDeclareClass() {
        return field.getDeclaringClass();
    }

    public void setSetMethod(Method setMethod) {
        this.setMethod = setMethod;
    }

    /**
     * 返回值
     *
     * @param fieldValue
     *
     * @return
     *
     * @throws Exception
     */
    public Object getFieldValueByType(Object fieldValue) throws Exception {
        return ClassUtils.getValeByType(field.getType(), fieldValue);
    }

    public Object getFieldDefaultValue(Object object) throws Exception {
        return field.get(object);
    }

    /**
     * 设置value, 优先使用 setter method, 然后其次是反射
     *
     * @param value
     */
    public Object setValue4StaticFileItem(Object value) throws Exception {

        if (setMethod != null) {
            setMethod.invoke(null, value);
        } else {
            field.set(null, value);
        }

        return value;
    }

    public Object setValue4FileItem(Object object, Object value) throws Exception {

        if (setMethod != null) {
            setMethod.invoke(object, value);
        } else {
            field.set(object, value);
        }

        return value;
    }

    @Override
    public String toString() {
        return "\n\tDisconfCenterItem [\n\tkey=" + key + "\n\tvalue=" + value + "\n\tfield=" + field +
                super.toString() + "]";
    }

    @Override
    public String infoString() {
        return "\n\tDisconfCenterItem [\n\tvalue=" + value + "\n\tfield=" + field + super.infoString() + "]";
    }

}
