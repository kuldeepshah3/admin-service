package com.oracle.demo.handler;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Log4j2
public class LoggingHandler {

    @Before("within(com.oracle.demo.controller.*)")
    public void before(JoinPoint joinPoint) {
        log.info("Start - Method {}.{}()", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
    }

    @AfterReturning("within(com.oracle.demo.controller.*)")
    public void afterReturning(JoinPoint joinPoint) {
        log.info("End - Success - Method {}.{}()", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
    }

    @AfterThrowing("within(com.oracle.demo.controller.*)")
    public void afterThrowing(JoinPoint joinPoint) {
        log.info("End - Exception - Method {}.{}()", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
    }

}