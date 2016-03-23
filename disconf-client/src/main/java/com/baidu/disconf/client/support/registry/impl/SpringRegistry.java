package com.baidu.disconf.client.support.registry.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.baidu.disconf.client.support.registry.Registry;

/**
 * Created by knightliao on 15/11/26.
 */
public class SpringRegistry implements Registry, ApplicationContextAware {

    protected static final Logger LOGGER = LoggerFactory.getLogger(SpringRegistry.class);

    // Spring应用上下文环境
    private static ApplicationContext applicationContext;

    private SimpleRegistry simpleRegistry = new SimpleRegistry();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public <T> List<T> findByType(Class<T> type, boolean newInstance) {

        if (applicationContext == null) {
            LOGGER.error("Spring Context is null. Cannot autowire " + type.getCanonicalName());
            return new ArrayList<T>(0);
        }

        if (type == null) {
            return new ArrayList<T>(0);
        }

        Map<String, T> map = findByTypeWithName(type);
        if (map == null || map.isEmpty()) {
            if (newInstance) {
                LOGGER.debug("Not found from Spring IoC container for " + type.getSimpleName() + ", and try to init by "
                        + "calling newInstance.");
                return simpleRegistry.findByType(type, newInstance);
            }
        }

        return new ArrayList<T>(map.values());
    }

    @Override
    public <T> T getFirstByType(Class<T> type, boolean newInstance) {
        List<T> list = this.findByType(type, newInstance);
        if (list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public <T> T getFirstByType(Class<T> type, boolean newInstance, boolean withProxy) {

        T object = getFirstByType(type, newInstance);

        if (!withProxy) {
            return object;
        }

        try {
            return getTargetObject(object, type);
        } catch (Exception e) {
            LOGGER.warn(e.toString());
            return object;
        }
    }

    /**
     * 调用Spring工具类获取bean
     *
     * @param type 类类型
     *
     * @return 容器托管的bean字典
     */
    public <T> Map<String, T> findByTypeWithName(Class<T> type) {
        return applicationContext.getBeansOfType(type);
    }

    protected <T> T getTargetObject(Object proxy, Class<T> targetClass) throws Exception {
        if (AopUtils.isJdkDynamicProxy(proxy)) {
            return (T) ((Advised) proxy).getTargetSource().getTarget();
        } else if (AopUtils.isCglibProxy(proxy)) {
            return (T) ((Advised) proxy).getTargetSource().getTarget();
        } else {
            return (T) proxy;
        }
    }
}
