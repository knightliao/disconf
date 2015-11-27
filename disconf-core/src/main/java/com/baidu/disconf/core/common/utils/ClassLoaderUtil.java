package com.baidu.disconf.core.common.utils;

import java.net.URLDecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ClassLoaderUtil
 */
public final class ClassLoaderUtil {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(ClassLoaderUtil.class);

    // class path
    private static String classPath = "";

    // loader
    private static ClassLoader loader = Thread.currentThread().getContextClassLoader();

    private ClassLoaderUtil() {

    }

    //
    // get class path
    //
    static {

        if (loader == null) {
            LOGGER.info("using system class loader!");
            loader = ClassLoader.getSystemClassLoader();
        }

        try {

            java.net.URL url = loader.getResource("");
            // get class path
            classPath = url.getPath();
            classPath = URLDecoder.decode(classPath, "utf-8");

            // 如果是jar包内的，则返回当前路径
            if (classPath.contains(".jar!")) {
                LOGGER.warn("using config file inline jar!");
                classPath = System.getProperty("user.dir");
            }

        } catch (Exception e) {
            LOGGER.warn("cannot get classpath using getResource(), now using user.dir");
            classPath = System.getProperty("user.dir");
        }

        LOGGER.info("classpath: {}", classPath);
    }

    public static String getClassPath() {
        return classPath;
    }

    public static ClassLoader getLoader() {
        return loader;
    }
}
