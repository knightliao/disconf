/**
 * beidou-core-493#com.baidu.beidou.common.annotation.Column.java
 * 下午1:53:40 created by Darwin(Tianxin)
 */
package com.baidu.ub.common.generic.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 标记其对应的数据库字段信息的annotation
 * 
 * @author Darwin(Tianxin)
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    /**
     * 字段名
     * 
     * @return
     */
    String value();

    /**
     * 是否需要update
     * 
     * @return 上午10:15:15 created by Darwin(Tianxin)
     */
    boolean maybeModified() default true;
}
