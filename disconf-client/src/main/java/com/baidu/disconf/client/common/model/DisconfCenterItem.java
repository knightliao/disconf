package com.baidu.disconf.client.common.model;

import java.lang.reflect.Field;

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

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return "\n\tDisconfCenterItem [\n\tkey=" + key + "\n\tvalue=" + value
                + "\n\tfield=" + field +  super.toString() + "]";
    }

}
