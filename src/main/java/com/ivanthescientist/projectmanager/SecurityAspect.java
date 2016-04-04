package com.ivanthescientist.projectmanager;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SecurityAspect {
    Logger logger = LoggerFactory.getLogger(SecurityAspect.class);

    @Around("@annotation(com.ivanthescientist.projectmanager.infrastructure.security.DomainSecurity) && execution(public * *(..))")
    public Object anyPublicMethodAction(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return proceedingJoinPoint.proceed();
    }
}
