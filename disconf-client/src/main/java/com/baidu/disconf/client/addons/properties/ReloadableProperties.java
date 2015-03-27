package com.baidu.disconf.client.addons.properties;

import java.util.Properties;

/**
 * For Properties maps that notify about changes.
 * Would extend interface java.util.Properties if it were an interface.
 * Classes implementing this interface should consider extending {@link DelegatingProperties}.
 */
public interface ReloadableProperties {
    public Properties getProperties();

    void addReloadablePropertiesListener(ReloadablePropertiesListener l);

    boolean removeReloadablePropertiesListener(ReloadablePropertiesListener l);
}
