package dev.shermende.support.spring.aop.intercept;

import dev.shermende.support.spring.aop.intercept.annotation.InterceptResult;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
@Aspect
public class InterceptResultAspect implements InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(InterceptResultAspect.class);

    @Autowired
    private BeanFactory beanFactory;

    @AfterReturning(pointcut = "@annotation(dev.shermende.support.spring.aop.intercept.annotation.InterceptResult) && execution(public * *(..))", returning = "result")
    public void interceptResult(
        JoinPoint joinPoint,
        Object result
    ) {
        beanFactory.getBean(getAnnotation(joinPoint).value()).doIntercept(result);
    }

    private InterceptResult getAnnotation(
        JoinPoint joinPoint
    ) {
        return ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(InterceptResult.class);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.warn("Attention!!! @dev.shermende.support.spring.aop.intercept.annotation.InterceptResult annotation enabled");
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

}