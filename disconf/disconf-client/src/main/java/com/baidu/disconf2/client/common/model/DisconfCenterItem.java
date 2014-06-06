package com.baidu.disconf2.client.common.model;

import java.lang.reflect.Field;

import com.baidu.disconf2.client.common.constants.DisConfigTypeEnum;

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

    // 配置占用类型
    private DisConfigTypeEnum disConfigTypeEnum;

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

    public DisConfigTypeEnum getDisConfigTypeEnum() {
        return disConfigTypeEnum;
    }

    public void setDisConfigTypeEnum(DisConfigTypeEnum disConfigTypeEnum) {
        this.disConfigTypeEnum = disConfigTypeEnum;
    }

    public DisconfCommonCallbackModel getDisconfCommonCallbackModel() {
        return disconfCommonCallbackModel;
    }

    public void setDisconfCommonCallbackModel(
            DisconfCommonCallbackModel disconfCommonCallbackModel) {
        this.disconfCommonCallbackModel = disconfCommonCallbackModel;
    }

}
