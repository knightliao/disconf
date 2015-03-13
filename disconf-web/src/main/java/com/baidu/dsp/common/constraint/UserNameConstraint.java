package com.baidu.dsp.common.constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author liaoqiqi
 * @version 2014-1-14
 */
@NotNull
@Size(min = 3, max = 20)
@Pattern(regexp = "^[a-zA-Z0-9]+$", message = "username.error")
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
public @interface UserNameConstraint {

    String message() default "username.error";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
