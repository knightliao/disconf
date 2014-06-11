package com.baidu.disconf2.client.common.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分布式的配置项
 * 
 * @author liaoqiqi
 * @version 2014-5-16
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DisconfItem {

    /**
     * 配置项的KEY，不能为空
     * 
     * @return
     */
    String key();

    /**
     * 环境,,默认为用户指定的环境
     * 
     * @return
     */
    String env() default "";

    /**
     * 版本,默认为用户指定的版本
     * 
     * 
     * @return
     */
    String version() default "";
}
