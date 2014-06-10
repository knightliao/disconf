/**
 * beidou-core#com.baidu.beidou.common.annotation.Sequence.java
 * 下午1:21:56 created by Darwin(Tianxin)
 */
package com.baidu.ub.common.generic.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 配置Sequence的annotation
 * 
 * @author Darwin(Tianxin)
 */
@Retention(RetentionPolicy.RUNTIME)
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
