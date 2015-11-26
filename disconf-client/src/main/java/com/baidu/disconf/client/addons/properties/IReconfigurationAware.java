package com.baidu.disconf.client.addons.properties;

/**
 * 配置更新时 的拦截
 */
public interface IReconfigurationAware {

    void beforeReconfiguration() throws Exception;

    void afterReconfiguration() throws Exception;
}
