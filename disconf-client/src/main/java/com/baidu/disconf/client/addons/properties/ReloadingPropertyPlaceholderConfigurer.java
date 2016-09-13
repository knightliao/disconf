package com.baidu.disconf.client.addons.properties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionVisitor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringValueResolver;

/**
 * 具有 reloadable 的 property bean
 * 扩展了 DefaultPropertyPlaceholderConfigurer
 * 特性如下：
 * 1. 启动时 监控 动态config，并维护它们与相应bean的关系
 * 2. 当动态config变动时，此configurer会进行reload
 * 3. reload 时会 compare config value, and set value for beans
 */
public class ReloadingPropertyPlaceholderConfigurer extends DefaultPropertyPlaceholderConfigurer implements
        InitializingBean, DisposableBean, IReloadablePropertiesListener, ApplicationContextAware {

    protected static final Logger logger = LoggerFactory.getLogger(ReloadingPropertyPlaceholderConfigurer.class);

    // 默认的 property 标识符
    private String placeholderPrefix = DEFAULT_PLACEHOLDER_PREFIX;

    private String placeholderSuffix = DEFAULT_PLACEHOLDER_SUFFIX;

    private String beanName;

    private BeanFactory beanFactory;
    private Properties[] propertiesArray;

    /**
     * 对于被标记为动态的，进行 构造 property dependency
     * 非动态的，则由原来的spring进行处理
     *
     * @param strVal
     * @param props
     * @param visitedPlaceholders
     *
     * @return
     *
     * @throws BeanDefinitionStoreException
     */
    protected String parseStringValue(String strVal, Properties props, Set visitedPlaceholders)
            throws BeanDefinitionStoreException {

        DynamicProperty dynamic = null;

        // replace reloading prefix and suffix by "normal" prefix and suffix.
        // remember all the "dynamic" placeholders encountered.
        StringBuffer buf = new StringBuffer(strVal);
        int startIndex = strVal.indexOf(this.placeholderPrefix);
        while (startIndex != -1) {
            int endIndex = buf.toString().indexOf(this.placeholderSuffix, startIndex + this.placeholderPrefix.length());
            if (endIndex != -1) {
                if (currentBeanName != null && currentPropertyName != null) {
                    String placeholder = buf.substring(startIndex + this.placeholderPrefix.length(), endIndex);
                    placeholder = getPlaceholder(placeholder);
                    if (dynamic == null) {
                        dynamic = getDynamic(currentBeanName, currentPropertyName, strVal);
                    }
                    addDependency(dynamic, placeholder);
                } else {
                    logger.debug("dynamic property outside bean property value - ignored: " + strVal);
                }
                startIndex = endIndex - this.placeholderPrefix.length() + this.placeholderPrefix.length() +
                        this.placeholderSuffix.length();
                startIndex = strVal.indexOf(this.placeholderPrefix, startIndex);
            } else {
                startIndex = -1;
            }
        }
        // then, business as usual. no recursive reloading placeholders please.
        return super.parseStringValue(buf.toString(), props, visitedPlaceholders);
    }

    /**
     * @param currentBeanName     当前的bean name
     * @param currentPropertyName 当前它的属性
     * @param orgStrVal           原来的值
     *
     * @return
     */
    private DynamicProperty getDynamic(String currentBeanName, String currentPropertyName, String orgStrVal) {
        DynamicProperty dynamic = new DynamicProperty(currentBeanName, currentPropertyName, orgStrVal);
        DynamicProperty found = dynamicProperties.get(dynamic);
        if (found != null) {
            return found;
        }
        dynamicProperties.put(dynamic, dynamic);
        return dynamic;
    }

    private Properties lastMergedProperties;

    /**
     * merge property and record last merge
     *
     * @return
     *
     * @throws IOException
     */
    protected Properties mergeProperties() throws IOException {
        Properties properties = super.mergeProperties();
        this.lastMergedProperties = properties;
        return properties;
    }

    /**
     * 当配置更新时，被调用
     *
     * @param event
     */
    public void propertiesReloaded(PropertiesReloadedEvent event) {

        Properties oldProperties = lastMergedProperties;

        try {
            //
            Properties newProperties = mergeProperties();

            //
            // 获取哪些 dynamic property 被影响
            //
            Set<String> placeholders = placeholderToDynamics.keySet();
            Set<DynamicProperty> allDynamics = new HashSet<DynamicProperty>();
            for (String placeholder : placeholders) {
                String newValue = newProperties.getProperty(placeholder);
                String oldValue = oldProperties.getProperty(placeholder);
                if (newValue != null && !newValue.equals(oldValue) || newValue == null && oldValue != null) {
                    if (logger.isInfoEnabled()) {
                        logger.info("Property changed detected: " + placeholder +
                                (newValue != null ? "=" + newValue : " removed"));
                    }
                    List<DynamicProperty> affectedDynamics = placeholderToDynamics.get(placeholder);
                    allDynamics.addAll(affectedDynamics);
                }
            }

            //
            // 获取受影响的beans
            //
            Map<String, List<DynamicProperty>> dynamicsByBeanName = new HashMap<String, List<DynamicProperty>>();
            Map<String, Object> beanByBeanName = new HashMap<String, Object>();
            for (DynamicProperty dynamic : allDynamics) {
                String beanName = dynamic.getBeanName();
                List<DynamicProperty> l = dynamicsByBeanName.get(beanName);

                if (l == null) {
                    dynamicsByBeanName.put(beanName, (l = new ArrayList<DynamicProperty>()));
                    Object bean = null;
                    try {
                        bean = applicationContext.getBean(beanName);
                        beanByBeanName.put(beanName, bean);
                    } catch (BeansException e) {
                        // keep dynamicsByBeanName list, warn only once.
                        logger.error("Error obtaining bean " + beanName, e);
                    }

                    //
                    // say hello
                    //
                    try {
                        if (bean instanceof IReconfigurationAware) {
                            ((IReconfigurationAware) bean).beforeReconfiguration();  // hello!
                        }
                    } catch (Exception e) {
                        logger.error("Error calling beforeReconfiguration on " + beanName, e);
                    }
                }
                l.add(dynamic);
            }

            //
            // 处理受影响的bean
            //
            Collection<String> beanNames = dynamicsByBeanName.keySet();
            for (String beanName : beanNames) {
                Object bean = beanByBeanName.get(beanName);
                if (bean == null) // problems obtaining bean, earlier
                {
                    continue;
                }
                BeanWrapper beanWrapper = new BeanWrapperImpl(bean);

                // for all affected ...
                List<DynamicProperty> dynamics = dynamicsByBeanName.get(beanName);
                for (DynamicProperty dynamic : dynamics) {
                    String propertyName = dynamic.getPropertyName();
                    String unparsedValue = dynamic.getUnparsedValue();

                    // obtain an updated value, including dependencies
                    String newValue;
                    removeDynamic(dynamic);
                    currentBeanName = beanName;
                    currentPropertyName = propertyName;
                    try {
                        newValue = parseStringValue(unparsedValue, newProperties, new HashSet());
                    } finally {
                        currentBeanName = null;
                        currentPropertyName = null;
                    }
                    if (logger.isInfoEnabled()) {
                        logger.info("Updating property " + beanName + "." + propertyName + " to " + newValue);
                    }

                    // assign it to the bean
                    try {
                        beanWrapper.setPropertyValue(propertyName, newValue);
                    } catch (BeansException e) {
                        logger.error("Error setting property " + beanName + "." + propertyName + " to " + newValue, e);
                    }
                }
            }

            //
            // say goodbye.
            //
            for (String beanName : beanNames) {
                Object bean = beanByBeanName.get(beanName);
                try {

                    if (bean instanceof IReconfigurationAware) {
                        ((IReconfigurationAware) bean).afterReconfiguration();
                    }
                } catch (Exception e) {
                    logger.error("Error calling afterReconfiguration on " + beanName, e);
                }
            }

        } catch (IOException e) {
            logger.error("Error trying to reload net.unicon.iamlabs.spring.properties.example.net.unicon.iamlabs" +
                    ".spring" + ".properties: " + e.getMessage(), e);
        }
    }

    /**
     *
     */
    static class DynamicProperty {
        final String beanName;
        final String propertyName;
        final String unparsedValue;
        List<String> placeholders = new ArrayList<String>();

        /**
         * @param beanName
         * @param propertyName
         * @param unparsedValue
         */
        public DynamicProperty(String beanName, String propertyName, String unparsedValue) {
            this.beanName = beanName;
            this.propertyName = propertyName;
            this.unparsedValue = unparsedValue;
        }

        public void addPlaceholder(String placeholder) {
            placeholders.add(placeholder);
        }

        public String getUnparsedValue() {
            return unparsedValue;
        }

        public String getBeanName() {
            return beanName;
        }

        public String getPropertyName() {
            return propertyName;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            final DynamicProperty that = (DynamicProperty) o;

            if (beanName != null ? !beanName.equals(that.beanName) : that.beanName != null) {
                return false;
            }
            if (propertyName != null ? !propertyName.equals(that.propertyName) : that.propertyName != null) {
                return false;
            }

            return true;
        }

        public int hashCode() {
            int result;
            result = (beanName != null ? beanName.hashCode() : 0);
            result = 29 * result + (propertyName != null ? propertyName.hashCode() : 0);
            return result;
        }
    }

    private Map<DynamicProperty, DynamicProperty> dynamicProperties = new HashMap<DynamicProperty, DynamicProperty>();
    private Map<String, List<DynamicProperty>> placeholderToDynamics = new HashMap<String, List<DynamicProperty>>();

    /**
     * 建立 placeholder 与 dynamic 的对应关系
     *
     * @param dynamic
     * @param placeholder
     */
    private void addDependency(DynamicProperty dynamic, String placeholder) {
        List<DynamicProperty> l = placeholderToDynamics.get(placeholder);
        if (l == null) {
            l = new ArrayList<DynamicProperty>();
            placeholderToDynamics.put(placeholder, l);
        }
        if (!l.contains(dynamic)) {
            l.add(dynamic);
        }
        dynamic.addPlaceholder(placeholder);
    }

    /**
     * 删除 placeholder 与 dynamic 的对应关系
     *
     * @param dynamic
     */
    private void removeDynamic(DynamicProperty dynamic) {
        List<String> placeholders = dynamic.placeholders;
        for (String placeholder : placeholders) {
            List<DynamicProperty> l = placeholderToDynamics.get(placeholder);
            l.remove(dynamic);
        }
        dynamic.placeholders.clear();
        dynamicProperties.remove(dynamic);
    }

    private String currentBeanName;
    private String currentPropertyName;

    /**
     * copy & paste, just so we can insert our own visitor.
     * 启动时 进行配置的解析
     */
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
            throws BeansException {

        BeanDefinitionVisitor visitor =
                new ReloadingPropertyPlaceholderConfigurer.PlaceholderResolvingBeanDefinitionVisitor(props);
        String[] beanNames = beanFactoryToProcess.getBeanDefinitionNames();
        for (int i = 0; i < beanNames.length; i++) {
            // Check that we're not parsing our own bean definition,
            // to avoid failing on unresolvable placeholders in net.unicon.iamlabs.spring.properties.example.net
            // .unicon.iamlabs.spring.properties file locations.
            if (!(beanNames[i].equals(this.beanName) && beanFactoryToProcess.equals(this.beanFactory))) {
                this.currentBeanName = beanNames[i];
                try {
                    BeanDefinition bd = beanFactoryToProcess.getBeanDefinition(beanNames[i]);
                    try {
                        visitor.visitBeanDefinition(bd);
                    } catch (BeanDefinitionStoreException ex) {
                        throw new BeanDefinitionStoreException(bd.getResourceDescription(), beanNames[i],
                                ex.getMessage());
                    }
                } finally {
                    currentBeanName = null;
                }
            }
        }

        StringValueResolver stringValueResolver = new PlaceholderResolvingStringValueResolver(props);

        // New in Spring 2.5: resolve placeholders in alias target names and aliases as well.
        beanFactoryToProcess.resolveAliases(stringValueResolver);

        // New in Spring 3.0: resolve placeholders in embedded values such as annotation attributes.
        beanFactoryToProcess.addEmbeddedValueResolver(stringValueResolver);
    }

    /**
     * afterPropertiesSet
     * 将自己 添加 property listener
     */
    public void afterPropertiesSet() {
        for (Properties properties : propertiesArray) {
            if (properties instanceof ReloadableProperties) {
                logger.debug("add property listener: " + properties.toString());
                ((ReloadableProperties) properties).addReloadablePropertiesListener(this);
            }
        }
    }

    /**
     * destroy
     * 删除 property listener
     *
     * @throws Exception
     */
    public void destroy() throws Exception {
        for (Properties properties : propertiesArray) {
            if (properties instanceof ReloadableProperties) {
                logger.debug("remove property listener: " + properties.toString());
                ((ReloadableProperties) properties).removeReloadablePropertiesListener(this);
            }
        }
    }

    /**
     * 替换掉spring的 config resolver，这样我们才可以解析掉自己的config
     */
    private class PlaceholderResolvingBeanDefinitionVisitor extends BeanDefinitionVisitor {

        private final Properties props;

        public PlaceholderResolvingBeanDefinitionVisitor(Properties props) {
            this.props = props;
        }

        protected void visitPropertyValues(MutablePropertyValues pvs) {
            PropertyValue[] pvArray = pvs.getPropertyValues();
            for (PropertyValue pv : pvArray) {
                currentPropertyName = pv.getName();
                try {
                    Object newVal = resolveValue(pv.getValue());
                    if (!ObjectUtils.nullSafeEquals(newVal, pv.getValue())) {
                        pvs.addPropertyValue(pv.getName(), newVal);
                    }
                } finally {
                    currentPropertyName = null;
                }
            }
        }

        protected String resolveStringValue(String strVal) throws BeansException {
            return parseStringValue(strVal, this.props, new HashSet());
        }
    }

    /**
     *
     */
    protected class PlaceholderResolvingStringValueResolver implements StringValueResolver {

        private final Properties props;

        public PlaceholderResolvingStringValueResolver(Properties props) {
            this.props = props;
        }

        @Override
        public String resolveStringValue(String strVal) throws BeansException {
            return parseStringValue(strVal, this.props, new HashSet());
        }
    }

    /**
     * the application context is needed to find the beans again during reconfiguration
     */
    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void setProperties(Properties properties) {
        setPropertiesArray(new Properties[] {properties});
    }

    public void setPropertiesArray(Properties[] propertiesArray) {
        this.propertiesArray = propertiesArray;
        super.setPropertiesArray(propertiesArray);
    }

    public void setPlaceholderPrefix(String placeholderPrefix) {
        this.placeholderPrefix = placeholderPrefix;
        super.setPlaceholderPrefix(placeholderPrefix);
    }

    public void setPlaceholderSuffix(String placeholderSuffix) {
        this.placeholderSuffix = placeholderSuffix;
        super.setPlaceholderSuffix(placeholderPrefix);
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
        super.setBeanName(beanName);
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        super.setBeanFactory(beanFactory);
    }
}
