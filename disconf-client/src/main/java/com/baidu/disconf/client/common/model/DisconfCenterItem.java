package com.baidu.disconf.client.common.model;

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
    private Object value;

    // Field
    private Field field;

    // 所在类实体
    private Object object;

    // 远程配置服务的URL路径,不包含IP和PORT的
    private String remoteServerUrl;

    // 通用配置
    private DisConfCommonModel disConfCommonModel = new DisConfCommonModel();

    // 回调函数
    private DisconfCommonCallbackModel disconfCommonCallbackModel = new DisconfCommonCallbackModel();

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

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public DisconfCommonCallbackModel getDisconfCommonCallbackModel() {
        return disconfCommonCallbackModel;
    }

    public void setDisconfCommonCallbackModel(
            DisconfCommonCallbackModel disconfCommonCallbackModel) {
        this.disconfCommonCallbackModel = disconfCommonCallbackModel;
    }

    public String getRemoteServerUrl() {
        return remoteServerUrl;
    }

    public void setRemoteServerUrl(String remoteServerUrl) {
        this.remoteServerUrl = remoteServerUrl;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "DisconfCenterItem [key=" + key + ", value=" + value
                + ", field=" + field + ", object=" + object
                + ", remoteServerUrl=" + remoteServerUrl
                + ", disConfCommonModel=" + disConfCommonModel
                + ", disconfCommonCallbackModel=" + disconfCommonCallbackModel
                + "]";
    }

}
