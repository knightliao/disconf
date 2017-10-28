package com.baidu.disconf.client.addons.properties;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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
import com.baidu.disconf.client.common.model.DisconfCenterFile;
import com.baidu.disconf.client.config.DisClientConfig;

/**
 * A properties factory bean that creates a reconfigurable Properties object.
 * When the Properties' reloadConfiguration method is called, and the file has
 * changed, the properties are read again from the file.
 * <p/>
 * 真正的 reload bean 定义，它可以定义多个 resource 为 reload config file
 */
public class ReloadablePropertiesFactoryBean extends PropertiesFactoryBean implements DisposableBean,
        ApplicationContextAware {

    private static ApplicationContext applicationContext;

    protected static final Logger log = LoggerFactory.getLogger(ReloadablePropertiesFactoryBean.class);

    private Resource[] locations;
    private long[] lastModified;
    private List<IReloadablePropertiesListener> preListeners;

    /**
     * 定义资源文件
     *
     * @param fileNames
     */
    public void setLocation(final String fileNames) {
        List<String> list = new ArrayList<String>();
        list.add(fileNames);
        setLocations(list);
    }

    
    /**
     * 处理规则：
     * 如果用户有指定的配置文件，则读取配置文件，如果没有指定
     * 则读取 远程全部公共配置文件和本地文件
     * @param fileNames
     */
    private Map<String,List<String>> fileNameAnalysis(List<String> fileNames){
    	
    	
    	Map<String,List<String>> fileList = new HashMap<String, List<String>>();
    		
		if(fileNames.get(0).equals("classpath:/*.properties")){
			fileList = DisconfMgr.getInstance().loadDefultFileList();
		}else{
    		fileList.put("local", fileNames);
    		
    	}
    	
    	return fileList;
    }
    
    
    /**
     * 新的setLocations支持通配符，获取该环境的全部配置文件
     * @param fileNames
     */
    public void setLocations(List<String> fileNames) {
    	
    	Map<String,List<String>> fileList = fileNameAnalysis(fileNames);

        List<Resource> resources = new ArrayList<Resource>();
        
        Boolean isPublicFile = false;
        for(String str: fileList.keySet()){
        	
        	if(str.equals("local")){
        		isPublicFile = false;
        	}else{
        		isPublicFile = true;
        	}
        	String app = isPublicFile ? DisconfCenterFile.COMMONPATH:DisconfCenterFile.LOCALPATH;
 
        	List<String> files = fileList.get(str);
        	
            for (String filename : files) {

                // trim
                filename = filename.trim();

                String realFileName = getFileName(filename);

                //
                // register to disconf
                //
                DisconfMgr.getInstance().reloadableScan(realFileName,isPublicFile);

                //
                // only properties will reload
                //
                String ext = FilenameUtils.getExtension(filename);
                if (ext.equals("properties")) {

                    PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver =
                            new PathMatchingResourcePatternResolver();
                    try {
 
                        Resource[] resourceList = pathMatchingResourcePatternResolver.getResources(app+filename);
                        for (Resource resource : resourceList) {
                            resources.add(resource);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        	
        }

        this.locations = resources.toArray(new Resource[resources.size()]);
        lastModified = new long[locations.length];
        super.setLocations(locations);
    }
 

    /**
     * get file name from resource
     *
     * @param fileName
     *
     * @return
     */
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
     * listener , 用于通知回调
     *
     * @param listeners
     */
    public void setListeners(final List listeners) {
        // early type check, and avoid aliassing
        this.preListeners = new ArrayList<IReloadablePropertiesListener>();
        for (Object o : listeners) {
            preListeners.add((IReloadablePropertiesListener) o);
        }
    }

    private ReloadablePropertiesBase reloadableProperties;

    /**
     * @return
     *
     * @throws IOException
     */
    @Override
    protected Properties createProperties() throws IOException {

        return (Properties) createMyInstance();
    }

    /**
     * createInstance 废弃了
     *
     * @throws IOException
     */
    protected Object createMyInstance() throws IOException {
        // would like to uninherit from AbstractFactoryBean (but it's final!)
        if (!isSingleton()) {
            throw new RuntimeException("ReloadablePropertiesFactoryBean only works as singleton");
        }

        // set listener
        reloadableProperties = new ReloadablePropertiesImpl();
        if (preListeners != null) {
            reloadableProperties.setListeners(preListeners);
        }

        // reload
        reload(true);

        // add for monitor
        ReloadConfigurationMonitor.addReconfigurableBean((ReconfigurableBean) reloadableProperties);

        return reloadableProperties;
    }

    public void destroy() throws Exception {
        reloadableProperties = null;
    }

    /**
     * 根据修改时间来判定是否reload
     *
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
                // may be spring boot
                log.warn(e.toString());
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
     * 设置新的值
     *
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
     * 回调自己
     */
    class ReloadablePropertiesImpl extends ReloadablePropertiesBase implements ReconfigurableBean {

        // reload myself
        public void reloadConfiguration() throws Exception {
            ReloadablePropertiesFactoryBean.this.reload(false);
        }
    }

}
