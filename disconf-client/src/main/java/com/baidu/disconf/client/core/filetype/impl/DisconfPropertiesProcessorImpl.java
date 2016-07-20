package com.baidu.disconf.client.core.filetype.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.baidu.disconf.client.core.filetype.DisconfFileTypeProcessor;
import com.baidu.disconf.client.support.utils.ConfigLoaderUtils;

/**
 * Properties 处理器
 *
 * @author knightliao
 */
public class DisconfPropertiesProcessorImpl implements DisconfFileTypeProcessor {

    @Override
    public Map<String, Object> getKvMap(String fileName) throws Exception {

        Properties properties;

        // 读取配置
        properties = ConfigLoaderUtils.loadConfig(fileName);
        if (properties == null) {
            return null;
        }

        Map<String, Object> map = new HashMap<String, Object>();
        for (Object object : properties.keySet()) {

            String key = String.valueOf(object);
            Object value = properties.get(object);

            map.put(key, value);
        }

        return map;
    }
}
