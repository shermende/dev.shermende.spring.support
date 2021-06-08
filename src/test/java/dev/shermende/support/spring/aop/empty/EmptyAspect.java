package dev.shermende.support.spring.aop.empty;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class EmptyAspect {

    @Around("@annotation(dev.shermende.support.spring.aop.empty.annotation.Empty)")
    public Object empty(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        final Object proceed = proceedingJoinPoint.proceed();
        return proceed;
    }

}