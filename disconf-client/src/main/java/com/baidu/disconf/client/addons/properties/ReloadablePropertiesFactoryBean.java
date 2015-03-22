package com.baidu.disconf.client.addons.properties;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.baidu.disconf.client.DisconfMgr;

/**
 * A properties factory bean that creates a reconfigurable Properties object.
 * When the Properties' reloadConfiguration method is called, and the file has
 * changed, the properties are read again from the file.
 */
public class ReloadablePropertiesFactoryBean extends PropertiesFactoryBean implements DisposableBean,
                                                                                          ApplicationContextAware {

    private static ApplicationContext applicationContext;

    protected static final Logger log = LoggerFactory.getLogger(ReloadablePropertiesFactoryBean.class);

    private Resource[] locations;
    private long[] lastModified;
    private List<ReloadablePropertiesListener> preListeners;

    public void setLocation(final String fileNames) {
        List<String> list = new ArrayList<String>();
        list.add(fileNames);
        setLocations(list);
    }

    /**
     * @param fileNames
     */
    public void setLocations(List<String> fileNames) {

        List<Resource> resources = new ArrayList<Resource>();
        for (String filename : fileNames) {

            // trim
            filename = filename.trim();

            String realFileName = getFileName(filename);

            //
            // register to disconf
            //
            DisconfMgr.reloadableScan(realFileName);

            //
            // only properties will reload
            //
            String ext = FilenameUtils.getExtension(filename);
            if (ext.equals("properties")) {

                log.info("disconf reloadable file:" + filename + "\t" + realFileName);

                PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver =
                    new PathMatchingResourcePatternResolver();
                try {
                    Resource[] resourceList = pathMatchingResourcePatternResolver.getResources(filename);
                    for (Resource resource : resourceList) {
                        resources.add(resource);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        this.locations = resources.toArray(new Resource[resources.size()]);
        lastModified = new long[locations.length];
        super.setLocations(locations);
    }

    private String getFileName(String fileName) {

        if (fileName != null) {
            int index = fileName.indexOf(':');
            if (index < 0) {
                return fileName;
            } else {

                fileName = fileName.substring(index + 1);

                index = fileName.lastIndexOf('/');
                if (index < 0) {
                    return fileName;
                } else {
                    return fileName.substring(index + 1);
                }

            }
        }
        return null;
    }

    protected Resource[] getLocations() {
        return locations;
    }

    /**
     * @param listeners
     */
    public void setListeners(final List listeners) {
        // early type check, and avoid aliassing
        this.preListeners = new ArrayList<ReloadablePropertiesListener>();
        for (Object o : listeners) {
            preListeners.add((ReloadablePropertiesListener) o);
        }
    }

    private ReloadablePropertiesBase reloadableProperties;

    /**
     * @return
     *
     * @throws IOException
     */
    protected Object createInstance() throws IOException {
        // would like to uninherit from AbstractFactoryBean (but it's final!)
        if (!isSingleton()) {
            throw new RuntimeException("ReloadablePropertiesFactoryBean only works as singleton");
        }
        reloadableProperties = new ReloadablePropertiesImpl();
        if (preListeners != null) {
            reloadableProperties.setListeners(preListeners);
        }
        reload(true);

        // add for monitor
        ReloadConfigurationMonitor.addReconfigurableBean((ReconfigurableBean) reloadableProperties);

        return reloadableProperties;
    }

    public void destroy() throws Exception {
        reloadableProperties = null;
    }

    /**
     * @param forceReload
     *
     * @throws IOException
     */
    protected void reload(final boolean forceReload) throws IOException {
        boolean reload = forceReload;
        for (int i = 0; i < locations.length; i++) {
            Resource location = locations[i];
            File file;
            try {
                file = location.getFile();
            } catch (IOException e) {
                // not a file resource
                continue;
            }
            try {
                long l = file.lastModified();
                if (l > lastModified[i]) {
                    lastModified[i] = l;
                    reload = true;
                }
            } catch (Exception e) {
                // cannot access file. assume unchanged.
                if (log.isDebugEnabled()) {
                    log.debug("can't determine modification time of " + file + " for " + location, e);
                }
            }
        }
        if (reload) {
            doReload();
        }
    }

    /**
     * @throws IOException
     */
    private void doReload() throws IOException {
        reloadableProperties.setProperties(mergeProperties());
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     *
     */
    class ReloadablePropertiesImpl extends ReloadablePropertiesBase implements ReconfigurableBean {
        public void reloadConfiguration() throws Exception {
            ReloadablePropertiesFactoryBean.this.reload(false);
        }
    }

}
