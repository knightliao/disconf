package com.baidu.disconf.client.store.processor.model;

import java.util.Properties;

/**
 * 配置的值，配置文件是properties, 配置项是string<br/>
 * 这个类是为了做兼容
 * 
 * @author liaoqiqi
 * @version 2014-8-4
 */
public class DisconfValue {

    // 配置项使用
    private String value;

    // 配置文件使用
    private Properties properties;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "DisconfValue [value=" + value + ", properties=" + properties
                + "]";
    }

    public DisconfValue(String value, Properties properties) {
        super();
        this.value = value;
        this.properties = properties;
    }

}
