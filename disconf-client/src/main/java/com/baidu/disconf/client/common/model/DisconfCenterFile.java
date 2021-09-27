package com.baidu.disconf.client.common.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.client.common.constants.SupportFileTypeEnum;
import com.baidu.disconf.client.config.DisClientConfig;
import com.baidu.disconf.client.support.utils.ClassUtils;
import com.baidu.disconf.core.common.utils.ClassLoaderUtil;
import com.baidu.disconf.core.common.utils.OsUtil;

/**
 * 配置文件表示
 *
 * @author liaoqiqi
 * @version 2014-5-20
 */
public class DisconfCenterFile extends DisconfCenterBaseModel {

    protected static final Logger LOGGER = LoggerFactory.getLogger(DisconfCenterFile.class);

    // -----key: 配置文件中的项名
    // -----value: 默认值
    private Map<String, FileItemValue> keyMaps = new HashMap<String, FileItemValue>();

    // 额外的配置数据，非注解式使用它来存储
    private Map<String, Object> additionalKeyMaps = new HashMap<String, Object>();

    // 是否是非注解注入方式
    private boolean isTaggedWithNonAnnotationFile = false;

    // 配置文件类
    private Class<?> cls;

    // 文件名
    private String fileName;

    // 配置文件 指定路径下
    private String targetDirPath;

    // 文件类型
    private SupportFileTypeEnum supportFileTypeEnum = SupportFileTypeEnum.ANY;

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

    public Map<String, Object> getAdditionalKeyMaps() {
        return additionalKeyMaps;
    }

    public void setAdditionalKeyMaps(Map<String, Object> additionalKeyMaps) {
        this.additionalKeyMaps = additionalKeyMaps;
    }

    public SupportFileTypeEnum getSupportFileTypeEnum() {
        return supportFileTypeEnum;
    }

    public void setSupportFileTypeEnum(SupportFileTypeEnum supportFileTypeEnum) {
        this.supportFileTypeEnum = supportFileTypeEnum;
    }

    public boolean isTaggedWithNonAnnotationFile() {
        return isTaggedWithNonAnnotationFile;
    }

    public void setIsTaggedWithNonAnnotationFile(boolean isTaggedWithNonAnnotationFile) {
        this.isTaggedWithNonAnnotationFile = isTaggedWithNonAnnotationFile;
    }

    public String getTargetDirPath() {
        return targetDirPath;
    }

    public void setTargetDirPath(String targetDirPath) {
        this.targetDirPath = targetDirPath;
    }

    @Override
    public String toString() {
        return "\n\tDisconfCenterFile [\n\tkeyMaps=" + keyMaps + "\n\tcls=" + cls + "\n\tfileName=" + fileName
                + "\n\ttargetDirPath=" + targetDirPath +
                super.toString() + "]";
    }

    @Override
    public String infoString() {
        return "\n\tDisconfCenterFile [\n\tkeyMaps=" + keyMaps + "\n" +
                "\tadditionalKeyMaps=" + additionalKeyMaps + "\n\tcls=" + cls + super.infoString() + "]";
    }

    /**
     * 获取可以表示的KeyMap对
     */
    public Map<String, Object> getKV() {

        // 非注解式的
        if (keyMaps.size() == 0) {
            return additionalKeyMaps;
        }

        //
        // 注解式的
        //
        Map<String, Object> map = new HashMap<String, Object>();
        for (String key : keyMaps.keySet()) {
            map.put(key, keyMaps.get(key).getValue());
        }

        return map;
    }

    /**
     * 配置文件的路径
     */
    public String getFilePath() {

        // 不放到classpath, 则文件路径根据 userDefineDownloadDir 来设置
        if (!DisClientConfig.getInstance().enableLocalDownloadDirInClassPath) {
            return OsUtil.pathJoin(DisClientConfig.getInstance().userDefineDownloadDir, fileName);
        }

        if (targetDirPath != null) {

            if (targetDirPath.startsWith("/")) {
                return OsUtil.pathJoin(targetDirPath, fileName);
            }

            return OsUtil.pathJoin(ClassLoaderUtil.getClassPath(), targetDirPath, fileName);
        }

        return OsUtil.pathJoin(ClassLoaderUtil.getClassPath(), fileName);
    }

    /**
     * 配置文件的路径
     */
    public String getFileDir() {

        // 获取相对于classpath的路径
        if (targetDirPath != null) {

            if (targetDirPath.startsWith("/")) {
                return OsUtil.pathJoin(targetDirPath);
            }

            return OsUtil.pathJoin(ClassLoaderUtil.getClassPath(), targetDirPath);
        }

        return ClassLoaderUtil.getClassPath();
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
        private Method setMethod;

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

        /**
         * 设置value, 优先使用 setter method, 然后其次是反射
         *
         * @param value
         */
        public Object setValue4StaticFileItem(Object value) throws Exception {

            try {
                if (setMethod != null) {
                    setMethod.invoke(null, value);
                } else {
                    field.set(null, value);
                }

            } catch (Exception e) {
                LOGGER.warn(e.toString());
            }

            return value;
        }

        public Object setValue4FileItem(Object object, Object value) throws Exception {

            try {
                if (setMethod != null) {
                    setMethod.invoke(object, value);
                } else {
                    field.set(object, value);
                }
            } catch (Exception e) {
                LOGGER.warn(e.toString());
            }

            return value;
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

        @Override
        public String toString() {
            return "FileItemValue{" +
                    "value=" + value +
                    ", field=" + field +
                    ", setMethod=" + setMethod +
                    '}';
        }

        public FileItemValue(Object value, Field field) {
            super();
            this.value = value;
            this.field = field;
        }

        public FileItemValue(Object value, Field field, Method setMethod) {
            super();
            this.value = value;
            this.field = field;
            this.setMethod = setMethod;
        }
    }
}
