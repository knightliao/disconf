package com.baidu.disconf.client.core.filetype.impl;

import com.baidu.disconf.client.core.filetype.DisconfFileTypeProcessor;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * yml 处理器
 *
 * @author jadedrip
 */
public class DisconfYmlProcessorImpl implements DisconfFileTypeProcessor {

    @Override
    public Map<String, Object> getKvMap(String fileName) throws Exception {
        try {
            File file = new File(fileName);
            FileSystemResource resource = new FileSystemResource(file);
            YamlPropertiesFactoryBean bean = new YamlPropertiesFactoryBean();
            bean.setResources(resource);
            bean.afterPropertiesSet();

            Properties properties = bean.getObject();
            Map<String, Object> map = new HashMap<String, Object>();
            for (Object object : properties.keySet()) {
                String key = String.valueOf(object);
                Object value = properties.get(object);
                map.put(key, value);
            }

            return map;
        } catch (Exception e) {
            return null;
        }
    }

}
