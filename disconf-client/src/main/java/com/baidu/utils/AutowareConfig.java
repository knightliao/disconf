package com.baidu.utils;

import java.util.Properties;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

}
