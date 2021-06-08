package dev.shermende.support.spring.aop.profiling;

import dev.shermende.support.spring.jmx.JmxControl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;

/**
 *
 */
@Aspect
public class ProfilingAspect implements InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(ProfilingAspect.class);

    @Autowired
    private JmxControl jmxControl;

    @Around("@annotation(dev.shermende.support.spring.aop.profiling.annotation.Profiling) && execution(public * *(..))")
    public Object profiling(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // do nothing if disabled
        if (!jmxControl.isEnabled()) return proceedingJoinPoint.proceed();
        // logging if enabled
        final long start = System.currentTimeMillis();
        final Object proceed = proceedingJoinPoint.proceed();
        final long delta = System.currentTimeMillis() - start;
        try {
            final Class<?> aClass = proceedingJoinPoint.getTarget().getClass();
            final MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
            final Method method = signature.getMethod();
            log.debug("[Profiling] [{}#{}] [Duration:{}]",
                aClass.getSimpleName(),
                method.getName(),
                delta
            );
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return proceed;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.warn("Attention!!! @dev.shermende.support.spring.aop.profiling.annotation.Profiling annotation enabled");
    }

    public void setJmxControl(JmxControl jmxControl) {
        this.jmxControl = jmxControl;
    }
}