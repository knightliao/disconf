package com.baidu.disconf.client.common.model;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置文件表示
 * 
 * @author liaoqiqi
 * @version 2014-5-20
 */
public class DisconfCenterFile extends DisconfCenterBaseModel {

    // -----key: 配置文件中的项名
    // -----value: 默认值
    private Map<String, FileItemValue> keyMaps = new HashMap<String, FileItemValue>();

    // 配置文件类
    private Class<?> cls;

    // 文件名
    private String fileName;

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

    public Map<String, FileItemValue> getKeyMaps() {
        return keyMaps;
    }

    public void setKeyMaps(Map<String, FileItemValue> keyMaps) {
        this.keyMaps = keyMaps;
    }

    @Override
    public String toString() {
        return "\n\tDisconfCenterFile [\n\tkeyMaps=" + keyMaps + "\n\tcls="
                + cls + "\n\tfileName=" + fileName + super.toString() + "]";
    }

    @Override
    public String infoString() {
        return "\n\tDisconfCenterFile [\n\tkeyMaps=" + keyMaps + "\n\tcls="
                + cls + super.infoString() + "]";
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
