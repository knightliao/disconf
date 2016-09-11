package com.baidu.disconf.client.common.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分布式的配置文件
 *
 * @author liaoqiqi
 * @version 2014-5-16
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DisconfFile {

    /**
     * 配置文件名,必须指定
     */
    String filename();

    /**
     * 环境,默认为用户指定的环境
     */
    String env() default "";

    /**
     * 版本,默认为用户指定的版本
     */
    String version() default "";

    /**
     * 版本,默认为用户指定的app
     */
    String app() default "";

    /**
     * 配置文件目标地址dir, 以"/"开头则是系统的全路径，否则则是相对于classpath的路径，默认是classpath根路径
     * 注意：根路径要注意是否有权限，否则会出现找不到路径，推荐采用相对路径
     *
     * @return
     */
    String targetDirPath() default "";
}
