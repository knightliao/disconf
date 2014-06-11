package com.baidu.disconf2.client.core;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class DisconfAspectJ {

    @Pointcut(value = "execution(public * *(..,@com.baidu.dsp.common.annotation.PartFormValidate (*)))")
    public void anyPublicMethodWithParaAnnotation() {
    }

    @Around("anyPublicMethodWithParaAnnotation()")
    public Object modifyFormValidate(ProceedingJoinPoint jp) throws Throwable {

        return null;
    }
}
