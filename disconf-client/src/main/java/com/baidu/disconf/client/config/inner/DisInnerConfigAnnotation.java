package com.baidu.disconf.client.config.inner;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Disconf 自己的配置文件中的项标注
 *
 * @author liaoqiqi
 * @version 2014-6-16
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DisInnerConfigAnnotation {

    String name();

    String defaultValue() default "";
}
