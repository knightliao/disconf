package com.baidu.dsp.common.constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.baidu.dsp.common.constraint.validation.ChineseLengthValidator;

/**
 * 中文校验
 *
 * @author zhangbi
 * @date 2014年1月13日下午9:24:42
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ChineseLengthValidator.class})
public @interface ChineseLengthConstrant {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 支持的最大长度
     *
     * @return
     */
    long maxLength() default Long.MAX_VALUE;

    /**
     * 支持最小长度
     *
     * @return
     */
    long minLength() default 0;

    /**
     * 中文计长度的个数,默认为<code>2</code>
     *
     * @return
     */
    int cnHoldLength() default 2;
}
