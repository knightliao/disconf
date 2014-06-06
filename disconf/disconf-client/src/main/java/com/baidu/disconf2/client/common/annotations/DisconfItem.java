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
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DisconfItem {

    /**
     * 配置项的KEY
     * 
     * @return
     */
    String key() default "";;

    /**
     * 配置项的Value
     * 
     * @return
     */
    String defaultValue() default "";

    /**
     * 环境
     * 
     * @return
     */
    String env() default "";

    /**
     * 版本
     * 
     * @return
     */
    String version() default "";
}
