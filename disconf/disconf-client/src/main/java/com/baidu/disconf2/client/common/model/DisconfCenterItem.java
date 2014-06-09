package com.baidu.disconf2.client.common.model;

import java.lang.reflect.Field;

/**
 * 配置项表示
 * 
 * @author liaoqiqi
 * @version 2014-5-20
 */
public class DisconfCenterItem {

    // 文件项的KEY
    private String key;
    private String value;

    // 域
    private Field field;

    // 通用配置
    private DisConfCommonModel disConfCommonModel;

    // 回调函数
    private DisconfCommonCallbackModel disconfCommonCallbackModel;

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public DisConfCommonModel getDisConfCommonModel() {
        return disConfCommonModel;
    }

    public void setDisConfCommonModel(DisConfCommonModel disConfCommonModel) {
        this.disConfCommonModel = disConfCommonModel;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public DisconfCommonCallbackModel getDisconfCommonCallbackModel() {
        return disconfCommonCallbackModel;
    }

    public void setDisconfCommonCallbackModel(
            DisconfCommonCallbackModel disconfCommonCallbackModel) {
        this.disconfCommonCallbackModel = disconfCommonCallbackModel;
    }

    @Override
    public String toString() {
        return "DisconfCenterItem [key=" + key + ", value=" + value
                + ", field=" + field + ", disConfCommonModel="
                + disConfCommonModel + ", disconfCommonCallbackModel="
                + disconfCommonCallbackModel + "]";
    }

}
