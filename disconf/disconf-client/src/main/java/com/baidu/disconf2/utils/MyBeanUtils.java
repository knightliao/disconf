package com.baidu.disconf2.utils;

import org.springframework.aop.framework.Advised;
import org.springframework.stereotype.Service;

/**
 * 
 * @author liaoqiqi
 * @version 2014-6-17
 */
public class MyBeanUtils {

    /**
     * 获取Spring service bean name,如果是非Spring，则返回null
     * 
     * @return
     */
    public static String getSpringServiceName(Class<?> disconfUpdateServiceClass) {

        Service serviceAnnotation = disconfUpdateServiceClass
                .getAnnotation(Service.class);
        if (serviceAnnotation == null) {
            return null;
        }

        String name = serviceAnnotation.value();
        if (name.isEmpty()) {
            name = disconfUpdateServiceClass.getSimpleName();
            name = name.substring(0, 1).toLowerCase() + name.substring(1);
        }

        return name;
    }

    /**
     * 
     * @param proxy
     * @param targetClass
     * @return
     * @throws Exception
     */
    @SuppressWarnings({ "unchecked" })
    public static <T> T getTargetObject(Object proxy, Class<T> targetClass)
            throws Exception {

        return (T) ((Advised) proxy).getTargetSource().getTarget();
    }
}
