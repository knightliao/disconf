package com.baidu.disconf2.common.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分布式的配置文件中的ITEM
 * 
 * @author liaoqiqi
 * @version 2014-5-16
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DisconfFileItem {

    /**
     * 配置文件中某Item的名字
     * 
     * @return
     */
    String key() default "";

    /**
     * Item项的值
     * 
     * @return
     */
    String defaultValue() default "";
}
