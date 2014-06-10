package com.baidu.disconf2.client.config.inner;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.utils.ConfigLoaderUtils;

/**
 * 配置导入工具
 * 
 * @author liaoqiqi
 * @version 2014-6-6
 */
public final class AutowareConfig {

    private AutowareConfig() {

    }

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(AutowareConfig.class);

    /**
     * 先用TOMCAT模式进行导入配置文件，若找不到，则用项目目录模式进行导入
     * 
     * @param filename
     * @return
     */
    public static Properties getProperties(final String propertyFilePath) {

        try {

            // 使用全路径的配置文件载入器
            return ConfigLoaderUtils.loadConfig(propertyFilePath);
        }

        catch (Exception e) {

            try {

                // 只用文件名 来载入试试
                String filename = FilenameUtils.getName(propertyFilePath);
                return ConfigLoaderUtils.loadConfig(filename);

            } catch (Exception e1) {

                LOGGER.error(String.format("read properties file %s error",
                        propertyFilePath), e1);
            }

        }
        return null;
    }

    /**
     * 
     * 自动导入配置数据
     * 
     * @Description: auto ware
     * 
     * @param
     * 
     * @param propertyFilePath
     */
    public static void autowareConfig(final Object obj, Properties prop)
            throws Exception {

        if (null == prop || obj == null) {
            throw new Exception("cannot autowareConfig");
        }

        //
        // autoware field
        //
        try {

            Field[] fields = obj.getClass().getDeclaredFields();

            for (Field field : fields) {

                if (!Modifier.isStatic(field.getModifiers())
                        && field.isAnnotationPresent(DisInnerConfigAnnotation.class)) {

                    field.setAccessible(true);
                    DisInnerConfigAnnotation config = field
                            .getAnnotation(DisInnerConfigAnnotation.class);
                    String name = config.name();
                    String defaultValue = config.defaultValue();
                    String value = prop.getProperty(name, defaultValue);

                    if (null == value) {

                        continue;

                    } else {

                        Class<?> type = field.getType();
                        String typeName = type.getName();

                        try {

                            if (typeName.equals("int")) {
                                if (value.equals("")) {
                                    value = "0";
                                }
                                field.set(obj, Integer.valueOf(value));
                            } else if (typeName.equals("long")) {
                                if (value.equals("")) {
                                    value = "0";
                                }
                                field.set(obj, Long.valueOf(value));
                            } else if (typeName.equals("boolean")) {
                                if (value.equals("")) {
                                    value = "false";
                                }
                                field.set(obj, Boolean.valueOf(value));
                            } else if (typeName.equals("double")) {
                                if (value.equals("")) {
                                    value = "0.0";
                                }
                                field.set(obj, Double.valueOf(value));
                            } else {
                                field.set(obj, value);
                            }

                        } catch (Exception e) {

                            LOGGER.error(String.format("invalid config: %s@%s",
                                    name), e);
                        }
                    }
                }
            }
        } catch (Exception e) {

            throw new Exception("error while reading config file", e);
        }
    }

    /**
     * 
     * @Description: auto ware
     * 
     * @param
     * 
     * @param propertyFilePath
     */
    public static void autowareConfig(final Object obj,
            final String propertyFilePath) throws Exception {

        // 读配置文件
        Properties prop = getProperties(propertyFilePath);
        if (null == prop || obj == null) {
            throw new Exception("cannot autowareConfig " + propertyFilePath);
        }

        autowareConfig(obj, prop);
    }
}
