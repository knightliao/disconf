package com.baidu.disconf.client.addons.properties;

import java.util.Properties;

public class PropertiesReloadedEvent {

    final ReloadableProperties target;
    final Properties oldProperties;

    public PropertiesReloadedEvent(ReloadableProperties target, Properties oldProperties) {
        this.target = target;
        this.oldProperties = oldProperties;
    }

    public ReloadableProperties getTarget() {
        return target;
    }

    public Properties getOldProperties() {
        return oldProperties;
    }
}
