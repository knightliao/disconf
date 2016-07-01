package com.baidu.unbiz.common.genericdao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记其对应的数据库字段信息的annotation
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Column {

    /**
     * 字段名,
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

    boolean ignore() default false;
}
