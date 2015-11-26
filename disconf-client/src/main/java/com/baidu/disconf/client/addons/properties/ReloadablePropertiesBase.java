package com.baidu.disconf.client.addons.properties;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Useful base class for implementing {@link ReloadableProperties}.
 */
public class ReloadablePropertiesBase extends DelegatingProperties implements ReloadableProperties {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ReloadablePropertiesBase.class);

    private List<IReloadablePropertiesListener> listeners = new ArrayList<IReloadablePropertiesListener>();
    private Properties internalProperties;

    public void setListeners(List listeners) {
        this.listeners = listeners;
    }

    protected Properties getDelegate() {
        synchronized(this) {
            return internalProperties;
        }
    }

    public Properties getProperties() {
        return getDelegate();
    }

    /**
     * 添加listener
     *
     * @param l
     */
    public void addReloadablePropertiesListener(IReloadablePropertiesListener l) {
        listeners.add(l);
    }

    /**
     * 删除listener
     *
     * @param l
     *
     * @return
     */
    public boolean removeReloadablePropertiesListener(IReloadablePropertiesListener l) {
        return listeners.remove(l);
    }

    /**
     * 通过listener去通知 reload
     *
     * @param oldProperties
     */
    protected void notifyPropertiesChanged(Properties oldProperties) {
        PropertiesReloadedEvent event = new PropertiesReloadedEvent(this, oldProperties);
        for (IReloadablePropertiesListener listener : listeners) {
            listener.propertiesReloaded(event);
        }
    }

    /**
     * set value 触发
     *
     * @param properties
     */
    protected void setProperties(Properties properties) {
        Properties oldProperties = internalProperties;
        synchronized(this) {
            internalProperties = properties;
        }
        notifyPropertiesChanged(oldProperties);
    }
}
