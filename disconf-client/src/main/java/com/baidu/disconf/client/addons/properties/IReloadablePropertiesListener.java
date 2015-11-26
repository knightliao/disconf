package com.baidu.disconf.client.addons.properties;

/**
 * property reload listener
 */
public interface IReloadablePropertiesListener {

    void propertiesReloaded(PropertiesReloadedEvent event);
}
