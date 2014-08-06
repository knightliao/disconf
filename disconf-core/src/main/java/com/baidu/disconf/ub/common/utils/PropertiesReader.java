package com.baidu.disconf.ub.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.WeakHashMap;

/**
 * 读取properties文件的辅助类
 * 
 * @author piggie
 * @version 2.0
 */
public class PropertiesReader {

    private static Map<String, Properties> filePropMapping = new WeakHashMap<String, Properties>();

    /**
     * 取得指定properties文件的指定key的value
     * 
     * @param fileName
     * @param key
     * @return
     * @throws MissingResourceException
     */
    public static String getValue(String fileName, String key)
            throws MissingResourceException {
        final Properties properties = fillProperties(fileName);
        String value = properties.getProperty(key);
        return value.trim();
    }

    /**
     * 将文件中配置信息填充到properties对象中(用earth的ClassLoader)
     * 
     * @param fileName
     * @return Properties对象
     * @author liuzeyin
     */
    public static Properties fillProperties(String fileName) {
        return fillProperties(fileName, PropertiesReader.class.getClassLoader());
    }

    /**
     * 将文件中配置信息填充到properties对象中(用指定的ClassLoader)
     * 
     * @param fileName
     * @param cl
     * @return Properties对象
     * @author liuzeyin
     */
    public static Properties fillProperties(String fileName, ClassLoader cl) {

        if (!fileName.endsWith(".properties")) {
            fileName = fileName + ".properties";
        }

        Properties properties = new Properties();

        if (filePropMapping.containsKey(fileName)) {
            properties = filePropMapping.get(fileName);
        } else {
            InputStream is = cl.getResourceAsStream(fileName);
            try {
                properties.load(is);
                filePropMapping.put(fileName, properties);
            } catch (Exception e) {
                throw new RuntimeException("load properties file error "
                        + fileName, e);
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return properties;

    }
}
