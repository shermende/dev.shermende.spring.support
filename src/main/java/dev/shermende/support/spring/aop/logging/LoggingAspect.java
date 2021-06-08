package dev.shermende.support.spring.aop.logging;

import dev.shermende.support.spring.jmx.JmxControl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
@Aspect
public class LoggingAspect implements InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Autowired
    private JmxControl jmxControl;

    @Around("@annotation(dev.shermende.support.spring.aop.logging.annotation.Logging) && execution(public * *(..))")
    public Object logging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // do nothing if disabled
        if (!jmxControl.isEnabled()) return proceedingJoinPoint.proceed();
        // logging if enabled
        try {
            log.debug("[Logging before] [{}#{}] [Args:{}]",
                proceedingJoinPoint.getTarget().getClass().getSimpleName(),
                ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod().getName(),
                proceedingJoinPoint.getArgs()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        final Object proceed = proceedingJoinPoint.proceed();
        try {
            log.debug("[Logging after] [{}#{}] [Args:{}] [Result:{}]",
                proceedingJoinPoint.getTarget().getClass().getSimpleName(),
                ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod().getName(),
                proceedingJoinPoint.getArgs(),
                proceed
            );
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return proceed;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.warn("Attention!!! @dev.shermende.support.spring.aop.logging.annotation.Logging annotation enabled");
    }

    public void setJmxControl(JmxControl jmxControl) {
        this.jmxControl = jmxControl;
    }

}