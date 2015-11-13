package com.baidu.disconf.client.addons.properties;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Useful base class for implementing {@link ReloadableProperties}.
 */
public class ReloadablePropertiesBase extends DelegatingProperties implements ReloadableProperties {
    private List<ReloadablePropertiesListener> listeners = new ArrayList<ReloadablePropertiesListener>();
    private Properties internalProperties;

    public void setListeners(List listeners) {
        this.listeners = listeners;
    }

    protected Properties getDelegate()  {
        synchronized(this) {
            return internalProperties;
        }
    }

    public Properties getProperties() {
        return getDelegate();
    }

    public void addReloadablePropertiesListener(ReloadablePropertiesListener l) {
        listeners.add(l);
    }

    public boolean removeReloadablePropertiesListener(ReloadablePropertiesListener l) {
        return listeners.remove(l);
    }

    protected void notifyPropertiesChanged(Properties oldProperties) {
        PropertiesReloadedEvent event = new PropertiesReloadedEvent(this, oldProperties);
        for (ReloadablePropertiesListener listener : listeners) {
            listener.propertiesReloaded(event);
        }
    }

    protected void setProperties(Properties properties) {
        Properties oldProperties = internalProperties;
        synchronized(this) {
            internalProperties = properties;
        }
        notifyPropertiesChanged(oldProperties);
    }
}
