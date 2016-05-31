package com.baidu.disconf.client.support.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.core.common.utils.ClassLoaderUtil;

/**
 * 配置导入工具
 *
 * @author liaoqiqi
 * @version 2014-6-6
 */
public final class ConfigLoaderUtils {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(ConfigLoaderUtils.class);

    private ConfigLoaderUtils() {

    }

    /**
     * @param propertyFilePath
     *
     * @return void
     *
     * @Description: 使用TOMCAT方式来导入
     * @author liaoqiqi
     * @date 2013-6-19
     */
    private static Properties loadWithTomcatMode(final String propertyFilePath)
            throws Exception {

        Properties props = new Properties();

        try {

            // 先用TOMCAT模式进行导入
            // http://blog.csdn.net/minfree/article/details/1800311
            // http://stackoverflow.com/questions/3263560/sysloader-getresource-problem-in-java
            URL url = ClassLoaderUtil.getLoader().getResource(propertyFilePath);
            URI uri = new URI(url.toString());
            props.load(new InputStreamReader(new FileInputStream(uri.getPath()), "utf-8"));

        } catch (Exception e) {

            // http://stackoverflow.com/questions/574809/load-a-resource-contained-in-a-jar
            props.load(new InputStreamReader(ClassLoaderUtil.getLoader().getResourceAsStream(propertyFilePath),
                    "utf-8"));
        }
        return props;
    }

    /**
     * @param propertyFilePath
     *
     * @return void
     *
     * @Description: 使用普通模式导入
     * @author liaoqiqi
     * @date 2013-6-19
     */
    private static Properties loadWithNormalMode(final String propertyFilePath)
            throws Exception {

        Properties props = new Properties();
        props.load(new InputStreamReader(new FileInputStream(propertyFilePath), "utf-8"));
        return props;
    }

    /**
     * @param propertyFilePath
     *
     * @return Properties
     *
     * @throws Exception
     * @Description: 配置文件载入器助手
     * @author liaoqiqi
     * @date 2013-6-19
     */
    public static Properties loadConfig(final String propertyFilePath)
            throws Exception {

        try {

            // 用TOMCAT模式 来载入试试
            return ConfigLoaderUtils.loadWithTomcatMode(propertyFilePath);

        } catch (Exception e1) {

            try {
                // 用普通模式进行载入
                return loadWithNormalMode(propertyFilePath);

            } catch (Exception e2) {

                throw new Exception("cannot load config file: "
                        + propertyFilePath);
            }
        }
    }

    /**
     * @param filePath
     *
     * @return InputStream
     *
     * @Description: 采用两种方式来载入文件
     * @author liaoqiqi
     * @date 2013-6-20
     */
    public static InputStream loadFile(String filePath) {

        InputStream in = null;

        try {

            // 先用TOMCAT模式进行导入
            in = ClassLoaderUtil.getLoader().getResourceAsStream(filePath);
            if (in == null) {

                // 使用普通模式导入
                try {

                    return new FileInputStream(filePath);

                } catch (FileNotFoundException e) {
                    return null;
                }
            } else {

                return in;
            }

        } finally {

            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    LOGGER.error("WHY HERE!", e);
                }
            }
        }
    }
}
