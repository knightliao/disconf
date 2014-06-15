package com.baidu.disconf2.client.common.model;

import java.lang.reflect.Type;

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

    // KEY类型
    private Type keyType;

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

    public Type getKeyType() {
        return keyType;
    }

    public void setKeyType(Type keyType) {
        this.keyType = keyType;
    }

    @Override
    public String toString() {
        return "DisconfCenterItem [key=" + key + ", value=" + value
                + ", keyType=" + keyType + ", remoteServerUrl="
                + remoteServerUrl + ", disConfCommonModel="
                + disConfCommonModel + ", disconfCommonCallbackModel="
                + disconfCommonCallbackModel + "]";
    }

}
