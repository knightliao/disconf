package com.baidu.unbiz.common.genericdao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 配置Sequence的annotation
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Sequence {

    /**
     * sequence的名字
     *
     * @return 下午1:22:37 created by Darwin(Tianxin)
     */
    String name();

    /**
     * 该Sequence每次缓存多少个ID
     *
     * @return 下午1:22:49 created by Darwin(Tianxin)
     */
    int size() default 1000;
}
