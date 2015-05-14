package com.baidu.disconf.client.common.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识配置更新时需要进行更新的服务,需要指定它影响的配置数据，<br/>
 * 可以是配置文件或者是配置项
 *
 * @author liaoqiqi
 * @version 2014-5-16
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DisconfUpdateService {

    /**
     * 配置文件
     */
    Class<?>[] classes() default {};

    /**
     * 配置文件key名
     */
    String[] confFileKeys() default {};

    /**
     * 配置项
     */
    String[] itemKeys() default {};
}
