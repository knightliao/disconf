package com.baidu.disconf.client.utils;

import org.springframework.aop.framework.Advised;

/**
 * @author liaoqiqi
 * @version 2014-6-17
 */
public class MyBeanUtils {

    /**
     * 获取经过CGLIB包装过的原始target bean
     *
     * @param proxy
     * @param targetClass
     *
     * @return
     *
     * @throws Exception
     */
    @SuppressWarnings({"unchecked"})
    public static <T> T getTargetObject(Object proxy, Class<T> targetClass) throws Exception {

        return (T) ((Advised) proxy).getTargetSource().getTarget();
    }
}
