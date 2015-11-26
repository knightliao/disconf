package com.baidu.disconf.client.addons.properties;

import java.util.Properties;

/**
 * For Properties maps that notify about changes.
 * Would extend interface java.util.Properties if it were an interface.
 * Classes implementing this interface should consider extending {@link DelegatingProperties}.
 */
public interface ReloadableProperties {

    Properties getProperties();

    void addReloadablePropertiesListener(IReloadablePropertiesListener l);

    boolean removeReloadablePropertiesListener(IReloadablePropertiesListener l);
}
