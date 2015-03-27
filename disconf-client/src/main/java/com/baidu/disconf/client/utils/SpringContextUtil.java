package com.baidu.disconf.client.utils;

import java.lang.reflect.Field;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * 获取spring容器，以访问容器中定义的其他bean
 */
@Service
public class SpringContextUtil implements ApplicationContextAware {

    // Spring应用上下文环境
    private static ApplicationContext applicationContext;

    /**
     * 实现ApplicationContextAware接口的回调方法，设置上下文环境
     */
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * @return
     *
     * @throws BeansException
     */
    public static Object getBean(Class<?> cls) throws BeansException {
        return applicationContext.getBean(cls);
    }

    /**
     * @return
     *
     * @throws BeansException
     */
    public static Object getBeansOfType(Class<?> type) throws BeansException {
        return applicationContext.getBeansOfType(type);
    }

    /**
     * @return
     *
     * @throws BeansException
     */
    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }

    /**
     * 获取使用 Proxy.newProxyInstance 生成的代理对象
     *
     * @param proxy
     * @param thisClass
     *
     * @return
     *
     * @throws Exception
     */
    @SuppressWarnings({"unchecked"})
    public static <T> T getProxyObject(Object proxy, Class<T> thisClass) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        return (T) h.get(proxy);
    }
}