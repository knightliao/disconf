package com.baidu.disconf.client.support;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import com.baidu.disconf.client.config.inner.DisInnerConfigAnnotation;
import com.baidu.disconf.client.support.utils.ClassUtils;
import com.baidu.disconf.client.support.utils.ConfigLoaderUtils;

/**
 * 配置导入工具
 *
 * @author liaoqiqi
 * @version 2014-6-6
 */
public final class DisconfAutowareConfig {

    private DisconfAutowareConfig() {

    }

    protected static final Logger LOGGER = LoggerFactory.getLogger(DisconfAutowareConfig.class);

    /**
     * 先用TOMCAT模式进行导入配置文件，若找不到，则用项目目录模式进行导入
     */
    private static Properties getProperties(final String propertyFilePath) throws Exception {

        // 使用全路径的配置文件载入器
        return ConfigLoaderUtils.loadConfig(propertyFilePath);
    }

    /**
     * 使用 system env 进行数据导入, 能识别   DisInnerConfigAnnotation 的标识
     *
     * @Description: auto ware
     */
    public static void autowareConfigWithSystemEnv(final Object obj) throws Exception {

        try {

            Field[] fields = obj.getClass().getDeclaredFields();

            for (Field field : fields) {

                if (field.isAnnotationPresent(DisInnerConfigAnnotation.class)) {

                    if (Modifier.isStatic(field.getModifiers())) {
                        continue;
                    }

                    String name;
                    String value;

                    // disconf使用的配置

                    DisInnerConfigAnnotation config = field.getAnnotation(DisInnerConfigAnnotation.class);
                    name = config.name();

                    // 优先使用 系统参数或命令行导入
                    value = System.getProperty(name);
                    field.setAccessible(true);

                    if (null != value) {

                        try {

                            ClassUtils.setFieldValeByType(field, obj, value);

                        } catch (Exception e) {

                            LOGGER.error(String.format("invalid config: %s", name), e);
                        }

                    }
                }
            }
        } catch (Exception e) {

            throw new Exception("error while autowareConfigWithSystemEnv autowire config file", e);
        }
    }

    /**
     * 自动导入配置数据,能识别 DisconfFileItem 或 DisInnerConfigAnnotation 的标识
     *
     * @Description: auto ware
     */
    private static void autowareConfig(final Object obj, Properties prop) throws Exception {

        if (null == prop || obj == null) {
            throw new Exception("cannot autowareConfig null");
        }

        try {

            Field[] fields = obj.getClass().getDeclaredFields();

            for (Field field : fields) {

                if (field.isAnnotationPresent(DisconfFileItem.class)
                        || field.isAnnotationPresent(DisInnerConfigAnnotation.class)) {

                    if (Modifier.isStatic(field.getModifiers())) {
                        continue;
                    }

                    String name;
                    String value;

                    if (field.isAnnotationPresent(DisconfFileItem.class)) {

                        name = field.getName();
                        value = prop.getProperty(name, null);

                    } else {

                        // disconf使用的配置

                        DisInnerConfigAnnotation config = field.getAnnotation(DisInnerConfigAnnotation.class);
                        name = config.name();

                        String defaultValue = config.defaultValue();
                        value = prop.getProperty(name, defaultValue);

                        // using disconf as prefix to avoid env confusion
                        if (value.equals(defaultValue) && name != null) {
                            if (name.contains("disconf.")) {
                                String newName = name.substring(name.indexOf('.') + 1);
                                value = prop.getProperty(newName, defaultValue);
                            }
                        }
                    }

                    field.setAccessible(true);

                    if (null != value) {

                        try {

                            ClassUtils.setFieldValeByType(field, obj, value);

                        } catch (Exception e) {

                            LOGGER.error(String.format("invalid config: %s", name), e);
                        }
                    }
                }
            }
        } catch (Exception e) {

            throw new Exception("error while autowire config file", e);
        }
    }

    /**
     * 自动导入某个配置文件
     *
     * @throws Exception
     */
    public static void autowareConfig(final Object obj, final String propertyFilePath) throws Exception {

        // 读配置文件
        Properties prop = getProperties(propertyFilePath);
        if (null == prop || obj == null) {
            throw new Exception("cannot autowareConfig " + propertyFilePath);
        }

        autowareConfig(obj, prop);
    }

    /**
     * 自动导入Static配置数据,能识别 DisconfFileItem 或 DisconfFileItem 的标识
     *
     * @Description: auto ware
     */
    private static void autowareStaticConfig(Class<?> cls, Properties prop) throws Exception {

        if (null == prop) {
            throw new Exception("cannot autowareConfig null");
        }

        try {

            Field[] fields = cls.getDeclaredFields();

            for (Field field : fields) {

                if (field.isAnnotationPresent(DisconfFileItem.class)) {

                    if (!Modifier.isStatic(field.getModifiers())) {
                        continue;
                    }

                    field.setAccessible(true);

                    String name = field.getName();
                    Object value = prop.getProperty(name, null);
                    if (value != null) {
                        ClassUtils.setFieldValeByType(field, null, String.valueOf(value));
                    }
                }
            }
        } catch (Exception e) {

            throw new Exception("error while autowire config file", e);
        }
    }

    /**
     * 自动导入配置文件至 static变量
     *
     * @throws Exception
     */
    public static void autowareStaticConfig(Class<?> cls, final String propertyFilePath) throws Exception {

        // 读配置文件
        Properties prop = getProperties(propertyFilePath);
        if (null == prop) {
            throw new Exception("cannot autowareConfig " + propertyFilePath);
        }

        autowareStaticConfig(cls, prop);
    }

}
