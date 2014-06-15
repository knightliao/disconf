package com.baidu.disconf2.client.common.model;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置文件表示
 * 
 * @author liaoqiqi
 * @version 2014-5-20
 */
public class DisconfCenterFile {

    // -----key: 配置文件中的项名
    // -----value: 默认值
    private Map<String, FileItemValue> keyMaps = new HashMap<String, FileItemValue>();

    // 配置文件类
    private Class<?> cls;

    // 文件名
    private String fileName;

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

    public String getRemoteServerUrl() {
        return remoteServerUrl;
    }

    public void setRemoteServerUrl(String remoteServerUrl) {
        this.remoteServerUrl = remoteServerUrl;
    }

    public Map<String, FileItemValue> getKeyMaps() {
        return keyMaps;
    }

    @Override
    public String toString() {
        return "DisconfCenterFile [keyMaps=" + keyMaps + ", cls=" + cls
                + ", fileName=" + fileName + ", remoteServerUrl="
                + remoteServerUrl + ", disConfCommonModel="
                + disConfCommonModel + ", disconfCommonCallbackModel="
                + disconfCommonCallbackModel + "]";
    }

    public void setKeyMaps(Map<String, FileItemValue> keyMaps) {
        this.keyMaps = keyMaps;
    }

    public static class FileItemValue {
        private Object value;
        private Type type;

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "FileItemValue [value=" + value + ", type=" + type + "]";
        }

        public FileItemValue(Object value, Type type) {
            super();
            this.value = value;
            this.type = type;
        }

    }
}
