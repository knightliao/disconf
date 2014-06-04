package com.baidu.disconf2.common.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识配置更新时需要进行更新的服务,需要指定它影响的配置数据，<br/>
 * 包括了配置文件和配置项
 * 
 * @author liaoqiqi
 * @version 2014-5-16
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DisconfUpdateService {

    String[] keys() default "";
}
