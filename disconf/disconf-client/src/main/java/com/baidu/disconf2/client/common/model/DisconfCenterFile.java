package com.baidu.disconf2.client.common.model;

import java.lang.reflect.Field;
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

    // 所在类实体
    private Object object;

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

    public void setKeyMaps(Map<String, FileItemValue> keyMaps) {
        this.keyMaps = keyMaps;
    }

    @Override
    public String toString() {
        return "DisconfCenterFile [keyMaps=" + keyMaps + ", cls=" + cls
                + ", object=" + object + ", fileName=" + fileName
                + ", remoteServerUrl=" + remoteServerUrl
                + ", disConfCommonModel=" + disConfCommonModel
                + ", disconfCommonCallbackModel=" + disconfCommonCallbackModel
                + "]";
    }

    /**
     * 
     * 获取可以表示的KeyMap对
     * 
     * @return
     */
    public Map<String, Object> getKV() {

        Map<String, Object> map = new HashMap<String, Object>();
        for (String key : keyMaps.keySet()) {
            map.put(key, keyMaps.get(key).getValue());
        }

        return map;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    /**
     * 配置文件Item项表示，包括了值，还有其类型
     * 
     * @author liaoqiqi
     * @version 2014-6-16
     */
    public static class FileItemValue {

        private Object value;
        private Field field;

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
            return "FileItemValue [value=" + value + ", field=" + field + "]";
        }

        public FileItemValue(Object value, Field field) {
            super();
            this.value = value;
            this.field = field;
        }

    }
}
