package com.baidu.disconf.client.addons.properties;

/**
 */
public interface ReconfigurationAware {
    public void beforeReconfiguration() throws Exception;

    ;

    public void afterReconfiguration() throws Exception;

    ;
}
