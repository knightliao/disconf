package com.baidu.disconf.ub.common.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 配置导入工具
 * 
 * @author liaoqiqi
 * @version 2014-6-6
 */
public class ConfigLoaderUtils {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(ConfigLoaderUtils.class);

    // loader
    private static ClassLoader loader = ConfigLoaderUtils.class
            .getClassLoader();

    public static String CLASS_PATH = "";

    //
    // get class path
    //
    static {

        if (loader == null) {
            LOGGER.info("using system class loader!");
            loader = ClassLoader.getSystemClassLoader();
        }
        java.net.URL url = loader.getResource("");

        try {
            // get class path
            CLASS_PATH = url.getPath();
            CLASS_PATH = URLDecoder.decode(CLASS_PATH, "utf-8");
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }
    }

    /**
     * 
     * @Description: 使用TOMCAT方式来导入
     * 
     * @param propertyFilePath
     * @return void
     * @author liaoqiqi
     * @date 2013-6-19
     */
    private static Properties loadWithTomcatMode(final String propertyFilePath)
            throws Exception {

        Properties props = new Properties();

        // 先用TOMCAT模式进行导入
        // http://blog.csdn.net/minfree/article/details/1800311
        // http://stackoverflow.com/questions/3263560/sysloader-getresource-problem-in-java
        URL url = loader.getResource(propertyFilePath);
        URI uri = new URI(url.toString());
        props.load(new FileInputStream(uri.getPath()));
        return props;
    }

    /**
     * 
     * @Description: 使用普通模式导入
     * 
     * @param propertyFilePath
     * @return void
     * @author liaoqiqi
     * @date 2013-6-19
     */
    private static Properties loadWithNormalMode(final String propertyFilePath)
            throws Exception {

        Properties props = new Properties();
        props.load(new FileInputStream(propertyFilePath));
        return props;
    }

    /**
     * 
     * @Description: 配置文件载入器助手
     * 
     * @param propertyFilePath
     * @return
     * @throws Exception
     * @return Properties
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
     * 
     * @Description: 采用两种方式来载入文件
     * 
     * @param file
     * @return
     * @return InputStream
     * @author liaoqiqi
     * @date 2013-6-20
     */
    public static InputStream loadFile(String filePath) {

        InputStream in = null;

        try {

            // 先用TOMCAT模式进行导入
            in = loader.getResourceAsStream(filePath);
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
