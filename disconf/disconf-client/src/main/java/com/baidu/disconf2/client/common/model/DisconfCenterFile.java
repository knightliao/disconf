package com.baidu.disconf2.client.common.model;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 配置文件表示
 * 
 * @author liaoqiqi
 * @version 2014-5-20
 */
public class DisconfCenterFile {

    // -----key: 配置文件中的项名
    // -----value: 默认值
    private Map<String, Object> keyMaps = new HashMap<String, Object>();

    // 配置文件类
    private Class<?> cls;

    // 域列表
    private Set<Field> fields = new HashSet<Field>();

    // 文件名
    private String fileName;

    // 远程配置服务的URL路径,不包含IP和PORT的
    private String remoteServerUrl;

    // 通用配置
    private DisConfCommonModel disConfCommonModel = new DisConfCommonModel();

    // 回调函数
    private DisconfCommonCallbackModel disconfCommonCallbackModel = new DisconfCommonCallbackModel();

    public Map<String, Object> getKeyMaps() {
        return keyMaps;
    }

    public void setKeyMaps(Map<String, Object> keyMaps) {
        this.keyMaps = keyMaps;
    }

    public Set<Field> getFields() {
        return fields;
    }

    public void setFields(Set<Field> fields) {
        this.fields = fields;
    }

    public DisConfCommonModel getDisConfCommonModel() {
        return disConfCommonModel;
    }

    public void setDisConfCommonModel(DisConfCommonModel disConfCommonModel) {
        this.disConfCommonModel = disConfCommonModel;
    }

    public Class<?> getCls() {
        return cls;
    }

    public void setCls(Class<?> cls) {
        this.cls = cls;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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
        return "DisconfCenterFile [keyMaps=" + keyMaps + ", cls=" + cls
                + ", fields=" + fields + ", fileName=" + fileName
                + ", remoteServerUrl=" + remoteServerUrl
                + ", disConfCommonModel=" + disConfCommonModel
                + ", disconfCommonCallbackModel=" + disconfCommonCallbackModel
                + "]";
    }

    public String getRemoteServerUrl() {
        return remoteServerUrl;
    }

    public void setRemoteServerUrl(String remoteServerUrl) {
        this.remoteServerUrl = remoteServerUrl;
    }

}
